package jpacman.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * The primary responsibility of this class is
 * to trigger the board viewer to display the
 * next animation.
 *
 * @author Arie van Deursen, 2007.
 * @version $Id: Animator.java 4222 2011-01-24 11:28:49Z arievandeursen $
 *
 */
public class Animator
{

    /**
     * The viewer that must be informed to show the
     * next animation.
     */
    private final BoardViewer boardViewer;

    /**
     * The timer used.
     */
    private final Timer timer;

    /**
     * The delay between two animations.
     */
    private static final int DELAY = 200;

    /**
     * Create an animator for a particular board viewer.
     * @param bv The view to be animated.
     */
    public Animator(BoardViewer bv)
    {
        boardViewer = bv;
        timer = new Timer(DELAY,
                new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                boardViewer.nextAnimation();
            }
        }
        );
    }

    /**
     * Stop triggering animation events.
     */
    public void stop()
    {
        timer.stop();
    }

    /**
     * Start triggering animation events.
     */
    public void start() 
    {
        timer.start();
    }
}
