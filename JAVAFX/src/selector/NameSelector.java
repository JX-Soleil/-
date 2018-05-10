package selector;

import application.FilterInfo;
import application.MyFile;

public class NameSelector implements Selector{
	private String pattern;
	
	public NameSelector(FilterInfo filterInfo) {
			pattern = filterInfo.getPattern();
	}
	@Override
	public boolean filter(MyFile f) {
		// TODO Auto-generated method stub
		if(f.getName().matches(".*"+pattern+".*"))
			return true;
		return false;
	}
}
