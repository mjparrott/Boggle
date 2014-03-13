public class TrieNode
{
	private TrieNode[] letters = new TrieNode[26]; //The list of letters below this letter
	private char c; //The character at this node
	private boolean endOfWord = false; //Whether or not this is the end of a word
	
	/**
	 * Constructor for TrieNode
	 * @param ch Character to set the node to
	 * @param end Whether or not this is the end of a word
	 */
	public TrieNode( char ch, boolean end )
	{
		c = ch;
		endOfWord = end;
	}
	
	/**
	 * Create a new node at character c
	 * @param c Character to add
	 * @param end Whether or not it's the end of a word
	 */
	public void setLetter( char c, boolean end )
	{
		letters[ (int)c - 97 ] = new TrieNode( c, end );
	}
	
	/**
	 * Get the nodes this one is pointing to
	 * @return Array of nodes
	 */
	public TrieNode[] getLetters()
	{
		return letters;
	}

	/**
	 * Check to see if this is the end of a word
	 * @return Whether or not this is the end of a word
	 */
	public boolean isEndOfWord()
	{
		return endOfWord;
	}
}