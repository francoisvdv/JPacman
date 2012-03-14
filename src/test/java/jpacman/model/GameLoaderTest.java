package jpacman.model;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static jpacman.model.GameLoader.checkSanity;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for loading games.
 * The main challenge lies in immitating failing IO behavior.s
 * 
 * @author Arie van Deursen
 * @version $Id: GameLoaderTest.java 5093 2011-12-19 12:46:08Z arievandeursen $
 */
public class GameLoaderTest
{
    
    private GameLoader gl;

    /**
     * Create a game loader that we can use for testing.
     */
    @Before
    public void setUp()
    {
        gl = new GameLoader();
    }
    
    /**
     * Test what happens if there is no property file.
     * @throws GameLoadException if there is no such property file.
     */
    @Test(expected = GameLoadException.class)
    public void testNonExistingPropfile() throws GameLoadException
    {
        gl.loadProperties("thispropertyfiledoesnotexist");
    }
    
    /**
     * Test what happens if we search for a non-existing property.
     * @throws GameLoadException if the property can't be found.
     */
    @Test(expected = GameLoadException.class)
    public void testNonExistingProperty() throws GameLoadException
    {
        gl.loadProperties(GameLoader.PROPERTY_FILE);
        gl.getMapFileName("thisisnotaproperty");
    }  
 
    /**
     * Thest what happens if the file containing the map doesn't exist.
     * @throws GameLoadException if the map can't be found.
     */
    @Test(expected = GameLoadException.class)
    public void testNonExistingMapFile() throws GameLoadException
    {
         gl.getMap("thismapfiledoesnotexist");
    }  

    /**
     * Test that the default board conforms to the sanity checks.
     */
    @Test
    public void testCorrectBoard()
    {
        assertNull(checkSanity(GameLoader.DEFAULT_WORLD_MAP));
    }
    
    /**
     * An empty board is not OK.
     */
    @Test
    public void testEmptyBoard()
    {
        String[] map = new String[] {};
        assertNotNull(checkSanity(map));
    }
    
    /**
     * A board with too short rows is not OK.
     */
    @Test
    public void testRowsTooShort()
    {
        String[] map = new String[] { "" };
        assertNotNull(checkSanity(map));
    }
    
    
    /**
     * A board with rows of different lengths is not OK.
     */
    @Test
    public void testRowsDifferent() 
   {
        String[] map = new String[] { "W", "WW" };
        assertNotNull(checkSanity(map));
    }

    /**
     * A board without a player is not ok.
     */
    @Test
    public void testNoPlayer()
    {
        String[] map = new String[] { "W", "W" };
        assertNotNull(checkSanity(map));
    }

    /**
     * A board with non-Guest characters is not OK.
     */
    @Test
    public void testIllegalChar() 
    {
        String[] map = new String[] { "/" };
        assertNotNull(checkSanity(map));
    }

    
}
