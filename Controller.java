package receptionSystem;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField clientName;

    @FXML
    private TextField receiverName;

    @FXML
    private TextField clientPhoneNum;

    @FXML
    private ChoiceBox<?> receiverAddress;

    @FXML
    private TextField luggageName;

    @FXML
    private Button registerButton;
    @FXML
    private Label resultLabel;

    @FXML
    void handleRegister(ActionEvent event) {
    	// OKボタンがクリックされた時の動作
    	String readClientName = clientName.getText();
    	String readReceiverName = receiverName.getText();
    	String readClientPhoneNum = clientPhoneNum.getText();
        String readAddress = (String) receiverAddress.getValue() ;
        String readLuggageName = luggageName.getText();

    }


    @FXML
    void initialize() {
        assert clientName != null : "fx:id=\"clientName\" was not injected: check your FXML file 'GUI.fxml'.";
        assert receiverName != null : "fx:id=\"receiverName\" was not injected: check your FXML file 'GUI.fxml'.";
        assert clientPhoneNum != null : "fx:id=\"clientPhoneNum\" was not injected: check your FXML file 'GUI.fxml'.";
        assert receiverAddress != null : "fx:id=\"receiverAddress\" was not injected: check your FXML file 'GUI.fxml'.";
        assert luggageName != null : "fx:id=\"luggageName\" was not injected: check your FXML file 'GUI.fxml'.";
        assert registerButton != null : "fx:id=\"registerButton\" was not injected: check your FXML file 'GUI.fxml'.";
        assert resultLabel != null : "fx:id=\"resultLabel\" was not injected: check your FXML file 'GUI.fxml'.";

    }
}
