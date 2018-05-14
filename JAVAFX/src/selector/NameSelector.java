package selector;

import application.FilterInfo;
import application.MyFile;

public class NameSelector extends DecorateSelector {

	private String pattern;
	public NameSelector(Selector selector,FilterInfo filterInfo) {
		super(selector);
		// TODO Auto-generated constructor stub
		pattern = filterInfo.getPattern();
	}

	@Override
	public boolean filter(MyFile myFile) {
		// TODO Auto-generated method stub
		//System.out.println("nameSelector :   "+myFile.getName());
		if(myFile.getName().matches(".*"+pattern+".*"))
			return true&&super.filter(myFile);
		return false;
	}

}
