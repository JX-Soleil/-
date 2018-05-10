package selector;

import application.FilterInfo;

public class SelectorFactory {	
	//�����ʹ������ѡ����
	private static int type = 0;
	public Selector getSelector(FilterInfo filterInfo)
	{
		Selector selector = null;
		//������������filterInfo��SelectFactory����϶����ߡ�
		//----��Ϊ��һ��int�����ݣ���Ϊѡ�񴴽���ͬ��selector��
		//���Ǿ����selector��Ҫ��ͬ����Ϣ�� ����������ֲ��ɻ�ȡ��������
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
