package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.SqLiteConnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import model.Fruit;

public class CartController implements Initializable {

	@FXML
	private ImageView homeBtn;

	@FXML
	private GridPane grid;

	List<Fruit> fruits = new ArrayList<>();

	@FXML
	void goToHome(MouseEvent event) {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		int column = 0, row = 1;
		try {
			List<Fruit> dataFruits = getCartItems(SqLiteConnection.connector());
			System.out.println(dataFruits);
			for (Fruit fruit : dataFruits) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/views/itemCartUI.fxml"));
				AnchorPane anchorPane = fxmlLoader.load();

				ItemCartController itemCartController = fxmlLoader.getController();
				itemCartController.setData(fruit);

				if (column == 1) {
					column = 0;
					row++;
				}

				grid.add(anchorPane, column++, row);

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
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	List<Fruit> getCartItems(Connection conn) throws SQLException {

		fruits = FXCollections.observableArrayList();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from Cart");
		while (rs.next()) {
			fruits.add(new Fruit(rs.getLong("id"), rs.getString("name"), rs.getDouble("quantity"),
					rs.getDouble("price"), rs.getString("image")));
		}
		return fruits;
	}
}
