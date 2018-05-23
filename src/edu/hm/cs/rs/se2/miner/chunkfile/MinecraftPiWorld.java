/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.8.0_121, Linux i386 4.8.4
 * sol (Intel Xeon CPU E5-2660 v2/2201 MHz, 40 Cores, 64256 MB RAM)
 */
package edu.hm.cs.rs.se2.miner.chunkfile;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Eine Welt von Minecraft-Pi.
 * 
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-03-30
 */
public class MinecraftPiWorld {
	/** Breite einer Welt als Anzahl Bloecke. Ausdehnung in x-Richtung. */
	public static final int WORLD_WIDTH = 256;

	/** Tiefe einer Welt als Anzahl Bloecke. Ausdehnung in z-Richtung. */
	public static final int WORLD_DEPTH = 256;

	/** Hoehe einer Welt als Anzahl Bloecke. Ausdehnung in y-Richtung. */
	public static final int WORLD_HEIGHT = 128;

	/** Bitmaske fuer ein Byte. */
	private static final int BYTE_BITMASK = 0xFF;

	/** Bitmaske fuer die hohe Nibble eines Bytes. */
	private static final int HIGHNIBBLE_BITMASK = 0xF0;

	/** Bitmaske fuer die niedere Nibble eines Bytes. */
	private static final int LOWNIBBLE_BITMASK = 0xF;

	/** Anzahl Bits in einem Nibble (Halbbyte). */
	private static final int NIBBLE_BITS = 4;

	/**
	 * Ids aller Bloecke.
	 * Am Haeufigsten sind: 0 = Luft, 1 = Stein, 7 = Fels.
	 * 
	 * @see pi.Block
	 */
	private byte[][][] blocksXZY = new byte[WORLD_WIDTH][WORLD_DEPTH][WORLD_HEIGHT];

	/**
	 * Blockdaten.
	 * Die beiden Nibbles jedes Bytes enthalten die Daten von zwei uebereinander liegenden Bloecken.
	 */
	private byte[][][] dataXZY = new byte[WORLD_WIDTH][WORLD_DEPTH][WORLD_HEIGHT / 2];

	/**
	 * Ausdehnung in Richtung x-Koordinaten (von links nach rechts).
	 * 
	 * @return Anzahl Bloecke.
	 */
	public int getWidth() {
		return blocksXZY.length;
	}

	/**
	 * Ausdehnung in Richtung z-Koordinaten (von vorne nach hinten).
	 * 
	 * @return Anzahl Bloecke.
	 */
	public int getDepth() {
		return blocksXZY[0].length;
	}

	/**
	 * Ausdehnung in Richtung y-Koordinaten (von unten nach oben).
	 * 
	 * @return Anzahl Bloecke.
	 */
	public int getHeight() {
		return blocksXZY[0][0].length;
	}

	/**
	 * Liefert den Blocktyp an einer Position.
	 * 
	 * @param x Koordinate quer.
	 * @param z Koordinate nach hinten.
	 * @param y Koordinate nach oben.
	 * @return Blocktyp.
	 * @see pi.Block
	 */
	public int getBlock(int x, int z, int y) {
		return blocksXZY[x][z][y] & BYTE_BITMASK;
	}

	/**
	 * Liefert die Blockdaten an einer Position.
	 * 
	 * @param x Koordinate quer.
	 * @param z Koordinate nach hinten.
	 * @param y Koordinate nach oben.
	 * @return Blockdaten.
	 * @see pi.Block
	 */
	public int getData(int x, int z, int y) {
		final int shift = y % 2 == 0? 0: NIBBLE_BITS;
		return (dataXZY[x][z][y / 2] >> shift) & LOWNIBBLE_BITMASK;
	}

	/**
	 * Kopiert eine Blocksaeule in ein Array.
	 * 
	 * @param destination Array, das die Daten aufnimmt.
	 *        Die Laenge dieses Arrays entscheidet darueber, was die Methode kopiert:
	 *        Laenge = WORLD_HEIGHT: Block-Ids; andernfalls die Blockdaten.
	 * @param x Koordinate quer.
	 * @param z Koordinate nach hinten.
	 * @return Erstes Argument.
	 */
	public byte[] copyColumn(byte[] destination, int x, int z) {
		final int numBytes = destination.length;
		System.arraycopy(numBytes == WORLD_HEIGHT? blocksXZY[x][z]: dataXZY[x][z], 0,
				destination, 0,
				numBytes);
		return destination;
	}

	/**
	 * Legt den Blocktyp an einer Position neu fest.
	 * 
	 * @param x Koordinate quer.
	 * @param z Koordinate nach hinten.
	 * @param y Koordinate nach oben.
	 * @param type Blocktyp.
	 * @see pi.Block
	 */
	public void setBlock(int x, int z, int y, int type) {
		blocksXZY[x][z][y] = (byte)(type & BYTE_BITMASK);
	}

	/**
	 * Legt die Blockdaten an einer Position neu fest.
	 * 
	 * @param x Koordinate quer.
	 * @param z Koordinate nach hinten.
	 * @param y Koordinate nach oben.
	 * @param data Blockdaten.
	 * @see pi.Block
	 */
	public void setData(int x, int z, int y, int data) {
		final int combinedData;
		if(y % 2 == 0)
			combinedData = dataXZY[x][z][y / 2] & HIGHNIBBLE_BITMASK | (data & LOWNIBBLE_BITMASK);
		else
			combinedData = dataXZY[x][z][y / 2] & LOWNIBBLE_BITMASK | (data & LOWNIBBLE_BITMASK) << NIBBLE_BITS;
		dataXZY[x][z][y / 2] = (byte)combinedData;
	}

	/**
	 * Liest ein Chunkfile und initialisiert damit ein neues Objekt.
	 * 
	 * @param filename Name eines Files, meistens chunks.dat.
	 *        Auf dem Raspberry Pi liegen diese Files in den Subdirs
	 *        ~pi/.minecraft/games/com.mojang/minecraftWorlds/.../chunks.dat
	 * @return Neues ChunkFile-Objekt.
	 * @throws IOException wenn bei Lesen des Files ein Fehler passiert.
	 */
	public static MinecraftPiWorld load(String filename) throws IOException {
		return new ChunkfileParser(new MinecraftPiWorld()).parse(filename);
	}

	/**
	 * Schreibt ein neues Chunkfile.
	 * 
	 * @param filename Name des neuen Files.
	 * @throws IOException wenn beim Schreiben etwas schief geht.
	 */
	public void save(String filename) throws IOException {
		new ChunkfileWriter(this).save(filename);
	}

	/**
	 * Blocktypen, die in dieser Minecraft-World erlaubt sind.
	 */
	public enum Block {
		// CHECKSTYLE:OFF JavadocVariable
		Air(0, false), Stone(1, true), Mushroom(40, false);
		// CHECKSTYLE:ON

		/** Gecachtes Array mit Blocktypen. */
		private static final Map<Integer, Block> IDS_TO_BLOCK;

		static {
			IDS_TO_BLOCK = Stream.of(values())
					.collect(Collectors.toMap(Block::getId,
							Function.identity()));
		}

		/** Die Minecraft-Id. */
		private int minecraftId;

		/** Ist das ein Blocktyp, auf dem Steve stehen kann? */
		private boolean surface;

		/**
		 * Neuer Blocktyp.
		 * 
		 * @param minecraftId Minecraft-Id.
		 * @param surface Kann Steve darauf stehen?
		 */
		Block(int minecraftId, boolean surface) {
			this.minecraftId = minecraftId;
			this.surface = surface;
		}

		/**
		 * Minecraft-Id dieses Typs.
		 * 
		 * @return Id.
		 */
		public int getId() {
			return minecraftId;
		}

		public boolean isSurface() {
			return surface;
		}

		/**
		 * Liefert den Blocktyp zu einer Id.
		 * 
		 * @param minecraftId Minecraft-Id.
		 * @return Blocktyp.
		 * @throws IllegalArgumentException wenn diese Id nicht vorkommt.
		 */
		@SuppressWarnings("PMD.ShortMethodName")
		public static Block of(int minecraftId) {
			final Block result = IDS_TO_BLOCK.get(minecraftId);
			if(result == null)
				throw new IllegalArgumentException("invalid block id: " + minecraftId);
			return result;
		}

	}

}
