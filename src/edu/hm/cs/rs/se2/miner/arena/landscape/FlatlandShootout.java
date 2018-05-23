package edu.hm.cs.rs.se2.miner.arena.landscape.shootout;

import edu.hm.cs.rs.se2.miner.arena.landscape.Flatland;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Basisklasse fuer Flatland-Landschaften in einem Shootout.
 * Die Landschaften unterscheiden sich in den Pilzpositionen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-05-12
 */
public abstract class FlatlandShootout extends Flatland {
    /** Erster Parameter fuer den Algorithmus, der Pilzpositionen liefert. */
    private final int parameter0;

    /** Weiterer Parameter fuer den Algorithmus, der Pilzpositionen liefert. */
    private final int parameter1;

    /** Die Positionen der Pilze. */
    private final Set<Position> mushrooms = new HashSet<>();

    /** Ctor fuer eine neue Landschaft.
     * @param parameter0 Erster Parameter fuer den Algorithmus, der Pilzpositionen liefert.
     * @param parameter1 Weiterer Parameter fuer den Algorithmus, der Pilzpositionen liefert.
     */
    public FlatlandShootout(int parameter0, int parameter1) {
        this.parameter0 = parameter0;
        this.parameter1 = parameter1;
        mushrooms.addAll(positionsGenerator().apply(parameter0, parameter1)
                                 .collect(Collectors.toSet()));
        assert !mushrooms.isEmpty(): "landscape has at least 1 mushroom";
    }

    /** Liefert eine Funktion, die mit zwei Parametern einen Stream von Pilzpositionen berechnet.
     * @return Funktion fuer Pilzpositionen.
     */
    abstract BiFunction<Integer, Integer, Stream<Position>> positionsGenerator();

    @Override
    public Position getDestination() {
        return getStart();
    }

    @Override
    public Set<Position> getMushrooms() {
        return Collections.unmodifiableSet(mushrooms);
    }

    @Override
    public Position getStart() {
        return getPositionAt(0, 0);
    }
}
