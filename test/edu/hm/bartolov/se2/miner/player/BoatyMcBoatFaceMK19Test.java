package edu.hm.bartolov.se2.miner.player;

import edu.hm.bartolov.se2.miner.ruler.MyRuler;
import edu.hm.cs.rs.se2.miner.arena.Landscape;
import edu.hm.cs.rs.se2.miner.arena.full.CoreArena;
import edu.hm.cs.rs.se2.miner.arena.full.FullArena;
import edu.hm.cs.rs.se2.miner.arena.landscape.Flatland;
import edu.hm.cs.rs.se2.miner.player.Player;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import java.io.IOException;
import org.junit.Test;
import edu.hm.cs.rs.se2.miner.arena.landscape.shootout.FlatlandBox2;
import edu.hm.cs.rs.se2.miner.arena.landscape.shootout.FlatlandCircle;
import edu.hm.cs.rs.se2.miner.arena.landscape.shootout.FlatlandCircle2;
import edu.hm.cs.rs.se2.miner.arena.landscape.shootout.FlatlandCross;
import edu.hm.cs.rs.se2.miner.arena.landscape.shootout.FlatlandGrid;
import edu.hm.cs.rs.se2.miner.arena.landscape.shootout.FlatlandRandom;
import edu.hm.cs.rs.se2.miner.common.State;
/**
 *
 * @author Eduard Bartolovic
 */
public class BoatyMcBoatFaceMK19Test {
    
    public BoatyMcBoatFaceMK19Test() {
    }
    
    @Test(timeout = 30000)
    public void testRun0() throws IOException {
        final Landscape landscape = new Flatland();
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("smoke  (1 Mushroom)(64): "+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        System.out.println("smoke  (1 Mushroom)(64): "+arena.getTicks());
        
    }

    @Test(timeout = 30000)
    public void testRun1() throws IOException {
        final Landscape landscape = new FlatlandBox2();
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("FlatBox (128 Mushroom)(2160): "+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        assert arena.getTicks() == 2160;
        System.out.println("FlatBox (128 Mushroom)(2160): "+arena.getTicks());
        
    }
    
    @Test(timeout = 30000)
    public void testRun2() throws IOException {
        final Landscape landscape = new FlatlandCircle();
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Circle (32 Mushroom)(2004): "+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        assert arena.getTicks() == 2004;
        System.out.println("Circle (32 Mushroom)(2004): "+arena.getTicks());
        
    }
    
    @Test(timeout = 30000)
    public void testRun3() throws IOException {
        final Landscape landscape = new FlatlandCircle2();
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Circle2 (96 Mushroom)(3492): "+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        System.out.println("Circle2 (96 Mushroom)(3492): "+arena.getTicks());
        
    }
    
    @Test(timeout = 30000)
    public void testRun4() throws IOException {
        final Landscape landscape = new FlatlandCross();
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Cross (64 Mushroom)(2044): "+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        System.out.println("Cross (64 Mushroom)(2044): "+arena.getTicks());
        
    }
    
    @Test(timeout = 30000)
    public void testRun5() throws IOException {
        final Landscape landscape = new FlatlandGrid();
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Grid (100 Mushroom)(6288): "+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        System.out.println("Grid (100 Mushroom)(6288): "+arena.getTicks());
        
    }
    
    @Test(timeout = 30000)
    public void testRun6() throws IOException {
        final Landscape landscape = new FlatlandRandom();
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Random (383 Mushroom)(11312):"+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        System.out.println("Random (383 Mushroom)(11312):"+arena.getTicks());
        
    }
    
    @Test(timeout = 30000)
    public void testRun7() throws IOException {
        final Landscape landscape = new Flatland("17/93;5/8;50/100;45/23;127/90;180/90;130/23;35/47;42/42;250/154;154/250;125/25;250/5");
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Random2 (11 Mushrooms)(2188):"+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        assert arena.getTicks() == 2188;
        System.out.println("Random2 (11 Mushrooms)(2188):"+arena.getTicks());
        
    }
    
    @Test(timeout = 30000)
    public void testRun8() throws IOException {
        final Landscape landscape = new Flatland("17/93;5/8;50/100;130/130;135/135;130/130;138/120;115/110;30/30;3/3;4/4;5/5;6/6;7/7;8/8;9/9;10/10;11/11;12/12;13/13;45/23;127/90;180/90;130/23;35/47;42/42;250/154;154/250;125/25;250/5;36/9;44/90;57/5;1/99;67/88;90/91;57/108;77/99;22/54;48/55");
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Random3 (37 Mushrooms)(2872):"+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        System.out.println("Random3 (37 Mushrooms)(2872):"+arena.getTicks());
        
    } 
    
    @Test(timeout = 30000)
    public void testRandom4() throws IOException {
        final Landscape landscape = new Flatland("17/93;5/8;50/100;45/23;127/90;180/90;130/23;34/55;35/47;42/42;250/154");
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Random4 (9 Mushrooms)(1596):"+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;  
        assert arena.getTicks() == 1596;
        System.out.println("Random4 (9 Mushrooms)(1596):"+arena.getTicks());
    }
    
    @Test//(timeout = 30000)
    public void testRun10() throws IOException {
        final Landscape landscape = new Flatland("17/93;2/2;5/8;6/6;7/7;8/8;8/9;99/98;45/23;127/90;180/90;130/23;34/55;35/47;42/42;55/23;42/42;50/154");
        final FullArena arena = new CoreArena(landscape);//,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Random5 (15 Mushrooms)(1254):"+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        assert arena.getTicks() == 1254;
        System.out.println("Random5 (15 Mushrooms)(1254):"+arena.getTicks());
        
    }
    
    @Test(timeout = 30000)
    public void testLine10() throws IOException {
        final Landscape landscape = new Flatland("10/10;1/1;20/20;11/11;12/12;13/13;14/14;15/15;16/16;17/17;18/18;19/19");
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Line (10 Mushrooms)(112):"+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        assert arena.getTicks() == 122;
        System.out.println("Line (10 Mushrooms)(112):"+arena.getTicks());
        
    }
    
    @Test(timeout = 30000)
    public void testLine20() throws IOException {
        final Landscape landscape = new Flatland("10/1;1/1;30/1;11/1;12/1;13/1;14/1;15/1;16/1;17/1;18/1;19/1;20/1;21/1;22/1;23/1;24/1;24/01;26/1;27/1;28/1;29/1");
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("Line (20 Mushrooms)(76):"+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        assert arena.getTicks() == 76;
        System.out.println("Line (20 Mushrooms)(76):"+arena.getTicks());
        
    }
    
    @Test(timeout = 30000)
    public void testRun12() throws IOException {
        final Landscape landscape = new Flatland("10/10;20/20;20/20");
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("MushOnDestination (1 Mushrooms)(40):"+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        assert arena.getTicks() == 40;
        System.out.println("MushOnDestination (1 Mushrooms)(40):"+arena.getTicks());        
    }
    
    @Test(timeout = 30000)
    public void testRun13() throws IOException {
        final Landscape landscape = new Flatland("10/10;20/20;20/20;45/10;1/1");
        final FullArena arena = new CoreArena(landscape,30000);
        final Ruler ruler = new MyRuler(arena);
        //final Viewer viewer = new ConsoleReporter(arena);
        final Player player = new BoatyMcBoatFaceMK19(ruler);
        System.out.println("MushOnDestination2 (3 Mushrooms)(212):"+arena.getTicks());
        player.run();
        assert arena.getState() == State.Completed;
        assert arena.getTicks() == 212;
        System.out.println("MushOnDestination2 (3 Mushrooms)(212):"+arena.getTicks());        
    }
}
