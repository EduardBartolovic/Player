package edu.hm.bartolov.se2.miner.player.tsp;

import edu.hm.bartolov.se2.miner.player.tsp.old.TSPNearestMushroomRouteMK3;
import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute2;
import edu.hm.bartolov.se2.miner.player.common.Route;
import edu.hm.bartolov.se2.miner.player.tool.ComperatorNorthEast;
import edu.hm.bartolov.se2.miner.player.tool.ComperatorNorthWest;
import edu.hm.bartolov.se2.miner.player.tool.ComperatorSouthEast;
import edu.hm.bartolov.se2.miner.player.tool.ComperatorSouthWest;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Set;

/**
 *
 * @author Edo
 */
public class TSPGridSolverMK1 implements TSP {
    /**
    * remaing Mushrooms.
    */
    private final Set<Position> remainingMushrooms;
    /**
     * 
     */
    private final Position startPosition;
    /**
     * 
     */
    private final Position endPosition;

    public TSPGridSolverMK1(Set<Position> remainingMushrooms, Position startPosition, Position endPosition) {
        this.remainingMushrooms = remainingMushrooms;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    @Override
    public Route getRoute() {
        
        
        //nearest Methode Route is the best Route till something better is found                
        Route bestRoute = new FlexibleRoute2(new TSPNearestMushroomRouteMK3(startPosition,endPosition,remainingMushrooms).getRoute());
        int rekordTime = bestRoute.totalDistance(startPosition, endPosition);
        
        final Route routeLeftToRightSouthToNorth = new FlexibleRoute2(remainingMushrooms);
        routeLeftToRightSouthToNorth.sort(new ComperatorSouthWest());
        
        final Route routeLeftToRightNorthToSouth = new FlexibleRoute2(remainingMushrooms);
        routeLeftToRightNorthToSouth.sort(new ComperatorNorthWest()); 
        
        
        final Route routeRightToLeftSouthToNorth = new FlexibleRoute2(remainingMushrooms);
        routeRightToLeftSouthToNorth.sort(new ComperatorSouthEast()); 
        
        final Route routeRightToLeftNorthToSouth = new FlexibleRoute2(remainingMushrooms);
        routeRightToLeftNorthToSouth.sort(new ComperatorNorthEast());
        
        int currentTime = routeLeftToRightSouthToNorth.totalDistance(startPosition, endPosition);
        if(currentTime < rekordTime){
            rekordTime = currentTime;
            bestRoute = routeLeftToRightSouthToNorth;
        }
        
        currentTime = routeLeftToRightNorthToSouth.totalDistance(startPosition, endPosition);
        if(currentTime < rekordTime){
            rekordTime = currentTime;
            bestRoute = routeLeftToRightNorthToSouth;
        }
        
        currentTime = routeRightToLeftSouthToNorth.totalDistance(startPosition, endPosition);
        if(currentTime < rekordTime){
            rekordTime = currentTime;
            bestRoute = routeRightToLeftSouthToNorth;
        }
        
        currentTime = routeRightToLeftNorthToSouth.totalDistance(startPosition, endPosition);
        if(currentTime < rekordTime){
            bestRoute = routeRightToLeftNorthToSouth;
        }
        
        bestRoute.add(endPosition);

        return bestRoute;    
    }
    
}
