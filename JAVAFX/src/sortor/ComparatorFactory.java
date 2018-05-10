package sortor;

import java.io.File;
import java.util.Comparator;

import application.MyFile;
import application.SortInfo;

public class ComparatorFactory {
	
	public Comparator<MyFile> getComparator(SortInfo sortInfo)
	{
		Comparator<MyFile> comparator=null;
		switch (sortInfo.getType()) {
		case 0:
			comparator = new NameComparator(sortInfo.getOrder());
			break;
		case 1:
			comparator = new TimeComparator(sortInfo.getOrder());
			break;
		case 2:
			comparator = new SizeComparator(sortInfo.getOrder());
			break;
		default:
			break;
		}
		return comparator;
	}
}
