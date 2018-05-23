package edu.hm.bartolov.se2.miner.player.pathfinder;

import java.util.Comparator;

public class SortingQueue implements Comparator<Node>{


    @Override
    public int compare(Node o1, Node o2) {
        if(o1.getName().equals(o2.getName()))
            return 0;
        //return (o1.getHeuristicDistanceToEnd()) - (o2.getHeuristicDistanceToEnd());
        return (o1.getDistance()+o1.getHeuristicDistanceToEnd()) - (o2.getDistance()+o2.getHeuristicDistanceToEnd());
        //return o1.getDistance() - o2.getDistance();    
    }
    
}
