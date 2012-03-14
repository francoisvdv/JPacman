package jpacman.model;

/**
 * Type for guests with moving capabilities. The actual moving is handled by the
 * Move class and its subclasses.
 *
 * @author Arie van Deursen; Aug 3, 2003
 * @version $Id: MovingGuest.java 4235 2011-01-24 21:07:15Z arievandeursen $
 */
public abstract class MovingGuest extends Guest
{

    /**
     * Constructs a new moving guest.
     */ 
    public MovingGuest() 
    {
        // Deferred to subclasses.
    }
}
