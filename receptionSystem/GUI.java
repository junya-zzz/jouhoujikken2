package receptionSystem;

import javax.swing.JFrame;
import javax.swing.JTextField;

import recordSystem.DeliveryRecord;
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
		JButton requestButton = new JButton("依頼する");
		JButton trackButton = new JButton("問い合わせる");
		requestButton.addActionListener(new RequestLuggage());
		trackButton.addActionListener(new TrackLuggage());
		resultLabel = new JLabel();

		Box requestBox = Box.createVerticalBox();
	    requestBox.setBorder(BorderFactory.createTitledBorder("依頼情報入力フォーム"));
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(new JLabel("依頼者名"));
		requestBox.add(text1);
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(new JLabel("受取人名"));
		requestBox.add(text2);
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(new JLabel("電話番号"));
		requestBox.add(text3);
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(new JLabel("受取人住所"));
		requestBox.add(text4);
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(new JLabel("荷物名"));
		requestBox.add(text5);
	    requestBox.add(Box.createVerticalStrut(10));
		requestBox.add(requestButton);
		
		Box trackBox = Box.createHorizontalBox();
	    trackBox.setBorder(BorderFactory.createTitledBorder("荷物問い合わせフォーム"));
	    trackBox.add(Box.createHorizontalStrut(10));
		trackBox.add(new JLabel("配達記録ID"));
	    trackBox.add(Box.createHorizontalStrut(10));
		trackBox.add(text6);
	    trackBox.add(Box.createHorizontalStrut(10));
		trackBox.add(trackButton);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
		contentPane.add(requestBox);
		contentPane.add(trackBox);
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
				resultLabel.setText("全て入力してください");
				return;
			}
			try {
				receiverAddressNum = Integer.parseInt(receiverAddress);
				if (receiverAddressNum < 0 || 9 < receiverAddressNum) {
					resultLabel.setText("受取人住所は0~9で指定してください");
					return;
				}
			} catch (NumberFormatException e1) {
				resultLabel.setText("受取人住所は数字で入力してください");
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
					resultLabel.setText("荷物が見つかりません");
				} else {
					String result = record.toString();
					result = result.replace(System.getProperty("line.separator"), "<br>");
					result = "<html>" + result + "<html>";
					resultLabel.setText(result);
				}
			} catch (NumberFormatException e1) {
				resultLabel.setText("数字で入力してください");
				return;
			}
		}
	}
}