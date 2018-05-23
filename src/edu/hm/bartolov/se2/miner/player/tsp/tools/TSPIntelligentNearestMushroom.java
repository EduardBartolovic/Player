package edu.hm.bartolov.se2.miner.player.tsp.tools;

import edu.hm.bartolov.se.miner.player.tsp.TSPBruteForceMK4;
import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute;
import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute3;
import edu.hm.bartolov.se2.miner.player.common.Route;
import edu.hm.bartolov.se2.miner.player.tsp.TSP;
import edu.hm.bartolov.se2.miner.player.tsp.TSPBruteForceIntelligentMK5;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class TSPIntelligentNearestMushroom implements TSP{

    /**
     *
     */
    private final Position startPosition;
    /**
     *
     */
    private final Position endPosition;
    /**
    * remaing Mushrooms.
    */
    private final Set<Position> remainingMushrooms;

    public TSPIntelligentNearestMushroom(Position startPosition, Position endPosition, Set<Position> remainingMushrooms) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.remainingMushrooms = remainingMushrooms;
    }
    
    @Override
    public Route getRoute() {
        
        final Route nearestMushTour = new FlexibleRoute3(remainingMushrooms.size()); // return List for Route
        Position start = startPosition;
        Position end;
        
        while(!remainingMushrooms.isEmpty()){
            
            final Route nearestRoute = nearestMushrooms(start, remainingMushrooms);  //get some nearest mushrooms
            end = nearestRoute.get(nearestRoute.size()-1); //get last position
            nearestRoute.remove(end);                       //remove last
            nearestMushTour.addAll(new TSPBruteForceMK4(start,end,new HashSet(nearestRoute)).getRoute()); // get best combinations
            remainingMushrooms.removeAll(nearestRoute);   //remove allready used mushrooms
            start = end;  //next start is last end
       
        }
        
        nearestMushTour.add(endPosition);
        return nearestMushTour;

    }
    
    /**Search the nearest Mushroom from startPosition.
     * @param positionToStart from Typ Position
     * @param hypoRemainingMush list of Mushrooms which are not hypotetical picked up
     * @return nearestMush typ Position
     * @trows NullpointerException if no Mushrooms left in hypoRemainingMush.
     */
    protected Route nearestMushrooms(Position positionToStart, Set<Position> hypoRemainingMush){
 
        final Route nearestMushTour = new FlexibleRoute3(13); // return List for Route
        Position lastPosition = startPosition; // start Position
        //Iterate thrue hypoRemainingMush to find nearest Mushroom
        for(int counter = 0 ;counter < 13 ; counter++){
            if(hypoRemainingMush.isEmpty())
                return nearestMushTour;
            final Position newPosition = nearMush(lastPosition,hypoRemainingMush);
            lastPosition = newPosition;
            hypoRemainingMush.remove(newPosition);
            nearestMushTour.add(newPosition);
        }
        
        return nearestMushTour;
        
    }
    
    
    /**Search the nearest Mushroom from startPosition.
     * @param positionToStart from Typ Position
     * @param hypoRemainingMush list of Mushrooms which are not hypotetical picked up
     * @return nearestMush typ Position
     * @trows NullpointerException if no Mushrooms left in hypoRemainingMush.
     */
    protected Position nearMush(Position positionToStart, Set<Position> hypoRemainingMush){

        Position nearestMush = null;
        int shortestDistance = Integer.MAX_VALUE;// Rekord Distance

        for(Position positionToTest: hypoRemainingMush){
            final int newDistanceToRun = FlexibleRoute.distanceBetweenPositions(positionToStart,positionToTest);

            if(newDistanceToRun < shortestDistance){// if new distance is a rekord
                shortestDistance = newDistanceToRun;
                nearestMush = positionToTest;
            }
            //if to mushrooms have the same distance to startPos than go to more distance to destination
            else if(newDistanceToRun == shortestDistance){
                shortestDistance = newDistanceToRun;
                nearestMush = positionToTest;
            }
        }
        
        return nearestMush;
    }
    
    
    
    
}
