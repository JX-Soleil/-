package seacher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import application.MyFile;
import selector.Selector;

public class FileSearcher{

	public static void scandir(String path,ArrayList<MyFile> fileList,
			Comparator<MyFile> comparator,Selector selector)
	{
		if(path.isEmpty())
		{
			File[] root = File.listRoots();   
			fileList.clear();
			for(File f : root)
			{
				MyFile mf = new MyFile(f);
				if(selector.filter(mf))
					fileList.add(mf);
			}
			fileList.sort(comparator);
			return ;
		}
		
		File dir = new File(path);
		//System.out.println("path:  "+path);
		//…∏—°
		if(dir.exists()&&dir.isDirectory())
		{
			fileList.clear();
			for(File file : dir.listFiles()) {
				MyFile mf = new MyFile(file);
				//System.out.println(mf.getName());
				if(selector.filter(mf))
					fileList.add(mf);
			}
		}
		//≈≈–Ú
		fileList.sort(comparator);
		//System.out.println("list:  "+fileList);
		//Collections.sort(fileList, comparator);
	}
}
