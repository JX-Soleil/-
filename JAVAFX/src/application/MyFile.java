package application;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import javax.print.attribute.ResolutionSyntax;
import javax.swing.text.StyledEditorKit.BoldAction;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MyFile {
	private SimpleStringProperty name = new SimpleStringProperty();
	private SimpleStringProperty path = new SimpleStringProperty();
	private SimpleLongProperty lastModified = new SimpleLongProperty();
	private SimpleLongProperty size = new SimpleLongProperty();
	
	public final boolean isFile;
	public final boolean isDir;
	//标记是否加载过子目录
	private boolean loadListFile = false;
	private List<MyFile> listFile;
	private MyFile parent;
	private File file;
	
	public MyFile()
	{
		isFile = false;
		isDir = false;
	}
	
	public MyFile(File f)
	{
		//解决磁盘名称为空的情况，eg：c盘文件getname()返回null
		if(f.getName().isEmpty())
			this.name.set(f.toString());
		else
			this.name.set(f.getName());
		this.path.set(f.getAbsolutePath());
		this.lastModified.set(f.lastModified());
		this.size.set(f.length());
		this.isFile = f.isFile();
		this.isDir = f.isDirectory();
		this.parent = null;
		this.file = f;
	}
	
	public MyFile(String path) {
		this(new File(path));	
	}
	
	public MyFile(File f , MyFile parent)
	{
		this(f);
		this.parent = parent;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getPath() {
		return path.get();
	}

	public void setPath(String path) {
		this.path.set(path);;
	}

	public Date getLastModifiedDate()
	{
		return new Date(lastModified.get());
	}
	
	public String getLastModified() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		return format.format(lastModified.get());
	}

	public void setLastModified(Long lastModified) {
		this.lastModified.set(lastModified);
	}

	public Long getSize() {
		if(isFile)
			return size.get();
		return null;
	}

	public void setSize(Long size) {
		this.size.set(size);;
	}
	
	public List<MyFile> getFileList()
	{
		if(isFile)
			return null;
		//这里应该还需要判断是否修改过。
		if(!loadListFile)
		{
			listFile = new ArrayList<>();
			for(File f : file.listFiles())
			{
				listFile.add(new MyFile(f,this));
			}
			loadListFile = true;
		}	
		return listFile;
	}
	
	public MyFile getParent()
	{
		return parent;
	}
	
	
	public boolean canWrite()
	{
		return file.canWrite();
	}
	public boolean canRead()
	{
		return file.canRead();
	}
	public boolean isHidden()
	{
		return file.isHidden();
	}
}
