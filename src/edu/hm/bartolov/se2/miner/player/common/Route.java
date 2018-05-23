
package edu.hm.bartolov.se2.miner.player.common;

import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.List;

/**
 *
 * @author Edo
 */
public interface Route extends List<Position> {
    
    int totalDistance(Position startPosition, Position endPosition);
    
    int totalDistance();
    
    /** Distance from Position to Position.
     * @param startPosition from Typ Position
     * @param destinationPosition from Typ Position
     * @return distance between points in int of Fields
     */
    static int distanceBetweenPositions(Position startPosition ,Position destinationPosition){
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
    static int totalDistance(Position[] route ,Position startPosition, Position endPosition){
        
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
