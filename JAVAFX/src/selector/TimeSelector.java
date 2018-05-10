package selector;

import java.io.File;
import java.util.Date;

import application.FilterInfo;
import application.MyFile;

public class TimeSelector implements Selector {
	private Date startDate;
	private Date endDate;
	
	public TimeSelector(FilterInfo filterInfo) {
		startDate = filterInfo.getStartDate();
		endDate = filterInfo.getEndDate();
	}
	@Override
	public boolean filter(MyFile f) {	
		// TODO Auto-generated method stub
		if(f.getLastModifiedDate().after(startDate)&&f.getLastModifiedDate().before(endDate))
			return true;
		return false;
	}

	
}
