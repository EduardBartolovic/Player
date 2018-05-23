/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.8.0_121, Linux i386 4.8.15
 * bluna (Intel Core i7-5600U CPU/2601 MHz, 4 Cores, 15872 MB RAM)
 */
package edu.hm.cs.rs.se2.miner.arena.landscape;

/**
 * Landschaft, die ihre Hoehen aus den Pixelhelligkeiten eines Imagefiles holt.
 * Pixel weiss = minimale Hoehe, schwarz = maximale Hoehe, Grauwerte dazwischen.
 * Skaliert das Bild auf die Groesse der Landschaft.
 *
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-11
 */
public class Flatland extends PointsOfInterest {
    /** Hoehe der Landschaft. */
    private static final int ALTITUDE = 64;

    /**
     * Neue Landschaft mit den Defaultpositionen.
     */
    public Flatland() {
        placePointsOfInterest("");
    }

    /**
     * Neue Landschaft mit vorgegebenen Positionen.
     * @param givenPositions Positionen der interessanten Punkte.
     * @see PointsOfInterest#placePointsOfInterest(java.lang.String)
     */
    public Flatland(String givenPositions) {
        placePointsOfInterest(givenPositions);
    }

    /**
     * Neue Landschaft mit einem vorgegebenen Pilz.
     * @param mushroomlatitude Vertikale Position des Pilzes.
     * @param mushroomLongitude Horizontale Position des Pilzes.
     * @see PointsOfInterest#placePointsOfInterest(java.lang.String)
     */
    public Flatland(int mushroomlatitude, int mushroomLongitude) {
        placePointsOfInterest(String.format("%d/%d;%d/%d",
                                            SIZE / 2, SIZE / 2,
                                            mushroomlatitude, mushroomLongitude));
    }

    @Override public int getAltitude(int latitude, int longitude) {
        return ALTITUDE;
    }

}
