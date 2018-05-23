/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.15
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2000 MHz, 16000 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.common;

/** Zustand des Spiel.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-03-29
 */
public enum State {
    /** Spiel laeuft. */
    Running,
    /** Spiel erfolgreich beendet. */
    Completed,
    /** Spiel erfolglos beendet: Realzeitschranke ueberschritten. */
    Timedout,
    /** Spiel erfolglos beendet: aufgegeben. */
    Surrendered,
    /** Spiel erfolglos beendet: Miner im Wasser oder ausserhalb des Spielfeldes. */
    Lost

}
