import java.io.*;
import java.util.*;

/**
 * Trie data structure
 * Used for storing a dictionary and fast retrieval of words
 */
public class Trie
{
	private TrieNode root; //Root node of the trie
	
	/**
	 * Creates the trie from a list of words in a text file
	 * @param textFile The text file to look for words in
	 * @throws FileNotFoundException If textFile is not found
	 */
	public Trie( String textFile ) throws FileNotFoundException
	{
		//Set up the root node (No character there)
		root = new TrieNode( ' ', false );
		Scanner in = new Scanner( new File( textFile ) );
		
		//Add every word in the text file specified
		while( in.hasNext() )
		{
			String s = in.next();
			addWord( s );
		}
	}
	
	/**
	 * Simplifies addWordHelper
	 * @param w Word to add to the trie
	 */
	public void addWord( String w )
	{
		addWordHelper( w, 0, root );
	}
	
	/**
	 * Add a word to the trie
	 * @param word The word to add
	 * @param pos What letter it is on
	 * @param curNode The current node it is on
	 */
	private void addWordHelper( String word, int pos, TrieNode curNode )
	{
		//Check to see if the node has been occupied yet
		if( curNode.getLetters()[ (int)word.charAt(pos) - 97 ] == null )
			//If we are at the last character of the word
			if( pos >= word.length() - 1 )
				//Make sure the isWord property is set to true
				curNode.setLetter( word.charAt( pos ), true );
			else
				curNode.setLetter( word.charAt( pos ), false );
		
		//If it hasn't reached the last letter yet
		if( !( pos >= word.length() - 1 ) )
			//Keep on adding the word
			addWordHelper( word, pos + 1, curNode.getLetters()[ (int)word.charAt( pos ) - 97 ] );
	}
	
	/**
	 * Check to see if a word exists in a trie
	 * @param s Word to check for
	 * @return Whether or not the word exists
	 */
	public boolean hasWord( String s )
	{
		TrieNode cur = root;
		
		for( int i = 0; i < s.length(); i++ )
		{
			int letterIndex = (int)s.charAt(i) - 97;
			
			if( cur.getLetters()[ letterIndex ] != null )
				cur = cur.getLetters()[ letterIndex ];
			else
				return false;
		}
		
		return cur.isEndOfWord();
	}
	
	/**
	 * Check to see if any word exists with a prefix
	 * @param s The prefix to check for
	 * @return Whether or not a word exists with prefix s
	 */
	public boolean hasPrefix( String s )
	{
		TrieNode cur = root;
		
		for( int i = 0; i < s.length(); i++ )
		{
			int letterIndex = (int)s.charAt(i) - 97;
			
			if( cur.getLetters()[ letterIndex ] != null )
				cur = cur.getLetters()[ letterIndex ];
			else
				return false;
		}
		
		return true;
	}
}