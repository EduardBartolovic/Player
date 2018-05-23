/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.15
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2601 MHz, 16000 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.arena;

import edu.hm.cs.rs.se2.miner.common.Factory;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.io.Serializable;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Eine Landschaft.
 * Lanschaften sind immutable.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-12
 */
public interface Landscape extends Serializable {
    /**
     * Kantenlaenge der Landschaft.
     * Alle Landschaften sind quadratisch.
     * @return Kantenlaenge. Echt positiv.
     */
    int getSize();

    /**
     * Startposition des Maennchens.
     * @return Position. Nicht null.
     */
    Position getStart();

    /**
     * Zielposition des Maennchens, nachdem es alle Pilze eingesammelt hat.
     * @return Position. Nicht null.
     */
    Position getDestination();

    /**
     * Gibt Auskunft, ob die Position innerhalb der Landschaft liegt oder nicht.
     * Wertet nur die Breite und Laenge aus, ignoriert die Hoehe.
     * @param position Eine Position. Nicht null.
     * @return true, wenn die Position in der Landschaft liegt; false ansonsten.
     */
    default boolean isValid(Position position) {
        final Predicate<Integer> inRange = tested -> tested >= 0 && tested < getSize();
        return inRange.test(position.getLatitude())
               && inRange.test(position.getLongitude());
    }

    /**
     * Liefert die Hoehe der Landschaft an gegebenen Koordinaten.
     * @param latitude Geografische Breite.
     * @param longitude Geografische Laenge.
     * @return Anzahl fester Bloecke, die an diesen Koordinaten uebereinander liegen.
     * Echt positiv.
     */
    int getAltitude(int latitude, int longitude);

    /**
     * Die Positionen aller Pilze.
     * @return Pilzpositionen. Nicht null, kein Element null.
     */
    Set<Position> getMushrooms();

    /**
     * Die Position des freien Blocks direkt auf der Oberflaeche.
     * @param latitude Geografische Breite.
     * @param longitude Geografische Laenge.
     * @return Position auf der Oberflaeche, direkt ueber dem obersten Felsblock.
     * Nicht null.
     */
    default Position getPositionAt(int latitude, int longitude) {
        return new Position(latitude, longitude, getAltitude(latitude, longitude));
    }

    /**
     * Produziert eine neue Landschaft.
     * @param specification Typ und Parameter der gewuenschten Landschaft.
     * @return Neue Landschaft.
     * @exception ReflectiveOperationException bei einem unbekannten Typ und so weiter.
     */
    static Landscape make(String specification) throws ReflectiveOperationException {
        return Factory.<Landscape>make(specification);
    }

}
