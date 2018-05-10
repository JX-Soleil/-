package application;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class FilterDialog extends Dialog<FilterInfo>{
	FilterInfo filterInfo = new FilterInfo();
	
	CheckBox nameCheckBox = new CheckBox("文件名匹配");
	CheckBox timeCheckBox = new CheckBox("文件时间匹配");
	CheckBox sizeCheckBox = new CheckBox("文件大小匹配");
	 
	TextField patternField = new TextField();
	 
	DatePicker startDatePicker = new DatePicker();
	DatePicker endDatePicker = new DatePicker();
	 
	TextField minSizeField = new TextField();
	
	TextField maxSizeField = new TextField();
	
	ChoiceBox minUnit = new ChoiceBox(FXCollections.observableArrayList(
		    "B", "K", "M","G"));
	ChoiceBox maxUnit = new ChoiceBox(FXCollections.observableArrayList(
		    "B", "K", "M","G"));
	
	boolean isPatternField = false;
	
	boolean isStartPicker = false;
	boolean isEndPicker = false;
	
	boolean isMinField = false;
	boolean isMaxField = false;
	boolean isMinChoic = false;
	boolean isMaxChoic = false;

	Node ensureButton;
	
	private void initPane()
	{
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		 
		grid.add(nameCheckBox, 0, 0);
		 
		grid.add(new Label("字符串模式"), 0, 1);
		grid.add(patternField, 1, 1);
		 
		grid.add(timeCheckBox, 0, 2);
		 
		grid.add(new Label("开始时间"), 0, 3);
		grid.add(startDatePicker, 1, 3);
		grid.add(new Label("结束时间"), 2, 3);
		grid.add(endDatePicker, 3, 3);
		 
		grid.add(sizeCheckBox, 0, 4);
		 
		grid.add(new Label("文件大小下限"), 0, 5);
		grid.add(minSizeField, 0, 6);
		grid.add(minUnit, 1, 6);
		grid.add(new Label("文件大小上限"), 2, 5);
		grid.add(maxSizeField, 2, 6);
		grid.add(maxUnit, 3, 6);
		this.getDialogPane().setContent(grid);
	}
	
	private void updateFilterInfo()
	{
		//判断名称匹配是否被选中
		if(nameCheckBox.isSelected())
		{
			filterInfo.setPattern(true);
			filterInfo.setType(1);
			filterInfo.setPattern(patternField.getText());
		}
		else {
			filterInfo.setPattern(false);
		}
		//判断时间范围匹配是否选中
		if(timeCheckBox.isSelected())
		{
			filterInfo.setDate(true);
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
		else {
			filterInfo.setDate(false);
		}
		//判断文件大小范围匹配是否选中
		if(sizeCheckBox.isSelected())
		{
			filterInfo.setSize(true);
			filterInfo.setType(3);
			long[] unit = {1,1024,1024*1024,1024*1024*1024};	
			int i=0,a=0;
			switch ((String)minUnit.getValue()) {
			case "B":
				i=0;
				break;
			case "K":
				i=1;
				break;
			case "M":
				i=2;
				break;
			case "G":
				i=3;
				break;
			default:
				break;
			}
			switch ((String)maxUnit.getValue()) {
			case "B":
				a=0;
				break;
			case "K":
				a=1;
				break;
			case "M":
				a=2;
				break;
			case "G":
				a=3;
				break;
			default:
				break;
			}
			long minLong = Long.parseLong(minSizeField.getText())*unit[i];
			long maxLong = Long.parseLong(maxSizeField.getText())*unit[a];
			filterInfo.setSmallSize(minLong);
			filterInfo.setLargeSize(maxLong);
		}
		else {
			filterInfo.setSize(false);
		}
	}
	
	private void initButton()
	{
		maxSizeField.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("MaxnewValue:  "+newValue);
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
				isMinField = minSizeField.getText()!="";
				isMaxField = maxSizeField.getText()!="";
				isMinChoic = minUnit.getSelectionModel().getSelectedIndex()!=-1;
				isMaxChoic = maxUnit.getSelectionModel().getSelectedIndex()!=-1;
				if(isMinField&&isMaxField&&isMinChoic&&isMaxChoic)
					ensureButton.setDisable(false);
				else
					ensureButton.setDisable(true);
			}
			else {
				minSizeField.setDisable(true);
				maxSizeField.setDisable(true);
				minUnit.setDisable(true);
				maxUnit.setDisable(true);
			}
		});
	}
	
	public FilterDialog(FilterInfo fInfo) {
		// TODO Auto-generated constructor stub
		filterInfo = fInfo;
		
		this.setTitle("筛选器");
		ButtonType ensureButtonType = new ButtonType("确定", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(ensureButtonType, ButtonType.CANCEL);
		ensureButton = this.getDialogPane().lookupButton(ensureButtonType);
		minSizeField.setMaxSize(100, 20);
		maxSizeField.setMaxSize(100, 20);
		
		
		initButton();
		//初始化布局
		initPane();
		
		this.setResultConverter(dialogButton -> {
		    if (dialogButton == ensureButtonType) {
		    	updateFilterInfo();
		    }
		    return filterInfo;
		});
	}
}
