package log;

import java.awt.List;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import application.MyFile;

public class Log {
	//将path路径下的全部文件、目录以及其所有子目录下的文件、目录记录到一个map表上
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
	
	//没有开线程----很慢
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
	
	//将日志文件保存到对应的文件下
	public void saveLog(Object object , String path){
		FileSL.saveObjToFile(object, path);
	}
	
	//从对应的文件中读取日志文件信息
	public Object getHisLog(String path){
		return FileSL.getObjFromFile(path);
	}
}
