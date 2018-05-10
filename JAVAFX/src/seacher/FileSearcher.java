package seacher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import selector.Selector;
import selector.SelectorFactory;
import sortor.ComparatorFactory;

public class FileSearcher{

	public void scandir(String path,ArrayList<File> fileList,
			Comparator<File> comparator,Selector selector)
	{
		File dir = new File(path);
		//谓词选择
		//放到参数中
		//Selector selector =  new SelectorFactory().getSelector();
		fileList.clear();
		//有问题MyFILE
//		for(File f:dir.listFiles())
//		{
//			if(selector.filter(f))
//				fileList.add(f);
//		}
		//fileList排序
		//放到参数中
		//Comparator<File> comparator = new ComparatorFactory().getComparator();
		fileList.sort(comparator);
		Collections.sort(fileList, comparator);
	}
}
