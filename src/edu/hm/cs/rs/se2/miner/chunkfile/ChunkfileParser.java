/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.8.0_121, Linux i386 4.8.15
 * bluna (Intel Core i7-5600U CPU/2601 MHz, 4 Cores, 15872 MB RAM)
 */
package edu.hm.cs.rs.se2.miner.chunkfile;

import static edu.hm.cs.rs.se2.miner.chunkfile.MinecraftPiWorld.WORLD_DEPTH;
import static edu.hm.cs.rs.se2.miner.chunkfile.MinecraftPiWorld.WORLD_HEIGHT;
import static edu.hm.cs.rs.se2.miner.chunkfile.MinecraftPiWorld.WORLD_WIDTH;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Liest ein Chunkfile und fuellt damit eine Welt.
 * Die Info in http://minecraft.gamepedia.com/Pocket_Edition_level_format#chunks.dat
 * ist ziemlich falsch.
 * 
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-03-30
 */
// CHECKSTYLE:OFF MagicNumber
public class ChunkfileParser {
	/** Kennbytes am Anfang jedes Chunks. */
	public static final byte[] CHUNK_MAGIC = { 4, 65, 1, 0 };

	/** Kantenlaenge einer quadratischen Saeule. */
	public static final int COLUMN_BASESIZE = 16;

	/** Anzahl Sektoren eines Chunks. */
	public static final int SECTORS_PER_CHUNK = 21;

	/** Anzahl Byte eines Sektors. */
	public static final int SECTOR_BYTES = 1 << 12;

	/** Sector-# der letzten Traceausgabe. */
	private int lastTracedSector;

	/** Inhalt des Chunkfiles. */
	private byte[] rawImage;

	/**
	 * Flag: parsen protokollieren?
	 * false per Default.
	 * true, wenn die Umgebungsvariable TRACING einen beliebigen Wert hat.
	 */
	private final boolean tracing = System.getenv("TRACING") != null;

	/** Diese Welt landet im Chunkfile. */
	private final MinecraftPiWorld world;

	/**
	 * Ein Parser, der Chunkfiles in eine Welt laedt.
	 * 
	 * @param world Welt, die den Inhalt eines Chunkfiles aufnimmt.
	 */
	public ChunkfileParser(MinecraftPiWorld world) {
		this.world = world;
	}

	/**
	 * Fuellt die Welt mit einem Chunkfile.
	 * 
	 * @param chunkFilename Name eines Chunkfiles.
	 * @return Die Welt mit dem Inhalt des Chunkfiles.
	 * @throws IOException wenn beim Lesen des Chunkfiles etwas schief geht.
	 */
	public MinecraftPiWorld parse(String chunkFilename) throws IOException {
		lastTracedSector = -1;
		rawImage = Files.readAllBytes(Paths.get(chunkFilename));
		// Erst das Directory aus dem ersten Sektor lesen ...
		final List<Integer> directory = parseDirectory();
		// ... dann zu jedem Directoreintrag einen Chunk holen
		int chunkIndex = 0;
		for(int startOffset: directory) {
			parseChunk(startOffset,
					(chunkIndex % COLUMN_BASESIZE) * COLUMN_BASESIZE,
					chunkIndex / COLUMN_BASESIZE * COLUMN_BASESIZE);
			chunkIndex++;
		}
		return world;
	}

	/**
	 * Stellt die Chunk-Signatur sicher.
	 * 
	 * @param startOffset Byte-Index im File, an dem der Chunk beginnt.
	 * @throws IOException wenn die Signatur fehlt.
	 */
	private void assertChunkMagic(int startOffset) throws IOException {
		int offset = startOffset;
		for(int magic: CHUNK_MAGIC)
			if(imageAt(offset) == magic)
				offset++;
			else
				throw new IOException("Invalid chunk magic @" + offset);
		trace("chunk magic", startOffset, Arrays.toString(CHUNK_MAGIC));
	}

	/**
	 * Liefert den Bytewert im Chunkfile als int im Bereich 0-255.
	 * 
	 * @param index Index eines Bytes im Chunkfile.
	 * @return Int-Wert des Bytes. Nicht negativ.
	 */
	private int imageAt(int index) {
		return rawImage[index] & 0xFF;
	}

	/**
	 * Liest einen Chunk.
	 * 
	 * @param startOffset Byte-Index im File, an dem der Chunk beginnt.
	 * @param xBase x-Kooridante der Saeule in der Welt.
	 * @param zBase z-Koordinate der Saeule in der Welt.
	 * @throws IOException wenn beim Lesen des Files etwas schief geht.
	 */
	private void parseChunk(int startOffset, int xBase, int zBase) throws IOException {
		assertChunkMagic(startOffset);
		int offset = startOffset + 4; // skip 4 byte = magic
		final byte[] buffer = new byte[128];
		for(int x = 0; x < COLUMN_BASESIZE; x++)
			for(int z = 0; z < COLUMN_BASESIZE; z++) {
				final int blockOffset = offset;
				for(int height = 0; height < WORLD_HEIGHT; height++) {
					final int type = imageAt(offset++);
					world.setBlock(xBase + x, zBase + z, height, type);
				}
				trace(String.format("block column (x=%d, z=%d)", xBase + x, zBase + z),
						blockOffset,
						Arrays.toString(world.copyColumn(buffer, xBase + x, zBase + z)));
			}
	}

	/**
	 * Liest das Directory eines Chunkfiles.
	 * 
	 * @return Liste der Sector-Indexe der Chunks in diesem File.
	 * @throws IOException wenn beim Lesen des Files etwas schief geht.
	 */
	@SuppressWarnings("PMD.PrematureDeclaration") private List<Integer> parseDirectory() throws IOException {
		final List<Integer> directory = new ArrayList<>();
		int offset = 0;
		if(tracing)
			System.out.println("directory");
		while(offset < SECTOR_BYTES) {
			final int numSectors = trace("# sectors", offset, imageAt(offset++));
			// Sector-Index: 2-Byte, little-endian
			final int startSector = trace("start sector index", offset, imageAt(offset++) + (imageAt(offset++) << 8));
			final int unused = trace("unused", offset, imageAt(offset++));
			if(unused != 0)
				throw new IOException("Invalid directory entry unused byte value (expected 0): " + unused);
			if(numSectors == SECTORS_PER_CHUNK)
				directory.add(SECTOR_BYTES * startSector);
			else if(numSectors != 0)
				throw new IOException("Invalid directory entry sector count (expected 0 or 21): " + numSectors);
		}
		assert directory.size() == (WORLD_DEPTH * WORLD_WIDTH) / COLUMN_BASESIZE / COLUMN_BASESIZE;
		return directory;
	}

	/**
	 * Schreibt eine Nachricht auf System.out und liefert den gegebenen Wert wieder zurueck.
	 * Keine Ausgabe, wenn das Traceflag false ist.
	 * @param message Nachricht.
	 * @param address Adresse, auf die sich der Text bezieht.
	 * @param returned Wert zur Nachricht.
	 * @return Wert.
	 */
	private int trace(String message, int address, int returned) {
		trace(message, address, Integer.toString(returned));
		return returned;
	}

	/**
	 * Schreibt eine Nachricht auf System.out und liefert den gegebenen Wert wieder zurueck.
	 * Gibt vorher noch den Sektor-Index aus, wenn die Adresse in einen neuen Sektor faellt.
	 * Keine Ausgabe, wenn das Traceflag false ist.
	 * 
	 * @param message Nachricht.
	 * @param address Adresse, auf die sich der Text bezieht.
	 * @param returned Text zur Nachricht.
	 */
	private void trace(String message, int address, String returned) {
		if( !tracing)
			return;
		final int sector = address / SECTOR_BYTES;
		if(sector > lastTracedSector) {
			System.out.printf("[@%7x] sector #%d%n", address, sector);
			lastTracedSector = sector;
		}
		System.out.printf("[%7x] %s = %s%n", address, message, returned);
	}

	/**
	 * Demo-Hauptprogramm: Parst ein Chunksfile und protokolliert den Parsers.
	 * Wirft die gelesene Welt dann weg.
	 * 
	 * @param args Filename eines Chunksfiles.
	 * @throws IOException wenn beim Lesen des Chunksfiles ein Fehler auftritt.
	 */
	public static void main(String... args) throws IOException {
		new ChunkfileParser(new MinecraftPiWorld()).parse(args[0]);
	}

}
