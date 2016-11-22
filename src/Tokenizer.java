import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
	
	ArrayList<String> tokenList;	// passed tokenList from outside of the Tokenizer object
	
	// constructor
	Tokenizer(ArrayList<String> list){
		
		this.tokenList = list;
		
	}
	
	// tokenize passed string and with each match add it to the Tokenizer's tokenList
	// this list is created outside of the Tokenizer object, therefore can be accessed independently as well
	public ArrayList<String> tokenize(String str){
		
		// regular expression to match any known token
		str = str.toLowerCase();
		Matcher m = Pattern
				.compile("(select)|(as)|(from)|(where)|<|>|,|\\+|[A-Za-z][A-Za-z0-9_$#]*(.[A-Za-z0-9_$#]+)?|[A-Za-z0-9]+")
				.matcher(str);
		
		// for each match in the string, add to the token list
		while (m.find()) {
			 this.tokenList.add(m.group());
		}
		
		// return tokenList, if called outside of Tokenizer class
		return this.tokenList;
		
	}
	
}