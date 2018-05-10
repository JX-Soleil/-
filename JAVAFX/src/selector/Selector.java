package selector;

import java.io.File;

import application.MyFile;

public interface Selector {
	public boolean filter(MyFile f);
}
