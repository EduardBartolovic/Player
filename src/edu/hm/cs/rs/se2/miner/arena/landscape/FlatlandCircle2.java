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

/**
 * Pilzpositionen in konzentrischen Kreisen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-05-03
 */
public class FlatlandCircle2 extends Flatland {
    /**
     * Pilzpositionen.
     */
    private final Set<Position> mushrooms = new HashSet<>();

    /**
     * Landschafte mit je 32 Pilzen auf drei Kreisen mit Radien 80, 90, 100.
     */
    public FlatlandCircle2() {
        this("100", "32");
    }

    /**
     * Neue Landschaft.
     * @param radiusString Radius des ausseren Pilzkreises.
     * Die inneren Kreise haben Radien -10, -20.
     * @param mushroomsString Anzahl Pilze auf dem Kreis.
     */
    public FlatlandCircle2(String radiusString, String mushroomsString) {
        final int radius = Integer.parseInt(radiusString);
        int numMushrooms = Integer.parseInt(mushroomsString);
        placeCircle(numMushrooms, radius);
        placeCircle(numMushrooms, radius - 10);
        placeCircle(numMushrooms, radius - 20);
    }

    @Override
    public Position getDestination() {
        return getStart();
    }

    @Override
    public Set<Position> getMushrooms() {
        return Collections.unmodifiableSet(mushrooms);
    }

    @Override
    public Position getStart() {
        return getPositionAt(0, 0);
    }

    private void placeCircle(int numMushrooms, final int radius) {
        for(int mushroom = 0; mushroom < numMushrooms; mushroom++) {
            double angle = Math.PI*2*mushroom/numMushrooms;
            mushrooms.add(getPositionAt((int)(radius*Math.cos(angle) + getSize()/2),
                                        (int)(radius*Math.sin(angle) + getSize()/2)));
        }
    }

}
