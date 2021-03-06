/**
 * AutoHirsch.java
 * 
 * Represents the main content panel with all the buttons set with
 * their respective action listeners
 * 
 * Authors: Kiara Wahnschafft, Shannon Wing, Kelly Finke
 * Date: 5/31/16
 */

//import statements
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

public class AutoHirsch extends JFrame {

  private JTextArea essay; //essay panel
  private JTextArea sentence; //error panel
  private JLabel rule; //rule panel
  private JTextArea copy; //insert text panel
  private String copied; //text in insert text panel
  private TreeMap tree; //word tree
  private ListNode2 current; //currently-displayed node
  //the four buttons
  private JButton next;
  private JButton previous;
  private JButton change;
  private JButton correct;
  //boolean of whether or not text in error panel is edited
  private boolean newInsert;
  private boolean newRemove;
  private JFrame frame;
  private MenuBar thisMenu;
  Essay essayEssay;
  private String currentError = "";
  private final String blueColor = "#B8DFEF";
  
  /*
   * constructor that instantiates the gui, creating the whole JPanel 
   */
  public AutoHirsch() throws Exception {

	thisMenu = new MenuBar(this, new EssayAction());
    setJMenuBar(thisMenu);

    //create panel that displays the input text being checked
    Font font = new Font("Monospaced", Font.PLAIN, 12);
    essay = new JTextArea(30, 50);
    essay.setEditable(false);
    essay.setFont(font);
    essay.setLineWrap(true);
    essay.setWrapStyleWord(true);
    JScrollPane areaScrollPaneIn = new JScrollPane(essay);
    areaScrollPaneIn.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
    //create panel with the blue sheet radio buttons
    JPanel p1 = new JPanel();
    p1.setPreferredSize(new Dimension(400, 81));
    p1.setLayout(new GridLayout(5, 1));
    p1.setBorder(new LineBorder(Color.BLACK));
    ButtonGroup bluesheets = new ButtonGroup();
    JRadioButton one = new JRadioButton("I. Past Tense");
    JRadioButton three = new JRadioButton("III. First or Second Person");
    JRadioButton four = new JRadioButton("IV. Vague 'this' or 'which'");
    JRadioButton six = new JRadioButton("VI. Pronoun Case");
    JRadioButton eight = new JRadioButton("VIII. Apostrophe Problem");
    JRadioButton nine = new JRadioButton("IX. Avoid passive voice.");
    JRadioButton twelve = new JRadioButton("XII. Progressive Tense");
    JRadioButton thirteen = new JRadioButton("XIII. Problem in Quotation Form");
    
    //add the blue sheet radio buttons to one button group 
    //(so that only one button is selected at one)
    bluesheets.add(one);
    bluesheets.add(three);
    bluesheets.add(four);
    bluesheets.add(six);
    bluesheets.add(eight);
    bluesheets.add(nine);
    bluesheets.add(twelve);
    bluesheets.add(thirteen);
   
    //add action listeners to the blue sheet buttons
    one.addActionListener(new CustomActionListenerOne());
    three.addActionListener(new CustomActionListenerThree());
    four.addActionListener(new CustomActionListenerFour());
    six.addActionListener(new CustomActionListenerSix());
    eight.addActionListener(new CustomActionListenerEight());
    nine.addActionListener(new CustomActionListenerNine());
    twelve.addActionListener(new CustomActionListenerTwelve());
    thirteen.addActionListener(new CustomActionListenerThirteen());
    
    //add the blue sheet buttons to the panel
    p1.add(one);
    p1.add(three);
    p1.add(four);
    p1.add(six);
    p1.add(eight);
    p1.add(nine);
    p1.add(twelve);
    p1.add(thirteen);
    p1.setBackground(Color.decode(blueColor));
    
    //create panel that holds the current sentence being changed or deemed correct
    sentence = new JTextArea(5, 20);
    sentence.setFont(font);
    sentence.setLineWrap(true);
    sentence.setWrapStyleWord(true);
    sentence.getDocument().addDocumentListener(new CustomDocumentListenerChangedSentence());
    JScrollPane sentenceScrollPane = new JScrollPane(sentence);
    sentenceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
    //create panel that displays the current blue sheet rule
    rule = new JLabel();
    rule.setVerticalAlignment(JLabel.TOP);
    rule.setPreferredSize(new Dimension(100,350));
    JScrollPane scroller = new JScrollPane(rule, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    rule.setOpaque(true);
    rule.setBackground(Color.decode(blueColor));
    JPanel p2 = new JPanel();
    p2.setPreferredSize(new Dimension(200, 60));
    p2.setLayout(new GridBagLayout());
    p2.setBorder(new LineBorder(Color.BLACK));
    GridBagConstraints p2gbc = new GridBagConstraints();
    p2gbc.gridx = 0;
    p2gbc.gridy = 0;
    p2gbc.gridwidth = 1;
	p2gbc.gridheight = 1;
	p2gbc.weightx = 1.0;
	p2gbc.weighty = 1.0;
	p2gbc.anchor = GridBagConstraints.NORTHWEST;
	p2gbc.fill = GridBagConstraints.BOTH;
    p2.add(scroller, p2gbc);
    
    //create panel that has the "change" and "correct" buttons
    JPanel changes = new JPanel();
    changes.setPreferredSize(new Dimension(100,50));
    changes.setLayout(new GridLayout(2,1));
    correct = new JButton("Correct");
    correct.addActionListener(new CustomActionListenerCorrect());
    correct.setEnabled(false);
    change = new JButton("Change");
    change.addActionListener(new CustomActionListenerChange());
    change.setEnabled(false);
    changes.add(correct);
    changes.add(change);
    
    //create panel that has the previous and next buttons 
    //and the panel just created with the "change" and "correct" buttons
    JPanel p3 = new JPanel();
    p3.setPreferredSize(new Dimension(100, 50));
    p3.setLayout(new GridLayout(1,3));
    p3.setBorder(new LineBorder(Color.BLACK));
    previous = new JButton("Previous");
    previous.addActionListener(new CustomActionListenerPrevious());
    previous.setEnabled(false);
    p3.add(previous);
    p3.add(changes);
    next = new JButton("Next");
    next.addActionListener(new CustomActionListenerNext());
    next.setEnabled(false);
    p3.add(next);
     
     //create final panel and add all the previous panels to this panel
	 JPanel panel = new JPanel(new GridBagLayout());
	 GridBagConstraints gbc = new GridBagConstraints();
	 gbc.gridx = 0;
	 gbc.gridy = 0;
	 gbc.gridwidth = 1;
	 gbc.gridheight = 1;
	 gbc.weightx = 1.0;
	 gbc.weighty = 1.0;
	 gbc.fill = GridBagConstraints.BOTH;
	 gbc.anchor = GridBagConstraints.CENTER;
	 panel.add(areaScrollPaneIn, gbc);
	 
	 //grid bag constraints for the first panel
	 gbc.gridx = 1;
	 gbc.gridy = 0;
	 gbc.gridwidth = 1;
	 gbc.gridheight = 1;
	 panel.add(p1, gbc);

	//grid bag constraints for the sentence scroll panel
	 gbc.gridx = 0;
	 gbc.gridy = 1;
	 gbc.gridheight = 1;
	 panel.add(sentenceScrollPane, gbc);
	 
	//grid bag constraints for the second panel
	 gbc.gridx = 1;
	 gbc.gridy = 1;
	 gbc.gridheight = 3;
	 panel.add(p2, gbc);
	 
	//grid bag constraints for the third panel
	 gbc.gridx = 0;
	 gbc.gridy = 3;
	 gbc.gridheight = 1;
	 panel.add(p3, gbc);
	 
	//grid bag constraints for the fourth panel
	 gbc.gridx = 0;
	 gbc.gridy = 4;
	 gbc.gridheight = 1;
	 gbc.gridwidth = 2;
	 
	 //create container and add the final panel to the container
	 Container c = getContentPane();
	 c.add(panel, BorderLayout.CENTER);
	 c.setMinimumSize(c.getSize());
  }
  
  /*
   * getter method for error panel
   */
  public JTextArea getError() {
	  return sentence;
  }
  /*
   * creates a new essay object from the text that the user has
   * inputed and creates a new tree of words in that essay
   */
  public void makeEssayAndTree(String text) {
	  essayEssay = new Essay(text);
	  newRemove = true;
	  tree = essayEssay.getTree();
  }
  
  /*
   * reads in the .wav file "welcome.wav" and plays it
   */
  public static void read() throws Exception {

	    //choose the file to use (in this case, welcome.wav)
	    File file = new File("welcome.wav");
	    AudioInputStream ais = AudioSystem.getAudioInputStream(file);

	    //load the file into a Clip object
	    DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
	    Clip cl = (Clip) AudioSystem.getLine(info);
	    cl.open(ais);

	    //stop reading when clip ends
	    cl.addLineListener(new LineListener() {
	        public void update(LineEvent event) {
	            if (event.getType() == LineEvent.Type.STOP) {
	                event.getLine().close();
	            }
	        }

	    });

	    //play the sound stored in the clip object
	    cl.start();
	}

  /*
   * set the text in the essay text box to the concatenated sentences
   * stored in the essay object's linked list
   */
  public void updateDisplay() {
	  String currentEssay = "";
	  for(ListNode2 node = essayEssay.getSentences(); node != null; node = node.getNext())  {
		  currentEssay += node.getValue() + " ";
	  }
	  essay.setText(currentEssay);
  }
  
  /*
   * makes sure that the previous and next buttons are only enabled if
   * there is a previous and next potential error to go to, respectively
   * also makes sure that the change and correct buttons are disabled
   * if there is no sentence currently being displayed
   */
  public void checkButtons() {
	  if(current != null && current.getValue() != null) {
		  if(getPreviousNotEmpty() == null) {
			  previous.setEnabled(false);
		  }
		  if(getNextNotEmpty() == null) {
			  next.setEnabled(false);
		  }
		  if(current.getValue() != null && getPreviousNotEmpty() != null) {
			  previous.setEnabled(true);
		  }
		  if(current.getValue() != null && getNextNotEmpty() != null)
			  next.setEnabled(true);
	  }
	  else if (current == null || current.getValue() == null) {
		  previous.setEnabled(false);
		  next.setEnabled(false);
		  change.setEnabled(false);
		  correct.setEnabled(false);
	  }
  }
  
  /*
   * displays the potential errors in the designated text area
   * from an array of list nodes that contain WordLocs, returned
   * by the strategy currently being used
   */
  public void displaySentences(ListNode2[] array) {
	  next.setEnabled(false);
	  previous.setEnabled(false);
	  createList(array);
	  if(current != null && current.getValue() != null) {
		  displaySentence(current);
		  if(current.getNext() != null)
			  next.setEnabled(true);
	  }
	  else {
		  sentence.setText("");
	  }
	  checkButtons();
	  		
  }
  
  /*
   * helper method for displaySentences that displays
   * the WordLoc (sentence with potential error highlighted)
   * in the designated text box
   */
  public void displaySentence(ListNode2 node) {
	  WordLoc sentenceLoc = (WordLoc)(node.getValue());
	  String displayedSentence = ((String)(sentenceLoc).getSentenceNode().getValue());
	  sentence.setText(displayedSentence);
	  Highlighter highlighter = sentence.getHighlighter();
      int start = sentenceLoc.getWordIndex();
      int end = start;
      int spaceCounter = 0;
      //apostrophe error highlight (not its)
      if(currentError.equals("apostrophe") && !(Character.isLetter(displayedSentence.charAt(end)))) {
    	  while(end < displayedSentence.length() && !Character.isLetter(displayedSentence.charAt(end)) && displayedSentence.charAt(end) != ' ' && displayedSentence.charAt(end) != ',')
    		  end++;
      }
      //quotation error highlight
      else if(currentError.equals("quotation")) {
    	  while(end < displayedSentence.length() && !Character.isLetter(displayedSentence.charAt(end)) && displayedSentence.charAt(end) != ' ' && displayedSentence.charAt(end) != ',')
    		  end++;
      }
      else if(currentError.equals("passive") || currentError.equals("progressive")) {
    	  while(end < displayedSentence.length() && spaceCounter < 2) {
    		  end++;
    		  if(displayedSentence.charAt(end) == ' ')
    			  spaceCounter++;
    	  }
      }
      //any other error highlight (and its for apostrophe error)
      else {
	      while(end < displayedSentence.length() && displayedSentence.charAt(end) != '.' && displayedSentence.charAt(end) != ' ' && Character.isLetter(displayedSentence.charAt(end))) {
	    	  end++;
	      }
      }
      try {
		highlighter.addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(Color.pink));
	  } 
      catch (BadLocationException e) {
		// Auto-generated catch block
		e.printStackTrace();
	  }
  }
  
  /*
   * helper method for displaySentences that creates a
   * linked list from the list nodes stored in an array
   * returned by the strategy currently being used
   */
  public void createList(ListNode2[] array) {
	  int i = 0;
	  while(i < array.length && array[i] == null){ //find first linked list that is populated
		  i++;
	  }
	  if(i >= array.length){
		  //no populated lists
		  current = null;
		  return;
	  }
	  else{
		  ListNode2 headHead = new ListNode2("Temp Node"); //head of final loop
		  ListNode2 tail = addNodeDuplicates(array[i], headHead);
		  i ++;
		  //add lists from other words
		  while(i < array.length){ //add other lists
			  if(array[i] != null){//add list to headHead
				  tail = addNodeDuplicates(array[i], tail);
			  }
			  i++;
		  }

		  //remove temporary headHead
		  headHead = headHead.getNext();
		  if(headHead == null){
			  //TODO no words, idk what to do here
		  }else{
			  headHead.setPrevious(null);
		  }
		  current = headHead;
	  }
  }
  
  /*
   * creates duplicates of nodes to be added to list of potential errors
   */
  private ListNode2 addNodeDuplicates(ListNode2 head, ListNode2 tail){ //head to be copied, tail of larger list
	  ListNode2 node = head;
	  do{
		  tail.setNext(new ListNode2(node.getValue(), tail, null));
		  tail = tail.getNext();
		  node = node.getNext();
	  }while(node != null && node != head);
	  return tail;
  }
  
  /*
   * returns the passage that the user inputed and is editing
   */
  public String getEssayText() {
    return essay.getText();
  }
  
  /*
   * returns the sentence of the currently displayed WordLoc
   */
  public String getSentenceText() {
	  return sentence.getText();
  }

  /*
   * sets the text in the essay text box to the parameter "text"
   */
  public void setEssayText(String text) {
    essay.setText(text.toString());
    essay.setCaretPosition(0);
  }

  /*
   * sets the text in the displayed WordLoc text box to the parameter "text"
   */
  public void setSentenceText(String text) {
    sentence.setCaretPosition(0);
  }
  
  /******************************************************************/
  /***************     Action Listener Classes     ****************/

  /*
  * Set the rule box to the past tense rule + use the past tense strategy
  * to obtain and display the sentences with potential past tense errors
  */
  class CustomActionListenerOne implements ActionListener{ //database strategy
      public void actionPerformed(ActionEvent e) {
    	  setUp("past");
    	  PastTenseStrategy past = new PastTenseStrategy();
    	  rule.setText(past.getRule());
          if(!essay.getText().equals("")) {
        	  ArrayList<ListNode2> list = past.findInDatabase(tree);
        	  ListNode2[] array = convertToArray(list);
        	  displaySentences(array);
          }
      }
   }
  
  /*
   * Set the rule box to the first or second person rule + use the first 
   * or second person strategy to obtain and display the sentences with 
   * potential first or second person errors
   */
  class CustomActionListenerThree implements ActionListener{ //essay strategy
      public void actionPerformed(ActionEvent e) {
    	  setUp("fs");
    	  FirstSecondPersonStrategy firstSecond = new FirstSecondPersonStrategy();
    	  rule.setText(firstSecond.getRule());
    	  if(!essay.getText().equals("")) {
          	  ListNode2[] array = firstSecond.findInEssay(tree);
          	  displaySentences(array);
          }
      }
   }
  
  /*
   * Set the rule box to the this, which rule + use the this, which strategy
   * to obtain and display the sentences with potential this, which errors
   */
  class CustomActionListenerFour implements ActionListener{ //essay strategy
      public void actionPerformed(ActionEvent e) {
    	  setUp("fw");
    	  ThisWhichStrategy tw = new ThisWhichStrategy();
          rule.setText(tw.getRule());
          if(!essay.getText().equals("")) {
          	  ListNode2[] array = tw.findInEssay(tree);
          	  displaySentences(array);
          }
      }
   }
  
  /*
   * Set the rule box to the pronoun case rule + use the past tense strategy
   * to obtain and display the sentences with potential pronoun case errors
   */
  class CustomActionListenerSix implements ActionListener{ //essay strategy
      public void actionPerformed(ActionEvent e) {
    	  setUp("case");
    	  AppropriateCasePronounsStrategy approp = new AppropriateCasePronounsStrategy();
          rule.setText(approp.getRule());
          if(!essay.getText().equals("")) {
        	  ListNode2[] array = approp.findInEssay(tree);
              displaySentences(array);
          }
      }
   }
  
  /*
   * Set the rule box to the apostrophe rule + use the apostrophe strategy
   * to obtain and display the sentences with potential apostrophe errors
   */
  class CustomActionListenerEight implements ActionListener{ //essay strategy
      public void actionPerformed(ActionEvent e) {
	      setUp("apostrophe");
    	  ApostropheStrategy apost = new ApostropheStrategy();
          rule.setText(apost.getRule());
    	  if(!essay.getText().equals("")) {
    		  ListNode2[] array = apost.findInEssay(tree);
              displaySentences(array);
    	  }
      }
   }
  
  /*
   * Set the rule box to the passive voice rule + use the passive voice strategy
   * to obtain and display the sentences with potential passive voic errors
   */
  class CustomActionListenerNine implements ActionListener{ //database strategy
      public void actionPerformed(ActionEvent e) {
    	  setUp("passive");
    	  PassiveVoiceStrategy passiveVoice = new PassiveVoiceStrategy();
          rule.setText(passiveVoice.getRule());
          if(!essay.getText().equals("")) {
        	  ArrayList<ListNode2> list = passiveVoice.findInDatabase(tree);
        	  ListNode2[] array = convertToArray(list);
        	  displaySentences(array);
          }
      }
   }
  
  /*
   * Set the rule box to the progressive voice rule + use the progressive voice strategy
   * to obtain and display the sentences with potential passive voice errors
   */
  class CustomActionListenerTwelve implements ActionListener{ //database strategy
      public void actionPerformed(ActionEvent e) {
    	  setUp("progressive");
    	  ProgressiveTenseStrategy progressive = new ProgressiveTenseStrategy();
          rule.setText(progressive.getRule());
          if(!essay.getText().equals("")) {
        	  ArrayList<ListNode2> list = progressive.findInDatabase(tree);
        	  ListNode2[] array = convertToArray(list);
        	  displaySentences(array);
          }
      }
   }
  
  /*
   * Set the rule box to the quotation rule + use the quotation strategy
   * to obtain and display the sentences with potential quotation errors
   */
  class CustomActionListenerThirteen implements ActionListener{ //essay strategy
      public void actionPerformed(ActionEvent e) {
    	  setUp("quotation");
    	  QuotationStrategy quotation = new QuotationStrategy();
          rule.setText(quotation.getRule());
          if(!essay.getText().equals("")) {
	          ListNode2[] array = quotation.findInEssay(tree);
	          displaySentences(array);
          }
      }
   }
  
  /*
   * helper method that sets newInsert to false (so that the change
   * button is only enabled after the user makes a change to the sentence),
   * sets the error panel to empty text and sets the currentError to the
   * blue sheet error the user is searching for
   */
  private void setUp(String error){
	  newInsert = false;
	  currentError = error;
  }
  
  /*
   * helper method for action listeners for radio buttons that converts 
   * an array list of ListNode2 objects to an array of ListNode2 objects
   */
  private ListNode2[] convertToArray(ArrayList<ListNode2> list) {
	  ListNode2[] array = new ListNode2[list.size()];
	  for(int i = 0; i < list.size(); i++) {
		  array[i] = list.get(i);
	  }
	  return array;
  }
 
 /*
 * displays the previous WordLoc and ensures that the 
 * buttons are enabled/disabled properly
 */
  class CustomActionListenerPrevious implements ActionListener{
	  public void actionPerformed(ActionEvent e) {
		  newInsert = false;
		  newRemove = false;
		  current = getPreviousNotEmpty();
		  correct.setEnabled(true);
		  change.setEnabled(false);
		  displaySentence(current);
		  checkButtons();
      }
   }
  
  /*
   * helper method for the previous button action listener that returns 
   * the first previous node that does not contain a WordLoc of value null
   */
  private ListNode2 getPreviousNotEmpty() {
	  ListNode2 node = current;
	  do{
		  node= node.getPrevious();
		  if(node == null){
			  return null;
		  }
	  }while(node.getValue() == null);
	  return node;
  }

  /*
   * displays the next WordLoc and ensures that the 
   * buttons are enabled/disabled properly
   */
  class CustomActionListenerNext implements ActionListener{
	  public void actionPerformed(ActionEvent e) {
		  newInsert = false;
		  newRemove = false;
		  current = getNextNotEmpty();
		  displaySentence(current);
		  correct.setEnabled(true);
		  change.setEnabled(false);
		  checkButtons();
      }
   }
 
  /*
   * helper method for the next button action listener that returns 
   * the next node that does not contain a WordLoc of value null
   */
  private ListNode2 getNextNotEmpty() {
	  ListNode2 node = current;
	  do{
		  node= node.getNext();
		  if(node == null){
			  return null;
		  }
	  }while(node.getValue() == null);
	  return node;
  }
 
  /*
   * removes the currently displayed WordLoc from the list of WordLocs
   * being displayed and adds this new, edited WordLoc back into the
   * essay's tree map
   * also ensures that the buttons are enabled/disabled properly and ensures
   * that the essay text box contains the most recent version of the essay
   */
  class CustomActionListenerChange implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		  String changedSentence = sentence.getText();
		  ListNode2 nodeBeingChanged = current;
		  if(next.isEnabled())
			  next.doClick();
		  else if(previous.isEnabled())
			  previous.doClick();
		  else {
			  sentence.setText("");
		  }
    	  essayEssay.disconnectAndAdd(nodeBeingChanged, changedSentence);
    	  checkButtons();
    	  updateDisplay();
      }
  }
  
  /*
   * removes the currently displayed WordLoc from the list of WordLocs,
   * makes sure that the buttons are enabled/disabled properly, and ensures
   * that the essay text box contains the most recent version of the essay
   */
  class CustomActionListenerCorrect implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		  ListNode2 nodeBeingRemoved = current;
		  if(next.isEnabled())
			  next.doClick();
		  else if(previous.isEnabled())
			  previous.doClick();
		  else {
			  sentence.setText("");
		  }
    	  essayEssay.removeCorrected(nodeBeingRemoved);
    	  checkButtons();
    	  updateDisplay();
      }
  }
  
  /*
   * ensures that between the correct and change buttons, only one is enabled
   * at once - enables change and disabled correct as soon as the user makes
   * a change to the sentence displayed in the displayed WordLoc text box
   */
  class CustomDocumentListenerChangedSentence implements DocumentListener{
	    public void insertUpdate(DocumentEvent e) {
	        if(!newInsert) {
	        	correct.setEnabled(true);
	        	change.setEnabled(false);
	        	newInsert = true;
	        }
	        else {
	        	correct.setEnabled(false);
	        	change.setEnabled(true);
	        }
	    }
	    public void removeUpdate(DocumentEvent e) {
	    	if(!newRemove) {
	        	correct.setEnabled(true);
	        	change.setEnabled(true);
	        	newRemove = true;
	        }
	        else {
	        	correct.setEnabled(false);
	        	change.setEnabled(true);
	        }
	    	
	    }
	    public void changedUpdate(DocumentEvent e) {
	        //Plain text components do not fire these events
	    }
   }
  
  /*
   * as soon as the error adds text to the "inert text" box, 
   * saves the text placed in the "insert text" box into the field "copied"
   */
  class CustomDocumentListenerCopyMade implements DocumentListener{
	    public void insertUpdate(DocumentEvent e) {
	    	copied = copy.getText();
	    }
	    public void removeUpdate(DocumentEvent e) {
	    	copied = copy.getText();
	    }
	    public void changedUpdate(DocumentEvent e) {
	        //Plain text components do not fire these events
	    }
 }
 
  /*
   * closes the "insert text" window after the "insert text" button
   * is pressed and calls the menu to parse the inserted text
   * and create an essay object from it
   */
  class CustomActionListenerPaste implements ActionListener{
	  public void actionPerformed(ActionEvent e) {
		  frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		  thisMenu.parseAndCreateEssay(copied);
      }
  }
  
  /*
   * when the user clicks file, insert text, this creates a text
   * box in which the user can insert their own text and then run
   * the program with that text
   */
  private class EssayAction implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      String cmd = ((AbstractButton)e.getSource()).getActionCommand();

      if ("Insert...".equals(cmd)) {
    	  frame = new JFrame("Insert your essay here");
    	  frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	  JPanel p = new JPanel();
    	  p.setLayout(new GridBagLayout());
    	  copy = new JTextArea(30,50);
    	  Font font = new Font("Monospaced", Font.PLAIN, 12);
    	  copy.setFont(font);
    	  copy.setLineWrap(true);
    	  copy.setWrapStyleWord(true);
    	  copy.getDocument().addDocumentListener(new CustomDocumentListenerCopyMade());
    	  JScrollPane sentenceScrollPane = new JScrollPane(copy);
    	  sentenceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	  JButton closeAndPaste = new JButton("Insert Text");
    	  closeAndPaste.addActionListener(new CustomActionListenerPaste());
    	  GridBagConstraints gbc = new GridBagConstraints();
    	  gbc.gridx = 0;
    	  gbc.gridy = 0;
    	  gbc.weightx = 1.0;
    	  gbc.weighty = 1.0;
    	  gbc.gridheight = 2;
    	  gbc.anchor = GridBagConstraints.NORTHWEST;
    	  gbc.fill = GridBagConstraints.BOTH;
    	  p.add(sentenceScrollPane, gbc);
    	  gbc.gridy = 2;
    	  gbc.gridheight = 1;
    	  p.add(closeAndPaste, gbc);
    	  frame.add(p);
  
    	  frame.pack();
    	  frame.setVisible(true);
    	  frame.setMinimumSize(frame.getSize());
      }
    }
  }

  /******************************************************************/
  /***************                  main             ****************/
  /**
 * @throws Exception ****************************************************************/

  public static void main(String[] args) throws Exception
  {
    AutoHirsch window = new AutoHirsch();
    window.setDefaultCloseOperation(EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);
    window.setMinimumSize(window.getSize());
    read();
  }
}
