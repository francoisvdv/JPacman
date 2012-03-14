package jpacman.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.awt.Image;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * Fairly basic test cases for the image factory.
 * @author Arie van Deursen, TU Delft, created 2007.
 */
public class ImageFactoryTest
{

    /**
     * The factory under test.
     */
    private ImageFactory imf;

    /**
     * Actually create the image factory.
     * @throws IOException if images can't be found.
     */
    @Before public void setUp() throws IOException
    {
        imf = new ImageFactory();
        imf.loadImages();
    }

    /**
     * Are images for player properly loaded?
     */
    @Test public void testPlayer()
    {
        Image up = imf.player(0, -1, 1);
        Image down = imf.player(0, 1, 1);
        assertNotSame(up, down);
    }

    /**
     * Are monster images properly loaded?
     */
    @Test public void testMonster()
    {
        Image m1 = imf.monster(0);
        Image m2 = imf.monster(0);
        assertEquals(m1, m2);
    }
    
}