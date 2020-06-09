package io.github.rowak.recipesappserver.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgurImage {
	private static final String BASE_URL = "https://api.imgur.com/3/image/";
	private static final String FILE_TYPE = "jpg";
	
	public byte[] getImageData(String imageHash) throws IOException {
		URL url = null;
		try {
			url = getUrl(imageHash);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException();
		}
		int read;
		byte[] data = new byte[16384];
		InputStream is = url.openStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((read = is.read(data)) != -1) {
			out.write(data, 0, read);
		}
		is.close();
		return out.toByteArray();
	}
	
	private URL getUrl(String imageHash) throws MalformedURLException {
		return new URL(BASE_URL + imageHash + "." + FILE_TYPE);
	}
}
