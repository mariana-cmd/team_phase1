package NameRecognizer;

/*******
 * <p> Title: PasswordRecognizer Class. </p>
 *
 * <p> Description: Validates a password string against complexity requirements using
 * a character-by-character scan. Returns an error message string or empty string if valid.</p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 *
 * @version 1.00 2025-08-15 Initial version
 */
public class PasswordRecognizer {

	/** Default constructor. */
	public PasswordRecognizer() {}

	/** The error message text produced by the recognizer. */
	public static String passwordErrorMessage = "";		// The error message text
	/** The input string being processed by the recognizer. */
	public static String passwordInput = "";			// The input being processed
	/** The index within the input where an error was located (-1 if none). */
	public static int passwordIndexofError = -1;		// The index where the error was located
	/** True if an upper case letter was found in the input. */
	public static boolean foundUpperCase = false;
	/** True if a lower case letter was found in the input. */
	public static boolean foundLowerCase = false;
	/** True if a numeric digit was found in the input. */
	public static boolean foundNumericDigit = false;
	/** True if a special character was found in the input. */
	public static boolean foundSpecialChar = false;
	/** True if the input is at least 8 characters long. */
	public static boolean foundLongEnough = false;
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;	
	
	
	/*******
	 * <p> Method: checkPassword(String input) </p>
	 *
	 * <p> Description: Checks the given password for complexity requirements. Returns an empty
	 * string if the password is valid, or a descriptive error message if it is not.</p>
	 *
	 * @param input the password string to validate
	 * @return an empty string if valid, or an error message describing what is missing
	 */
	public static String checkPassword(String input) {
		passwordErrorMessage = "";
		passwordIndexofError = 0;
		inputLine = input;
		currentCharNdx = 0;
		
		//no input
		if (input.length() < 7) {
			return "Password must be longer than 8 characters.";
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
		boolean firstError = true;

		if (!foundUpperCase) {
		    errMessage += "→ At least one upper case letter";
		    firstError = false;
		}

		if (!foundLowerCase) {
		    if (!firstError) {
		        errMessage += "\n";
		    }
		    errMessage += "→ At least one lower case letter";
		    firstError = false;
		}

		if (!foundNumericDigit) {
		    if (!firstError) {
		        errMessage += "\n";
		    }
		    errMessage += "→ At least one numeric digit";
		    firstError = false;
		}

		if (!foundSpecialChar) {
		    if (!firstError) {
		        errMessage += "\n";
		    }
		    errMessage += "→ At least one special character";
		    firstError = false;
		}

		if (!foundLongEnough) {
		    if (!firstError) {
		        errMessage += "\n";
		    }
		    errMessage += "→ At least 8 characters";
		}

		if (errMessage.isEmpty())
		    return "";

		passwordIndexofError = currentCharNdx;
		return "Password is missing:\n" + errMessage + "\n";
	

		}
		
	}

