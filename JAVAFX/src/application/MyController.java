package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.sql.rowset.serial.SerialStruct;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Pair;
import log.Log;
import seacher.FileSearcher;
import selector.ConcreteSelector;
import selector.Selector;
import selector.SelectorFactory;
import sortor.ComparatorFactory;

public class MyController implements Initializable {

   
   @FXML
   private TableView<MyFile> mainTable; 
   @FXML
   private TableColumn<MyFile, String> nameCol;
   @FXML
   private TableColumn<MyFile, String> lastModifiedCol;
   @FXML
   private TableColumn<MyFile, String> sizeCol;
   @FXML
   private TextField pathFiled;
   @FXML
   private Button filterButton;
   @FXML
   private Button sortButton;
   @FXML
   private Button upButton;
   @FXML
   private Button jumpButton;
   @FXML
   private Button recordButton;
   @FXML
   private Button saveRecordButton;
   @FXML
   private Button difButton;
   
   ObservableList<MyFile> data = FXCollections.observableArrayList();
   //MyFile parentOfCurrentFile;
   //��¼��ǰ·��
   private StringBuilder currentPath = new StringBuilder();
   
   //�ļ�ɸѡ����ɸѡ������
   private Selector selector;
   private SelectorFactory selectorFactory = new SelectorFactory();
   
   //�ļ�������������������
   private Comparator<MyFile> comparator;
   private ComparatorFactory comparatorFactory = new ComparatorFactory();
   
   //FilterInfo filterInfo = new FilterInfo();
   SortInfo sortInfo = new SortInfo();
   
   @Override
   public void initialize(URL location, ResourceBundle resources) {

      // TODO (don't really need to do anything here).
	  System.out.println("start");
	  //tableView��ʼ��
	  upButton.setDisable(true);
	  //��ʼ��selector
	  comparator = comparatorFactory.getComparator(sortInfo);
	  
	  selector = new ConcreteSelector();
	  initTable();
	  
   }
  
   //����Ӧ�¼�(��ʵ��cell����Ӧ�¼�)
   private class TaskCellFactory implements Callback<TableColumn<MyFile, String>, TableCell<MyFile, String>> {
	    @Override
	    public TableCell<MyFile, String> call(TableColumn<MyFile, String> param) {
	        TextFieldTableCell<MyFile, String> cell = new TextFieldTableCell<>();
	        cell.setOnMouseClicked((MouseEvent t) -> {
	            if (t.getClickCount() == 2) {
	            	//ÿ����ת����selector
	            	selector = new ConcreteSelector();
	            	//System.out.println("double clicked!!");
	            	MyFile selectFile = mainTable.getSelectionModel().getSelectedItem();
					if(selectFile.isFile)
						return ;
					upButton.setDisable(false);
					//����·��
					if(currentPath.length()>0 && currentPath.charAt(currentPath.length()-1)!='\\')
						currentPath.append('\\');
					currentPath.append(selectFile.getName());
					pathFiled.setText(currentPath.toString());
					//�����ļ��б�
					//parentOfCurrentFile = selectFile;
					data.clear();
				
					//һ���ļ�����û�ж�Ȩ��---�ᱨ��--bug��Ȼ����
//					if(selector!=null)
//					{
//						for(int i=0;i<selectFile.getFileList().size();i++)
//						{
//							if(selector.filter(selectFile.getFileList().get(i)))
//								data.add(selectFile.getFileList().get(i));
//						}
//					}
//					else {
//						data.addAll(selectFile.getFileList());
//					}

					//ִ��ɸѡ�Լ�����
					ArrayList<MyFile> fileList = new ArrayList<>();
					FileSearcher.scandir(currentPath.toString(), fileList, comparator, selector);
					data.addAll(fileList);
	            }
	        });
			//cell.setContextMenu(taskContextMenu );
	        return cell;
	    }
	}
   
   public void initTable()
   {
	   //��ʼ����Ŀ¼�б�
	   File[] root = File.listRoots();   
	   List<MyFile> rootList = new ArrayList<>();
	   for(File f : root)
		   rootList.add(new MyFile(f));
	   data.addAll(rootList);
	   data.sort(comparator);
	   mainTable.setItems(data);
	   
	   //���԰�
	   //TableColumn nameCol = new TableColumn("����");
       nameCol.setMinWidth(100);
       nameCol.setSortable(false);
       nameCol.setCellFactory(new TaskCellFactory());
       nameCol.setCellValueFactory(
               new PropertyValueFactory<>("name"));
	      
	   //TableColumn lastModifiedCol = new TableColumn("�޸�ʱ��");
	   lastModifiedCol.setMinWidth(150);
	   lastModifiedCol.setSortable(false);
	   lastModifiedCol.setCellFactory(new TaskCellFactory());
	   lastModifiedCol.setCellValueFactory(
               new PropertyValueFactory<>("lastModified"));

       //TableColumn sizeCol = new TableColumn("��С");
       sizeCol.setMinWidth(200);
       sizeCol.setSortable(false);
       sizeCol.setCellFactory(new TaskCellFactory());
       sizeCol.setCellValueFactory(
               new PropertyValueFactory<>("size"));    
       
   }

   //������һ�㰴ť��Ӧ�¼�
   public void upButtonAction(ActionEvent event)
   {
	   //ÿ����ת����selector
	   selector = new ConcreteSelector();
	   //���µ�ǰ·��
	   if(currentPath.indexOf("\\")!=currentPath.lastIndexOf("\\"))
		   currentPath.delete(currentPath.lastIndexOf("\\"), currentPath.length());
	   else
		   if(currentPath.lastIndexOf("\\")+1==currentPath.length()) {
			   currentPath.setLength(0);
			   upButton.setDisable(true);
		   }  
		   else
			   currentPath.delete(currentPath.lastIndexOf("\\")+1, currentPath.length());
	   pathFiled.setText(currentPath.toString());
	   
	   //�����б�
	   data.clear();
	   ArrayList<MyFile> fileList = new ArrayList<>();
	   FileSearcher.scandir(currentPath.toString(), fileList, comparator, selector);
	   data.addAll(fileList);
   }
   
   public void filterButtonAction(ActionEvent event)
   {
	   FilterDialog fDialog = new FilterDialog();
	   //��ȡ����ֵresult---�趨Filter
	   Optional<Selector> result = fDialog.showAndWait();
	   selector = result.get();
	   //��selector������и���
	   for(int i=0;i<data.size();)
	   {
		   if(!selector.filter(data.get(i)))
			   data.remove(i);
		   else
			   i++;
	   }
   }
   
   public void sortButtonAction(ActionEvent event)
   {
	   //System.out.println("click");
	   SortDialog sDialog = new SortDialog(sortInfo);
	   Optional<SortInfo> result=sDialog.showAndWait();
	   sortInfo = result.get();
	   comparator = comparatorFactory.getComparator(sortInfo);
	   data.sort(comparator);
   }
   
   public void jumpButtonAction(ActionEvent event)
   {
	   //System.out.println("jump");
	   File file = new File(pathFiled.getText());
	   
	   if(!file.exists()) {
		   Alert alert = new Alert(AlertType.ERROR);
		   alert.setTitle("Error Dialog");
		   alert.setHeaderText("������һ������");
		   alert.setContentText("û������ļ�����Ŀ¼��");
		   alert.showAndWait();
	   }
	   else {
		   if(file.isDirectory()){			  
			   data.clear();
			   for(File f : file.listFiles())
			   {
				   MyFile mf = new MyFile(f);
				   if(selector.filter(mf))
					   data.add(mf);
			   }
			   data.sort(comparator);
			   currentPath.setLength(0);
			   currentPath.append(pathFiled.getText());
		   }
		   else {
			   Alert alert = new Alert(AlertType.ERROR);
			   alert.setTitle("Error Dialog");
			   alert.setHeaderText("������һ������");
			   alert.setContentText("·������Ϊһ��Ŀ¼��");
			   alert.showAndWait();
		   }
	   }
   }
   
   //��־ģʽ
   public void recordButtonAction(ActionEvent event)
   {
	   System.out.println("record click!!");
	   Log log = new Log();
	   HashMap<String, MyFile> map = (HashMap<String, MyFile>) log.record(pathFiled.getText());
	   for(Entry<String, MyFile> entry :map.entrySet()) {
		   System.out.println(entry);
	   }
   }
}