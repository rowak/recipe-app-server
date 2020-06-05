package io.github.rowak.recipesappserver.net;

import java.util.Map;

import org.json.JSONObject;

public class Request
{
	private RequestType reqType;
	private JSONObject data;
	
	public Request(RequestType reqType, String data)
	{
		this.reqType = reqType;
		this.data = new JSONObject(data);
	}
	
	public Request(Map<String, String> parms)
	{
		this.reqType = getRequestType(parms.containsKey("type") ?
				parms.get("type") : "");
		this.data = new JSONObject(parms);
	}
	
	public static Request fromJSON(String json)
	{
		Request req = new Request(null);
		JSONObject obj = new JSONObject(json);
		req.reqType = getRequestType(obj.has("type") ?
				obj.getString("type") : "");
		req.data = new JSONObject(json);
		return req;
	}
	
	public static Request categories()
	{
		return new Request(RequestType.CATEGORIES, null);
	}
	
	public static Request tags()
	{
		return new Request(RequestType.RECIPE_HEADERS, null);
	}
	
	public static Request series()
	{
		return new Request(RequestType.RECIPE, null);
	}
	
	public JSONObject toJSON()
	{
		return data;
	}
	
	public RequestType getType()
	{
		return reqType;
	}
	
	public JSONObject getData()
	{
		return data;
	}
	
	public void setData(JSONObject data)
	{
		this.data = data;
	}
	
	private static RequestType getRequestType(String requestStr)
	{
		for (RequestType type : RequestType.values())
		{
			if (type.toString().equals(requestStr))
			{
				return type;
			}
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		return toJSON().toString();
	}
}
