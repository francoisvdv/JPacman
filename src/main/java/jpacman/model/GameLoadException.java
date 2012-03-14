package jpacman.model;

/**
 * Representation of things that can go wrong when loading
 * games / maps from file.
 * 
 * @author Arie van Deursen, January 2009.
 * @version $Id: GameLoadException.java 3394 2010-02-21 16:09:39Z arievandeursen $
 */

public class GameLoadException extends Exception
{

    /**
     * ID possibly used for serialization.
     */
    private static final long serialVersionUID = -7677883417366623357L;

    /**
     * A new game loading exception.
     * @param message the given explanation.
     */
    public GameLoadException(String message)
    {
        super(message);
    }

    /**
     * A new game loading exception.
     * @param message Explanation of context
     * @param cause thrown earlier and propagated here.
     */
    public GameLoadException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
