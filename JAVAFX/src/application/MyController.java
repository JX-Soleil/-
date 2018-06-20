package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import log.Log;
import log.LogComparator;
import log.RecordMaker;
import seacher.FileSearcher;
import selector.NullSelector;
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

	ObservableList<MyFile> data = FXCollections.observableArrayList();
	// ��¼��ǰ·��
	private StringBuilder currentPath = new StringBuilder();

	// �ļ�ɸѡ����ɸѡ������
	private Selector selector;
	private SelectorFactory selectorFactory = new SelectorFactory();

	// �ļ�������������������
	private Comparator<MyFile> comparator;
	private ComparatorFactory comparatorFactory = new ComparatorFactory();

	SortInfo sortInfo = new SortInfo();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		upButton.setDisable(true);
		comparator = comparatorFactory.getComparator(sortInfo);
		selector = selectorFactory.getSelector();
		File logDir = new File("./LogDir");
		if(!logDir.exists()) {
			logDir.mkdirs();
		}
		// tableView��ʼ��
		initTable();
	}

	// ����Ӧ�¼�(��ʵ��cell����Ӧ�¼�)
	private class TaskCellFactory implements Callback<TableColumn<MyFile, String>, TableCell<MyFile, String>> {
		@Override
		public TableCell<MyFile, String> call(TableColumn<MyFile, String> param) {
			TextFieldTableCell<MyFile, String> cell = new TextFieldTableCell<>();
			cell.setOnMouseClicked((MouseEvent t) -> {
				if (t.getClickCount() == 2) {
					// ÿ����ת����selector
					selector = new NullSelector();
					MyFile selectFile = mainTable.getSelectionModel().getSelectedItem();
					if(selectFile==null||selectFile.isFile)
						return;
					upButton.setDisable(false);
					// ����·��
					if (currentPath.length() > 0 && currentPath.charAt(currentPath.length() - 1) != '\\')
						currentPath.append('\\');
					currentPath.append(selectFile.getName());
					pathFiled.setText(currentPath.toString());
					// �����ļ��б�
					data.clear();
					
					// ִ��ɸѡ�Լ�����
					ArrayList<MyFile> fileList = new ArrayList<>();
					FileSearcher.scandir(currentPath.toString(), fileList, comparator, selector);
					data.addAll(fileList);
				}
				
				MouseButton button = t.getButton();
				switch (button) {
				case PRIMARY:
					break;
				case SECONDARY:
					MenuItem getSize = new MenuItem("�鿴��С");
					getSize.setOnAction((ActionEvent e) -> {
						MyFile file = mainTable.getSelectionModel().getSelectedItem();
						long size = 0;
						if(file.isFile)
							size = new File(file.getPath()).length();
						else
							try {
								size= new RecordMaker().visitDir(new File(file.getPath()));
							} catch (InterruptedException | ExecutionException | TimeoutException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						showFileSizeDialog(file,size);
					});
					ContextMenu taskContextMenu = new ContextMenu();
					taskContextMenu.getItems().add(getSize);
					cell.setContextMenu(taskContextMenu);
				case MIDDLE:
					break;
				default:
					;
				}
			});
			return cell;
		}
		
		//��ʾ�ļ���С
		private void showFileSizeDialog(MyFile file, long size) {
			// TODO Auto-generated method stub
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("�ļ���С��Ϣ");
			alert.setHeaderText(null);
			alert.setContentText(file.getName()+"�Ĵ�С��"+size);
			alert.showAndWait();
		}
	}

	public void initTable() {
		// ��ʼ����Ŀ¼�б�
		File[] root = File.listRoots();
		List<MyFile> rootList = new ArrayList<>();
		for (File f : root)
			rootList.add(new MyFile(f));
		data.addAll(rootList);
		data.sort(comparator);
		mainTable.setItems(data);

		// ���԰�
		// TableColumn nameCol = new TableColumn("����");
		nameCol.setMinWidth(100);
		nameCol.setSortable(false);
		nameCol.setCellFactory(new TaskCellFactory());
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		// TableColumn lastModifiedCol = new TableColumn("�޸�ʱ��");
		lastModifiedCol.setMinWidth(150);
		lastModifiedCol.setSortable(false);
		lastModifiedCol.setCellFactory(new TaskCellFactory());
		lastModifiedCol.setCellValueFactory(new PropertyValueFactory<>("lastModified"));

		// TableColumn sizeCol = new TableColumn("��С");
		sizeCol.setMinWidth(200);
		sizeCol.setSortable(false);
		sizeCol.setCellFactory(new TaskCellFactory());
		sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

	}

	// ������һ�㰴ť��Ӧ�¼�
	public void upButtonAction(ActionEvent event) {
		// ÿ����ת����selector
		selector = new NullSelector();
		// ���µ�ǰ·��
		if (currentPath.indexOf("\\") != currentPath.lastIndexOf("\\"))
			currentPath.delete(currentPath.lastIndexOf("\\"), currentPath.length());
		else if (currentPath.lastIndexOf("\\") + 1 == currentPath.length()) {
			currentPath.setLength(0);
			upButton.setDisable(true);
		} else
			currentPath.delete(currentPath.lastIndexOf("\\") + 1, currentPath.length());
		pathFiled.setText(currentPath.toString());

		// �����б�
		data.clear();
		ArrayList<MyFile> fileList = new ArrayList<>();
		FileSearcher.scandir(currentPath.toString(), fileList, comparator, selector);
		data.addAll(fileList);
	}

	//ɸѡ����Ӧ
	public void filterButtonAction(ActionEvent event) {
		FilterDialog fDialog = new FilterDialog();
		// ��ȡ����ֵresult---�趨Filter
		Optional<Selector> result = fDialog.showAndWait();
		selector = result.get();
		// ��selector������и���
		for (int i = 0; i < data.size();) {
			if (!selector.filter(data.get(i)))
				data.remove(i);
			else
				i++;
		}
	}

	//������Ӧ
	public void sortButtonAction(ActionEvent event) {
		SortDialog sDialog = new SortDialog(sortInfo);
		Optional<SortInfo> result = sDialog.showAndWait();
		sortInfo = result.get();
		comparator = comparatorFactory.getComparator(sortInfo);
		data.sort(comparator);
	}

	//��ת��Ӧ
	public void jumpButtonAction(ActionEvent event) {
		File file = new File(pathFiled.getText());
		if (!file.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("������һ������");
			alert.setContentText("û������ļ�����Ŀ¼��");
			alert.showAndWait();
		} else {
			if (file.isDirectory()) {
				data.clear();
				for (File f : file.listFiles()) {
					MyFile mf = new MyFile(f);
					if (selector.filter(mf))
						data.add(mf);
				}
				data.sort(comparator);
				currentPath.setLength(0);
				currentPath.append(pathFiled.getText());
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("������һ������");
				alert.setContentText("·������Ϊһ��Ŀ¼��");
				alert.showAndWait();
			}
		}
	}

	// ��־ģʽ
	public void recordButtonAction(ActionEvent event) {
		//final long start = System.nanoTime();
		ConcurrentHashMap<String, MyFile> map = (ConcurrentHashMap<String, MyFile>) Log.record(pathFiled.getText());
		//final long end = System.nanoTime();
		Log.saveLog(map, "./" + "LogDir/"+Log.pathTransformToLogPath(pathFiled.getText()));
		//final long end2 = System.nanoTime();
//		System.out.println(" MAP Time taken: " + (end - start) / 1.0e9);
//		System.out.println(" Save Time taken: " + (end2 - end) / 1.0e9);
	
	}

	//����ģʽ��Ӧ
	public void logCompareButtonAction(ActionEvent event) {
		File file = new File(pathFiled.getText());
		if (file.exists()) {
			File historyFile = new File("./" + "LogDir/"+Log.pathTransformToLogPath(pathFiled.getText()));
			if (historyFile.exists()) {		
				ConcurrentHashMap<String, MyFile> historyMap = Log.getHisLog(historyFile.getPath());
				ConcurrentHashMap<String, MyFile> currentMap = Log.record(file.getPath());
				HashMap<MyFile, Integer> logComparateMap = LogComparator.getDif(currentMap, historyMap);
				showLogComparateResult(logComparateMap);
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("������һ������");
				alert.setContentText("�����ڴ�Ŀ¼���ļ���־��\n�޷��Ƚ���־���죡");
				alert.showAndWait();
			}
		}
	}

	//��ʾ����ģʽ�µĶԱȽ��
	private void showLogComparateResult(HashMap<MyFile, Integer> logComparateMap) {
		Dialog<Object> comDia = new Dialog<>();
		comDia.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		TextArea textArea = new TextArea();
		StringBuilder text = new StringBuilder();
		for(Entry<MyFile, Integer> entry :logComparateMap.entrySet()) {
			switch (entry.getValue().intValue()) {
			case LogComparator.FILE_SAME:
				//text.append("SAME:        ");
				break;
			case LogComparator.FILE_DIFFERENCE:
				text.append("DIFFERENCE:  ");
				break;
			case LogComparator.FILE_ADD:
				text.append("ADD:         ");
				break;
			case LogComparator.FILE_DELETE:
				text.append("DELETE:      ");
				break;
			default:
				break;
			}
			if(entry.getValue().intValue()!=LogComparator.FILE_SAME)
				text.append(entry.getKey().getPath()+"\n");
		}
		textArea.setText(text.toString());
		comDia.getDialogPane().setContent(textArea);
		comDia.setResultConverter(dialogButton -> {
			return null;
		});
		Optional<Object> result = comDia.showAndWait();
		
	}
}