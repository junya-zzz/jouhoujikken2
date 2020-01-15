package receptionSystem;

import recordSystem.*;
import signal.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;




public class Reception {
	public static final String EXIST_LUGGAGE = "existLuggage";

	private ArrayList<Luggage> lugList;
	//private DeliveryRecordList deliList;
	//private ArrayDeque<DeliveryRecord>; = new ArrayDeque<DeliveryRecord>();
	private Boundary boundary;

	private PCSignal signal;

	public static void main(String[] args) {
		// �e�X�g�p
		Reception reception = new Reception();
		reception.getLug();
		reception.sendLug();
	}

	public Reception(){
		this.lugList = new ArrayList<Luggage>();
		//this.deliList = new DeliveryRecordList();
		this.boundary = new Boundary();
		this.signal = new PCSignal();
	}
	/*�z�B�L�^��{���ɑ���
    public void sendDeliveryRecord() {

    }

	 */
	
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


	/*�������Ԃ�{���ɕ񍐂���*/
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
	public void getIsDelivery() {

	}


	/*�ו������W�S�����{�b�g�ɓn��*/
	public void sendLug() {
		try {
			Luggage sendLug = null;
			System.out.println("send lug.");
			signal.waitSig(Port.RECEPTION);
			String getMessage = (String)signal.getSig();
			if(getMessage.contentEquals(EXIST_LUGGAGE)) { //���{�b�g����̃��b�Z�[�W��������������
				if(!this.lugList.isEmpty()) { //�ו����X�g�ɉו�����������
					signal.sendSig(true);
					sendLug = this.lugList.remove(0);  //�ו����X�g����擪�̗v�f�����o���đ���
					System.out.println("send lug:" + sendLug);
					signal.sendSig(sendLug);
					/******�n�����ו��ɑΉ�����z�B�L�^�ɔ������Ԃ�ǋL**/
					//deliList.updateDeliveryRecord(sendLug.getLuggageID(), LuggageCondition.delivering, new Date());
					
				} else {
					signal.sendSig(false);
				}
			}
			signal.closeSig();
			sendShipTime(sendLug/*new DeliveryRecord(sendLug.getLuggageID(), sendLug)*/);
		}catch(Exception e) {
			//��O����
		}
	}

	public int idNum=0;

	/*�ו����˗��l����󂯎��*/
	public void getLug() {
		try {
			RequestInformation info = this.boundary.inputReqInfo(); //�o�E���_������z�B�������
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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

	private int setLuggageIDNum() { //�ו�ID��ݒ�
		idNum++;
		return idNum;
	}
}
