/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.17
 * emma (Intel Core i7-4790 CPU/3.60GHz, 8 cores, 2200 MHz, 32128 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.viewer;

import java.util.Observer;
import edu.hm.cs.rs.se2.miner.arena.Arena;
import edu.hm.cs.rs.se2.miner.common.Factory;

/** Ein Beobachter.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-05
 */
public abstract class Viewer implements Observer {
	/** Meldet diesen Beobachter an, sodass er Aenderungen erfaehrt.
     * @param arena Eine Arena, die dieser Viewer beobachtet.
     */
    public Viewer(Arena arena) {
        arena.addObserver(this);
    }

    /** Erzeugt einen neuen Beobachter.
     * @param specification Beschreibung der gewuenschten Implementierung.
     * @param arena Eine Arena, die dieser Viewer beobachtet.
     * @return Konkreter Beobachter. Nicht null.
     * @throws ReflectiveOperationException wenn die Methode kein Objekt erzeugen kann.
     */
    public static Viewer make(String specification, Arena arena) throws ReflectiveOperationException {
        return Factory.<Viewer>make(specification, arena);
    }
    
    

}
