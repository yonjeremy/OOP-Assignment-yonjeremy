package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer implements Runnable{

	private BlockingQueue<Shingle> q;
	private int k;
	private int[] minhashes;
	private Map<Integer,List<Integer>> map = new HashMap();
	private ExecutorService pool;

	public Map<Integer, List<Integer>> getMap() {
		return map;
	}


	public Consumer(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.q = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
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
			
				if(s.getHashCode() !=48)
				{
					pool.execute(new Runnable() {
						int temp = Integer.MAX_VALUE;
						@Override
						public void run() {
							
							
							for(int i=0;i<minhashes.length;i++) {
								List<Integer>list = map.get(s.getDocId());
								int value = s.getHashCode() ^ minhashes[i];
								if(list == null) {
									list = new ArrayList<Integer>(k);
									for(int j=0;j<k;j++) {
										list.add(Integer.MAX_VALUE);
									}
									map.put(s.getDocId(),list);
								}
								else {
									if(list.get(i)>value) {
										list.set(i, value);
									}
								}
							}				
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
		List<Integer> intersection = map.get(1);
		intersection.retainAll(map.get(2));
		float jacquared = (float)intersection.size()/(k*2-(float)intersection.size());
		
		System.out.println("J: " + jacquared);
	}
}
