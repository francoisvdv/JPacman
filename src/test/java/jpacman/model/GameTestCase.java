package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

/**
 * This is a JUnit test class, intended for use in
 * cases where a filled Pacman board is necessary.
 * This class creates such a filled board, and offers
 * various instance variables to access parts of the
 * board. Typical clients of this class will inherit
 * the game and its content from this class.
 * <p>
 * @author Arie van Deursen; Aug 23, 2003
 * @version $Id: GameTestCase.java 4244 2011-01-27 19:46:18Z arievandeursen $
 */
public class GameTestCase
{
    
    /**
     * Protected constructor since only subclasses are expected
     * to be the primary users.
     */
    protected GameTestCase()
    {
        /* Empty on purpose */
    }

    /**
     * The game that we're setting up for testing purposes.
     */
    private Game theGame;

    /**
     * The player of the game.
     */
    private Player thePlayer;

    /**
     * One of the monsters in the game.
     */
    private Monster theMonster;

    /**
     * A food element.
     */
    private Food theFood;

    /**
     * Series of cells containing different types of guests.
     */
    private Cell playerCell, wallCell, monsterCell, foodCell, emptyCell;

    /**
     * Simple map that can be used for various testing purposes.
     * It contains every type of guest, as well as several empty cells.
     */
    public static final String[] SIMPLE_MAP =
        new String[]{
        "0W0",
        "FP0",
        "FM0",
        "0WM"
    };


    /**
     * Setup a game that can be used in test cases
     * defined in subclasses of this class.
     * @throws GameLoadException  if the resources can't be found.
     */
    @Before
    public void setUpGame() throws GameLoadException
    {
        theGame = new Game(SIMPLE_MAP);
        theGame.initialize();

        Board theBoard = theGame.getBoard();

        wallCell = theBoard.getCell(1, 0);
        monsterCell = theBoard.getCell(1, 2);
        foodCell = theBoard.getCell(0, 1);
        playerCell = theBoard.getCell(1, 1);
        emptyCell = theBoard.getCell(2, 1);

        theFood = (Food) foodCell.getInhabitant();
        thePlayer = theGame.getPlayer();
        theMonster = (Monster) monsterCell.getInhabitant();
    }

    /**
     * Is the standard board created in the GameTestCase
     * setup correctly? Invoked from test method elsewhere,
     * but put close to the actual setup method.
     * Note: giving this the @Test annotation would invoke
     * this test in every subclass, which is not necessary.
     */
    protected void checkBoardSanity()
    {
        assertTrue(wallCell.getInhabitant() instanceof Wall);
        assertEquals(Guest.WALL_TYPE, wallCell.getInhabitant().guestType());
 
        assertTrue(monsterCell.getInhabitant() instanceof Monster);
        assertEquals(Guest.MONSTER_TYPE,
                monsterCell.getInhabitant().guestType());

        assertTrue(playerCell.getInhabitant() instanceof Player);
        assertEquals(Guest.PLAYER_TYPE,
                playerCell.getInhabitant().guestType());

        assertTrue(foodCell.getInhabitant() instanceof Food);
        assertEquals(Guest.FOOD_TYPE,
                foodCell.getInhabitant().guestType());

        assertNull(emptyCell.getInhabitant());

        assertTrue(theGame.getMonsters().contains(theMonster));
        assertEquals(2, theGame.getMonsters().size());
    }

    /**
     * @return the game instance.
     */
    public Game getTheGame()
    {
        return theGame;
    }

    /**
     * @return the player in this test game.
     */
    public Player getThePlayer()
    {
        return thePlayer;
    }

    /**
     * @return One (fixed) of the monsters in the game.
     */
    public Monster getTheMonster()
    {
        return theMonster;
    }

    /**
     * @return A (fixed) food element in the game.
     */
    public Food getTheFood()
    {
        return theFood;
    }

    /**
     * @return The location of the player.
     */
    public Cell getPlayerCell()
    {
        return playerCell;
    }

    /**
     * @return The location of a (fixed) wall element.
     */
    public Cell getWallCell()
    {
        return wallCell;
    }

    /**
     * @return The location of the given monster.
     */
    public Cell getMonsterCell()
    {
        return monsterCell;
    }

    /**
     * @return The locatiation of the given food element.
     */
    public Cell getFoodCell()
    {
        return foodCell;
    }

    /**
     * @return One (fixed) cell that is empty.
     */
    public Cell getEmptyCell()
    {
        return emptyCell;
    }
}
