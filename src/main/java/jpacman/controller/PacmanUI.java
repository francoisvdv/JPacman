package jpacman.controller;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jpacman.model.Engine;

/**
 * Construct the top level GUI.
 *
 * @author Arie van Deursen; Aug 17, 2003
 * @version $Id: PacmanUI.java 4250 2011-01-27 20:51:40Z arievandeursen $
 */
public class PacmanUI extends JFrame implements KeyListener, Observer
{

    /**
     * Universal version ID for serialization.
     */
    static final long serialVersionUID = -59470379321937183L;

    /**
     * The underlying model of the game.
     */
    private Engine engine;

    /**
     * The user interface controllers.
     */
    private Pacman controller;

    /**
     * The user interface display.
     */
    private BoardViewer boardViewer;

    /**
     * The status field for the amount of food eaten so far.
     */
    private JTextField eatenField;

    /**
     * The status field indicating what state the game is in.
     */
    private JTextField statusField;
    
    /**
     * The undo button.
     */
    private JButton undoButton;
    
    /**
     * The actual text that is shown in the status field.
     */
    public enum Status
    {
        STARTING("Press start"),
        PLAYING("Use arrows"),
        WON("You won!"),
        LOST("You lost!"),
        HALTED("Press start");
        
        private String theStatus;
        private Status(String n)
        {
            theStatus = n;
        }
        
        /**
         * @return The UI text for a given state.
         */
        public String statusText()
        {
            return theStatus;
        }
    }

    /**
     * Create a new Pacman top level user interface.
     *
     * @param theEngine Underlying pacman model
     * @param p Top level machine responding to gui requests.
     */
    public PacmanUI(Engine theEngine, Pacman p)
    {
        engine = theEngine;
        this.controller = p;
        boardViewer = new BoardViewer(engine);
        engine.addObserver(this);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                getController().start();
                // ensure the full window has the focus.
                requestFocusInWindow();
            }
        });
        startButton.requestFocusInWindow();

        undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                getController().undo();
                // ensure the full window has the focus.
                requestFocusInWindow();
            }
        });
        
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                getController().quit();
                // ensure the full window has the focus.
                requestFocusInWindow();
            }
        });



        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                getController().exit();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(exitButton);

        JLabel pointsLabel = new JLabel("Points: ");
        final int eatenWidth = 4;
        eatenField = new JTextField("0", eatenWidth);
        eatenField.setEditable(false);
        eatenField.setName("jpacman.points");
        
        final int statusWidth = 12;
        statusField = new JTextField("", statusWidth);
        statusField.setEditable(false);
        statusField.setName("jpacman.status");
        
        JPanel statusPanel = new JPanel();
        statusPanel.add(pointsLabel);
        statusPanel.add(eatenField);
        statusPanel.add(statusField);

        JPanel topDown;
        topDown = new JPanel(new BorderLayout());
        topDown.add(statusPanel, BorderLayout.NORTH);
        topDown.add(boardViewer, BorderLayout.CENTER);
        topDown.add(buttonPanel, BorderLayout.SOUTH);

        Container contentPane = getContentPane();
        contentPane.add(topDown);

        attachListeners();
        update(engine, null);
        
        setName("UIFrame");
        setTitle("JPacman");
    }

    /**
     * Attach the window and key listeners.
     */
    private void attachListeners()
    {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                getController().exit();
            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {
                getController().start();
            }

            @Override
            public void windowIconified(WindowEvent e)
            {
                getController().quit();
            }
        });

        // key listeners only work in window with the focus.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                requestFocusInWindow();
            }
        });
        addKeyListener(this);
    }

    /**
     * @see java.awt.event.KeyListener
     * @param e the key typed.
     */
    public void keyTyped(KeyEvent e)
    {
        // System.out.println("KEY TYPED");
    }

    /**
     * @see java.awt.event.KeyListener
     * @param e the key released.
     */
    public void keyReleased(KeyEvent e)
    {
        // System.out.println("KEY RELEASED");
    }

    
    /**
     * @see java.awt.event.KeyListener
     * @param event the key released.
     */
    public void keyPressed(KeyEvent event)
    {
        int code;

        code = event.getKeyCode();
        
        switch (code)
        {
        case KeyEvent.VK_UP: // or
        case KeyEvent.VK_K:
            getController().up();
            break;
        case KeyEvent.VK_DOWN: // or
        case KeyEvent.VK_J:
            getController().down();
            break;
        case KeyEvent.VK_LEFT: // or
        case KeyEvent.VK_H:
            getController().left();
            break;
        case KeyEvent.VK_RIGHT: // or
        case KeyEvent.VK_L:
            getController().right();
            break;
        case KeyEvent.VK_Q:
            getController().quit();  
            break;
        case KeyEvent.VK_X:
            getController().exit(); 
            break;
        case KeyEvent.VK_S:
            getController().start(); 
            break;
        default:
            // do nothing
        }
    }

    /**
     * Redraw the board and refresh status related information.
     * @see java.util.Observable
     * @param observable the one being watched
     * @param rest remaining info
     */
    public final void update(Observable observable, Object rest)
    {
        boardViewer.repaint();
        updateStatus();
        updateFood();
    }

    /**
     * Update the display of the total amount of food eaten.
     */
    private void updateFood()
    {
        int amount = engine.getFoodEaten();
        eatenField.setText(Integer.toString(amount));
    }

    /**
     * Set the status in the GUI depending on the game's state.
     */
    private void updateStatus()
    {
        String text = null;
        undoButton.setEnabled(false);
        if (engine.inStartingState())
        {
            text = Status.STARTING.statusText();
        }
        if (engine.inPlayingState())
        {
            text = Status.PLAYING.statusText();
        }
        if (engine.inDiedState())
        {
            text = Status.LOST.statusText();
            undoButton.setEnabled(true);
        }
        if (engine.inWonState())
        {
            text = Status.WON.statusText();
            undoButton.setEnabled(true);
        }
        if (engine.inHaltedState())
        {
            text = Status.HALTED.statusText();
            undoButton.setEnabled(true);
        }
        assert text != null : "Illegal state";
        statusField.setText(text);
    }

    /**
     * Actually display the GUI on the screen.
     */
    public void display()
    {
        final int buttonWidth = 70;
        final int buttonHeight = 45;
        final int numberOfButtons = 4;
        int displayWidth = 
            Math.max(boardViewer.windowWidth(), numberOfButtons * buttonWidth);
        setSize(displayWidth,
                boardViewer.windowHeight()
                + 2 * buttonHeight);
        setVisible(true);
        requestFocus();
    }

    /**
     * @return The controller.
     */
    Pacman getController()
    {
        return controller;
    }

    /**
     * @return the viewer for the board.
     */
    public BoardViewer getBoardViewer()
    {
        return boardViewer;
    }
}
