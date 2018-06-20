package selector;

import application.FilterInfo;
import application.MyFile;

public class NameSelector extends DecorateSelector {

	private String pattern;
	public NameSelector(Selector selector,FilterInfo filterInfo) {
		super(selector);
		pattern = filterInfo.getPattern();
	}

	@Override
	public boolean filter(MyFile myFile) {
		if(myFile.getName().matches(".*"+pattern+".*"))
			return true&&super.filter(myFile);
		return false;
	}

}
