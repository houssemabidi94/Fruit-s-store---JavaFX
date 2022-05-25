package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import controller.MarketController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Fruit;

public class SqLiteConnection {
	private ObservableList<Fruit> dataFruits;

	public static Connection connector() {

		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:FruitsMarketDB.sqlite");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS Fruit " +

					"(id INTEGER PRIMARY KEY AUTOINCREMENT," +

					" name TEXT UNIQUE NOT NULL, " + " image TEXT NOT NULL, " + " price DOUBLE NOT NULL, "
					+ " color TEXT NOT NULL," + " quantity INTEGER NOT NULL);" + " CREATE TABLE IF NOT EXISTS Cart"
					+ "(id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT UNIQUE NOT NULL,"
					+ "quantity DOUBLE NOT NULL," + "price DOUBLE NOT NULL," + "image TEXT NOT NULL);";

			stmt.executeUpdate(sql);

		} catch (Exception e) {
			System.out.println(e);
		}
		return conn;
	}

	// String name, String image,double price,String color
	public void insert(Fruit fruit) {
		String sql = "INSERT INTO Fruit(name,image,price,color,quantity) VALUES(?,?,?,?,?)";

		try (Connection conn = connector(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, fruit.getName());
			pstmt.setString(2, fruit.getImgSrc());
			pstmt.setDouble(3, fruit.getPrice());
			pstmt.setString(4, fruit.getColor());
			pstmt.setDouble(5, fruit.getQuantity());
			pstmt.executeUpdate();
			System.out.println("insert into success");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public ObservableList<Fruit> retrieveData(Connection con) throws SQLException {
		dataFruits = FXCollections.observableArrayList();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from Fruit");
		while (rs.next()) {
			dataFruits.add(new Fruit(rs.getLong("id"), rs.getString("name"), rs.getString("image"),
					rs.getDouble("price"), rs.getString("color"), rs.getInt("quantity")));
		}
		return dataFruits;
	}

}