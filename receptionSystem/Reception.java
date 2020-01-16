package receptionSystem;

import recordSystem.*;
import signal.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import headquartersSystem.Headquarters;




public class Reception extends Thread{
	public static final String EXIST_LUGGAGE = "existLuggage";

	private ArrayList<Luggage> lugList;
	//private DeliveryRecordList deliList;
	//private ArrayDeque<DeliveryRecord>; = new ArrayDeque<DeliveryRecord>();
	private Boundary boundary;

	private PCSignal signal;

	public static void main(String[] args) {
		// �e�X�g�p
		Reception reception = new Reception();
		reception.start();
		while (true) {
			reception.choseFunction();
		}
	}
	
	private void choseFunction() {
		try {
			PCSignal sig = new PCSignal();
			sig.waitSig(Port.RECEPTION);
			int flag = (int) sig.getSig();
			if (flag == 0) {
				sendLug(sig);
			} else if (flag == 1) {
				getIsDelivery(sig);
			}
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Reception(){
		this.lugList = new ArrayList<Luggage>();
		//this.deliList = new DeliveryRecordList();
		this.boundary = new Boundary();
		this.signal = new PCSignal();
	}

	
	// �ו����˗��l����󂯎�������Ƃ�{���ɕ񍐂���
	public void sendReceiptTime(DeliveryRecord d) {
		try {
			signal.openSig(Port.HEAD);
			/**�z�B�L�^���X�g����T�[�`**/
			signal.sendSig(0);
			signal.sendSig(d); 



			/**************/
			//signal.sendSig(deliveryRecord);
			signal.closeSig();

		}catch(Exception e) {
			//��O����
			//
			//

		}
	}


	/*�ו������W�S�����{�b�g�ɓn�������Ƃ�{���ɕ񍐂���*/
	public void sendShipTime(Luggage lug) {
		try {
			signal.openSig(Port.HEAD);
			/**�z�B�L�^���X�g����T�[�`**/
			signal.sendSig(1);
			signal.sendSig(lug.getLuggageID()); 
			signal.sendSig(LuggageCondition.shipping); 
			signal.sendSig(new Date()); 


			/**************/
			//signal.sendSig(deliveryRecord);
			signal.closeSig();

		}catch(Exception e) {
			//��O����
			//
			//

		}
	}


	/*���p���Ƃ̈��n�����ʂ𓾂�*/
	public void getIsDelivery(PCSignal sig) {
		try {
			boolean isDelivery = (boolean) sig.getSig();
			if (!isDelivery) {
				lugList.add((Luggage) sig.getSig());
			}
			sig.closeSig();
		} catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}


	/*�ו������W�S�����{�b�g�ɓn��*/
	public void sendLug(PCSignal sig) {
		try {
			Luggage sendLug = null;
			sig.waitSig(Port.RECEPTION);
			String getMessage = (String)sig.getSig();
			if(getMessage.contentEquals(EXIST_LUGGAGE)) { //���{�b�g����̃��b�Z�[�W��������������
				if(!this.lugList.isEmpty()) { //�ו����X�g�ɉו�����������
					sig.sendSig(true);
					sendLug = this.lugList.remove(0);  //�ו����X�g����擪�̗v�f�����o���đ���
					System.out.println("send lug:" + sendLug);
					sig.sendSig(sendLug);
					/******�n�����ו��ɑΉ�����z�B�L�^�ɔ������Ԃ�ǋL**/
					//deliList.updateDeliveryRecord(sendLug.getLuggageID(), LuggageCondition.delivering, new Date());
				} else {
					System.out.println("no lug.");
					sig.sendSig(false);
				}
			}
			sig.closeSig();
			if (sendLug != null) {
				sendShipTime(sendLug/*new DeliveryRecord(sendLug.getLuggageID(), sendLug)*/);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public int idNum=0;
	
	// ����I��
	@Override
	public void run() {
		while(true) {
			String input = null;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("�����I��");
				System.out.println("�˗� : 0, �ו���Ԋm�F : 1 -> ");
				input = br.readLine();
			} catch (IOException e){
				e.printStackTrace();
				System.exit(1);
			}
			if (input.equals("0")){
				getLug();
			} else if (input.equals("1")){
				luggageTracking();
			}
		}
	}
	

	/*�ו����˗��l����󂯎��*/
	public void getLug() {
		try {
			RequestInformation info = this.boundary.inputReqInfo(); //�o�E���_������z�B�������
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("�ו��̖��O����� -> ");
			String luggageName = br.readLine();
			int id = setLuggageIDNum();
			Luggage lug = new Luggage(id,luggageName, info);                      /****ID,Amount�ǉ�****/
			lugList.add(lug);   //�ו����X�g�ɒǉ�
			//DeliveryRecord deliveryRecord = new DeliveryRecord(id, lug); //�ו��̔z�B�L�^����
			//deliveryRecord.setReceiveTime(new Date()); //�z�B�L�^�Ɏ�t���Ԃ�ǉ�
			//deliList.addDeliveryRecord(deliveryRecord); //�z�B�L�^���X�g�ɒǉ�
			sendReceiptTime(new DeliveryRecord(lug.getLuggageID(),lug));
		} catch (IOException e){
			//��O
		}
	}
	
	// �ו��̏�Ԃ�{���ɖ₢���킹�Ď擾���A�\������
	private void luggageTracking() {
		int id;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("�ו�ID����� -> ");
			id = Integer.parseInt(br.readLine());
			signal.openSig(Port.HEAD);
			signal.sendSig(Headquarters.TRACK_LUGGAGE); //�ו��₢���킹
			signal.sendSig(id);
			DeliveryRecord dr = (DeliveryRecord)signal.getSig();
			if (dr == null) {
				System.out.println("�ו���������܂���");
			} else {
				System.out.println(dr);
			}
			signal.closeSig();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (NumberFormatException e) {
			System.out.println("��������͂��Ă�������");
		}
	}

	private int setLuggageIDNum() { //�ו�ID��ݒ�
		idNum++;
		return idNum;
	}
}
