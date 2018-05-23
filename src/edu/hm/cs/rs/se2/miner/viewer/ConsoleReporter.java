/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.17
 * emma (Intel Core i7-4790 CPU/3.60GHz, 8 cores, 3601 MHz, 32128 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.viewer;

import edu.hm.cs.rs.se2.miner.arena.Arena;
import edu.hm.cs.rs.se2.miner.common.Event;
import java.util.Collection;
import java.util.Observable;

/**
 * Protokolliert Aenderungen auf der Standardausgabe.
 *
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-05
 */
public class ConsoleReporter extends Viewer {
    /**
     * Neuer Reporter.
     *
     * @param arena Arena, die dieser Reporter beobachtet.
     */
    public ConsoleReporter(Arena arena) {
        super(arena);
    }

    @SuppressWarnings("unchecked") @Override public void update(Observable arena, Object reason) {
        if(reason instanceof Collection<?>)
            for(Event event: (Collection<Event>)reason)
                printMessage((Arena)arena, event);
        else
            printMessage((Arena)arena, reason);
    }

    /** Protokolliert ein Event auf der Konsole.
     * @param arena Arena, die dieser Reporter beobachtet.
     * @param event Event.
     */
    private void printMessage(Arena arena, Object event) {
        System.out.printf("[%-12s] %s%n", event, arena);
    }

}
