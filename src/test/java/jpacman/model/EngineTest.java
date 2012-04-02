package jpacman.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


/**
 * Systematic testing of the game state transitions.
 *
 * The test makes use of the simple map and its containing monsters
 * and players, as defined in the GameTestCase.
 * <p>
 *
 * @author Arie van Deursen; Aug 5, 2003
 * @version $Id: EngineTest.java 4924 2011-10-18 11:02:09Z arievandeursen $
 */
public class EngineTest extends GameTestCase
{

    /**
     * The engine that we'll push along every possible transition.
     */
    private Engine theEngine;


    /**
     * Set up an Engine, making use of the Game object
     * (with a small map containing all sorts of guests)
     * created in the superclass.
     * @throws GameLoadException if game resources can't be found.
     */
    @Before
    public void setUp() throws GameLoadException
    {
        theEngine = new Engine(getTheGame());
        theEngine.initialize();
        assertTrue(theEngine.inStartingState());
    }
    
    /**
     * Actually start the game.
     */
    @Test
    public void testStart()
    {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());    
    }

    /**
     * Example test case -- other test cases to be added by yourself!
     */
    @Test
    public void testSimpleMove()
    {
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        theEngine.movePlayer(1, 0);
        assertTrue(theEngine.inPlayingState());
    }
    
    @Test
    public void testPlayerKilledByPlayerMove()
    {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        assertTrue(theEngine.getPlayer().living());
        
        //Let the player die:
        movePlayerToCell(getMonsterCell());

        assertFalse(theEngine.getPlayer().living());
        assertTrue(theEngine.inGameOverState());
        assertTrue(theEngine.inDiedState());
        
        theEngine.start();
        assertTrue(theEngine.inStartingState());
    }
    
    @Test
    public void testPlayerKilledByMonsterMove()
    {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        assertTrue(theEngine.getPlayer().living());

        //Move player to a new cell
        assertEquals(theEngine.getPlayer().getLocation(), getPlayerCell());
        movePlayerToCell(getEmptyCell());
        assertEquals(theEngine.getPlayer().getLocation(), getEmptyCell());
        assertTrue(theEngine.inPlayingState());
        assertTrue(theEngine.getPlayer().living());
        
        //Move player back
        assertEquals(theEngine.getPlayer().getLocation(), getEmptyCell());
        movePlayerToCell(getPlayerCell());
        assertEquals(theEngine.getPlayer().getLocation(), getPlayerCell());
        assertTrue(theEngine.inPlayingState());
        assertTrue(theEngine.getPlayer().living());
        
        //Let the player die:
        moveMonsterToCell(getPlayerCell());

        assertFalse(theEngine.getPlayer().living());
        assertTrue(theEngine.inGameOverState());
        assertTrue(theEngine.inDiedState());
        
        theEngine.start();
        assertTrue(theEngine.inStartingState());
    }
    
    @Test
    public void testPlayerWon()
    {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        assertTrue(theEngine.getPlayer().living());

        theEngine.quit();
        assertTrue(theEngine.inHaltedState());
        
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        
        //Let the player win:
        Cell foodCell1 = getTheGame().getBoard().getCell(0, 1);
        Cell foodCell2 = getTheGame().getBoard().getCell(0, 2);
        movePlayerToCell(foodCell1);
        movePlayerToCell(foodCell2);
        
        assertTrue(theEngine.getPlayer().living());
        assertTrue(getTheGame().playerWon());
        assertTrue(theEngine.inGameOverState());
        assertTrue(theEngine.inWonState());
        
        theEngine.start();
        assertTrue(theEngine.inStartingState());
    }
    
    @Test
    public void testMovePlayerToCell()
    {
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        
        assertFalse(getThePlayer().getLocation() == getEmptyCell());
        movePlayerToCell(getEmptyCell());
        assertEquals(getThePlayer().getLocation(), getEmptyCell());
    }
    protected void movePlayerToCell(Cell target)
    {
        theEngine.movePlayer(
                target.getX() - getThePlayer().getLocation().getX(),
                target.getY() - getThePlayer().getLocation().getY());
    }
    @Test
    public void testMoveMonsterToCell()
    {
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        
        assertFalse(getTheMonster().getLocation() == getEmptyCell());
        moveMonsterToCell(getEmptyCell());
        assertEquals(getTheMonster().getLocation(), getEmptyCell());
    }
    void moveMonsterToCell(Cell target)
    {
        theEngine.moveMonster(getTheMonster(),
                target.getX() - getTheMonster().getLocation().getX(),
                target.getY() - getTheMonster().getLocation().getY());
    }
}
