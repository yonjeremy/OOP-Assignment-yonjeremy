package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

/**
 * Class that takes in a document, reads it using a buffered reader, and breaks the lines into shingles,
 * then creates shingles based on shingleSize
 * 
 * @author yonjeremy
 */
public class DocumentParser implements Runnable{
	// declare all variables
	private String file;
	private int shingleSize;
	private int k;
	private BlockingQueue<Shingle> q;
	private Deque<String> buffer = new LinkedList<>();
	private int docId;
	
	/**
	 * Constructor that creates a <code>DocumentParser</code> object. A DocumentParser contains a file name, shingleSize, k number, a blocking queue and a docId.
	 * 
	 * @param file String holding the name of a file.
	 * @param shingleSize Integer that indicates the size of a <code>Shingle</code>
	 * @param k Integer that indicates the k number
	 * @param q A blocking queue that holds all the shingles
	 * @param docId Integer that indicates the document ID.
	 */
	public DocumentParser(String file, int shingleSize, int k, BlockingQueue<Shingle> q,int docId) {
		super();
		this.file = file;
		this.shingleSize = shingleSize;
		this.k = k;
		this.q = q;
		this.docId = docId;
	}
	
	// runs the thread when start() is called
	public void run() {
		try {
			// reads the file line by line
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			// reads the file til EOF
			while((line = br.readLine())!= null) {
				// ignore blank lines
				if(line.length()>0) {
					// makes all texts uppercase
					String uLine = line.toUpperCase();
					// split the words by spaces
					String [] words = uLine.split("\\s+");
					// add the words to a buffer
					addWordsToBuffer(words);
					// call the method that returns a shingle
					Shingle s = getNextShingle();
					// puts the shingle unto the queue, ready to be processed by consumer thread
					q.put(s);
				}
			}
		
			// close the buffer
			br.close();
			// flush the buffer
			flushBuffer();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to addWords to the buffer
	 * 
	 * @param words is the array of words that has been sent to the method from the bufferReader
	 */
	private void addWordsToBuffer(String[] words) {
		for (String s: words) {
			buffer.add(s);
		}
	}
	
	
	/**
	 * Method to get the next shingle
	 * 
	 * @return A new Shingle object with the size of integer ShingleSize
	 */
	private Shingle getNextShingle() {
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		
		// makes sure shingle contains amount of words equivalent to shingle size
		while(counter < shingleSize) {
			// looks at the buffer to check if its not a null value
			if(buffer.peek()!=null) {
				// remove the word from the buffer and place it on the string builder
				sb.append(buffer.poll());
				// counter to keep track of amount of words in a shingle
				counter++;
			}
			else {
				// if no other words left in line but shingle not yet full
				counter = shingleSize;
			}
			
		}
		// returns the shingle to the method
		if(sb.length() > 0) {
			// calls the Shingle class and creates a new shingle
			return (new Shingle(docId,sb.toString().hashCode()));
		}
		else {
			return null;
		}
		
	}
	
	/**
	 * Flushes the buffer
	 * 
	 * @throws InterruptedException
	 */
	private void flushBuffer() throws InterruptedException{
		// loops over buffer til 0
		while(buffer.size() > 0) {
			Shingle s = getNextShingle();
			if(s != null) {
				q.put(s);
			}
		}
		// puts a poison on the file to indicate EOF
		q.put(new Poison(docId,"0".toString().hashCode()));
	}
	

}
