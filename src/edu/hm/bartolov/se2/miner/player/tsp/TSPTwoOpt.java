package edu.hm.bartolov.se2.miner.player.tsp;

import static edu.hm.bartolov.se2.miner.player.common.FlexibleRoute.totalDistance;
import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute2;
import edu.hm.bartolov.se2.miner.player.common.Route;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Edo
 */
public class TSPTwoOpt implements TSP{
    /**
    * remaing Mushrooms.
    */
    private final Position[] tour;
    
    private final Position[] newTour ;
    /**
     *
     */
    private final Position startPosition;
    /**
     *
     */
    private final Position endPosition;

    public TSPTwoOpt(Set<Position> remainingMushrooms, Position startPosition, Position endPosition) {
        tour = new Position[remainingMushrooms.size()];
        final Iterator<Position> iter = remainingMushrooms.iterator();
        for(int counter = 0; counter < remainingMushrooms.size() ; counter++){
            tour[counter] = iter.next();
        }
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        newTour = new Position[remainingMushrooms.size()];
    }
    
    @Override
    public Route getRoute() {

        for(int improve = 0; improve < 1000 ; improve ++){
            twoOpt();
        }
        
        final Route returnable = new FlexibleRoute2(tour);
        returnable.add(endPosition);
        return returnable;
    }
    
    private void twoOptSwap( int i, int k ) {
        final int size = tour.length;

        // 1. take route[0] to route[i-1] and add them in order to new_route
        for ( int c = 0; c <= i - 1; ++c ){
            newTour[c] = tour[c] ;
        }

        // 2. take route[i] to route[k] and add them in reverse order to new_route
        int dec = 0;
        for ( int c = i; c <= k; ++c ){
            newTour[c] = tour[k - dec];
            dec++;
        }

        // 3. take route[k+1] to end and add them in order to new_route
        for ( int c = k + 1; c < size; ++c ){
            newTour[c] = tour[c];
        }
    }
    
    public void twoOpt() {
        // Get tour size
        final int size = tour.length;
        
        System.arraycopy(tour, 0, newTour, 0, size);

        // repeat until no improvement is made 
        int improve = 0;

        while ( improve < 1000 ){
            int best_distance = totalDistance(tour,startPosition, endPosition);

            for ( int i = 1; i < size - 1; i++ ){
                
                for ( int k = i + 1; k < size; k++) {
                    twoOptSwap( i, k );
                    int new_distance = totalDistance(tour,startPosition, endPosition);

                    if ( new_distance < best_distance ){
                        // Improvement found so reset
                        improve = 0;

                        System.arraycopy(newTour, 0, tour, 0, size);

                        best_distance = new_distance;

                    }
                }
            }

            improve ++;
        }
    }
    
}
