/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.17
 * emma (Intel Core i7-4790 CPU/3.60GHz, 8 cores, 2200 MHz, 32128 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.player;

import edu.hm.cs.rs.se2.miner.common.Factory;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;

/** Ein Spieler, der das Maennchen steuert.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-03-29
 */
public interface Player {
    /** Liefert einen Player.
     * @param specification Bescheibung des gewuenschten Spielers.
     * @param ruler Spielregeln. Nicht null.
     * @return Ein Player. Nicht null.
     * @throws ReflectiveOperationException wenn die Methode keinen Spieler erzeugen kann.
     */
    static Player make(String specification, Ruler ruler) throws ReflectiveOperationException {
        return Factory.<Player>make(specification, ruler);
    }

    /** Startet den Player.
     * Kehrt zurueck, wenn das Spiel beendet ist (ob erfolgreich oder nicht).
     */
    void run();

}
