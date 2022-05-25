package model;

public class Fruit {
	private Long id;
	private String name;
	private String imgSrc;
	private double price;
	private String color;
	private double quantity;

	public String getName() {
		return name;
	}

	public Fruit() {
		super();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Fruit(Long id, String name, String imgSrc, double price, String color, int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.imgSrc = imgSrc;
		this.price = price;
		this.color = color;
		this.quantity = quantity;
	}

	public Fruit(long id, String name, double quantity, double price ,String image) {

		this.id = id;
		this.name = name;
		this.imgSrc = image;
		this.price = price;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Fruit [id=" + id + ", name=" + name + ", imgSrc=" + imgSrc + ", price=" + price + ", color=" + color
				+ "]";
	}

}
