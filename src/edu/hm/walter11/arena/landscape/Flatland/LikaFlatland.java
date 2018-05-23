/* (C) 2017, Angelika Walter, walter11@hm.edu
 * Java 1.8.0_121
 **/
package edu.hm.walter11.arena.landscape.Flatland;

import edu.hm.cs.rs.se2.miner.arena.landscape.Flatland;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Own flatland landscape with random mushrooms. Apply seed for same landscapes.
 *
 * @author Angelika Walter, walter11@hm.edu
 * @version 20.04.2017
 */
public class LikaFlatland extends Flatland {

    private final Random random;

    private final Set<Position> mushrooms;

    private final Position start;

    private final Position destination;

    /**
     * My own flatland, random positions.
     */
    public LikaFlatland() {
        this(new Random(), 1234, 200);
    }

    /**
     * My own flatland, reproducible.
     *
     * @param seed To reproduce the landscape.
     */
    public LikaFlatland(int seed) {
        this(seed, 200);
    }

    /**
     * My own flatland. Defined number of mushrooms and reproducible.
     *
     * @param seed To reproduce the landscape.
     * @param numberMushrooms The number of mushrooms to create.
     */
    public LikaFlatland(int seed, int numberMushrooms) {
        this(new Random(seed), seed, numberMushrooms);
    }

    /**
     * My own flatland. Create landscape with given parameters.
     *
     * @param random The random generator.
     * @param seed Dummy.
     * @param numberMushrooms The number of mushrooms to create.
     */
    private LikaFlatland(Random random, int seed, int numberMushrooms) {
        this.random = random;

        final int latitudeStart = random.nextInt(256);
        final int longitudeStart = random.nextInt(256);
        final int altitudeStart = getAltitude(latitudeStart, longitudeStart);
        start = new Position(latitudeStart, longitudeStart, altitudeStart);

        final int latitudeDestination = random.nextInt(256);
        final int longitudeDestination = random.nextInt(256);
        final int altitudeDestination = getAltitude(latitudeDestination, longitudeDestination);
        destination = new Position(latitudeDestination, longitudeDestination, altitudeDestination);

        mushrooms = new HashSet<>();
        for (int index = 0; index < numberMushrooms; index++) {
            final int latitude = random.nextInt(256);
            final int longitude = random.nextInt(256);
            mushrooms.add(new Position(latitude, longitude, getAltitude(latitude, longitude)));
        }
        System.out.println(mushrooms);
    }

    @Override
    public Position getStart() {
        return start;
    }

    @Override
    public Position getDestination() {
        return destination;
    }

    @Override
    public Set<Position> getMushrooms() {
        return mushrooms;
    }
}
