/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.15
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2000 MHz, 16000 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.arena;

import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.common.State;
import java.util.Observer;
import java.util.Set;

/**
 * Die oeffentlichen Daten eines Spieles.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-12
 */
public interface Arena {
    /**
     * Die Position des Maennchens.
     * @return Maennchen. Nicht null.
     */
    Position getFigure();

    /**
     * Die oeffentlichen Daten der Landschaft.
     * @return Landschaft. Nicht null.
     */
    Landscape getLandscape();

    /**
     * Die bisher gesammelten Pilze.
     * @return Die bisher gesammelten Pilze in unbestimmter Reihenfolge.
     * Nicht null. Kein Element null.
     */
    Set<Position> getMushroomsCollected();

    /**
     * Der aktuelle Zustand des Spieles.
     * @return Zustand. Nicht null.
     */
    State getState();

    /**
     * Verbleibende Realzeit bis zum Abbruch.
     * @return Anzahl Millisekunden bis zum Abbruch. Nicht negativ.
     * 0, wenn das Realzeitlimit erreicht oder ueberschritten ist.
     */
    int getMillisRemaining();

    /**
     * Abgelaufene Spielzeit.
     * @return Anzahl Ticks. Nicht negativ.
     */
    int getTicks();

    /**
     * Fuegt ein Objekt dazu, das an Aenderungen interessiert ist.
     * @param observer Ein Objekt, das an Aenderungen interessiert ist.
     */
    void addObserver(Observer observer);

}
