package edu.hm.bartolov.se2.miner.player.common;

import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Edo
 */
public class FlexibleRoute extends ArrayList<Position>  {
    
    public FlexibleRoute(){
        super();
    }
    
    public FlexibleRoute(int size){
        super(size);
    }
    
    public FlexibleRoute(Collection<Position> collection){
        super(collection);
    }
    
    public FlexibleRoute(Position... positions){
        super(positions.length);
        super.addAll(Arrays.asList(positions));
    }
    
    /** Calculate the total Distance of an Route.
     * @param startPosition
     * @param endPosition
     * @return currentDistance in a Route
     */
    public int totalDistance(Position startPosition, Position endPosition){
        if(super.isEmpty())
            return 0;
        
        int currentDistance = distanceBetweenPositions(startPosition,super.get(0));
        for(int counter = 0;counter+1 < super.size();counter++){
            currentDistance += distanceBetweenPositions(super.get(counter),super.get(counter+1));
        }
        currentDistance += distanceBetweenPositions(super.get(super.size()-1), endPosition);
        return currentDistance;
    }
    
    /** Calculate the total Distance of an Route.
     * @return currentDistance in a Route
     */
    public int totalDistance(){
        if(super.isEmpty())
            return 0;
        
        int currentDistance = 0;
        for(int counter = 0;counter+1 < super.size();counter++){
            currentDistance += distanceBetweenPositions(super.get(counter),super.get(counter+1));
        }
        
        return currentDistance;
    }
    
    /** Distance from Position to Position.
     * @param startPosition from Typ Position
     * @param destinationPosition from Typ Position
     * @return distance between points in int of Fields
     */
    public static int distanceBetweenPositions(Position startPosition ,Position destinationPosition){
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
    
    /** Calculate the total Distance of an Route.
     * @param route
     * @param startPosition
     * @param endPosition
     * @return currentDistance in a Route
     */
    public static int totalDistance(Position[] route,Position startPosition, Position endPosition){
        
        if(route.length == 0)
            return 0;
        
        int currentDistance = distanceBetweenPositions(startPosition,route[0]);
        for(int counter = 0;counter+1 < route.length ;counter++){
            currentDistance += distanceBetweenPositions(route[counter],route[counter+1]);
        }
        currentDistance += distanceBetweenPositions(route[route.length - 1], endPosition);
        return currentDistance;
    }




}
