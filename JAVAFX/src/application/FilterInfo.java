package application;

import java.util.Date;

public class FilterInfo {
	//字符串模式
	private String pattern;
	//时间范围
	private Date startDate;
	private Date endDate;
	//文件大小范围
	private long smallSize;
	private long largeSize;
	
	private boolean isPattern;
	private boolean isDate;
	private boolean isSize;
	
	private int type;
	
	public FilterInfo() {
		// TODO Auto-generated constructor stub
		isPattern = false;
		isDate = false;
		isSize = false;
		type = 0;
	}

	public int getType()
	{
		return type;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getSmallSize() {
		return smallSize;
	}

	public void setSmallSize(long smallSize) {
		this.smallSize = smallSize;
	}

	public long getLargeSize() {
		return largeSize;
	}

	public void setLargeSize(long largeSize) {
		this.largeSize = largeSize;
	}

	public boolean isPattern() {
		return isPattern;
	}

	public void setPattern(boolean isPattern) {
		this.isPattern = isPattern;
	}

	public boolean isDate() {
		return isDate;
	}

	public void setDate(boolean isDate) {
		this.isDate = isDate;
	}

	public boolean isSize() {
		return isSize;
	}

	public void setSize(boolean isSize) {
		this.isSize = isSize;
	}
}
