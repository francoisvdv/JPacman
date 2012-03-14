package jpacman.model;

import java.util.List;
import java.util.Observable;

/**
 * The top level state machine, which also acts as interface to the viewer
 * (which observes the state machine) and the controller (which triggers the
 * state machine events).
 *
 * @author Arie van Deursen; Aug 1, 2003
 * @version $Id: Engine.java 4914 2011-10-17 10:44:32Z arievandeursen $
 */
public class Engine extends Observable
{

    /**
     * The underlying pacman game, on top of which this engine runs.
     */
    private final Game theGame;

    /**
     * The starting state is special, since within the starting state the rest
     * of the game is being constructed. So other state inspection methods
     * should each check that they are not in the starting state first, and only
     * then their inspection can yield a meaningful result.
     */
    private boolean starting = false;

    /**
     * True iff the game is being suspended.
     */
    private boolean halted = false;

    /**
     * The game has been set up, and is just waiting to get started.
     * @return true iff game is starting.
     */
    public boolean inStartingState()
    {
        synchronized (this)
        {
            return starting && !(theGame.playerDied() 
                    || theGame.playerWon());
        }
    }

    /**
     * We're actually playing a game.
     * @return true iff game is still on.
     */
    public boolean inPlayingState()
    {
        synchronized (this)
        {
            return !starting && !halted && theGame.initialized()
            && !theGame.playerDied() && !theGame.playerWon();
        }
    }

    /**
     * Alas, we died after playing.
     * @return true iff player died.
     */
    public boolean inDiedState()
    {
        synchronized (this)
        {
            return !starting && theGame.playerDied();
        }
    }

    /**
     * Yes, we have been lucky and we won!
     * @return true iff player has won.
     */
    public boolean inWonState()
    {
        synchronized (this)
        {
            return !starting && theGame.playerWon();
        }
    }

    /**
     * We're pausing a little and halted the game.
     * @return true iff we're halted.
     */
    public boolean inHaltedState()
    {
        synchronized (this)
        {
            return halted;
        }
    }

    /**
     * A superstate representing having died or won.
     * @return true iff the game is over.
     */
    public boolean inGameOverState()
    {
        synchronized (this)
        {
            return inDiedState() || inWonState();
        }
    }

    /**
     * We can be in at most one of the Engine's states.
     *
     * @return True if we in exactly one state.
     */
    protected boolean invariant()
    {
        // beware of the xor:
        // xor on odd nr of args also permits all args to be true.
        // (see in3420 exam in 2003)
        final boolean oneStateOnly = inStartingState()
        ^ inPlayingState()
        ^ inHaltedState()
        ^ inDiedState()
        ^ inWonState()
        && !(inStartingState() && inPlayingState() && inHaltedState()
                && inDiedState() && inWonState());
        return oneStateOnly && theGame != null;
    }

    /**
     * Start a new engine with a default game using a default map.
     * Postcondition: the game is ready to be played.
     */
    public Engine()
    {
        this(new Game());
     }

    /**
     * Start a new engine with a specific game.
     *
     * @param game The Game to be used.
     */
    public Engine(Game game)
    {
        assert game != null;
        theGame = game;
    }

    /**
     * Initialize the game.
     * @throws GameLoadException If the game can't be loaded.
     */
    public void initialize() throws GameLoadException
    {
        assert theGame != null;
        if (!theGame.initialized())
        {
            theGame.initialize();
        }
        starting = true;
        assert inStartingState();
        assert invariant();
    }

    /**
     * Stop the game if it is in a playing state.
     */
    public void quit()
    {
        synchronized (this)
        {
            assert invariant();
            if (inPlayingState())
            {
                halted = true;
                notifyViewers();
            }
            assert invariant();
        }
    }

    /**
     * (Re)start the game, unless it is playing.
     */
    public void start()
    {
        synchronized (this)
        {
            assert invariant();
            if (inHaltedState())
            {
                assert halted;
                halted = false;
            } else
            {
                if (inStartingState())
                {
                    assert starting;
                    starting = false;
                } else
                {
                    if (inGameOverState())
                    {
                        assert !starting;
                        theGame.reInitialize();
                        starting = true;
                    }
                }
            }
            notifyViewers();
            assert invariant() : "Invariant invalid after updating the viewers";
        }
    }

    /**
     * Try to move the player along a given offset.
     *
     * @param dx
     *            Horizontal offset
     * @param dy
     *            Vertical offset.
     */
    public void movePlayer(int dx, int dy)
    {
        synchronized (this)
        {
            assert invariant();
            if (inPlayingState())
            {
                theGame.movePlayer(dx, dy);
                notifyViewers();
            }
            assert invariant();
        }
    }

    /**
     * Try to move the given monster along a given offset.
     *
     * @param monster
     *            The monster to be moved
     * @param dx
     *            Horizontal offset
     * @param dy
     *            Vertical offset
     */
    public void moveMonster(Monster monster, int dx, int dy) 
   {
         // TODO monster moves not yet supported.

    }



    /**
     * Warn the observers that the state has changed.
     */
    private void notifyViewers()
    {
        setChanged();
        notifyObservers();
    }

    /**
     * The game itself. Package visible, used for testing purposes.
     *
     * @return the full game.
     */
    Game getGame()
    {
        return theGame;
    }

    /**
     * The guest code at position x, y. Offered for viewers who would like to
     * draw the board.
     *
     * @param x
     *            X coordinate
     * @param y
     *            Y coordinate
     * @return Type code of the guest.
     */
    public char getGuestCode(int x, int y)
    {
        synchronized (this)
        {
            return getGame().getGuestCode(x, y);
        }
    }

    /**
     * Width of the board, offered for clients wishing to draw the board.
     *
     * @return the board's width.
     */
    public int boardWidth()
    {
        synchronized (this)
        {
            return getGame().boardWidth();
        }
    }

    /**
     * Height of the board, offered for clients wishing to draw the board.
     *
     * @return the board's height.
     */
    public int boardHeight()
    {
        synchronized (this)
        {
            assert invariant();
            return getGame().boardHeight();
        }
    }

    /**
     * The amount of food eaten so far.
     *
     * @return Amount of food eaten.
     */
    public int getFoodEaten()
    {
        synchronized (this)
        {
            assert invariant();
            return getPlayer().getPointsEaten();
        }
    }

    /**
     * Return a fresh vector containing all the monsters in the game.
     *
     * @return All monsters.
     */
    public List<Monster> getMonsters()
    {
        synchronized (this)
        {
            assert invariant();
            List<Monster> result = theGame.getMonsters();
            assert result != null;
            return result;
        }
    }

    /**
     * Return the player of the game.
     *
     * @return The game's player.
     */
    public Player getPlayer() 
   {
        synchronized (this)
        {
            assert invariant();
            return theGame.getPlayer();
        }
    }

    /**
     * @return Returns the most recent advancement of the player in the
     *         horizontal direction.
     */
    public int getPlayerLastDx()
    {
        synchronized (this)
        {
            return getGame().getPlayerLastDx();
        }
    }

    /**
     * @return Returns the most recent advancement of the player in the vertical
     *         direction.
     */
    public int getPlayerLastDy()
    {
        synchronized (this)
        {
            return getGame().getPlayerLastDy();
        }
    }
}
