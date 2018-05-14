package selector;

import java.util.Date;

import application.FilterInfo;
import application.MyFile;

public class TimeSelector extends DecorateSelector {
	private Date startDate;
	private Date endDate;
	
	public TimeSelector(Selector selector, FilterInfo filterInfo) {
		super(selector);
		// TODO Auto-generated constructor stub
		startDate = filterInfo.getStartDate();
		endDate = filterInfo.getEndDate();
	}

	@Override
	public boolean filter(MyFile myFile) {	
		// TODO Auto-generated method stub
		if(myFile.getLastModifiedDate().after(startDate)&&myFile.getLastModifiedDate().before(endDate))
			return true&&super.filter(myFile);
		return false;
	}
}
