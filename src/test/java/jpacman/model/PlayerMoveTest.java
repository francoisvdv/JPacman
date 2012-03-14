package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Specialize the general MoveTest test suite to one
 * that is tailored to PlayerMoves.
 * Thanks to inheritance, all test cases from MoveTest
 * are also methods in PlayerMoveTest, thus helping us
 * to test conformance with Liskov's Substitution Principle (LSP)
 * of the Move hierarchy.
 * <p>
 * @author Arie van Deursen; August 21, 2003.
 * @version $Id: PlayerMoveTest.java 4260 2011-01-29 10:12:29Z arievandeursen $
 */
public class PlayerMoveTest extends MoveTest
{
   
    /**
     * @return the current (Player) move we're testing.
     */
    @Override
    protected PlayerMove getMove()
    {
        return (PlayerMove) super.getMove();
    }

    /**
     * Simple test of a few getters.
     */
    @Test
    public void testSimpleGetters()
    {
        PlayerMove playerMove = new PlayerMove(getThePlayer(), getFoodCell());
        assertEquals(getThePlayer(), playerMove.getPlayer());
        assertTrue(playerMove.movePossible());
        assertFalse(playerMove.playerWillDie());
        assertEquals(Food.DEFAULT_POINTS, playerMove.getFoodEaten());
        assertTrue(playerMove.invariant());
    }


    /**
     * Create a move object that will be tested.
     *  @see jpacman.model.MoveTest#createMove(jpacman.model.Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
    @Override
    protected PlayerMove createMove(Cell target)
    {
        setMove(new PlayerMove(getThePlayer(), target));
        return getMove();
    }
}
