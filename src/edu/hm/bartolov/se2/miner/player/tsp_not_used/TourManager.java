package edu.hm.bartolov.se2.miner.player.tsp_not_used;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * TourManager.
 * Manages multiple Tours.
 * Changelog:
 * 
 * Optimization:
 * 
 * Ideen:
 * ---
 * @author E.Bartolovic 
 * @version MK1
 */
public class TourManager {

    /**
     * Holds Positions.
     */
    private static final List<Position> POSITIONSTOGO = new ArrayList<>();

    /**
     * Adds a Position.
     * @param mush from typ position
     */
    static void addPosition(Position mush) {
        POSITIONSTOGO.add(mush);
    }
    
    /** Get a Position from given index.
     * 
     * @param index from typ int
     * @return Position
     */
    static Position getPosition(int index){
        return POSITIONSTOGO.get(index);
    }
    
    /**
     * Get the number of Positions.
     * @return numberOfPosition 
     */ 
    static int numberOfPosition(){
        return POSITIONSTOGO.size();
    }
}