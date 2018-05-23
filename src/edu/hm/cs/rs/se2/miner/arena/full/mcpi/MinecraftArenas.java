///* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
// * Java 1.8.0_121, Linux x86_64 4.8.15
// * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2300 MHz, 16000 MByte RAM)
// */
//package edu.hm.cs.rs.se2.miner.arena.full.mcpi;
//
//import edu.hm.cs.rs.se2.miner.arena.Landscape;
//import edu.hm.cs.rs.se2.miner.chunkfile.MinecraftPiWorld;
//import edu.hm.cs.rs.se2.miner.common.Position;
//import java.util.Random;
//import pi.Minecraft;
//import pi.Vec;
//
///** Companionklasse mit Methoden,
// * die eine Landschaft mit der Welt eines laufenden Minecraft-Pi vergleichen.
// * @author R. Schiedermeier, rs@cs.hm.edu
// * @version 2017-04-11
// */
//public class MinecraftArenas {
//    /** Altitude, die der Hoehe 0 der Welt von Minecraft-Pi entspricht. */
//    static final int PIWORLD_CENTER_Y = MinecraftPiWorld.WORLD_HEIGHT / 2;
//
//    /** Gitterbreite des Rasters, dessen Punkte die Methoden dieser Klasse ueberpruefen.
//     * Um die Ecken zu erfassen, sollte diese Zahl 255 teilen.
//     * Je kleiner die Zahl ist, desto laenger dauert der Test.
//     * Bei 1 testet die Klasse jedes Feld, bei 255 nur die vier Ecken.
//     */
//    private static final int CHECKED_RASTER_STEPSIZE = 51;
//
//    /** Anzahl Zufallspunkte, die die Methoden dieser Klasse ueberpruefen. */
//    private static final int NUM_CHECKED_RANDOM_POINTS = 32;
//
//    /** Breite und Laenge, die dem mittleren Block der Welt von Minecraft-Pi entspricht. */
//    private static final int PIWORLD_CENTER_XZ = MinecraftPiWorld.WORLD_WIDTH / 2;
//
//    /** Y-Koordinate der Minecraft-Pi-Welt zur Hoehe.
//     * @param altitude Hoehe.
//     * @return Y-Koordinate.
//     */
//    static int altitudeToY(int altitude) {
//        return altitude - PIWORLD_CENTER_Y;
//    }
//
//    /** Z-Koordinate der Minecraft-Pi-Welt zur Breitenkoordinate.
//     * @param latitude Breite.
//     * @return Z-Koordinate.
//     */
//    static int latitudeToZ(int latitude) {
//        return latitude - PIWORLD_CENTER_XZ;
//    }
//
//    /** X-Koordinate der Minecraft-Pi-Welt zur Laenge.
//     * @param longitude Laenge.
//     * @return X-Koordinate.
//     */
//    static int longitudeToX(int longitude) {
//        return longitude - PIWORLD_CENTER_XZ;
//    }
//
//    /** Stellt sicher, dass die Landschaft dieser Anwendung mit der Wwlt
//     * des laufenden Minecraft-Pi uebereinstimmt.
//     * Ein vollstaendiger Vergleich ist zu langsam.
//     * Testet einige ausgewaehlte Eigenschaften.
//     * @param landscape Landschaft.
//     * @param mcpiWorld Welt von Minecraft-Pi.
//     * @throws IllegalStateException wenn die Landschaft nicht mit der Welt von Minecraft uebereinstimmt.
//     */
//    static void validateSameLandscape(Landscape landscape, Minecraft mcpiWorld) {
//        isValidSize(landscape);
//        isAltitudesInRange(landscape);
////        isValidAtGridPoints(landscape, mcpiWorld);
////        isValidAtRandomPoints(landscape, mcpiWorld);
////        hasMushrooms(landscape, mcpiWorld);
//    }
//
//    /** Ganzzahliger Vector der Minecraft-Pi-Wlt zu einer Position.
//     * @param position Position.
//     * @return Vector der Minecraft-Pi-Welt.
//     */
//    static Vec vecOf(Position position) {
//        return Vec.xyz(longitudeToX(position.getLongitude()),
//                       altitudeToY(position.getAltitude()),
//                       latitudeToZ(position.getLatitude()));
//    }
//
//    /** Stellt sicher,
//     * dass die Pilze der Landschaft in der Welt von Minecraft-Pi existieren.
//     * @param landscape Landschaft.
//     * @param mcpiWorld Welt von Minecraft-Pi.
//     * @throws IllegalStateException wenn in Minecraft-Pi ein Pilz fehlt.
//     */
//    private static void hasMushrooms(Landscape landscape, Minecraft mcpiWorld) throws IllegalStateException {
//        for(Position position: landscape.getMushrooms()) {
//            final Vec mushroomVec = MinecraftArenas.vecOf(position.movedBy(0, 0, -1));
//            if(!mcpiWorld.getBlock(mushroomVec).equals(MinecraftPiWorld.Block.Mushroom.getId()))
//                throw new IllegalStateException("mushroom not found @" + position);
//        }
//    }
//
//    /** Stellt sicher,
//     * dass alle Hoehen einer Landschaft in dem Bereich liegen, den Minecraft-Pi erlaubt.
//     * @param landscape Landschaft.
//     * @throws IllegalStateException wenn es eine unzulaessige Hoehe gibt.
//     */
//    private static void isAltitudesInRange(Landscape landscape) throws IllegalStateException {
//        final int landscapeSize = landscape.getSize();
//        for(int latitude = 0; latitude < landscapeSize; latitude++)
//            for(int longitude = 0; longitude < landscapeSize; longitude++) {
//                final int height = landscape.getAltitude(latitude, longitude);
//                if(height < 1)
//                    throw new IllegalStateException("landscape height underrun: " + height);
//                if(MinecraftArenas.altitudeToY(height) > MinecraftPiWorld.WORLD_HEIGHT)
//                    throw new IllegalStateException("landscape height overrun: " + height);
//            }
//    }
//
//    /** Stellt sicher,
//     * dass die Hoehen der Felder in einem regelmaessigen Raster uebereinstimmen.
//     * @param landscape Landschaft.
//     * @param mcpiWorld Welt von Minecraft-Pi.
//     * @throws IllegalStateException wenn eine Hoehe abweicht.
//     */
//    private static void isValidAtGridPoints(Landscape landscape, Minecraft mcpiWorld) throws IllegalStateException {
//        final int landscapeSize = landscape.getSize();
//        for(int latitude = 0; latitude < landscapeSize; latitude += CHECKED_RASTER_STEPSIZE)
//            for(int longitude = 0; longitude < landscapeSize; longitude += CHECKED_RASTER_STEPSIZE)
//                MinecraftArenas.matchAltitude(latitude, longitude, landscape, mcpiWorld);
//    }
//
//    /** Stellt sicher,
//     * dass die Hoehen an einigen zufaelligen Stellen uebereinstimmen.
//     * @param landscape Landschaft.
//     * @param mcpiWorld Welt von Minecraft-Pi.
//     * @throws IllegalStateException wenn eine Hoehe abweicht.
//     */
//    private static void isValidAtRandomPoints(Landscape landscape, Minecraft mcpiWorld) throws IllegalStateException {
//        final int landscapeSize = landscape.getSize();
//        final Random random = new Random();
//        for(int randomPoints = 0; randomPoints < NUM_CHECKED_RANDOM_POINTS; randomPoints++)
//            MinecraftArenas.matchAltitude(random.nextInt(landscapeSize),
//                                          random.nextInt(landscapeSize),
//                                          landscape,
//                                          mcpiWorld
//            );
//    }
//
//    /** Stellt sicher,
//     * dass die Landschaft genauso gross ist, wie die Welt von Minecraft-Pi.
//     * @param landscape Landschaft.
//     * @throws IllegalStateException wenn die Groesse abweicht.
//     */
//    private static void isValidSize(Landscape landscape) throws IllegalStateException {
//        final int landscapeSize = landscape.getSize();
//        // Laenge und Breite vergleichen
//        if(landscapeSize != MinecraftPiWorld.WORLD_WIDTH)
//            throw new IllegalStateException("landscape invalid width: " + landscapeSize);
//        if(landscapeSize != MinecraftPiWorld.WORLD_DEPTH)
//            throw new IllegalStateException("landscape invalid depth: " + landscapeSize);
//    }
//
//    /** Stellt sicher,
//     * dass die Hoehe an einer Position mit der Hoehe der Welt von Minecraft-Pi uebereinstimmt.
//     * @param latitude Breite.
//     * @param longitude Laenge.
//     * @param landscape Landschaft.
//     * @param mcpiWorld Welt von Minecraft-Pi.
//     * @throws IllegalStateException wenn die Hoehen abweichen.
//     */
//    private static void matchAltitude(int latitude, int longitude, Landscape landscape, Minecraft mcpiWorld) throws IllegalStateException {
//        final int altitude = landscape.getAltitude(latitude, longitude);
//        final int height = mcpiWorld.getHeight(MinecraftArenas.longitudeToX(longitude), MinecraftArenas.latitudeToZ(latitude));
//        if(MinecraftArenas.altitudeToY(altitude) != height)
//            throw new IllegalStateException(String.format("altitude mismatch @(%d/%d): %d, MCPI has %d", latitude, longitude, altitude, height + MinecraftArenas.PIWORLD_CENTER_Y));
//    }
//
//}
