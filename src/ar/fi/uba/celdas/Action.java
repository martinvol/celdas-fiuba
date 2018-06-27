package ar.fi.uba.celdas;

import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

public class Action {

	String action_taken; 
	
	int tried  = 1; // starts in one to avoid divided by zero 
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
		float is_repeating_index = ((float) is_repeating)/floatTried;
		float number_of_enemies_index = ((float) number_of_enemies)/floatTried;
		int dies = tried - ok;
		
		int will_get_closer = 0; 
		
		if (next_path.equals(name)) {
			will_get_closer = 1000;
		} 
		
		return (10*survive_rate) + (closerIndex) + (1000*will_get_closer) - (dies*100000) + (number_of_enemies_index*100);
	}
	
	public JSONObject getJsonRpr(){
		
		JSONObject json = new JSONObject();
		
		try {
			//json.put("name", this.name);
			json.put("tried", this.tried);
			json.put("ok", this.ok);
			json.put("got_closer_to_the_target", this.got_closer_to_the_target);
			json.put("number_of_enemies", this.number_of_enemies);
			json.put("is_repeating", this.is_repeating);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}
}
