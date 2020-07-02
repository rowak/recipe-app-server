package io.github.rowak.recipesappserver.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import io.github.rowak.recipesappserver.tools.ConsoleOutput;
import io.github.rowak.recipesappserver.tools.Version;

public class Server extends NanoHTTPD {
	public static final Version VERSION = new Version("0.0.1");
	public static final Version[] SUPPORTED_VERSIONS = {new Version("0.0.1")};
	private final String HTML_PATH = "index.html";
	
	private String html;
	private RequestHandler requestHandler = new RequestHandler();
	
	public Server(int port) throws IOException {
		super(port);
		html = getHtml();
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		System.out.println(ConsoleOutput.format("Server started on port " + port + "."));
	}
	
	@Override
    public Response serve(IHTTPSession session) {
		Request req = sessionToRequest(session);
		if (req != null) {
			Response quickResponse = getQuickResponse(req);
			if (quickResponse != null) {
				return quickResponse;
			}
			io.github.rowak.recipesappserver.net.Response resp =
					requestHandler.getResponse(req);
			ResponseType type = resp.getType();
			if (type == ResponseType.INVALID_REQUEST) {
				/* Request not fulfilled */
				return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,
						"application/json", resp.toString().replace("\\/", "/"));
			}
			else {
				/* Request fulfilled */
				Response r = newFixedLengthResponse(NanoHTTPD.Response.Status.OK,
						"application/json", resp.toString().replace("\\/", "/"));
				r.addHeader("Access-Control-Allow-Origin", "*");
				r.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
				r.addHeader("Access-Control-Allow-Credentials", "true");
				r.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
				return r;
			}
		}
		return newFixedLengthResponse(html);
    }
	
	/*
	 * Converts a NanoHTTPD session object into a request object
	 * by parsing the HTTP parameters.
	 */
	private Request sessionToRequest(IHTTPSession session) {
		Map<String, String> parms = session.getParms();
		String uri = session.getUri();
		String requestTypeParm = uri.equals("/") ? null : uri.substring(1);
		if (requestTypeParm != null) {
			/* Build a request object (request type + data) */
			RequestType reqType = getRequestType(requestTypeParm);
			JSONObject data = new JSONObject(parms);
			if (!data.has("type")) {
				data.put("type", requestTypeParm);
			}
			Request req = new Request(reqType, data.toString());
			req.setData(data);
			return req;
		}
		return null;
	}
	
	/*
	 * Converts a request type string into its RequestType equivalent.
	 */
	private static RequestType getRequestType(String requestStr) {
		for (RequestType type : RequestType.values()) {
			if (type.toString().equals(requestStr.toUpperCase())) {
				return type;
			}
		}
		return null;
	}
	
	/*
	 * Checks if the request can be processed more quickly if
	 * it matches certain special cases. If so, returns the
	 * NanoHTTPD response. If not, returns null.
	 */
	private Response getQuickResponse(Request req) {
		if (req == null || req.getType() == null) {
			/* Request is invalid; no type given */
			return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,
					"application/json", io.github.rowak.recipesappserver
					.net.Response.INVALID_REQUEST.toString());
		}
		if (req.getType() == RequestType.VERSION)
		{
			/* Avoid version check below, no version parameter required */
			return newFixedLengthResponse(NanoHTTPD.Response.Status.OK,
					"application/json", io.github.rowak.recipesappserver
					.net.Response.VERSION.toString());
		}
		else if (req.getType() == RequestType.FILE)
		{
			final String ROOT = "src/main/";
			String url = req.getData().has("fileName") ?
					ROOT + req.getData().getString("fileName") : null;
			if (url != null && !url.contains("/../"));
			{
				File file = new File(url);
				if (file.exists())
				{
					String extensionLong = url.substring(url.length()-5);
					String extension = extensionLong.split("\\.")[1];
					String mimeType = "text/" + extension;
					return newFixedLengthResponse(NanoHTTPD.Response.Status.OK,
							mimeType, readFileToString(file));
				}
			}
			return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND,
					"application/json", io.github.rowak.recipesappserver
					.net.Response.RESOURCE_NOT_FOUND.toString());
		}
//		/*
//		 * Safety check: if client reports a version that does not
//		 * match server, then the request is rejected, since the
//		 * client may be expecting the response data in a
//		 * different format
//		 */
//		if (!checkVersion(req)) {
//			return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,
//					"application/json", io.github.rowak.recipesappserver
//						.net.Response.OUTDATED_CLIENT.toString());
//		}
		return null;
	}
	
	private String readFileToString(File file)
	{
		try
		{
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line);
			}
			reader.close();
			return sb.toString();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Checks if the client version (contained in the request) is greater or
	 * equal to the server version. If the client version is less than the
	 * server version, or not in the supported versions array, then the
	 * client is considered outdated. This way, both the client and server
	 * can be more easily updated without the needing to be update the other.
	 * 
	 * Side Note: if the server is updated and it WON'T affect the client, then
	 * the version doesn't matter. But if the update will affect the client
	 * then the server version should be set to one higher than the latest
	 * client version.
	 */
	private boolean checkVersion(Request request) {
		Version clientVersion = new Version(request.getData().has("version") ?
				request.getData().getString("version") : null);
		for (Version version : SUPPORTED_VERSIONS) {
			if (clientVersion.equals(version)) {
				return true;
			}
		}
		return clientVersion.greater(VERSION) || clientVersion.equals(VERSION);
	}
	
	/*
	 * Returns the contents of the "index.html" file as a string.
	 * If the file does not exist then returns null instead.
	 */
	private String getHtml() {
		StringBuilder html = new StringBuilder();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(HTML_PATH));
			String line = "";
			while ((line = reader.readLine()) != null) {
				html.append(line);
			}
			reader.close();
		}
		catch (IOException ioe) {
			return "<html>The " + HTML_PATH + " file could not be located.</html>";
		}
		return html.toString();
	}
}
