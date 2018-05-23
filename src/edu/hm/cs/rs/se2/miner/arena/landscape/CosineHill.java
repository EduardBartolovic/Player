package edu.hm.cs.rs.se2.miner.arena.landscape;

public class CosineHill extends PointsOfInterest {
    private static final int ALTITUDE_FLOOR = 1;

    private static final int ALTITUDE_TOP = 127;

    private final int hillRadius;

    public CosineHill() {
        this(Integer.toString(SIZE / 2) + ';');
    }

    public CosineHill(String givenPositions) {
        int commaAt = givenPositions.indexOf(';');
        hillRadius = Integer.parseInt(givenPositions.substring(0, commaAt));
        placePointsOfInterest(givenPositions.substring(commaAt + 1));
    }

    @Override public int getAltitude(int latitude, int longitude) {
        final double distanceToCenter = Math.hypot(SIZE / 2 - latitude, SIZE / 2 - longitude);
        if(distanceToCenter > hillRadius)
            return ALTITUDE_FLOOR; 
        return (int)ruleOf3(Math.cos(ruleOf3(distanceToCenter,
                                             0,
                                             hillRadius,
                                             0,
                                             Math.PI)),
                            -1,
                            1,
                            ALTITUDE_FLOOR,
                            ALTITUDE_TOP);
    }

    private double ruleOf3(double value, double inFrom, double inTo, double outFrom, double outTo) {
        return (value - inFrom) * (outTo - outFrom) / (inTo - inFrom) + outFrom;
    }
}