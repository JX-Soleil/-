package selector;

import application.FilterInfo;
import application.MyFile;

public class SizeSelector extends DecorateSelector {
	private long minSize;
	private long maxSize;
	
	public SizeSelector(Selector selector,FilterInfo filterInfo) {
		super(selector);
		// TODO Auto-generated constructor stub
		minSize = filterInfo.getSmallSize();
		maxSize = filterInfo.getLargeSize();
	}

	@Override
	public boolean filter(MyFile myFile) {
		// TODO Auto-generated method stub
		//System.out.println("sizeSelector :   "+myFile.getSize());
		if(myFile.isDir)
			return true&&super.filter(myFile);
		if(myFile.getSize()>=minSize&&myFile.getSize()<=maxSize)
			return true&&super.filter(myFile);
		return false;
	}

}
