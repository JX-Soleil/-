package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Test1 {
	
	public class FileHelper {  
	    private String fileName;       
	    public FileHelper(){  
	          
	    }      
	    public FileHelper(String fileName){  
	        this.fileName=fileName;  
	    }      
	    /* 
	     * 将person对象保存到文件中 
	     * params: 
	     *  p:person类对象 
	     */  
	    public void saveObjToFile(Person p){  
	        try {  
	            //写对象流的对象  
	            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fileName));  
	            
	            oos.writeObject(p);                 //将Person对象p写入到oos中  
	              
	            oos.close();                        //关闭文件流  
	        } catch (FileNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }   
	    }  
	      
	    /* 
	     * 从文件中读出对象，并且返回Person对象 
	     */  
	    public Person getObjFromFile(){  
	        try {  
	            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fileName));  
	              
	            Person person=(Person)ois.readObject();              //读出对象  
	            ois.close();
	            return person;                                       //返回对象  
	        } catch (FileNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (ClassNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	          
	        return null;  
	    }  
	}  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "C:\\DATA\\a.txt";
		Test1 test1 = new Test1();
		FileHelper f1 = test1.new FileHelper(path);
		Person person = new Person("haha", "男", 10, 10);
		System.out.println(person.getFile());
		f1.saveObjToFile(person);
		Person p1 = f1.getObjFromFile();
		System.out.println(p1);
		System.out.println(p1.getFile());
	}
}
