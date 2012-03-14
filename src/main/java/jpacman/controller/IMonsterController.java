package jpacman.controller;

/**
 * Interface for a controller which generates a monster move at regular
 * intervals.
 * <p>
 *
 * @author Arie van Deursen, 3 September, 2003
 * @version $Id: IMonsterController.java 3394 2010-02-21 16:09:39Z arievandeursen $
 */

public interface IMonsterController
{

    /**
     * Start the timer.
     */
    void start();

    /**
     * Stop the timer.
     */
    void stop();

    /**
     * Method that should be refined to conduct an actual monster move. By
     * default, nothing is done.
     */
    void doTick();

}
