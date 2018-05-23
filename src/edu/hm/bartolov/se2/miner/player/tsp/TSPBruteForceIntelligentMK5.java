package edu.hm.bartolov.se2.miner.player.tsp;

import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute;
import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute3;
import edu.hm.bartolov.se2.miner.player.common.Route;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * TSPBruteFroveMK5
 * returns the best/optimal Route for Collecting Mushrooms.
 * try to solve TSP with brute Force.
 * reducing overhead thrue eleminating bad branches
 *  works only for 9 or smaller size of remaining Mushrooms.
 * Changelog:
 * solved a bug if a mush on Destiantion
 * ---
 * @author E.Bartolovic 
 * @version MK3
 */
public class TSPBruteForceIntelligentMK5 implements TSP{
    
    private final Position startPosition;
    private final Position endPosition;
    private final FlexibleRoute remainingMushrooms;
    private final int sortCriteria;

    
    /**
     * Constructor.
     * @param startPosition
     * @param endPosition
     * @param remainingMushrooms
     * @param sortCriteria
     */
    public TSPBruteForceIntelligentMK5(Position startPosition,Position endPosition,Set<Position> remainingMushrooms,int sortCriteria){
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.remainingMushrooms = new FlexibleRoute(remainingMushrooms);
        this.sortCriteria = sortCriteria;
    }
    
    
    @Override
    public Route getRoute(){
        
        final List<Route> allCombinations = generatePerm(remainingMushrooms);// get all combinations of Mushroomremaining

        int rekordListTime;// to track best time found 
        Route rekordList; // to track best list found
        final Iterator<Route> counter = allCombinations.iterator();
        
        rekordList = counter.next();//set up first List to test
        rekordListTime = rekordList.totalDistance(startPosition, endPosition);//calculate the total Distance between Mushrooms of List
        
        while(counter.hasNext()){   //search the best Route out of all combinations
            final Route currentList = counter.next();
            final int currentListTime = currentList.totalDistance(startPosition, endPosition);//calculate the total Distance between Mushrooms of List
            if(currentListTime < rekordListTime){// checks if new List is a rekord
                rekordList = currentList; // set new rekord time
                rekordListTime = currentListTime; // set new rekord time
            }
        }
        
        rekordList.add(endPosition);
        return rekordList;
    }
    /**
     * returns all possible permutations without duplicates.
     * @param original from Typ Route
     * @return returnValue
     */
    private List<Route> generatePerm(FlexibleRoute original) {
        final List<Route> result = new ArrayList<>(1);
        result.add(new FlexibleRoute3());
        if(original.isEmpty() ) // stop it if original is empty -> all branches checked
            return Collections.unmodifiableList(result);//return empty List<List<>> to stop branch

        final Position firstElement = original.remove(0);
        final List<Route> allPermutation = new LinkedList<>(); 
        
        generatePerm(original) //generate new branches
                .forEach((Route smallerPermutated) -> {
                    for(int index=0; index <= smallerPermutated.size(); index++) {
                        if(smallerPermutated.totalDistance(startPosition, endPosition) <= sortCriteria){ // destroy bad branch
                            final Route permutatedList = new FlexibleRoute3(smallerPermutated);
                            permutatedList.add(index, firstElement);
                            allPermutation.add(permutatedList);
                        }
                    }  
        });
        //System.out.println(allPermutation.size());
        return Collections.unmodifiableList(allPermutation);
    }


}
