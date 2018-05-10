package selector;

import application.FilterInfo;

public class SelectorFactory {	
	//标记是使用哪种选择器
	private static int type = 0;
	public Selector getSelector(FilterInfo filterInfo)
	{
		Selector selector = null;
		//这样做好像导致filterInfo和SelectFactory的耦合度升高。
		//----改为传一个int型数据，作为选择创建不同的selector？
		//但是具体的selector需要不同的信息来 创建，这个又不可获取。。。。
		switch (filterInfo.getType()) {
		case 0:
			selector = null;
			break;
		case 1:
			selector = new NameSelector(filterInfo);
			break;
		case 2:
			selector = new TimeSelector(filterInfo); 
			break;
		case 3:
			selector = new SizeSelector(filterInfo);
			break;
		default:
			break;
		}
		return selector;
	}
}
