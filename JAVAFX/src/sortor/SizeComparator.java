package sortor;

import java.io.File;
import java.util.Comparator;
import java.util.Date;

import application.MyFile;

public class SizeComparator implements Comparator<MyFile>{ 
	
	private boolean isUp;
	public SizeComparator(boolean isUp) {
		// TODO Auto-generated constructor stub
		super();
		this.isUp = isUp;
	}
	
	@Override
	public int compare(MyFile arg0, MyFile arg1) {
		// TODO Auto-generated method stub
		if(arg0.isDir) {
			if(arg1.isDir)
				return isUp?arg0.getName().compareTo(arg1.getName()):
					arg1.getName().compareTo(arg0.getName());	
			else
				return isUp?0:1;
		}
		else {
			if(arg1.isDir)
				return isUp?1:0;
			else {
				boolean eflag = false;
				if(arg0.getSize()==arg1.getSize())
						eflag = true;
				if(isUp)
					if(eflag)
						return arg0.getName().compareTo(arg1.getName());
					else 
						return arg0.getSize()>arg1.getSize()?1:0;
				else
					if(eflag)
						return arg1.getName().compareTo(arg0.getName());
					else 
						return arg0.getSize()>arg1.getSize()?0:1;
			}		
		}
	};
}
