package sortor;

import java.util.Comparator;
import application.MyFile;
import application.SortInfo;

public class ComparatorFactory {
	
	public Comparator<MyFile> getComparator(SortInfo sortInfo)
	{
		Comparator<MyFile> comparator=null;
		switch (sortInfo.getType()) {
		case 0:
			comparator = NameComparator.getNameComparator(sortInfo.getOrder());
			break;
		case 1:
			comparator = TimeComparator.getTimeComparator(sortInfo.getOrder());
			break;
		case 2:
			comparator = SizeComparator.getSizeComparator(sortInfo.getOrder());
			break;
		default:
			break;
		}
		return comparator;
	}
}
