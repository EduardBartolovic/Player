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

/** Pilzpositionen in einem Kreuz.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-05-03
 */
public class FlatlandCross extends Flatland {
    /** Pilzpositionen. */
    private final Set<Position> mushrooms = new HashSet<>();

    /** Landschaft mit je 32 Pilzen auf den Achsen des Kreuzes.
     */
    public FlatlandCross() {
        this("32");
    }

    /** Landschaft mit einem Kreuz, auf dessen Achsen Pilze stehen.
     * @param cellsString Anzahl Pilze auf jeder Achse.
     */
    public FlatlandCross(String cellsString) {
        final int cells = Integer.parseInt(cellsString);
        if(cells % 2 != 0)
            throw new IllegalArgumentException("even number required");
        final int offset = getSize() / (cells + 1);
        for(int m = 0; m < cells; m++)
            mushrooms.add(getPositionAt(getSize() * m / cells + offset,
                                        getSize() / 2));
        for(int m = 0; m < cells; m++)
            mushrooms.add(getPositionAt(getSize() / 2,
                                        getSize() * m / cells + offset));
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

}
