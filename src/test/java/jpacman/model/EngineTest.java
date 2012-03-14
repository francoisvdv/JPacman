package jpacman.model;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


/**
 * Systematic testing of the game state transitions.
 *
 * The test makes use of the simple map and its containing monsters
 * and players, as defined in the GameTestCase.
 * <p>
 *
 * @author Arie van Deursen; Aug 5, 2003
 * @version $Id: EngineTest.java 4924 2011-10-18 11:02:09Z arievandeursen $
 */
public class EngineTest extends GameTestCase
{

    /**
     * The engine that we'll push along every possible transition.
     */
    private Engine theEngine;


    /**
     * Set up an Engine, making use of the Game object
     * (with a small map containing all sorts of guests)
     * created in the superclass.
     * @throws GameLoadException if game resources can't be found.
     */
    @Before public void setUp() throws GameLoadException
    {
        theEngine = new Engine(getTheGame());
        theEngine.initialize();
        assertTrue(theEngine.inStartingState());
    }
    
    /**
     * Actually start the game.
     */
    @Test public void testStart()
    {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());    
    }

    /**
     * Example test case -- other test cases to be added by yourself!
     */
    @Test
    public void testSimpleMove()
    {
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        theEngine.movePlayer(1, 0);
        assertTrue(theEngine.inPlayingState());
    }
    

}
