package edu.hm.bartolov.se2.miner.player.tsp_not_used;

import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Genetic Algorithm.
 * Manages algorithms for evolving population
 * Changelog:
 * 
 * Optimization:
 * 
 * Ideen:
 * ---
 * @author E.Bartolovic 
 * @version MK1
 */
public class GeneticAlgorithm {

    /**
     * MUATIONSRATE.
     */
    private static final double MUTATIONRATE = 0.015;
    /**
     * size of Tour.
     */
    private static final int SIZEOFTOUR = 5;
    /**
     * eltism.
     */
    private static final boolean ELITSM = true;

    /**
     * Evolves a population over with new generation.
     * @param generation from typ Population
     * @param ruler from typ game rules
     * @return newGeneration
     */
    public static Population evolvePopulation(Population generation,Ruler ruler) {
        final Population newGeneration = new Population(generation.populationSize(), false,ruler);

        // Keep our best individual if ELITSM is enabled
        int elitsmOffset = 0;
        if (ELITSM) {
            newGeneration.saveTour(0, generation.getFittest());
            elitsmOffset = 1;
        }

        // Crossover population
        // Loop over the new population's size and create individuals from
        // Current population
        for (int counter = elitsmOffset; counter < newGeneration.populationSize(); counter++) {
            // Select parents
            final List<Position> parent1 = tournamentSelection(generation,ruler);
            final List<Position> parent2 = tournamentSelection(generation,ruler);
            // Crossover parents
            final List<Position> child = crossover(parent1, parent2,ruler);
            // Add child to new population
            newGeneration.saveTour(counter, child);
        }

        // Mutate the new population a bit to add some new genetic material
        for (int counter = elitsmOffset; counter < newGeneration.populationSize(); counter++) {
            mutate(newGeneration.getTour(counter));
        }
        
        return newGeneration;
    }

    /**
     * Applies crossover to a set of parents and creates offspring.
     * @param parent1 from typ tour
     * @param parent2 from typ tour
     * @param ruler from typ gamerule
     * @return child 
     */
    public static List<Position> crossover(List<Position> parent1, List<Position> parent2,Ruler ruler) {
        // Create new child tour
        final List<Position> child = new ArrayList<>();

        // Get start and end sub tour positions for parent1's tour
        final int startPos ;
        final int endPos ;
        final int firstrandom = (int) (Math.random() * parent1.size());
        final int seccondrandom = (int) (Math.random() * parent1.size());
        if(firstrandom<seccondrandom){
            startPos =firstrandom;
            endPos =seccondrandom;
        }else{
            endPos =firstrandom;
            startPos =seccondrandom;
        }
        
        // Loop and add the sub tour from parent1 to child
        for (int counter = startPos; counter < endPos; counter++) {
                child.set(counter, parent1.get(counter));
        }

        // Loop through parent2's mushroom tour
        for (int outerCounter = 0; outerCounter < parent2.size(); outerCounter++) {
            // If child doesn't have the mushroom add it
            if (!child.contains(parent2.get(outerCounter))) {
                // Loop to find a spare position in the child's tour
                for (int innerCounter = 0; innerCounter < child.size(); innerCounter++) {
                    // set position found, add mushroom
                    if (child.get(innerCounter) == null) {
                        child.set(innerCounter, parent2.get(outerCounter));
                        //build an loop stopper like break but without break :D-------------------------------------+++
                    }
                }
            }
        }
        return child;
    }

    /**
     * Mutate a tour using swap mutation.
     * @param tour 
     */
    private static void mutate(List<Position> tour) {
        // Loop through tour cities
        for(int tourPos1=0; tourPos1 < tour.size(); tourPos1++){
            
            // Apply mutation rate
            if(Math.random() < MUTATIONRATE){
                // Get a second random position in the tour
                final Random randomnumber = new Random(); 
                final int tourPos2 = randomnumber.nextInt(tour.size());

                // Get the cities at target position in tour
                final Position city1 = tour.get(tourPos1);
                final Position city2 = tour.get(tourPos2);

                // Swap them around
                tour.set(tourPos2, city1);
                tour.set(tourPos1, city2);
            }
        }
    }

    /**
     * Selects candidate tour for crossover.
     * @param generation from typ population
     * @param ruler from typ game rule
     * @return besttour for be a parent
     */
    private static List<Position> tournamentSelection(Population generation,Ruler ruler) {
        // Create a tournament population
        final Population tournament = new Population(SIZEOFTOUR, false,ruler);
        // For each place in the tournament get a random candidate tour and
        // add it
        for (int index = 0; index < SIZEOFTOUR; index++) {
            final int randomId = (int) (Math.random() * generation.populationSize());
            tournament.saveTour(index, generation.getTour(randomId));
        }
        // return the fittest tour
        return tournament.getFittest();
    }
}