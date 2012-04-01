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
      * This should always hold. If a Guest is in a Cell, the Cell should also
      * know it has this Guest.
      * @return true iff invariant holds.
      */
    protected final boolean guestInvariant()
    {
        return location == null || location.contains(this);
    }


    /**
     * @return The location of this guest.
     */
    public Cell getLocation()
    {
        return location;
    }

    /**
     * Occupy a Cell with this Guest.
     * @pre The current Guest must not have occupied another cell.
     * aCell can't be null.
     * @post Both the cell and the guest have changed their pointers to
     * reflect the occupation.
     *
     * @param aCell
     *            New location for this guest.
     */
    public void occupy(Cell aCell)
    {
        assert guestInvariant();

        assert aCell != null;
        assert !aCell.contains(this);
        assert location == null;
        
        location = aCell;
        aCell.addGuest(this);

        assert aCell.contains(this);
        assert location != null;
        
        assert guestInvariant();
    }

    /**
     * Remove the Guest from the Cell it occupies.
     * The method assumes the Guest is occupying a cell,
     * and ensures it does not do so anymore after the method
     * has been called.
     */
    public void deoccupy()
    {
        assert guestInvariant();
        
        assert location != null;
        assert location.contains(this);
        
        // Save the old location so we can free it after resetting ourselves
        Cell oldLocation = location;
        // Reset the guest's pointer
        location = null;
        // Reset the cell's guest association.
        oldLocation.removeGuest(this);
        
        assert location == null;
        assert !oldLocation.contains(this);
        
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
     * The monster would like to visit the cell occupied by this guest. Indicate
     * whether this is possible, and modify the move's state to indicate what
     * the effect of such a move would be. Precondition: the move object is non
     * null and initializing.
     * <p>
     *
     * @param aMove
     *            theMove move object representing intended move and its
     *            effects.
     *
     * @return True iff this guest has no objection to the monster taking his
     *         place.
     */
    protected abstract boolean meetMonster(MonsterMove aMove);

    /**
     * Return a character code representing the type of guest.
     *
     * @return Type code for this guest.
     */
    public abstract char guestType();

}
