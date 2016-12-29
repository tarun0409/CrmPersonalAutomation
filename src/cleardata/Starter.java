package cleardata;

import java.util.logging.Logger;

import org.json.JSONObject;

public class Starter {

	//private static final Logger LOGGER = Logger.getLogger( Starter.class.getName() );
	public static void main(String[] args) throws Exception {
		
		String[] modules = {"Leads","Accounts","Contacts","Potentials","Products","Campaigns","Tasks","Events","Calls","Notes"};
		JSONObject properties = new JSONObject();
		properties.put("port", "4040");
		properties.put("module", "Leads");
		properties.put("authtoken", "1cac6cd5f8aafed2708c208fe8edff9d");
		for(int i=0; i<modules.length; i++)
		{
			properties.put("module", modules[i]);
			Delete deleteObj = new Delete(properties);
			deleteObj.deleteAllData();
		}
	}

}
