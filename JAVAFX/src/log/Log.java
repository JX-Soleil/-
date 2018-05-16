package log;

import java.awt.List;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import application.MyFile;

public class Log {
	//将path路径下的全部文件、目录以及其所有子目录下的文件、目录记录到一个map表上
	public ConcurrentHashMap<String, MyFile> record(String path){
		File root_record = new File(path);
		ConcurrentHashMap<String, MyFile> currentFileLog = new ConcurrentHashMap<>();
		if(root_record.exists()){
			System.out.println("entry record  "+root_record.getPath());
			try {
				currentFileLog =new RecordMaker().getFilesInDir(root_record);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("file not exist");
		}
		return currentFileLog;
	}
	
	//将日志文件保存到对应的文件下
	public void saveLog(Object object , String path){
		FileSL.saveObjToFile(object, path);
	}
	
	//从对应的文件中读取日志文件信息
	public ConcurrentHashMap<String, MyFile> getHisLog(String path){
		return (ConcurrentHashMap<String, MyFile>) FileSL.getObjFromFile(path);
	}
}
