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
	     * ��person���󱣴浽�ļ��� 
	     * params: 
	     *  p:person����� 
	     */  
	    public void saveObjToFile(Person p){  
	        try {  
	            //д�������Ķ���  
	            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fileName));  
	            
	            oos.writeObject(p);                 //��Person����pд�뵽oos��  
	              
	            oos.close();                        //�ر��ļ���  
	        } catch (FileNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }   
	    }  
	      
	    /* 
	     * ���ļ��ж������󣬲��ҷ���Person���� 
	     */  
	    public Person getObjFromFile(){  
	        try {  
	            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fileName));  
	              
	            Person person=(Person)ois.readObject();              //��������  
	            ois.close();
	            return person;                                       //���ض���  
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
		Person person = new Person("haha", "��", 10, 10);
		System.out.println(person.getFile());
		f1.saveObjToFile(person);
		Person p1 = f1.getObjFromFile();
		System.out.println(p1);
		System.out.println(p1.getFile());
	}
}
