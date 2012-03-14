package jpacman.model;

/**
 * A wall positioned on a cell on the board.
 *
 * @author Arie van Deursen; Aug 2, 2003
 * @version $Id: Wall.java 4240 2011-01-27 14:21:33Z arievandeursen $
 */
public class Wall extends Guest
{

    /**
     * Create a new wall.
     */
    public Wall()
    {
        super();
    }

    /**
     * The player wants to occupy the wall's cell.
     * @param aMove the move the player wants to make.
     * @return false, the player cannot move here.
     *
     * @see jpacman.model.Guest#meetPlayer(jpacman.model.PlayerMove)
     */
    @Override
    public boolean meetPlayer(PlayerMove aMove)
    {
        assert guestInvariant();
        assert aMove != null;
        assert !aMove.isInitialized();
        return false;
    }

    /**
     * @see jpacman.model.Guest#guestType()
     * @return A character encoding for the wall.
     */
    @Override
    public char guestType()
    {
        return Guest.WALL_TYPE;
    }

}
