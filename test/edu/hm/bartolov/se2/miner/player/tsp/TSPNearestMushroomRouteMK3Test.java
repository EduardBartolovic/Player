/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.hm.bartolov.se2.miner.player.tsp;

import edu.hm.bartolov.se2.miner.player.tsp.old.TSPNearestMushroomRouteMK3;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Edo
 */
public class TSPNearestMushroomRouteMK3Test {
    
    public TSPNearestMushroomRouteMK3Test() {
    }
   
    @Test
    public void testGetRouteSimple() {
        
        final List<Position> want = new ArrayList<>(7);
        want.add(new Position(0,0,0));
        want.add(new Position(1,1,0));
        want.add(new Position(2,2,0));
        want.add(new Position(3,3,0));
        want.add(new Position(4,4,0));
        want.add(new Position(5,5,0));
        want.add(new Position(6,6,0));
        want.add(new Position(7,7,0));
        
        final Set<Position> input = new HashSet<>();
        input.add(new Position(1,1,0));
        input.add(new Position(5,5,0));
        input.add(new Position(2,2,0));
        input.add(new Position(4,4,0));
        input.add(new Position(3,3,0));
        input.add(new Position(6,6,0));
        input.add(new Position(0,0,0));
        
        
        final TSPNearestMushroomRouteMK3 SUT = new TSPNearestMushroomRouteMK3(new Position(0,0,0),new Position(7,7,0),input);
        // act
        List have = SUT.getRoute();
        // assert
        Assert.assertEquals(want, have);
    }
    @Test
    public void testGetRoute13MushRandom2188Perfect() {
        final List<Position> want = new ArrayList<>(7);
        want.add(new Position(35,47,0));
        want.add(new Position(42,42,0));
        want.add(new Position(45,23,0));
        want.add(new Position(5,8,0));
        want.add(new Position(125,25,0));
        want.add(new Position(130,23,0));
        want.add(new Position(127,90,0));
        want.add(new Position(180,90,0));
        want.add(new Position(250,154,0));
        want.add(new Position(250,5,0));
        want.add(new Position(154,250,0));
        want.add(new Position(50,100,0));
        
        
        
        //#2188 is Perfect(test1)13Mush: landscape=edu.hm.cs.rs.se2.miner.arena.landscape.Flatland(17/93;5/8;50/100;45/23;127/90;180/90;130/23;35/47;42/42;250/154;154/250;125/25;250/5)
    
        final Set<Position> input = new HashSet<>();
        
        input.add(new Position(5,8,0));
        input.add(new Position(45,23,0));
        input.add(new Position(127,90,0));
        input.add(new Position(180,90,0));
        input.add(new Position(130,23,0));
        input.add(new Position(35,47,0));
        input.add(new Position(42,42,0));
        input.add(new Position(250,154,0));
        input.add(new Position(154,250,0));
        input.add(new Position(125,25,0));
        input.add(new Position(250,5,0));
        
        
        final TSPNearestMushroomRouteMK3 SUT = new TSPNearestMushroomRouteMK3(new Position(17,93,0),new Position(50,100,0),input);
        // act
        List have = SUT.getRoute();
        //System.out.println(want);
        //System.out.println(have);
        // assert
        Assert.assertEquals(want, have);
    }
    
}
