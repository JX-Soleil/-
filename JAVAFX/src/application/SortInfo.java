package application;

public class SortInfo {
	private int type;//����������ͣ����ƣ�ʱ�䣬��С��
	private boolean upOrDown;//trueΪ����falseΪ����
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
