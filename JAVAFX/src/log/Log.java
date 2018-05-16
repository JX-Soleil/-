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
	//��path·���µ�ȫ���ļ���Ŀ¼�Լ���������Ŀ¼�µ��ļ���Ŀ¼��¼��һ��map����
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
	
	//����־�ļ����浽��Ӧ���ļ���
	public void saveLog(Object object , String path){
		FileSL.saveObjToFile(object, path);
	}
	
	//�Ӷ�Ӧ���ļ��ж�ȡ��־�ļ���Ϣ
	public ConcurrentHashMap<String, MyFile> getHisLog(String path){
		return (ConcurrentHashMap<String, MyFile>) FileSL.getObjFromFile(path);
	}
}
