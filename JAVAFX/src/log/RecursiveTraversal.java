package log;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import application.MyFile;

import java.util.Set;

public class RecursiveTraversal {

	private ExecutorService service;
	final private BlockingQueue<ConcurrentHashMap<String, List<MyFile>>> fileLog = new ArrayBlockingQueue<ConcurrentHashMap<String, List<MyFile>>>(
			500);
	final AtomicLong fileVisits = new AtomicLong();

	private static ConcurrentHashMap<String, List<MyFile>> map = new ConcurrentHashMap<>();

    // ���̼߳���
    private void startDir(String p) {
    	//System.out.println("yes");
    	fileVisits.incrementAndGet();
        service.execute(new Runnable() {
            public void run() {
            	getRecursiveTree(p);
            }
        });
    }
	
	
	public ConcurrentHashMap<String, List<MyFile>> getR(String p) throws InterruptedException{
		map.clear();
		service = Executors.newFixedThreadPool(100);
        try {
            startDir(p);
            while (fileVisits.get() > 0 || fileLog.size() > 0) {
            	// ��BlockingQueueȡ�����׵Ķ���,
            	// �����ָ��ʱ����,һ�������ݿ�ȡ,���������ض����е�����  
                // ����ֱ��ʱ�䳬ʱ��û�����ݿ�ȡ,����ʧ�ܡ� 
                final ConcurrentHashMap<String, List<MyFile>> m1 = fileLog.poll(100, TimeUnit.SECONDS);
                //���������е�map ���������еļ�ֵ�Լ��뵽Ҫ���ص�map��
                Set<Entry<String, List<MyFile>>> entrySet = m1.entrySet();
        		for (Entry<String, List<MyFile>> e : entrySet) {
        			List<MyFile> f = e.getValue();
        			if (f != null) {
        				map.put(e.getKey(), f);
        			}
        		}
            }
            return map;
        } finally {
            service.shutdown();
        }
	}
	
	public void getRecursiveTree(String path) {
		
		ConcurrentHashMap<String, List<MyFile>> m = new ConcurrentHashMap<>();
		
		File file = new File(path);
		List<MyFile> listFile = new ArrayList<>();
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isFile()) {
					MyFile MF = new MyFile(f);
					listFile.add(MF);
				} else {
					MyFile MD =  new MyFile(f);
					listFile.add(MD);
					startDir(f.getAbsolutePath());
				}
			}
		}
		m.put(path, listFile);
		try {
        	//��fileLog�ӵ�BlockingQueue��,���BlockQueueû�пռ�
        	//����ô˷������̱߳����ֱ��BlockingQueue�����пռ�
            fileLog.put(m);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        fileVisits.decrementAndGet();
	}

	//For Test
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		RecursiveTraversal r = new RecursiveTraversal();
		ConcurrentHashMap<String, List<MyFile>> m = null;
		final long start = System.nanoTime();
		m = r.getR("C:\\");
		final long end = System.nanoTime();
//		ReadAndSaveHelper helper = new ReadAndSaveHelper("D_TIM.txt");
//		helper.writeObjToFile(m);
//		ConcurrentHashMap<String, List<MyFile>> m1 = null;
//		m1 = helper.readObjFromFile();
//		Set<Entry<String, List<MyFile>>> entrySet = m1.entrySet();
		
		for (Entry<String, List<MyFile>> e : m.entrySet()) {
			System.out.println(e.getKey());
			List<MyFile> f = e.getValue();
			if (f != null) {
				for (MyFile file : f) {
					System.out.println(file);
				}
			}
		}
		System.out.println(m.size());
		System.out.println(" MAP Time taken: " + (end - start) / 1.0e9);
	}
}