package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This class offers a test suite for the Move class hierarchy
 * from Pacman. It contains test cases that should pass for
 * <em>all</em> subclasses of Move, and builds a parallel class
 * hierarchy for testing purposes.
 *
 * (For those familiar with Binder: this class follows the
 * <em>Polymorphic Server Test</em> design pattern).
 *
 * @author Arie van Deursen; Aug 2, 2003
 * @version $Id: MoveTest.java 4260 2011-01-29 10:12:29Z arievandeursen $
 */
public abstract class MoveTest extends GameTestCase
{

    /**
     * The move we make.
     */
    private Move aMove;

    /**
     * @return The move that we're testing
     */
    protected Move getMove()
    {
        return aMove;
    }

    /**
     * @param aMove The move that we're going to test.
     */
    protected void setMove(Move aMove)
    {
        this.aMove = aMove;
    }

    /**
     * A simple test case that should hold for all moves
     * that are possible: the guest should indeed have moved
     * after actually applying the move.
     * Note that the creation of the Move object itself
     * is deferred to subclasses via the createMove factory method.
     */
    @Test
    public void testApply()
    {
        // create move, mover, and cell to be moved to.
        createMove(getEmptyCell());
        MovingGuest mover = getMove().getMovingGuest();
        Cell location1 = mover.getLocation();
        assertNotNull(mover);
        assertNotNull(location1);

        // status before.
        assertTrue(getMove().movePossible());
        assertEquals(location1, mover.getLocation());
        assertEquals(mover, location1.getGuests().get(0));

        // do the move.
        getMove().apply();
        Cell location2 = mover.getLocation();
        assertNotNull(location2);
        assertFalse(location1.contains(mover));
        assertEquals(getEmptyCell(), location2);
        assertEquals(mover, location2.getGuests().get(0));
        assertTrue(getMove().moveDone());
        assertFalse(getMove().movePossible());

    }
    

    /**
     * Create a move object.
     * The actual guest to be moved (to the target Cell)
     * is determined in the subclasses, who also know how to create
     * the specific Move subclass for that type of guest.
     * (See the Gang-of-Four (Gamma et al) "Factory Method" design pattern)
     * 
     * Postcondition getMove() != null.
     * 
     * @param target Cell to be moved to
     * @return Instantiated Move subclass object.
     */
    protected abstract Move createMove(Cell target);
    
    /**
     * Test for the helper function movePlayerToCell.
     */
    @Test
    public void testMovePlayerToCell()
    {
        //A simple test for the helper function
                
        assertFalse(getThePlayer().getLocation() == getEmptyCell());
        movePlayerToCell(getEmptyCell());
        assertEquals(getThePlayer().getLocation(), getEmptyCell());
    }
    /**
     * Move player to cell.
     * @param target The target cell.
     * @return The PlayerMove object.
     */
    PlayerMove movePlayerToCell(Cell target)
    {
        return getTheGame().movePlayer(
                target.getX() - getThePlayer().getLocation().getX(),
                target.getY() - getThePlayer().getLocation().getY());
    }
    
    /**
     * Test for the helper function moveMonsterToCell.
     */
    @Test
    public void testMoveMonsterToCell()
    {
        assertFalse(getTheMonster().getLocation() == getEmptyCell());
        moveMonsterToCell(getEmptyCell());
        assertEquals(getTheMonster().getLocation(), getEmptyCell());
    }
    /**
     * Move monster to the specified cell.
     * @param target The target cell.
     * @return The MonsterMove object.
     */
    MonsterMove moveMonsterToCell(Cell target)
    {
        return getTheGame().moveMonster(getTheMonster(),
                target.getX() - getTheMonster().getLocation().getX(),
                target.getY() - getTheMonster().getLocation().getY());
    }
}
