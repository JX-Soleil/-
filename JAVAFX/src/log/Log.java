package log;

import java.awt.List;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import application.MyFile;

public class Log {
	//��path·���µ�ȫ���ļ���Ŀ¼�Լ���������Ŀ¼�µ��ļ���Ŀ¼��¼��һ��map����
	public Object record(String path){
		File root_record = new File(path);
		HashMap<String, MyFile> map = new HashMap<>();
		if(root_record.exists()){
			System.out.println("entry record  "+root_record.getPath());
				
			makeRecord(root_record, map);
		}
		else {
			System.out.println("file not exist");
		}
		return map;
	}
	
	//û�п��߳�----����
	private void makeRecord(File file , HashMap<String, MyFile> map)
	{
		System.out.println("PUT : "+file.getName());
		map.put(file.getPath(), new MyFile(file));
		if(file.isDirectory()){
			System.out.println("yes");
			if(file.listFiles()!=null)
			for(File f : file.listFiles()){
//				if(f.isHidden())
//					continue ;
				System.out.println("f: "+f.canRead());
				makeRecord(f, map);
			}
		}
	}
	
	//����־�ļ����浽��Ӧ���ļ���
	public void saveLog(Object object , String path){
		FileSL.saveObjToFile(object, path);
	}
	
	//�Ӷ�Ӧ���ļ��ж�ȡ��־�ļ���Ϣ
	public Object getHisLog(String path){
		return FileSL.getObjFromFile(path);
	}
}
