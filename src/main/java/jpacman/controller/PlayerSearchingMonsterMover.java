package jpacman.controller;

import jpacman.model.Engine;
import jpacman.model.Monster;

/**
 *
 * @author Arie van Deursen; Sep 1, 2003
 * @version $Id: PlayerSearchingMonsterMover.java 4254 2011-01-28 10:40:40Z arievandeursen $
 */
public class PlayerSearchingMonsterMover extends AbstractMonsterController
{


    /**
     * Create a new player searching monster.
     * @param e the underlying engine.
     */
    public PlayerSearchingMonsterMover(Engine e)
    {
        super(e);
    }

    /**
     * @see jpacman.controller.IMonsterController#doTick()
     */
    public void doTick()
    {
        Monster m = getRandomMonster();
        if (m != null)
        {
            int px = getEngine().getPlayer().getLocation().getX();
            int py = getEngine().getPlayer().getLocation().getY();
            int mx = m.getLocation().getX();
            int my = m.getLocation().getY();
            int dx = closer(mx, px);
            int dy = closer(my, py);
            
            if (getRandomizer().nextBoolean())
            {
                // we're going to do x;
                dy = 0;
                dx = pick(dx);
             } else
            {
                // we're going to do y
                dx = 0;
                dy = pick(dy);
            }
 
            assert !(dx == 0 && dy == 0);
            getEngine().moveMonster(m, dx, dy);
        }
    }
    
    /**
     * Go into the given direction with a given probability.
     * @param dir The direction where you can find the player: 
     *            Can be 0 if it doesn't matter where to go. 
     * @return Either -1 or 1.
     */
    int pick(int dir)
    {
        assert dir == 1 || dir == -1 || dir == 0;
        int newdir = dir;
        if (newdir == 0)
        {
            if (getRandomizer().nextBoolean())
            {
                newdir = 1;
            } else
            {
                newdir = -1;
            }
        } else
        {
            final double deviationProbability = 0.2;
            if (getRandomizer().nextDouble() < deviationProbability)
            {
                newdir = -1 * dir;
            }
            // else ok, pick the suggested direction.
        }
        assert newdir == 1 || newdir == -1;
        return newdir;    
    }

    /**
     * If "from" is before "to", return direction 1,
     * if it is after, return -1,
     * and if from equals to return 0.
     * @param from starting coordinate
     * @param to ending coordinate
     * @return -1, 0, or +1
     */
    private int closer(int from, int to)
    {
        assert from >= 0;
        assert to >= 0;
        int direction = 0;
        if (from > to)
        {
            direction = -1;
        }
        else
        {
            if (from < to)
            {
                direction = +1;
            } else
            {
                assert from == to;
            }
        }
        assert direction == -1 || direction == 1 || direction == 0;
        return direction;
    }
}
