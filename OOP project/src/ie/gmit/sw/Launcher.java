package ie.gmit.sw;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {
	
	public void launch(String doc1, String doc2, int k, int shingleSize, int threadPoolSize) throws InterruptedException {
		
		BlockingQueue<Shingle> q  = new LinkedBlockingQueue<Shingle>(100);
		
		Thread t1 = new Thread(new DocumentParser(doc1,shingleSize,k,q,1));
		Thread t2 = new Thread(new DocumentParser(doc2,shingleSize,k,q,2));
		Thread t3 = new Thread(new Consumer(q,k,threadPoolSize));
		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
		
		

	}
}
