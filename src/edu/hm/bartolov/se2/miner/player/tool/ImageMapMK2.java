package edu.hm.bartolov.se2.miner.player.tool;

import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageMapMK2 {
	
	/**
	 * The Map of the landscape.
	 * Shows zero when no radar Image of this Position has been taken yet.
	 */
	private final int[][] savedMap;
        
        /**
         * size of arena.
         */
        private final int arenaSize;
        /**
         * list of all position where a picture was taken,
         */
        private final Set<Position> picturePlaces;
        /**
	 * custom construktor.
	 * @param arenaSize as int
	 */
	public ImageMapMK2(int arenaSize){
            this.picturePlaces = new HashSet<>();
            this.savedMap =  new int[arenaSize][arenaSize] ;
            this.arenaSize = arenaSize;
            final int[] row = new int[arenaSize];
            for(int d = 0; d < arenaSize; d++){
                    row[d] = -1;
            }
                
            for(int c = 0; c < arenaSize; c++){
                savedMap[c] = row;
            }
            
	}
	/**
         * simple getter.
         * @param position from Typ Position
         * @return imageHeight
         */
	public int getImageHeightPosition(Position position){
            return savedMap[position.getLatitude()][position.getLongitude()];
	}
	/**
         * simple getter.
         * @param x from type int
         * @param y from type int
         * @return imageheight
         */
	public int getImageHeightCord(int x, int y){
            return savedMap[y][x];
	}
	/**
         * simple getter.
         * @return arenaSize
         */
	public int getImageSize(){
            return savedMap.length;
	}
	
	
	/**
	 * fill map with radar Image.
	 * @param radar The Radar Image that has been taken.
	 * @param player The Position of the player.
	 */
	public void fillMap(List<List<Integer>> radar, Position player){
                picturePlaces.add(player);
		int indexLong = player.getLongitude() - (radar.size()-1)/2;
		int indexLat = player.getLatitude() - (radar.size()-1)/2;
		int radarIndexLat = 0;
		
		for(int c = 0; c < arenaSize; c++){
                    final int[] newLine;
                    if(indexLat >= 0 && indexLat < arenaSize && radarIndexLat < radar.size()){
                            newLine = fillLine(radar.get(radarIndexLat) ,indexLong, indexLat);
                            savedMap[indexLat]= newLine;
                    }
                    indexLat++;
                    radarIndexLat++;
		}
                
	}
	/**
         * linefiller.
         * @param radarLine
         * @param indexLong
         * @param indexLat
         * @return line from type int[]
         */
	private int[] fillLine(List<Integer> radarLine ,int indexLong, int indexLat){
		
                final int[] retLine = savedMap[indexLat];
		for(int c = 0; c < radarLine.size(); c++){
                    if(indexLong >= 0 && indexLong < retLine.length){
                            retLine[indexLong] = radarLine.get(c);
                    }
                    indexLong++;
                    
		}
		return retLine;
	}
        /**
         * return boolean to say if position contains info about height.
         * @param position
         * @return boolean true if position not -1
         */
        public boolean containsInformation(Position position){
            return savedMap[position.getLatitude()][position.getLongitude()] != -1;
        }
        /**
         * return boolean to say if position contains info about height.
         * @param xCord from type int
         * @param yCord from type int
         * @return boolean true if position not -1
         */
        public boolean containsInformation(int xCord,int yCord){
            return savedMap[yCord][xCord] != -1;
        }
        /**
        * prints the height map on console.
        */
       public void printImageMap(){
           for(int counter = savedMap.length-1 ; counter != -1;counter--)
               System.out.println(Arrays.toString(savedMap[counter]));
       }
       
       public boolean wasPictureTakenOnPosition(Position position){
           return picturePlaces.contains(position);
       }
}

























