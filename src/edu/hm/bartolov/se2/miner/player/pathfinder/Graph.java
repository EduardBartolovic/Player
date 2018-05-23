package edu.hm.bartolov.se2.miner.player.pathfinder;

import edu.hm.bartolov.se2.miner.player.tool.ImageMapMK2;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Graph.
 * Build the graph construct.
 * Changelog:
 * 
 * Optimization:
 * 
 * Ideen:
 * 
 * ---
 * @author E.Bartolovic 
 * @version MK1
 */
public abstract class Graph{
    
    /** contains all infomations about nodes. */
    private final Map<String,Node> nodes = new HashMap<>();
    /** rules of game. */
    private final ImageMapMK2 imageMap;
    /** position endPositon. */
    private final Position endPosition;
    
    /**
     * custom constructor.
     * @param imageMap from Typ SaveMap
     * @param endPosition
     */
    public Graph(ImageMapMK2 imageMap,Position endPosition){
        this.imageMap=imageMap;
        this.endPosition = endPosition;
    }
    
    /**
     * returns a node with given name.
     * if no Node is in graph make one.
     * @param name from typ String
     * @return node from type node
     */
    Node getNode(String name,int xCord,int yCord){
        Node result = nodes.get(name);
        if(result==null){
            result = new Node(name,xCord,yCord,imageMap.containsInformation(xCord,yCord),BoatyMcBoatFaceMK2MovePart.propableDistanceBetweenPositions(new Position(yCord,xCord,0),endPosition));
            nodes.put(name,result);
        }
        return result;
    }
    
    /**
     * simple getter.
     * @return every node
     */
    Collection<Node> getNodes(){
        return Collections.unmodifiableCollection(nodes.values());
    }
    
    /**
     * makes a directed edge.
     * @param from type String
     * @param to type String
     * @param length type int
     */
    void edgeTo(Node from , Node to , int length){
        if(to.getReachable())
            from.connectTo(to, length);
        
    }
    
    /**
     * makes a undirected edge.
     * @param from type String
     * @param to type String
     * @param length type int
     */
    void edge(Node from , Node to , int length){
        edgeTo(from,to,length);
        edgeTo(to,from,length);
    }
    
    /** Distance from Position to Position.
     * must be directly next to each other.
     * @param startPosition from Typ Position
     * @param destinationPosition from Typ Position
     * @return timeCost between those Positions
     */
    int realTimeCostBetweenCords(Position startPosition ,Position destinationPosition){
        final int timeCost;
        final int startAltitude = imageMap.getImageHeightPosition(startPosition);
        final int destinationAltitude = imageMap.getImageHeightPosition(destinationPosition);
        if(destinationAltitude == -1 || startAltitude == -1)
            return Integer.MAX_VALUE;
        if(startAltitude==destinationAltitude){
            timeCost = 2;
        }else if(startAltitude > destinationAltitude){
            timeCost = 2 + startAltitude-destinationAltitude;
        }else{
            final int heightDifference = startAltitude-destinationAltitude;
            timeCost = 2 + heightDifference * heightDifference;
        }
        return timeCost;
    }
    
    /**
     * print graph on console.
     */
    void printGraph(){
        final Iterator<Node> counter = getNodes().iterator();
        getNodes().size();
        while(counter.hasNext()){
            System.out.println(counter.next());
        }   
    }
    
    /**
     * simple getter.
     * @return Node from type node
     */
    abstract Node getStart();
    
    /**
     * simple getter.
     * @return Node from type node
     */
    abstract Node getDestination();
    
}