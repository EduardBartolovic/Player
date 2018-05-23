/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.7.4
 * violet (Intel Core i7 CPU 920/2.67GHz, 8 cores, 2668 MHz, 12032 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.arena.landscape.shootout;

import edu.hm.cs.rs.se2.miner.arena.landscape.Flatland;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Pilzpositionen in zwei engen Rasterboxen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-05-03
 */
public class FlatlandBox2 extends Flatland {
    /** Pilzpositionen. */
    private final Set<Position> mushrooms = new HashSet<>();

    /** Neue Landschaft.
     */
    public FlatlandBox2() {
        final int latitudeQuarterWorld = getSize()/4;
        final int longitudeHalfWorld = getSize()/2;
        final int cells = 8;
        final int distance = 4;
        box(latitudeQuarterWorld, longitudeHalfWorld , cells, distance);
        box(3 * getSize() / 4, longitudeHalfWorld , cells, distance);
    }

    @Override public Position getDestination() {
        return getStart();
    }

    @Override public Set<Position> getMushrooms() {
        return Collections.unmodifiableSet(mushrooms);
    }

    @Override public Position getStart() {
        return getPositionAt(0, 0);
    }

    private void box(int atLat, int atLong, int cells, int distance) {
        final int halfSize = cells * distance / 2;
        for(int x = 0; x < cells; x++)
            for(int y = 0; y < cells; y++)
                mushrooms.add(getPositionAt(atLat - halfSize + x * distance,
                                            atLong - halfSize + y * distance));
    }

}
