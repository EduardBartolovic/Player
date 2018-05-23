/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.17
 * emma (Intel Core i7-4790 CPU/3.60GHz, 8 cores, 3601 MHz, 32128 MByte RAM)
 */
package edu.hm.cs.rs.se2.miner.chunkfile.util;

import edu.hm.cs.rs.se2.miner.arena.Landscape;
import edu.hm.cs.rs.se2.miner.chunkfile.ChunkfileWriter;
import edu.hm.cs.rs.se2.miner.chunkfile.MinecraftPiWorld;
import static edu.hm.cs.rs.se2.miner.chunkfile.MinecraftPiWorld.Block.Mushroom;
import static edu.hm.cs.rs.se2.miner.chunkfile.MinecraftPiWorld.Block.Stone;
import edu.hm.cs.rs.se2.miner.common.Factory;
import edu.hm.cs.rs.se2.miner.common.Position;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Generates a chunks file from a landscape.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-11
 */
public class LandscapeChunkfile {
    /**
     * Chunks file sector size = 4 kByte.
     */
    private static final int SECTOR_BYTES = 4096;

    /**
     * Chunks file length.
     */
    private static final int CHUNKFILE_LENGTH = SECTOR_BYTES*(21*256 + 1);

    /**
     * Entry point.
     * @param args Commandline args: Landscape specification, optional chunks file name
     * (default = chunks.dat).
     * @exception ReflectiveOperationException when loading the landscape fails.
     * @exception IOException when writing the chunks file fails.
     */
    public static void main(String... args) throws ReflectiveOperationException, IOException {
        int arg = 0;
        final Landscape landscape = Factory.<Landscape>make(args[arg++]);
        checkSize(landscape);
        final MinecraftPiWorld world = fillWorldWithBlocks(landscape);
        placeMushrooms(landscape, world);
        writeChunksfile(arg < args.length? args[arg]: "chunks.dat", world);
    }

    /** Schreibt das Chunksfile.
     * @param chunkfilename Filename.
     * @param world MCPI-Welt.
     * @throws IOException wenn beim Schreiben des Files etwas schief geht.
     */
    private static void writeChunksfile(String chunkfilename, MinecraftPiWorld world) throws IOException {
        System.out.println("writing chunksfile: " + chunkfilename);
        new ChunkfileWriter(world).save(chunkfilename);
        final long fileLength = Files.size(Paths.get(chunkfilename));

        if(fileLength != CHUNKFILE_LENGTH)
            throw new IOException("bad file size: have " + fileLength + ", wanted " + CHUNKFILE_LENGTH);
        System.out.println("validating file size: ok");
    }

    /** Verteilt die Pilze in der MCPI-Welt.
     * @param landscape Landschaft.
     * @param world MCPI-Welt, in dei die Methode Pilze einbaut.
     */
    private static void placeMushrooms(Landscape landscape, MinecraftPiWorld world) {
        System.out.println("placing mushrooms");
        final int mushroomId = Mushroom.getId();
        for(Position mushroom : landscape.getMushrooms()) {
            final int x = mushroom.getLongitude();
            final int z = mushroom.getLatitude();
            System.out.printf("\tmushroom @(x=%d, y=%d, z=%d)%n", x, landscape.getAltitude(x, z), z);
            setBlock(world, x, z, landscape.getAltitude(x, z), mushroomId);
        }
    }

    /** Fuellt die MCPI-Welt mit Steinen entsprechend den Hoehen der Landschaft.
     * @param landscape Landschaft.
     * @return Eine neue MCPI-Welt.
     * @throws IOException wenn in der Landschaft eine Hoehe von 0 vorkommt.
     */
    private static MinecraftPiWorld fillWorldWithBlocks(Landscape landscape) throws IOException {
        System.out.println("building Minecraft world");
        final int stoneId = Stone.getId();
        final MinecraftPiWorld world = new MinecraftPiWorld();
        for(int x = 0; x < MinecraftPiWorld.WORLD_WIDTH; x++)
            for(int z = 0; z < MinecraftPiWorld.WORLD_DEPTH; z++) {
                final int height = landscape.getAltitude(z, x);
                if(height == 0)
                    throw new IOException("chunk file height 0 at (x=" + x + ", z=" + z + ")");
                for(int y = 0; y < height; y++)
                    setBlock(world, x, z, y, stoneId);
            }
        return world;
    }

    /** Stellt sicher, dass die Landschaft die richtige Groesse hat.
     * @param landscape Die Landschaft.
     * @throws IllegalArgumentException wenn die Groesse nicht passt.
     */
    private static void checkSize(Landscape landscape) {// check size
        if(landscape.getSize() != MinecraftPiWorld.WORLD_WIDTH)
            throw new IllegalArgumentException("Landscape has invalid width: " + landscape.getSize() + " (" + MinecraftPiWorld.WORLD_WIDTH + " required)");
        if(landscape.getSize() != MinecraftPiWorld.WORLD_DEPTH)
            throw new IllegalArgumentException("Landscape has invalid depth: " + landscape.getSize() + " (" + MinecraftPiWorld.WORLD_DEPTH + " required)");
    }

    /**
     * Platziert einen Block in der MCPI-Welt.
     * Rechnet den Koordinatenursprung um (MCPI-Welt links oben, Miner links unten).
     * @param world MCPI-Welt.
     * @param x Horizontale Koordinate (bleibt).
     * @param z Vertikale Koordinate (spiegeln).
     * @param y Hoehe.
     * @param type Block-Id.
     */
    private static void setBlock(MinecraftPiWorld world, int x, int z, int y, int type) {
        world.setBlock(x, MinecraftPiWorld.WORLD_DEPTH - z - 1, y, type);
    }

}
