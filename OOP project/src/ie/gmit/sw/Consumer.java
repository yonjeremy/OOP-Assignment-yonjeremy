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

public class Consumer implements Runnable{

	private BlockingQueue<Shingle> q;
	private int k;
	private int[] minhashes;
	private ConcurrentMap<Integer,List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	private ExecutorService pool;


	public Consumer(BlockingQueue<Shingle> q, int k) {
		this.q = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(1000);
		init();
	}
	
	public void init() {
		Random random = new Random();
		minhashes = new int[k];
		for(int i=0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}
	
	public void run() {
		int docCount = 2;
		while(docCount > 0) {
			try {
				Shingle s = q.take();
				// when s.getHashCode returns a poison indicating EOF
				if(!(s instanceof Poison))
				{
					pool.execute(new Runnable() {
						
						@Override
						public void run() {
							List<Integer>list = map.get(s.getDocId());

							for(int i=0;i<minhashes.length;i++) {
								int value = s.getHashCode() ^ minhashes[i];
								if(list == null) {
									list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
									map.put(s.getDocId(),list);
								}
								else {		
									if(list.get(i)>value) {
										list.set(i, value);
									}
								}
							} 
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
		calculateJaqcuared();
	}
	
	public void calculateJaqcuared() {
		List<Integer> intersection = map.get(1);
		intersection.retainAll(map.get(2));
		float jacquared = ((float)intersection.size()/(k*2-(float)intersection.size())) * 100;
		
		System.out.println("Similarity: " + jacquared + "%");
	}
}


