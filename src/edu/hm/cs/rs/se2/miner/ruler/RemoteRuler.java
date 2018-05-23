/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.8.0_121, Linux i386 4.7.4
 * violet (Intel Core i7 CPU 920/2668 MHz, 8 Cores, 12032 MB RAM)
 */
package edu.hm.cs.rs.se2.miner.ruler;

import static edu.hm.cs.rs.se2.miner.common.State.Running;
import static edu.hm.cs.rs.se2.miner.ruler.RemoteRuler.Message.ArenaSize;
import static edu.hm.cs.rs.se2.miner.ruler.RemoteRuler.Message.Hello;
import static edu.hm.cs.rs.se2.miner.ruler.RemoteRuler.Message.Landscape;
import static edu.hm.cs.rs.se2.miner.ruler.RemoteRuler.Message.LoadMap;
import static edu.hm.cs.rs.se2.miner.ruler.RemoteRuler.Message.MillisRemaining;
import static edu.hm.cs.rs.se2.miner.ruler.RemoteRuler.Message.MushroomsRemaining;
import static edu.hm.cs.rs.se2.miner.ruler.RemoteRuler.Message.State;
import static edu.hm.cs.rs.se2.miner.ruler.RemoteRuler.Message.Surrender;
import static edu.hm.cs.rs.se2.miner.ruler.RemoteRuler.Message.TakeRadarImage;
import static edu.hm.cs.rs.se2.miner.ruler.RemoteRuler.Message.Ticks;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import edu.hm.cs.rs.se2.miner.arena.full.FullArena;
import edu.hm.cs.rs.se2.miner.common.Direction;
import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.common.State;

/** Der Client eines Rulers, der uebers Netzwerk kommuniziert.
 * Das Protokoll verwendet serialisierte Java-Objekte:
 * Request:
 * 1. Message.
 * 2. Object: Argument oder null.
 * Response:
 * 1. String: Erfolgssignal.
 * 2. Object, falls Erfolgssignal.equals("OK"); faellt sonst weg.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2016-11-04
 */
public class RemoteRuler implements Ruler {
    /** Port, auf dem der Server antwortet. */
    static final int PORT = 23859;

    /** Konstanten fuer Methodenaufrufe. */
    // CHECKSTYLE:OFF MagicNumber
    enum Message {
    	Hello,
        /** Nur erste Nachricht: Auswahl einer Landschaft. Kein Ruler-Methodenaufruf. */
        Landscape,
        ArenaSize,
        Position,
        Destination,
        MushroomsRemaining,
        Move,
        Ticks,
        TakeRadarImage,
        Surrender,
        State,
        MillisRemaining,
        /** Letzte Nachricht: Ruecksenden einer Map. Kein Ruler-Methodenaufruf. */
        LoadMap

    }
    // CHECKSTYLE:ON

    /** Objekte, die vom Server reinkommen. */
    private final ObjectInputStream objectInputStream;

    /** Objekte, die zum Server rausgehen. */
    private final ObjectOutputStream objectOutputStream;

    /** Socket, ueber den dieser Client kommuniziert. */
    private final Socket socket;

    /** Zuletzt vom Server gelieferter Zustand. */
    private State state = Running;

    /** Neue Spielregeln, die auf dem Server laufen.
     * @param arena Arena, in der das Spiel laeuft. Nicht null.
     * @param hostname Hostname oder IP-Adresse des Servers.
     * @throws IOException wenn die Netzwerkverbindung nicht funktioniert.
     * @throws ClassNotFoundException wenn die Antwort ein Objekt einer unbekannten Klasse enthaelt.
     */
    public RemoteRuler(FullArena arena, String hostname) throws IOException, ClassNotFoundException {
        this.socket = new Socket(hostname, PORT);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        callRemote(Hello, socket.getLocalAddress().getCanonicalHostName());
        callRemote(Landscape, arena.getLandscape());
    }

    @Override public int getArenaSize() {
        return (Integer)callRemote(ArenaSize, null);
    }

    @Override public Position getDestination() {
        return (Position)callRemote(Message.Destination, null);
    }

    @Override public int getMillisRemaining() {
        return (Integer)callRemote(MillisRemaining, null);
    }

    @Override public Set<Position> getMushroomsRemaining() {
        return (Set<Position>)callRemote(MushroomsRemaining, null);
    }

    @Override public Position getPosition() {
        return (Position)callRemote(Message.Position, null);
    }

    @Override public State getState() {
        state = (State)callRemote(State, null);
        return state;
    }

    @Override public int getTicks() {
        return (Integer)callRemote(Ticks, null);
    }

    @Override public State move(Direction direction) {
        state = (State)callRemote(Message.Move, direction);
        return state;
    }

    @Override public Ruler surrender() {
        callRemote(Surrender, null);
        return this;
    }

    @Override public List<List<Integer>> takeRadarImage() {
        return (List<List<Integer>>)callRemote(TakeRadarImage, null);
    }

    /** Schickt einen Request zum Server und wertet die Response aus.
     * @param message Methode.
     * @param arg Argument zur Methoden; null bei parameterlosen Methoden.
     * @return Ergebnis des Methodenaufrufs;
     * null, wenn der der Methodenaufruf nicht funktioniert hat.
     */
    private Object callRemote(Message message, Object arg) {
        try {
            System.out.printf(arg == null ? "calling %s" : "calling %s(%s)",
                              message,
                              arg);
            // Request senden
            objectOutputStream.writeObject(message);
            objectOutputStream.writeObject(arg);
            objectOutputStream.flush();

            // Response empfangen
            final String code = (String)objectInputStream.readObject();
            final Object result = "OK".equals(code)
                ? objectInputStream.readObject()
                : null;

            System.out.printf(" => [%s] %s%n", code, result);
            return result;
        }
        catch(IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            // Rekursion vermeiden: Wenn LoadMap schon laeuft, nicht nochmal eine Karte holen
            if(message != LoadMap && state != Running)
                try {
                    // Falls das Spiel zu Ende ist, eine Karte vom Server holen und abspeichern
                    final String filename = "map.png";
                    Files.write(Paths.get(filename), (byte[])callRemote(LoadMap, null));
                }
                catch(IOException ex) {
                    throw new RuntimeException(ex);
                }
        }
    }

}
