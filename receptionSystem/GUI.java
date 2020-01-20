package receptionSystem;

import javax.swing.JFrame;
import javax.swing.JTextField;

import recordSystem.DeliveryRecord;
import recordSystem.Luggage;
import recordSystem.RequestInformation;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class GUI extends JFrame{

	JTextField text1;
	JTextField text2;
	JTextField text3;
	JTextField text4;
	JTextField text5;
	JTextField text6;
	
	JTextField text7;
	JTextField text8;
	JTextField text9;
	JTextField text10;
	JTextField text11;
	JTextField text12;
	
	JLabel resultLabel;

	RequestInformation requestInformation = null;
	Reception reception;


	GUI(String title, Reception r){
		reception = r;
		setTitle(title);
		setBounds(100, 100, 500, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		text1 = new JTextField(30);
		text2 = new JTextField(30);
		text3 = new JTextField(30);
		text4 = new JTextField(30);
		text5 = new JTextField(30);
		text6 = new JTextField(10);
		
		text7 = new JTextField(10);
		text8 = new JTextField(30);
		text9 = new JTextField(30);
		text10 = new JTextField(30);
		text11 = new JTextField(30);
		text12 = new JTextField(30);
		
		JButton requestButton = new JButton("�˗�����");
		JButton trackButton = new JButton("�₢���킹��");
		JButton fixButton = new JButton("�ו������C��");
		requestButton.addActionListener(new RequestLuggage());
		trackButton.addActionListener(new TrackLuggage());
		fixButton.addActionListener(new FixedLuggage());
		resultLabel = new JLabel();

		Box requestBox = Box.createVerticalBox();
	    requestBox.setBorder(BorderFactory.createTitledBorder("�˗������̓t�H�[��"));
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(new JLabel("�˗��Җ�"));
		requestBox.add(text1);
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(new JLabel("���l��"));
		requestBox.add(text2);
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(new JLabel("�d�b�ԍ�"));
		requestBox.add(text3);
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(new JLabel("���l�Z��"));
		requestBox.add(text4);
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(new JLabel("�ו���"));
		requestBox.add(text5);
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(requestButton);
		
		Box trackBox = Box.createHorizontalBox();
	    trackBox.setBorder(BorderFactory.createTitledBorder("�ו��₢���킹�t�H�[��"));
	    trackBox.add(Box.createHorizontalStrut(10));
		trackBox.add(new JLabel("�z�B�L�^ID"));
	    trackBox.add(Box.createHorizontalStrut(10));
		trackBox.add(text6);
	    trackBox.add(Box.createHorizontalStrut(10));
		trackBox.add(trackButton);
		
		Box fixLugBox = Box.createVerticalBox();
	    fixLugBox.setBorder(BorderFactory.createTitledBorder("�˗����C���t�H�[��"));
	    fixLugBox.add(Box.createVerticalStrut(10));
		fixLugBox.add(new JLabel("�ו�ID"));
		fixLugBox.add(text7);
	    fixLugBox.add(Box.createVerticalStrut(10));
		fixLugBox.add(new JLabel("�˗��Җ�"));
		fixLugBox.add(text8);
	    fixLugBox.add(Box.createVerticalStrut(10));
		fixLugBox.add(new JLabel("���l��"));
		fixLugBox.add(text9);
	    fixLugBox.add(Box.createVerticalStrut(10));
		fixLugBox.add(new JLabel("�d�b�ԍ�"));
		fixLugBox.add(text10);
	    fixLugBox.add(Box.createVerticalStrut(10));
		fixLugBox.add(new JLabel("���l�Z��"));
		fixLugBox.add(text11);
	    fixLugBox.add(Box.createVerticalStrut(10));
		fixLugBox.add(new JLabel("�ו���"));
		fixLugBox.add(text12);
	    fixLugBox.add(Box.createVerticalStrut(10));
		fixLugBox.add(fixButton);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
		contentPane.add(requestBox);
		contentPane.add(trackBox);
		contentPane.add(fixLugBox);
		contentPane.add(resultLabel);
	}

	class RequestLuggage implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int receiverAddressNum;
			String clientName = text1.getText();
			String receiverName = text2.getText();
			String clientPhoneNum = text3.getText();
			String receiverAddress = text4.getText();
			String luggageName = text5.getText();
			if (clientName.length()==0||receiverName.length()==0||clientPhoneNum.length()==0||receiverAddress.length()==0||luggageName.length()==0) {
				resultLabel.setText("�S�ē��͂��Ă�������");
				return;
			}
			try {
				receiverAddressNum = Integer.parseInt(receiverAddress);
				if (receiverAddressNum < 0 || 9 < receiverAddressNum) {
					resultLabel.setText("���l�Z����0~9�Ŏw�肵�Ă�������");
					return;
				}
			} catch (NumberFormatException e1) {
				resultLabel.setText("���l�Z���͐����œ��͂��Ă�������");
				return;
			}
			requestInformation = new RequestInformation(clientName, receiverName, clientPhoneNum, receiverAddressNum);
			String result = reception.getLug(requestInformation, luggageName).toString();
			result = result.replace(System.getProperty("line.separator"), "<br>");
			result = "<html>" + result + "<html>";
			resultLabel.setText(result);
		}
	}
	
	class TrackLuggage implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int id = Integer.parseInt(text6.getText());
				DeliveryRecord record = reception.luggageTracking(id);
				if (record == null) {
					resultLabel.setText("�ו���������܂���");
				} else {
					String result = record.toString();
					result = result.replace(System.getProperty("line.separator"), "<br>");
					result = "<html>" + result + "<html>";
					resultLabel.setText(result);
				}
			} catch (NumberFormatException e1) {
				resultLabel.setText("�����œ��͂��Ă�������");
				return;
			}
		}
	}
	
	class FixedLuggage implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int receiverAddressNum;
			int luggageID = Integer.parseInt(text7.getText());
			String clientName = text8.getText();
			String receiverName = text9.getText();
			String clientPhoneNum = text10.getText();
			String receiverAddress = text11.getText();
			String luggageName = text12.getText();
			if (clientName.length()==0||receiverName.length()==0||clientPhoneNum.length()==0||receiverAddress.length()==0||luggageName.length()==0) {
				resultLabel.setText("�S�ē��͂��Ă�������");
				return;
			}
			try {
				receiverAddressNum = Integer.parseInt(receiverAddress);
				if (receiverAddressNum < 0 || 9 < receiverAddressNum) {
					resultLabel.setText("���l�Z����0~9�Ŏw�肵�Ă�������");
					return;
				}
			} catch (NumberFormatException e1) {
				resultLabel.setText("���l�Z���͐����œ��͂��Ă�������");
				return;
			}
			requestInformation = new RequestInformation(clientName, receiverName, clientPhoneNum, receiverAddressNum);
			reception.fixeLuggage(new Luggage(luggageID,luggageName,requestInformation));
			String result = "�C������";
			result = result.replace(System.getProperty("line.separator"), "<br>");
			result = "<html>" + result + "<html>";
			resultLabel.setText(result);
		}
	}
}