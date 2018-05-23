/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.8.15
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2300 MHz, 16000 MByte RAM)
 **/
package edu.hm.cs.rs.se2.miner.common;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Generische Factoryklasse.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-18
 */
public class Factory {
    /** Wenn ein Security-Manager existiert,
     * laeuft die Klasse wahrscheinlich in der Courseware.
     * In diesem Fall sind Ausgaben auf die Konsole sinnlos.
     */
    private static final boolean useStdout = System.getProperty("java.security.policy") == null;

    /**
     * Produziert ein Objekt eines vorgegebenen Typs.
     * Wenn der Typ nicht existiert, sucht die Klasse in einem Defaultpackage.
     * Wenn er dort auch nicht existiert, gibt die Methode auf.
     * Die Methode erzeugt das Objekt mit einem Konstruktor.
     * Der erhaelt als erstes die initialArgs, gefolgt von den Argumenten der Spezifikation.
     * @param <T> Type des produzierten Objektes.
     * @param typenameAndArgs String mit einem Klassennamen mit oder ohne Packagepfad, gefolgt von Konstruktorargumenten.
     * Trenner zwischen Klassennamen und Argumenten sind Doppelpunkte oder Kommas.
     * @param initialArgs Argumente fuer den Konstruktor.
     * @return Ein Objekt vom typ T.
     * @exception ReflectiveOperationException wenn beim Erzeugen des Objektes etwas schief geht.
     */
    @SuppressWarnings("unchecked")
    public static <T> T make(String typenameAndArgs, Object... initialArgs) throws ReflectiveOperationException {
        final String[] token = typenameAndArgs.split("[^-\\w\\./;]");
        String typename = null;
        final List<Object> arglist = new ArrayList<>(Arrays.asList(initialArgs));
        for(String arg: token)
            if(typename == null)
                typename = arg.replace(File.separatorChar, '.');
            else
                try {
                    arglist.add(Integer.parseInt(arg));
                }
                catch(NumberFormatException exception) {
                    arglist.add(arg);
                }
        final Class<?> type = Class.forName(typename);
        return (T)Stream.of(type.getDeclaredConstructors())
            .filter(ctor -> ctor.getParameterTypes().length == arglist.size())
            .peek(ctor -> ctor.setAccessible(true))
            .peek(ctor -> logCtor(ctor, arglist))
            .findAny()
            .orElseThrow(IllegalArgumentException::new)
            .newInstance(arglist.toArray());
    }

    /** Ersetzt in einem String FQCN durch einfache Klassennamen.
     * @param string String mit FQCN.
     * @return String mit einfachen Klassennamen.
     */
    static String stripPackages(String string) {
        return string.replaceAll("(?:[_a-z0-9]+\\.)+([A-Z]\\w+)", "$1")
            .replace("public ", "");
    }

    /** Protokolliert die Argumente auf der Konsole, falls useStdout true ist.
     * @param constructor Konstruktor.
     * @param arglist Argumente.
     */
    private static void logCtor(Constructor<?> constructor, List<?> arglist) {
        if(useStdout)
            System.out.printf("make: %s %s%n",
                              stripPackages(constructor.toString()),
                              stripPackages(arglist.toString()));
    }

}
