package sortor;

import java.util.Comparator;

import com.sun.glass.ui.TouchInputSupport;

import application.MyFile;

public class NameComparator implements Comparator<MyFile>{ 

	private static boolean isUp;
	private static Comparator<MyFile> nameComparator;
	private NameComparator() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public static Comparator<MyFile> getNameComparator(boolean isUp) {
		if(nameComparator==null)
			nameComparator = new NameComparator();
		NameComparator.isUp = isUp;
		return nameComparator;
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
			else
				return isUp?arg0.getName().compareTo(arg1.getName()):arg1.getName().compareTo(arg0.getName());
		}
	};
}
