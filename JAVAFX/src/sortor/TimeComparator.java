package sortor;

import java.util.Comparator;
import java.util.Date;
import application.MyFile;

public class TimeComparator implements Comparator<MyFile>{

	private static boolean isUp;
	private static Comparator<MyFile> timeComparator;
	public TimeComparator() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public static Comparator<MyFile> getTimeComparator(boolean isUp) {
		if(timeComparator==null)
			timeComparator = new TimeComparator();
		TimeComparator.isUp = isUp;
		return timeComparator;
	}
	
	@Override
	public int compare(MyFile arg0, MyFile arg1) {
		// TODO Auto-generated method stub
		if(arg0.isDir) {
			if(arg1.isDir)
			{
				boolean eflag = false;
				if(arg0.getLastModifiedDate().equals((Date)arg1.getLastModifiedDate()))
						eflag = true;
				if(isUp)
					if(eflag)
						return arg0.getName().compareTo(arg1.getName());
					else 
						return arg0.getLastModifiedDate().before(arg1.getLastModifiedDate())?0:1;
				else
					if(eflag)
						return arg1.getName().compareTo(arg0.getName());
					else 
						return arg0.getLastModifiedDate().before(arg1.getLastModifiedDate())?1:0;
			}		
			else
				return isUp?0:1;
		}
		else {
			if(arg1.isDir)
				return isUp?1:0;
			else {
				boolean eflag = false;
				if(arg0.getLastModifiedDate().equals((Date)arg1.getLastModifiedDate()))
						eflag = true;
				if(isUp)
					if(eflag)
						return arg0.getName().compareTo(arg1.getName());
					else 
						return arg0.getLastModifiedDate().before(arg1.getLastModifiedDate())?0:1;
				else
					if(eflag)
						return arg1.getName().compareTo(arg0.getName());
					else 
						return arg0.getLastModifiedDate().before(arg1.getLastModifiedDate())?1:0;
			}	
		}
	} 
	
}
