package edu.hm.bartolov.se2.miner.player.pathfinder;

import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute;
import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute2;
import edu.hm.bartolov.se2.miner.player.common.Route;
import edu.hm.bartolov.se2.miner.player.tool.ImageMapMK2;
import edu.hm.cs.rs.se2.miner.common.Direction;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Iterator;

/**
 * BoatyMcBoatFaceMK2MovePart.
 * Version2.
 * get a List which moves Player thrue all Positions of List.
 * ---
 * @author E.Bartolovic 
 * @version MK2
 */
public class BoatyMcBoatFaceMK2MovePart{
    /**
     * map of game.
     */
    private final ImageMapMK2 map;
    /**
     * which Route to take.
     */
    private final Route route;
    /**
     * SpielRegeln.
     */
    private final Ruler ruler;
    //private final IntermediateObjektiv intermediateObjektivFinder;
    
    public BoatyMcBoatFaceMK2MovePart(ImageMapMK2 map, Route routeToGo ,Ruler ruler){
        this.map = map;
        route = routeToGo;
        this.ruler = ruler;
        //intermediateObjektivFinder = new IntermediateObjektiv(map);
    }
    
    public BoatyMcBoatFaceMK2MovePart(ImageMapMK2 map, FlexibleRoute routeToGo ,Ruler ruler){
        this.map = map;
        route = new FlexibleRoute2(routeToGo);
        this.ruler = ruler;
        //intermediateObjektivFinder = new IntermediateObjektiv(map);
    }
    
    /** goes to all Positions in route.
     */
    public void move(){
        final Iterator<Position> curser = route.iterator();
        Position lastPosition = ruler.getPosition();
        while(curser.hasNext()){          
            final Position nextPosition = curser.next();
            goToPosition(lastPosition,nextPosition);
            lastPosition = nextPosition;
        } 
        //map.printImageMap();///++++++++++++++++++++++++++++++++++++++++
    }
    /**
     * go from to given Posiition.
     * @param lastPosition from typ Position 
     * @param nextPosition from typ Position 
     */
    private void goToPosition(Position lastPosition,Position nextPosition){
        runToDestination(nextPosition); 
//        while(!lastPosition.equals(nextPosition)){
//            if(!map.wasPictureTakenOnPosition(nextPosition))//if on Position already a picture was taken then skip takeRadarImage()
//                map.fillMap(ruler.takeRadarImage(), lastPosition);
//            map.printImageMap();
//             find a predestination in known area
//            final Position preDestination = intermediateObjektivFinder.getIntermediateObjektiv(lastPosition,nextPosition);//preDestiantionFinder(lastPosition,nextPosition);//best Position inside known area to destination
//            System.out.println(preDestination.getLatitude()+" "+preDestination.getLongitude());
//            if(!lastPosition.equals(preDestination)){
//                if(propableDistanceBetweenPositions(lastPosition,preDestination)==1){
//                    final List<Position> routePrecise = new ArrayList<>();
//                    routePrecise.add(preDestination);
//                    moveListOfPosition(routePrecise);
//                }else{
//                    final List<Node> routePrecise = new AStar(ruler,map,lastPosition,preDestination).search();
//                    moveListOfPosition(AStar.nodesToPositions(routePrecise));
//                }
//            }
//           lastPosition = ruler.getPosition();
//        }
        
    }
    /** goes to all Positions in given List.
     * @param route route to move
     */
    private void moveListOfPosition( FlexibleRoute route){
        
        route.forEach((nextPosition) -> {
            nextRunToDestination(nextPosition);
        });
        
    }
    
    /**
     * run to Position wich is next to player.
     * @param position from Type Position.
     */
    private void nextRunToDestination(Position position){
        
        final Position positionOfPlayer = ruler.getPosition();
        final int playerLatitude = positionOfPlayer.getLatitude(); // getting Player Latitude
        final int playerLongitude = positionOfPlayer.getLongitude(); // getting Player Longitude
        final int destinationLatitude = position.getLatitude(); 
        final int destinationLongitude = position.getLongitude();
        final int latitudeToRun = destinationLatitude-playerLatitude;//how much to move in latitude
        final int longitudeToRun = destinationLongitude-playerLongitude;//how much to move in longitude
        if(latitudeToRun!=0){
            ruler.move(whichDirectionToMoveLatitude(positionOfPlayer,position));
        }
        if(longitudeToRun!=0){
            ruler.move(whichDirectionToMoveLongitude(positionOfPlayer,position));
        }
        
    }
    
    /** Player will run stupid to given Position.
     * @param position from Typ Position
     */
    private void runToDestination(Position position){
        final Position positionOfPlayer = ruler.getPosition();
        final int playerLatitude = positionOfPlayer.getLatitude(); // getting Player Latitude
        final int playerLongitude = positionOfPlayer.getLongitude(); // getting Player Longitude
        final int destinationLatitude = position.getLatitude(); 
        final int destinationLongitude = position.getLongitude();
        final int latitudeToRun = destinationLatitude-playerLatitude;//how much to move in latitude
        final int longitudeToRun = destinationLongitude-playerLongitude;//how much to move in longitude
        
        
        for(int counter = 0;counter < Math.abs(latitudeToRun);counter++){//move Player in Latitude
            ruler.move(whichDirectionToMoveLatitude(ruler.getPosition(),position));
        }
        for(int counter = 0;counter < Math.abs(longitudeToRun);counter++){//move player in Longitude
            ruler.move(whichDirectionToMoveLongitude(ruler.getPosition(),position));
        }
        
    }
    
    /**
     * Return the Direction to Run.
     * @param startPosition from Typ Position
     * @param endPosition from Typ Position
     * @return directionToRun North or South
     */
    public static Direction whichDirectionToMoveLatitude(Position startPosition, Position endPosition){
        final int playerLatitude = startPosition.getLatitude(); // getting Player Latitude
        final int destinationLatitude = endPosition.getLatitude(); 
        final int latitudeToRun = destinationLatitude-playerLatitude;//how much to move in latitude
        
        final Direction directionToRun;
        if(latitudeToRun<0)//which direction Latitude
            directionToRun = Direction.S;
        else
            directionToRun = Direction.N;
        
        return directionToRun;
    }
    
    /**
     * Return the Direction to Run.
     * @param startPosition from Typ Position 
     * @param endPosition from Typ Position
     * @return directionToRun Est or West
     */
    public static Direction whichDirectionToMoveLongitude(Position startPosition, Position endPosition){
        final int playerLongitude = startPosition.getLongitude(); // getting Player Longitude
        final int destinationLongitude = endPosition.getLongitude();
        final int longitudeToRun = destinationLongitude-playerLongitude;//how much to move in longitude

        final Direction directionToRun;
        if(longitudeToRun<0)//which direction Longitude
            directionToRun = Direction.W;
        else
            directionToRun = Direction.E;
        
        return directionToRun;
    }
    
    /*public static Direction whichDirectionToMove(Position startPosition, Position endPosition){

        final int playerLatitude = startPosition.getLatitude(); // getting Player Latitude
        final int playerLongitude = startPosition.getLongitude(); // getting Player Longitude
        final int destinationLatitude = endPosition.getLatitude(); 
        final int destinationLongitude = endPosition.getLongitude();
        final int latitudeToRun = destinationLatitude-playerLatitude;//how much to move in latitude
        final int longitudeToRun = destinationLongitude-playerLongitude;//how much to move in longitude
        
        final Direction directionToRun;
        if(longitudeToRun != 0){
            
            if(longitudeToRun<0)//which direction Longitude
                directionToRun = Direction.W;
            else
                directionToRun = Direction.E;
            
        }else if(latitudeToRun != 0){
            
            if(latitudeToRun<0)//which direction Latitude
                directionToRun = Direction.S;
            else
                directionToRun = Direction.N;
        
        }else{
            throw new ArithmeticException("No Dircetion to Move");
        }
        
        
        return directionToRun;
        
    }*/
        
        
    /** Distance from Position to Position.
     * @param startPosition from Typ Position
     * @param destinationPosition from Typ Position
     * @return distance between points in int of Fields
     */
    public static int propableDistanceBetweenPositions(Position startPosition ,Position destinationPosition){
        final int startLatitude = startPosition.getLatitude();
        final int startLongitude = startPosition.getLongitude();
        final int destinationLatitude = destinationPosition.getLatitude();
        final int destinationLongitude = destinationPosition.getLongitude();
        final int latitudeToRun = Math.abs(startLatitude-destinationLatitude);
        final int longitudeToRun = Math.abs(startLongitude-destinationLongitude);
        final int distance = longitudeToRun+latitudeToRun;
        assert distance >= 0: "distance should not negativ";
      
        return distance;
    }
    

}
