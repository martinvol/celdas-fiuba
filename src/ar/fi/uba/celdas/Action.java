package ar.fi.uba.celdas;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

public class Action {

	String action_taken; 
	
	int tried  = 1; 
	int ok = 0;
	int got_closer_to_the_target  = 0; 
	int number_of_enemies = 0; //times killed is implicit
	int gano = 0;
	int is_repeating = 0;
	
	public String name;
	
	Action (String name){
		this.name = name;
	}
	
	public float getUtility(String next_path) {
		float floatTried = ((float) tried);
		
		float survive_rate = ((float) ok)/floatTried;
		float closerIndex = ((float) got_closer_to_the_target)/floatTried;
		int dies = tried - ok;
		
		int will_get_closer = 0; 
		
		if (next_path.equals(name)) {
			will_get_closer = 1000;
		} 
		
		return survive_rate + (10*closerIndex) + (10*will_get_closer) - (dies*10000);
	}
	
	public JSONObject getJsonRpr(){
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("name", this.name);
			json.put("tried", this.tried);
			json.put("ok", this.ok);
			json.put("got_closer_to_the_target", this.got_closer_to_the_target);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}
}
