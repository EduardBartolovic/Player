
package edu.hm.bartolov.se2.miner.player.tool;

import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Comparator;

/**
 *
 * @author Edo
 */
public class ComperatorSouthWest implements Comparator<Position>{

    @Override
    public int compare(Position o1, Position o2) {
        return (o2.getLongitude()+o2.getLatitude()) - (o1.getLongitude()+o1.getLatitude());
    }
    
}
