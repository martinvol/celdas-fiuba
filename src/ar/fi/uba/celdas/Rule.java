package ar.fi.uba.celdas;

import java.util.ArrayList;
import java.util.Hashtable;

public class Rule {
	public String rule_id;
	
	public ArrayList<Action> possible_actions = new ArrayList<Action>();
	
	public static Hashtable<String, Rule> index = new Hashtable<String, Rule>();

	public Rule (String rule_id){
		// here we fill posible_actions with all the actions initialized in zero
		this.rule_id = rule_id;
		
		possible_actions.add(new Action("u"));
		possible_actions.add(new Action("d"));
		possible_actions.add(new Action("l"));
		possible_actions.add(new Action("r"));
		possible_actions.add(new Action("a"));
		possible_actions.add(new Action("p"));
		
		System.out.println("Created rule: " + this.rule_id);
	}
	
	public static Rule getRule(String ruleId) {
		// returns the best action
		if (!index.containsKey(ruleId)) {
			//create rule and add;
			index.put(ruleId, new Rule(ruleId));
		}
		
		return index.get(ruleId);
	}
	
	public static void trainAction(String rule_id) {
		// given a rule id, pick an action and then store the result
	}
	
	
	public Action getRandomAction() {
  
		java.util.Random random = new java.util.Random();
		int random_computer_card = random.nextInt(possible_actions.size());
		Action outAction = possible_actions.get(random_computer_card);
		
		return outAction;
	}

	public Action getBestAction(String next_path) {
		Action bestAction = possible_actions.get(0);
		float maxUtility = bestAction.getUtility(next_path);
		
		System.out.println("Analysing rules");
		for (Action action : possible_actions) {
			float utility = action.getUtility(next_path);
			if (utility > maxUtility) {
				maxUtility = utility;
				bestAction = action;
			}
		System.out.println(action.name + " " + String.valueOf(utility));
		}
		return bestAction;
	}
	
}


//format
//
//
//1 2 3
//4 5 6 # five is always going to be the pj, not included in the rule then
//7 8 9
//
//finder u
//orientation: u
//
//orientation: u d l r
//finder: u d l r
//
//rule:
//    situation: act
//    "12346789Ouf": * up (cout, murio:True/False, )
//              * down (count)
//              * right (count)
//              * left (count)
//              * act (count)
//              * pass (count)
//
//
//utilidad: cuan cerca estás de la salida + 100*proba de morir + 1/cantidadDeBichos
//
//
//guardar teoría anterior? para garantizar resultado
//
//
//
//{
//    'ruleid': {
//                'actionid': {
//                    'tried': number
//                    'ok': number,
//                    'killed': number, // tried-ok
//                    'got_closer_to_the_target': number, # if the desition was the same as the closest path
//                    'number_of_enemies': number
//                }
//    }
//
//
//}
//
//Entrenamiento:
//buscar ruleid y selecionar las acciones posibles.
//elegir una acción, guardar su resultado. cada regla tiene su lista de acciones
//
//
//Usar:
//buscar el rule id y ejecutar el que maximise la utilidad.