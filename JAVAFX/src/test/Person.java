package test;

import java.io.File;
import java.io.Serializable;

public class Person implements Serializable{
	private String name;
	private String sex;
	private int age;
	private int test;
	private File file;
	public Person(String name,String sex , int age , int test) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.test = test;
		file = new File("C:\\DATA\\a.txt");
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getSex()
	{
		return sex;
	}
	public void setSex(String sex)
	{
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getFile()
	{
		return file.toString();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name+" "+sex+" "+age+" "+test;
	}
}