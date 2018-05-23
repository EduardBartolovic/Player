/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.7.4
 * violet (Intel Core i7 CPU 920/2.67GHz, 8 cores, 2668 MHz, 12032 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.arena.landscape.shootout;

import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Pilzpositionen in einem regelmaessigen Gitter.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-05-03
 */
public class FlatlandGrid extends FlatlandShootout {
    /** 10x10 Pilze.
     */
    public FlatlandGrid() {
        this("10");
    }

    /** Quadratisches Gitter mit gegebener Anzahl Pilze Kantenlaenge.
     * @param gridsize Anzahl Pilze im Quadrat.
     */
    public FlatlandGrid(String gridsize) {
        super(Integer.parseInt(gridsize), -1);
    }

    @Override
    BiFunction<Integer, Integer, Stream<Position>> positionsGenerator() {
        return (gridsize, unused) -> {
            if(gridsize < 1)
                throw new IllegalArgumentException("required grid size at least 1, but got " + gridsize);
            final int offset = getSize()/gridsize/2;
            return Stream.iterate(0, x -> x + 1)
                    .limit(gridsize)
                    .flatMap(x -> Stream.iterate(0, y -> y + 1)
                            .limit(gridsize)
                            .map(y -> getPositionAt(getSize()*x/gridsize + offset,
                                                    getSize()*y/gridsize + offset)));
        };
    }


}
