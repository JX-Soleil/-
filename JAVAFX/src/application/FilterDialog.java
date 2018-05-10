package application;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	
	CheckBox nameCheckBox = new CheckBox("�ļ���ƥ��");
	CheckBox timeCheckBox = new CheckBox("�ļ�ʱ��ƥ��");
	CheckBox sizeCheckBox = new CheckBox("�ļ���Сƥ��");
	 
	TextField patternField = new TextField();
	 
	DatePicker startDatePicker = new DatePicker();
	DatePicker endDatePicker = new DatePicker();
	 
	TextField minSizeField = new TextField();
	
	TextField maxSizeField = new TextField();
	
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
	
	//��ʼ��pane,��һЩ��������
	private void initPane()
	{
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		 
		grid.add(nameCheckBox, 0, 0);
		 
		grid.add(new Label("�ַ���ģʽ"), 0, 1);
		grid.add(patternField, 1, 1);
		 
		grid.add(timeCheckBox, 0, 2);
		 
		grid.add(new Label("��ʼʱ��"), 0, 3);
		grid.add(startDatePicker, 1, 3);
		grid.add(new Label("����ʱ��"), 2, 3);
		grid.add(endDatePicker, 3, 3);
		 
		grid.add(sizeCheckBox, 0, 4);
		 
		grid.add(new Label("�ļ���С����"), 0, 5);
		grid.add(minSizeField, 0, 6);
		grid.add(minUnit, 1, 6);
		grid.add(new Label("�ļ���С����"), 2, 5);
		grid.add(maxSizeField, 2, 6);
		grid.add(maxUnit, 3, 6);
		this.getDialogPane().setContent(grid);
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
				ensureButton.setDisable(true);
			}	
			else {
				isPatternField = true;
				ensureButton.setDisable(false);
			}
		});
		nameCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue)
			{
				timeCheckBox.setSelected(false);
				sizeCheckBox.setSelected(false);
				
				patternField.setDisable(false);
				if(isPatternField)
					ensureButton.setDisable(false);
				else
					ensureButton.setDisable(true);
			}
			else {
				patternField.setDisable(true);
				ensureButton.setDisable(false);
			}
		});
		//�ļ��޸�ʱ�䲿��
		startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!=null)
				isStartPicker = true;
			else
				isStartPicker = false;
			if(isStartPicker&&isEndPicker)
				ensureButton.setDisable(false);
			else
				ensureButton.setDisable(true);
				
		});
		endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!=null)
				isEndPicker = true;
			else
				isEndPicker = false;
			if(isStartPicker&&isEndPicker)
				ensureButton.setDisable(false);
			else
				ensureButton.setDisable(true);
		});
		timeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue) {
				nameCheckBox.setSelected(false);
				sizeCheckBox.setSelected(false);
				
				startDatePicker.setDisable(false);
				endDatePicker.setDisable(false);
				if(isStartPicker&&isEndPicker)
					ensureButton.setDisable(false);
				else
					ensureButton.setDisable(true);
			}
			else {
				startDatePicker.setDisable(true);
				endDatePicker.setDisable(true);
				ensureButton.setDisable(false);
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
					ensureButton.setDisable(false);
				else
					ensureButton.setDisable(true);
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
					ensureButton.setDisable(false);
				else
					ensureButton.setDisable(true);
		    }
		});	
		minUnit.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(!isMinChoic&&newValue!=null)
				isMinChoic = true;
			if(isMinField&&isMaxField&&isMinChoic&&isMaxChoic)
				ensureButton.setDisable(false);
			else
				ensureButton.setDisable(true);
		});
		maxUnit.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(!isMaxChoic&&newValue!=null)
				isMaxChoic = true;
			if(isMinField&&isMaxField&&isMinChoic&&isMaxChoic)
				ensureButton.setDisable(false);
			else
				ensureButton.setDisable(true);
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
					ensureButton.setDisable(false);
				else
					ensureButton.setDisable(true);
			}
			else {
				minSizeField.setDisable(true);
				maxSizeField.setDisable(true);
				minUnit.setDisable(true);
				maxUnit.setDisable(true);
				ensureButton.setDisable(false);
			}
		});
	}
	
	public FilterDialog(FilterInfo fInfo) {
		// TODO Auto-generated constructor stub
		filterInfo = fInfo;
		
		this.setTitle("ɸѡ��");
		ButtonType ensureButtonType = new ButtonType("ȷ��", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(ensureButtonType, ButtonType.CANCEL);
		ensureButton = this.getDialogPane().lookupButton(ensureButtonType);
//		ensureButton.setDisable(true);
		minSizeField.setMaxSize(100, 20);
		maxSizeField.setMaxSize(100, 20);
		
		initButton();
		//��ʼ������
		initPane();
		
		this.setResultConverter(dialogButton -> {
		    if (dialogButton == ensureButtonType) {
		    	updateFilterInfo();
		    }
		    return filterInfo;
		});
	}
}
