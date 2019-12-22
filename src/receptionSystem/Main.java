package receptionSystem;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root = (Pane)FXMLLoader.load(getClass().getResource("GUI.fxml"));
			Scene scene = new Scene(root,700,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle("Title(仮)");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
    private TextField clientName;

    @FXML
    private TextField receiverName;

    @FXML
    private TextField clientPhoneNum;

    @FXML
    private ChoiceBox<?> Prefecture;

    @FXML
    private TextField luggageName;

    @FXML
    private Button registerButton;

    @FXML
    private Label resultLabel;

    @FXML
    public void handleRegister(ActionEvent event) {
        // OKボタンがクリックされた時の動作
        String result = clientName.getText() ;
        resultLabel.setText(result);

    }

	public static void main(String[] args) {
		launch(args);
	}
}
