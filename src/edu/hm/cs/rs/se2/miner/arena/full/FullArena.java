/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.17
 * emma (Intel Core i7-4790 CPU/3.60GHz, 8 cores, 2200 MHz, 32128 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.arena.full;

import edu.hm.cs.rs.se2.miner.arena.Arena;
import edu.hm.cs.rs.se2.miner.arena.Landscape;
import edu.hm.cs.rs.se2.miner.common.Factory;
import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.common.State;
import java.util.HashSet;
import java.util.Set;

/**
 * Eine Arena mit unbeschraenktem Zugriff.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-05
 */
public interface FullArena extends Arena {
    /**
     * Sammelt einen Pilz ein.
     * @param mushroom Neu eingesammelter Pilz.
     * Nicht null.
     * Muss in der Landschaft vorkommen.
     * Darf noch nicht eingesammelt sein.
     * @return diese Arena.
     */
    FullArena collectMushroom(Position mushroom);

    /**
     * Benachrichtigt alle Viewer ueber eine Aenderung.
     * Ohne Wirkung, wenn sich die Arena seit dem letzten Aufruf nicht geaendert hat.
     * @param event Gegenstand der Aenderung.
     * Entweder ein einzelnes Event-Objekt oder eine Collection von Event-Objekten.
     */
    void notifyObservers(Object event);

    /**
     * Produziert eine neue Arena.
     * @param specification Typ und Parameter der gewuenschten Arena.
     * @param landscape Landschaft der Arena.
     * @return Neue Arena.
     * @exception ReflectiveOperationException bei einem unbekannten Typ und so weiter.
     */
    static FullArena make(String specification, Landscape landscape) throws ReflectiveOperationException {
        return Factory.<FullArena>make(specification, landscape);
    }

    /**
     * Legt den Spielzustand neu fest.
     * @param state Neuer Zustand. Nicht null.
     * @return Diese Arena.
     */
    FullArena setState(State state);

    /**
     * Liefert die Kantenlaenge eines Radarbildes.
     * @return Kantenlaenge. Echt positiv und ungerade.
     */
    int getRadarEdgeLength();

    /**
     * Legt die Spielzeit neu fest.
     * @param newTicks Neue Spielzeit. Nicht negativ.
     * @return diese Arena.
     */
    FullArena setTicks(int newTicks);

    /**
     * Setzt den Spieler an eine neue Position.
     * @param position Neue Position des Spielers. Nicht null.
     * @return diese Arena.
     */
    FullArena setFigure(Position position);

    /**
     * Die noch nicht gesammelten Pilze.
     * @return Noch nicht gesammelte Pilze. Nicht null. Kein Element null.
     */
    default Set<Position> getMushroomsRemaining() {
        final Set<Position> remaining = new HashSet<>(getLandscape().getMushrooms());
        remaining.removeAll(getMushroomsCollected());
        return remaining;
    }

}
