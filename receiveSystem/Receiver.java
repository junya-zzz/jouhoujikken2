	package receiveSystem; 
	import java.io.IOException; 
	import signal.PCSignal; 
	import signal.Port;
	/**受取人宅クラス
	 * <PRE>
	 * 配達担当ロボットから荷物を受け取る
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
	 * 受取人名	
	 */
	private static String receiverName; 
	
	/**
	 * 受取人住所
	 */
	private static Integer receiverAddress; 
	
	 /**
	  * 受取人宅のインスタンスを生成するコンストラクタ
	  * 受取人名と受取人住所を与えて受取人宅コンストラクタをつくる。
	  * @param 受取人名
	  * @param 受取人住所
	  */
	public Receiver(String name,Integer ad){ 
	receiverName = name; 
	receiverAddress = ad; 
	} 
	
	/**
	 * メイン文
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{ 
	Receiver r = new Receiver("saito", 3); 
	r.getLug(); 
	} 
	 
	/*public static void main(String[] args) { 
	Receiver r = new Receiver("saito",3);//ここで情報を変える 
	try{ 
	while(true){ 
	r.getLug(); 
	} 
	}catch(IOException e){ 
	e.printStackTrace(); 
	} 
	}*/ 
	 	        
	
	/**
	 * 配達担当ロボットと通信するメソッドである。配達担当ロボットから通信を受け取って、
	 * 受取人名や受取人住所が正しいかどうかの確認をする。
	 * @throws IOException
	 */
	public void getLug() throws IOException{ 
	PCSignal sig1 = new PCSignal(); 
	sig1.waitSig(Port.RECEIVE); 
	sig1.getSig(); 
	sig1.sendSig("Exist"); 
	if(sig1.getSig().equals("false")) sig1.closeSig();    //false→10秒以内に返信できなかった 
	else{ 
	sig1.sendSig(receiverName); 
	sig1.sendSig(receiverAddress); 
	if(sig1.getSig().equals("false")) sig1.closeSig(); 
	else{ 
	sig1.getSig();     //荷物受け取り 
	System.out.println("received."); 
	sig1.closeSig(); 
	} 
	} 
	}  
	 
	} 
