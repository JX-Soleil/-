package application;

public class SortInfo {
	private int type;//标记排序类型，名称，时间，大小等
	private boolean upOrDown;//true为升序，false为降序
	public SortInfo() {
		// TODO Auto-generated constructor stub
		type = 0;
		upOrDown = true;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setOrder(boolean isUP) {
		this.upOrDown = isUP;
	}
	
	public boolean getOrder()
	{
		return upOrDown;
	}
}
