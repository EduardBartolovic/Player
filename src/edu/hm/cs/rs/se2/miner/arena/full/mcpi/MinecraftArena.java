///* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
// * Oracle Corporation Java 1.8.0_72, Linux i386 4.3.0
// * mozart (Intel Core i7-4600U CPU/2701 MHz, 4 Cores, 11776 MB RAM)
// */
//package edu.hm.cs.rs.se2.miner.arena.full.mcpi;
//
//import edu.hm.cs.rs.se2.miner.arena.Landscape;
//import edu.hm.cs.rs.se2.miner.arena.full.CoreArena;
//import pi.Minecraft;
//
///** Eine Arena zur Welt von Minecraft-Pi.
// * @author R. Schiedermeier, rs@cs.hm.edu
// * @version 2017-04-11
// */
//class MinecraftArena extends CoreArena {
//    /** Neue Arena.
//     * Wenn dieser Ctor laeuft, muss Minecraft-Pi laufen.
//     * Der Ctor stellt sicher, dass Minecraft-Pi die gleiche Welt benutzt.
//     * @param landscape Landschaft.
//     * @param hostname IP-Adresse oder Hostname des Raspberry Pi.
//     * @throws IllegalStateException wenn in Minecraft-Pi eine andere Welt geladen ist.
//     */
//    MinecraftArena(Landscape landscape, String hostname) {
//        super(landscape);
//        final Minecraft mcpiWorld = Minecraft.connect(hostname);
//        MinecraftArenas.validateSameLandscape(landscape, mcpiWorld);
//    }
//
//    @Override public int getMillisRemaining() {
//        return Integer.MAX_VALUE;
//    }
//
//}
