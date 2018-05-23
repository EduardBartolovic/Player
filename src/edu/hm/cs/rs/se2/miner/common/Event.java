/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.17
 * emma (Intel Core i7-4790 CPU/3.60GHz, 8 cores, 3601 MHz, 32128 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.common;

/** Vorkommnisse im Spiel.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-03-29
 */
public enum Event {
    /** Radarbild geholt. */
    RadarImage,
    /** Maennchen bewegt. */
    Moved,
    /** Aufgegeben. */
    Surrendered,
    /** Zeitschranke ueberschritten. */
    TimedOut,
    /** Einen Pilz eingesammelt. */
    Collected,
    /** Den Rand ueberquert. */
    Lost,
    /** Mit allen Pilzen an der Zielposition angekommen. */
    Completed

}
