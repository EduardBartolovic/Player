/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.15
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2500 MHz, 16000 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.arena.landscape;

import edu.hm.cs.rs.se2.miner.arena.Landscape;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Besondere Punkte einer Landschaft: Start, Ziel, Pilze.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-11
 */
abstract class PointsOfInterest implements Landscape {
    /**
     * Voreingestellte Position des einzigen Pilzes, Lat = Long.
     */
    static final int DEFAULT_MUSHROOM_LAT_LONG = 136;

    /**
     * Groesse der Landschaft.
     */
    static final int SIZE = 256;

    /**
     * Zielposition. Default = Startposition.
     */
    private Position destination;

    /**
     * Positionen der Pilze.
     */
    private final Set<Position> mushrooms = new HashSet<>();

    /**
     * Startposition. Default = Mitte der Landschaft.
     */
    private Position start;

    @Override
    public Position getDestination() {
        return destination;
    }

    @Override
    public Set<Position> getMushrooms() {
        return Collections.unmodifiableSet(mushrooms);
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public Position getStart() {
        return start;
    }

    /**
     * Liest einen String mit Koordinatenpaaren und liefert die Positionen.
     * Die Koordinatenpaare sind mit Strichpunkten getrennt.
     * Latitude und Longitude sind mit einem Slash getrennt.
     * @param givenPositionsString String mit semicolon-getrennten Koordinaten,
     * jeweils Latitude/Longitude.
     * @return Array mit Positionen. Die Hoehen muessen verfuegbar sein.
     */
    private Position[] parsePositions(String givenPositionsString) {
        assert getAltitude(0, 0) > 0: "altitudes initialized";
        return Stream.of(givenPositionsString.split("\\s*;\\s*"))
                .filter(string -> !string.isEmpty())
                .map(latLongString -> latLongString.split("/"))
                .map((String[] latLongStringArray) -> new int[] {
                        Integer.parseInt(latLongStringArray[0]),
                        Integer.parseInt(latLongStringArray[1])})
                .map((latLongArray) -> getPositionAt(latLongArray[0], latLongArray[1]))
                .toArray(Position[]::new);
    }

    /**
     * Initialisiert die interessanten Punkte aus einem String mit Koordinaten.
     * Nacheinander mit Strichpunkten getrennt: Start, erster Pilz, Ziel, weitere Pilze ...
     * Jede Position besteht aus zwei mit Slash getrennten ganzen Zahlen Latitude/Longitude.
     * Fehlende Koordinaten ersetzt die Methoden durch Defaultpositionen:
     * Start = Mitte der Landschaft;
     * Erster Pilz = Start + 8 in jede Richtung;
     * Ziel = Start.
     * <p>
     * Ein Beispiel: Der String "128/128;136/136;120/120;1/1" bedeutet
     * Start = (128,128), Ziel = (120,120), Pilze = (136,136) und (1,1).
     * @param givenPositionsString String mit Koordinaten.
     */
    final void placePointsOfInterest(String givenPositionsString) {
        final Position[] givenPositions = parsePositions(givenPositionsString);
        int givenPosition = 0;
        start = givenPositions.length > givenPosition
                ? givenPositions[givenPosition++]
                : getPositionAt(SIZE/2, SIZE/2);
        mushrooms.add(givenPositions.length > givenPosition
                              ? givenPositions[givenPosition++]
                              : getPositionAt(DEFAULT_MUSHROOM_LAT_LONG, DEFAULT_MUSHROOM_LAT_LONG));
        destination = givenPositions.length > givenPosition
                ? givenPositions[givenPosition++]
                : start;
        while(givenPosition < givenPositions.length)
            mushrooms.add(givenPositions[givenPosition++]);
    }

}
