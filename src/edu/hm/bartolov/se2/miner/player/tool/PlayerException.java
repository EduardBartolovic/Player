package edu.hm.bartolov.se2.miner.player.tool;

/**
 * custom exception class.
 * 
 * @author Edo
 */
public class PlayerException extends RuntimeException {
    
    PlayerException(){
    }
    
    PlayerException(String message){
        super(message);
    }
    
}
