package application;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class SortDialog extends Dialog<SortInfo>{
	
	private SortInfo sortInfo;
	
	CheckBox nameCheckBox = new CheckBox("按文件名排序");
	CheckBox timeCheckBox = new CheckBox("按文件时间排序");
	CheckBox sizeCheckBox = new CheckBox("按文件大小排序");
	ChoiceBox order = new ChoiceBox(FXCollections.observableArrayList(
		    "升序", "降序"));
	
	public SortDialog(SortInfo sInfo) {
		// TODO Auto-generated constructor stub
		sortInfo = sInfo;
		
		this.setTitle("排序器");
		ButtonType ensureButtonType = new ButtonType("确定", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(ensureButtonType, ButtonType.CANCEL);
		
		//初始化CheckBox
		initBox();
		
		//初始化布局
		initPane();
		
		this.setResultConverter(dialogButton -> {
		    if (dialogButton == ensureButtonType) {
		    	updateSortInfo();
		    }
		    return sortInfo;
		});
	}

	private void initBox() {
		if(sortInfo.getType()==0)
			nameCheckBox.setSelected(true);
		else if(sortInfo.getType()==1)
			timeCheckBox.setSelected(true);
		else if(sortInfo.getType()==2)
			sizeCheckBox.setSelected(true);
		
		if(sortInfo.getOrder())
			order.setValue("升序");
		else 
			order.setValue("降序");
		
		nameCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(nameCheckBox.isSelected())
				{
					if(timeCheckBox.isSelected())
						timeCheckBox.setSelected(false);
					if(sizeCheckBox.isSelected())
						sizeCheckBox.setSelected(false);
				}
			}
		});
		timeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(timeCheckBox.isSelected())
				{
					if(nameCheckBox.isSelected())
						nameCheckBox.setSelected(false);
					if(sizeCheckBox.isSelected())
						sizeCheckBox.setSelected(false);
				}
			}
		});
		sizeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(sizeCheckBox.isSelected())
				{
					if(timeCheckBox.isSelected())
						timeCheckBox.setSelected(false);
					if(nameCheckBox.isSelected())
						nameCheckBox.setSelected(false);
				}
			}
		});
	}

	private void updateSortInfo() {
		//这里应该先要判断order的getValue()的返回值是否为空，可能什么都没选
		if((String)order.getValue()=="升序")
			sortInfo.setOrder(true);
		else if((String)order.getValue()=="降序")
			sortInfo.setOrder(false);
		
		if(nameCheckBox.isSelected())
			sortInfo.setType(0);
		else if(timeCheckBox.isSelected())
			sortInfo.setType(1);
		else if(sizeCheckBox.isSelected())
			sortInfo.setType(2);	
	}

	private void initPane() {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		grid.add(nameCheckBox, 0, 0);
		grid.add(timeCheckBox, 1, 0);
		grid.add(sizeCheckBox, 2, 0);
		grid.add(order, 0, 1);
		
		this.getDialogPane().setContent(grid);
	}
}
