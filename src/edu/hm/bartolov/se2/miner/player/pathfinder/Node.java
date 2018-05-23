package edu.hm.bartolov.se2.miner.player.pathfinder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import edu.hm.cs.rs.se2.miner.common.Position;

/**
 * Objekt Node.
 * Changelog:
 * 
 * Optimization:
 * 
 * Ideen:
 * remove String.
 * ---
 * @author E.Bartolovic 
 * @version MK1
 */
public class Node implements Comparable<Node>{
    /**distance To Start. because in beginning unknown Integrer.MAX . **/
    private int distance = Integer.MAX_VALUE;
    /** xCord of Node. **/
    private final int xCord;
    /** yCord of Node. **/
    private final int yCord;
    /** Node before this Node. **/
    private Node via = null;
    /** Name of Node. **/
    private final String name;
    /** Outgoing Connection of this Node. **/
    private final Map<Node,Integer> outgoing = new HashMap<>();
    /** To Say if Node is rechable. **/
    private final boolean reachable;
    /** heuristic Distance To End. **/
    private final int heuristicDistanceToEnd;
    
    /**
     * Custom Constructor.
     * @param name Name of Node.
     * @param xCord xCord of Node.
     * @param yCord yCord of Node.
     * @param reachable To Say if Node is rechable
     * @param heuristicDistnaceToEnd heuristic Distance To End. 
     */
    public Node(String name,int xCord , int yCord,boolean reachable,int heuristicDistnaceToEnd){
        this.name = name;
        this.xCord = xCord;
        this.yCord = yCord;
        this.reachable= reachable;
        this.heuristicDistanceToEnd = heuristicDistnaceToEnd;
    }
    
    /**
     * simple getter.
     * @return name as string
     */
    String getName(){
        return name;
    }
    
    /**
     * simple getter.
     * @return xCord as int
     */
    int getxCord(){
        return xCord;
    }
    
    /**
     * simple getter.
     * @return nyCord as int
     */
    int getyCord(){
        return yCord;
    }
    
    /**
     * simple getter.
     * @return distance as int
     */
    int getDistance(){
        return distance;
    }
    
    /**
     * simple getter to see if node is reachable.
     * @return reachable from type boolean
     */
    boolean getReachable(){
        return reachable;
    }
    
    /**
     * simple getter.
     * @return heuristicDistnaceToEnd from type int
     */
    int getHeuristicDistanceToEnd(){
        return heuristicDistanceToEnd;
    }

    /**
     * simple getter.
     * @return via as node
     */
    Node getVia(){
        return via;
    }
    
    /**
     * set a distance.
     * @param via from typ node
     * @param distance from type int
     */
    void setDistanceVia(Node via , int distance){
        this.via = via;
        this.distance = distance;
    }
    
    /**
     * set a connection.
     * @param node  from type node
     * @param length from type int
     */
    void connectTo(Node node,int length ){
        outgoing.put(node,length);
    }
    
    /**
     * simple getter.
     * @return outgoing connections
     */
    Map<Node,Integer> getOutgoing(){
        return Collections.unmodifiableMap(outgoing);
    }
    
    /**
     * Convert this Node to Position.
     * @return Position.
     */
    Position toPosition(){
    	return new Position(yCord,xCord,0);
    }
    

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
                return true;
        if (obj == null)
                return false;
        if (getClass() != obj.getClass())
                return false;
        Node other = (Node) obj;
        
        if(xCord != other.xCord)
            return false;
        
        return yCord == other.yCord;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.xCord;
        hash = 83 * hash + this.yCord;
        return hash;
    }
    @Override
    public int compareTo(Node that){
        if(!this.getName().equals(that.getName()))
            return (this.getDistance()+this.heuristicDistanceToEnd) - (that.getDistance()+that.heuristicDistanceToEnd);
        return 0;
    }
    
}

