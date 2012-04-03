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
public class MonsterMoveTest extends MoveTest
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
     * Simple test of a few getters.
     */
    @Test
    public void testSimpleGetters()
    {
        MonsterMove monsterMove = new MonsterMove(getTheMonster(), getFoodCell());
        assertEquals(getTheMonster(), monsterMove.getMonster());
        assertTrue(monsterMove.movePossible());
        assertFalse(monsterMove.playerWillDie());
        assertTrue(monsterMove.invariant());
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

    /** Test the monster colliding with another monster */
    @Test
    public void testMonsterCollision()
    {        
        MonsterMove monsterMove = createMove(getMonsterCell(1));
        assertFalse(monsterMove.playerWillDie());
        assertFalse(monsterMove.movePossible());

        Cell oldLocation = getTheMonster().getLocation();
        moveMonsterToCell(getMonsterCell(1)); //monster shouldn't move
        assertEquals(oldLocation, getTheMonster().getLocation());
    }
    /** Test the monster colliding with a Player */
    @Test
    public void testPlayerCollision()
    {
        MonsterMove monsterMove = createMove(getPlayerCell());
        assertTrue(monsterMove.playerWillDie());
        assertFalse(monsterMove.movePossible());
        
        Cell oldLocation = getTheMonster().getLocation();
        moveMonsterToCell(getPlayerCell()); //monster shouldn't move
        assertEquals(oldLocation, getTheMonster().getLocation());
        
        assertFalse(getThePlayer().living());
    }
    /** Test the monster colliding with a wall */
    @Test
    public void testWallCollision()
    {
        MonsterMove monsterMove = createMove(getWallCell());
        assertFalse(monsterMove.playerWillDie());
        assertFalse(monsterMove.movePossible());
        
        Cell oldLocation = getTheMonster().getLocation();
        moveMonsterToCell(getPlayerCell()); //monster shouldn't move
        assertEquals(oldLocation, getTheMonster().getLocation());
    }
    
    @Test
    public void testMoveMonsterToCell()
    {
        assertFalse(getTheMonster().getLocation() == getEmptyCell());
        moveMonsterToCell(getEmptyCell());
        assertEquals(getTheMonster().getLocation(), getEmptyCell());
    }
    void moveMonsterToCell(Cell target)
    {
        getTheGame().moveMonster(getTheMonster(),
                target.getX() - getTheMonster().getLocation().getX(),
                target.getY() - getTheMonster().getLocation().getY());
    }
}
