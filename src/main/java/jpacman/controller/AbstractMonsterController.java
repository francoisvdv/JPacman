package jpacman.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import jpacman.model.Engine;
import jpacman.model.Monster;

/**
 * A controller which generates a monster move at regular intervals. The actual
 * move generation is deferred to subclasses, which can use their own moving
 * strategy. As more different monster controller subclasses are created, more
 * shared monster moving methods can be put in this class.
 * <p>
 *
 * @author Arie van Deursen, 3 September, 2003
 * @version $Id: AbstractMonsterController.java,v 1.1 2006/08/29 14:49:23 arie
 *          Exp $
 */
public abstract class AbstractMonsterController implements ActionListener,
IMonsterController
{

    /**
     * Randomizer used to pick, e.g., a monster at random.
     */
    private static Random randomizer = new Random();

    /**
     * Timer to be used to trigger monster moves.
     */
    private final Timer timer;

    /**
     * Vector of monsters that are to be moved.
     */
    private List<Monster> monsters;

    /**
     * Underlying game engine.
     */
    private final Engine theEngine;

    
    /**
     * The default delay between monster moves.
     */
    public static final int DELAY = 40;

    /**
     * Create a new monstercontroller using the default
     * delay and the given game engine.
     *
     * @param e
     *            The underlying model of the game.
     */
    public AbstractMonsterController(Engine e)
    {
        theEngine = e;
        timer = new Timer(DELAY, this);
        assert controllerInvariant();
    }

    /**
     * Variable that should always be set.
     * @return true iff all vars non-null.
     */
    protected final boolean controllerInvariant()
    {
        return timer != null && theEngine != null;
    }

    /**
     * ActionListener event caught when timer ticks.
     * @param e Event caught.
     */
    public void actionPerformed(ActionEvent e)
    {
        assert controllerInvariant();
        synchronized (getEngine())
        {
            doTick();
        }
        assert controllerInvariant();
    }

    /**
     * @see jpacman.controller.IMonsterController#start()
     */
    public void start()
    {
        assert controllerInvariant();
        // the game may have been restarted -- refresh the monster list
        // contained.
        synchronized (getEngine())
        {
            monsters = getEngine().getMonsters();
            timer.start();
            assert monsters != null;
        }
        assert controllerInvariant();
     }

    /**
     * @see jpacman.controller.IMonsterController#stop()
     */
    public void stop()
    {
        assert controllerInvariant();
        timer.stop();
        assert controllerInvariant();
    }

    /**
     * Return a randomly chosen monster, or null if there
     * are no monsters in this game.
     * @return Random monster or null;
     */
    protected Monster getRandomMonster()
    {
        Monster theMonster = null;
        if (!monsters.isEmpty())
        {
            final int monsterIndex = randomizer.nextInt(monsters.size());
            theMonster = monsters.get(monsterIndex);
        } 
        return theMonster;
    }

    /**
     * Obtain the randomizer used for monster moves.
     * @return the randomizer.
     */
    protected static Random getRandomizer()
    {
        return randomizer;
    }

    /**
     * @return The Engine containing the monsters.
     */
    protected Engine getEngine()
    {
        return theEngine;
    }
}
