package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import application.MyListener;
import application.SqLiteConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Fruit;

public class MarketController implements Initializable {
	@FXML
	private VBox chosenFruitCard;

	@FXML
	private Label fruitNameLable;

	@FXML
	private Label fruitPriceLabel;

	@FXML
	private ImageView fruitImg;

	@FXML
	private ScrollPane scroll;

	@FXML
	private GridPane grid;

	@FXML
	private TextField searchTxt = new TextField();

	@FXML
	private Button searchBtn;

	@FXML
	private Button addBtn;

	@FXML
	private Label loginBtn;

	@FXML
	private TextField nmbPieces;

	@FXML
	private ComboBox<Number> kgListChoices = new ComboBox<>();

	@FXML
	private Button addToCartBtn;

	@FXML
	private ImageView cartBtn;

	private List<Fruit> fruits = new ArrayList<>();
	private Image image;
	private MyListener myListener;
	private SqLiteConnection sqLiteConnection = new SqLiteConnection();

	public List<Fruit> getData() {
		List<Fruit> fruits = new ArrayList<>();
		Fruit fruit;

		fruit = new Fruit();
		fruit.setName("Kiwi");
		fruit.setPrice(2.99);
		fruit.setImgSrc("/images/kiwi.png");
		fruit.setColor("6A7324");
		fruit.setQuantity(50);
		fruits.add(fruit);

		fruit = new Fruit();
		fruit.setName("Coconut");
		fruit.setPrice(3.99);
		fruit.setImgSrc("/images/coconut.png");
		fruit.setColor("A7745B");
		fruit.setQuantity(33);
		fruits.add(fruit);

		fruit = new Fruit();
		fruit.setName("Peach");
		fruit.setPrice(1.50);
		fruit.setImgSrc("/images/peach.png");
		fruit.setColor("F16C31");
		fruit.setQuantity(100);
		fruits.add(fruit);

		fruit = new Fruit();
		fruit.setName("Grapes");
		fruit.setPrice(0.99);
		fruit.setImgSrc("/images/grapes.png");
		fruit.setColor("291D36");
		fruit.setQuantity(80);
		fruits.add(fruit);

		fruit = new Fruit();
		fruit.setName("Watermelon");
		fruit.setPrice(4.99);
		fruit.setImgSrc("/images/watermelon.png");
		fruit.setColor("22371D");
		fruit.setQuantity(44);
		fruits.add(fruit);

		fruit = new Fruit();
		fruit.setName("Orange");
		fruit.setPrice(2.99);
		fruit.setImgSrc("/images/orange.png");
		fruit.setColor("FB5D03");
		fruit.setQuantity(111);
		fruits.add(fruit);

		fruit = new Fruit();
		fruit.setName("StrawBerry");
		fruit.setPrice(0.99);
		fruit.setImgSrc("/images/strawberry.png");
		fruit.setColor("80080C");
		fruit.setQuantity(22);
		fruits.add(fruit);

		fruit = new Fruit();
		fruit.setName("Mango");
		fruit.setPrice(0.99);
		fruit.setImgSrc("/images/mango.png");
		fruit.setColor("FFB605");
		fruit.setQuantity(99);
		fruits.add(fruit);

		fruit = new Fruit();
		fruit.setName("Cherry");
		fruit.setPrice(0.99);
		fruit.setImgSrc("/images/cherry.png");
		fruit.setColor("5F060E");
		fruit.setQuantity(66);
		fruits.add(fruit);

		fruit = new Fruit();
		fruit.setName("Banana");
		fruit.setPrice(1.99);
		fruit.setImgSrc("/images/banana.png");
		fruit.setColor("E7C00F");
		fruit.setQuantity(59);
		fruits.add(fruit);

		return fruits;
	}

	public void setChosenFruit(Fruit fruit) {
		fruitNameLable.setText(fruit.getName());
		fruitPriceLabel.setText(fruit.getPrice() + Main.CURRENCY);
		image = new Image(getClass().getResourceAsStream(fruit.getImgSrc()));
		fruitImg.setImage(image);
		chosenFruitCard
				.setStyle("-fx-background-color: #" + fruit.getColor() + ";\n" + "    -fx-background-radius: 30;");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ObservableList<Number> kgList = FXCollections.observableArrayList(1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5);
		kgListChoices.setItems(kgList);
		try {
			fruitsToDb();
			fruits = sqLiteConnection.retrieveData(SqLiteConnection.connector());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (fruits.size() > 0) {
			setChosenFruit(fruits.get(0));
			myListener = new MyListener() {
				@Override
				public void onClickListener(Fruit fruit) {
					setChosenFruit(fruit);
				}
			};
		}

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

	public void fruitsToDb() throws SQLException {
		List<Fruit> fruits = getData();
		for (Fruit fruit : fruits) {

			sqLiteConnection.insert(fruit);
		}
	}

	public Fruit searchFruit(String name) {
		Fruit fruit = new Fruit();
		try {
			Connection con = SqLiteConnection.connector();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from Fruit where name='" + name + "'");

			while (rs.next()) {
				fruit.setName(rs.getString("name"));
				fruit.setImgSrc(rs.getString("image"));
				fruit.setPrice(rs.getDouble("price"));
				fruit.setColor(rs.getString("color"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fruit;
	}

	@FXML
	void searchFruit(ActionEvent event) {
		String searchTxtString = searchTxt.getText();
		System.out.println(searchTxt.getText());
		Fruit fruit = searchFruit(searchTxtString);
		setChosenFruit(fruit);
	}

	@FXML
	void addNewFruitForm(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../views/addFruitForm.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void goToLogin(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../views/loginUI.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void addToCart(ActionEvent event) throws SQLException {

		if (kgListChoices.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Undefined");
			alert.setHeaderText("Please select Quantity");
			alert.show();
		}

		String sql = "INSERT INTO Cart(name,quantity,price,image) VALUES(?,?,?,?)";
		PreparedStatement pstmt = SqLiteConnection.connector().prepareStatement(sql);
		Fruit fruit = searchFruit(fruitNameLable.getText());
		pstmt.setString(1, fruit.getName());
		pstmt.setDouble(2, kgListChoices.getValue().doubleValue());
		pstmt.setDouble(3, kgListChoices.getValue().doubleValue() * fruit.getPrice());
		pstmt.setString(4, fruit.getImgSrc());
		pstmt.executeUpdate();

	}

	@FXML
	void goToCart(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../views/cartUI.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
