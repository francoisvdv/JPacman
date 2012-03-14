package jpacman.model;

/**
 * A monster on the board.
 *
 * @author Arie van Deursen; Jul 28, 2003
 * @version $Id: Monster.java 4243 2011-01-27 17:57:04Z arievandeursen $
 */
public class Monster extends MovingGuest
{

    /**
     * Create a new monster, not occupying a cell yet.
     */
    public Monster()
    {
        super();
    }

    /**
     * The player decided to bumb into this monster. Modify the move's state
     * reflecting the fact that this will cause the player to die.
     *
     * @param theMove
     *            move object representing intended move and its effects.
     * @return false, the player cannot occupy the monster's cell.
     *
     * @see jpacman.model.Guest#meetPlayer(jpacman.model.PlayerMove)
     */
    @Override
    protected boolean meetPlayer(PlayerMove theMove)
    {
        assert guestInvariant();
        assert theMove != null;
        assert !theMove.isInitialized();
        theMove.die();
        return false;
    }

    /**
     * @see jpacman.model.Guest#guestType()
     * @return Character encoding for a monster.
     */
    @Override
    public char guestType()
    {
        return Guest.MONSTER_TYPE;
    }


}
