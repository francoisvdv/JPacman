package jpacman.controller;

import jpacman.model.Engine;
import jpacman.model.GameLoadException;

/**
 * Top level Pacman class. The main method creates the engine, the GUI, and the
 * controllers; the methods of the Pacman object created get invoked by the GUI
 * and the controllers.
 *
 * @author Arie van Deursen; Aug 31, 2003
 * @version $Id: Pacman.java 4244 2011-01-27 19:46:18Z arievandeursen $
 */
public class Pacman
{

    /**
     * The model of the game.
     */
    private Engine theEngine;

    /**
     * The display of the game.
     */
    private PacmanUI theViewer;

    /**
     * A controller that moves monsters around.
     */
    private IMonsterController monsterTicker;

    /**
     * A controller triggering animations.
     */
    private Animator theAnimator;

    /**
     * Create a default new game, containing an egnine, a gui, and a monster
     * driver.
     */
    public Pacman()
    {
        this(new Engine());
        assert invariant();
    }

    /**
     * Create a new game around a given engine, using the default random
     * monster mover.
     *
     * @param e
     *                The Engine to be used.
     */
    public Pacman(Engine e)
    {
        this(e, new PlayerSearchingMonsterMover(e));
        assert invariant();
    }

    /**
     * Create a new game from a given engine and monster mover.
     *
     * @param e
     *                The Engine to be used, not null.
     * @param m
     *                The monster mover to be used, not null.
     */
    public Pacman(Engine e, IMonsterController m) 
    {
        assert e != null;
        assert m != null;
        theEngine = e;
        initEngine();
        monsterTicker = m;
        theViewer = new PacmanUI(theEngine, this);
        theAnimator = new Animator(theViewer.getBoardViewer());
        theViewer.display();
        assert invariant();
    }
    
    private void initEngine()
    {
        assert theEngine != null;
        try
        {
            theEngine.initialize();
        } catch (GameLoadException gle)
        {
            System.err.println(// NOPMD by Arie on 1/16/11 2:42 PM
                "Could not load specified game: proceeding with default values." 
                + gle);
        }
    }

    /**
     * Instance variables that can't be null.
     * @return True iff selected instance variables all aren't null.
     */
    protected final boolean invariant()
    {
        return theEngine != null && monsterTicker != null && theViewer != null;
    }

    /**
     * Start the new game.
     */
    public void start()
    {
        assert invariant();
        theEngine.start();
        monsterTicker.start();
        theAnimator.start();
        assert invariant();
    }

    /**
     * Halt the pacman game.
     */
    public void quit()
    {
        assert invariant();
        monsterTicker.stop();
        theEngine.quit();
        theAnimator.stop();
        assert invariant();
    }


    /**
     * Terminate the game.
     */
    public void exit()
    {
        assert invariant();
        quit();
        theViewer.dispose();
        // No need for a hard exit using, e.g., System.exit(0):
        // we'd like to be able to run a series of pacman's in a single
        // JUnit test suite.
        assert invariant();
    }

    /**
     * Respond to an up request from the GUI.
     */
    @SuppressWarnings("PMD.ShortMethodName")
    public void up()
    {
        assert invariant();
        theEngine.movePlayer(0, -1);
        assert invariant();
    }

    /**
     * Respond to a down request from the GUI.
     */
    public void down()
    {
        assert invariant();
        theEngine.movePlayer(0, 1);
        assert invariant();
    }

    /**
     * Respond to a left request from the GUI.
     */
    public void left()
    {
        assert invariant();
        theEngine.movePlayer(-1, 0);
        assert invariant();
    }

    /**
     * Respond to a right request from the GUI.
     */
    public void right()
    {
        assert invariant();
        theEngine.movePlayer(1, 0);
        assert invariant();
    }

    /**
     * @return the Engine of this pacman game
     */
    public Engine getEngine()
    {
        return theEngine;
    }

    /**
     * Start me up.
     *
     * @param args
     *                Are ignored.
     * @throws GameLoadException 
     *                  If images can't be found.
     */
    public static void main(String[] args) throws GameLoadException
    {
        if (args.length > 0)
        {
            System.err.println(// NOPMD by Arie on 1/16/11 2:41 PM
                    "Ignoring command line arguments."); 
        }
        new Pacman();
    }
}
