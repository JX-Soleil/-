package log;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import application.MyFile;

public class LogComparator {
	public static final int FILE_SAME = 0;
	public static final int FILE_DIFFERENCE = 1;
	public static final int FILE_ADD = 2;
	public static final int FILE_DELETE = 3;
	public static HashMap<MyFile, Integer> getDif(ConcurrentHashMap<String, MyFile> currentMap ,
			ConcurrentHashMap<String, MyFile> historyMap){
		HashMap<MyFile, Integer> resultMap = new HashMap<>();
		int len = currentMap.size();
		for(Entry<String, MyFile> entry : currentMap.entrySet()) {
			if(historyMap.get(entry.getKey())!=null){
				if(entry.getValue().getLastModifiedDate().equals(historyMap.get(entry.getKey()).getLastModifiedDate())) {
					resultMap.put(entry.getValue(), FILE_SAME);
					System.out.println("equal : "+entry.getKey());
				}else{
					resultMap.put(entry.getValue(),FILE_DIFFERENCE);
					System.out.println("timeDif :"+entry.getKey());
				}
				currentMap.remove(entry.getKey());
				historyMap.remove(entry.getKey());
			}else {
				resultMap.put(entry.getValue(),FILE_ADD);
				System.out.println("add  :"+entry.getKey());
				currentMap.remove(entry.getKey());
			}
		}
		for(Entry<String, MyFile> entry : historyMap.entrySet()) {
			resultMap.put(entry.getValue(),FILE_DELETE);
			System.out.println("delete  :"+entry.getKey());
		}
		System.out.println(len);
		return resultMap;
	}
}
