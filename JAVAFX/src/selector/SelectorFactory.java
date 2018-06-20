package selector;

import application.FilterInfo;

public class SelectorFactory {
	public Selector getSelector() {
		return new NullSelector();
	}
	
	static public Selector addDecorate(Selector selector , FilterInfo filterInfo)
	{
		switch (filterInfo.getType()) {
		case 0: break;
		case 1: selector = new NameSelector(selector, filterInfo);break;
		case 2: selector = new TimeSelector(selector, filterInfo);break;
		case 3: selector = new SizeSelector(selector, filterInfo);break;
		default: break;
		}
		return selector;
	}
}
