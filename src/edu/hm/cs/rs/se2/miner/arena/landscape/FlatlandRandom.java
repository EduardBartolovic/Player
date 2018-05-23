/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.7.4
 * violet (Intel Core i7 CPU 920/2.67GHz, 8 cores, 2668 MHz, 12032 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.arena.landscape.shootout;

import edu.hm.cs.rs.se2.miner.arena.landscape.Flatland;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/** Pilz an Zufallspositionen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-05-03
 */
public class FlatlandRandom extends Flatland {
    /** Pilzpositionen. */
    private final Set<Position> mushrooms = new HashSet<>();

    /** Landschaft mit 384 Pilzen,
     */
    public FlatlandRandom() {
        this("23", "384");
    }

    /** Landhschaft mit zufaellige verteilten Pilzen.
     * @param seedString Seed des ZZG.
     * @param mushroomsString Anzahl Pilze.
     */
    public FlatlandRandom(String seedString, String mushroomsString) {
        Random random = new Random(Integer.parseInt(seedString));
        int numMushrooms = Integer.parseInt(mushroomsString);
        for(int mushroom = 0; mushroom < numMushrooms; mushroom++) {
            double angle = Math.PI * 2 * mushroom / numMushrooms;
            mushrooms.add(getPositionAt(random.nextInt(getSize()),
                                        random.nextInt(getSize())));
        }
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
