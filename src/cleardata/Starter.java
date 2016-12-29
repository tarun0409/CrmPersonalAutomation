package cleardata;



import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class Starter {
	

	static final Logger LOGGER = LogManager.getLogger(Starter.class.getName());
	public static void main(String[] args) throws Exception {
		JSONObject properties = new JSONObject();
		properties.put("port", "4040");
		properties.put("authtoken", "1cac6cd5f8aafed2708c208fe8edff9d");
		Delete deleteObj = new Delete(properties);
		deleteObj.deleteAllModuleData();
	}

}
