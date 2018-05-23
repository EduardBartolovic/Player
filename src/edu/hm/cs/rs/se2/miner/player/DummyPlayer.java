/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.17
 * emma (Intel Core i7-4790 CPU/3.60GHz, 8 cores, 2200 MHz, 32128 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.player;

import edu.hm.cs.rs.se2.miner.common.Direction;
import static edu.hm.cs.rs.se2.miner.common.Direction.values;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;

/**
 * Demo-Spieler.
 * Bewegt sich einmal im Kreis und gibt dann auf.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-03-29
 */
class DummyPlayer implements Player {
    /**
     * Spielregeln.
     */
    private final Ruler ruler;

    /**
     * Neuer Spieler.
     * @param ruler Die Spielregeln.
     */
    DummyPlayer(Ruler ruler) {
        this.ruler = ruler;
    }

    @Override
    public void run() {
        for(Direction direction : values())
            ruler.move(direction);
        ruler.surrender();
    }

}
