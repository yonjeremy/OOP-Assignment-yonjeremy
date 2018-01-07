package ie.gmit.sw;

import java.util.*;
public class Menu {

	public void show() throws InterruptedException {
		Scanner console = new Scanner(System.in);
		System.out.println("Enter doc 1:");
		String doc1 = console.nextLine();
		System.out.println("Enter doc 2:");
		String doc2 = console.nextLine();
		System.out.println("Enter k size:");
		int k = console.nextInt();
		System.out.println("Enter shingle size:");
		int shingleSize = console.nextInt();
		System.out.println("Enter Thread pool size:");
		int threadPoolSize = console.nextInt();
		new Launcher().launch(doc1,doc2,k,shingleSize,threadPoolSize);
		
	}
}
