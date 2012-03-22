package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * One of the simpler test classes, so a good point to start understanding
 * how testing JPacman has been setup.
 *
 * @author Arie van Deursen; Jul 29, 2003
 * @version $Id: BoardTest.java 4236 2011-01-24 22:44:49Z arievandeursen $
 */
public class BoardTest
{

    /**
     * The width & height of the board to be used.
     */
    private static final int WIDTH = 5, HEIGHT = 10;

    /**
     * The board to be used in the tests.
     */
    private Board theBoard;

    /**
     * Create a simple (empty) board to be used for testing.
     * Note that JUnit invokes such a Before method before every test case
     * so that we start in a fresh situation.
     */
    @Before
    public void setUp()
    {
        theBoard = new Board(WIDTH, HEIGHT);
    }

    /**
     * Simple test of the width/height getters,
     * to get in the mood for testing.
     */
    @Test
    public void testGettingWidthHeight()
    {
        assertEquals(WIDTH, theBoard.getWidth());
        assertEquals(HEIGHT, theBoard.getHeight());
    }

    /**
     * Obtain a cell that is on the board at given (x,y) position,
     * and check if the (x,y) coordinates of the Cell obtained are ok.
     * To make the test a little more exciting use a point that is on the
     * border (an "invariant on point" in Binder's terminology).
     */
    @Test
    public void testGettingCellsFromBoard()
    {
        // the coordinates to be used.
        int x = 0;
        int y = 0;

        // actually get the cell.
        Cell aCell = theBoard.getCell(x, y);

        // compare the coordinates.
        assertEquals(x, aCell.getX());
        assertEquals(y, aCell.getY());
    }

    /**
     * Let a guest occupy one of the Cells on the board, and
     * check if it is actually there.
     * Again, pick a point on the border (but a different one)
     * to (slightly) increase the chances of failure.
     */
    @Test
    public void testOccupy()
    {
        // place to put the guest.
        int x = WIDTH - 1;
        int y = HEIGHT - 1;
        Cell aCell = theBoard.getCell(x, y);

        // guest to be put on the board.
        Food food = new Food();

        // put the guest on the cell.
        food.occupy(aCell);

        // verify its presence.
        assertEquals(food, theBoard.getGuest(x, y));
        assertEquals(Guest.FOOD_TYPE, theBoard.guestCode(x, y));
    }

    /**
     * Try to create an illegal board (e.g., negative sizes),
     * and verify that this generates an assertion failure.
     * This test also serves to illustrate how to test whether a method
     * generates an assertion failure if a precondition is violated.
     */
     @Test
    public void testFailingBoardAssumingAssertions()
     {
        // only need to run this test if assertion
        // checking is enabled:
        assumeTrue(Board.class.desiredAssertionStatus());
        boolean failureGenerated;
        try
        {
            new Board(-1, -1);
            failureGenerated = false;
        } catch (AssertionError ae)
        {
            failureGenerated = true;
        }
        assertTrue(failureGenerated);
    }
    

}
