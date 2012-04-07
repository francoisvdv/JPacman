package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Specialize the general MoveTest test suite to one
 * that is tailored to MonsterMoves.
 * Thanks to inheritance, all test cases from MoveTest
 * are also methods in MonsterMoveTest, thus helping us
 * to test conformance with Liskov's Substitution Principle (LSP)
 * of the Move hierarchy.
 * <p>
 * @author Francois van der Ven, April 2012.
 */
public class UndoMonsterMoveTest extends MoveTest
{
   
    /**
     * @return the current (Player) move we're testing.
     */
    @Override
    protected MonsterMove getMove()
    {
        return (MonsterMove) super.getMove();
    }

    /**
     * Create a move object that will be tested.
     *  @see jpacman.model.MoveTest#createMove(jpacman.model.Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
    @Override
    protected MonsterMove createMove(Cell target)
    {
        setMove(new MonsterMove(getTheMonster(), target));
        return getMove();
    }
}
