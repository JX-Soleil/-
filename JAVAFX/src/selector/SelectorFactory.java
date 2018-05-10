package selector;

import application.FilterInfo;

public class SelectorFactory {	
	//�����ʹ������ѡ����
	private static int type = 0;
	public Selector getSelector(FilterInfo filterInfo)
	{
		Selector selector = null;
		switch (filterInfo.getType()) {
		case 0:
			selector = null;
			break;
		case 1:
			selector = null;
			break;
		case 2:
			selector = new TimeSelector(filterInfo); 
			break;
		case 3:
			selector = null;
			break;
		default:
			break;
		}
		return selector;
	}
}
