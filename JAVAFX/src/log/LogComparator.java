package log;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import application.MyFile;

public class LogComparator {
	public static HashMap<MyFile, Integer> getDif(ConcurrentHashMap<String, MyFile> currentMap ,
			ConcurrentHashMap<String, MyFile> historyMap){
//		Set<Entry<String, MyFile>> historySet= historyMap.entrySet();
//		Set<Entry<String, MyFile>> currentSet= currentMap.entrySet();
		int len = currentMap.size();
		for(Entry<String, MyFile> entry : currentMap.entrySet()) {
			if(historyMap.get(entry.getKey())!=null){
				if(entry.getValue().getLastModifiedDate().equals(historyMap.get(entry.getKey()).getLastModifiedDate())) {
					System.out.println("equal : "+entry.getKey());
				}else{
					System.out.println("timeDif :"+entry.getKey());
				}
				currentMap.remove(entry.getKey());
				historyMap.remove(entry.getKey());
			}else {
				System.out.println("add  :"+entry.getKey());
				currentMap.remove(entry.getKey());
			}
		}
		for(Entry<String, MyFile> entry : historyMap.entrySet()) {
			System.out.println("delete  :"+entry.getKey());
		}
		System.out.println(len);
		return null;
	}
}
