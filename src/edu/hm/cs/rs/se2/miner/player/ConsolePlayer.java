/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.7.4
 * violet (Intel Core i7 CPU 920/2.67GHz, 8 cores, 2668 MHz, 12032 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.player;

import edu.hm.cs.rs.se2.miner.common.Direction;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/** Ein Spieler ohne eigene Logik, der Methodenaufrufe von der Tastatur liest.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2016-12-30
 */
public class ConsolePlayer implements Player {
    /** Die Regeln. */
    private final Ruler ruler;

    /** Neuer Spieler.
     * @param ruler Die Regeln.
     */
    public ConsolePlayer(Ruler ruler) {
        this.ruler = ruler;
    }

    @Override public void run() {
        // Lebenszeichen ausgeben
        System.out.println('\n' + getClass().getSimpleName() + " running ...");

        // Was bei den verschiedenen Kommandos passieren soll
        // Hier verliert der Java-8-Compiler den Faden und kann den Diamond-Operator nicht mehr aufloesen
        final Map<String, Supplier<Object>> commandsToMethod = new TreeMap<>();
        final Set<String> commandsWithDescription = new TreeSet<>();

        final BiConsumer<String, Supplier<Object>> feeder = (text, action) -> {
            commandsToMethod.put(text.substring(0, text.indexOf(' ')), action);
            commandsWithDescription.add(text);
        };

        feeder.accept("n - move North", () -> ruler.move(Direction.N));
        feeder.accept("w - move West", () -> ruler.move(Direction.W));
        feeder.accept("s - move South", () -> ruler.move(Direction.S));
        feeder.accept("e - move Easr", () -> ruler.move(Direction.E));
        feeder.accept("r - take radar image", ruler::takeRadarImage);
        feeder.accept("t - ticks", ruler::getTicks);
        feeder.accept("mu - mushrooms remaining", ruler::getMushroomsRemaining);
        feeder.accept("p - player position", ruler::getPosition);
        feeder.accept("su - surrender", ruler::surrender);
        feeder.accept("st - game state", ruler::getState);
        feeder.accept("mi - millis remaining", ruler::getMillisRemaining);
        feeder.accept("a - arena size", ruler::getArenaSize);
        feeder.accept("d - destination", ruler::getDestination);
        feeder.accept("x - exit shell", () -> {
                      System.exit(0); // silent death :-(
                      throw new AssertionError("cannot happen");
                  });

        try(InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            // Tastaturzeilen lesen bis nichts mehr kommt
            for(String input = readInput(bufferedReader); input != null; input = readInput(bufferedReader)) {
                // Code, der das Ergebnis liefert
                final Supplier<Object> supplier = commandsToMethod.get(input.toLowerCase());
                if(supplier == null)
                    System.out.printf("unknown command: %s%nvalid commands are: %s%n",
                                      input,
                                      commandsWithDescription.stream().collect(Collectors.joining("\n\t", "\n\t", "")));
                else {
                    // Ergebnis berechnen
                    final Object result = supplier.get();

                    // Je nach Typ anschaulich ausgeben
                    if(result instanceof List) {
                        final List<?> reverted = new ArrayList<>((List<?>)result);
                        Collections.reverse(reverted);
                        // Radarbild
                        System.out.println(reverted.stream()
                            .map(list -> ((List<Integer>)list).stream()
                                .map(altitude -> altitude == Integer.MAX_VALUE ? -1 : altitude)
                                .map(altitude -> String.format("%4d", altitude))
                                .collect(Collectors.joining()))
                            .collect(Collectors.joining("\n")));
                    }
                    else
                        System.out.println(result);
                }
            }
        }
        catch(IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /** Gibt ein Promptzeichen aus und liest dann eine Zeile.
     * @param bufferedReader Reader zum zeilenweisen Lesen.
     * @return Zeile oder null, wenn nichts mehr kommt.
     * @throws IOException wenn das Lesen schief geht.
     */
    private String readInput(final BufferedReader bufferedReader) throws IOException {
        System.out.print("> ");
        System.out.flush();
        return bufferedReader.readLine();
    }

}
