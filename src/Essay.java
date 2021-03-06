/** 
 * Represents an essay and holds data structures of sentences and words
 * 
 * Written By: Kelly Finke, Kiara Wahnschafft, Shannon Wing
 * Date: 5/31/16
 */

public class Essay {
	//blank header node for sentences linked list
	private ListNode2 sentencesHead;
	private TreeMap wordsTree = new TreeMap();
	
	// Constructs an essay
	public Essay(String text) {
		parse(text);
		addAllWords();
	}
	
	/*
	 * place all sentences of essay into a linked list of sentences (strings)
	 */
	public void parse(String essay) {
		sentencesHead = new ListNode2(null);
		sentencesHead.setNext(sentencesHead);
		sentencesHead.setPrevious(sentencesHead);
		String temp = "";
		ListNode2 tempNode;
		int i = 0;
		while(i < essay.length()) {
			int k = i;
			
			while(k < essay.length() - 1 && essay.charAt(k) != '.') 
			{
				temp += essay.charAt(k);
				k++;
			}
			temp += essay.charAt(k);
			//add node to sentence list
			tempNode = new ListNode2(temp.trim());
			tempNode.setNext(sentencesHead);
			tempNode.setPrevious(sentencesHead.getPrevious());
			sentencesHead.getPrevious().setNext(tempNode);
			sentencesHead.setPrevious(tempNode);
			i = k+1;
			temp = "";
		}
		//make un circular
		sentencesHead.getPrevious().setNext(null);
		sentencesHead = sentencesHead.getNext();
		if(sentencesHead != null){
			sentencesHead.setPrevious(null);
		}

	}

	/*
	 * adds all the words in all the sentences to the tree of words
	 */
	public void addAllWords() {
		ListNode2 node = sentencesHead;
		while(node != null) {
			addSentenceWords(node);
			node = node.getNext();
		}
	}
	
	//adds words from a single sentence to the tree of words
	public void addSentenceWords(ListNode2 sen){
		String str = (String)sen.getValue();
		//TODO ...
		String punctuation = ".,\"\'()���[]{}';:?!-/\\"; 
		String temp = "";
		int i = 0;
		while(i < str.length()) {
			int k = i;

			if(punctuation.indexOf(str.charAt(k)) >= 0){ //character is punctuation
				temp += str.charAt(k);
				k++;
			}
			else{
				while(k < str.length() && str.charAt(k) != ' ' && punctuation.indexOf(str.charAt(k)) < 0) {
					temp += str.charAt(k);
					k++;
				}
			}
			wordsTree.put(temp, new WordLoc(sen, i, temp)); //add method of the tree

			if(k < str.length() && str.charAt(k) == ' ')
				k++;
			i = k;
			temp = "";
		}
	}
	
	//removes a wordloc node from a word and re-inserts the sentence
	public void disconnectAndAdd(ListNode2 node, String newStr){
		WordLoc wloc = (WordLoc)node.getValue();
		String sentence = (String)wloc.getSentenceNode().getValue();
		if(!sentence.equals(newStr)){ //Not the exact same
			
			//fix words tree map
			disconnect(sentence); //from words TreeMap
			
			//add words of new sentence to wordsTree
			wloc.getSentenceNode().setValue(newStr);
			addSentenceWords(wloc.getSentenceNode());
			node.setValue(null);
		}
		
	}
	
	//updates any changes in a sentence in the word tree
	public void disconnect(String str){
		String punctuation = ".,\"\'()���[]{}';:?!-/\\"; 
		String temp = "";
		int i = 0;
		ListNode2 head;
		ListNode2 node;
		while(i < str.length()) {
			int k = i;
			if(punctuation.indexOf(str.charAt(k)) >= 0){ //character is punctuation
				temp += str.charAt(k);
				k++;
			}
			else{
				while(k < str.length() && str.charAt(k) != ' ' && punctuation.indexOf(str.charAt(k)) < 0) {
					temp += str.charAt(k);
					k++;
				}
			}
			
			temp = temp.toLowerCase();
			
			//remove node from given word
			head = wordsTree.get(temp);
			node = head;
			do{

				if(((WordLoc)node.getValue()).getSentenceString() == str){
					if(node == head){ //don't delete head unless its the only node left
						if(node.getNext() == node)
						{ //only node left 
							wordsTree.remove(temp);
						}
						else
						{ //make second node new head
							head.setValue(head.getNext().getValue());
							ListNode2 tempNode = head.getNext();
							head.setNext(tempNode.getNext());
							tempNode.getNext().setPrevious(head);
							tempNode.setNext(null);
							tempNode.setPrevious(null);
						}
					}
					else {//not head
						node.getPrevious().setNext(node.getNext());
						node.getNext().setPrevious(node.getPrevious());
						node.setNext(null);
						node.setPrevious(null);

					}
					break;
				}
				node = node.getNext();
			}while(node != head);
			
			if(k < str.length() && str.charAt(k) == ' ')
				k++;
			i = k;
			temp = "";
		}
	}
	
	//removes corrected word from wordsTree
	public void removeCorrected(ListNode2 node){

		WordLoc wloc = (WordLoc)node.getValue();
		ListNode2 head = wordsTree.get(wloc.getWord());
		ListNode2 next = head;
		
		//removes this error from list to be displayed
		node.setValue(null);
		
		do{

			if(((WordLoc)next.getValue()) == wloc){
				if(next == head){ //don't delete head unless its the only node left
					if(next.getNext() == next){ //only node left 
						wordsTree.remove(wloc.getWord());
	
					}else{ //make second node new head
						head.setValue(head.getNext().getValue());
						ListNode2 tempNode = head.getNext();
						head.setNext(tempNode.getNext());
						tempNode.getNext().setPrevious(head);
						tempNode.setNext(null);
						tempNode.setPrevious(null);
					}
				}else {//not head
					next.getPrevious().setNext(next.getNext());
					next.getNext().setPrevious(next.getPrevious());
					next.setNext(null);
					next.setPrevious(null);
				}

				return;
			}
			next = next.getNext();
		}while(next != head);
		
	}
	
	
	//returns the wordsTree
	public TreeMap getTree(){
		return wordsTree;
	}
	
	//returns header node of sentences
	public ListNode2 getSentences(){
		return sentencesHead;
	}
}
