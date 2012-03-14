package jpacman.model;

/**
 * Food having a number of calories, which can occupy a cell on the board.
 *
 * @author Arie van Deursen; Jul 28, 2003
 * @version $Id: Food.java 4261 2011-01-29 13:16:57Z arievandeursen $
 */
public class Food extends Guest
{
    
    public static final int DEFAULT_POINTS = 10;

    /**
     * Number of points this food element represents.
     */
    private int points;

    /**
     * Create a new Food element.
     *
     * @param p
     *            Calories of this piece of food.
     */
    public Food(int p)
    {
        points = p;
        assert foodInvariant();
    }

    /**
     * Create a simple piece of food of just one point.
     */
    public Food()
    {
        this(DEFAULT_POINTS);
        assert foodInvariant();
    }

    /**
     * Whatever happens, the amount of food is non-negative.
     *
     * @return True iff the food is non-negative and the guest's super invariant
     *         holds as well.
     */
    public final boolean foodInvariant()
    {
        return guestInvariant() && points >= 0;
    }

    /**
     * Provide the number of calories for this piece of food.
     *
     * @return Points of this food element.
     */
    public int getPoints()
    {
        return points;
    }

    /**
     * The player wants to eat this food cell. Modify the move's state to
     * reflect the effect this would have. Precondition: the move is in its
     * initialization stage.
     *
     * @see jpacman.model.Guest#meetPlayer(jpacman.model.PlayerMove)
     * @param theMove the move the player intends to do.
     * @return True, since such a move is possible.
     */
    @Override
    protected boolean meetPlayer(PlayerMove theMove)
    {
        assert foodInvariant();
        assert theMove != null;
        assert !theMove.isInitialized();
        theMove.setFoodEaten(this);
        return true;
    }


    /**
     * @see jpacman.model.Guest#guestType()
     * @return character encoding for food guests.
     */
    @Override
    public char guestType()
    {
        return Guest.FOOD_TYPE;
    }

}
