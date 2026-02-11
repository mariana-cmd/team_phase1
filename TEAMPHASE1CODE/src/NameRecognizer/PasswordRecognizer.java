package NameRecognizer;

public class PasswordRecognizer {
	public static String passwordErrorMessage = "";		// The error message text
	public static String passwordInput = "";			// The input being processed
	public static int passwordIndexofError = -1;		// The index where the error was located
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;	
	
	
	public static String checkPassword(String input) {
		passwordErrorMessage = "";
		passwordIndexofError = 0;
		inputLine = input;
		currentCharNdx = 0;
		
		//no input
		if (input.length() <= 0) {
			return "Password must be at least 8 characters.";
		}
		//input is too long
		if (input.length() >= 255) {
			return "Password is too long.";
		}
		
		currentChar = input.charAt(0);
		
		passwordInput = input;
		
		foundUpperCase = false;	
		foundLowerCase = false;				
		foundNumericDigit = false;			
		foundSpecialChar = false;			
		foundNumericDigit = false;			
		foundLongEnough = false;
		
		running = true;
		
		while (running) {
			//checking if each condition is fulfilled at an index 
			if (currentChar >= 'A' && currentChar <= 'Z') {
				foundUpperCase = true;
			}
			else if (currentChar >= 'a' && currentChar <= 'z') {
				foundLowerCase = true;
			}
			else if (currentChar >= '0' && currentChar <= '9') {
				foundNumericDigit = true;
			}
			else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
				foundSpecialChar = true;
			}
			else {
				passwordIndexofError = currentCharNdx; 
				return "***Invalid character found***";
			}
			
			//checking if the password is long enough 
			if (currentCharNdx >= 7) {
				foundLongEnough = true;
			}
			
			//increment the index that is checking the password 
			currentCharNdx++;
			
			//reached the end of the string, so stop
			if (currentCharNdx >= inputLine.length()) {
				running = false;
			}
			
			// change current character to the next index
			else {
				currentChar = input.charAt(currentCharNdx);
			}
		}
			
			String errMessage = "";
			if (!foundUpperCase)
				errMessage += " atleast one upper case letter.";
			
			if (!foundLowerCase)
				errMessage += " atleast one lower case letter.";
			
			if (!foundNumericDigit)
				errMessage += " atleast one numeric digit";
				
			if (!foundSpecialChar)
				errMessage += " atleast one special character.";
				
			if (!foundLongEnough)
				errMessage += " atleast 8 characters.";
			
			if (errMessage == "")
				return "";
			
			// If it gets here, there something was not found, so return an appropriate message
			passwordIndexofError = currentCharNdx;
			
			return "Password does not have " + errMessage;
	

		}
		
	}

