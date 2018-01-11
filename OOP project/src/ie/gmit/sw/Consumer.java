package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that creates a worker thread that takes a Shingle of the blocking queue, XOR the value with the minhash array, and calculates the similarities between the two documents.
 * 
 * @author yonjeremy
 *
 */
public class Consumer implements Runnable{

	private BlockingQueue<Shingle> q;
	private int k;
	private int[] minhashes;
	private ConcurrentMap<Integer,List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	private ExecutorService pool;


	/**
	 * Constructs a <code>Consumer</code> object that contains a blocking queue and the int k.
	 * 
	 * @param q BlockingQueue that is holding all the shingles
	 * @param k The k number to indicate how many random hashes to be generated
	 */
	public Consumer(BlockingQueue<Shingle> q, int k) {
		this.q = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(1000);
		init();
	}
	
	/**
	 * Initialises the minhash array by generating a list of random hashes with size k.
	 */
	public void init() {
		// gets a random number
		Random random = new Random();
		// initialise the minhash array
		minhashes = new int[k];
		// loop over k number of times while generating random number
		for(int i=0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}
	
	public void run() {
		// declare number of documents to be parsed
		int docCount = 2;
		// loop until all Shingles of documents have been parsed
		while(docCount > 0) {
			try {
				Shingle s = q.take();
				// when instance of f is a poison indicating EOF
				if(!(s instanceof Poison))
				{
					// executes a new worker thread
					pool.execute(new Runnable() {
						
						// runs the new worker thread
						@Override
						public void run() {
							// get the list of minhashes from the map using docID as the key
							List<Integer>list = map.get(s.getDocId());

							// loops over the minhash array k number of times
							for(int i=0;i<minhashes.length;i++) {
								// XOR the hashcode of the shingle with the random hash in the minhash array
								int value = s.getHashCode() ^ minhashes[i];
								
								// checks if the list has not been generated (should only run once per document)
								if(list == null) {
									// creates a list with a max integer default
									list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
									// adds the list to the map
									map.put(s.getDocId(),list);
								}
								else {		
									// if the value of the XOR result is smaller than the current value in the minhash array,
									// replace the value at that i number with the new value
									if(list.get(i)>value) {
										list.set(i, value);
									}
								}
							} 
							// put the list back into the map
							map.put(s.getDocId(), list);			
						}
					});
				}
				else {						
					docCount--;
				}
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
		}
		// calls the calculate Jaccared method
		calculateJaccared();
	}
	
	/**
	 * Calculates the Jaccared value by running a retain all method that gets the intersection of the two lists and performing the Jaccard index.
	 */
	public void calculateJaccared() {
		// gets the first list
		List<Integer> intersection = map.get(1);
		// find all common minhashes with the second list
		intersection.retainAll(map.get(2));
		// gets the percentage of similarity between the two documents
		float jacquared = ((float)intersection.size()/(k*2-(float)intersection.size())) * 100;
		
		// prints the result
		System.out.println("Similarity: " + jacquared + "%");
	}
}


