package jpacman.model;

/**
 * Class to represent the effects of moving the player.
 *
 * @author Arie van Deursen; Aug 18, 2003
 * @version $Id: PlayerMove.java 4261 2011-01-29 13:16:57Z arievandeursen $
 */
public class PlayerMove extends Move
{

    /**
     * The player wishing to move.
     */
    private final Player thePlayer;
    
    /**
     * Food that may have been eaten.
     */
    private Food food = null;

    /**
     * Create a move for the given player to a given target cell.
     *
     * @param player
     *            the Player to be moved
     * @param newCell
     *            the target location.
     * @see jpacman.model.Move
     */
    public PlayerMove(Player player, Cell newCell)
    {
        // preconditions checked in super method,
        // and cannot be repeated here ("super(...)" must be 1st stat.).
        super(player, newCell);
        thePlayer = player;
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
        return moveInvariant() &&  thePlayer != null
        && getMovingGuest().equals(thePlayer);
    }

    /**
     * Attempt to move the player towards a target guest.
     * @param targetGuest The guest that the player will meet.
     * @return true if the move is possible, false otherwise.
     * @see jpacman.model.Move#tryMoveToGuest(jpacman.model.Guest)
     */
    @Override
    protected boolean tryMoveToGuest(Guest targetGuest)
    {
        assert tryMoveToGuestPrecondition(targetGuest)
            : "percolated precondition";
        return targetGuest.meetPlayer(this);
    }

    /**
     * Return the player initiating this move.
     *
     * @return The moving player.
     */
    public Player getPlayer()
    {
        assert invariant();
        return thePlayer;
    }

    /**
     * Set the food that will be eaten by this move.
     *
     * @param food
     *            the amount of food.
     */
    protected void setFoodEaten(Food food)
    {
        assert invariant() : "Invariant before set food.";
        this.food = food;
        assert invariant() : "Invariant in set Food Eaten";
    }

    /**
     * Actually apply the move, assuming it is possible.
     */
    @Override
    public void apply()
    {
        assert invariant();
        assert movePossible();
        if (food != null)
        {
            food.deoccupy();
        }
        super.apply();
        if (food != null)
        {
            final int oldFood = getPlayer().getPointsEaten();
            final int newFood = food.getPoints();
            getPlayer().eat(newFood);
            assert getPlayer().getPointsEaten() == oldFood + newFood;
        }
        assert invariant();
    }
    
    /**
     * Undo the move.
     */
    @Override
    public void undo()
    {
        assert invariant();
        
        super.undo();
        
        if(food != null)
        {
            //food has been eaten by this move, so we need to restore that
            food.occupy(getArrivalCell());
            thePlayer.eat(-food.getPoints());
        }
        if(playerWillDie())
        {
            //the player has died as a result of the move done. We need to
            //bring the player back alive now.
            thePlayer.reanimate();
        }
        
        assert invariant();
    }
    
    /**
     * @return The amount of food that will be eaten by this move.
     */
    public int getFoodEaten()
    {
        int result = 0;
        if (food != null) 
        {
            result = food.getPoints();
        }
        return result;
   }
}
