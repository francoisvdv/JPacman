package jpacman.model;

/**
 * Class containing responsibilities shared by all Guests.
 *
 * @author Arie van Deursen; Jul 28, 2003
 * @version $Id: Guest.java 4258 2011-01-29 08:50:00Z arievandeursen $
 */
public abstract class Guest
{

    /**
     * The cell the guest is occupying.
     */
    private Cell location = null;

    /**
     * The character code representing the player guest type.
     */
    public static final char PLAYER_TYPE = 'P';

    /**
     * The character code representing the monster guest type.
     */
    public static final char MONSTER_TYPE = 'M';

    /**
     * The character code representing the food guest type.
     */
    public static final char FOOD_TYPE = 'F';

    /**
     * The character code representing the wall guest type.
     */
    public static final char WALL_TYPE = 'W';

    /**
     * The character code representing an empty cell.
     */
    public static final char EMPTY_TYPE = '0';

    /**
     * Create a new Guest satisfying the class invariant.
     */
    public Guest()
    {
        assert guestInvariant();
    }

     /**
      * Actual invariant left as an exercise.
      * @return true iff invariant holds.
      */
     protected final boolean guestInvariant()
     {
         return true;
     }


    /**
     * @return The location of this guest.
     */
    public Cell getLocation()
    {
        return location;
    }

    /**
     * Occupy a non-null, empty cell.
     * Precondition: the current Guest must not
     * have occupied another cell, and the target cell should be empty.
     * Postcondition: both the cell and the guest
     * have changed their pointers to reflect the occupation.
     *
     * @param aCell
     *            New location for this guest.
     */
    public void occupy(Cell aCell)
    {
        assert guestInvariant();

        location = aCell;
        aCell.addGuest(this);

        assert guestInvariant();
    }

    /**
     * Remove the guest from the cell it occupies.
     * The method assumes the guest is occupying a cell,
     * and ensures it does not do so anymore after the method
     * has been called. Also the formerly occupied cell
     * is empty afterwards.
     */
    public void deoccupy()
    {
        assert guestInvariant();
        // Save the old location so we can free it after resetting ourselves
        Cell oldLocation = location;
        // Reset the guest's pointer
        location = null;
        // Reset the cell's guest association.
        oldLocation.removeGuest(this);
        assert guestInvariant();
    }

    /**
     * The player would like to visit the cell occupied by this guest. Indicate
     * whether this is possible, and modify the move's state to indicate what
     * the effect of such a move would be. Precondition: the move object is non
     * null and initializing.
     * <p>
     *
     * @param aMove
     *            theMove move object representing intended move and its
     *            effects.
     *
     * @return True iff this guest has no objection to the player taking his
     *         place.
     */
    protected abstract boolean meetPlayer(PlayerMove aMove);


    /**
     * Return a character code representing the type of guest.
     *
     * @return Type code for this guest.
     */
    public abstract char guestType();

}
