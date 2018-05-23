package edu.hm.bartolov.se2.miner.player.tool;

import edu.hm.bartolov.se2.miner.player.common.FlexibleRoute;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Comparator;

/**
 * comparing to Routes 
 * @author Edo
 */
public class RouteComperator implements Comparator<FlexibleRoute>{

    private final Position startPosition;
    
    private final Position endPosition;     

    public RouteComperator(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    @Override
    public int compare(FlexibleRoute o1, FlexibleRoute o2) {
        return o1.totalDistance(startPosition,endPosition) - o2.totalDistance(startPosition,endPosition);
    }
    
}
