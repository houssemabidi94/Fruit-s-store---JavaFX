package controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.server.LoaderHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import application.MyListener;
import application.SqLiteConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Fruit;

public class AdminController implements Initializable {

	@FXML
	private TextField searchTxt;

	@FXML
	private Button searchBtn;

	@FXML
	private VBox chosenFruitCard;

	@FXML
	private Label fruitNameLabel;

	@FXML
	private Label fruitPriceLabel;

	@FXML
	private ImageView fruitImg;

	@FXML
	private Button deleteBtn;

	@FXML
	private HBox addBtn;

	@FXML
	private ScrollPane scroll;

	@FXML
	private GridPane grid;

	@FXML
	private Label logOutBtn;

	@FXML
	private Button updateBtn;
	
	@FXML
	private HBox viewStatsBtn;

	private List<Fruit> fruits = new ArrayList<>();

	private SqLiteConnection sqLiteConnection = new SqLiteConnection();

	MarketController marketController = new MarketController();

	MyListener myListener;

	private Image image;

	private Fruit fruit = new Fruit();

	@FXML
	void addFruit(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../views/addFruitForm.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void deleteFruit(ActionEvent event) {

		try {
			Connection con = SqLiteConnection.connector();
			Statement st = con.createStatement();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Delete");
			alert.setHeaderText("Are you sure you want Delete it ?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				st.executeUpdate("Delete from Fruit where f_name='" + fruitNameLabel.getText() + "'");
				fruits.remove(fruitNameLabel.getText());
				initUI(fruits);
				Thread.sleep(1000);
				refresh(event);
				System.out.println(fruits.size());
				// Alert
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Successfully");
				alert2.setHeaderText("Fruit deleted successfully");
				alert2.show();
			} else {
				// ... user chose CANCEL or closed the dialog
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void searchFruit(ActionEvent event) {

	}

	public Fruit setChosenFruit(Fruit fruit) {
		fruitNameLabel.setText(fruit.getName());
		fruitPriceLabel.setText(fruit.getPrice() + Main.CURRENCY);
		image = new Image(getClass().getResourceAsStream(fruit.getImgSrc()));
		fruitImg.setImage(image);
		chosenFruitCard
				.setStyle("-fx-background-color: #" + fruit.getColor() + ";\n" + "    -fx-background-radius: 30;");
		return fruit;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			fruits = sqLiteConnection.retrieveData(SqLiteConnection.connector());

		} catch (Exception e) {
			e.printStackTrace();
		}
		initUI(fruits);
	}

	@FXML
	void logOut(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../views/market.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void initUI(List<Fruit> fruits) {
		if (fruits.size() > 0) {
			setChosenFruit(fruits.get(0));
			myListener = new MyListener() {
				@Override
				public void onClickListener(Fruit fruit) {
					setChosenFruit(fruit);
				}
			};

			int column = 0;
			int row = 1;
			try {
				for (int i = 0; i < fruits.size(); i++) {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/views/item.fxml"));
					AnchorPane anchorPane = fxmlLoader.load();

					ItemController itemController = fxmlLoader.getController();
					itemController.setData(fruits.get(i), myListener);

					if (column == 3) {
						column = 0;
						row++;
					}

					grid.add(anchorPane, column++, row); // (child,column,row)
					// set grid width
					grid.setMinWidth(Region.USE_COMPUTED_SIZE);
					grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
					grid.setMaxWidth(Region.USE_PREF_SIZE);

					// set grid height
					grid.setMinHeight(Region.USE_COMPUTED_SIZE);
					grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
					grid.setMaxHeight(Region.USE_PREF_SIZE);

					GridPane.setMargin(anchorPane, new Insets(10));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void refresh(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../views/adminUI.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void updateFruit(MouseEvent event) throws IOException, SQLException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../views/updateFruit.fxml"));
		Parent root = fxmlLoader.load();
		updateController uController = fxmlLoader.getController();
		Fruit fruit = searchFruitByName(fruitNameLabel.getText());
		uController.setFruit(fruit);
		uController.getData(fruit);
		uController.setChosenFruit(fruit);
		// uController.getData(fruitNameLabel.getText(), fruitPriceLabel.getText());
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void goToHome(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../views/adminUI.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public Fruit setFruit() {
		fruit.setName(fruitNameLabel.getText());
		String priceString = fruitPriceLabel.getText().substring(3);
		fruit.setPrice(Double.parseDouble(priceString));
		// System.out.println(fruit.getPrice());
		return fruit;
	}

	public Fruit searchFruitByName(String name) throws SQLException {
		Connection con = SqLiteConnection.connector();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from Fruit where f_name = '" + name + "'");
		return new Fruit(rs.getLong("f_id"), rs.getString("f_name"), rs.getString("f_image"), rs.getDouble("price"),
				rs.getString("f_color"), rs.getInt("quantity"));
	}

	@FXML
	void viewStats(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../views/statsUI.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
