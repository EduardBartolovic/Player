/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.17
 * emma (Intel Core i7-4790 CPU/3.60GHz, 8 cores, 2200 MHz, 32128 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.arena.full;

import edu.hm.cs.rs.se2.miner.arena.Landscape;
import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.common.State;
import java.util.HashSet;
import java.util.Objects;
import java.util.Observable;
import java.util.Set;

/** Konkrete Arena.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-05
 */
public class CoreArena extends Observable implements FullArena {
    /**
     * Realzeitschranke fur ein Spiel.
     */
    private static final int DEFAULT_TIMEOUT_SECONDS = 100;

    /**
     * Gesammelte Pilze.
     */
    private final Set<Position> collectedMushrooms = new HashSet<>();

    /**
     * Maennchen.
     */
    private Position figure;

    /**
     * Landschaft.
     */
    private final Landscape landscape;

    /**
     * Kantenlaenge des Radarbildes.
     */
    private final int radarEdgeLength;

    /**
     * Spielzustand.
     */
    private State state;

    /**
     * Abgelaufene Spielzeit.
     */
    private int ticks;

    /**
     * Systemzeit, zu der das Spiel abbricht.
     */
    private final long timeoutAtSystemMillis;

    /**
     * Eine neue Arena.
     * @param landscape Landschaft, in der das Spiel ablaeuft. Nicht null.
     */
    public CoreArena(Landscape landscape) {
        this(landscape, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Eine neue Arena.
     * @param landscape Landschaft, in der das Spiel ablaeuft. Nicht null.
     * @param timeoutSeconds Maximale Realzeit bis zum Abbruch des Spieles.
     */
    public CoreArena(Landscape landscape, int timeoutSeconds) {
        this.landscape = Objects.requireNonNull(landscape);
        figure = landscape.getStart();
        state = State.Running;
        timeoutAtSystemMillis = System.currentTimeMillis() + 1_000 * timeoutSeconds;
        final int minRadarSize = 3;
        final int maxRadarSize = 20;
        radarEdgeLength = Math.abs(landscape.getMushrooms().hashCode()) % (maxRadarSize - minRadarSize) / 2 * 2 + minRadarSize;
        assert radarEdgeLength % 2 != 0: "radar image has odd size";
    }

    @Override
    public FullArena collectMushroom(Position mushroom) {
        boolean exists = false;
        for(Position aMushroom: landscape.getMushrooms())
            if(mushroom.equals(aMushroom))
                exists = true;
        if(!exists)
            throw new IllegalArgumentException("no mushroom at: " + mushroom);
        if(collectedMushrooms.contains(mushroom))
            throw new IllegalArgumentException("mushroom already collected: " + mushroom);
        collectedMushrooms.add(mushroom);
        setChanged();
        return this;
    }

    @Override
    public Position getFigure() {
        return figure;
    }

    @Override
    public Landscape getLandscape() {
        return landscape;
    }

    @Override
    public int getMillisRemaining() {
        return Math.max(0, (int)(timeoutAtSystemMillis - System.currentTimeMillis()));
    }

    @Override
    public Set<Position> getMushroomsCollected() {
        return collectedMushrooms;
    }

    @Override
    public int getRadarEdgeLength() {
        return radarEdgeLength;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public int getTicks() {
        return ticks;
    }

    @SuppressWarnings("PMD.UselessOverridingMethod")
    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public FullArena setFigure(Position newFigure) {
        if(!figure.equals(newFigure)) {
            figure = Objects.requireNonNull(newFigure);
            setChanged();
        }
        return this;
    }

    @Override
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    public CoreArena setState(State newState) {
        if(state != newState) {
            setChanged();
            this.state = Objects.requireNonNull(newState);
        }
        return this;
    }

    @Override
    public FullArena setTicks(int newTicks) {
        if(newTicks < 0)
            throw new IllegalArgumentException("negative tic: " + newTicks);
        if(ticks != newTicks) {
            ticks = newTicks;
            setChanged();
        }
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{F@"
               + getFigure() + ", "
               + state.toString().charAt(0) + ", "
               + ticks + "T, "
               + collectedMushrooms.size() + '/'
               + landscape.getMushrooms().size() + "M}";
    }

}
