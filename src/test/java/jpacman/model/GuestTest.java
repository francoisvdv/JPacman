package jpacman.model;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Test the Cell Guest association.
 * Originally set up following Binder's Class Association Test
 * pattern.
 *
 * @author Arie van Deursen; Jul 29, 2003
 * @version $Id: GuestTest.java 4914 2011-10-17 10:44:32Z arievandeursen $
 */
public class GuestTest
{

    /**
     * Two cells that we'll (try to) occupy.
     */
    private Cell theCell, anotherCell;

    /**
     * Two guests that will participate in occupying cells.
     */
    private Guest theGuest, anotherGuest;

    /**
     * Create two cells, and two guests, which we can
     * use for occupying / deoccupying different cells with various guests.
     */
    @Before
    public void setUp()
    {
        // create the board.
        int width = 2;
        int height = 2;
        Board theBoard = new Board(width, height);

        // obtain two cells.
        int x = 0;
        int y = 0;
        theCell = theBoard.getCell(x, y);
        anotherCell = theBoard.getCell(x + 1, y);

        // create two guests.
        theGuest = new Food();
        anotherGuest = new Food();
    }
    
    /**
     * Minimal tests for some basic properties.
     */
    @Test
    public void testSimpleProperties()
    {
        assertThat(theCell, is(not(anotherCell)));
        assertThat(theGuest, is(not(anotherGuest)));
        assertThat(theGuest.guestType(), is(Guest.FOOD_TYPE));
        assertThat(theGuest.getLocation(), nullValue());
    }

    @Test
    public void testOccupyDeoccupy()
    {
        //Testing the adding of a guest
        theGuest.occupy(theCell);
        assertTrue(theCell.contains(theGuest));
        assertEquals(theGuest.getLocation(), theCell);
        
        theGuest.deoccupy();
        assertFalse(theCell.contains(theGuest));
        assertEquals(theGuest.getLocation(), null);
        
        theGuest.occupy(theCell);
        assertTrue(theCell.contains(theGuest));
        assertEquals(theGuest.getLocation(), theCell);
        
        theGuest.deoccupy();
        assertFalse(theCell.contains(theGuest));
        assertEquals(theGuest.getLocation(), null);
    }
    
    
}
