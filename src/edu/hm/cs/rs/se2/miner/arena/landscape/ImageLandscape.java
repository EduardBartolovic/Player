/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.8.0_121, Linux i386 4.8.15
 * bluna (Intel Core i7-5600U CPU/2601 MHz, 4 Cores, 15872 MB RAM)
 */
package edu.hm.cs.rs.se2.miner.arena.landscape;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

/**
 * Landschaft, die ihre Hoehen aus den Pixelhelligkeiten eines Imagefiles holt.
 * Pixel weiss = minimale Hoehe, schwarz = maximale Hoehe, Grauwerte dazwischen.
 * Skaliert das Bild auf die Groesse der Landschaft.
 *
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-11
 */
public class ImageLandscape extends PointsOfInterest {

    /** Anzahl Bits, die der Gruenwert in ARGB nach links verschoben ist. */
    private static final int GREENCHANNEL_BITSHIFT = 8;

    /** Bitmaske fuer das niederwertige Byte. */
    private static final int LOWBYTE_BITMASK = 0xFF;

    /** Hoehe von schwarzen Pixeln. */
    private static final int MAX_ALTITUDE = 128;

    /** Anzahl Bits, die der Rotwert in ARGB nach links verschoben ist. */
    private static final int REDCHANNEL_BITSHIFT = 16;

    /**
     * Array mit den Hoehen im Bereich 1...MAX_ALTITUDE.
     * 1. Index = Latitude, 2. Index = Longitude.
     */
    private final int[][] altitudes = new int[SIZE][SIZE];

    /**
     * Neue Landschaft mit den Defaultpositionen
     * Start = (128,128), Ziel = Start, Pilz = (136,136).
     *
     * @param filename Imagefile.
     * @throws IOException wenn das Lesen des Files nicht klappt.
     */
    public ImageLandscape(String filename) throws IOException {
        this(filename, "");
    }

    /**
     * Neue Landschaft mit vorgegebenen Positionen.
     *
     * @param filename Imagefile.
     * @param givenPositions Nacheinander mit Kommas getrennt: Start, erster Pilz, Ziel, weitere Pilze ...
     * Jede Position besteht aus zwei mit Slash getrennten ganzen Zahlen Latitude/Longitude.
     * <p>
     * Ein Beispiel: 128/128,136/136,120/120,1/1
     * Start = (128,128), Ziel = (120,120), Pilze = (136,136) und (1,1).
     * @throws IOException wenn das Lesen des Files nicht klappt.
     */
    public ImageLandscape(String filename, String givenPositions) throws IOException {
        compileAltitudes(filename);
        placePointsOfInterest(givenPositions);
    }

    @Override public int getAltitude(int latitude, int longitude) {
        return altitudes[latitude][longitude];
    }

    /**
     * Berechnet die Hoehen aus dem Imagefile.
     *
     * @param filename Name eines Imagefile.
     * @throws IOException wenn das Lesen des Files schied geht.
     */
    private void compileAltitudes(String filename) throws IOException {
        try(InputStream inputStream = Files.newInputStream(Paths.get(filename))) {
            final BufferedImage image = scale(ImageIO.read(inputStream), SIZE, SIZE);
            for(int x = 0; x < SIZE; x++)
                for(int y = 0; y < SIZE; y++) {
                    final int brightness = rgbToLuminance(image.getRGB(x, y));
                    // Der Ursprung des Bildes liegt links oben
                    // Minimale Helligkeit ist 1
                    final int altitude = brightness * (MAX_ALTITUDE - 1) / 256 + 1;
                    assert altitude > 0: "altitude above minimum";
                    assert altitude < MAX_ALTITUDE: "altitude below maximum";
                    altitudes[SIZE - 1 - y][x] = altitude;
                }
        }
    }

    /**
     * Rechnet einen RGB-Wert in eine Helligkeit um.
     * Ignoriert den Alpha-Kanal.
     *
     * @param rgba 32-Bit ARGB.
     * @return Helligkeit 0...255.
     */
    private static int rgbToLuminance(int rgba) {
        final int red = (rgba >> REDCHANNEL_BITSHIFT) & LOWBYTE_BITMASK;
        final int green = (rgba >> GREENCHANNEL_BITSHIFT) & LOWBYTE_BITMASK;
        final int blue = rgba & LOWBYTE_BITMASK;
        // CHECKSTYLE:OFF MagicNumber
        return (int)(0.299 * red + 0.587 * green + 0.114 * blue);
        // CHECKSTYLE:ON
    }

    /**
     * Skaliert ein Bild auf eine neue Groesse.
     *
     * @param unscaled Originalbild mit beiebigem Seitenverhaeltnis.
     * @param newWidth Neue Breite.
     * @param newHeight Neue Hoehe.
     * @return Skaliertes Bild.
     */
    private static BufferedImage scale(BufferedImage unscaled, int newWidth, int newHeight) {
        final BufferedImage scaled = new BufferedImage(newWidth, newHeight, unscaled.getType());
        final AffineTransform scale = AffineTransform.getScaleInstance((double)newWidth / unscaled.getWidth(),
                                                                       (double)newHeight / unscaled.getHeight());
        scaled.createGraphics().drawRenderedImage(unscaled, scale);
        return scaled;
    }

}
