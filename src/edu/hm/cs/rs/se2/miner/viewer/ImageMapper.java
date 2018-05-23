/* (C) 2017, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.8.0_121, Linux i386 4.7.4
 * violet (Intel Core i7 CPU 920/2668 MHz, 8 Cores, 12032 MB RAM)
 */
package edu.hm.cs.rs.se2.miner.viewer;

import edu.hm.cs.rs.se2.miner.arena.Arena;
import edu.hm.cs.rs.se2.miner.arena.Landscape;
import edu.hm.cs.rs.se2.miner.common.Event;
import static edu.hm.cs.rs.se2.miner.common.Event.Moved;
import edu.hm.cs.rs.se2.miner.common.Position;
import static edu.hm.cs.rs.se2.miner.common.State.Running;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import javax.imageio.ImageIO;

/** Viewer saving an image file of the map, the trail and points of interest.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2017-04-13
 */
public class ImageMapper extends Viewer {
    /** Alpha channel in ARGB color code. */
    private static final int ALPHA_BITMASK = 0xFF000000;

    /** Low byte of int value. */
    private static final int BITMASK_LOWBYTE = 0xFF;

    /** Pixel square size of map position. */
    private static final int IMAGE_SCALE = 8;

    /** Observed arena. */
    private final Arena arena;

    /** Image filename. */
    private final String filename;

    /** Flag: Image file written? */
    private boolean imageSaved;

    /** Positions the figure visited. */
    private final List<Position> trail = new ArrayList<>();

    /** New Viewer.
     * The image file is map.png.
     * @param arena Observed arena. Not null.
     */
    public ImageMapper(Arena arena) {
        this(arena, "map.png");
    }

    /** New Viewer.
     * @param arena Observed arena. Not null.
     * @param filename Name of image file to write.
     */
    public ImageMapper(Arena arena, String filename) {
        super(arena);
        this.arena = Objects.requireNonNull(arena);
        this.filename = Objects.requireNonNull(filename);
    }

    /** Build an image.
     * @return An image.
     */
    public BufferedImage buildMapImage() {
        final Landscape landscape = arena.getLandscape();
        final int imageSize = IMAGE_SCALE * landscape.getSize();
        final BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
        drawHeights(image, landscape);
        drawTrail(image, landscape.getStart());
        drawBox(image, landscape.getStart(), Color.BLUE.getRGB());
        drawBox(image, landscape.getDestination(), Color.YELLOW.getRGB());
        landscape.getMushrooms().stream().forEach(mushroom -> drawBox(image, mushroom, Color.RED.getRGB()));
        return image;
    }

    public void setImageSaved(boolean imageSaved) {
        this.imageSaved = imageSaved;
    }

    @Override @SuppressWarnings("unchecked") public void update(Observable ignored, Object reason) {
        try {
            if(reason instanceof Event)
                registerEvent((Event)reason);
            else if(reason instanceof Iterable<?>)
                for(Event event: (Iterable<Event>)reason)
                    registerEvent(event);
        }
        catch(IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /** Draws a filled box.
     * @param image Image to draw onto.
     * @param startX Box offset from left edge.
     * @param startY Box offset from top edge.
     * @param altitude Altitude of box.
     */
    private void drawBlock(BufferedImage image, int startX, int startY, int altitude) {
        final int maxAltitude = 255;
        if(altitude < 0 || altitude > maxAltitude)
            throw new IllegalArgumentException("altitude out of range: " + altitude);

        // fill box
        final int boxColor = grayscaleColorOf(altitude & BITMASK_LOWBYTE);
        for(int x = startX; x < startX + IMAGE_SCALE; x++)
            for(int y = startY; y < startY + IMAGE_SCALE; y++)
                setRGB(image, x, y, boxColor);

        // frame box
//        final int gridColor = Color.BLUE.getRGB();
//        final int midGray = 64;
//        final int grayShift = 16;
//        final int gridColor = grayscaleColorOf((altitude > midGray ? altitude - grayShift : altitude + grayShift) & BITMASK_LOWBYTE);
//        for(int x = startX; x < startX + IMAGE_SCALE; x++)
//            setRGB(image, x, startY, gridColor);
//        for(int y = startY; y < startY + IMAGE_SCALE; y++)
//            setRGB(image, startX, y, gridColor);
        setRGB(image, startX, startY, Color.WHITE.getRGB());
        setRGB(image, startX, startY + 1, Color.BLACK.getRGB());
    }

    /** Draws a wireframe square.
     * @param image Image to draw onto.
     * @param position Position in landscape.
     * @param boxColor Color.
     */
    private void drawBox(BufferedImage image, Position position, int boxColor) {
        final int atX = position.getLongitude() * IMAGE_SCALE;
        final int atY = position.getLatitude() * IMAGE_SCALE;
        for(int x = atX; x < atX + IMAGE_SCALE; x++) {
            setRGB(image, x, atY, boxColor);
            setRGB(image, x, atY + IMAGE_SCALE, boxColor);
        }
        for(int y = atY; y < atY + IMAGE_SCALE; y++) {
            setRGB(image, atX, y, boxColor);
            setRGB(image, atX + IMAGE_SCALE, y, boxColor);
        }
    }

    /** Draw a filled square for each map position.
     * @param image Image to draw onto.
     * @param landscape Landscape to draw.
     */
    private void drawHeights(BufferedImage image, final Landscape landscape) {
        final int size = landscape.getSize();
        for(int latitude = 0; latitude < size; latitude++)
            for(int longitude = 0; longitude < size; longitude++)
                drawBlock(image, IMAGE_SCALE * longitude, IMAGE_SCALE * latitude, landscape.getAltitude(latitude, longitude));
    }

    /** Draws a trail through all positions of the trail.
     * @param image Image to draw onto.
     * @param initial Initial position. Not part of trail.
     */
    private void drawTrail(BufferedImage image, Position initial) {
        Position previous = initial;
        for(Position current: trail) {
            final int fromX = previous.getLongitude() * IMAGE_SCALE;
            final int toX = current.getLongitude() * IMAGE_SCALE;
            final int fromY = previous.getLatitude() * IMAGE_SCALE;
            final int toY = current.getLatitude() * IMAGE_SCALE;
            drawTrailSegment(image,
                             Math.min(fromX, toX),
                             Math.min(fromY, toY),
                             Math.max(fromX, toX),
                             Math.max(fromY, toY));
            previous = current;
        }
    }

    /** Draw a paralaxial line.
     * @param image Image to draw onto.
     * @param minX Lower left point ordinate.
     * @param minY Lower left point abscissa.
     * @param maxX Upper right point ordinate.
     * @param maxY Upper right point abscissa.
     */
    private void drawTrailSegment(BufferedImage image, int minX, int minY, int maxX, int maxY) {
        final int trailColor = Color.GREEN.getRGB();
        final int boxCenterOffset = IMAGE_SCALE / 2;
        for(int x = minX; x < maxX; x++)
            setRGB(image, x + boxCenterOffset, minY + boxCenterOffset, trailColor);
        for(int y = minY; y < maxY; y++)
            setRGB(image, minX + boxCenterOffset, y + boxCenterOffset, trailColor);
    }

    /** Maps an altitude to a color.
     * @param altitude An altitude. Uses only the low byte, ignores the rest.
     * @return An ARGB color code.
     */
    private int grayscaleColorOf(int altitude) {
        final int bitshiftRedChannel = 16;
        final int bitshiftGreenChannel = 8;
        return 2 * (ALPHA_BITMASK
                    | (altitude << bitshiftRedChannel)
                    | (altitude << bitshiftGreenChannel)
                    | altitude);
    }

    /** Decides what to do with an event.
     * Appends the trail when the event is Moved.
     * Writes the image file when the state is anything but Running.
     * @param event The event.
     * @throws IOException when writing the image file fails.
     */
    private void registerEvent(Event event) throws IOException {
        if(event == Moved)
            trail.add(arena.getFigure());
        if(arena.getState() != Running)
            saveImageMap();
    }

    /** Write an image file.
     * @throws IOException when writing the file fails.
     */
    private void saveImageMap() throws IOException {
        if(imageSaved) // won't save twice
            return;
        ImageIO.write(buildMapImage(), filename.substring(filename.lastIndexOf('.') + 1), new File(filename));
        imageSaved = true;
    }

    /** Color a pixel. Discard pixels outside of image.
     * @param image The image.
     * @param x Horizontal pixel position.
     * @param y Vertical pixel position.
     * @param color 32 bit ARGB color code.
     */
    private void setRGB(BufferedImage image, int x, int y, int color) {
        if(x < 0)
            return;
        if(y < 0)
            return;
        if(x >= image.getWidth())
            return;
        if(y >= image.getHeight())
            return;
        // BufferedImage origin is in upper left corner.
        image.setRGB(x, image.getHeight() - 1 - y, color);
    }

}
