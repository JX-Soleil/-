package application;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import selector.ConcreteSelector;
import selector.Selector;
import selector.SelectorFactory;

public class FilterDialog extends Dialog<Selector>{
	FilterInfo filterInfo = new FilterInfo();
	
	Selector selector = new ConcreteSelector();
	
	CheckBox nameCheckBox = new CheckBox("�ļ���ƥ��");
	CheckBox timeCheckBox = new CheckBox("�ļ�ʱ��ƥ��");
	CheckBox sizeCheckBox = new CheckBox("�ļ���Сƥ��");
	 
	TextField patternField = new TextField();
	 
	DatePicker startDatePicker = new DatePicker();
	DatePicker endDatePicker = new DatePicker();
	 
	TextField minSizeField = new TextField();
	
	TextField maxSizeField = new TextField();
	
	Button addButton = new Button("���");
	Button clearButton = new Button("���");
	
	ChoiceBox<String> minUnit = new ChoiceBox<String>(FXCollections.observableArrayList(
		    "B", "K", "M","G"));
	ChoiceBox<String> maxUnit = new ChoiceBox<String>(FXCollections.observableArrayList(
		    "B", "K", "M","G"));
	
	boolean isPatternField = false;
	
	boolean isStartPicker = false;
	boolean isEndPicker = false;
	
	boolean isMinField = false;
	boolean isMaxField = false;
	boolean isMinChoic = false;
	boolean isMaxChoic = false;

	Node ensureButton;
	
	TableView<TableInfo> infoTable;
	TableColumn<TableInfo, String> nameCol;
	TableColumn<TableInfo, String> detailCol;
	
	ObservableList<TableInfo> data = FXCollections.observableArrayList();
	
	public class TableInfo {
		private FilterInfo info;
		public SimpleStringProperty name = new SimpleStringProperty();
		public SimpleStringProperty detail = new SimpleStringProperty();
		public TableInfo(FilterInfo info) {
			// TODO Auto-generated constructor stub
			this.info = info;
			switch (info.getType()) {
			case 0: name.set("");break;
			case 1: name.set("����ƥ��");break;
			case 2: name.set("ʱ��ƥ��");break;
			case 3: name.set("��Сƥ��");break;
			default: break;
			}	
			switch (info.getType()) {
			case 0: detail.set("");break;
			case 1: detail.set(info.getPattern());break;
			case 2: 
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				detail.set(format.format(info.getStartDate())+"~"+format.format(info.getStartDate()));
				break;
			case 3: detail.set(info.getSmallSize()+"~"+info.getLargeSize());break;
			default: break;
			}	
		}
		public String getName()
		{
			return name.get();
		}
		public void setName(String name)
		{
			this.name.set(name);
		}
		
		public String getDetail()
		{
			return detail.get();
		}
		public void setDetail(String detail)
		{
			this.detail.set(detail);
		}
	}
	
	//��ʼ��pane,��һЩ��������
	private void initPane()
	{
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		grid.add(nameCheckBox, 1, 0);
		 
		grid.add(new Label("�ַ���ģʽ"), 1, 1);
		grid.add(patternField, 2, 1);
		 
		grid.add(timeCheckBox, 1, 2);
		grid.add(new Label("��ʼʱ��"), 1, 3);
		grid.add(startDatePicker, 2, 3);
		grid.add(new Label("����ʱ��"), 3, 3);
		grid.add(endDatePicker, 4, 3);
		 
		grid.add(sizeCheckBox, 1, 4);
		 
		grid.add(new Label("�ļ���С����"), 1, 5);
		grid.add(minSizeField, 1, 6);
		grid.add(minUnit, 2, 6);
		grid.add(new Label("�ļ���С����"), 3, 5);
		grid.add(maxSizeField, 3, 6);
		grid.add(maxUnit, 4, 6);
		
		grid.add(addButton, 1, 7);
		grid.add(clearButton, 2, 7);
		
		grid.add(infoTable,0,0,1,8);
		this.getDialogPane().setContent(grid);
	}
	
	//��ʼ��tableView
	private void initTable()
	{
		infoTable = new TableView<>();
		infoTable.setMaxHeight(250);
		
		infoTable.setItems(data);
		nameCol = new TableColumn<>("ƥ��ģʽ");
		//nameCol.setMinWidth(100);
		detailCol = new TableColumn<>("��ϸ");
		detailCol.setMinWidth(200);
		
		nameCol.setSortable(false);
	    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
	    
	    detailCol.setSortable(false);
	    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    detailCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
	    
	    infoTable.getColumns().addAll(nameCol,detailCol);
	}
	//ensureButton�˳�ʱ��Ҫ���ĸ���filterInfo����
	private void updateFilterInfo()
	{
		if(nameCheckBox.isSelected())
		{
			filterInfo.setType(1);
			filterInfo.setPattern(patternField.getText());
		}
		else if(timeCheckBox.isSelected()){
			filterInfo.setType(2);
			LocalTime localTime = LocalTime.of(0,0,0);
			LocalDateTime startTime = LocalDateTime.of(startDatePicker.getValue(), localTime);
			LocalDateTime endTime = LocalDateTime.of(endDatePicker.getValue(), localTime);
			ZoneId zone = ZoneId.systemDefault();
		    Instant startInstant = startTime.atZone(zone).toInstant();
		    Instant endInstant = endTime.atZone(zone).toInstant();
			filterInfo.setStartDate(Date.from(startInstant));
			filterInfo.setEndDate(Date.from(endInstant));
		}
		else if(sizeCheckBox.isSelected()){
			filterInfo.setType(3);
			long[] unit = {1,1024,1024*1024,1024*1024*1024};	
			int i=0,a=0;
			switch ((String)minUnit.getValue()) {
			case "B": i=0; break;
			case "K": i=1; break;
			case "M": i=2; break;
			case "G": i=3; break;
			default: break;
			}
			switch ((String)maxUnit.getValue()) {
			case "B": a=0; break;
			case "K": a=1; break;
			case "M": a=2; break;
			case "G": a=3; break;
			default: break;
			}
			long minLong = Long.parseLong(minSizeField.getText())*unit[i];
			long maxLong = Long.parseLong(maxSizeField.getText())*unit[a];
			filterInfo.setSmallSize(minLong);
			filterInfo.setLargeSize(maxLong);
		}
		else {
			filterInfo.setType(0);
		}
	}
	
	//������ʼ��������һЩ����change()�¼�---����ʲôʱ�����һЩ����
	private void initButton()
	{
		addButton.setOnAction((ActionEvent e)->{
			updateFilterInfo();
			TableInfo tableInfo = new TableInfo(filterInfo);
			System.out.println(tableInfo.getName()+"  "+tableInfo.getDetail());
			data.add(tableInfo);
			selector = SelectorFactory.addDecorate(selector, filterInfo);
		});
		addButton.setDisable(true);
		
		clearButton.setOnAction((ActionEvent e)->{
			selector = new ConcreteSelector();
			//���tableview 
			data.clear();
		});
				
		patternField.setDisable(true);
		startDatePicker.setDisable(true);
		endDatePicker.setDisable(true);
		minSizeField.setDisable(true);
		maxSizeField.setDisable(true);
		minUnit.setDisable(true);
		maxUnit.setDisable(true);
		
		//�ļ����Ʋ���
		patternField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.equals("")) {
				isPatternField = false;
				addButton.setDisable(true);
			}	
			else {
				isPatternField = true;
				addButton.setDisable(false);
			}
		});
		nameCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue)
			{
				timeCheckBox.setSelected(false);
				sizeCheckBox.setSelected(false);
				
				patternField.setDisable(false);
				if(isPatternField)
					addButton.setDisable(false);
				else
					addButton.setDisable(true);
			}
			else {
				patternField.setDisable(true);
				addButton.setDisable(true);
			}
		});
		//�ļ��޸�ʱ�䲿��
		startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!=null)
				isStartPicker = true;
			else
				isStartPicker = false;
			if(isStartPicker&&isEndPicker)
				addButton.setDisable(false);
			else
				addButton.setDisable(true);
				
		});
		endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!=null)
				isEndPicker = true;
			else
				isEndPicker = false;
			if(isStartPicker&&isEndPicker)
				addButton.setDisable(false);
			else
				addButton.setDisable(true);
		});
		timeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue) {
				nameCheckBox.setSelected(false);
				sizeCheckBox.setSelected(false);
				
				startDatePicker.setDisable(false);
				endDatePicker.setDisable(false);
				if(isStartPicker&&isEndPicker)
					addButton.setDisable(false);
				else
					addButton.setDisable(true);
			}
			else {
				startDatePicker.setDisable(true);
				endDatePicker.setDisable(true);
				addButton.setDisable(true);
			}
		});
		//�ļ���С����
		minSizeField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	minSizeField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		        isMinField = !minSizeField.getText().equals("");
		        if(isMinField&&isMaxField&&isMinChoic&&isMaxChoic)
		        	addButton.setDisable(false);
				else
					addButton.setDisable(true);
		    }
		});
		maxSizeField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	maxSizeField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		        isMaxField = !maxSizeField.getText().equals("");
		        if(isMinField&&isMaxField&&isMinChoic&&isMaxChoic)
		        	addButton.setDisable(false);
				else
					addButton.setDisable(true);
		    }
		});	
		minUnit.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(!isMinChoic&&newValue!=null)
				isMinChoic = true;
			if(isMinField&&isMaxField&&isMinChoic&&isMaxChoic)
				addButton.setDisable(false);
			else
				addButton.setDisable(true);
		});
		maxUnit.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(!isMaxChoic&&newValue!=null)
				isMaxChoic = true;
			if(isMinField&&isMaxField&&isMinChoic&&isMaxChoic)
				addButton.setDisable(false);
			else
				addButton.setDisable(true);
		});
		sizeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue)
			{
				nameCheckBox.setSelected(false);
				timeCheckBox.setSelected(false);
				minSizeField.setDisable(false);
				maxSizeField.setDisable(false);
				minUnit.setDisable(false);
				maxUnit.setDisable(false);
				//System.out.println(isMinField+" "+isMaxField+" "+isMinChoic+" "+isMaxChoic);
				if(isMinField&&isMaxField&&isMinChoic&&isMaxChoic)
					addButton.setDisable(false);
				else
					addButton.setDisable(true);
			}
			else {
				minSizeField.setDisable(true);
				maxSizeField.setDisable(true);
				minUnit.setDisable(true);
				maxUnit.setDisable(true);
				addButton.setDisable(true);
			}
		});
	}
	
	public FilterDialog() {
		// TODO Auto-generated constructor stub
		//filterInfo = fInfo;
		
		this.setTitle("ɸѡ��");
		ButtonType ensureButtonType = new ButtonType("ȷ��", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(ensureButtonType, ButtonType.CANCEL);
		ensureButton = this.getDialogPane().lookupButton(ensureButtonType);
//		ensureButton.setDisable(true);
		
		minSizeField.setMaxSize(100, 20);
		maxSizeField.setMaxSize(100, 20);
		
		initButton();
		initTable();
		//��ʼ������
		initPane();
		
		this.setResultConverter(dialogButton -> {
		    if (dialogButton == ensureButtonType) {
		    	return selector;
		    }
		    else
		    	return new ConcreteSelector();
		});
	}
}
