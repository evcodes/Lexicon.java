import structure5.*;
import java.util.Iterator;
import java.util.Scanner;

public class LexiconTrie implements Lexicon {
    
    protected int count = 0;
    static LexiconNode root = new LexiconNode(' ',false);
    
    public boolean addWord(String word) {
	//pre: give a word to be added into the Lexicon
	//post: return true if the word is new, or false if existing
	String lowerCaseWord = word.toLowerCase();
	boolean result = false;
	LexiconNode node = root;
	
	for(int i = 0; i<lowerCaseWord.length();++i){
	    //gives us access to the child node
	    char letter = lowerCaseWord.charAt(i);
	    if(node.getChild(letter) == null){  
		if(i < lowerCaseWord.length()-1){
		    node.addChild(new LexiconNode(letter,false));
		}
		else{
		    node.addChild(new LexiconNode(letter,true));
		}
		node = node.getChild(letter);
		result = true;
	    }
	    else{
		node = node.getChild(letter);
	    }
	}
	if(result) count++;
	return result;
    }
    
    //pre: valid filename
    //post: scan each line and add the word contained
    public int addWordsFromFile(String filename) {
	Scanner scan = new Scanner(filename);
	while(scan.hasNextLine()){
	    addWord(scan.nextLine());
		}
	return 0;
    }
    
    // post: removes word if present and returns true or false it it was/wasn't found
    public boolean removeWord(String word) {
	return removeWordHelper(word.toLowerCase(), root);
    }

    private boolean removeWordHelper(String word, LexiconNode current){
	if(word.isEmpty() && current.isWord){
	    current.isWord = false;
	    count--;
	    return true;
	}
	LexiconNode nextNode = current.getChild(word.charAt(0));
	if(nextNode == null){
	    return false;
	}
	else{
	    if(removeWordHelper(word.substring(1), nextNode)){
		// only delete a node if its only child is a dead end now
		if(nextNode.children.size()==1){
		    current.getChildren().remove();
		}
	    }
	}
	return true;
    }
    
    //post: return number of words in LexiconTrie
    public int numWords() { return count; }


    //post: returns true if word is found and false if it isn't
    public boolean containsWord(String word) {
	return containsWordHelper(word.toLowerCase(), root);
    }
    
    private boolean containsWordHelper(String word, LexiconNode current){
	if(word.isEmpty()) return current.isWord;
	
	char nextChar = word.charAt(0);
	if(current.getChild(nextChar)==null) return false;
	
	else{
	    return containsWordHelper(word.substring(1),
				      current.getChild(nextChar));
	}
    }

    //post: returns true if prefix is found and false if it isn't
    public boolean containsPrefix(String prefix){
	LexiconNode current = root;
	boolean prefixFound = true;
	for(int i = 0; i < prefix.length(); i++){
	    // update current node as long as the next letter is a child of the node
	    if(current.getChild(prefix.charAt(i))==null){
		prefixFound = false;
		break;
	    }
	    else{
		current = current.getChild(prefix.charAt(i));
	    }
	}
	return prefixFound;
    }
    
    public Iterator<String> iterator() {
       
	return IteratorHelper(root, count, "", new Vector<String>()).iterator();
    }

    private Vector<String> IteratorHelper(LexiconNode current, int numLeft,
					  String word, Vector<String> words){
	if(numLeft==0) return new Vector();
	else if(current.getChildren().isEmpty()){
	    numLeft = numLeft-1;
	    words.add(word+current.letter());
	    return words;
	}
	
	for(int i = 0; i< current.getChildren().size(); i++){
	    words.addAll(IteratorHelper((LexiconNode)current.getChildren().get(i),
					numLeft, word, words));
	}
	return words;
    }
    
    public Set<String> suggestCorrections(String target, int maxDistance) {
	return null;
    }

    public Set<String> matchRegex(String pattern){
	return null;
    }
    
    public static void main(String args[]){
	
	LexiconTrie n = new LexiconTrie();
	
	boolean baseWord = n.addWord(args[0]);
	boolean checkWordSame = n.addWord(args[1]);
	boolean checkWordDifferent = n.addWord(args[2]);
	n.addWord(args[3]);
	n.addWord(args[4]);
	n.addWord(args[5]);
	
	System.out.println("<added> " + args[0] + " to LexiconTrie and got " + baseWord);
	
	System.out.println("<added> " + args[1]+ " to LexiconTrie and expected false. Actual: " + checkWordSame);
	
	System.out.println("<added> " + args[2]+ " to LexiconTrie and expected true. Actual: " + checkWordDifferent);
	
	for(int i =0 ; i < root.getChildren().size(); i++){
	    LexiconNode l = (LexiconNode)root.getChildren().get(i);
	    System.out.println(i + ": " + l.letter());
	    
	}

	System.out.println("Number of words: " + n.numWords());

	n.removeWord(args[1]);

	System.out.println("Number of words now: " + n.numWords());
    }
}