package jpacman.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Representation of the board and its guests. This class's responsibilities
 * include the correct movement of moving guests, and keeping track of the state
 * of the game (whether the player died, or whether everything has been eaten,
 * for example)
 * <p>
 *
 * @author Arie van Deursen; Aug 24, 2003
 * @version $Id: Game.java 4234 2011-01-24 21:01:15Z arievandeursen $
 */
public class Game
{

    /**
     * The board containing all guests.
     */
    private Board theBoard = null;

    /**
     * The player of the game.
     */
    private Player thePlayer = null;

    /**
     * All monsters active in this game.
     */
    private List<Monster> monsters = null;

    /**
     * The total number of points that can be earned in this game.
     */
    private int totalPoints = 0;

    /**
     * The initial map / layout on the board.
     */
    private String[] theMap = null;

    /**
     * The moves the monster or the player has done.
     */
    private Stack<Move> moves = new Stack<Move>();
    

    /**
     * Create a new Game using a default map.
     */
    public Game()
    { /* No action needed */ }

    /**
     * Create a new Game using a custom map.
     * @param map The world to be used in the game.
     */
    public Game(String[] map)
    {
        theMap = map.clone();
    }

    /**
     * Set the fields of the game to their initial values.     *
     * @throws GameLoadException if the game can't be loaded
     *         (in which case default values are used).
     */
    void initialize() throws GameLoadException
    {
        if (theMap == null)
        {
            try
            {
                theMap = new GameLoader().obtainMap();
            } catch (GameLoadException gle)
            {
                // switch to default world map
                // (which should always load correctly).
                theMap = GameLoader.DEFAULT_WORLD_MAP;
                loadWorld(theMap);
                // inform outside world of switch to new map.
                throw gle;
            }
        }
        loadWorld(theMap);
        assert invariant();
    }
    
    /**
     * Reset the game.
     */
    void reInitialize()
    {
        assert theMap != null;
        assert invariant();
        loadWorld(theMap);
        assert invariant();
    }

    /**
     * Check whether all relevant fields have been initialized.
     *
     * @return true iff initialization has completed successfully.
     */
    public boolean initialized()
    {
        return theBoard != null 
            && thePlayer != null 
            && monsters != null
            && totalPoints >= 0;
    }

    /**
     * The game should always be in a consistent state,
     * in particular, a player cannot win and die at the same time.
     *
     * @return True iff the above holds.
     */
    private boolean consistent()
    {
        return !(playerDied() && playerWon())
        && thePlayer.getPointsEaten() <= totalPoints;
    }

    /**
     * A game is either not yet initialized, or it is in a consistent state.
     *
     * @return True iff this is the case.
     */
    protected boolean invariant()
    {
        return initialized() && consistent();
    }



    /**
     * Get the board of this game, which can be null if
     * the game has not been initialized.
     *
     * @return The game's board.
     */
    public Board getBoard()
    {
        return theBoard;
    }

    /**
     * Precondition: the player doesn't exist yet.
     * @return a new Player.
     */
    private Player createPlayer()
    {
        assert thePlayer == null : "Player exists already";
        thePlayer = new Player();
        return thePlayer;
    }

    /**
     * @return a new food element.
     */
    private Food createFood()
    {
        Food f = new Food();
        totalPoints += f.getPoints();
        return f;
    }

    /**
     * Create monster, and add it to the list of known monsters.
     * @return a new monster.
     */
    private Monster createMonster()
    {
        Monster m = new Monster();
        monsters.add(m);
        return m;
    }

    /**
     * Return the current player, which can be null if the game has not yet been
     * initialized.
     *
     * @return the player of the game.
     */
    public Player getPlayer()
    {
        return thePlayer;
    }

    /**
     * Return a fresh Vector containing all the monsters in the game.
     *
     * @return All the monsters.
     */
    public List<Monster> getMonsters()
    {
        assert invariant();
        List<Monster> result = new ArrayList<Monster>();
        result.addAll(monsters);
        assert result != null;
        return result;
    }

    /**
     * Add a new guest to the board.
     * @param code Representation of the sort of guest
     * @param x x-position
     * @param y y-position
     */
    private void addGuestFromCode(char code, int x, int y)
    {
        assert getBoard() != null : "Board should exist";
        assert getBoard().withinBorders(x, y);
        Guest theGuest = null;
        switch (code)
        {
        case Guest.WALL_TYPE:
            theGuest = new Wall();
            break;
        case Guest.PLAYER_TYPE:
            theGuest = createPlayer();
            break;
        case Guest.FOOD_TYPE:
            theGuest = createFood();
            break;
        case Guest.MONSTER_TYPE:
            theGuest = createMonster();
            break;
        case Guest.EMPTY_TYPE:
            theGuest = null;
            break;
        default:
            assert false : "unknown cell type``" + code + "'' in worldmap";
        break;
        }
        assert (code == Guest.EMPTY_TYPE && theGuest == null) || theGuest != null;
        if (theGuest != null)
        {
            theGuest.occupy(getBoard().getCell(x, y));
        }
        assert theGuest == null
            || getBoard().getCell(x, y).equals(theGuest.getLocation());
    }

    /**
     * Load a custom map. Postcondition: the invariant holds.
     *
     * @param map
     *            String array for a customized world map.
     */
    private void loadWorld(String[] map)
    {
        assert map != null;
        assert GameLoader.checkSanity(map) == null;
        int height = map.length;
        assert height > 0 : "at least one cell with one player required.";
        int width = map[0].length();
        assert width > 0 : "empty rows not permitted.";
        
        // initialize Game fields.
        monsters = new ArrayList<Monster>();
        totalPoints = 0;
        thePlayer = null;
        theBoard = null;



        assert theBoard == null;
        theBoard = new Board(width, height);

        // read the map into the cells
        for (int y = 0; y < height; y++)
        {
            assert map[y].length() == width
                : "all lines in map should be of equal length.";
            for (int x = 0; x < width; x++)
            {
                addGuestFromCode(map[y].charAt(x), x, y);
            }
        }
        assert invariant();
    }

    /**
     * Move the player to offsets (x+dx,y+dy). If the move is not possible
     * (wall, beyond borders), the move is not carried out. Precondition:
     * initialized and game isn't over yet. Postcondition: if the move is
     * possible, it has been carried out, and the game has been updated to
     * reflect the new situation.
     *
     * @param dx
     *            Horizontal movement
     * @param dy
     *            Vertical movement
     * @return Returns the PlayerMove.
     */
    protected PlayerMove movePlayer(int dx, int dy)
    {
        assert invariant();
        assert !gameOver() : "can only move when game isn't over";
        Cell targetCell =
            getPlayer().getLocation().cellAtOffset(dx, dy);
        PlayerMove playerMove = new PlayerMove(getPlayer(), targetCell);
        applyMove(playerMove);
        getPlayer().setLastDirection(dx, dy);
        assert invariant();
        
        moves.push(playerMove);
        
        return playerMove;
    }

    /**
     * Move the monster to offsets (x+dx,y+dy). If the move is not possible
     * (wall, beyond borders), the move is not carried out. Precondition:
     * initialized and game isn't over yet. Postcondition: if the move is
     * possible, it has been carried out, and the game has been updated to
     * reflect the new situation.
     *
     * @param dx
     *            Horizontal movement
     * @param dy
     *            Vertical movement
     * @return Returns the MonsterMove.
     */
    protected MonsterMove moveMonster(Monster monster, int dx, int dy)
    {
        assert invariant();
        assert !gameOver() : "can only move when game isn't over";
        Cell targetCell =
            monster.getLocation().cellAtOffset(dx, dy);
        MonsterMove monsterMove = new MonsterMove(monster, targetCell);
        applyMove(monsterMove);
        assert invariant();
        
        moves.push(monsterMove);
        
        return monsterMove;
    }
    
    
    /**
     * Actually apply the given move, if it is possible.
     * @param move The move to be made.
     */
    private void applyMove(Move move)
    {
        assert move != null;
        assert invariant();
        assert !gameOver();
        if (move.movePossible())
        {
            move.apply();
            assert move.moveDone();
            assert !playerDied() : "move possible => not killed";
        } else
        {
            if (move.playerWillDie())
            {
                assert !playerWon() : "you can't win by dying";
                getPlayer().die();
                assert playerDied();
            }
        }
        assert invariant();
    }

    /**
     * Undo the last move by the player or a monster. If there are no moves to
     * be undone, simply do nothing.
     */
    public void undoLastMove()
    {
        assert invariant();

        if(canUndo())
        {
            Move m = moves.pop();
            m.undo();
        }
        
        assert invariant();
    }
    
    /**
     * Check if the player has died. Precondition: initialization completed.
     *
     * @return True iff the player is dead.
     */
    public boolean playerDied()
    {
        assert initialized();
        return !getPlayer().living();
    }

    /**
     * Check if the player has eaten all food. Precondition: initialization
     * completed.
     *
     * @return True iff the player has won.
     */
    public boolean playerWon()
    {
        assert initialized();
        return getPlayer().getPointsEaten() >= totalPoints;
    }

    /**
     * Check whether the game is over.
     *
     * @return True iff the game is over.
     */
    public boolean gameOver()
    {
        assert initialized();
        return playerWon() || playerDied();
    }

    /**
     * Return the delta in the x-direction of the last
     * (attempted or succesful) player move.
     * Returns 0 if player hasn't moved yet.
     * @return delta in x direction
     */
    public int getPlayerLastDx()
    {
        return getPlayer().getLastDx();
    }

    /**
     * Return the delta in the y-direction of the last
     * (attempted or successful) player move.
     * Returns 0 if player hasn't moved yet.
     * @return delta in y direction
     */
    public int getPlayerLastDy()
    {
        return getPlayer().getLastDy();
    }

    /**
     * Return the width of the Board used.
     * @return width of the board.
     */
    public int boardWidth()
    {
        return getBoard().getWidth();
    }

    /**
     * Return the height of the Board used.
     * @return height of the board.
     */
    public int boardHeight()
    {
        return getBoard().getHeight();
    }

    /**
     * Return the guest code at position (x,y).
     * @param x x-coordinate of guest
     * @param y y-coordinate of guest
     * @return Character representing guest at x,y
     */
    public char[] getGuestCodes(int x, int y)
    {
        return getBoard().guestCodes(x, y);
    }
    
    /**
     * Return whether or not we can undo a move.
     * @return A value indicating if we can undo.
     */
    public boolean canUndo()
    {
        return moves.size() != 0;
    }
}
