/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.15
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2000 MHz, 16000 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.common;

import static edu.hm.cs.rs.se2.miner.common.Direction.*;
import java.io.Serializable;

/** Position in der Arena.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-03-29
 */
public class Position implements Serializable {
    /** Koordinate vom Boden zum Himmel. */
    private final int altitude;

    /** Koordinate von Sueden nach Norden. Nicht negativ. */
    private final int latitude;

    /** Koordinate von West nach Ost. Nicht negativ. */
    private final int longitude;

    /** Neue Position.
     * @param latitude Koordinate von Sueden nach Norden.
     * @param longitude Koordinate von West nach Ost.
     * @param altitude Koordinate vom Boden in den Himmel.
     */
    public Position(int latitude, int longitude, int altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    @Override public boolean equals(Object anything) {
        if(this == anything)
            return true;
        if(anything == null)
            return false;
        if(getClass() != anything.getClass())
            return false;
        final Position other = (Position)anything;
        if(altitude != other.altitude)
            return false;
        if(latitude != other.latitude)
            return false;
        if(longitude != other.longitude)
            return false;
        return true;
    }

    /** Manhattan-Entfernung zwischen diesem und einem weiteren Punkt.
     * Ignoriert den Hoehenunterschied.
     * @param that Anderer Punkt. Nicht null.
     * @return Manhattan-Distanz. Nicht negativ.
     */
    public int flatDistance(Position that) {
        return Math.abs(getLatitude() - that.getLatitude()) + Math.abs(getLongitude() - that.getLongitude());
    }

    public int getAltitude() {
        return altitude;
    }

    /** Liefert die Richtung von dieser Position zu einer anderen.
     * @param destination Andere Position.
     * @return Richtung oder null.
     */
    public Direction getDirectionTo(Position destination) {
        Direction direction;
        if(getLatitude() > destination.getLatitude())
            direction = S;
        else if(getLatitude() < destination.getLatitude())
            direction = N;
        else if(getLongitude() > destination.getLongitude())
            direction = W;
        else if(getLongitude() < destination.getLongitude())
            direction = E;
        else
            direction = null;
        return direction;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + altitude;
        result = prime * result + latitude;
        result = prime * result + longitude;
        return result;
    }

    /** Eine neue Position versetzt gegenueber dieser Position.
     * @param latitudeShift Versatz in SN-Richtung.
     * @param longitudeShift Versatz in WE-Richtung.
     * @param altitudeShift Versatz in der Bodenhoehe.
     * @return Neue Position. Nicht null.
     */
    public Position movedBy(int latitudeShift, int longitudeShift, int altitudeShift) {
        return new Position(getLatitude() + latitudeShift,
                            getLongitude() + longitudeShift,
                            getAltitude() + altitudeShift);
    }

    /** Eine neue Position versetzt gegenueber dieser Position.
     * Die Hoehe bleibt unveraendert und passt nachher eventuell nicht zur Landschaft.
     * @param direction Himmelsrichtung. Nicht null.
     * @return Neue Position. Nicht null.
     */
    public Position movedTo(Direction direction) {
        return new Position(getLatitude() + direction.getOffsetLatitude(),
                            getLongitude() + direction.getOffsetLongitude(),
                            getAltitude());
    }

    @Override public String toString() {
        return "(" + latitude + '/' + longitude + '/' + altitude + ")";
    }

}
