/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.17
 * emma (Intel Core i7-4790 CPU/3.60GHz, 8 cores, 2200 MHz, 32128 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import edu.hm.cs.rs.se2.miner.arena.Landscape;
import edu.hm.cs.rs.se2.miner.arena.full.FullArena;
import edu.hm.cs.rs.se2.miner.player.Player;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import edu.hm.cs.rs.se2.miner.viewer.Viewer;

/** Hauptprogramm.
 * Laedt die Komponenten der Anwendung nach Vorgaben eines Properties-Files.
 * Sucht das Properties-File nacheinander bis zum ersten Treffer:
 * <ol><li>Erstes Kommandozeilenargument
 * <li>System-Property miner.props
 * <li>Umgebungsvariable MINER_PROPS
 * <li>File miner.properties im aktuellen Arbeitsdirectory
 * <li>File miner.properties im Homedirectoryaktuellen Arbeitsdirectory
 * <li>Voreingestellte Werte
 * </ol>
 * Im Properties-Files gibt es die folgenden Eintraege:
 * <ul><li>landscape: FQCN der Klasse mit der Landschaft (Default = edu.hm.cs.rs.se2.miner.landscape.Flatland).
 * <li>arena: FQCN der Klasse mit der Arena (Default = edu.hm.cs.rs.se2.miner.arena.full.CoreArena).
 * <li>ruler: FQCN der Klasse mit den Spielregeln (Default = edu.hm.cs.rs.se2.miner.ruler.FakeRuler).
 * <li>player: FQCN der Klasse mit dem Spieler (Default = edu.hm.cs.rs.se2.miner.player.DummyPlayer).
 * <li>viewer.id: FQCN einer Klasse mit einem Viewer (Default = edu.hm.cs.rs.se2.miner.view.ConsoleReporter).
 * </ul>
 * Es kann mehrere Viewer geben mit unterschiedlichen Ids.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-15
 */
public class Main {
    /** Initialisiert alle Bausteine, verknuepft sie und startet dann das Spiel.
     * @param args Kommandozeilenargument: Filename eines Properties-Files, optional.
     * @throws ReflectiveOperationException wenn die Klasse einer Komponente nicht existiert oder
     * beim Erzeugen eines Objektes etwas nicht klappt.
     * @throws IOException das Lesen des Propertiesfiles scheitert.
     */
    public static void main(String... args) throws ReflectiveOperationException, IOException {
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        final List<String> viewerSpecifications = new ArrayList<>();

        final Properties properties = new Properties();
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "edu.hm.cs.rs.se2.miner.ruler.LocalRuler");
            playerSpecification = properties.getProperty("player", "edu.hm.cs.rs.se2.miner.player.DummyPlayer");
            for(@SuppressWarnings("unchecked") Enumeration<String> propertyNames = (Enumeration<String>)properties.propertyNames();
                propertyNames.hasMoreElements();) {
                final String name = propertyNames.nextElement();
                if(name.startsWith("viewer."))
                    viewerSpecifications.add(properties.getProperty(name));
            }
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        for(String viewerSpecification: viewerSpecifications)
            Viewer.make(viewerSpecification, arena);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
    }

    /** Sucht das Properties-File.
     * @param args Kommandozeilenargumente.
     * @return Properties-File. Nicht null.
     */
    private static Path findPropertiesFile(String... args) {
        if(args.length > 0)
            return Paths.get(args[0]);
        final String propertiesFilename = System.getProperty("miner.props",
                                                             System.getenv("MINER_PROPS"));
        if(propertiesFilename != null)
            return Paths.get(propertiesFilename);
        final String defaultFilename = "miner.properties";
        final Path propertiesFile = Paths.get(System.getProperty("user.dir")).resolve(defaultFilename);
        if(Files.exists(propertiesFile))
            return propertiesFile;
        return Paths.get(System.getProperty("user.home")).resolve(defaultFilename);
    }

}
