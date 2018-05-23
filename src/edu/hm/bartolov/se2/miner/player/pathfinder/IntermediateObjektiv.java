package edu.hm.bartolov.se2.miner.player.pathfinder;

import static edu.hm.bartolov.se2.miner.player.pathfinder.BoatyMcBoatFaceMK2MovePart.propableDistanceBetweenPositions;
import edu.hm.bartolov.se2.miner.player.tool.ImageMapMK2;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * BoatyMcBoatFaceMK2MovePart.
 * Version2.
 * get a List which moves Player thrue all Positions of List.
 * ---
 * @author E.Bartolovic 
 * @version MK2
 */
public class IntermediateObjektiv {
    /** list of Predsetinations**/
    private final List<Position> predestinations;
    /** reference to map **/
    private final ImageMapMK2 map;
    /** size of arena **/
    private final int arenaSize;
    
    /**
     * size of arena.
     * @param map from typ ImageMap
     */
    IntermediateObjektiv(ImageMapMK2 map){
        this.map=map;
        predestinations = new ArrayList<>();
        arenaSize = map.getImageSize();
    }
    
    /**
     * Best Position to aim for.
     * @param playerPosition from type Position
     * @param nextDestination from type Position
     * @return prePosition from typ Position
     */
    public Position getIntermediateObjektiv(Position playerPosition, Position nextDestination){
        Position rekordPosition = new Position(0,0,0);
        int rekordDistance = Integer.MAX_VALUE;
        
        //go thrue map an search the nearest Position to the destination 
        for(int latitudeCounter = 0 ;latitudeCounter<arenaSize;latitudeCounter++){
            for(int longitudeCounter = 0 ;longitudeCounter<arenaSize;longitudeCounter++){
                if(map.containsInformation(longitudeCounter,latitudeCounter)){
                    final Position positionToTest = new Position(latitudeCounter,longitudeCounter,0);
                    final int newDistance = propableDistanceBetweenPositions(positionToTest,nextDestination);//heightDifferenceCost(positionToTest,nextDestination);
                    if(newDistance<rekordDistance){//++++++optimization needed
                        rekordPosition = positionToTest;
                        rekordDistance = newDistance;
                    }
                }
            }
        }
        predestinations.add(rekordPosition);
        return rekordPosition;
    }
    
    /**
     * Errechnet die anzahl an Zeit die fuer eine Bewegung benoetigt werden.
     * @param oldPosition Position, alte Position des Spielers.
     * @param newPosition Position, neue Position des Spielers.
     * @return Ticks, anzahl der Ticks die fuer eine Bewegung benoetigt werden.
     */
    /*private int heightDifferenceCost(Position oldPosition, Position newPosition){
        //Altitude difference
        final int newAltitude = newPosition.getAltitude();
        final int oldAltitude = oldPosition.getAltitude();
        final int altitudeDifference = newAltitude - oldAltitude;
        
        final int cost;// up or down or straigth
        if(oldAltitude > newAltitude)
            cost = Math.abs(altitudeDifference) + 2;
        else if(oldAltitude < newAltitude)
            cost = altitudeDifference*altitudeDifference + 2;
        else 
            cost = 2;
        return cost;
    }*/
}
