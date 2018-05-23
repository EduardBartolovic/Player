
package edu.hm.bartolov.se2.miner.player.tsp_not_used;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * TSP_GA_MK1.
 * sucht besten weg mit hilfe von genetic Algorithm
 * Changelog:
 * 
 * Optimization:
 * 
 * Ideen:
 * ---
 * @author E.Bartolovic 
 * @version MK1
 */
public class TSPGA extends RouteMK2{
    /**
     * number of Population.
     */
    private static final int POPULATIONSIZE = 50;
    /**
     * Spielregeln.
     */
    private final Ruler ruler;
    /**
     * Constructor.
     * @param ruler 
     */
    public TSPGA(Ruler ruler){
        super(ruler);
        this.ruler = ruler;
    }

    @Override
    public List<Position> getRoute() {
        // Create and add our cities
        final Iterator<Position> curser = ruler.getMushroomsRemaining().iterator();
        while(curser.hasNext()){
            TourManager.addPosition(curser.next());
        }

        // Initialize population
        Population pop = new Population(POPULATIONSIZE, true,ruler);
        System.out.println("Initial distance: " + totalDistance(pop.getFittest()));

        // Evolve population for 100 generations
        pop = GeneticAlgorithm.evolvePopulation(pop,ruler);
        for (int index = 0; index < 100; index++) {
            pop = GeneticAlgorithm.evolvePopulation(pop,ruler);
        }

        // Print final results
        /*System.out.println("Finished");
        System.out.println("Final distance: " + pop.getFittest().getDistance());
        System.out.println("Solution:");
        System.out.println(pop.getFittest());*/
        final List<Position> route = pop.getFittest();
        //route.add(ruler.getDestination());
        return Collections.unmodifiableList(route);
    }
}