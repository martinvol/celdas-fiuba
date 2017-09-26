package ar.fi.uba.celdas;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.*;

import core.game.Observation;

import tools.Vector2d;


public class JsonGetter {
	JSONObject obj;
	int current_target = 0;
	JSONArray movements_array;
	
	public JsonGetter(){
		try {
			//obj = new JSONObject(new FileReader("src/ar/fi/uba/celdas/rules.json").);
			
			//obj = new JSONObject(new Files.readAllLines("src/ar/fi/uba/celdas/rules.json", StandardCharsets.UTF_8));
			byte[] encoded = Files.readAllBytes(Paths.get("src/ar/fi/uba/celdas/rules.json"));
			obj = new JSONObject(new String(encoded, StandardCharsets.UTF_8));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.get_path();
	}
	
	private void get_path(){	
		try {
			movements_array = obj.getJSONArray("movements");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Vector2d get_next_target(){
		JSONArray movement;
		try {
			movement = movements_array.getJSONArray(this.current_target);
			this.current_target += 1;
			Vector2d out = new Vector2d();
			
			out.x = movement.getInt(0);
			out.y = movement.getInt(1);
			
			return out;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<InputAction> read_rules(){
		ArrayList<InputAction> rules =  new ArrayList<InputAction>();
		
		try {
			JSONObject rules_json = obj.getJSONObject("rules"); 
			Iterator keys = rules_json.keys();

			while( keys.hasNext() ) {
			    String key = (String)keys.next();
			    InputAction input_action = new InputAction();
			    input_action.rule = key;
			    input_action.action = rules_json.getString(key);
			    rules.add(input_action);
			    /*System.out.println(key);
			    System.out.println(rules_json.get(key));*/
			}
			return rules;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
}
