package ie.gmit.sw;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Launches the code by creating consumer and producer threads
 * 
 * @author yonjeremy
 */
public class Launcher {
	
	
	/**
	 * Method to launch threads.
	 * 
	 * @param doc1 The name of Document 1 (String) ending with txt.
	 * @param doc2 The name of Document 2 (String)ending with txt.
	 * @param k Integer of the k number
	 * @param shingleSize Integer of the ShingleSize of one Shingle.
	 * @throws InterruptedException If the Document Parser cannot read the document.
	 */
	public void launch(String doc1, String doc2, int k, int shingleSize) throws InterruptedException {
		//declare all variables
		BlockingQueue<Shingle> q  = new LinkedBlockingQueue<Shingle>(100);
		
		// declare Thread 1 to parse first document
		Thread t1 = new Thread(new DocumentParser(doc1,shingleSize,k,q,1));
		// declare Thread 2 to parse second document
		Thread t2 = new Thread(new DocumentParser(doc2,shingleSize,k,q,2));
		// declare Thread 3 to create worker threads to read from blocking queue
		Thread t3 = new Thread(new Consumer(q,k));
		
		// start all threads
		t1.start();
		t2.start();
		t3.start();
		
		// join all threads
		t1.join();
		t2.join();
		t3.join();
	}
}
