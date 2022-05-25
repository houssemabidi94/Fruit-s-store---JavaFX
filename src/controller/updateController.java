package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.Main;
import application.SqLiteConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Fruit;

public class updateController {

	@FXML
	private AnchorPane ui;

	@FXML
	private VBox chosenFruitCard;

	@FXML
	private Label fruitNameLable;

	@FXML
	private Label fruitPriceLabel;

	@FXML
	private ImageView fruitImg;

	@FXML
	private Label logOutBtn;

	@FXML
	private ImageView homeBtn;

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
	private Button updateFruit;

	@FXML
	private HBox addNewFruit;
	
    @FXML
    private TextField fruitQty;
    
	private Image image;
	private Color color;
	Fruit fruit;

	AdminController adminController = new AdminController();

	@FXML
	void addNewFruit(MouseEvent event) throws IOException {
		adminController.addFruit(event);
	}

	@FXML
	void goToHome(MouseEvent event) throws IOException {
		adminController.goToHome(event);
	}

	@FXML
	void logOut(MouseEvent event) throws IOException {
		adminController.logOut(event);
	}

	void getData(Fruit fruit) {
		fruitName.setText(fruit.getName());
		fruitPrice.setText(Double.toString(fruit.getPrice()));
		fruitColor.setValue(color.valueOf(fruit.getColor()));
	}

	public void setChosenFruit(Fruit fruit) {
		fruitNameLable.setText(fruit.getName());
		fruitPriceLabel.setText(fruit.getPrice() + Main.CURRENCY);
		image = new Image(getClass().getResourceAsStream(fruit.getImgSrc()));
		fruitImg.setImage(image);
		chosenFruitCard
				.setStyle("-fx-background-color: #" + fruit.getColor() + ";\n" + "    -fx-background-radius: 30;");
	}

	Fruit setFruit(Fruit fruit) {
		return this.fruit = fruit;
	}

	@FXML
	void updateFruit(ActionEvent event) throws SQLException {
		try {
			PreparedStatement st = SqLiteConnection.connector().prepareStatement(
					"UPDATE Fruit SET f_name = ?, f_image = ?, price = ? , f_color = ? WHERE f_id = ?");

			st.setString(1, fruitName.getText());
			st.setString(2, this.fruit.getImgSrc());
			st.setDouble(3, Double.parseDouble(fruitPrice.getText()));
			st.setString(4, fruitColor.getValue().toString().substring(2));
			st.setLong(5, this.fruit.getId());
			st.executeUpdate();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Successfully");
			alert.setHeaderText("Fruit updated successfully");
			alert.show();
			Fruit fruit = new Fruit(this.fruit.getId(), fruitName.getText(), this.fruit.getImgSrc(),
					Double.parseDouble(fruitPrice.getText()), fruitColor.getValue().toString().substring(2),Integer.parseInt(fruitQty.getText()));
			setChosenFruit(fruit);
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setHeaderText("Fruit not Updated !");
			alert.show();
		}
	}
}
