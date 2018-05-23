/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.15
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2000 MHz, 16000 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.ruler;

import edu.hm.cs.rs.se2.miner.arena.full.FullArena;
import edu.hm.cs.rs.se2.miner.common.Direction;
import edu.hm.cs.rs.se2.miner.common.Event;
import edu.hm.cs.rs.se2.miner.common.Factory;
import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.common.State;
import java.util.List;
import java.util.Set;

/** Logische Operationen und Auskunft ueber das Spiel.
 * Ein Teil der Methoden kostet Spielzeit.
 * <p>
 * Die Methoden, die Spielzeit verbrauchen, pruefen,
 * ob das Realzeitlimit erreicht oder ueberschritten ist.
 * Sie setzen den Spielzustand in diesem Fall auf TimedOut und senden das entsprechende Event an die Arena.
 * Die anderen Methoden testen die Realzeit nicht.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-05
 * @see Event#TimedOut
 * @see State#Timedout
 */
public interface Ruler {
    /** Liefert eine Spiellogik.
     * @param specification Beschreibung der gewuenschten Implementierung.
     * @param arena Arena, in der das Spiel ablaeuft. Nicht null.
     * @return Konkrete Spiellogik. Nicht null.
     * @throws ReflectiveOperationException bei einem unbekannten Typ und so weiter.
     */
    static Ruler make(String specification, FullArena arena) throws ReflectiveOperationException {
        return Factory.<Ruler>make(specification, arena);
    }

    /** Ein quadratisches Bild der Gelaendehoehen in der Umgebung des Maennchens.
     * Das Maennchen steht auf dem Feld in der Mitte.
     * Felder ausserhalb der Arena haben die Hoehe MAX_VALUE.
     * Diese Methode kostet Spielzeit.
     * Benachrichtigt die Arena mit dem Event RadarImage.
     * @return n Listen mit jeweils n Elementen. n = ungerade Kantenlaenge des Radarbildes.
     * Nicht null und kein Element null.
     * Die aeussere Liste sind Zeilen des Radarbildes nach steigender Latitude.
     * Die inneren Listen enthalten Hoehen nach steigender Longitude.
     * @throws IllegalStateException beim Aufruf mit einem anderen State als Running.
     * @see Event#RadarImage
     */
    List<List<Integer>> takeRadarImage();

    /** Bisher verbrauchte Spielzeit.
     * @return Verbrauchte Spielzeit. Nicht negativ.
     */
    int getTicks();

    /** Positionen der verbleibenden Pilze.
     * @return Die Anzahl Elemente ist die Anzahl verbleibender Pilze.
     * Nicht null. Kein Element null.
     */
    Set<Position> getMushroomsRemaining();

    /** Bewegt das Maennchen.
     * Diese Methode kostet Spielzeit.
     * Benachrichtigt die Arena in jedem Fall mit dem Event Moved.
     * Wenn das Maennchen den Rand der Arena ueberquert, ist das Spiel verloren (Event und State Lost).
     * Wenn das Maennchen die Position eines noch nicht gesammelten Pilzes erreicht,
     * kommt das Event Collected dazu und der Pilz ist eingesammelt.
     * Wenn das Maennchen mit allen Pilzen die Zielposition erreicht,
     * kommt das Event Completed dazu und das Spiel endet erfolgreich.
     * @param direction Himmelsrichtung. Nicht null.
     * @return Spielzustand. Nicht null.
     * @throws NullPointerException wenn die Richtung null ist.
     * @throws IllegalStateException beim Aufruf mit einem anderen State als Running.
     * @see Event#Moved
     * @see Event#Lost
     * @see Event#Collected
     * @see Event#Completed
     */
    State move(Direction direction);

    /** Die aktuelle Position des Maennchens.
     * @return Die Position. Nicht null.
     * Kann ausserhalb der Landschaft liegen, wenn das Spiel verloren ist.
     * Liegt ansonsten innerhalb der Landschaft.
     */
    Position getPosition();

    /** Der Spieler gibt auf.
     * Ohne Wirkung nach Spielende.
     * Benachrichtigt die Arena.
     * @return Dieses Objekt.
     * @see State#Surrendered
     * @see Event#Surrendered
     */
    Ruler surrender();

    /** Liefert den Spielzustand.
     * @return Zustand. Nicht null.
     */
    State getState();

    /** Verbleibende Realzeit bis zum Abbruch.
     * @return Anzahl Millisekunden bis zum Abbruch. Nicht negativ.
     * 0 nach Spielende.
     */
    int getMillisRemaining();

    /** Die Kantenlaenge der Arena.
     * @return Kantenlaenge. Echt positiv.
     */
    int getArenaSize();

    /** Die Zielposition.
     * Wenn der Spieler alle Pilze eingesammelt hat und diese Position erreicht,
     * endet das Spiel erfolgreich.
     * @return Zielposition. Nicht null.
     */
    Position getDestination();

}
