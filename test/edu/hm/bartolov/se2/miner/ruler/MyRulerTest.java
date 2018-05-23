package edu.hm.bartolov.se2.miner.ruler;

import edu.hm.cs.rs.se2.miner.arena.full.CoreArena;
import edu.hm.cs.rs.se2.miner.arena.landscape.Flatland;
import edu.hm.cs.rs.se2.miner.arena.landscape.TestZone;
import edu.hm.cs.rs.se2.miner.common.Direction;
import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.common.State;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Eduard Bartolovic
 */
public class MyRulerTest {
    
    public MyRulerTest() {
    }

    @Test(timeout = 1000)
    public void testGetArenaSize() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        
        assertEquals(256,sut.getArenaSize());
        
    }

    @Test(timeout = 1000)
    public void testGetDestination() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        
        assertEquals(new Position(128,128,64),sut.getDestination());
    }
    
    @Test(timeout = 1000)
    public void testGetDestination2() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland("17/93;5/8;50/100")));
        
        assertEquals(new Position(50,100,64),sut.getDestination());
    }

    @Test(timeout = 1000)
    public void testGetMillisRemaining1() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        
        assertEquals(100000,sut.getMillisRemaining());
    }
    
    @Test(timeout = 1000)
    public void testGetMillisRemaining2() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        sut.surrender();
        assertEquals(0,sut.getMillisRemaining());
    }

    @Test(timeout = 1000)
    public void testGetMushroomsRemaining() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        final Set set = new HashSet<>();
        set.add(new Position(136,136,64));
        assertEquals(set,sut.getMushroomsRemaining());
    }
    
    @Test(timeout = 1000)
    public void testGetMushroomsRemaining2() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland("17/93;140/140;50/100;2/2")));
        final Set set = new HashSet<>();
        set.add(new Position(140,140,64));
        set.add(new Position(2,2,64));
        assertEquals(set,sut.getMushroomsRemaining());
    }
    
    @Test(timeout = 1000)
    public void testGetMushroomsRemaining3() {
        final CoreArena arena = new CoreArena(new Flatland());
        final Ruler sut = new MyRuler(arena);
        final Set set = new HashSet<>();
        arena.setFigure(new Position(135,136,64));
        sut.move(Direction.N);
        assertEquals(set,sut.getMushroomsRemaining());
    }
    
    @Test(timeout = 1000)
    public void testGetPosition() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        assertEquals(new Position(128,128,64),sut.getPosition());
    }

    @Test(timeout = 1000)
    public void testGetState() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        assertEquals(State.Running,sut.getState());
    }
    
    @Test(timeout = 1000)
    public void testGetState2() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        for(int counter = 0 ; counter < 127 ; counter++){
            sut.move(Direction.N);
        }
        assertEquals(State.Running,sut.getState());
        sut.move(Direction.N);
        assertEquals(State.Lost,sut.getState());
    }
    
    @Test(timeout = 1000)
    public void testGetTicks() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        assertEquals(0,sut.getTicks());
    }
    
    @Test(timeout = 1000)
    public void testGetTicks2() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        sut.move(Direction.N);
        assertEquals(2,sut.getTicks());
    }
    
    @Test(timeout = 1000)
    public void testGetTicks3() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        sut.move(Direction.W);
        assertEquals(2,sut.getTicks());
    }
    @Test(timeout = 1000)
    public void testGetTicks4() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        sut.move(Direction.S);
        assertEquals(2,sut.getTicks());
    }
    @Test(timeout = 1000)
    public void testGetTicks5() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        sut.move(Direction.E);
        sut.move(Direction.E);
        sut.move(Direction.E);
        assertEquals(6,sut.getTicks());
    }
    
    @Test(timeout = 1000)
    public void testGetTicks6() {
        final Ruler sut = new MyRuler(new CoreArena(new TestZone()));
        sut.move(Direction.E); // 5 up
        assertEquals(27,sut.getTicks());
    }
    
    @Test(timeout = 1000)
    public void testGetTicks7() {
        final Ruler sut = new MyRuler(new CoreArena(new TestZone()));
        sut.move(Direction.N); // 1 up
        assertEquals(3,sut.getTicks());
    }
    
    @Test(timeout = 1000)
    public void testGetTicks8() {
        final Ruler sut = new MyRuler(new CoreArena(new TestZone()));
        sut.move(Direction.E);
        assertEquals(27,sut.getTicks());
        sut.move(Direction.E);
        assertEquals(54,sut.getTicks());
        sut.move(Direction.N);
        assertEquals(57,sut.getTicks());
        sut.move(Direction.W);
        assertEquals(64,sut.getTicks());
        sut.move(Direction.S);
        assertEquals(67,sut.getTicks());
    }
    
    @Test(timeout = 1000)
    public void testGetTicks9() {
        final Ruler sut = new MyRuler(new CoreArena(new TestZone()));
        sut.move(Direction.E);
        sut.move(Direction.E);
        sut.move(Direction.N);
        sut.move(Direction.N);
        assertEquals(80,sut.getTicks());
    }
    

    @Test(timeout = 1000)
    public void testMove() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        sut.move(Direction.S);
        assertEquals(new Position(127,128,64),sut.getPosition());
        sut.move(Direction.S);
        assertEquals(new Position(126,128,64),sut.getPosition());
        sut.move(Direction.S);
        assertEquals(new Position(125,128,64),sut.getPosition());
        sut.move(Direction.W);
        assertEquals(new Position(125,127,64),sut.getPosition());
        sut.move(Direction.N);
        assertEquals(new Position(126,127,64),sut.getPosition());
        sut.move(Direction.E);
        assertEquals(new Position(126,128,64),sut.getPosition());
        
    }
    
    @Test(timeout = 1000 , expected = NullPointerException.class)
    public void testMove2() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        sut.move(null);
    }
    
    @Test(timeout = 1000 , expected = IllegalStateException.class)
    public void testMove3() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        sut.surrender();
        sut.move(Direction.S);
    }
    @Test(timeout = 1000 , expected = IllegalStateException.class)
    public void testMove4() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        while(true){
            sut.move(Direction.S);
        }
    }
    
    @Test(timeout = 1000)
    public void testSurrender() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        assertEquals(State.Surrendered,sut.surrender().getState());
    }
    
    @Test(timeout = 1000)
    public void testTakeRadarImage() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        assertEquals(7,sut.takeRadarImage().size());   
    }

    @Test(timeout = 1000)
    public void testTakeRadarImage2() {
        final Ruler sut = new MyRuler(new CoreArena(new Flatland()));
        final List<List<Integer>> map = new ArrayList<>(7);
        final List<Integer> row = new ArrayList<>(7);
        for(int c = 0 ; c < 7 ; c++)
            row.add(64);
        for(int c = 0 ; c < 7 ; c++)
            map.add(row);
        
        assertEquals(map,sut.takeRadarImage());
    }
    
    @Test(timeout = 1000)
    public void testTakeRadarImage3() {
        final Ruler sut = new MyRuler(new CoreArena(new TestZone()));
        final List<List<Integer>> map = new ArrayList<>(15);
        final List<Integer> rowOut = new ArrayList<>(15);
        for(int c = 0 ; c < 15 ; c++)
            rowOut.add(Integer.MAX_VALUE);
        
        for(int c = 0 ; c < 4 ; c++)
            map.add(rowOut);
        
        map.add(Arrays.asList(new Integer[]{Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,1,6,11,16,21,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE}));
        map.add(Arrays.asList(new Integer[]{Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,2,7,12,17,22,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE}));
        map.add(Arrays.asList(new Integer[]{Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,3,8,13,18,23,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE}));
        map.add(Arrays.asList(new Integer[]{Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,4,9,14,19,24,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE}));
        map.add(Arrays.asList(new Integer[]{Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,5,10,15,20,25,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE}));
        
        for(int c = 0 ; c < 6 ; c++)
            map.add(rowOut);

        assertEquals(map,sut.takeRadarImage());
    }
}