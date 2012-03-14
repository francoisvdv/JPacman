package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for methods working directly on Cells.
 *
 * @author Arie van Deursen; Jul 29, 2003
 * @version $Id: CellTest.java 4771 2011-06-28 14:20:46Z arievandeursen $
 */
public class CellTest
{

    /**
     * Width and height of test board to be used.
     */
    private static final int WIDTH = 5, HEIGHT = 5;

    /**
     * The board the cells occur on.
     */
    private Board aBoard;

    /**
     * The "Cell Under Test".
     */
    private Cell boundaryCell, centerCell;

    /**
     * Actually create the board and the cells.
     */
    @Before
    public void setUpBoard()
    {
        aBoard = new Board(WIDTH, HEIGHT);
        // put the cell on a "risky" invariant boundary value.
        boundaryCell = aBoard.getCell(0, HEIGHT - 1);
        centerCell = aBoard.getCell(1, 1);
    }


    
    /**
     * Test obtaining a cell at a given offset.
     */
    @Test 
    public void testCellAtOffset()
    {
        // correct processing of x coordinate
        assertEquals(2, aBoard.getCell(1, 1).cellAtOffset(1, 0).getX());
        
        // correct processing of y coordinate.
        assertEquals(2, aBoard.getCell(1, 1).cellAtOffset(0, 1).getY());
        
        // actually getting a cell.
        assertEquals(boundaryCell, centerCell.cellAtOffset(-1, HEIGHT - 2));
    }
    
    /**
    * Test the adjacent function.
    */
    @Test
    public void testAdjacent()
    {
        Cell ul = aBoard.getCell(0, 0);
        Cell ur = aBoard.getCell(WIDTH - 1, 0);
        Cell ll = aBoard.getCell(0, HEIGHT - 1);
        Cell lr = aBoard.getCell(WIDTH - 1, HEIGHT - 1);

        assertTrue(ul.adjacent(aBoard.getCell(1, 0)));
        assertTrue(ul.adjacent(aBoard.getCell(0, 1)));
        assertFalse(ul.adjacent(aBoard.getCell(1, 1)));

        assertTrue(ur.adjacent(aBoard.getCell(2, 0)));
        assertFalse(ur.adjacent(aBoard.getCell(2, 1)));

        assertTrue(ll.adjacent(aBoard.getCell(0, 3)));
        assertFalse(ll.adjacent(aBoard.getCell(1, 3)));
        assertFalse(ll.adjacent(aBoard.getCell(3, 2)));

        assertFalse(lr.adjacent(aBoard.getCell(0, 2)));
        assertFalse(lr.adjacent(aBoard.getCell(2, 3)));
    }
}
