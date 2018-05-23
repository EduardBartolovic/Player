package edu.hm.bartolov.se2.miner.player.pathfinder;

import edu.hm.bartolov.se2.miner.player.tool.ImageMapMK2;
import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
/**
 * PathfinderAStar.
 * sucht besten Weg mit a Star.
 * Changelog:
 * 
 * Optimization:
 * 
 * Ideen:
 * ---
 * @author E.Bartolovic
 * @version MK3
 */
public class AStar extends Graph{
    
    /** position startposition. */
    private final Position startPosition;
    /** position endposition. */
    private final Position endPosition;
    /** int value. */
    private final int arenaSize;
    /** priorityQueue. */
    private final PriorityQueue<Node> priorityQueue;
    /** nodes which positions ar already checked. */
    final Set<Node> whereHaveIBeen = new HashSet<>();
    
    
    /**
     * Dijkstra algorithm.
     * @param ruler from typ gamerule
     * @param imageMap from typ imageMap
     * @param startPosition from typ Position
     * @param endPosition  from typ Position
     */
    public AStar(Ruler ruler, ImageMapMK2 imageMap, Position startPosition, Position endPosition) {
        super(imageMap,endPosition);
        if(BoatyMcBoatFaceMK2MovePart.propableDistanceBetweenPositions(startPosition,endPosition)==1)
            throw new IllegalArgumentException("startPosition und endPosition should not be next to each other"+startPosition.getLongitude()+" "+startPosition.getLongitude());
        
        if(startPosition.equals(endPosition))
            throw new IllegalArgumentException("startPosition und endPosition should not be same"+startPosition.getLongitude()+" "+startPosition.getLongitude());
        
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        arenaSize = ruler.getArenaSize();
        priorityQueue = new PriorityQueue<>(1,new SortingQueue());
        setUpListOfNodes();
    }
    /**
     * private void to set up nodes and edges them
     */
    private void setUpListOfNodes(){
        
        //set up all nodes
        for(int yCord = 0;yCord < arenaSize;yCord++){
            for(int xCord = 0;xCord < arenaSize;xCord++){
                getNode( xCord + "|" + yCord ,xCord,yCord);
            }
        }
        //set up all edges
        for(int yCord = 0;yCord < arenaSize;yCord++){
            for(int xCord = 0;xCord < arenaSize;xCord++){
                connections(xCord,yCord);
            }    
        }
    }
    
    /**
     * set up edge and middle connections.
     * @param xCord  from typ int
     * @param yCord  from typ int
     */
    private void connections(int xCord,int yCord){
    	
    	final Node from = getNode( xCord + "|" + yCord ,0,0);
    	
    	if(isCorner(xCord, yCord)){
    		cornerConnections(xCord, yCord);
    	}else if(xCord == 0){
            edgeTo(from,getNode( xCord + "|" + (yCord-1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord-1,xCord,0)));
            edgeTo(from,getNode( xCord + "|" + (yCord+1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord+1,xCord,0)));
            edgeTo(from,getNode( (xCord+1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord+1,0))); 
        }else if(xCord == arenaSize-1){
            edgeTo(from,getNode( xCord + "|" + (yCord-1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord-1,xCord,0)));
            edgeTo(from,getNode( xCord + "|" + (yCord+1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord+1,xCord,0)));
            edgeTo(from,getNode((xCord-1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord-1,0))); 
        }else if(yCord == 0){
            edgeTo(from,getNode( (xCord-1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord-1,0))); 
            edgeTo(from,getNode( xCord + "|" + (yCord+1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord+1,xCord,0)));
            edgeTo(from,getNode((xCord+1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord+1,0))); 
        }else if(yCord == arenaSize-1){
            edgeTo(from,getNode( (xCord-1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord-1,0)));
            edgeTo(from,getNode( xCord + "|" + (yCord-1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord-1,xCord,0)));
            edgeTo(from,getNode( (xCord+1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord+1,0))); 
        }else{
            edgeTo(from,getNode((xCord-1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord-1,0)));
            edgeTo(from,getNode( xCord + "|" + (yCord-1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord-1,xCord,0)));
            edgeTo(from,getNode( xCord + "|" + (yCord+1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord+1,xCord,0)));
            edgeTo(from,getNode((xCord+1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord+1,0))); 
        }
    }
    
    /**
     * set up connections on corners. 
     * @param xCord from typ  int
     * @param yCord from typ int
     */
    private void cornerConnections(int xCord, int yCord){
    	
    	final Node from = getNode( xCord + "|" + yCord,0,0);
    	if(xCord == 0 && yCord == 0){
            edgeTo(from,getNode((xCord+1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord+1,0)));
            edgeTo(from,getNode( xCord + "|" + (yCord+1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord+1,xCord,0)));
    	} else if(xCord == 0 && yCord == arenaSize-1){
            edgeTo(from,getNode( (xCord+1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord+1,0)));
            edgeTo(from,getNode( xCord + "|" + (yCord-1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord-1,xCord,0)));
    	} else if(xCord == arenaSize-1 && yCord == 0){
            edgeTo(from,getNode((xCord-1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord-1,0)));
            edgeTo(from,getNode( xCord + "|" + (yCord+1) ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord+1,xCord,0)));
    	} else {
            edgeTo(from,getNode((xCord-1) + "|" + yCord ,0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord,xCord-1,0)));
            edgeTo(from,getNode( xCord + "|" + (yCord-1),0,0),realTimeCostBetweenCords(new Position(yCord,xCord,0),new Position(yCord-1,xCord,0)));
    	}
    		
    }
    
    /**
     * checks if a coordinate is a corner.
     * @param xCord from typ int
     * @param yCord from typ int
     * @return is point is a corner point
     */
    private boolean isCorner(int xCord, int yCord){
    	return ((xCord == 0 && yCord == 0) || (xCord == 0 && yCord == arenaSize-1) || (xCord == arenaSize-1 && yCord == 0) || (xCord == arenaSize-1 && yCord == arenaSize-1));
    }
    
    /**
     * start Dijkstra.
     * @return List to go.
     */
    public List<Node> search(){
        
    	priorityQueue.add(getStart());
        getStart().setDistanceVia(getStart(),0);
        
        while(!priorityQueue.isEmpty()){
            
            final Node nextNode = priorityQueue.poll();
            if(nextNode.equals(getDestination())){
                    priorityQueue.clear();
            } else {
                    neighbour(nextNode);
                    whereHaveIBeen.add(nextNode);
            }
        }
        
        //reverse from destination with get via to start to get the route as Positions.
        final List<Node> listOfNodes = new ArrayList<>();
        listOfNodes.add(getDestination());
        Node reverseSearchNode = getDestination().getVia();
        listOfNodes.add(reverseSearchNode);
        while(!reverseSearchNode.equals(getStart())){//till start position reached
        	reverseSearchNode = reverseSearchNode.getVia();
        	listOfNodes.add(reverseSearchNode);
        }
        Collections.reverse(listOfNodes);
        return Collections.unmodifiableList(listOfNodes);
    }
    /**
     * takes Node and looks all true its neigbours.
     * @param nextNode  from typ Node
     */
    private void neighbour(Node nextNode){
        
        nextNode.getOutgoing().entrySet().stream().filter((neigboursNode) -> ( !whereHaveIBeen.contains(neigboursNode.getKey()) )).map((neigboursNode) -> {
            final int distance = neigboursNode.getValue();
            if(neigboursNode.getKey().getDistance()> distance + nextNode.getDistance()){
                neigboursNode.getKey().setDistanceVia(nextNode, distance + nextNode.getDistance());
            }
            return neigboursNode;
        }).filter((neigboursNode) -> (!priorityQueue.contains(neigboursNode.getKey()) )).forEachOrdered((neigboursNode) -> {
            priorityQueue.add(neigboursNode.getKey());
        });
        
        /*for(Map.Entry<Node,Integer> neigboursNode: nextNode.getOutgoing().entrySet()){
    		if( !whereHaveIBeen.contains(neigboursNode.getKey()) ){
                    final int distance = neigboursNode.getValue();
                    if(neigboursNode.getKey().getDistance()> distance + nextNode.getDistance()){
                            neigboursNode.getKey().setDistanceVia(nextNode, distance + nextNode.getDistance());
                    }
                    if(!priorityQueue.contains(neigboursNode.getKey()) ){
                        priorityQueue.add(neigboursNode.getKey());
                    }
    		}
        }*/
        
    }
    /**
     * transform List of nodes to direction toMove.
     * @param nodes ListofNodes
     * @return
     */
    public static List<Position> nodesToPositions (List<Node> nodes){
    	final List<Position> nodesAsPositions = new ArrayList<>(nodes.size());
        nodes.forEach((nextNode) -> { // convert nextNode to Position and adds it to List
            nodesAsPositions.add(nextNode.toPosition());
        });
    	return Collections.unmodifiableList(nodesAsPositions);
    }

    @Override
    Node getStart() {
            return getNode( startPosition.getLongitude()+"|"+startPosition.getLatitude() ,0,0);
    }

    @Override
    Node getDestination() {
            return getNode( endPosition.getLongitude()+"|"+endPosition.getLatitude() ,0,0);
    }
 
}