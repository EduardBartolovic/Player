package edu.hm.bartolov.se2.miner.player.tsp;

import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute3;
import edu.hm.bartolov.se2.miner.player.common.Route;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
/**
 * NearestAndSiulatesAnnealingMK1
 * versucht wege zu pilzen per simulated anneling zu optimieren und nearest Methode zur sicherheit.

 * @author E.Bartolovic 
 * @version MK13
 */

public class TSPSimulatedAnnealingMK3 implements TSP, Callable<Route>{
    /**
     * cooling rate for simulated anneling.
     */
    private static final double COOLINGRATE = 0.9;
    /**
     * how much time is acceptable to calculate the perfect way.
     */
    private static final int MUSHROOMBORDER = 20;
    /**
     * starting Temperatur for simulated anneling.
     */
    private final int startFrigidity ;
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
    
   /**
    * 
    */
    private final int iterations;
    
    /**
     * Constructor.
     * @param startPosition
     * @param endPosition
     * @param remainingMushrooms from Typ Set of Position
     */
    public TSPSimulatedAnnealingMK3(Position startPosition, Position endPosition,Set<Position> remainingMushrooms){
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.iterations = remainingMushrooms.size()*2000;
        
        if(remainingMushrooms.size()>MUSHROOMBORDER)
            startFrigidity = 1000;
        else
            startFrigidity = 100;    
        this.remainingMushrooms = remainingMushrooms;
    }
    
    @Override
    public Route getRoute(){
        
        //nearest Methode Route is the best Route till something better is found        
        Route bestWayToGo = new FlexibleRoute3(new TSPNearestMushroomRouteMK4(startPosition,endPosition,remainingMushrooms).getRoute());
        
        int rekordDistance = bestWayToGo.totalDistance(startPosition, endPosition);
        
        for(int counter = iterations ; counter > 0 ; counter--){  // search till bigger than timelimit with simulated annealing 
            final Route routeTry = simulatedAnnealingRun(bestWayToGo);
            final int newDistance = routeTry.totalDistance(startPosition, endPosition);
            if(newDistance < rekordDistance){// if new rekord make tested as rekord
                rekordDistance = newDistance;
                bestWayToGo = routeTry;
            }
        }
        
        bestWayToGo.add(endPosition);
        return bestWayToGo;
    }
    
    /** Calculates the best Route to pick Mushrooms and Destiantion.
     * with simulated annealing.
     * @param remainingPositions from Type Route
     * @return Route the best way to walk is a unmodifiableList
     */
    private Route simulatedAnnealingRun(Route remainingPositions) {
        // Create and add Positions
        Route currentSolution =  new FlexibleRoute3(remainingPositions); // current Solution is random given remaining Mushroooms and as Last one Destinantion
        
        //simulates anneling start varibles
        int frigidity = startFrigidity; //Set start temperatur for System

        Route best = currentSolution;// keep track for the best solution
        int bestTime = best.totalDistance(startPosition, endPosition);
        
        final int numberRange = best.size();  //for the range in ehich random numbers should be generated
        final Random randomNumber = new Random(); 
        
        // Loop until system has cooled off
        while(frigidity > 1){
            // Create new random neighbour Solution
            final Route newSolution = new FlexibleRoute3(currentSolution);
            
            // Get random positions in the tour without touching the last Position
            final int tourPos1 = randomNumber.nextInt(numberRange); // generate Random Number in NumberRange
            int tourPos2 = randomNumber.nextInt(numberRange);// generate Random Number in NumberRange
            while(tourPos1 == tourPos2 ){//to make sure that tourPos1 and tourPos2 are different
                tourPos2 = randomNumber.nextInt(numberRange); 
            }

            //Get the Mushrooms at selected positions in the Route only last can not be moved(Destination)
            final Position mushSwap1 = newSolution.get(tourPos1);
            final Position mushSwap2 = newSolution.get(tourPos2);
            // Swap them
            newSolution.set(tourPos2, mushSwap1);
            newSolution.set(tourPos1, mushSwap2);
            
            // Get total Distance of solutions
            final int currentDistance = currentSolution.totalDistance(startPosition, endPosition);
            final int neighbourDistance = newSolution.totalDistance(startPosition, endPosition);

            // Remember the best solution if better than rekord
            if( currentDistance < bestTime) {
                best = currentSolution;
                bestTime = currentDistance;
            }
            
            // Decide if we should accept the neighbour Solution
            final double randomAcceptance = randomNumber.nextInt(1000)/1000.0;// generate Random acceptance
            if(permissionTest(currentDistance, neighbourDistance , frigidity ) > randomAcceptance) {
                currentSolution = newSolution;// fill old currentSolution with new values
            }

            
            frigidity *= COOLINGRATE;//-= COOLINGRATE; // Cool system
        }       
        return best;
    }
    //private----------------
    
    /** Calculate the probabilityvalue. 
     * @param currentDistance from Typ int from old Distance
     * @param newDistance from typ int distance of new Tour
     * @param frigidity from system
     * @return acceptanceProbability which discricbes how likely it should be to accept a Differnece.
     */
    private double permissionTest(int currentDistance, int newDistance, double frigidity) {
        
	final double acceptanceProbability;	
        // If the new solution is better, accept it
        if (newDistance < currentDistance) {
            acceptanceProbability = Double.MAX_VALUE;
        }else{
        // If the new solution is worse, calculate an acceptance probability
            acceptanceProbability = Math.exp((currentDistance - newDistance) / frigidity);
        }
        return acceptanceProbability;    
    }

    @Override
    public Route call() throws Exception {
        return getRoute();
    }
    
}

