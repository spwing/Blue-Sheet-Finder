/*
 * strategy to check for sentences with possible this, which errors within an essay
 * 
 * Written By: Shannon Wing, Kelly Finke, and Kiara Wahnschafft
 * Date: 5/31/16
 */

public class ThisWhichStrategy implements EssaySearchStrategy {

	private static String rule = "<html>" +
	 "<b>IV. Do not use 'this' or 'which' to refer to a clause.</b>" +
	  "<ul>" + "<li style='list-style-type: none'><b>(Incorrect)</b> In Dr. Seuss's <u>Horton Hears a Who</u>, Horton the elephant says that he hears a voice. <i>This</i> causes his friends to accuse him of being insane.</li>" 
	         + "<li style='list-style-type:none'></li>"
	         + "<li style='list-style-type: none'><b>(Correct)</b> In Dr. Seuss's <u>Horton Hears a Who</u>, Horton the elephant says that he hears a voice. <i>This claim</i> causes his friends to accuse him of being insane.</li>" + "</ul" + "</html>";
	
	//returns an array of LinkedLists containing all of the sentences 
	//within the essay that contain a this or a which
	public ListNode2[] findInEssay(TreeMap tree) {
		String[] searchFor = {"this", "which"};
			
			return StrategyHelperMethods.findInEssayHelper(searchFor, tree);
		
	}

	//returns a String representation of the 
	//this which bluesheet rule
	public String getRule() {
		return rule;
	}

}
