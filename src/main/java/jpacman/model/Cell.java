package jpacman.model;

import java.util.ArrayList;



/**
 * A Cell keeps track of its (x,y) position on the board, and the potential
 * Guest occupying the Cell. It's responsibilities include identifying
 * neighboring cells that fall within the Board's borders (to support moving
 * guests on the board), and keeping the Cell-Guest association consistent.
 *
 * @author Arie van Deursen; Jul 27, 2003
 * @version $Id: Cell.java 4492 2011-04-28 15:46:14Z arievandeursen $
 */
public class Cell
{

    /**
     * The x and y coordinates of the cell.
     */
    private final int x, y;

    /**
     * The board the cell lives on.
     */
    private final Board board;

    /**
     * The guests occupying the cell.
     */
    private final ArrayList<Guest> guests = new ArrayList<Guest>();

    /**
     * Create a new cell at a given position on the board.
     *
     * @param xCoordinate
     *            The X coordinate
     * @param yCoordinate
     *            The Y coordinate
     * @param b
     *            The board
     */
    public Cell(int xCoordinate, int yCoordinate, Board b)
    {
        x = xCoordinate;
        y = yCoordinate;
        this.board = b;
        assert invariant();
    }

    /**
     * Conjunction of all invariants.
     *
     * @return true iff all invariants hold.
     */
    protected final boolean invariant()
    {
        return guestInvariant() && boardInvariant();
    }

    /**
     * A Cell should always be part of the Board given at construction.
     *
     * @return true iff this is the case.
     */
    protected final boolean boardInvariant()
    {
        return board != null && board.withinBorders(x, y);
    }

     /**
     * This should always hold. If this Cell has a Guest, the Guest should also
     * know it belongs to this Cell.
     * @return true for the time being.
     */
     protected final boolean guestInvariant()
     {
         for (Guest g : guests)
         {
             if (g.getLocation() != this)
                 return false;
         }
         
         return true;
     }



    /**
     * Return the inhabitants of this cell.
     *
     * @return The (most recent) Guest hosted by this Cell, 
     *         or null if the Cell is free.
     */
    public ArrayList<Guest> getGuests()
    {
         return guests;
    }


    /**
     * Add a Guest to this cell. This method is needed by the Guest's
     * occupy method which keeps track of the links in the Cell-Guest
     * association. If the Guest is already in the Cell, it isn't added again -
     * nothing happens.
     * 
     * @pre The Guest's location should be set to this Cell.
     *
     * @param aGuest
     *            The new guest of this cell.
     */
    protected void addGuest(Guest aGuest)
    {
        assert aGuest.getLocation() == this;
        
        if (!guests.contains(aGuest))
            guests.add(aGuest);
    }

    
    /**
     * Remove the specified Guest from this Cell. (Only) to be used by
     * Guest.deoccupy(). If this Cell doesn't contain the Guest, nothing
     * happens.
     * 
     * @pre This method assumes that that the Guest has already removed its
     * link to this Cell. aGuest must not be null.
     *
     * @param aGuest The guest to be removed.
     */
    void removeGuest(Guest aGuest)
    {
        assert aGuest != null;
        assert aGuest.getLocation() == null;
        
        if (guests.contains(aGuest))
            guests.remove(aGuest);
    }

    
    /**
     * Determine if the guest is one of the inhabitants
     * of this cell.
     * 
     * @pre aGuest is not null.
     * @param aGuest Guest that may be here
     * @return true iff aGuest is on this cell.
     */
    public boolean contains(Guest aGuest)
    {
        assert aGuest != null;
        
        return guests.contains(aGuest);
    }


    /**
     * Get the horizontal position of this cell.
     *
     * @return the cell's X coordinate
     */
    public int getX()
    {
        assert invariant();
        return x;
    }

    /**
     * Get the vertical position of this cell.
     *
     * @return the cell's Y coordinate.
     */
    public int getY()
    {
        assert invariant();
        return y;
    }

    /**
     * Return the cell located at position (x + dx, y + dy). If the return cell
     * would lie out of the board, we 'tunnel' to the corresponding opposite 
     * side of the board and return that cell.
     *
     * @param dx
     *            The x offset
     * @param dy
     *            The y offset
     * @return the cell at (x+dx,y+dy) or if that's outside of the board the
     * corresponding cell at the opposite of the board.
     */
    public Cell cellAtOffset(int dx, int dy)
    {
        assert invariant();
        Cell result = null;
        int newx = (x + dx) % board.getWidth();
        int newy = (y + dy) % board.getHeight();
        
        if (newx < 0)
            newx += board.getWidth();
        if (newy < 0)
            newy += board.getHeight();
        
        assert getBoard().withinBorders(newx, newy);
        
        result = getBoard().getCell(newx, newy);
        
        assert invariant();
        return result;
    }
    

    /**
     * Return the board this cell is part of.
     *
     * @return This cell's board.
     */
    public Board getBoard()
    {
        assert invariant();
        return board;
    }
    
    /**
     * @return [x,y]@<guest code> string representation.
     */
    @Override
    public String toString()
    {
        final String location = "[" + x + "," + y + "]";
        String inhabitants = "";

        for (Guest g : guests)
        {
            inhabitants += g.guestType() + " ";
        }
        
        if (inhabitants.isEmpty())
            inhabitants = String.valueOf(Guest.EMPTY_TYPE);
        
        return inhabitants + "@ " + location;
    }
    
    /**
    * Determine if the other cell is an immediate
    * neighbor of the current cell.
    * @pre otherCell is not null
    * @param otherCell The cell to be checked if it is adjacent.
    * @return true iff the other cell is immediately adjacent.
    */
    public boolean adjacent(Cell otherCell)
    {
        assert invariant();
        assert otherCell != null;
        
        int dx = Math.abs(otherCell.getX() - getX());
        int dy = Math.abs(otherCell.getY() - getY());
        if (dx != dy && ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)))
            return true;

        return false;
    }
}