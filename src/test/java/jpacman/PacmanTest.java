package jpacman;


import static org.junit.Assert.assertTrue;

import jpacman.controller.AbstractMonsterController;
import jpacman.controller.EmptyMonsterController;
import jpacman.controller.Pacman;
import jpacman.model.Engine;
import jpacman.model.GameLoadException;
import jpacman.model.GameTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Systematic acceptance test for Pacman.
 * It follows the test suite design document,
 * which in turn follows the Pacman use cases.
 *
 * The test cases have been derived by translating the use
 * cases into test-ready decision tables and state machines,
 * both available in the JPacman documentation.
 *
 * @author Arie van Deursen; 5-sep-2004
 * @version $Id: PacmanTest.java 5171 2012-01-18 10:04:11Z arievandeursen $
 */
public class PacmanTest extends GameTestCase
{

    /**
     * The full game, including a gui.
     */
    private Pacman myPacman;

    /**
     * Set up the game and its UI, making use of the
     * game model created in the superclass.
     */
    @Before public void setUp() 
    {
        myPacman = new Pacman(new Engine(getTheGame()),
                        new EmptyMonsterController());
        myPacman.start();
        assertTrue(myPacman.getEngine().inPlayingState());
    }

    /**
     * And, at the end, nicely close the windows.
     */
    @After public void tearDown()
    {
        assert myPacman != null : "Done after setUp";
        myPacman.exit();
    }

    /**
     * Simple 'smoke test' running the default GUI, map, monstercontroller,
     * observers, etc. Note that this test just executes various methods to make sure that
     * they don't crash immediately.
     * More elaborate tests including proper asserts are conducted elsewhere.
     * @throws InterruptedException if threads don't work well
     * @throws GameLoadException If resources can't be found.
     */
    @Test 
    public void smokeTest()
    throws InterruptedException,  GameLoadException
    {
        
        // start things up
        Pacman p = new Pacman();
        p.start();
        assertTrue(p.getEngine().inPlayingState());
        
        // make some random moves
        p.left();
        p.right();

        
        // let the monsters move.
        final int nrOfMonsterMoves = 10;
        Thread.sleep(AbstractMonsterController.DELAY * nrOfMonsterMoves);
 
        // continue making some random moves.
        p.up();
        p.down();
        
        // let's take a break.
        p.quit();
        assertTrue(!p.getEngine().inPlayingState());
        p.start();
        
        // and move again.
        p.left();
        p.right();
        p.up();
        p.down();
        p.exit();
    }

     // The remaining acceptance test suite heavily depends on
     // proper implementation of the functionality that has been
     // left out as an exercise, and hence has been omitted from
     // this version.

}
 