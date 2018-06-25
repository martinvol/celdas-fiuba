package ar.fi.uba.celdas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class RuleDumper {
	static String fileName = "test.json"; 
	
	
	public static void loadRules(){
		// iterate rules
		
		File file = new File(fileName);
	    try {
			String content = FileUtils.readFileToString(file, "utf-8");
			JSONObject rulesJO = new JSONObject(content);
			
			
			Iterator<?> keys = rulesJO.keys();

			while( keys.hasNext() ) {
			    String key = (String) keys.next();
			    if ( rulesJO.get(key) instanceof JSONObject ) {
			    	Rule rule = Rule.getRule(key);

			    	
			    	
			    	setAction(rule, rulesJO.getJSONObject(key), 0, "u"); //.add(new Action("u"));
			    	setAction(rule, rulesJO.getJSONObject(key), 1, "d"); //.add(new Action("d")); 
			    	setAction(rule, rulesJO.getJSONObject(key), 2, "l"); //.add(new Action("l"));
			    	setAction(rule, rulesJO.getJSONObject(key), 3, "r"); //.add(new Action("r"));
			    	setAction(rule, rulesJO.getJSONObject(key), 4, "a"); //.add(new Action("a"));
			    	setAction(rule, rulesJO.getJSONObject(key), 5, "p"); //.add(new Action("p"));
			    }
			}
			
			//Rule.getRule(ruleId)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
		
	}
	
	
	private static void setAction(Rule rule, JSONObject rulesJO, int i, String key) {
		// TODO Auto-generated method stub
		try {
			
			JSONObject node = rulesJO.getJSONObject(key);
			//int a = (int) node.get("tried");
			rule.possible_actions.get(0).tried = (int) node.get("tried");
			rule.possible_actions.get(0).ok = (int) node.get("ok");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//.add(new Action("u"));
	}


	public static void saveRules(Hashtable<String, Rule> ruleIndex) {
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
		
		try (FileWriter file = new FileWriter(fileName)) {

            file.write(theoryJson.toString(4));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
			// TODO Auto-generated catch block
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