/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.8.0_121, Linux i386 4.8.15
 * tura (Intel Atom CPU N270/1600 MHz, 2 Cores, 2048 MB RAM)
 */
package edu.hm.cs.rs.se2.miner.chunkfile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Schreibt den Inhalt der Welt auf ein Chunkfile.
 * @author R. Schiedermeier, rs@cs.hm.edu.
 * @version 2017-03-29
 */
// CHECKSTYLE:OFF MagicNumber
public class ChunkfileWriter {
    /** Anzahl abwechselnd gueltiger und leerer Entries im Directory. */
    private static final int DIR_ENTRIES_RUN = 16;

    /** Anzahl Byte eines Directory-Entry. */
    private static final int DIR_ENTRY_LENGTH = 4;

    /** Anzahl Chunks im File. */
    private static final int NUM_CHUNKS = 256;

    /** Diese Welt landet im Chunkfile. */
    private final MinecraftPiWorld world;

    /** Ctor mit Quelle und Ziel.
     * @param world Diese Welt landet im Chunkfile.
     */
    public ChunkfileWriter(MinecraftPiWorld world) {
        this.world = world;
    }

    /** Schreibt den Inhalt der Welt auf ein Chunkfile.
     * @param chunkFilename Filename, Nicht null.
     * @throws IOException wenn beim Schreiben etwas schief geht.
     */
    public void save(String chunkFilename) throws IOException {
        final Path chunkfile = Paths.get(chunkFilename);
        try(OutputStream outputStream = Files.newOutputStream(chunkfile)) {
            writeDirectory(outputStream);
            writeChunks(outputStream);
        }
    }

    /** Schreibt die Block-Ids auf ein Chunkfile.
     * @param outputStream Chunkfile, offen zum Schreiben.
     * @param columnBuffer Tempraeres Array fuer Block-Ids.
     * @param xWorld x-Koordinate der linken unteren Ecke des Chunks in der Welt.
     * @param zWorld z-Koordinate der linken unteren Ecke des Chunks in der Welt.
     * @throws IOException wenn das Schreiben schief geht.
     */
    private void writeBlockIds(OutputStream outputStream, final byte[] columnBuffer, int xWorld, int zWorld) throws IOException {
        // Blocktypen: 0x0004 + 32k
        for(int x = 0; x < ChunkfileParser.COLUMN_BASESIZE; x++)
            for(int z = 0; z < ChunkfileParser.COLUMN_BASESIZE; z++)
                // Das klappt zwar, ist aber langsam
                //					for(int height = 0; height < MinecraftPiWorld.WORLD_HEIGHT; height++) {
                //						int type = world.getBlock(xWorld + x, zWorld + z, height);
                //						outputStream.write(type);
                //					}
                // Das ist weit schneller
                outputStream.write(world.copyColumn(columnBuffer, xWorld + x, zWorld + z));
    }

    /** Schreibt die Block-Ifos auf ein Chunkfile.
     * Diese Methode schreibt nur 0-Bytes, das heisst keine Infos.
     * @param outputStream Chunkfile, offen zum Schreiben.
     * @param emptySector Leeres Array.
     * @throws IOException wenn das Schreiben schief geht.
     */
    private void writeBlockInfo(OutputStream outputStream, final byte[] emptySector) throws IOException {
        // Block-Info: 0x8004 + 16k: 0-Bytes
        for(int sectors = 0; sectors < 4; sectors++)
            outputStream.write(emptySector);
    }

    /** Schreibt die Block-Helligkeiten auf ein Chunkfile.
     * Jedes Byte enthaelt die Helligkeiten von zwei Bloecken.
     * @param outputStream Chunkfile, offen zum Schreiben.
     * @param xWorld x-Koordinate der linken unteren Ecke des Chunks in der Welt.
     * @param zWorld z-Koordinate der linken unteren Ecke des Chunks in der Welt.
     * @throws IOException wenn das Schreiben schief geht.
     */
    private void writeBlocksBrightness(OutputStream outputStream, int xWorld, int zWorld) throws IOException {
        // Daylight: 0xC004 + 16k: Nibble F = min, 0 = max Helligkeit
        // CHECKSTYLE:OFF NestedForDepth
        for(int x = 0; x < ChunkfileParser.COLUMN_BASESIZE; x++)
            for(int z = 0; z < ChunkfileParser.COLUMN_BASESIZE; z++)
                for(int height = 0; height < MinecraftPiWorld.WORLD_HEIGHT; height += 2) {
                    final int lowerBlock = world.getBlock(xWorld + x, zWorld + z, height);
                    final int upperBlock = world.getBlock(xWorld + x, zWorld + z, height + 1);
                    final int packedLightNibbles = (lowerBlock == 0 ? 0xF : 0) | (upperBlock == 0 ? 0xF0 : 0);
                    outputStream.write(packedLightNibbles);
                }
    }

    /** Schreibt die Chunks.
     * @param outputStream Nimmt die Chunks auf.
     * @throws IOException wenn beim Schreiben etwas schief geht.
     */
    private void writeChunks(OutputStream outputStream) throws IOException {
        int xWorld = 0;
        int zWorld = 0;
        final byte[] emptySector = new byte[ChunkfileParser.SECTOR_BYTES];
        final byte[] columnBuffer = new byte[MinecraftPiWorld.WORLD_HEIGHT];
        for(int chunks = 0; chunks < NUM_CHUNKS; chunks++) {
            outputStream.write(ChunkfileParser.CHUNK_MAGIC);
            writeBlockIds(outputStream, columnBuffer, xWorld, zWorld);
            writeBlockInfo(outputStream, emptySector);
            writeBlocksBrightness(outputStream, xWorld, zWorld);
            writeBlockInfo(outputStream, emptySector);
            writeSectorFiller(outputStream, emptySector);

            xWorld += ChunkfileParser.COLUMN_BASESIZE;
            if(xWorld >= MinecraftPiWorld.WORLD_WIDTH) {
                xWorld = 0;
                zWorld += ChunkfileParser.COLUMN_BASESIZE;
            }
        }
    }

    /** Schreibt das Directory.
     * @param outputStream Nimmt das Directory auf.
     * @throws IOException wenn beim Schreiben etwas schief geht.
     */
    private void writeDirectory(OutputStream outputStream) throws IOException {
        final byte[] zeroes = new byte[DIR_ENTRIES_RUN * DIR_ENTRY_LENGTH];

        int sectorIndex = 1;
        for(int dirEntries = 0; dirEntries < NUM_CHUNKS; dirEntries += DIR_ENTRIES_RUN) {
            // Gueltige Directory-Entries
            for(int entry = 0; entry < DIR_ENTRIES_RUN; entry++) {
                outputStream.write(ChunkfileParser.SECTORS_PER_CHUNK);
                outputStream.write(sectorIndex & 0xFF);
                outputStream.write(sectorIndex >> 8);
                outputStream.write(0);
                sectorIndex += ChunkfileParser.SECTORS_PER_CHUNK;
            }

            // Ebenso viele unbenutzte Directory-Entries anfuegen
            outputStream.write(zeroes);
        }

        // Rest des Sectors mit 0-Bytes fuellen
        for(int dirEntries = 0; dirEntries < NUM_CHUNKS; dirEntries += DIR_ENTRIES_RUN) {
            outputStream.write(zeroes);
            outputStream.write(zeroes);
        }
    }

    /** Fuellt einen Sector auf.
     * @param outputStream Chunkfile, offen zum Schreiben.
     * @param emptySector Leeres Array.
     * @throws IOException wenn das Schreiben schief geht.
     */
    private void writeSectorFiller(OutputStream outputStream, final byte[] emptySector) throws IOException {
        // Bisher sind 4 Byte + 20 Sektoren geschrieben;
        // auffuellen auf 21 komplette Sektoren
        outputStream.write(emptySector, 0, ChunkfileParser.SECTOR_BYTES - ChunkfileParser.CHUNK_MAGIC.length);
    }

}
