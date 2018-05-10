package selector;

import application.FilterInfo;
import application.MyFile;

public class SizeSelector implements Selector {
	private long minSize;
	private long maxSize;
	
	public SizeSelector(FilterInfo filterInfo) {
		// TODO Auto-generated constructor stub
		minSize = filterInfo.getSmallSize();
		maxSize = filterInfo.getLargeSize();
	}
	
	@Override
	public boolean filter(MyFile f) {
		// TODO Auto-generated method stub
		if(f.isDir)
			return true;
		if(f.getSize()>=minSize&&f.getSize()<=maxSize)
			return true;
		return false;
	}

}
