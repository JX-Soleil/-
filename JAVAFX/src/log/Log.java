package log;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import application.MyFile;

public class Log {
	//��path·���µ�ȫ���ļ���Ŀ¼�Լ���������Ŀ¼�µ��ļ���Ŀ¼��¼��һ��map����
	public static ConcurrentHashMap<String, MyFile> record(String path){
		File root_record = new File(path);
		ConcurrentHashMap<String, MyFile> currentFileLog = new ConcurrentHashMap<>();
		if(root_record.exists()){
			System.out.println("entry record  "+root_record.getPath());
			try {
				RecordMaker recordMaker = new RecordMaker();
				recordMaker.visitDir(root_record);
				currentFileLog =recordMaker.getFilesInDir(root_record);
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
	public static void saveLog(Object object , String path){
		FileSL.saveObjToFile(object, path);
	}
	
	//�Ӷ�Ӧ���ļ��ж�ȡ��־�ļ���Ϣ
	@SuppressWarnings("unchecked")
	public static ConcurrentHashMap<String, MyFile> getHisLog(String path){
		return (ConcurrentHashMap<String, MyFile>) FileSL.getObjFromFile(path);
	}
	
	public static String pathTransformToLogPath(String path) {
		path = path.replace(':', '��');
		while(path.contains("\\")) {
			path = path.replace('\\', '_');
		}
		return path;
	}
}
