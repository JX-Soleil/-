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
		//ν��ѡ��
		//�ŵ�������
		//Selector selector =  new SelectorFactory().getSelector();
		fileList.clear();
		//������MyFILE
//		for(File f:dir.listFiles())
//		{
//			if(selector.filter(f))
//				fileList.add(f);
//		}
		//fileList����
		//�ŵ�������
		//Comparator<File> comparator = new ComparatorFactory().getComparator();
		fileList.sort(comparator);
		Collections.sort(fileList, comparator);
	}
}
