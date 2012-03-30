package jpacman.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import jpacman.model.Engine;
import jpacman.model.Guest;

/**
 * A simple way to paint a pacman board. Benefited from code by code from the
 * Java 2D Arcs Demo. Other inspiration taken from course CS102 from
 * Bilkent university, 2003.
 * <p/>
 * It should be fairly easy to create a nicer viewer, but since this is not the
 * topic of the testing course we leave it at this.
 * <p/>
 *
 * @author Arie van Deursen; Jul 28, 2003
 * @version $Id: BoardViewer.java 4378 2011-03-06 10:58:37Z arievandeursen $
 */
public class BoardViewer extends JPanel
{

    /** Since a JPanel (through JComponent) is serializable,
     *  so is the BoardViewer which extends it,
     *  which is why it should have a serialVersionUID.
     */
    static final long serialVersionUID = -4976741292570616918L;

    /**
     * The interface to the model of the game,
     * through the Engine Facade.
     */
    private final Engine engine;

    /**
     * Buffered image used for drawing cells.
     */
    private BufferedImage bimg = null;

    /**
     * Width of an individual cell, in pixels.
     */
    private static final int CELL_WIDTH = 20;

    /**
     * Height of an individual cell, in pixels.
     */
    private static final int CELL_HEIGHT = 20;

    /**
     * The horizontal gap between cells, in pixels.
     */
    public static final int CELL_HGAP = 1;

    /**
     * The vertical gap between cells, in pixels.
     */
    public static final int CELL_VGAP = 1;

    /**
     * The factory containing Image objects for the various animated images.
     * If the image's can't be found on the file system,
     * default colors are used.
     */
    @SuppressWarnings("PMD.ImmutableField")
    private ImageFactory imageFactory;

    /**
     * Indicator for animation.
     */
    private int animationCount;


    /**
     * Create a new board viewer and attach
     * it to the underlying pacman model.
     *
     * @param theEngine
     *            The underlying pacman engine.
     */
    public BoardViewer(Engine theEngine)
    {
        engine = theEngine;
        setBackground(Color.white);
        animationCount = 0;
        imageFactory = new ImageFactory(CELL_WIDTH, CELL_HEIGHT);
        try
        {
            imageFactory.loadImages();
        } catch (IOException e)
        {
            // Issue a warning, and continue to work
            // with a null imageFactory.
            System.err.println("Can't load images: " + e); // NOPMD by Arie on 1/16/11 2:41 PM
            imageFactory = null;
        }
    }

    /**
     * @return The board width measured in cells, >= 0.
     */
    private int worldWidth()
    {
        return engine.boardWidth();
    }

    /**
     * @return The board height measured in cells, >= 0.
     */
    private int worldHeight()
    {
        return engine.boardHeight();
    }

    /**
     * The width of the board viewer in pixels.
     *
     * @return The width of the board viewer.
     */
    public int windowWidth()
    {
        return (cellWidth() + CELL_HGAP) * (worldWidth() + 1);
    }

    /**
     * The height of the board viewer in pixels.
     *
     * @return The height of the board viewer.
     */
    public int windowHeight()
    {
        return
        (cellHeight() + CELL_VGAP) * (worldHeight() + 1);
    }

    /**
     * The height of a cell (in pixels).
     * @return Cell height in pixels
     */
    public int cellHeight()
    {
        return CELL_HEIGHT;
    }

    /**
     * Width of cell.
     * @return Cell width in pixels.
     */
    public int cellWidth()
    {
        return CELL_WIDTH;
    }

    /**
     * JComponent method invoked when the board needs to be drawn.
     * @param g The graphics to paint the board on.
     */
    @Override
    public void paint(Graphics g)
    {
        Dimension d = getSize();
        Graphics2D g2 = createGraphics2D(d.width, d.height);
        drawCells(g2);
        g2.dispose();
        g.drawImage(bimg, 0, 0, this);
    }

    /**
     * Create a piece of graphics to display the board on.
     * @param w desired width in pixels
     * @param h desired height in pixels
     * @return The graphics to display the board.
     */
    private Graphics2D createGraphics2D(int w, int h)
    {
        Graphics2D g2 = null;
        if (bimg == null
                || bimg.getWidth() != w
                || bimg.getHeight() != h)
        {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.clearRect(0, 0, w, h);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        return g2;
    }

    /**
     * Draw an individual cell.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param g2 Graphics to draw on
     */
    private void drawCell(int x, int y, Graphics2D g2)
    {
        Dimension dim = new Dimension(cellWidth(), cellHeight());
        Point loc = new Point(
                2 * CELL_HGAP + (cellWidth() + CELL_HGAP) * x,
                2 * CELL_VGAP + (cellHeight() + CELL_VGAP) * y);
        Rectangle rect = new Rectangle(loc, dim);
        g2.setColor(Color.blue);
        g2.draw(rect);
        
        char[] guestCodes = engine.getGuestCodes(x, y);

        for(char gc : guestCodes)
        {
            Color fillColor = guestColor(gc);
            Image img = guestImage(gc);

            if (img == null)
            {
                assert fillColor != null;
                g2.setColor(fillColor);
                if (gc == Guest.FOOD_TYPE)
                {
                    g2.setColor(Color.black);
                    g2.fill(rect);
                    g2.setColor(Color.orange);
                    rect = centeredRectangle(x, y, 2);
                }
                g2.fill(rect);
            } else
            {
                g2.drawImage(img, loc.x, loc.y, this);            
            }
        }

    }
    
    /**
     * Return a rectangle with coordinates ensuring that 
     * the rectange's center is at (x,y).
     * @param x The requested x-coordinate of the center
     * @param y The requested y-coordinate of the center
     * @param delta Size in pixels of half the rectangle's length.
     * @return A rectangle of length 2*delta centered at (x,y).
     */
    Rectangle centeredRectangle(int x, int y, int delta)
    {
        assert delta <= cellWidth() / 2;
        Rectangle result = null;
        int startx = 2 * CELL_HGAP + (cellWidth() + CELL_HGAP) * x;
        int starty = 2 * CELL_VGAP + (cellHeight() + CELL_VGAP) * y;
        Point loc = new Point(
                startx + (cellWidth() / 2 - delta),
                starty + (cellHeight() / 2 - delta)
                );
        Dimension dim = new Dimension(2 * delta + 1, 2 * delta + 1);
        result = new Rectangle(loc, dim);
        return result;
    }
    
    /**
     * @param x x-coordinate on board
     * @param y y-coordinate on board
     * @return the appropriate image for the content
     * of the cell at (x,y).
     */
    Image guestImage(char guestCode)
    {
        Image img = null;
        if (imageFactory != null)
        {
            if (guestCode == Guest.PLAYER_TYPE)
            {
                img = imageFactory.player(
                        engine.getPlayerLastDx(),
                        engine.getPlayerLastDy(),
                        animationCount);
            } 
            if (guestCode == Guest.MONSTER_TYPE)
            {
                img = imageFactory.monster(animationCount);
            }
        }
        return img;
    }
    
    /**
     * @param x x-coordinate on board
     * @param y y-coordinate on board
     * @return the appropriate color for the content
     * of the cell at (x,y).
     */
    Color guestColor(char guestCode)
    {
        Color c = null;
        switch (guestCode)
        {
        case Guest.WALL_TYPE:
            c = Color.green;
            break;
        case Guest.PLAYER_TYPE:
            c = Color.YELLOW;
            break;
        case Guest.FOOD_TYPE:
            c = Color.GREEN;
            break;
        case Guest.MONSTER_TYPE:
            c = Color.RED;
            break;
        case Guest.EMPTY_TYPE:
            c = Color.BLACK;
            break;
        default:
            assert false : "Illegal guest code";
        }
        assert c != null;
        return  c;
    }

    /**
     * Draw all cells on the board.
     * @param g2 The graphics to draw the cells on.
     */
    private void drawCells(Graphics2D g2)
    {
        final float strokeWidth = 5.0f;
        g2.setStroke(new BasicStroke(strokeWidth));

        for (int x = 0; x < worldWidth(); x++)
        {
            for (int y = 0; y < worldHeight(); y++)
            {
                drawCell(x, y, g2);
            }
        }
    }

    /**
     * Increment the animation counter, and redisplay,
     * so that the next animation becomes visible.
     */
    public void nextAnimation()
    {
        if (imageFactory != null)
        {
            animationCount = (animationCount + 1)
            % (imageFactory.monsterAnimationCount()
                    * imageFactory.playerAnimationCount());
            repaint();
        }
    }
}
