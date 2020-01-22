	package receiveSystem; 
	import java.io.IOException; 
	import signal.PCSignal; 
	import signal.Port;
	/**���l��N���X
	 * <PRE>
	 * �z�B�S�����{�b�g����ו����󂯎��
	 * </PRE>
	 * 
	 * <OL>
	 * <LI>public static void main(String[] args)
	 * <LI>public void getLug()
	 * </OL>
	 * @author bp17087
	 *
	 */
	
	public class Receiver { 
		
	/**
	 * ���l��	
	 */
	private static String receiverName; 
	
	/**
	 * ���l�Z��
	 */
	private static Integer receiverAddress; 
	
	 /**
	  * ���l��̃C���X�^���X�𐶐�����R���X�g���N�^
	  * ���l���Ǝ��l�Z����^���Ď��l��R���X�g���N�^������B
	  * @param ���l��
	  * @param ���l�Z��
	  */
	public Receiver(String name,Integer ad){ 
	receiverName = name; 
	receiverAddress = ad; 
	} 
	
	/**
	 * ���C����
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{ 
	Receiver r = new Receiver("saito", 3); 
	r.getLug(); 
	} 
	 
	/*public static void main(String[] args) { 
	Receiver r = new Receiver("saito",3);//�����ŏ���ς��� 
	try{ 
	while(true){ 
	r.getLug(); 
	} 
	}catch(IOException e){ 
	e.printStackTrace(); 
	} 
	}*/ 
	 	        
	
	/**
	 * �z�B�S�����{�b�g�ƒʐM���郁�\�b�h�ł���B�z�B�S�����{�b�g����ʐM���󂯎���āA
	 * ���l������l�Z�������������ǂ����̊m�F������B
	 * @throws IOException
	 */
	public void getLug() throws IOException{ 
	PCSignal sig1 = new PCSignal(); 
	sig1.waitSig(Port.RECEIVE); 
	sig1.getSig(); 
	sig1.sendSig("Exist"); 
	if(sig1.getSig().equals("false")) sig1.closeSig();    //false��10�b�ȓ��ɕԐM�ł��Ȃ����� 
	else{ 
	sig1.sendSig(receiverName); 
	sig1.sendSig(receiverAddress); 
	if(sig1.getSig().equals("false")) sig1.closeSig(); 
	else{ 
	sig1.getSig();     //�ו��󂯎�� 
	System.out.println("received."); 
	sig1.closeSig(); 
	} 
	} 
	}  
	 
	} 
