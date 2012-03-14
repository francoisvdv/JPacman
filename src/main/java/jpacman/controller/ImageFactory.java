package jpacman.controller;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * The responsibilities of this class include obtaining images from file,
 * that can be used for animations of the player and the monsters in Pacman.
 *
 * @author Arie van Deursen, Delft University of Technology, May 2007
 * @version $Id: ImageFactory.java 4916 2011-10-17 19:10:17Z arievandeursen $
 *
 */

public class ImageFactory
{

    /**
     * Animation sequence of images for monsters.
     */
    private Image[] monsterImage;

    /**
     * Animation sequence of images for the player.
     */
    private Image[][] playerImage;  
    
    /**
     * Width of the images.
     */
    private int width = -1;
    
    /**
     * Height of the images.
     */
    private int height = -1;
    
    /**
     * Create an empty (non intialized) image factory.
     */
    public ImageFactory()
    { /* Nothing needs to be done */ }
    
    /**
     * Create an empty (non intialized) image factory
     * requiring that all images are of the given (width, height).
     * @param w requested image width
     * @param h requested image height
     */
    public ImageFactory(int w, int h)
    { 
        width = w;
        height = h;
    }
     
    
    /**
     * Read images for player and monsters from file.
     * Different images exist for different phases of the animation.
     * @throws IOException if the images can't be found.
     */
    public void loadImages() throws IOException
    {
        monsterImage = new Image[]{
                getImage("Ghost1.gif"),
                getImage("Ghost2.gif") };

        String[] sequence = new String[]{"2", "3", "4", "3", "2"};
        String[] direction = new String[]{
                "right", "left", "down", "up" };
        playerImage = new Image[direction.length][sequence.length + 1];
        for (int dir = 0; dir < direction.length; dir++)
        {
            playerImage[dir][0] = getImage("PacMan1.gif");
            for (int seq = 0; seq < sequence.length; seq++)
            {
                String name = "PacMan" + sequence[seq]
                                 + direction[dir] + ".gif";
                playerImage[dir][seq + 1] = getImage(name);
            }
        }
        assert intialized();
    }

    /**
     * @return Number of different monster animation steps
     */
    public int monsterAnimationCount()
    {
        assert monsterImage != null;
        int result = monsterImage.length;
        assert result >= 0;
        return result;
    }

    /**
     * @return Number of different player animation steps
     */
    public int playerAnimationCount()
    {
        assert playerImage != null;
        assert playerImage[0] != null;
        return playerImage[0].length;
    }

    /**
     * Invariant that may be a bit expensive to compute all the time,
     * so it is selectively invoked.
     * @return true iff invariant holds and all images are non-null.
     */
    public boolean intialized()
    {
        boolean result = monsterImage != null;
        result = result && monsterImage.length > 0;
        for (int i = 0; i < monsterImage.length; i++)
        {
            result = result && monsterImage[i] != null;
        }
        result = result && playerImage != null;
        result = result && playerImage[0] != null;
        for (int i = 0; i < playerImage.length; i++)
        {
            for (int j = 0; j < playerImage[i].length; j++)
            {
                result = result && playerImage[i][j] != null;
            }
        }
        return result;
    }

    /**
     * Get a player (pizza slice) in the appropriate direction at the
     * given animation sequence.
     * 
     * TODO Refactor this so that enums are used -- will be cleaner,
     * and will resolve the checkstyle warning.
     * 
     * @param dx x direction
     * @param dy y direction
     * @param anim Animation step
     * @return Player image in appropriate direction.
     */
    public Image player(int dx, int dy, int anim)
    {
        assert anim >= 0;
        Image img = null;
        int dirIndex = 0;
        if (dx > 0)
        {
            dirIndex = 0;
        } else if (dx < 0)
        {
            dirIndex = 1;
        } else if (dy > 0)
        {
            dirIndex = 2;
        } else if (dy < 0)
        {
            dirIndex =
                    3;
        } else
        {
            assert dx == 0;
            assert dy == 0;
            // stick to default name.
        }
        img = playerImage[dirIndex][anim % playerAnimationCount()];
        assert img != null;
        return img;
    }

    /**
     * Obtain a picture of a monster.
     * @param animationIndex counter indicating which animation to use.
     * @return The monster image at the given animation index.
     */
    public Image monster(int animationIndex)
    {
        assert animationIndex >= 0;
        return monsterImage[animationIndex % monsterAnimationCount()];
    }

    /**
     * Obtain an image from a file / resource that can
     * be found on the classpath.
     * @param name The file containg, e.g., a .gif picture.
     * @return The corresponding Image.
     * @throws IOException If file can't be found.
     */
    private Image getImage(String name) throws IOException
    {
        assert name != null;
        
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL picfile = cl.getResource(name);
        if (picfile == null)
        {
            throw new IOException("Can't load image: "  + name);
        }
        Image result = new ImageIcon(picfile).getImage();
        assert result != null;
 
        return resize(result);
    }
    
     
    /**
     * Resize a given image to the required dimension.
     * @param im The image
     * @return The resized image.
     */
    Image resize(Image im)
    {
        assert im != null;
        Image result = im;
        if (width > 0 && height > 0)
        {
            int w = im.getWidth(null);        
            int h = im.getHeight(null);
            if (w != width || h != height)
            {
                result = im.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
            }
        }
        assert result != null;
        return result;
    }
}
