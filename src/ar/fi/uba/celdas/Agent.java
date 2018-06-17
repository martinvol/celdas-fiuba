package ar.fi.uba.celdas;

import java.util.ArrayList;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import tools.Vector2d;
import tools.pathfinder.Node;
import tools.pathfinder.PathFinder;

public class Agent extends AbstractPlayer{

	Vector2d player;
	Vector2d llave = new Vector2d();
	Vector2d target=llave;
	Vector2d salida;
	Boolean got_key = false;
	Boolean got_to_intermediary_point =false;
	JsonGetter rules;
	ArrayList<InputAction> rules_movement;
	
    Action action_used;
    
    Rule current_rule;
    
    String lastTimePerception;
    
    static int times_played=0;
    
    RuleDumper ruleDumper;
    
	/**
	 * initialize all variables for the agent
	 * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
	 */
	
	private int game = 0;
	
	public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
		this.rules = new JsonGetter();
		rules_movement = this.rules.read_rules();
		this.target = rules.get_next_target(); 
		times_played += 1;
		
		this.ruleDumper = new RuleDumper();
	}
	
	/**
	 * return ACTION_NIL on every call to simulate doNothing player
	 * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
	 * @return 	ACTION_NIL all the time
	 */
	
	
	private String getPerceptionFor(int x, int y, Perception perception) {
		int max_y = perception.getLevel().length;
		int max_x = perception.getLevel()[0].length;
		if (x < 0 || y < 0 || y > max_y || y > max_x) {
			System.out.println("Analisis fuera de límite");
			return "w";
		}
		
		return String.valueOf(perception.getLevel()[y][x]);
		
	}
	
	private char getOrientation(Vector2d toCompare) {
//		int payer_x = (int) this.player.x; int payer_y = (int) this.player.y;
//		int co

		if (player.x + 1 == toCompare.x) {
			return 'r';
		}
		if (player.x - 1 == toCompare.x) {
			return 'l';
		}
		if (player.y - 1 == toCompare.y) {
			return 'u';
		}
		if (player.y + 1 == toCompare.y) {
			return 'd';
		}
		System.out.println("Error en el getOrientation");
		return 'h';
	}
	
	private char getOrientationAvatar(Vector2d toCompare) {
		if (+1 == (int) toCompare.x) {
			return 'r';
		}
		if (-1 == (int) toCompare.x) {
			return 'l';
		}
		if (-1 == (int) toCompare.y) {
			return 'u';
		}
		if (+1 == (int) toCompare.y) {
			return 'd';
		}
		System.out.println("Error en el getOrientationAvatar");
		return 'h';
	}
	
	private Action generateOrGetRule(Perception perception, PathFinder pathfinder, StateObservation stateObs) {
		ArrayList<Node> path = pathfinder.getPath(this.player, this.target);
	    
		System.out.println("Target is in : " + String.valueOf(this.target.x) + ", " + String.valueOf(this.target.y));
		
	    for (int i=0; i < path.size(); i++) {
	    	Node node = path.get(i);
	    	System.out.print(node.position);
	    	System.out.print("=>");
	    }
	    System.out.println("");
	    
	    int x = (int) player.x;
	    int y = (int) player.y;
	    
	    String position_1 = getPerceptionFor(x-1, y-1, perception);
	    String position_2 = getPerceptionFor(x, y-1, perception);
	    String position_3 = getPerceptionFor(x+1, y-1, perception);
	    String position_4 = getPerceptionFor(x-1, y, perception);
	    String position_6 = getPerceptionFor(x+1, y, perception);
	    String position_7 = getPerceptionFor(x-1, y+1, perception);
	    String position_8 = getPerceptionFor(x, y+1, perception);
	    String position_9 = getPerceptionFor(x+1, y+1, perception);
	    String next_path= String.valueOf(getOrientation(path.get(0).position));
	    
	    String pjOrientation = String.valueOf(getOrientationAvatar(stateObs.getAvatarOrientation()));
	    
	    String currentStatus = position_1 + position_2 + position_3 + position_4 + position_6 + position_7 + position_8 + position_9 + next_path + pjOrientation ;
	    
	    System.out.println("Current perception: " + currentStatus);
	    
	    //Boolean training = !((times_played%2) == 0);
	    Boolean training = false;
	    
	    
	    if (current_rule == null) {
	    	System.out.println("First play, can't analyse a rule");
	    } else {
	    	action_used.ok += 1;
	    	if (lastTimePerception.equals(currentStatus.substring(0,8))) {
	    		System.out.println("Action is repeating");
	    		action_used.is_repeating +=1;
	    	}
	    }
	    
	    lastTimePerception = currentStatus.substring(0,8);
	    
	    current_rule = Rule.getRule(currentStatus);
	    if (training) {
	    	System.out.println("training!");
	    	action_used = current_rule.getRandomAction();
	    	// somewhere else I should do Rule.storeActionResult(currentStatus, action, result) 
	    } else {
	    	System.out.println("not training!");
	    	action_used = current_rule.getBestAction(next_path);
	    }
	    
	    action_used.tried += 1;
	    
		if (action_used.name.equals(next_path)) {
			System.out.println("Moved in the right direction! " + next_path);
			action_used.got_closer_to_the_target += 1; 
		}

		System.out.println("Action used: " + action_used.name);
	
	    return action_used;
	}
	
	private void findTargets(Perception perception, StateObservation stateObs) {
		player = new Vector2d();
	    salida = new Vector2d();
	    
	    for(int x=0;x< stateObs.getObservationGrid().length; x++){
        	for(int y=0;y< stateObs.getObservationGrid()[0].length; y++){
        		// donde está el player
        		if (perception.getLevel()[y][x] == 'A' | perception.getLevel()[y][x] == '?'){
        			player.x =x;
        			player.y=y;
        			        			
        		}
        		
        		if (perception.getLevel()[y][x] == '+'){
        		    System.out.print("Encontré llave: ");System.out.print(x);System.out.print(" ");System.out.println(y);
	
        			llave.x = x;
        			llave.y = y;
        		}
        		
        		if (perception.getLevel()[y][x] == 'g'){
        			salida.x = x;
        			salida.y = y;
        		}
        		
        		
        	}
        }
	    System.out.print("Player está en: ");System.out.println(player);
	}
	
	
	private ACTIONS get_actions_from_rule_action(String rule_action){
		switch (rule_action) {
        case "a":  
        	return ACTIONS.ACTION_USE;
        case "l":  
        	return ACTIONS.ACTION_LEFT;
        case "r":  
        	return ACTIONS.ACTION_RIGHT;
        case "d":  
        	return ACTIONS.ACTION_DOWN;
        case "u":  
        	return ACTIONS.ACTION_UP;
        default: 
                 return ACTIONS.ACTION_NIL;
    }
		
	}
	
	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		
		PathFinder pathfinder = new PathFinder(new ArrayList<Integer>()); 
		pathfinder.run(stateObs);
				
		Perception perception = new Perception(stateObs);
	    System.out.println(perception);
	    

	    this.findTargets(perception, stateObs);
	    
	    if (player.x == target.x && player.y == target.y){
	    	target = rules.get_next_target();
	    }
	        
	    Action to_use_action = generateOrGetRule(perception, pathfinder, stateObs);
	    
	    ACTIONS out = get_actions_from_rule_action(to_use_action.name);
	    this.ruleDumper.saveRules(Rule.index);
	    return out; 
	    
//	    for (InputAction temp : rules_movement) {
//			//System.out.println(temp.action);
//			String rule_name = temp.rule;
//			switch(rule_name) {
//			    case "bug_in_font":
//			    	// ataque
//					
//					if (perception.getLevel()[(int)(player.y+stateObs.getAvatarOrientation().y)][(int)(player.x + stateObs.getAvatarOrientation().x)] == '2'){
//						return get_actions_from_rule_action(temp.action);
//			 		}
//			        break;
//			    //defensa
//			    case "bug_right":
//			    	//derecha
//					if (perception.getLevel()[(int)player.y][(int)player.x+1] == '2'){
//						return get_actions_from_rule_action(temp.action);
//					}
//			        break;
//			    case "bug_left":
//			    	//izquierda
//					if (perception.getLevel()[(int)player.y-1][(int)player.x-1] == '2'){
//						return get_actions_from_rule_action(temp.action);
//					}
//			        break;
//			    case "bug_up":
//			    	//arriba
//					if (perception.getLevel()[(int)player.y-1][(int)player.x] == '2'){
//						return get_actions_from_rule_action(temp.action);
//					}
//			        break;
//			    case "bug_down":
//			    	//abajo
//					if (perception.getLevel()[(int)player.y+1][(int)player.x] == '2'){
//						return get_actions_from_rule_action(temp.action);
//					}
//			        break;
//			    default:
//			        //code block
//			}
//		}
	    
		
		// moverse, si no te moviste todavía 
		// return this.ir(target, perception, stateObs);
		// lave
	}
	
	

	private ACTIONS ir(Vector2d lugar, Perception perception, StateObservation stateObs) {
		System.out.print("target está en: x: ");
		System.out.print(lugar.x);
		System.out.print(" y:");
		System.out.print(lugar.y);
		System.out.println("");
		
		
		//if (player.x < lugar.x && !(perception.getLevel()[(int)player.y][(int)player.x+1]=='w')){
		if (player.x < lugar.x && !(perception.getLevel()[(int)player.y][(int)player.x+1]=='w')){
			return ACTIONS.ACTION_RIGHT;
		}
		
		if (player.x > lugar.x && !(perception.getLevel()[(int)player.y][(int)player.x-1]=='w')){
		//if (player.x > lugar.x){
			return ACTIONS.ACTION_LEFT;
		}

		//if (player.y > lugar.y&& !(perception.getLevel()[(int)player.y-1][(int)player.x]=='w')){
		if (player.y > lugar.y){
			return ACTIONS.ACTION_UP;
		}
		
		if (player.y < lugar.y && !(perception.getLevel()[(int)player.y+1][(int)player.x]=='w')){
		//if (player.y < lugar.y){
			return ACTIONS.ACTION_DOWN;
		}
		
		if(player.y == lugar.y && player.x == lugar.x){
			return ACTIONS.ACTION_NIL;
		} 
		
		return this.get_random_action(stateObs);
	}

	private ACTIONS get_random_action(StateObservation stateObs) {
		ArrayList<Types.ACTIONS> actions = stateObs.getAvailableActions();
        int index = (int)(Math.random() * actions.size());
        return  actions.get(index);
	}
}


/*metodología
 * seguí este camino harcodeado de pasos
 * si estás en(x1y2), andá a (x2y2) (en realidad, con caminos de líneas rectas)
 * si estás en(x2y2), andá a (x3y3) (pasando por llave y puerta)
 * tenés un bicho enfrente, ejecuta regla
 * tenés un bicho cerca, ejecutá la regla correspondiente
 * 
 * */

/*casos particularesl
No hay que pegarle a un chobi se tenés uno trás
hacer el mejor camino no quiere decir que sea el mejor con bichos en el medio
hacer el mejor camino no garantiza que no nos puedan encerrar
que pasa si no se puede mover en cada regla?
*/
