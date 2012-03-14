package jpacman.controller;


import jpacman.model.Engine;
import jpacman.model.Monster;

/**
 * Example, simple monster mover that just moves monsters randomly.
 *
 * @author Arie van Deursen; Aug 18, 2003
 * @version $Id: RandomMonsterMover.java 4223 2011-01-24 12:58:33Z arievandeursen $
 */
public class RandomMonsterMover extends AbstractMonsterController
{

    /**
     * Start a new mover with the given engine.
     *
     * @param theEngine Engine used.
     */
    public RandomMonsterMover(final Engine theEngine)
    {
        super(theEngine);
    }

    /**
     * Local enum for directions.
     */
    private enum Direction
    { UP, DOWN, LEFT, RIGHT };

    /**
     * Actually conduct a random move in the underlying engine.
     *
     * @see jpacman.controller.IMonsterController#doTick()
     */
    public void doTick()
    {
        synchronized (getEngine())
        {
            Monster theMonster = getRandomMonster();
            if (theMonster == null)
            {
                return;
            }

            int dx = 0;
            int dy = 0;

            final int dirIndex = getRandomizer().nextInt(Direction.values().length);
            final Direction dir = Direction.values()[dirIndex];
            switch(dir)
            {
            case UP:
                dy = -1;
                break;
            case DOWN:
                dy = 1;
                break;
            case LEFT:
                dx = -1;
                break;
            case RIGHT:
                dx = 1;
                break;
            default:
                assert false;
            }

            assert dy >= -1 && dy <= 1;
            assert
            Math.abs(dx) == 1 && dy == 0
            ||
            Math.abs(dy) == 1 && dx == 0;

            getEngine().moveMonster(theMonster, dx, dy);
        }
    }
}
