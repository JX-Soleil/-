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
import java.util.List;
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
   
   ObservableList<MyFile> data = FXCollections.observableArrayList();
   MyFile parentOfCurrentFile;
   //记录当前路径
   private StringBuilder currentPath = new StringBuilder();
   
   //文件筛选器和筛选器工厂
   private Selector selector;
   private SelectorFactory selectorFactory = new SelectorFactory();
   
   //文件排序器和排序器工厂
   private Comparator<MyFile> comparator;
   private ComparatorFactory comparatorFactory = new ComparatorFactory();
   
   //根目录列表
   List<MyFile> rootList = new ArrayList<>();
   
   FilterInfo filterInfo = new FilterInfo();
   SortInfo sortInfo = new SortInfo();
   
   @Override
   public void initialize(URL location, ResourceBundle resources) {

      // TODO (don't really need to do anything here).
	  System.out.println("start");
	  //tableView初始化
	  upButton.setDisable(true);
	  //初始化selector
	  comparator = comparatorFactory.getComparator(sortInfo);
	  selector = selectorFactory.getSelector(filterInfo);
	  initTable();
	  
   }
  
   //行响应事件(其实是cell的响应事件)
   private class TaskCellFactory implements Callback<TableColumn<MyFile, String>, TableCell<MyFile, String>> {
	    @Override
	    public TableCell<MyFile, String> call(TableColumn<MyFile, String> param) {
	        TextFieldTableCell<MyFile, String> cell = new TextFieldTableCell<>();
	        cell.setOnMouseClicked((MouseEvent t) -> {
	            if (t.getClickCount() == 2) {
	            	System.out.println("double clicked!!");
	            	MyFile selectFile = mainTable.getSelectionModel().getSelectedItem();
					if(selectFile.isFile)
						return ;
					upButton.setDisable(false);
					//更新路径
					if(currentPath.length()>0 && currentPath.charAt(currentPath.length()-1)!='\\')
						currentPath.append('\\');
					currentPath.append(selectFile.getName());
					pathFiled.setText(currentPath.toString());
					//更新文件列表
					parentOfCurrentFile = selectFile;
					data.clear();
					
					
					//一个文件可能没有读权限---会报错--bug依然存在
					if(selector!=null)
					{
						for(int i=0;i<selectFile.getFileList().size();i++)
						{
							if(selector.filter(selectFile.getFileList().get(i)))
								data.add(selectFile.getFileList().get(i));
						}
					}
					else {
						data.addAll(selectFile.getFileList());
					}
					
				
					//执行筛选以及排序
					data.sort(comparator);
	            }
	        });
			//cell.setContextMenu(taskContextMenu );
	        return cell;
	    }
	}
   
   public void initTable()
   {
	   //初始化根目录列表
	   File[] root = File.listRoots();   
	   for(File f : root)
	   {
		   rootList.add(new MyFile(f));
	   }
	   data.addAll(rootList);
	   data.sort(comparator);
	   mainTable.setItems(data);
	   
	   //属性绑定
	   //TableColumn nameCol = new TableColumn("名称");
       nameCol.setMinWidth(100);
       nameCol.setSortable(false);
       nameCol.setCellFactory(new TaskCellFactory());
       nameCol.setCellValueFactory(
               new PropertyValueFactory<>("name"));
	      
	   //TableColumn lastModifiedCol = new TableColumn("修改时间");
	   lastModifiedCol.setMinWidth(150);
	   lastModifiedCol.setSortable(false);
	   lastModifiedCol.setCellFactory(new TaskCellFactory());
	   lastModifiedCol.setCellValueFactory(
               new PropertyValueFactory<>("lastModified"));

       //TableColumn sizeCol = new TableColumn("大小");
       sizeCol.setMinWidth(200);
       sizeCol.setSortable(false);
       sizeCol.setCellFactory(new TaskCellFactory());
       sizeCol.setCellValueFactory(
               new PropertyValueFactory<>("size"));    
       
   }

   //返回上一层按钮响应事件
   public void upButtonAction(ActionEvent event)
   {
	   //更新当前路径
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
	   
	   //更新列表
	   List<MyFile> listFile = new ArrayList<>();
	   //data为空的时候会报错 ->已修复
	   //data.get(0).getParent().getParent().getFileList();
	   if(parentOfCurrentFile.getParent()!=null) {
		   listFile = parentOfCurrentFile.getParent().getFileList();
		   parentOfCurrentFile = parentOfCurrentFile.getParent();
	   }  
	   else
		   listFile = rootList;
	   data.clear();
	   if(selector!=null)
		{
			for(int i=0;i<listFile.size();i++)
			{
				if(selector.filter(listFile.get(i)))
					data.add(listFile.get(i));
			}
		}
		else {
			data.addAll(listFile);
		}

	   data.sort(comparator);
   }
   
   public void filterButtonAction(ActionEvent event)
   {
	   FilterDialog fDialog = new FilterDialog(filterInfo);
	   //获取返回值result---设定Filter
	   Optional<FilterInfo> result = fDialog.showAndWait();
	   filterInfo = result.get();
	   //对selector对象进行更新
	   selector = selectorFactory.getSelector(filterInfo);
	   System.out.println(filterInfo.getType());
	   System.out.println(selector);
//	   System.out.println("nameTest");
//	   System.out.println(filterInfo.isPattern());
//	   System.out.println(filterInfo.getPattern());
//	   System.out.println("timeTest");
//	   System.out.println(filterInfo.isDate());
//	   System.out.println(filterInfo.getStartDate());
//	   System.out.println(filterInfo.getEndDate());
//	   System.out.println("sizeTest");
//	   System.out.println(filterInfo.isSize());
//	   System.out.println(filterInfo.getSmallSize());
//	   System.out.println(filterInfo.getLargeSize());
//	   System.out.println("testEnd");
   }
   
   public void sortButtonAction(ActionEvent event)
   {
	   System.out.println("click");
	   SortDialog sDialog = new SortDialog(sortInfo);
	   Optional<SortInfo> result=sDialog.showAndWait();
	   sortInfo = result.get();
	   comparator = comparatorFactory.getComparator(sortInfo);
	   data.sort(comparator);
   }
}