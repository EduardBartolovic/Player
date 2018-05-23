/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.17
 * emma (Intel Core i7-4790 CPU/3.60GHz, 8 cores, 2000 MHz, 32128 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.ruler;

import java.util.List;
import java.util.Set;
import edu.hm.cs.rs.se2.miner.arena.full.FullArena;
import edu.hm.cs.rs.se2.miner.common.Direction;
import edu.hm.cs.rs.se2.miner.common.Position;
import edu.hm.cs.rs.se2.miner.common.State;

/**
 * Funktionslose Regel-Implementierung.
 * 
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-03-31
 */
class EmptyRuler implements Ruler {
	/**
	 * Neuer Ruler.
	 * 
	 * @param arena Arena. Nicht null.
	 */
	EmptyRuler(FullArena arena) {
		// dummy implementation
	}

	@Override public int getArenaSize() {
		return 0;
	}

	@Override public Position getDestination() {
		return null;
	}

	@Override public int getMillisRemaining() {
		return 0;
	}

	@Override public Set<Position> getMushroomsRemaining() {
		return null;
	}

	@Override public Position getPosition() {
		return null;
	}

	@Override public State getState() {
		return null;
	}

	@Override public int getTicks() {
		return 0;
	}

	@Override public State move(Direction direction) {
		return null;
	}

	@Override public Ruler surrender() {
		return null;
	}

	@Override public List<List<Integer>> takeRadarImage() {
		return null;
	}

}
