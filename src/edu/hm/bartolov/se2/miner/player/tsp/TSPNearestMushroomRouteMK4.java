package edu.hm.bartolov.se2.miner.player.tsp;


import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute;
import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute3;
import edu.hm.bartolov.se2.miner.player.common.Route;
import edu.hm.bartolov.se2.miner.player.pathfinder.BoatyMcBoatFaceMK2MovePart;
import edu.hm.cs.rs.se2.miner.common.Direction;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.HashSet;
import java.util.Set;


/**
 * NearestMushroomRouteMK1
 * search the Route from nearst mushroom to nearst mushroom. 
 * inlclusiv Destination. 
 * Changelog:
 * now checks for Latitude and Longitude Position compared to Destination.
 * ---
 * @author E.Bartolovic 
 * @version MK1
 */
public class TSPNearestMushroomRouteMK4 implements TSP {
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
     * Constructor.
     * @param startPosition
     * @param endPosition
     * @param remainingMushrooms
     */
    public TSPNearestMushroomRouteMK4(Position startPosition,Position endPosition, Set<Position> remainingMushrooms){
        this.startPosition=startPosition;
        this.endPosition=endPosition;
        this.remainingMushrooms = remainingMushrooms;
    } 
    
    @Override
    public Route getRoute(){
        
        //with latitudecheck
        final Set<Position> hypoRemainingMush = new HashSet<>(remainingMushrooms);// remaining mushrooms hypotetical
        
        final Route nearestMushTourLatitude = new FlexibleRoute3(remainingMushrooms.size()); // return List for Route
        Position lastPosition = startPosition; // start Position
        //Iterate thrue hypoRemainingMush to find nearest Mushroom
        while(!hypoRemainingMush.isEmpty()){
            final Position newPosition = nearMush(lastPosition,hypoRemainingMush,true);//false for longitude
            lastPosition = newPosition;
            hypoRemainingMush.remove(newPosition);
            nearestMushTourLatitude.add(newPosition);
        }
        nearestMushTourLatitude.remove(endPosition);
        
        //with longitudecheck
        hypoRemainingMush.addAll(remainingMushrooms);// fill again up with all Mushroom Positions)
        final Route nearestMushTourLongitude = new FlexibleRoute3(remainingMushrooms.size()); // return List for Route
        lastPosition = startPosition; // again set up start Position
        //Iterate thrue hypoRemainingMush to find nearest Mushroom
        while(!hypoRemainingMush.isEmpty()){
            final Position newPosition = nearMush(lastPosition,hypoRemainingMush,false);//false for longitude
            lastPosition = newPosition;
            hypoRemainingMush.remove(newPosition);
            nearestMushTourLongitude.add(newPosition);
        }
        nearestMushTourLongitude.remove(endPosition);
        
        
        //Compare the best Result and return it. 
        final Route nearestMushTour;
        if(nearestMushTourLongitude.totalDistance(startPosition, endPosition) < nearestMushTourLatitude.totalDistance(startPosition, endPosition)){
            nearestMushTour = nearestMushTourLongitude;
        }else{
            nearestMushTour = nearestMushTourLatitude;
        }
        nearestMushTour.add(endPosition);
        return nearestMushTour;
    }
    
    /**Search the nearest Mushroom from startPosition.
     * @param positionToStart from Typ Position
     * @param hypoRemainingMush list of Mushrooms which are not hypotetical picked up
     * @param latitudeOrLongitude decide if longitude check or latiude check is active.
     * @return nearestMush typ Position
     * @trows NullpointerException if no Mushrooms left in hypoRemainingMush.
     */
    protected Position nearMush(Position positionToStart, Set<Position> hypoRemainingMush, boolean latitudeOrLongitude){

        Position nearestMush = null;
        int shortestDistance = Integer.MAX_VALUE;// Rekord Distance

        for(Position positionToTest: hypoRemainingMush){
            final int newDistanceToRun = FlexibleRoute.distanceBetweenPositions(positionToStart,positionToTest);

            if(newDistanceToRun < shortestDistance){// if new distance is a rekord
                shortestDistance = newDistanceToRun;
                nearestMush = positionToTest;
            }
            //if to mushrooms have the same distance to startPos than go to more distance to destination
            else if(newDistanceToRun == shortestDistance && checkMushroomPositionPick(positionToTest,nearestMush,latitudeOrLongitude)){
                shortestDistance = newDistanceToRun;
                nearestMush = positionToTest;
            }
        }
        
        return nearestMush;
    }
    
    /** testing for nearestMethode.
     * @param positionToTest from Typ Position
     * @param rekordPosition from Typ Position
     * @param latitudeOrLongitude decide if longitude check or latiude check is active.
     * @return isNewMushbetter as boolean to say if a new Mushroom better to pick.
     */
    private boolean checkMushroomPositionPick(Position positionToTest, Position rekordPosition,boolean latitudeOrLongitude){
        final boolean isNewMushbetter;
        final int newDistanceToEnd = FlexibleRoute.distanceBetweenPositions(positionToTest,endPosition);
        final int shortestDistanceToEnd = FlexibleRoute.distanceBetweenPositions(rekordPosition,endPosition);

        if(newDistanceToEnd>shortestDistanceToEnd){
            isNewMushbetter = true;
        }else if(newDistanceToEnd==shortestDistanceToEnd){
            if(latitudeOrLongitude){
                isNewMushbetter = latitudeCheck(positionToTest,rekordPosition);
            }else{
                isNewMushbetter = longitudeCheck(positionToTest,rekordPosition);
            }
        }else{
            isNewMushbetter = false;
        }
        return isNewMushbetter;
    }
    /** looks for position of Destination to pick better mush.
     * @param positionToTest from Typ Position
     * @param rekordPosition from Typ Position
     * @return isNewbetterMush true if newer mush is better to pick
     */
    boolean latitudeCheck(Position positionToTest,Position rekordPosition){
        final boolean isNewbetterMush;
        if(BoatyMcBoatFaceMK2MovePart.whichDirectionToMoveLatitude(startPosition,endPosition)== Direction.N){
            isNewbetterMush = positionToTest.getLatitude()<rekordPosition.getLatitude();
        }else{
            isNewbetterMush = positionToTest.getLatitude()>rekordPosition.getLatitude();
        }
        return isNewbetterMush;
    }
    /** looks for position of Destination to pick better mush.
     * @param positionToTest from Typ Position
     * @param rekordPosition from Typ Position
     * @return isNewbetterMush true if newer mush is better to pick
     */
    boolean longitudeCheck(Position positionToTest,Position rekordPosition){
        final boolean isNewbetterMush;
        if(BoatyMcBoatFaceMK2MovePart.whichDirectionToMoveLongitude(startPosition,endPosition)==Direction.W){
            isNewbetterMush = positionToTest.getLongitude()>rekordPosition.getLongitude();
        }else{
            isNewbetterMush = positionToTest.getLongitude()<rekordPosition.getLongitude();
        }
        return isNewbetterMush;
    }



}
