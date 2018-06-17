package ar.fi.uba.celdas;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

public class RuleDumper {

	public void saveRules(Hashtable<String, Rule> ruleIndex) {
		// rule to actionDump
		// Hashtable<String, Hashtable<String, Integer>> dump = new Hashtable<String, Hashtable<String, Integer>>();  
		
		JSONObject theoryJson = new JSONObject();
		
		for (String key : ruleIndex.keySet()) {
			Rule rule = ruleIndex.get(key); 
			
			JSONObject rule_json = new JSONObject();			
			
			//Hashtable<String, Integer> actionRep = new Hashtable<String, Integer>();
			
			for (Action action: rule.possible_actions) {
				try {
					rule_json.put(action.name, action.getJsonRpr());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// dump.put(key, key)
			try {
				theoryJson.put(rule.rule_id, rule_json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try (FileWriter file = new FileWriter("test.json")) {

            file.write(theoryJson.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		System.out.println("dumping json!");
		//System.out.println(theoryJson);
		
	}
	
	
}


//json structure:
//	
//	rule_id : {
//				u : {
						//tried: 0, 
						//name : 'hello'
//						}
//	
//	
//}