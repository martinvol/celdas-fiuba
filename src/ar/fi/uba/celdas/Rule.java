package ar.fi.uba.celdas;

import java.util.ArrayList;
import java.util.Hashtable;

public class Rule {
	String rule_id;
	
	ArrayList<Action> possible_actions;
	
	static Hashtable<String, Rule> index = new Hashtable<String, Rule>();

	public Rule (){
		// here we fill posible_actions with all the actions initialized in zero
	}
	
	public static Rule getRule(String ruleId) {
		// returns the best action
		if (!index.containsKey(ruleId)) {
			//create rule and add;
		}
		
		return index.get(ruleId);
	}
	
	public Action pickRandomAction() {
		return new Action();
	}
	
	public static void trainAction(String rule_id) {
		// given a rule id, pick an action and then store the result
	}
	
	
	public static void getRandomAction() {
		String[] available_cards = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Queen", "King", "Jack", "Ace"};  
		java.util.Random random = new java.util.Random();
		int random_computer_card = random.nextInt(available_cards.length);
		System.out.println(available_cards[random_computer_card]);
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
//orientation: u d l f
//finder: u d l f
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