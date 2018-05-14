package test;

import java.util.Date;
import java.util.jar.Attributes.Name;

import application.FilterInfo;
import application.MyFile;
import selector.ConcreteSelector;
import selector.NameSelector;
import selector.Selector;
import selector.SizeSelector;

public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FilterInfo name = new FilterInfo();
		name.setType(1);
		name.setPattern("abc");
		
		FilterInfo size = new FilterInfo();
		size.setType(3);
		size.setSmallSize(10000);
		size.setLargeSize(10000000);
		
		Selector selector = new ConcreteSelector();
		selector = new NameSelector(selector, name);
		//name.setPattern("cdd");
		selector = new NameSelector(selector, name);
		selector = new SizeSelector(selector, size);
		MyFile myFile = new MyFile();
		myFile.setName("abcasdas");
		long size_long = 100000;
		myFile.setSize(size_long);
		System.out.println("size :  "+myFile.getSize());
		System.out.println(selector.filter(myFile));
	}

}
