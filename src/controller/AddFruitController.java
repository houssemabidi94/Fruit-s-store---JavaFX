package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import application.Main;
import application.SqLiteConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Fruit;

public class AddFruitController {

    @FXML
    private AnchorPane ui;
    
	@FXML
	private ImageView homeBtn;

	@FXML
	private VBox chosenFruitCard;

	@FXML
	private Label fruitNameLable;

	@FXML
	private Label fruitPriceLabel;

	@FXML
	private ImageView fruitImg;

	@FXML
	private TextField fruitName;

	@FXML
	private TextField fruitPrice;

	@FXML
	private ColorPicker fruitColor;

	@FXML
	private Button btnChooseImg;

	@FXML
	private Button addFruit;
	
    @FXML
    private ProgressIndicator pI;
    
    @FXML
    private TextField fruitQty;
	
	final FileChooser fileChooser = new FileChooser();

	File file;

	private Image image;

	@FXML
	void handleChooseImg(ActionEvent event) {

		fileChooser.setTitle("my file chooser");

		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg"));

		file = fileChooser.showOpenDialog(null);
		System.out.println(file.getName());
		if (!file.exists()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Image error");
			alert.setHeaderText("File Image not choosen succeffully");
			alert.setContentText("Please choose an image file correctly !");
		}
	}

	public void setChosenFruit(Fruit fruit) {
		fruitNameLable.setText(fruit.getName());
		fruitPriceLabel.setText(Main.CURRENCY + fruit.getPrice());
		image = new Image(getClass().getResourceAsStream(fruit.getImgSrc()));
		fruitImg.setImage(image);
		chosenFruitCard
				.setStyle("-fx-background-color: #" + fruit.getColor() + ";\n" + "    -fx-background-radius: 30;");
	}

	@FXML
	void addNewFruit(ActionEvent event) throws IOException, InterruptedException {

		try {
			// set Fruit Item
			Fruit fruit = new Fruit();
			fruit.setName(fruitName.getText());
			fruit.setPrice(Double.parseDouble(fruitPrice.getText()));
			fruit.setColor(fruitColor.getValue().toString().substring(2));
			fruit.setQuantity(Integer.parseInt(fruitQty.getText()));

			// set Image
			String pathDirectory = file.getAbsolutePath();
			String pathDestination = "C:\\Users\\Houssem\\Documents\\workspace-spring-tool-suite-4-4.13.1.RELEASE\\Fruits Market\\src\\images\\"+file.getName();
			File sourceFile = new File(pathDirectory);
			File destFile = new File(pathDestination);
			//File file = copyImageFromTo(pathDirectory, pathDestination);
				destFile.createNewFile();
				copyFileUsingJava7Files(sourceFile, destFile);
							
			fruit.setImgSrc("/images/" + file.getName());

			// sql Connection
			SqLiteConnection.connector();
			SqLiteConnection sqLiteConnection = new SqLiteConnection();
			sqLiteConnection.insert(fruit);
			// Alert
			Thread.sleep(2000);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Successfully");
			alert.setHeaderText("Fruit added successfully");
			alert.show();
			setChosenFruit(fruit);
		} catch (Exception e) {
			String msgError = "";
			if (fruitName.getText() == "" || fruitName.getText() == " ")
				msgError += "- Please enter a fruit Name . \n";
			if (fruitPrice.getText() == "" || fruitPrice.getText() == " ")
				msgError += "- Please enter a fruit Price . \n";
			if (fruitColor.getValue() == null)
				msgError += "- Please enter a fruit Color . \n";
			if (file == null)
				msgError += "- Please enter a fruit Image . \n";
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setHeaderText("Fruit not Added");
			alert.setContentText(msgError);
			alert.show();
			e.printStackTrace();
		} 
	}

private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
    Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
}

	@FXML
	void goToHome(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../views/adminUI.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	@FXML
	void logOut(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../views/market.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}