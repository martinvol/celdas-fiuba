package ar.fi.uba.celdas;

import java.util.ArrayList;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

public class Agent extends AbstractPlayer{

	Vector2d player;
	Vector2d llave = new Vector2d();
	Vector2d target=llave;
	Vector2d salida;
	Boolean got_key = false;
	Boolean got_to_intermediary_point =false;
	JsonGetter rules;
	ArrayList<InputAction> rules_movement;
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
	}
	
	/**
	 * return ACTION_NIL on every call to simulate doNothing player
	 * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
	 * @return 	ACTION_NIL all the time
	 */
	
	private ACTIONS get_actions_from_rule_action(String rule_action){
		switch (rule_action) {
        case "use":  
        	return ACTIONS.ACTION_USE;
        case "left":  
        	return ACTIONS.ACTION_LEFT;
        case "right":  
        	return ACTIONS.ACTION_RIGHT;
        case "down":  
        	return ACTIONS.ACTION_DOWN;
        case "up":  
        	return ACTIONS.ACTION_UP;
        default: 
                 return ACTIONS.ACTION_NIL;
    }
		
	}
	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		
		Perception perception = new Perception(stateObs);
	    System.out.println(perception);
	    

	    player = new Vector2d();
	    salida = new Vector2d();
	    
	    for(int x=0;x< stateObs.getObservationGrid().length; x++){
        	for(int y=0;y< stateObs.getObservationGrid()[0].length; y++){
        		// donde está el player
        		if (perception.getLevel()[y][x] == 'A' | perception.getLevel()[y][x] == '?'){
        			/*System.out.print("player está en: x: ");
        			System.out.print(x);
        			System.out.print(" y:");
        			System.out.print(y);
        			System.out.println("");*/
        			player.x =x;
        			player.y=y;
        			        			
        		}
        		
        		if (perception.getLevel()[y][x] == '+'){
        			llave.x = x;
        			llave.y = y;
        		}
        		
        		if (perception.getLevel()[y][x] == 'g'){
        			salida.x = x;
        			salida.y = y;
        		}
        		
        		
        	}
        }
	    
	    
	    if (player.x == target.x && player.y == target.y){
	    	target = rules.get_next_target();
	    }
	        
	    
	    for (InputAction temp : rules_movement) {
			System.out.println(temp.action);
			String rule_name = temp.rule;
			switch(rule_name) {
			    case "bug_in_font":
			    	// ataque
					
					if (perception.getLevel()[(int)(player.y+stateObs.getAvatarOrientation().y)][(int)(player.x + stateObs.getAvatarOrientation().x)] == '2'){
						return get_actions_from_rule_action(temp.action);
			 		}
			        break;
			    //defensa
			    case "bug_right":
			    	//derecha
					if (perception.getLevel()[(int)player.y][(int)player.x+1] == '2'){
						return get_actions_from_rule_action(temp.action);
					}
			        break;
			    case "bug_left":
			    	//izquierda
					if (perception.getLevel()[(int)player.y-1][(int)player.x-1] == '2'){
						return get_actions_from_rule_action(temp.action);
					}
			        break;
			    case "bug_up":
			    	//arriba
					if (perception.getLevel()[(int)player.y-1][(int)player.x] == '2'){
						return get_actions_from_rule_action(temp.action);
					}
			        break;
			    case "bug_down":
			    	//abajo
					if (perception.getLevel()[(int)player.y+1][(int)player.x] == '2'){
						return get_actions_from_rule_action(temp.action);
					}
			        break;
			    default:
			        //code block
			}
		}
	    
		
		// moverse, si no te moviste todavía 
		return this.ir(target, perception, stateObs);
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
