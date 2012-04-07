package jpacman;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jpacman.controller.ImageFactoryTest;

import jpacman.model.*;



/**
 * Test suite containing all Pacman junit test cases.
 * <p>
 * Invoke from command line via
 * java -cp ... org.junit.runner.JUnitCore jpacman.TestAll
 *
 * @author Arie van Deursen; Aug 1, 2003
 * @version $Id: TestAll.java 5170 2012-01-18 08:37:54Z arievandeursen $
 */


/**
 * If you'd like your class to be tested,
 * include it below in the list of suite classes.
 */
@RunWith(Suite.class)
@SuiteClasses({
    PacmanTest.class,
    BoardTest.class,
    CellTest.class,
    GameTest.class,
    EngineTest.class,
    ObserverTest.class,
    PlayerMoveTest.class,
    MonsterMoveTest.class,
    UndoTest.class,
    ImageFactoryTest.class,
    GuestTest.class,
    GameLoaderTest.class
})

public final class TestAll 
{
    
    /**
     * Utility class, no constructor.
     */
    private TestAll()
    { }

    /**
     * Convenience method making it easiest to exercise all JPacman test cases.
     * Best way to invoke this class is via <br>
     * java -cp ... org.junit.runner.JUnitCore jpacman.TestAll
     * <br>
     * (which doesn't require the main method).
     * @param args All arguments are ignored.
     */
    public static void main(String[] args)
    {
        org.junit.runner.JUnitCore.runClasses(jpacman.TestAll.class);
    }

}
