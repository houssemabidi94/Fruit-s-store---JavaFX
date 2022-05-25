package controller;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Fruit;

public class ItemCartController {

	@FXML
	private ImageView fruitImage;

	@FXML
	private Label fruitName;

	@FXML
	private Label fruitQty;

	@FXML
	private Label fruitPrice;


	public void setData(Fruit fruit) {
		fruitName.setText(fruit.getName());
		fruitPrice.setText(fruit.getPrice() + " " + Main.CURRENCY);
		Image image = new Image(getClass().getResourceAsStream(fruit.getImgSrc()));
		fruitImage.setImage(image);
		fruitQty.setText(Double.toString(fruit.getQuantity()));
	}
}
