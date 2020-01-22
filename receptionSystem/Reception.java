package receptionSystem;

import recordSystem.*;
import signal.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import headquartersSystem.Headquarters;

/** ��z��t���N���X
 * <PRE>
 * �˗��l����ו����󂯎��B
 * ���W�S�����{�b�g�A�{���ƒʐM����
 * </PRE>
 * <OL>
 * 	<LI>public static void main(String[] args)
 *  <LI>private void choseFunction()
 *  <LI>public void sendReceiptTime(DeliveryRecord d)
 *  <LI>public void sendShipTime(Luggage lug)
 *  <LI>public void getIsDelivery(PCSignal sig)
 *  <LI>public void sendLug(PCSignal sig)
 *  <LI>public Luggage getLug(RequestInformation requestInformation, String luggageName)
 *  <LI>public DeliveryRecord luggageTracking(int id) 
 *  <LI>private int setLuggageIDNum() 
 * </OL>
 * @author
 * @version 1.0
 */




public class Reception extends Thread{
	/**
	 * 
	 */
	public static final String EXIST_LUGGAGE = "existLuggage";

	/**
	 * �ו����X�g
	 */
	private ArrayList<Luggage> lugList;
	//private DeliveryRecordList deliList;
	//private ArrayDeque<DeliveryRecord>; = new ArrayDeque<DeliveryRecord>();
	/**
	 * �ו�ID
	 */
	private int idNum=0;
	/**
	 * �ʐM�V�O�i��
	 */
	private PCSignal signal;
	/**
	 * main���\�b�h
	 * @param args �R�}���h���C������
	 * 
	 */
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

	/**
	 * ���W�S�����{�b�g�ɉו��𑗂郁�\�b�h���z�B���ʂ̃��\�b�h�ǂ�����Ăяo�������肷�郁�\�b�h
	 * 
	 */
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
	/**
	 * ��z��t���N���X�̃C���X�^���X�𐶐�����R���X�g���N�^
	 */
	public Reception(){
		this.lugList = new ArrayList<Luggage>();
		this.signal = new PCSignal();
	}


	/**
	 *  �ו����˗��l����󂯎�������Ƃ�{���ɕ񍐂���
	 * @param d �z�B�L�^
	 * 
	 */
	public void sendReceiptTime(DeliveryRecord d) {
		try {
			signal.openSig(Port.HEAD);
			signal.sendSig(0);
			signal.sendSig(d); 
			signal.closeSig();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 * �ו������W�S�����{�b�g�ɓn�������Ƃ�{���ɕ񍐂���
	 *@param lug �ו�
	 * 
	 */
	public void sendShipTime(Luggage lug) {
		try {
			signal.openSig(Port.HEAD);
			signal.sendSig(1);
			signal.sendSig(lug.getLuggageID()); 
			signal.sendSig(LuggageCondition.shipping); 
			signal.sendSig(new Date()); 
			signal.closeSig();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 * ���p���Ƃ̈��n�����ʂ𓾂�
	 * @param sig PCSignal
	 * 
	 */
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


	/**
	 * �ו������W�S�����{�b�g�ɓn��
	 * @param sig PCSignal
	 */
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
				} else {
					System.out.println("no lug.");
					sig.sendSig(false);
				}
			}
			sig.closeSig();
			if (sendLug != null) {
				sendShipTime(sendLug);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 * �ו����˗��l����󂯎��
	 * @param requestInformation �˗����
	 * @param luggageName�@�ו��̖��O
	 */
	public Luggage getLug(RequestInformation requestInformation, String luggageName) {
		int id = setLuggageIDNum();
		Luggage lug = new Luggage(id,luggageName, requestInformation);                      /****ID,Amount�ǉ�****/
		lugList.add(lug);   //�ו����X�g�ɒǉ�
		sendReceiptTime(new DeliveryRecord(lug.getLuggageID(),lug));
		return lug;
	}

	/**
	 * �ו��̏�Ԃ�{���ɖ₢���킹�Ď擾���A�\������
	 * @param id �ו�ID
	 */
	public DeliveryRecord luggageTracking(int id) {
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

	/**
	 * �ו�ID��ݒ�
	 * 
	 */
	private int setLuggageIDNum() { //�ו�ID��ݒ�
		idNum++;
		return idNum;
	}
}
