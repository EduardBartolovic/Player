package edu.hm.bartolov.se2.miner.ruler;

import edu.hm.cs.rs.se2.miner.arena.full.FullArena;
import edu.hm.cs.rs.se2.miner.common.Event;
import edu.hm.cs.rs.se2.miner.common.Direction;
import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.common.State;
import edu.hm.cs.rs.se2.miner.ruler.Ruler;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/** (C) 2017, E.Bartolovic, bartolov@hm.edu
 * Java 1.8.0_121, Windows 10
 * Regel-Implementierung von Anfang April 2017.
 * @author E.Bartolovic
 * @version 2.0* 
 **/

 public final class MyRuler implements Ruler {
    /** 
     * arena contains informations about arena.
     **/
    private final FullArena arena;
    /**My Ruler.
     * @param arena from typ Fullarena
     **/
    public MyRuler(FullArena arena) {
        this.arena = arena;
    }
    
    /** Die Kantenlaenge der Arena.
     * @return Kantenlaenge. Echt positiv.
     */
    @Override
    public int getArenaSize() { 
        return arena.getLandscape().getSize();
    }
    
    /** Liefert die Zielposition.
     * Wenn der Spieler alle Pilze eingesammelt hat und diese Position erreicht,
     * endet das Spiel erfolgreich.
     * @return Zielposition. Nicht null.
     */
    @Override
    public Position getDestination() { //get Position of Destination
        return arena.getLandscape().getDestination();
    }
    
    /** Verbleibende Realzeit bis zum Abbruch.
     * @return Anzahl Millisekunden bis zum Abbruch. Nicht negativ.
     * 0 nach Timeout und nach Spielende.
     */
    @Override
    public int getMillisRemaining() { // get Time remaining
        final int timeRemaining;
        if(hasItEnded()){
            timeRemaining = 0;
            arena.notifyObservers(Event.TimedOut);
            arena.setState(State.Timedout);
        }else
            timeRemaining = arena.getMillisRemaining();
        return timeRemaining;
    }
    
    /** Positionen der verbleibenden Pilze.
     * @return Die Anzahl Elemente zeigt die Anzahl verbleibender Pilze an.
     * Nicht null. Kein Element null.
     */
    @Override
    public Set<Position> getMushroomsRemaining() { // get Position of remaining Mushrooms
        if(hasItEnded())
            throw new java.lang.IllegalStateException("Game is Over(getMush)");
        
        return arena.getMushroomsRemaining();
    }
    
    /** Die aktuelle Position des Maennchens.
     * @return Die Position. Nicht null.
     * Kann ausserhalb der Landschaft liegen, wenn das Spiel verloren ist.
     * Liegt ansonsten innerhalb der Landschaft.
     */
    @Override
    public Position getPosition() {  // get Position of figure
        //if(hasItEnded())
        //    throw new java.lang.IllegalStateException("Game is Over(getPos)");
        
        return arena.getFigure();
    }
    
    /** Liefert den Spielzustand.
     * @return Zustand. Nicht null.
     */
    @Override
    public State getState() {  // get game state
        return arena.getState();
    }
    
    /** Bisher verbrauchte Spielzeit.
     * @return Verbrauchte Spielzeit. Nicht negativ.
     */
    @Override
    public int getTicks() { // get time which is already used
        return arena.getTicks();
    }
    
    /** Bewegt das Maennchen.
     * Diese Methode kostet Spielzeit.
     * Benachrichtigt die Arena, eventuell auch mehrmals ueber mehrere aufeinander
     * folgenden Aenderungen.
     * @param direction Himmelsrichtung. Nicht null.
     * @return Spielzustand. Nicht null.
     * @throws NullPointerException wenn die Richtung null ist.
     * @throws IllegalStateException nach Spielende.
     * @see Event#Moved
     * @see Event#Lost
     * @see Event#Collected
     * @see Event#Completed
     */
    @Override
    public State move(Direction direction) { 
        if(direction == null)
            throw new java.lang.NullPointerException("Direction can not be Null");
        if(hasItEnded())
            throw new java.lang.IllegalStateException("Game is Over(move)");        
        
        final Position playersPosition = getPosition();
        if(getMushroomsRemaining().contains(playersPosition)){
            arena.collectMushroom(playersPosition);
            arena.notifyObservers(Event.Collected);
        }
        
        // add offset to Position
        final int newLongitude = playersPosition.getLongitude() + direction.getOffsetLongitude();  
        final int newLatitude = playersPosition.getLatitude() + direction.getOffsetLatitude();
        //setting up the new Position:
        
        final Position newPosition;
        if(newLatitude < 0 || newLatitude >= getArenaSize() || newLongitude < 0 || newLongitude >= getArenaSize())
            newPosition = new Position(newLatitude,newLongitude,-1);
        else
            newPosition = new Position(newLatitude, newLongitude,arena.getLandscape().getAltitude(newLatitude, newLongitude));
        
        arena.setFigure(newPosition); // move figure
        
        if(getMushroomsRemaining().contains(newPosition)){ //collecting Mushrooms
            arena.collectMushroom(newPosition);
            arena.notifyObservers(Event.Collected);
        }
        
        arena.setTicks(arena.getTicks() + timeCost(playersPosition,newPosition)); //calculate cost
        
        arena.setState(positionCheck(newPosition));
        arena.notifyObservers(Event.Moved);
        return getState();
    }
    
    /** Der Spieler gibt auf.
     * Ohne Wirkung nach Spielende.
     * Benachrichtigt die Arena.
     * @return Dieses Objekt.
     * @see State#Surrendered
     * @see Event#Surrendered
     */
    @Override
    public Ruler surrender() {
        if(!hasItEnded()){
            arena.setState(State.Surrendered);
            arena.notifyObservers(Event.Surrendered);
        }
        return this;
    }
    
    /** Ein quadratisches Bild der Gelaendehoehen in der Umgebung.
     * Das Maennchen steht auf dem Feld in der Mitte.
     * Felder ausserhalb der Arena haben die Hoehe MAX_VALUE.
     * Diese Methode kostet Spielzeit.
     * Benachrichtigt die Arena.
     * @return n Listen mit jeweils n Elementen. n = ungerade Kantenlaenge des Radarbildes.
     * Nicht null und kein Element null.
     * Die aeussere Liste sind Zeilen des Radarbildes nach steigender Latitude.
     * Die inneren Listen enthalten Hoehen nach steigender Longitude.
     * @throws IllegalStateException nach Spielende.
     * @see Event#RadarImage
     */
    @Override
    public List<List<Integer>> takeRadarImage() { //not ready
        if(hasItEnded())
            throw new java.lang.IllegalStateException("Game is Over(takeRadar)");
        //notify
        arena.setTicks(arena.getTicks() + arena.getRadarEdgeLength());
        arena.notifyObservers(Event.RadarImage);
        //radarImage
        final int radarEdgeSize = arena.getRadarEdgeLength();
        final Position playersPositon = getPosition();
        int radarLong = playersPositon.getLongitude() - ((radarEdgeSize-1)/2);
        int radarLat = playersPositon.getLatitude() - ((radarEdgeSize-1)/2);
        final List<List<Integer>> radarImage = new ArrayList<>();
        for(int outerListcounter = 0; outerListcounter < radarEdgeSize;outerListcounter++){
            final List<Integer> row = new ArrayList<>();
            for(int innerListcounter = 0; innerListcounter < radarEdgeSize;innerListcounter++){
                if(radarLat < 0 || radarLat >= getArenaSize() || radarLong < 0 || radarLong >= getArenaSize())
                    row.add(Integer.MAX_VALUE); 
                else
                    row.add(arena.getLandscape().getAltitude(radarLat, radarLong));
                
                radarLong++;
            }
            radarLat++;
            radarLong = playersPositon.getLongitude() - ((radarEdgeSize-1)/2);
            radarImage.add(row);
        }
        return radarImage;
    }

    //Private-------------------
    
    /** Wird aufgerufen um zu fragen ob das Spiel noch laeuft.
     * @return boolean if game is over true if over
     */
    private boolean hasItEnded(){
        boolean gameIsOver = false;
        if(State.Running != getState()){
            gameIsOver = true;
        }            
        if(arena.getMillisRemaining()<=0){
            arena.setState(State.Timedout);
            arena.notifyObservers(Event.TimedOut);
            gameIsOver = true;
        }
        return gameIsOver;
    }
    
    /**
     * Errechnet die anzahl an Zeit die fuer eine Bewegung benoetigt werden.
     * @param oldPosition Position, alte Position des Spielers.
     * @param newPosition Position, neue Position des Spielers.
     * @return Ticks, anzahl der Ticks die fuer eine Bewegung benoetigt werden.
     */
    private int timeCost(Position oldPosition, Position newPosition){
        //Altitude difference
        final int newAltitude = newPosition.getAltitude();
        final int oldAltitude = oldPosition.getAltitude();
        final int altitudeDifference = newAltitude - oldAltitude;
        
        final int cost;// up or down or straigth
        if(oldAltitude > newAltitude)
            cost = Math.abs(altitudeDifference) + 2;
        else if(oldAltitude < newAltitude)
            cost = altitudeDifference*altitudeDifference + 2;
        else 
            cost = 2;
        return cost;
    }
   
        /**
     * Checkt die spieler Position.
     * @param position Die neue Position des Spielers
     * @return State Running wenn der Spieler auf einer gueltigen Position steht.
     * Completed wenn der Spieler das Spiel erfolgreich Abgeschlossen hat.
     * Lost wenn der Spieler auf eine Ungueltige Position laeuft.
     */
    private State positionCheck(Position position){
        State gameState = gameState(position);
        if(gameState == State.Running){
            gameState = haveILost(position);
        }
        return gameState;
    }
    
    /**
     * Ueberprueft ob das Spiel erfolgreich abgeschlossen wurde.
     * @param position Position, Ist die neue Position des Spielers.
     * @return State, Running wenn das Spiel noch nicht erfolgreich beendet wurde,
     * Comleted wenn das Spiel erfolgreich beendet wurde.
     */
    private State gameState(Position position){
        final State gameState;
        if(getMushroomsRemaining().isEmpty() && getDestination().equals(position)){// voraussetzungen erfuellt
            gameState = State.Completed;
            arena.notifyObservers(Event.Completed);
        }else
            gameState =  State.Running;
        return gameState;    
    }
    
    
    /**
     * Ueberprueft ob das Spiel verloren wurde.
     * Checkt ob der Spieler in einen ungueltigen bereich des Spiel-
     * feldes getreten ist.
     * @param position Position, die neue Position des Spielers.
     * @return State, Lost wenn der Spieler in einen ungueltigen bereich des Spiel-
     * feldes gelaufen ist, Running wenn die neue Position des Spielers ok ist.
     */
    private State haveILost(Position position){
        final State gameState;
        if(position.getLatitude() >= getArenaSize() || position.getLongitude() >= getArenaSize()){
            arena.notifyObservers(Event.Lost);
            gameState = State.Lost;
        }else if(position.getLatitude() < 0 || position.getLongitude() < 0){
            arena.notifyObservers(Event.Lost);
            gameState = State.Lost;
        }else{
            gameState = State.Running;
        }
        return gameState;
    }
}
