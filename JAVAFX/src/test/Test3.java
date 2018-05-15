package test;

import java.util.HashMap;
import java.util.Map;
import application.MyFile;
import log.FileSL;

public class Test3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, MyFile> map = new HashMap<>();
		map.put("C:\\DATA", new MyFile("C:\\DATA"));
		FileSL.saveObjToFile(map, ".\\ass.txt");
		Map<String, MyFile> mapTest = new HashMap<>();
		mapTest = (Map<String, MyFile>) FileSL.getObjFromFile(".\\ass.txt");
		for(Map.Entry<String, MyFile> entry : mapTest.entrySet())
			System.out.println(entry.getKey()+" "+entry.getValue());
	}

}
