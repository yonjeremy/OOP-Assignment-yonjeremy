package ie.gmit.sw;

import java.util.*;
/**
 * @author yonjeremy
 * This class contains the menu for the project. It prompts users for input and inserts the inputs into variables
 * 
 * @throws InterruptedException
 */
public class Menu {
	// declare all public variables
	static Scanner console = new Scanner(System.in);
	
	// shows the menu
	public void show() throws InterruptedException{
		// get initial user menu options input
		System.out.println("Press 1 to compare 2 documents\nPress 2 to exit");
		int input = console.nextInt();
		
		// loop over menu until the user exits program
		while(input != 2) {
			// call the function to get paramaters to launch the program
			compareDocumentsFunction();
			
			// show the menu options and keep looping it
			System.out.println("Press 1 to compare 2 documents\nPress 2 to exit");
			input = console.nextInt();			
		}
		
		// display goodbye message
		System.out.println("GoodBye!");
	}
	
	public void compareDocumentsFunction() throws InterruptedException {
		console.nextLine();
		
		// get doc 1 name
		System.out.println("Enter doc 1:");
		String doc1 = console.nextLine();
		// get doc 2 name
		System.out.println("Enter doc 2:");
		String doc2 = console.nextLine();
		// get k value 
		System.out.println("Enter k size (recommended 100):");
		int k = console.nextInt();
		// get shingle size
		System.out.println("Enter shingle size (recommended 5):");
		int shingleSize = console.nextInt();
		
		// call launch method in Launcher class
		new Launcher().launch(doc1,doc2,k,shingleSize);
	}
}
