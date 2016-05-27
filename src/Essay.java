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
	public Essay(String text){
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
			while(k < essay.length() - 1 && essay.charAt(k) != '.') {
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
		ListNode2 node = sentencesHead.getNext();
		while(node != sentencesHead) {
			addSentenceWords((String)node.getValue());
			node = node.getNext();
		}
	}
	
	//adds words from a single sentence to the tree of words
	public void addSentenceWords(String str){
		//TODO ...
		String punctuation = ".,\"'()[]{};:?!-/\\"; //TODO all punctuation?
		String temp = "";
		int i = 0;
		while(i < str.length()) {
			int k = i;
			System.out.println(str.charAt(k));
			if(punctuation.indexOf(str.charAt(k)) >= 0){ //character is punctuation
				temp += str.charAt(k);
				System.out.println("here");
				k++;
			}
			else{
				while(k < str.length() && str.charAt(k) != ' ' && punctuation.indexOf(str.charAt(k)) < 0) {
					temp += str.charAt(k);
					k++;
				}
			}
	//		System.out.println("************************" + temp + " " + str + " " + i);
			wordsTree.put(temp, new WordLoc(str, i, temp)); //add method of the tree
			/*
			ListNode2 headd = wordsTree.get(temp);
	    	  ListNode2 nodee = headd;
	    	  do{
	    		  System.out.println(((WordLoc)nodee.getValue()).toString());
	    		  System.out.println("Prev: " + nodee.getPrevious().getValue());
	    		  System.out.println("Next: " + nodee.getNext().getValue());
	    		  System.out.println("------------------------------------------");
	    		  nodee = nodee.getNext();
	    	  }while(nodee != headd);
	    	  */
			System.out.println("added \"" + temp + "\"");
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
			//insert sentence into sentences linked list
			//TODO error is person deletes entire sentence?
			//wloc.getSentenceNode().setValue(newStr);
			//TODO is the above line needed??
			
			/*
			System.out.println("BEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFORE");
			ListNode2 headd = wordsTree.get("me");
	    	  ListNode2 nodee = headd;
	    	  do{
	    		  System.out.println(((WordLoc)nodee.getValue()).toString());
	    		  System.out.println("Prev: " + nodee.getPrevious().getValue());
	    		  System.out.println("Next: " + nodee.getNext().getValue());
	    		  System.out.println("------------------------------------------");
	    		  nodee = nodee.getNext();
	    	  }while(nodee != headd);
			*/
			//fix words tree map
			disconnect(sentence); //from words TreeMap
			addSentenceWords(newStr);
			
			//removes this error from list to be displayed
			node.setValue(null);
			/*
			System.out.println("AFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFERT");
			headd = wordsTree.get("me");
	    	 nodee = headd;
	    	  do{
	    		  System.out.println(((WordLoc)nodee.getValue()).toString());
	    		  System.out.println("Prev: " + nodee.getPrevious().getValue());
	    		  System.out.println("Next: " + nodee.getNext().getValue());
	    		  System.out.println("------------------------------------------");
	    		  nodee = nodee.getNext();
	    	  }while(nodee != headd);
	    	  */
		}
		
	}
	
	//updates any changes in a sentence in the word tree
	public void disconnect(String str){
		String punctuation = ".,\"'()[]{};:?!-/\\"; //TODO all punctuation?
		String temp = "";
		int i = 0;
		ListNode2 head;
		ListNode2 node;
		while(i < str.length()) {
			int k = i;
			System.out.println(str.charAt(k));
			if(punctuation.indexOf(str.charAt(k)) >= 0){ //character is punctuation
				temp += str.charAt(k);
				System.out.println("here");
				k++;
			}
			else{
				while(k < str.length() && str.charAt(k) != ' ' && punctuation.indexOf(str.charAt(k)) < 0) {
					temp += str.charAt(k);
					k++;
				}
			}
			
			temp = temp.toLowerCase();
			System.out.println(temp);
			
			/*
			if(temp.equals("it")){
				System.out.println("BEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFORE");
				ListNode2 headd = wordsTree.get("it");
		    	  ListNode2 nodee = headd;
		    	  do{
		    		  System.out.println(((WordLoc)nodee.getValue()).toString());
		    		  System.out.println("Prev: " + nodee.getPrevious().getValue());
		    		  System.out.println("Next: " + nodee.getNext().getValue());
		    		  System.out.println("------------------------------------------");
		    		  nodee = nodee.getNext();
		    	  }while(nodee != headd);
				}
		*/
			
			System.out.println("Looking for " + temp);
			//remove node from given word
			head = wordsTree.get(temp);
			node = head;
			do{
				System.out.println("node string " + ((WordLoc)node.getValue()).getSentenceString());
				System.out.println("String string: " + str);
				if(((WordLoc)node.getValue()).getSentenceString() == str){
					System.out.println("disconnecting \"" + temp + "\"");
					if(node == head){ //don't delete head unless its the only node left
						if(node.getNext() == node){ //only node left 
							wordsTree.remove(temp);
							System.out.println("..................DID IT A");
						}else{ //make second node new head
							head.setValue(head.getNext().getValue());
							ListNode2 tempNode = head.getNext();
							head.setNext(tempNode.getNext());
							tempNode.getNext().setPrevious(head);
							tempNode.setNext(null);
							tempNode.setPrevious(null);
							System.out.println("..................DID IT B");
						}
					}else {//not head
						node.getPrevious().setNext(node.getNext());
						node.getNext().setPrevious(node.getPrevious());
						node.setNext(null);
						node.setPrevious(null);
						System.out.println("..................DID IT C");
					}
					break;
				}
				node = node.getNext();
			}while(node != head);
			
			/*
			if(temp.equals("it")){
			System.out.println("AFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFERT");
			ListNode2 headd = wordsTree.get("it");
	    	  ListNode2 nodee = headd;
	    	  do{
	    		  System.out.println(((WordLoc)nodee.getValue()).toString());
	    		  System.out.println("Prev: " + nodee.getPrevious().getValue());
	    		  System.out.println("Next: " + nodee.getNext().getValue());
	    		  System.out.println("------------------------------------------");
	    		  nodee = nodee.getNext();
	    	  }while(nodee != headd);
			}
			*/
	    	  
			
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
		
	//	System.out.println(head.getValue()+ "***************************************************************************************************");
		/*
		System.out.println("BEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFOREBEFORE");
		ListNode2 headd = wordsTree.get(wloc.getWord());
    	  ListNode2 nodee = headd;
    	  do{
    		  System.out.println(((WordLoc)nodee.getValue()).toString());
    		  System.out.println("Prev: " + nodee.getPrevious().getValue());
    		  System.out.println("Next: " + nodee.getNext().getValue());
    		  System.out.println("------------------------------------------");
    		  nodee = nodee.getNext();
    	  }while(nodee != headd);
		*/
		do{
		//	System.out.println("node string " + ((WordLoc)node.getValue()).getSentenceString());
		//	System.out.println("String string: " + str);
			if(((WordLoc)next.getValue()) == wloc){
				System.out.println("HERERE WE ARE IN THIS METHOS");
	//		System.out.println("disconnecting \"" + temp + "\"");
				if(next == head){ //don't delete head unless its the only node left
					if(next.getNext() == next){ //only node left 
						wordsTree.remove(wloc.getWord());
						System.out.println("..................DID IT A");
					}else{ //make second node new head
						head.setValue(head.getNext().getValue());
						ListNode2 tempNode = head.getNext();
						head.setNext(tempNode.getNext());
						tempNode.getNext().setPrevious(head);
						tempNode.setNext(null);
						tempNode.setPrevious(null);
						System.out.println("..................DID IT B");
					}
				}else {//not head
					next.getPrevious().setNext(next.getNext());
					next.getNext().setPrevious(next.getPrevious());
					next.setNext(null);
					next.setPrevious(null);
					System.out.println("****************** info of removed node: " + next.getValue());
					System.out.println("..................DID IT C");
				}
				/*
				System.out.println("AFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFTERAFERT");
				headd = wordsTree.get("i");
		    	 nodee = headd;
		    	  do{
		    		  System.out.println(((WordLoc)nodee.getValue()).toString());
		    		  System.out.println("Prev: " + nodee.getPrevious().getValue());
		    		  System.out.println("Next: " + nodee.getNext().getValue());
		    		  System.out.println("------------------------------------------");
		    		  nodee = nodee.getNext();
		    	  }while(nodee != headd);
				*/
				return;
			}
			next = next.getNext();
		}while(next != head);
		
		//find wloc and remove
		/*node = head;
		do{
			if(node.getValue() == wloc){
				//remove node
				if(node.getNext() == node){ //only node
					wordsTree.remove(wloc.getWord());
				}else { //disconnect node from list
						node.getPrevious().setNext(node.getNext());
					if(node.getNext() != null)
						node.getNext().setPrevious(node.getPrevious());
					node.setNext(null);
					node.setPrevious(null);
				}
				return;
			}
		}while(node != head);*/
		//TODO idk why it would get here
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
