/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.15
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2000 MHz, 16000 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.common;

import java.io.Serializable;

/** Himmelsrichtungen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-03-29
 */
public enum Direction implements Serializable {
    // CHECKSTYLE:OFF JavadocVariable
    N(1, 0), E(0, 1), S(-1, 0, N), W(0, -1, E);
    // CHECKSTYLE:ON

    /** Verschiebung der Breite. */
    private final int offsetLatitude;

    /** Verschiebung der Laenge. */
    private final int offsetLongitude;

    /** Entgegengesetzte Richtung. */
    private Direction opposite;

    /** Neue Richtung mit Verschiebungen.
     * Die entgegengesetzte Richtung bleibt noch unbestimmt.
     * @param offsetLatitude Verschiebung der Breite.
     * @param offsetLongitude Verschiebung der Laenge.
     */
    Direction(int offsetLatitude, int offsetLongitude) {
        this(offsetLatitude, offsetLongitude, null);
    }

    /** Neue Richtung mit Verschiebungen.
     * @param offsetLatitude Verschiebung der Breite.
     * @param offsetLongitude Verschiebung der Laenge.
     * @param opposite Entgegengesetzte RichrEntgegengesetzte Richrungtung.
     */
    Direction(int offsetLatitude, int offsetLongitude, Direction opposite) {
        this.offsetLatitude = offsetLatitude;
        this.offsetLongitude = offsetLongitude;
        if(opposite != null) {
            this.opposite = opposite;
            opposite.setOpposite(this);
        }
    }

    public int getOffsetLatitude() {
        return offsetLatitude;
    }

    public int getOffsetLongitude() {
        return offsetLongitude;
    }

    /** Liefert die entgegengesetzte Richtung.
     * @return Entgegengesetzte Richtung. Nicht null.
     */
    public Direction opposite() {
        return opposite;
    }

    private void setOpposite(Direction opposite) {
        this.opposite = opposite;
    }

}
