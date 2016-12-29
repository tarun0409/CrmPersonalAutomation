package cleardata;

import java.util.ArrayList;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class Delete {
	static final Logger LOGGER = LogManager.getLogger(Get.class.getName());
	String authtoken;
	String port;
	JSONObject properties;
	public Delete(JSONObject properties) throws Exception
	{
		this.authtoken = properties.getString("authtoken");
		this.port = properties.getString("port");
		this.properties = properties;
	}
	public int deleteById(String module, String id) throws Exception
	{
		HttpClient client = HttpClientBuilder.create().build();
		String resourceUrl = "http://localhost:"+port+"/crm/v2/"+module+"/";
		resourceUrl = resourceUrl+(id.toString());
		HttpDelete request = new HttpDelete(resourceUrl);
		request.addHeader("Authorization", "Zoho-authtoken "+authtoken);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		return statusCode;
	}
	public int deleteDataByIds(String module, Long[] ids) throws Exception
	{
		int length = ids.length;
		int success = 0;
		for(int i=0; i<length; i++)
		{
			Long id = ids[i];
			int statusCode = this.deleteById(module, id.toString());
		    if(statusCode==200)
		    {
		    	LOGGER.debug("SUCCESS! Id "+id+" deleted!!! Module="+module);
		    	success++;
		    }
		    else
		    {
		    	LOGGER.debug("Something went wrong in deleting id "+id);
		    }
		}
		return success;
	}
	public void deleteSingleModuleData(String module) throws Exception
	{
		Get getObj = new Get(properties);
		JSONObject data = getObj.getData(module);
		Long[] ids = getObj.getIds(data);
		LOGGER.debug("No of records returned = "+ids.length);
		int noOfDeletes = this.deleteDataByIds(module, ids);
		System.out.println(noOfDeletes+" out of "+ids.length+" records deleted from module "+module);
		LOGGER.debug(noOfDeletes+" out of "+ids.length+" records deleted "+module);
	}
	public void deleteAllModuleData() throws Exception
	{
		Get getObj = new Get(properties);
		ArrayList<String> moduleList = getObj.getModuleList();
		for(String module : moduleList)
		{
			JSONObject data = getObj.getData(module);
			Long[] ids = getObj.getIds(data);
			LOGGER.debug("No of records returned = "+ids.length);
			int noOfDeletes = this.deleteDataByIds(module, ids);
			System.out.println(noOfDeletes+" out of "+ids.length+" records deleted "+module);
			LOGGER.debug(noOfDeletes+" out of "+ids.length+" records deleted "+module);
		}
	}
}
