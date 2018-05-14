package selector;

import application.MyFile;

public abstract class DecorateSelector extends Selector {
	private Selector selector;
	public DecorateSelector(Selector selector) {
		// TODO Auto-generated constructor stub
		this.selector = selector;
	}
	public boolean filter(MyFile myFile)
	{
		return selector.filter(myFile);
	}
}
