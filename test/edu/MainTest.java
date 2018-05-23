
package edu;

import edu.hm.cs.rs.se2.miner.common.State;
import org.junit.Assert;
import org.junit.Test;
import java.util.Properties;
import edu.hm.cs.rs.se2.miner.arena.Landscape;
import edu.hm.cs.rs.se2.miner.arena.full.FullArena;
import edu.hm.cs.rs.se2.miner.player.Player;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainTest {
    
    public MainTest() {
    }

    @Test(timeout = 60_000) public void testPlayer1Flatland () throws Exception {
        // arrange
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        
        final Properties properties = new Properties();
        String args = "C:\\Users\\Edo\\Documents\\NetBeansProjects\\Minecraft\\test\\testProperties\\miner1.properties";
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "edu.hm.cs.rs.se2.miner.ruler.LocalRuler");
            playerSpecification = properties.getProperty("player", "edu.hm.cs.rs.se2.miner.player.DummyPlayer");
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
        State want = State.Completed;
        // act
        State have = ruler.getState();
        // assert
        Assert.assertEquals(want, have);
    }
    @Test(timeout = 60_000) public void testPlayer2SchachbrettFlatland() throws Exception {
        // arrange
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        
        final Properties properties = new Properties();
        String args = "C:\\Users\\Edo\\Documents\\NetBeansProjects\\Minecraft\\test\\testProperties\\miner2.properties";
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "edu.hm.cs.rs.se2.miner.ruler.LocalRuler");
            playerSpecification = properties.getProperty("player", "edu.hm.cs.rs.se2.miner.player.DummyPlayer");
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
        final State want = State.Completed;
        final int want2 = 844;// perfect time
        // act
        final State have = ruler.getState();
        final int have2 = arena.getTicks();
        // assert
        Assert.assertEquals(want, have);
        Assert.assertEquals(want2, have2);
    }
    @Test(timeout = 60_000) public void testPlayer3DiagonalFlatland() throws Exception {
        // arrange
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        
        final Properties properties = new Properties();
        String args = "C:\\Users\\Edo\\Documents\\NetBeansProjects\\Minecraft\\test\\testProperties\\miner3.properties";
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "");
            playerSpecification = properties.getProperty("player", "");
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
        final State want = State.Completed;
        final int want2 = 2040;// perfect time
        // act
        State have = ruler.getState();
        final int have2 = arena.getTicks();
        // assert
        Assert.assertEquals(want, have);
        Assert.assertEquals(want2, have2);
    }
    @Test(timeout = 60_000) public void testPlayer4DiagonalRandomFlatland() throws Exception {
        // arrange
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        
        final Properties properties = new Properties();
        String args = "C:\\Users\\Edo\\Documents\\NetBeansProjects\\Minecraft\\test\\testProperties\\miner4.properties";
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "edu.hm.cs.rs.se2.miner.ruler.LocalRuler");
            playerSpecification = properties.getProperty("player", "edu.hm.cs.rs.se2.miner.player.DummyPlayer");
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
        final State want = State.Completed;
        final int want2 = 2040;// perfect time
        // act
        State have = ruler.getState();
        final int have2 = arena.getTicks();
        // assert
        Assert.assertEquals(want, have);
        Assert.assertEquals(want2, have2);
    }
    @Test(timeout = 60_000) public void testPlayer5DiagonalRandomDestinationOpositeFlatland() throws Exception {
        // arrange
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        
        final Properties properties = new Properties();
        String args = "C:\\Users\\Edo\\Documents\\NetBeansProjects\\Minecraft\\test\\testProperties\\miner5.properties";
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "edu.hm.cs.rs.se2.miner.ruler.LocalRuler");
            playerSpecification = properties.getProperty("player", "edu.hm.cs.rs.se2.miner.player.DummyPlayer");
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
        final State want = State.Completed;
        final int want2 = 1020;// perfect time
        // act
        State have = ruler.getState();
        final int have2 = arena.getTicks();
        // assert
        Assert.assertEquals(want, have);
        Assert.assertEquals(want2, have2);
    }
    @Test(timeout = 60_000) public void testPlayer6Diagonal_255_0_255_128Flatland() throws Exception {
        // arrange
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        
        final Properties properties = new Properties();
        String args = "C:\\Users\\Edo\\Documents\\NetBeansProjects\\Minecraft\\test\\testProperties\\miner6.properties";
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "edu.hm.cs.rs.se2.miner.ruler.LocalRuler");
            playerSpecification = properties.getProperty("player", "edu.hm.cs.rs.se2.miner.player.DummyPlayer");
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
        final State want = State.Completed;
        final int want2 = 2040;// perfect time
        // act
        State have = ruler.getState();
        final int have2 = arena.getTicks();
        // assert
        Assert.assertEquals(want, have);
        Assert.assertEquals(want2, have2);
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
    @Test(timeout = 60_000) public void testPlayer7SchachbrettFlatland() throws Exception {
        // arrange
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        
        final Properties properties = new Properties();
        String args = "C:\\Users\\Edo\\Documents\\NetBeansProjects\\Minecraft\\test\\testProperties\\miner7.properties";
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "edu.hm.cs.rs.se2.miner.ruler.LocalRuler");
            playerSpecification = properties.getProperty("player", "edu.hm.cs.rs.se2.miner.player.DummyPlayer");
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
        final State want = State.Completed;
        final int want2 = 898;// perfect time
        // act
        final State have = ruler.getState();
        final int have2 = arena.getTicks();
        // assert
        Assert.assertEquals(want, have);
        Assert.assertEquals(want2, have2);
    }
    @Test(timeout = 60_000) public void testPlayer8LikaFlatland() throws Exception {
        // arrange
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        
        final Properties properties = new Properties();
        String args = "C:\\Users\\Edo\\Documents\\NetBeansProjects\\Minecraft\\test\\testProperties\\miner8.properties";
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "edu.hm.cs.rs.se2.miner.ruler.LocalRuler");
            playerSpecification = properties.getProperty("player", "edu.hm.cs.rs.se2.miner.player.DummyPlayer");
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
        final State want = State.Completed;
        final int want2 = 12824;// perfect time
        // act
        final State have = ruler.getState();
        final int have2 = arena.getTicks();
        // assert
        Assert.assertEquals(want, have);
        Assert.assertEquals(want2, have2);
    }
    @Test(timeout = 60_000) public void testPlayer9MushOnStart5M() throws Exception {
        // arrange
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        
        final Properties properties = new Properties();
        String args = "C:\\Users\\Edo\\Documents\\NetBeansProjects\\Minecraft\\test\\testProperties\\miner8.properties";
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "edu.hm.cs.rs.se2.miner.ruler.LocalRuler");
            playerSpecification = properties.getProperty("player", "edu.hm.cs.rs.se2.miner.player.DummyPlayer");
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
        final State want = State.Completed;
        final int want2 = 12824;// perfect time
        // act
        final State have = ruler.getState();
        final int have2 = arena.getTicks();
        // assert
        Assert.assertEquals(want, have);
        Assert.assertEquals(want2, have2);
    }
    @Test(timeout = 60_000) public void testPlayer10Flatland6Mush() throws Exception {
        // arrange
        final String landscapeSpecification;
        final String arenaSpecification;
        final String rulerSpecification;
        final String playerSpecification;
        
        final Properties properties = new Properties();
        String args = "C:\\Users\\Edo\\Documents\\NetBeansProjects\\Minecraft\\test\\testProperties\\miner10.properties";
        try(InputStream inputStream = Files.newInputStream(findPropertiesFile(args))) {
            properties.load(inputStream);
            landscapeSpecification = properties.getProperty("landscape", "edu.hm.cs.rs.se2.miner.landscape.Flatland");
            arenaSpecification = properties.getProperty("arena", "edu.hm.cs.rs.se2.miner.arena.full.CoreArena");
            rulerSpecification = properties.getProperty("ruler", "edu.hm.cs.rs.se2.miner.ruler.LocalRuler");
            playerSpecification = properties.getProperty("player", "edu.hm.cs.rs.se2.miner.player.DummyPlayer");
        }

        final Landscape landscape = Landscape.make(landscapeSpecification);
        final FullArena arena = FullArena.make(arenaSpecification, landscape);
        final Ruler ruler = Ruler.make(rulerSpecification, arena);
        final Player player = Player.make(playerSpecification, ruler);
        player.run();
        final State want = State.Completed;
        final int want2 = 12824;// perfect time
        // act
        final State have = ruler.getState();
        final int have2 = arena.getTicks();
        // assert
        Assert.assertEquals(want, have);
        Assert.assertEquals(want2, have2);
    }
    
}
