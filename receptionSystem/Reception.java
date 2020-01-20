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
	private int idNum=0;

	private PCSignal signal;

	public static void main(String[] args) {
		/*
		Reception reception = new Reception();
		reception.start();
		while (true) {
			reception.choseFunction();
		}
		 */
		Reception reception = new Reception();
		GUI gui = new GUI("delivery system", reception);
		gui.setVisible(true);
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
				Luggage receiveLuggage = (Luggage) sig.getSig();
				lugList.add(receiveLuggage);
				sig.closeSig();
				sig.openSig(Port.HEAD);
				sig.sendSig(1);
				sig.sendSig(receiveLuggage.getLuggageID());
				sig.sendSig(LuggageCondition.relay_absence);
				sig.sendSig(null);
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


	/*�ו����˗��l����󂯎��*/
	public Luggage getLug(RequestInformation requestInformation, String luggageName) {
		int id = setLuggageIDNum();
		Luggage lug = new Luggage(id,luggageName, requestInformation);                      /****ID,Amount�ǉ�****/
		lugList.add(lug);   //�ו����X�g�ɒǉ�
		//DeliveryRecord deliveryRecord = new DeliveryRecord(id, lug); //�ו��̔z�B�L�^����
		//deliveryRecord.setReceiveTime(new Date()); //�z�B�L�^�Ɏ�t���Ԃ�ǉ�
		//deliList.addDeliveryRecord(deliveryRecord); //�z�B�L�^���X�g�ɒǉ�
		sendReceiptTime(new DeliveryRecord(lug.getLuggageID(),lug));
		return lug;
	}

	// �ו��̏�Ԃ�{���ɖ₢���킹�Ď擾���A�\������
	public DeliveryRecord luggageTracking(int id) {
		// int id;
		DeliveryRecord dr = null;
		try {
			signal.openSig(Port.HEAD);
			signal.sendSig(Headquarters.TRACK_LUGGAGE); //�ו��₢���킹
			signal.sendSig(id);
			dr = (DeliveryRecord)signal.getSig();
			signal.closeSig();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return dr;
	}

	private int setLuggageIDNum() { //�ו�ID��ݒ�
		idNum++;
		return idNum;
	}
}
