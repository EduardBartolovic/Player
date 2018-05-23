package edu.hm.bartolov.se2.miner.player.tsp;

import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute3;
import edu.hm.bartolov.se2.miner.player.common.Route;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Edo
 */
public class TSPIntelligentBruteForce implements TSP{
    
    private final Position[] mushrooms;
    private final Position start;
    private final Position end;

    public TSPIntelligentBruteForce(Set<Position> mushrooms,Position start,Position end) {
        this.mushrooms = new Position[mushrooms.size()];
        mushrooms.toArray(this.mushrooms);
        this.start = start;
        this.end = end;
    }
    
    
    @Override
    public Route getRoute() {

        final int size = mushrooms.length;
        final int[] c = new int[size];
        
        List<Position> bestRoute = Arrays.asList(mushrooms);
        int bestTime = Route.totalDistance(mushrooms, start, end);
        int counter = 0;
        int i = 0;
        while (i < size){
            if (c[i] < i){
                if (i%2==0){
                    final Position temp = mushrooms[0];
                    mushrooms[0] = mushrooms[i];
                    mushrooms[i] = temp;
                }else{
                    final Position temp = mushrooms[c[i]];
                    mushrooms[c[i]] = mushrooms[i];
                    mushrooms[i] = temp;
                }
                if(counter == size){
                    final int currentTime = Route.totalDistance(mushrooms, start, end);
                    if(currentTime < bestTime){
                        bestTime = currentTime;
                        bestRoute = Arrays.asList(mushrooms);
                    }
                    counter=0;
                }else{
                    counter++;
                }
                c[i]++;
                i = 0;
            }else{
                c[i] = 0;
                i++;
            }  
        }
        final Route route = new FlexibleRoute3(bestRoute);
        route.add(end);
        return route;
    }
    
}
