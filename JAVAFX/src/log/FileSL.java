package log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.print.DocFlavor.STRING;

import application.MyFile;
import test.Person;

public class FileSL {         
    public static void saveObjToFile(Object object,String fileName){  
        try {  
            //写对象流的对象  

            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fileName));  
            oos.writeObject(object);

            oos.close();
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
    }  

    public static Object getObjFromFile(String fileName){  
        try {  
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fileName));  
            Object object=ois.readObject();
            ois.close();
            return object;
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
