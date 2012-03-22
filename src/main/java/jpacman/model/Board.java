package jpacman.model;

/**
 * Maintain a rectangular board of cells, potentially occupied by guests. After
 * the board has been created, the dimensions cannot be modified anymore. Guests
 * can move around on the board, and keep track of their position on the board.
 *
 * @author Arie van Deursen; Jul 27, 2003
 * @version $Id: Board.java 4281 2011-02-01 10:40:02Z arievandeursen $
 */
public class Board
{

    /**
     * Width and height of the board.
     */
    private final int width, height;

    /**
     * The array of cells constituting the board.
     */
    private final Cell[][] cellAt;

    /**
     * Create a new board given a width and a height.
     *
     * @param w
     *            Width of the board
     * @param h
     *            Height of the board
     */
    public Board(int w, int h)
    {
        assert w >= 0;
        assert h >= 0;
        width = w;
        height = h;
        cellAt = new Cell[w][h];
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                cellAt[x][y] = new Cell(x, y, this);
            }
        }
        assert invariant();
        assert consistentBoardCellAssociation();
    }

    /**
     * A board's invariant is simply that both the width and the height are not
     * negative.
     *
     * @return True iff widht and height nonnegative.
     */
    protected final boolean invariant()
    {
        return width >= 0 && height >= 0;
    }

    /**
     * Check that each cell has a correct link to this board. This function
     * could be part of the invariant, but checking it each time is considered
     * too expensive, which is why it is offered as a separate function.
     *
     * @return True iff the cell/board association is consistent
     */
    protected final boolean consistentBoardCellAssociation()
    {
        boolean result = true;
        for (Cell[] row : cellAt)
        {
            for (Cell c : row)
            {
                result = result && c.getBoard().equals(this);
            }
        }
        return result;
    }

    /**
     * Return the cell at position (x,y). Precondition: (x,y) falls wihtin the
     * borders of the board. Postcondition: returned cell exists and is not
     * null.
     *
     * @param x
     *            Horizontal coordinate of the requested cell
     * @param y
     *            Vertical coordinate of the requested cell
     * @return The cell at (x,y).
     */
    public Cell getCell(int x, int y)
    {
        assert invariant();
        assert withinBorders(x, y)
        : "Cell requested (" + x + "," + y + ") out of borders "
        + width + " * " + height;
        Cell result = cellAt[x][y];
        assert result != null;
        assert invariant();
        return result;
    }

    /**
     * Return the guest occupying position (x,y), or null if the cell is emtpy.
     * Precondition: (x,y) falls wihtin the borders of the board.
     *
     * @param x
     *            Horizontal coordinate of the requested cell
     * @param y
     *            Vertical coordinate of the requested cell
     * @return The guest at (x,y).
     */
    public Guest getGuest(int x, int y)
    {
        assert invariant();
        assert withinBorders(x, y);
        return cellAt[x][y].getInhabitant();
    }

    /**
     * Return the guest code of the cell at (x,y).
     *
     * @param x
     *            Horizontal position
     * @param y
     *            Vertical position
     * @return Code representing guest type
     */
    public char guestCode(int x, int y)
    {
        assert invariant();
        assert withinBorders(x, y);
        char result = Guest.EMPTY_TYPE;
        Guest guest = getGuest(x, y);
        if (guest != null)       
        {
            result = guest.guestType();
        }
        assert invariant();
        return result;
    }

    /**
     * Return true iff (x,y) falls within the borders of the board.
     *
     * @param x
     *            Horizontal coordinate of requested position
     * @param y
     *            Vertical coordinate of requested position.
     * @return True iff (x,y) is on the board.
     */
    public boolean withinBorders(int x, int y)
    {
        if(x >= 0 && x < width && y >= 0 && y < height) 
            return true;
        
        return false;
    }

    /**
     * Return the width of the board.
     *
     * @return The board's with
     */
    public int getWidth()
    {
        assert invariant();
        return width;
    }

    /**
     * Return the height of the board.
     *
     * @return The board's height.
     */
    public int getHeight()
    {
        assert invariant();
        return height;
    }

    /**
     * @return an ascii version of the board that can be read back.
     */
    @Override
    public String toString()
    {
        String result = "";
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                result += guestCode(x, y);
            }
            result += "\n";
        }
        return result;
    }
}
