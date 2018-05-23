package edu.hm.bartolov.se2.miner.player.tsp.tools;

import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Edo
 */
public class HeapAlgorithm {
    
    private final List<Position[]> allRoutes;
    
    private Position[] bestRoute;
    
    private int bestTime;

    public HeapAlgorithm() {
        allRoutes = new ArrayList<>();
    }
    
    //Generating permutation using Heap Algorithm
//    public void heapPermutation(Position a[], int size, int size){
//        // if size becomes 1 then prints the obtained
//        // permutation
//        if (size == 1)
//            allRoutes.add(a);
// 
//        for (int i=0; i<size; i++){
//            heapPermutation(a, size-1, size);
// 
//            // if size is odd, swap first and last
//            // element
//            if (size % 2 == 1){
//                final Position temp = a[0];
//                a[0] = a[size-1];
//                a[size-1] = temp;
//            }else{ // If size is even, swap it and last element
//                final Position temp = a[i];
//                a[i] = a[size-1];
//                a[size-1] = temp;
//            }
//        }
//    }

    public List<Position[]> getAllRoutes() {
        return Collections.unmodifiableList(allRoutes);
    }
    
     public int getBestRoute() {
        return bestTime;
    }
    
    
    public void generate(Position[] a ,int size){
        final int[] c = new int[size];
        for( int i = 0; i < size; i++)
            c[i] = 0;
        
        allRoutes.add(a);

        int i = 0;
        while (i < size){
            if (c[i] < i){
                if (i%2==0){
                    final Position temp = a[0];
                    a[0] = a[i];
                    a[i] = temp;
                }else{
                    final Position temp = a[c[i]];
                    a[c[i]] = a[1];
                    a[i] = temp;
                }
                allRoutes.add(a);
                c[i]++;
                i = 0;
            }else{
                c[i] = 0;
                i++;
            }  
        }
    }
    
    public static List<Position[]> generateIntelligent(Position[] a ,int size){
        final List<Position[]> allRoutes = new ArrayList<>();
        
        final int[] c = new int[size];
        for( int i = 0; i < size; i++)
            c[i] = 0;
        
        allRoutes.add(a);
        int counter = 0;
        int i = 0;
        while (i < size){
            if (c[i] < i){
                if (i%2==0){
                    final Position temp = a[0];
                    a[0] = a[i];
                    a[i] = temp;
                }else{
                    final Position temp = a[c[i]];
                    a[c[i]] = a[1];
                    a[i] = temp;
                }
                if(counter == size){
                    allRoutes.add(a);
                    counter=0;
                }else{
                    counter++;
                }
                c[i]++;
                i = 0;
            }else{
                c[i] = 0;
                i++;
            }  
        }
        
        System.out.println("routesize"+allRoutes.size());
        return allRoutes;
    }
    
}
