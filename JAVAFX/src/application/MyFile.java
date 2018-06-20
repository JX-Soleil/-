package application;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFile implements Serializable{
	private String name = new String();
	private String path = new String();
	private long lastModified;
	private long size;
	
	public final boolean isFile;
	public final boolean isDir;
	
	public MyFile()
	{
		isFile = true;
		isDir = false;
	}
	
	public MyFile(File f)
	{
		//解决磁盘名称为空的情况，eg：c盘文件getname()返回null
		if(f.getName().isEmpty())
			this.name=f.toString();
		else
			this.name=f.getName();
		this.path=f.getAbsolutePath();
		this.lastModified=f.lastModified();
		this.size=f.length();
		this.isFile = f.isFile();
		this.isDir = f.isDirectory();
	}
	
	public MyFile(String path) {
		this(new File(path));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path=path;
	}

	public Date getLastModifiedDate()
	{
		return new Date(lastModified);
	}
	
	public String getLastModified() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		return format.format(lastModified);
	}

	public void setLastModified(Long lastModified) {
		this.lastModified=lastModified;
	}

	public Long getSize() {
		if(isFile)
			return size;
		return null;
	}

	public void setSize(Long size) {
		this.size=size;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name+" "+path+" "+this.getLastModified()+" "+size;
	}
}
