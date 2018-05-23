
package edu.hm.bartolov.se2.miner.player.tsp;

import edu.hm.bartolov.se2.miner.player.tsp.old.TSPNearestMushroomRouteMK3;
import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute3;
import edu.hm.bartolov.se2.miner.player.common.Route;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;


public class TSPTabuSearch implements TSP,Callable<Route>{

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
    
    private final int radius;
    
    private final int searchCycles;

    public TSPTabuSearch( Position startPosition, Position endPosition,Set<Position> remainingMushrooms, int radius, int searchCycles) {
        this.remainingMushrooms = remainingMushrooms;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.radius = radius;
        this.searchCycles = searchCycles;
    }
    
    
    @Override
    public Route getRoute() {
       
        Route bestCandidate = new TSPNearestMushroomRouteMK3(startPosition, endPosition, remainingMushrooms).getRoute();
        bestCandidate.remove(endPosition);
        Route bestFound = bestCandidate;
        int bestTime = bestCandidate.totalDistance(startPosition, endPosition);
        
        final Set<Route> tabuList = new HashSet<>();

        tabuList.add(bestCandidate);

        for(int counter = 0; counter < searchCycles ; counter++){

                final List<Route> neighbors = getNeighbors(bestCandidate);

                bestCandidate = neighbors.get(0);
                int currentbestTime = bestCandidate.totalDistance(startPosition, endPosition);
                
                for(Route candidate : neighbors){
                    final int time = candidate.totalDistance(startPosition, endPosition);
                    if ( !tabuList.contains(candidate) &&  time < currentbestTime ){
                        bestCandidate = candidate;
                        currentbestTime = time;
                        if(time < bestTime){
                            bestTime = time;
                            bestFound = candidate;
                        }    
                    }
                       
                }

                tabuList.addAll(neighbors);
        }

        bestFound.add(endPosition);
        return bestFound;
    }

    private List<Route> getNeighbors(Route bestCandidate) {
        
        final int numberRange = bestCandidate.size(); 
        final List<Route> neighbors = new ArrayList<>(radius);
        
        final Random randomNumber = new Random(); 

        for(int counter = 0; counter < radius; counter++){
            final int tourPos1 = randomNumber.nextInt(numberRange); // generate Random Number in NumberRange
            int tourPos2 = randomNumber.nextInt(numberRange);// generate Random Number in NumberRange
            while(tourPos1 == tourPos2 ){//to make sure that tourPos1 and tourPos2 are different
                tourPos2 = randomNumber.nextInt(numberRange); 
            }

            final Position mushSwap1 = bestCandidate.get(tourPos1);
            final Position mushSwap2 = bestCandidate.get(tourPos2);

            final Route newCandidate = new FlexibleRoute3(bestCandidate);
            // Swap them
            newCandidate.set(tourPos2, mushSwap1);
            newCandidate.set(tourPos1, mushSwap2);
            neighbors.add(newCandidate);
        }
        return neighbors;
    }

    @Override
    public Route call() throws Exception {
        return getRoute();
    }
    
}
