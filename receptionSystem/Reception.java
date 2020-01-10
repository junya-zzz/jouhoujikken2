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
	private DeliveryRecordList deliList;
	private Boundary boundary;

	private PCSignal signal;


	public Reception(){
		this.lugList = new ArrayList<Luggage>();
		this.deliList = new DeliveryRecordList();
		this.boundary = new Boundary();
		this.signal = new PCSignal();
	}
	/*�z�B�L�^��{���ɑ���
    public void sendDeliveryRecord() {

    }

	 */


	/*�������Ԃ�{���ɕ񍐂���*/
	public void sendShipTime() {
		try {
			signal.openSig("Headquarters");
			/**�z�B�L�^���X�g����T�[�`**/




			/**************/
			signal.sendSig(deliveryRecord);
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
			signal.openSig("CollectingRobot");
			String getMessage = (String)signal.getSig();
			if(getMessage.contentEquals(EXIST_LUGGAGE)) { //���{�b�g����̃��b�Z�[�W��������������
				if(!this.lugList.isEmpty()) { //�ו����X�g�ɉו�����������
					signal.sendSig(true);
					Luggage sendLug = this.lugList.remove(0);  //�ו����X�g����擪�̗v�f�����o���đ���
					signal.sendSig(sendLug);
					/******�n�����ו��ɑΉ�����z�B�L�^�ɔ������Ԃ�ǋL**/
					deliList.updateDeliveryRecord(sendLug.getLuggageID(), LuggageCondition.delivering, new Date());


				}
			}
			signal.closeSig();



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
			DeliveryRecord deliveryRecord = new DeliveryRecord(id, lug); //�ו��̔z�B�L�^����
			deliveryRecord.setReceiveTime(new Date()); //�z�B�L�^�Ɏ�t���Ԃ�ǉ�
			deliList.addDeliveryRecord(deliveryRecord); //�z�B�L�^���X�g�ɒǉ�
		} catch (IOException e){
			//��O
		}
	}

	private int setLuggageIDNum() { //�ו�ID��ݒ�
		idNum++;
		return idNum;
	}
}
