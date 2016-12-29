package cleardata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class Get {
	private static final Logger LOGGER = Logger.getLogger( Get.class.getName() );
	String module;
	String authtoken;
	String port;
	JSONObject properties;
	public Get(JSONObject properties) throws Exception
	{
		this.module = properties.getString("module");
		this.authtoken = properties.getString("authtoken");
		this.port = properties.getString("port");
		this.properties = properties;
	}
	public JSONObject getData() throws Exception
	{
		System.out.println("\n\n\n************Getting all data from "+module+"\n\n\n");
		LOGGER.log(Level.FINE,"\n\n\n************Getting all data from "+module+"\n\n\n");
		String url = "http://localhost:"+port+"/crm/v2/"+module;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("Authorization", "Zoho-authtoken "+authtoken);
		HttpResponse response = client.execute(request);
		System.out.println("Response = "+response);
		LOGGER.log(Level.FINE, "Response = "+response);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode==200)
		{
			System.out.println("Data recived successfully!!!!");
			LOGGER.log(Level.FINE,"Data recived successfully!!!!");
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			JSONObject resultObj = new JSONObject(result.toString());
			return resultObj;
		}
		else
		{
			System.out.println("Something went wrong while receiving data");
			LOGGER.log(Level.FINE, "Something went wrong while receiving data");
		}
		JSONObject resultObj = new JSONObject();
		resultObj.put("data", new JSONArray());
		return resultObj;
	}
	public Long[] getIds(JSONObject dataObj) throws Exception
	{
		JSONArray data = (JSONArray)dataObj.get("data");
		int length = data.length();
		Long[] ids = new Long[length];
		for(int i=0; i<length; i++)
		{
			JSONObject obj = data.getJSONObject(i);
			Long id = obj.getLong("id");
			ids[i]=id;
		}
		return ids;
	}
}
