package edu.hm.bartolov.se2.miner.player.tsp_not_used;

import edu.hm.bartolov.se2.miner.player.pathfinder.BoatyMcBoatFaceMK2MovePart;
import edu.hm.cs.rs.se2.miner.common.Direction;
import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * RouteMK2.
 * versucht wege zum pilze sammeln per simulated anneling zu optimieren und nearest Methode.
 * Changelog:
 * latitudecheck() longitudecheck() now splited
 * Optimization:
 * 
 * Ideen:
 * ---
 * @author E.Bartolovic 
 * @version MK2
 */
abstract class RouteMK2 {
    /**
    * ruler is ruler of Game.
    */
    private final Ruler ruler;
    /**
     * Custom Constructor.
     * @param ruler 
     */
    RouteMK2(Ruler ruler){
        this.ruler=ruler;
    }
    
    /** get the best Route.
     * 
     * @return best Route to Pick mushrooms and destination.
     */
    public abstract List<Position> getRoute();

    
    
    /** Calculate the total Distance of an Route. 
     * @param currentSolution from Typ List<Position>
     * @return currentDistance in a Route
     */
    protected int totalDistance(List<Position> currentSolution){
        int currentDistance = distanceBetweenPositions(ruler.getPosition(),currentSolution.get(0));
        for(int counter = 0;counter+1 < currentSolution.size();counter++){
             final Position position1 = currentSolution.get(counter);
             final Position position2 = currentSolution.get(counter+1);
             currentDistance += distanceBetweenPositions(position1,position2);
         }
        return currentDistance;
    }
    /** Distance from Position to Position.
     * @param startPosition from Typ Position
     * @param destinationPosition from Typ Position
     * @return distance between points in int of Fields
     */
    protected int distanceBetweenPositions(Position startPosition ,Position destinationPosition){
        final int startLatitude = startPosition.getLatitude();
        final int startLongitude = startPosition.getLongitude();
        final int destinationLatitude = destinationPosition.getLatitude();
        final int destinationLongitude = destinationPosition.getLongitude();
        final int latitudeToRun = Math.abs(startLatitude-destinationLatitude);
        final int longitudeToRun = Math.abs(startLongitude-destinationLongitude);
        final int distance = longitudeToRun+latitudeToRun;
        
        if(distance<0)
            throw new java.lang.ArithmeticException("distance should not negativ");
        
        return distance;
    }
    /** testing for nearestMethode.
     * @param positionToTest from Typ Position
     * @param rekordPosition from Typ Position
     * @param latitudeOrLongitude decide if longitude check or latiude check is active.
     * @return isNewMushbetter as boolean to say if a new Mushroom better to pick.
     */
    private boolean checkMushroomPositionPick(Position positionToTest, Position rekordPosition,boolean latitudeOrLongitude){
        final boolean isNewMushbetter;
        final int newDistanceToEnd = distanceBetweenPositions(positionToTest,ruler.getDestination());
        final int shortestDistanceToEnd = distanceBetweenPositions(rekordPosition,ruler.getDestination());
        
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
        if(BoatyMcBoatFaceMK2MovePart.whichDirectionToMoveLatitude(ruler.getPosition(),ruler.getDestination())==Direction.N){
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
        if(BoatyMcBoatFaceMK2MovePart.whichDirectionToMoveLongitude(ruler.getPosition(),ruler.getDestination())==Direction.W){
            isNewbetterMush = positionToTest.getLongitude()>rekordPosition.getLongitude();    
        }else{
            isNewbetterMush = positionToTest.getLongitude()<rekordPosition.getLongitude(); 
        }
        return isNewbetterMush;
    }
    
    /**Search the nearest Mushroom from startPosition. 
     * @param positionToStart from Typ Position 
     * @param hypoRemainingMush list of Mushrooms which are not hypotetical picked up
     * @param latitudeOrLongitude decide if longitude check or latiude check is active.
     * @return nearestMush typ Position
     * @trows NullpointerException if no Mushrooms left in hypoRemainingMush.
     */
    protected Position nearMush(Position positionToStart,Set<Position> hypoRemainingMush,boolean latitudeOrLongitude){
        
        Position nearestMush;
        int shortestDistance;// Rekord Distance
        
        final Iterator<Position> counter = hypoRemainingMush.iterator();
        
        if(counter.hasNext()){// set first minimum Distance
            final Position positionToTest = counter.next();
            nearestMush = positionToTest;
            shortestDistance= distanceBetweenPositions(positionToStart,positionToTest);
        }else{ //cannot happen:
            nearestMush = null;
            shortestDistance = 0;
        }
            
        while(counter.hasNext()){//search for nearest mushroom
            final Position positionToTest = counter.next();
            final int newDistanceToRun = distanceBetweenPositions(positionToStart,positionToTest);
            
            if(newDistanceToRun < shortestDistance){// if new distance is a rekord
                shortestDistance = newDistanceToRun;
                nearestMush = positionToTest;
            }
            
            //if to mushrooms have the same distance to startPos than go to more distance to destination
            if(newDistanceToRun == shortestDistance && checkMushroomPositionPick(positionToTest,nearestMush,latitudeOrLongitude)){//newDistanceToEnd>shortestDistanceToEnd){
                shortestDistance = newDistanceToRun;
                nearestMush = positionToTest;
            }
        }
        if(nearestMush==null)//Assert
            throw new java.lang.NullPointerException("no Mushrooms on Map");
        return nearestMush;
    }
    /**
     * Look where the Start Section is.
     * @param positionToTest from Typ Position.
     * @return startSection[] filled with Direction N/S in frist place an W/E in second placer
     *//*
    Direction[] whichSection(Position positionToTest){    
        final int halfArenaSize = ruler.getArenaSize()/2;
        final Direction[] startSection = new Direction[2];
        if(positionToTest.getLatitude() > halfArenaSize){
            startSection[0] = Direction.N;
        }else{
            startSection[0] = Direction.S;
        }
        if(positionToTest.getLongitude() > halfArenaSize){
            startSection[1] = Direction.E;
        }else{
            startSection[1] = Direction.W;
        }
        return startSection;
    }
    /**
     * Split mushrooms in sections.
     * @param section from type Direction[]
     * @return mushroomInSection Set<Position>
     *//*
    Set<Position> mushroomInSection(Direction[] section){
        final Iterator<Position> counter = ruler.getMushroomsRemaining().iterator();
        final Set<Position> mushroomPlacedInSection = new HashSet<>();
        while(counter.hasNext()){
            final Position currentMush = counter.next();
            final Direction[] currentSection = whichSection(currentMush);
            if(currentSection[0]==section[0] && currentSection[1]==section[1]){
                mushroomPlacedInSection.add(currentMush);
            }
        }
        return mushroomPlacedInSection;
    }*/
    
    
}
