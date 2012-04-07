package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

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
public class UndoPlayerMoveTest extends MoveTest
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
    
    @Test
    public void testUndo1()
    {
        Cell oldCell = getThePlayer().getLocation();
        
        PlayerMove move = new PlayerMove(getThePlayer(), getEmptyCell());
        move.apply();
        
        assertNotSame(oldCell, getThePlayer().getLocation());
        move.undo();
        assertEquals(oldCell, getThePlayer().getLocation());    
    }
    @Test
    public void testUndo2()
    {
        Cell oldCell = getThePlayer().getLocation();
        int oldPoints = getThePlayer().getPointsEaten();
        
        PlayerMove move = new PlayerMove(getThePlayer(), getFoodCell());
        move.apply();
        
        assertNotSame(oldCell, getThePlayer().getLocation());
        assertNotSame(oldPoints, getThePlayer().getPointsEaten());
        
        move.undo();
        
        assertEquals(oldCell, getThePlayer().getLocation());
        assertEquals(oldPoints, getThePlayer().getPointsEaten());
    }
    @Test
    public void testUndo3()
    {
        Cell oldCell = getTheMonster().getLocation();
        
        MonsterMove move = new MonsterMove(getTheMonster(), getEmptyCell());
        move.apply();
        
        assertNotSame(oldCell, getTheMonster().getLocation());
        move.undo();
        assertEquals(oldCell, getTheMonster().getLocation());
    }
    @Test
    public void testUndo4()
    {
        Cell oldCell = getThePlayer().getLocation();
        boolean living = getThePlayer().living();

        PlayerMove move = movePlayerToCell(getMonsterCell());

        assertEquals(oldCell, getThePlayer().getLocation());
        assertNotSame(living, getThePlayer().living());
        move.undo();
        assertEquals(oldCell, getThePlayer().getLocation());
        assertEquals(living, getThePlayer().living());
    }
    @Test
    public void testUndo5()
    {
        Cell oldCell = getTheMonster().getLocation();
        boolean living = getThePlayer().living();
        
        MonsterMove move = moveMonsterToCell(getPlayerCell());
        
        assertEquals(oldCell, getTheMonster().getLocation());
        assertNotSame(living, getThePlayer().living());
        move.undo();
        assertEquals(oldCell, getTheMonster().getLocation());
        assertEquals(living, getThePlayer().living());
    }
    @Test
    public void testUndo6()
    {
        Cell foodCell1 = getTheGame().getBoard().getCell(0, 1);
        Cell foodCell2 = getTheGame().getBoard().getCell(0, 2);
        
        PlayerMove move1 = new PlayerMove(getThePlayer(), foodCell1);
        PlayerMove move2 = new PlayerMove(getThePlayer(), foodCell2);
        
        assertFalse(getTheGame().playerWon());
        move1.apply();
        assertFalse(getTheGame().playerWon());
        move2.apply();
        assertTrue(getTheGame().playerWon());
        
        move2.undo();
        assertFalse(getTheGame().playerWon());
        move1.undo();
        assertFalse(getTheGame().playerWon());
    }
    @Test
    public void testUndo7()
    {
        Cell oldCell = getThePlayer().getLocation();
        PlayerMove move = movePlayerToCell(getEmptyCell());
        
        assertNotSame(oldCell, getThePlayer().getLocation());
        
        move.undo();
        assertEquals(oldCell, getThePlayer().getLocation());
        
        move.undo();
        assertEquals(oldCell, getThePlayer().getLocation());
        
        move.undo();
        assertEquals(oldCell, getThePlayer().getLocation());
    }
}
