package edu.hm.bartolov.se2.miner.player.common;

import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Edo
 */
public class FlexibleRoute3 extends ArrayList<Position> implements Route{
    

    public FlexibleRoute3(){
        super(5);
    }
    
    public FlexibleRoute3(int size){
        super(size);
    }
    
    public FlexibleRoute3(Collection<Position> collection){
        super(collection);
    }
    
    public FlexibleRoute3(Position... positions){
        super(Arrays.asList(positions));
    }
    
    /** Calculate the total Distance of an Route.
     * @param startPosition
     * @param endPosition
     * @return currentDistance in a Route
     */
    @Override
    public int totalDistance(Position startPosition, Position endPosition){
        if(isEmpty())
            return 0;
        
        int currentDistance = Route.distanceBetweenPositions(startPosition,get(0));
        for(int counter = 0;counter+1 < size();counter++){
            currentDistance += Route.distanceBetweenPositions(get(counter),get(counter+1));
        }
        currentDistance += Route.distanceBetweenPositions(get(size()-1), endPosition);
        return currentDistance;
    }
    
    /** Calculate the total Distance of an Route.
     * @return currentDistance in a Route
     */
    @Override
    public int totalDistance(){
        if(isEmpty())
            return 0;
        
        int currentDistance = 0;
        for(int counter = 0;counter+1 < size();counter++){
            currentDistance += Route.distanceBetweenPositions(get(counter),get(counter+1));
        }
        
        return currentDistance;
    }

    @Override
    public Object clone() {
        return super.clone();
    }
}
