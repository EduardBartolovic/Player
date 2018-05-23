package edu.hm.bartolov.se2.miner.landscape;

import edu.hm.cs.rs.se2.miner.arena.Landscape;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.HashSet;
import java.util.Set;
/** (C) 2017, E.Bartolovic, bartolov@hm.edu
 * Java 1.8.0_121, Windows 10
 * Landscape von Anfang April 2017.
 * @author E.Bartolovic
 * @version 1 
 **/
public class MyLand1 implements Landscape {
    
    private static final int ALTITUDE = 20;
    /** Zielposition. */
    private final Position destination;

    /** Kantenlaenge der Landschaft. */
    private final int size;

    /** Startposition. */
    private final Position start;
    
    public MyLand1(){
        this(20);
    }
    
    public MyLand1(int size){
        this.size = size;
        this.destination = new Position(0,size - 1,ALTITUDE);
        this.start = new Position(0,0,ALTITUDE);
    }
    
    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public Position getStart() {
        return this.start;
    }

    @Override
    public Position getDestination() {
        return this.destination;
    }

    @Override
    public int getAltitude(int latitude, int longitude) {
        return ALTITUDE;
    }

    @Override
    public Set<Position> getMushrooms() {
        Set<Position> mushs = new HashSet<>();
        for(int mushLong = 0;mushLong < size;mushLong = mushLong+2){
            for(int mushLat = 1;mushLat*2 < size;mushLat++){
                mushs.add(new Position(mushLat*2,mushLong,ALTITUDE));
            }       
        }
        return mushs;
    }
    
}