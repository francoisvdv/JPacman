package jpacman.model;

/**
 * Class to represent the effects of moving the player.
 *
 * @author Arie van Deursen; Aug 18, 2003
 * @version $Id: PlayerMove.java 4261 2011-01-29 13:16:57Z arievandeursen $
 */
public class MonsterMove extends Move
{

    /**
     * The player wishing to move.
     */
    private final Monster theMonster;
    
    /**
     * Create a move for the given player to a given target cell.
     *
     * @param player
     *            the Player to be moved
     * @param newCell
     *            the target location.
     * @see jpacman.model.Move
     */
    public MonsterMove(Monster monster, Cell newCell)
    {
        // preconditions checked in super method,
        // and cannot be repeated here ("super(...)" must be 1st stat.).
        super(monster, newCell);
        theMonster = monster;
        precomputeEffects();
        assert invariant();
    }

    /**
     * Verify that the food eaten remains non negative, the player/mover equal
     * and non-null.
     *
     * @return true iff the invariant holds.
     */
    public final boolean invariant()
    {
        return moveInvariant() &&  theMonster != null
        && getMovingGuest().equals(theMonster);
    }

    /**
     * Attempt to move the monster towards a target guest.
     * @param targetGuest The guest that the monster will meet.
     * @return true if the move is possible, false otherwise.
     * @see jpacman.model.Move#tryMoveToGuest(jpacman.model.Guest)
     */
    @Override
    protected boolean tryMoveToGuest(Guest targetGuest)
    {
        assert tryMoveToGuestPrecondition(targetGuest)
            : "percolated precondition";

        return targetGuest.meetMonster(this);
    }

    /**
     * Return the monster initiating this move.
     *
     * @return The moving player.
     */
    public Monster getMonster()
    {
        assert invariant();
        return theMonster;
    }

    /**
     * Actually apply the move, assuming it is possible.
     */
    @Override
    public void apply()
    {
        assert invariant();
        assert movePossible();
        super.apply();
        assert invariant();
    }
}
