/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.15
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2601 MHz, 16000 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.arena.landscape;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import edu.hm.cs.rs.se2.miner.arena.Landscape;
import edu.hm.cs.rs.se2.miner.common.Position;

/** Eine Testlandschaft mit festem Aufbau:
 * . . | . .
 * . S | . M
 * - - + - -
 * . . | D .
 * M . | . .
 * Die Hoehe von Feld (x, z) ist z + size*x.
 * x nach rechts ( ... size-1), z nach oben (0 ... size-1).
 * Mit size expandieren die Linien.
 * Die vier Felder in den Ecken bleiben in den Ecken.
 *
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-03-31
 */
public class TestZone implements Landscape {
    /** Standardgroesse. */
    private static final int DEFAULT_SIZE = 5;

    /** Zielposition. */
    private final Position destination;

    /** Schwammerl links unten. */
    private final Position mushroomBL;

    /** Pilz rechts oben. */
    private final Position mushroomTR;

    /** Kantenlaenge der Landschaft. */
    private final int size;

    /** Startposition. */
    private final Position start;

    /** Neue starre Landschaft mit der Standardroesse.
     */
    public TestZone() {
        this(DEFAULT_SIZE);
    }

    /** Neue starre Landschaft.
     * @param size Kantenlaenge.
     */
    public TestZone(int size) {
        if(size < 2)
            throw new IllegalArgumentException("invalid size: " + size);
        this.size = size;
        mushroomBL = getPositionAt(0, 0);
        mushroomTR = getPositionAt(size - 2, size - 1);
        destination = getPositionAt(1, size - 2);
        start = getPositionAt(size - 2, 1);
    }

    @Override public int getAltitude(int latitude, int longitude) {
        if(latitude < 0 || latitude >= size)
            throw new IllegalArgumentException("latitude out of range: " + latitude);
        if(longitude < 0 || longitude >= size)
            throw new IllegalArgumentException("longitude out of range: " + longitude);
        return 1 + latitude + size * longitude;
    }

    @Override public Position getDestination() {
        return destination;
    }

    public Position getMushroomBL() {
        return mushroomBL;
    }

    public Position getMushroomTR() {
        return mushroomTR;
    }

    @Override public Set<Position> getMushrooms() {
        return Stream.of(mushroomBL, mushroomTR).collect(Collectors.toSet());
    }

    @Override public int getSize() {
        return size;
    }

    @Override public Position getStart() {
        return start;
    }

}
