package NameRecognizer;

/*******
 * <p> Title: NameRecognizer Class. </p>
 *
 * <p> Description: Validates a name string (first, middle, or last) using a simple
 * finite-state machine. A valid name contains only letters and is 2–15 characters long.</p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 *
 * @version 1.00 2025-08-15 Initial version
 */
public class NameRecognizer {

	/** Default constructor. */
	public NameRecognizer() {}

	/** The error message text produced by the recognizer. */
	public static String nameRecognizerError = "";
	/** The input string being processed by the recognizer. */
	public static String nameRecognizerInput = "";
	/** The index within the input where an error was located (-1 if none). */
	public static int nameIndexOfError = -1;
	private static int state = 0;
	private static int nextState = 0;
	private static String inputLine = "";
	private static char currentChar; 
	private static int currentCharIndex;
	private static boolean running;
	
	private static int size = 0;
	
	private static void displayState() {
		System.out.println("\nState: " + state + " Character: " + currentChar + " Next State: " + nextState);
	}
	
	// move to the next character
	private static void moveToNextCharacter() {
		currentCharIndex++;
		if (currentCharIndex < inputLine.length()) {
			currentChar = inputLine.charAt(currentCharIndex);
		}
		else 
			currentChar = ' ';
	}
	
	public static boolean checkName(String input) {
		//input is empty
		if (input.length() <= 0) {
			nameIndexOfError = 0;
			return false;
		}
		//input is too long 
		if (input.length() > 16) {
			nameIndexOfError = 0;
			//nameRecognizerError = "Input is too long, no processing.\n";
			//System.out.println(nameRecognizerError);
			return false;
		}
		
		state = 0;
		inputLine = input;
		currentCharIndex = 0;
		currentChar = inputLine.charAt(0);
		
		nameRecognizerInput = input;
		nextState = -1;
		running = true;
		
		size = 0;
		
		while (running) {
			switch (state) {
			case 0: 
				if (currentChar >= 'A' && currentChar <= 'Z' ||
				(currentChar >= 'a' && currentChar <= 'z')) {
					nextState = 1;
					size++;
				}
				
				else 
					running = false;
				
				break;
			
			case 1: 
				//if input is a letter, continue 
				if (currentChar >= 'A' && currentChar <= 'Z' ||
				(currentChar >= 'a' && currentChar <= 'z')) {
					nextState = 1;
					size++;
				}
				//invalid character, halt the state machine
				else {
					running = false;
				}
				
				//input is too long 
				if (size >= 16) {
					running = false;
					
				}
				break;

		}
		if (running) {
				
			displayState();
				
			moveToNextCharacter();
				
			state = nextState;
				
		}
		}
		
		
		
		
		switch (state) {
		
		case 0:
			System.out.println("Name started with an invalid letter");
			return false;
			
		case 1: 
			if (size < 2) {
				System.out.println("Name was too short");
				return false;
			}
			else if (size >= 16) {
				System.out.println("Name was too long");
				return false;
			}
			
			else if (currentCharIndex < input.length()) {
				System.out.println("Invalid characters found");
				return false;
			}
			else {
				System.out.println("Name is valid");
				return true;
			}
		
		}
		return false;
		
	}

	
}
