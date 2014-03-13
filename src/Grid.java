import java.io.*;
import java.util.*;

public class Grid
{
	private char[][] letters;
	private Trie trie; //Holds all of the words in the dictionary
	private ArrayList<String> words = new ArrayList<String>(); //Words that exists in the grid
	
	/**
	 * Constructor for a random grid
	 */
	public Grid( int gridSize )
	{
		letters = new char[gridSize][gridSize];
		//Out of 100,000
		int[] letterFrequencies = 
		{
				8167,
				1492,
				2782,
				4253,
				12702,
				2228,
				2015,
				6094,
				6966,
				153,
				772,
				4025,
				2406,
				6749,
				7507,
				1929,
				95,
				5987,
				6327,
				9056,
				2758,
				978,
				2360,
				150,
				1974,
				0074
		};
		int[] newLetterFrequencies = new int[26];
		newLetterFrequencies[0] = letterFrequencies[0];
		for( int i = 1; i < 26; i++ )
		{
			newLetterFrequencies[i] = newLetterFrequencies[i-1] + letterFrequencies[i];
		}
		
		Random rnd = new Random();
		try
		{
			trie = new Trie( "dictionary.txt" );
		}
		catch( Exception e ) { 
		}
		
		for( int i = 0; i < letters.length; i++ )
		{
			for( int j = 0; j < letters[i].length; j++ )
			{
				int n = rnd.nextInt( 100000 ) + 1;
				int index = -1;
				for( int k = 0; k < 26; k++ )
				{
					if( n < newLetterFrequencies[k] )
					{
						index = k;
						break;
					}
				}
				letters[i][j] = (char)( 97 + index );
			}
		}
		
		for( int i = 0; i < letters.length; i++ )
		{
			for( int j = 0; j < letters[i].length; j++ )
			{
				addWords( i, j, "", new boolean[letters.length][letters[i].length] );
			}
		}
	}
	
	/**
	 * Get a character in the grid
	 * @param r Row to look in
	 * @param c Column to look in
	 * @return The character at row 'r' and column 'c'
	 */
	public char charAt( int r, int c )
	{
		return letters[r][c];
	}
	
	/**
	 * Method used to find all of the words in a Boggle grid
	 * @param r Row to start at
	 * @param c Column to start at
	 * @param curr The current word that has been found
	 * @param used Whether or not the letter has been used in curr yet
	 */
	private void addWords( int r, int c, String curr, boolean[][] used )
	{
		curr += letters[r][c];
		//clockwise from up
		int[] dx = { 0, 1, 1, 1, 0, -1, -1, -1 };
		int[] dy = { -1, -1, 0, 1, 1, 1, 0, -1 };
		
		//If a valid word is in the string
		if( curr.length() >= 2 && trie.hasWord( curr ) && !hasWord( curr ) )
			//Add it to the list of words
			words.add( curr );
		
		
		//Make sure you can still make a word from what is there
		if( trie.hasPrefix( curr ) )
			for( int i = 0; i < dx.length; i++ )
			{
				//Array bounds checking
				if( r + dy[i] >= 0 && r + dy[i] < letters.length && c + dx[i] >= 0 && c + dx[i] < letters[0].length )
				{
					if( !used[ r + dy[i] ][ c + dx[i] ] )
					{
						used[r + dy[i] ][ c + dx[i] ] = true;
						addWords( r + dy[i], c + dx[i], curr, used );
						used[r + dy[i] ][ c + dx[i] ] = false;
					}
				}
			}
	}
	
	/**
	 * Find all of the words in the grid
	 * @return An array of all words in the grid
	 */
	public String[] getAllWords()
	{
		String[] w = new String[ words.size() ];
		
		for( int i = 0; i < w.length; i++ )
		{
			w[ i ] = words.get( i );
		}
		
		return w;
	}
	
	/**
	 * Check for the existence of a word in the grid
	 * @param s The word to check for
	 * @return Whether or not he word exists
	 */
	public boolean hasWord( String s )
	{
		for( String str : words )
			if( s.equals( str ) )
				return true;
		
		return false;
	}
	
	/**
	 * String representation of the grid
	 */
	public String toString()
	{
		String s = new String();
		
		for( int i = 0; i < letters.length; i++ )
		{
			for( int j = 0; j < letters[i].length; j++ )
			{
				s += letters[i][j];
			}
			s += '\n';
		}
		
		return s;
	}
}