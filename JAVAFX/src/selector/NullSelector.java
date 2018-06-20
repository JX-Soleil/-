package selector;

import application.MyFile;

public class NullSelector extends Selector {
	@Override
	public boolean filter(MyFile myFile) {
		// TODO Auto-generated method stub
		//System.out.println("concreteSelector");
		return true;
	}
	
}
