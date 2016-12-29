package cleardata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class Get {
	static final Logger LOGGER = LogManager.getLogger(Get.class.getName());
	String authtoken;
	String port;
	public Get(JSONObject properties) throws Exception
	{
		this.authtoken = properties.getString("authtoken");
		this.port = properties.getString("port");
	}
	public ArrayList<String> getModuleList() throws Exception
	{
		String url = "http://localhost:"+port+"/crm/v2/settings/modules";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		request.addHeader("Authorization", "Zoho-authtoken "+authtoken);
		HttpResponse response = client.execute(request);
		LOGGER.debug("Response = "+response);
		int statusCode = response.getStatusLine().getStatusCode();
		ArrayList<String> moduleList = new ArrayList<String>();
		if(statusCode==200)
		{
			LOGGER.debug("Module data received successfully!!!!");
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			JSONObject resultObj = new JSONObject(result.toString());
			JSONArray modulesArray = resultObj.getJSONArray("modules");
			int length = modulesArray.length();
			for(int i=0; i<length; i++)
			{
				JSONObject module = modulesArray.getJSONObject(i);
				boolean editable = module.getBoolean("editable");
				boolean deletable = module.getBoolean("deletable");
				if(editable && deletable)
				{
					String moduleName = module.getString("api_name");
					moduleList.add(moduleName);
				}
			}
		}
		return moduleList;
	}
	public JSONObject getData(String module) throws Exception
	{
		LOGGER.debug("Getting all data from "+module+"\n\n\n");
		String url = "http://localhost:"+port+"/crm/v2/"+module;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		request.addHeader("Authorization", "Zoho-authtoken "+authtoken);
		HttpResponse response = client.execute(request);
		LOGGER.debug("Response = "+response);
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode==200)
		{
			LOGGER.debug("Data received successfully!!!!");
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
			LOGGER.error("Something went wrong while receiving data");
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
