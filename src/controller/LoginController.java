package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.PickResult;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController {

	@FXML
	private TextField username;

	@FXML
	private TextField password;

	@FXML
	private Button signInBtn;
	
    @FXML
    private AnchorPane root;

	@FXML
	private ProgressIndicator pi = new ProgressIndicator();

	@FXML
	void signIn(ActionEvent event) throws IOException{

			if (username.getText().equals("admin") && password.getText().equals("admin")) {
				Parent root = FXMLLoader.load(getClass().getResource("../views/adminUI.fxml"));
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Undefined");
				alert.setHeaderText("Please check login and password");
				alert.show();
			}
		}


}
