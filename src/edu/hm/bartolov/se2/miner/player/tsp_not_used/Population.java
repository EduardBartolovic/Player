
package edu.hm.bartolov.se2.miner.player.tsp_not_used;

import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**Population.
 * Manages a population of candidate tours.
 * @version MK1
 * @author Edo
 */
public class Population {
    /**
     * SpielRegeln.
     */
    private final Ruler ruler;
    /**
     * Holds population of tours.
     */
    private final List<List<Position>> tours;
    
    /**Construct a population.
     * 
     * @param populationSize from typ int
     * @param initialise from typ boolean
     * @param ruler from typ gamerules
     */
    public Population(int populationSize, boolean initialise,Ruler ruler) {
        this.ruler=ruler;
        tours = new ArrayList<>(populationSize);
        // If we need to initialise a population of tours do so
        if (initialise) {
            // Loop and create individuals
            for (int index = 0; index < populationSize; index++) {
                final List<Position> newTour = new ArrayList<>();
                tours.add(generateIndividual(newTour));
            }
        }
    }
    
    /**
     * saves a tour.
     * @param index from typ int
     * @param tour from typ List<Position>
     */
    public void saveTour(int index, List<Position> tour) {
        tours.add(index, tour);// was once set+++++++++++++++++++++++++++++++++++++++++++++++++++++
    }
    
    /**
     * Gets a tour from population.
     * @param index from typ int
     * @return tour at index
     */
    public List<Position> getTour(int index) {
        return tours.get(index);
    }

    /** Gets the best tour in the population.
     * 
     * @return best tour
     */
    List<Position> getFittest() {
        List<Position> fittest = tours.get(0);
        // Loop through individuals to find fittest
        for (int index = 1; index < populationSize(); index++) {
            if (getFitness(fittest) <= getFitness(getTour(index))) {
                fittest = getTour(index);
            }
        }
        return fittest;
    }
    /**
     * Gets population size.
     * @return length of tour
     */
    int populationSize() {
        return tours.size();
    }
    /**
     * shuffle the tour.
     * @param route from typ List<Position>
     * @return route shuffled
     */
    private List<Position> generateIndividual(List<Position> route) {
        // Loop through all our Mushrooms and add them to our tour
        for (int cityIndex = 0; cityIndex < TourManager.numberOfPosition(); cityIndex++) {
          route.add(TourManager.getPosition(cityIndex));
        }
        // Randomly reorder the tour
        Collections.shuffle(route);
        return route;
    }
    /**
     * Gets the tours fitness.
     * @param route from typ List<Position>
     * @return fitness of System.
     */
    private double getFitness(List<Position> route) {
        return 1/(double)getDistance(route);
    }
    
    /**
     * Gets the total distance of the tour.
     * @param route from typ List<Position>
     * @return total distance of tour
     */ 
    private int getDistance(List<Position> route){
        final List<Position> listToTest = new ArrayList<>(route);
        listToTest.add(ruler.getDestination());
        return new TSPGA(ruler).totalDistance(listToTest);
        
    }

}