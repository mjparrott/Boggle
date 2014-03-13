import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

public class BoggleGUI implements ActionListener
{
    //GUI components
	private JFrame frame;
    private JButton submitWord;
    private JButton restart;
    private JButton pause;
    private JButton exit;
    private JButton newWord;
    private JPanel guiGrid;
    private JButton[][] gridButtons;
    private JTextArea foundWordsArea;
    private JTextArea guessWordArea;
    private JLabel title;
    private JLabel scoreLabel;
    private JLabel messagesLabel;
    private JLabel timerLabel;

    //Game components
    private final int BOARD_SIZE = 4; //Width and height of the grid
    private Grid g; //Instance of grid for the game
    private ArrayList<String> foundWords = new ArrayList<String>(); //Words that the player has found
    private int score = 0; //The score the player has
    private String guess = ""; //The player's current guess
    private int lastRow = -1; //The row of the last letter the player used
    private int lastColumn = -1; //The column of the last letter the player used
    private boolean[][] used; //What letters the player has used so far
    private GameTimer t; //Timer to control the game
    private int foundWordsAmount = 0; //How many words the player has found
    private boolean paused = false; //If the game is paused
    
    /**
     * Default constructor
     */
    public BoggleGUI()
    {
    	
    }
    
    /**
     * Create the GUI and show it.
     */
    public void run()
    {
    	
        //Create and set up the window.
        frame = new JFrame("Boggle");
        //Have the program exit when the user clicks [X]
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create the GUI components
        guiSetup(frame.getContentPane());
        
        //Create the timer for the game
        t = new GameTimer();

        //Display the window.
        frame.setVisible( true );
        frame.setSize( 800, 600 );
        frame.setResizable( false );
        
        //Start the game timer
        t.setTime();
    }
    
    /**
     * Set up all of the components for the GUI
     * @param pane Where to show the GUI on
     */
    public void guiSetup(Container pane)
    {
    	/*
    	 * How it's organized:
    	 * 3 separate columns
    	 * Components in column one are small
    	 * Main grid component in column 2 takes up multiple spaces
    	 * Found word components in column 3 takes up multiple spaces as well
    	 */
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 0.5;
		
		/*
		 * First column GUI components
		 */
		c.weightx = 0.25;
		c.gridx = 0;
		
		Font f = new Font( "SansSerif", Font.ITALIC, 34 );
		title = new JLabel( "Boggle" );
		title.setFont( f );
		c.gridy = 0;
		pane.add(title, c);
		
		scoreLabel = new JLabel( "Score: 0" );
		f = new Font( "Serif", Font.PLAIN, 24 );
		scoreLabel.setFont( f );
		c.gridy = 1;
		pane.add(scoreLabel, c);
	
		timerLabel = new JLabel( "Timer: 3:00" );
		timerLabel.setFont( f );
		c.gridy = 2;
		pane.add(timerLabel, c);
	
		messagesLabel = new JLabel( "Enter word: " );
		f = new Font( "Serif", Font.PLAIN, 20 );
		messagesLabel.setFont( f );
		f = new Font( "Serif", Font.PLAIN, 24 );
		c.gridy = 3;
		c.fill = GridBagConstraints.VERTICAL;
		pane.add(messagesLabel, c);
		c.fill = GridBagConstraints.NONE;
		
		restart = new JButton( "Restart" );
		restart.addActionListener( this );
		
		c.gridy = 4;
		pane.add(restart, c);
		
		pause = new JButton( "Pause" );
		pause.addActionListener( this );
		
		c.gridy = 5;
		pane.add(pause, c);
		
		exit = new JButton( "Exit" );
		exit.addActionListener( this );
		
		c.gridy = 6;
		pane.add(exit, c);
		
		/*
		 * Second column
		 */
		c.gridx = 1;
		c.weightx = 0.75;
		
		guiGrid = new JPanel( new GridLayout( BOARD_SIZE, BOARD_SIZE ) );
		gridButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
		g = new Grid( BOARD_SIZE );
		
		for( int i = 0; i < BOARD_SIZE; i++ )
		{
			for( int j = 0; j < BOARD_SIZE; j++ )
			{
				gridButtons[i][j] = new JButton( Character.toString( g.charAt( i, j ) ) );
				gridButtons[i][j].addActionListener( this );
				gridButtons[i][j].setFont( f );
				guiGrid.add( gridButtons[i][j] );
			}
		}
		
		c.gridx = 1;
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 1;
		pane.add(guiGrid, c);
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		
		newWord = new JButton( "New word" );
		newWord.addActionListener( this );
		c.gridy = 5;
		pane.add( newWord, c );
		
		guessWordArea = new JTextArea( 1, 1 );
		guessWordArea.setEditable( false );
		guessWordArea.setBorder(BorderFactory.createEtchedBorder());
		
		c.gridy = 6;
		c.fill = GridBagConstraints.BOTH;
		pane.add(guessWordArea, c);
		c.fill = GridBagConstraints.NONE;
		
		submitWord = new JButton( "Submit word" );
		submitWord.addActionListener( this );
		
		c.gridx = 1;
		c.gridy = 7;
		pane.add( submitWord, c );
		
		/*
		 * Third column
		 */
		c.gridx = 2;
		c.weightx = 0.25;
		
		JLabel foundTitle = new JLabel( "Found words:" );
		foundTitle.setFont( f );
		c.gridy = 1;
		pane.add( foundTitle, c);
		
		foundWordsArea = new JTextArea( 10, 16 );
		foundWordsArea.setEditable( false );
		c.gridx = 2;
		c.gridy = 2;
		c.gridheight = 2;
		Insets in = new Insets( 25, 25, 25, 25 );
		foundWordsArea.setMargin( in );
		foundWordsArea.setBorder(BorderFactory.createEtchedBorder()); 
		pane.add( foundWordsArea, c);
    }
    
    /**
     * What to do when the user interacted with a GUI component
     * @param e The ActionEvent that occurred
     */
    public void actionPerformed( ActionEvent e )
    {
    	Object source = e.getSource();
    	
    	if( (JButton)source == exit )
    	{
    		//Exit the application
    		System.exit( 0 );
    	}
    	else if( (JButton)source == restart )
    	{
    		String message = "Are you sure you want to restart?";
    		
        	int n = JOptionPane.showConfirmDialog(
        		    frame,
        		    message,
        		    "Restart game",
        		    JOptionPane.YES_NO_OPTION );
        	if( n == JOptionPane.YES_OPTION )
        		//Restart the game
        		restart();
    	}
    	else if( (JButton)source == pause )
    	{
    		//Change the game start
    		paused = !paused;
    		
    		//If the game is paused now
    		if( paused )
    		{
    			//Stop the timer
    			t.pause();
    			
    			//Don't let the player see the grid
    			guiGrid.setVisible( false );
    			for( int r = 0; r < BOARD_SIZE; r++ )
    			{
    				for( int c = 0; c < BOARD_SIZE; c++ )
    				{
    					gridButtons[r][c].setText( "" );
    				}
    			}
    			pause.setText( "Unpause" );
    		}
    		else
    		{
    			t.unpause();
    			guiGrid.setVisible( true );
    			for( int r = 0; r < BOARD_SIZE; r++ )
    			{
    				for( int c = 0; c < BOARD_SIZE; c++ )
    				{
    					gridButtons[r][c].setText( Character.toString( g.charAt( r, c ) ) );
    				}
    			}
    			pause.setText( "Pause" );
    		}
    	}
    	else if( (JButton)source == submitWord )
    	{
    		if( guess.equals( "" ) )
    		{
    			messagesLabel.setText( "Enter a word!" );
    		}
    		else if( exists( guess ) )
    		{
    			messagesLabel.setText( "You've used " + guess + "!" );
    		}
    		else
    		{
    			if( guess.length() < 3 )
    			{
    				messagesLabel.setText( "Must be >= 3 letters." );
    			}
    			else
    			{
		    		if( g.hasWord( guess ) )
		    		{
		    			//Add to the score
		    			if( guess.length() <= 3 )
		    				score += 1;
		    			else if( guess.length() == 4 )
		    				score += 2;
		    			else if( guess.length() == 5 )
		    				score += 3;
		    			else if( guess.length() == 6 )
		    				score += 5;
		    			else
		    				score += 8;
		    			scoreLabel.setText( "Score: " + Integer.toString( score ) );
		    			
		    			messagesLabel.setText( guess + " is a word!" );
		    			foundWordsArea.insert( guess + "\n", 0 );
		    			foundWords.add( guess );
		    			foundWordsAmount++;
		    		}
		    		else
		    		{
		    			messagesLabel.setText( guess + " is not a word!" );
		    		}
    			}
    		}
    		
    		//Always set the word to blank when the press submit word
    		removeWord();
    		
    	}
    	else if( (JButton)source == newWord )
    	{
    		removeWord();
    	}
    	
    	//Check all of the grid buttons and see if they were clicked
    	for( int i = 0; i < BOARD_SIZE; i++ )
    	{
    		for( int j = 0; j < BOARD_SIZE; j++ )
    		{
    			if( (JButton)source == gridButtons[i][j] )
    			{
    				if( ( ( lastRow != -1 && lastColumn != -1 ) && ( Math.abs( lastRow - i ) <= 1 && Math.abs( lastColumn - j ) <= 1 && !used[i][j] ) )
    						|| ( lastRow == -1 && lastColumn == -1 ) )
    				{
	    					guessWordArea.insert( Character.toString( g.charAt( i, j ) ), guess.length() );
	    					guess += g.charAt( i, j );
	    					lastRow = i;
	        				lastColumn = j;
	        				used[i][j] = true;
	        				gridButtons[i][j].setBackground( new Color( 122, 122, 122 ) );
    				}
    				else
						JOptionPane.showMessageDialog( null, "Invalid move" );
    				
    				//There is only one button that could have been clicked
    				break;
    			}
    		}
    	}
    }
    
    /**
     * Change the player's currently guessed word to nothing
     */
    public void removeWord()
    {
    	lastRow = -1;
    	lastColumn = -1;
    	
    	guess = "";
    	guessWordArea.setText( "" );
    	
    	//Reset the letters that have been used for a word
    	used = new boolean[BOARD_SIZE][BOARD_SIZE];
    	//Change the background colour of all of the buttons in the grid
    	for( int i = 0; i < BOARD_SIZE; i++ )
    	{
    		for( int j = 0; j < BOARD_SIZE; j++ )
    		{
    			gridButtons[i][j].setBackground( restart.getBackground() ); //The restart button will always have the default background
    		}
    	}
    }
    
    /**
     * Code for starting up another game
     */
    public void restart()
    {
    	foundWords.clear();
    	foundWordsArea.setText( "" );
    	foundWordsAmount = 0;
    	
    	score = 0;
    	scoreLabel.setText( "Score: 0" );
    	
    	messagesLabel.setText( "Enter word: " );
    	
    	removeWord();
    	
    	String message = String.format( "Do you want a new board?" );
    	int n = JOptionPane.showConfirmDialog(
    		    frame,
    		    message,
    		    "New board?",
    		    JOptionPane.YES_NO_OPTION );
    	if( n == JOptionPane.YES_OPTION )
    	{
    		g = new Grid( BOARD_SIZE );
    		
    		for( int i = 0; i < BOARD_SIZE; i++ )
    		{
    			for( int j = 0; j < BOARD_SIZE; j++ )
    			{
    				gridButtons[i][j].setText( Character.toString( g.charAt( i, j ) ) );
    			}
    		}
    	}
    	
    	t.reset();
    }
    
    /**
     * Check to see if the user entered a word twice
     * @param s Word to check for
     * @return If the word already exists
     */
    public boolean exists( String s )
    {
    	for( String str : foundWords )
    		if( s.equals( str ) )
    			return true;
    	
    	return false;
    }
    
    /**
     * What happens when the player runs out of time
     */
    public void gameOver()
    {
    	String message = String.format( "Game over!\nYou scored %d.\nYou left %d words on the board.\nPlay again?", 
    			score, g.getAllWords().length - foundWordsAmount );
    	int n = JOptionPane.showConfirmDialog(
    		    frame,
    		    message,
    		    "Game over!",
    		    JOptionPane.YES_NO_OPTION );
    	if( n == JOptionPane.YES_OPTION )
    		restart();
    	else
    		System.exit( 0 );
    }
    
    /**
     * Timer to handle when the game ends and starts
     */
    public class GameTimer extends Timer
    {
    	Task t; // The task to run
    	private long startTime; //What time the game started
    	private long pauseStart;
    	
    	/**
    	 * Sets up the timer for moving flights
    	 */
    	public GameTimer()
    	{
    		super();
    		t = new Task();
    		scheduleAtFixedRate( t, 0, 1000 );
    		startTime = System.currentTimeMillis();
    	}
    	
    	/**
    	 * Restart the timer
    	 */
    	public void reset()
    	{
    		startTime = System.currentTimeMillis();
    	}
    	
    	/**
    	 * Update the timer label
    	 */
    	public void setTime()
    	{
    		if( !paused )
    		{
	    		long playingTime = System.currentTimeMillis() - startTime;
	    		long timeLeft = 180000 - playingTime;
	    		long min = timeLeft / 60000;
	    		long sec = ( timeLeft - ( min * 60000 ) ) / 1000L;
	    		
	    		if( timeLeft < 0 )
	    		{
	    			//Game over!
	    			gameOver();
	    			return;
	    		}
	    		
	    		String s = "Time: ";
	    		s += Integer.toString( (int)min ) + ":";
	    		if( sec >= 10 )
	    			s += Integer.toString( (int)sec );
	    		else
	    			s += "0" + Integer.toString( (int)sec );
	    		
	    		timerLabel.setText( s );
    		}
    	}
    	
    	/**
    	 * 
    	 */
    	public void pause()
    	{
    		pauseStart = System.currentTimeMillis();
    	}
    	
    	/**
    	 * 
    	 */
    	public void unpause()
    	{
    		startTime += System.currentTimeMillis() - pauseStart;
    	}
    	
    	/**
    	 * The task to run
    	 */
    	public class Task extends TimerTask
    	{
    		/**
    		 * What to do every interval
    		 */
    		public void run()
    		{
    			//Update the timer label
    			setTime();
    		}
    	}
    }
}
