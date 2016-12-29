package cleardata;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

public class Delete {
	private static final Logger LOGGER = Logger.getLogger( Delete.class.getName() );
	String module;
	String authtoken;
	String port;
	JSONObject properties;
	public Delete(JSONObject properties) throws Exception
	{
		this.module = properties.getString("module");
		this.authtoken = properties.getString("authtoken");
		this.port = properties.getString("port");
		this.properties = properties;
	}
	public int deleteById(String id) throws Exception
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
	public int deleteDataByIds(Long[] ids) throws Exception
	{
		int length = ids.length;
		int success = 0;
		for(int i=0; i<length; i++)
		{
			Long id = ids[i];
			int statusCode = this.deleteById(id.toString());
			System.out.println("Status Code "+statusCode);
		    if(statusCode==200)
		    {
		    	System.out.println("SUCCESS! Id "+id+" deleted!!! Module="+module);
		    	LOGGER.log(Level.FINE, "SUCCESS! Id "+id+" deleted!!! Module="+module);
		    	success++;
		    }
		    else
		    {
		    	System.out.println("Something went wrong in deleting id "+id);
		    	LOGGER.log(Level.FINE, "Something went wrong in deleting id "+id);
		    }
		}
		return success;
	}
	public void deleteAllData() throws Exception
	{
		Get getObj = new Get(properties);
		JSONObject data = getObj.getData();
		Long[] ids = getObj.getIds(data);
		System.out.println("No of records returned = "+ids.length);
		LOGGER.log(Level.FINE, "No of records returned = "+ids.length);
		int noOfDeletes = this.deleteDataByIds(ids);
		System.out.println(noOfDeletes+" out of "+ids.length+" records deleted!!");
		LOGGER.log(Level.FINE,noOfDeletes+" out of "+ids.length+" records deleted!!");
	}
}
