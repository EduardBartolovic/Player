package edu.hm.bartolov.se2.miner.player;

import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute2;
import edu.hm.bartolov.se2.miner.player.common.Route;
import edu.hm.bartolov.se2.miner.player.pathfinder.BoatyMcBoatFaceMK2MovePart;
import edu.hm.bartolov.se2.miner.player.tool.ImageMapMK2;
import edu.hm.bartolov.se2.miner.player.tsp.TSPBruteForceIntelligentMK5;
import edu.hm.bartolov.se2.miner.player.tsp.TSPNearestMushroomRouteMK4;
import edu.hm.bartolov.se2.miner.player.tsp.TSPTabuSearch;
import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.player.Player;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * BoatyMcBoatFaceMK3.
 * versucht wege zum pilze sammeln per simulated anneling zu optimieren und nearest Methode.
 * Changelog:
 * Implements Pathfinding algorithm.
 * Ideen:
 * 
 * ---
 * @author E.Bartolovic 
 * @version MK16
 */
public class BoatyMcBoatFaceMK19 implements Player {
    /**
     * how much time is acceptable to calculate the perfect way.
     */
    private static final int MUSHROOMTHRESHOLD = 50;
    /**
     * SpielRegeln.
     */
    private final Ruler ruler;
    
    private final Set<Position> mushrooms ;
    private final int remainingMushrooms;
    private final Position startPosition ;
    private final Position endPosition ;
    /**
     * Map of arena.
     */
    private final ImageMapMK2 map;
    /**
     * Neuer Spieler.
     * @param ruler Die Spielregeln.
     */

    public BoatyMcBoatFaceMK19(Ruler ruler) {
        this.ruler = ruler;
        map = new ImageMapMK2(ruler.getArenaSize());
        mushrooms = ruler.getMushroomsRemaining();
        remainingMushrooms = mushrooms.size();
        startPosition = ruler.getPosition();
        endPosition = ruler.getDestination();
        mushrooms.remove(endPosition); //removing end Positions to reduce workload.
    }

    @Override
    public void run() {
            
        //Different aproach for differnet number of Mushrooms.
        if( remainingMushrooms == 0){ // if no Mushroom on Map#######################
            System.out.println("go direct");
            new BoatyMcBoatFaceMK2MovePart(map,new FlexibleRoute2(endPosition),ruler).move();// find mushrooms an go to Destination
            
        }else  if(remainingMushrooms < 15 ){ // if less than 15 Mushroom on Map#############
            System.out.println("brute Force");
            new BoatyMcBoatFaceMK2MovePart(map,bruteForce(),ruler).move();// find mushrooms an go to Destination
            
        }else  if(remainingMushrooms < 20 ){ // if less than 15 Mushroom on Map#############
            System.out.println("tabuSearch");
            new BoatyMcBoatFaceMK2MovePart(map,tabuSearch(mushrooms.size(),25000),ruler).move();// find mushrooms an go to Destination
            
        }else if(remainingMushrooms > MUSHROOMTHRESHOLD ){
            System.out.println("nearest");
            new BoatyMcBoatFaceMK2MovePart(map,nearestMushRoute(),ruler).move();// find mushrooms an go to Destination
            
        }else{   // if less than MUSHROOMTHRESHOLD Mushroom on map##################
            System.out.println("simulatedAnnealing");
            new BoatyMcBoatFaceMK2MovePart(map,tabuSearch(mushrooms.size(),5000),ruler).move();// find mushrooms an go to Destination
            //new BoatyMcBoatFaceMK2MovePart(map,nearestAndSimulatedAnnealing(),ruler).move();// find mushrooms an go to Destination
            
        }
        
    }
    
    //private-------------------------------
    
//    private Route nearestAndSimulatedAnnealing() {
//        
//        final ExecutorService executor = Executors.newFixedThreadPool(4);
//        final Callable<Route> task = new TSPNearestAndSimulatedAnnealingMK3(startPosition,endPosition,mushrooms);
//        final Future<Route>[] list = new Future[4];
//        for(int i=0; i< 4; i++){
//            //submit Callable tasks to be executed by thread pool
//            list[i] = executor.submit(task);//add Future to the array
//        }
//        
//        Route best = null;
//        
//        try {
//            int bestTime = Integer.MAX_VALUE;
//            for(Future<Route> route : list){ 
//                final Route currentList = route.get(); // getting route from Thread
//                int time = currentList.totalDistance(startPosition, endPosition);
//                if(time < bestTime){
//                    bestTime = time;
//                    best = currentList;
//                }
//            }
//            
//        } catch (InterruptedException | ExecutionException ex) {
//           throw new IllegalStateException();
//        }
//        
//        executor.shutdown();
//        return best;    
//    }
    
    /**
     * use brute Force to find the Best Route.
     */
    private Route bruteForce(){
        final int sortCriteria = new TSPNearestMushroomRouteMK4(startPosition,endPosition,mushrooms).getRoute().totalDistance(ruler.getPosition(), ruler.getDestination());        
        //return new TSPIntelligentBruteForce(mushrooms, startPosition, endPosition).getRoute();
        return new TSPBruteForceIntelligentMK5(startPosition,endPosition,mushrooms,sortCriteria).getRoute(); 
    }
    
    /**
     * use nearest Methode when too many Mushrooms.
     */
    private Route nearestMushRoute(){
        return new TSPNearestMushroomRouteMK4(startPosition,endPosition,mushrooms).getRoute();
    }
    
    private Route tabuSearch(int radius, int iterations){
        final ExecutorService executor = Executors.newFixedThreadPool(4);
        final Callable<Route> task = new TSPTabuSearch(startPosition, endPosition,mushrooms,radius,iterations);
        final Future<Route>[] list = new Future[4];
        for(int i=0; i< 4; i++){
            //submit Callable tasks to be executed by thread pool
            list[i] = executor.submit(task);//add Future to the array
        }
        
        Route best = null;
        
        try {
            int bestTime = Integer.MAX_VALUE;
            for(Future<Route> route : list){ 
                final Route currentList = route.get(); // getting route from Thread
                int time = currentList.totalDistance(startPosition, endPosition);
                if(time < bestTime){
                    bestTime = time;
                    best = currentList;
                }
            }
            
        } catch (InterruptedException | ExecutionException ex) {
           throw new IllegalStateException();
        }
        
        executor.shutdown();
        return best;    
    }

}