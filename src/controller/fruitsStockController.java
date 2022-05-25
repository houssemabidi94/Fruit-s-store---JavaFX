package controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import application.SqLiteConnection;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Fruit;

public class fruitsStockController extends Application  {

	@FXML
	private AnchorPane ui;

    @FXML
    private HBox printBtn;
    
	@FXML
	private Label logOutBtn;

	@FXML
	private ImageView homeBtn;

	@FXML
	private HBox addNewFruit;

	@FXML
	private ProgressIndicator pI;

	@FXML
	private TableView<Fruit> table;

	@FXML
	private TableColumn<Fruit, String> col_name;

	@FXML
	private TableColumn<Fruit, Integer> col_qty;

	@FXML
	void addNewFruit(MouseEvent event) {

	}

	@FXML
	void goToHome(MouseEvent event) {

	}

	@FXML
	void logOut(MouseEvent event) {

	}

	private void loadData() throws SQLException {
		Connection connection = SqLiteConnection.connector();
		SqLiteConnection sqLiteConnection = new SqLiteConnection();
		ObservableList<Fruit> fruits = sqLiteConnection.retrieveData(connection);
		col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		table.setItems(fruits);
	}
	
    @FXML
    void printDoc(MouseEvent event) throws FileNotFoundException, DocumentException, SQLException {

		Document pdfDocument = new Document();
		PdfWriter.getInstance(pdfDocument, new FileOutputStream("fruits_stock.pdf"));
		pdfDocument.open();
		
		 Font f_title=new Font(FontFamily.TIMES_ROMAN,50.0f,Font.UNDERLINE,BaseColor.RED);
		 Font f_header=new Font(FontFamily.TIMES_ROMAN,20.0f,Font.BOLD,BaseColor.BLUE);
		 Paragraph p=new Paragraph("Fruit's Stock",f_title);
		 p.setAlignment(Paragraph.ALIGN_CENTER);
		 pdfDocument.add(p);
		pdfDocument.add(new Chunk("")); 
		PdfPTable headerTabel = new PdfPTable(2);
		PdfPCell headerCell = new PdfPCell();
		headerCell.setFixedHeight(30.0f);
		headerCell.setPhrase(new Phrase(new Paragraph("Name",f_header)));
		headerTabel.addCell(headerCell);
		headerCell.setPhrase(new Phrase(new Paragraph("Quantity  (kg)",f_header)));
		headerTabel.addCell(headerCell);
		pdfDocument.add(headerTabel);
		PdfPTable pdfPTable = new PdfPTable(2);
		PdfPCell table_cell = new PdfPCell();
		SqLiteConnection sqLiteConnection = new SqLiteConnection();
		ObservableList<Fruit> fruits= sqLiteConnection.retrieveData(SqLiteConnection.connector());
		
		for (Fruit fruit : fruits) {
			table_cell.setFixedHeight(20.0f);
			String Name = fruit.getName();
			table_cell.setPhrase(new Phrase(Name));
			pdfPTable.addCell(table_cell);
			String Quantity = Double.toString(fruit.getQuantity());
			table_cell = new PdfPCell(new Phrase(Quantity));	
			pdfPTable.addCell(table_cell);
		}
		pdfDocument.add(pdfPTable);
		pdfDocument.close();
		HostServices hostServices = getHostServices();
		hostServices.showDocument("fruits_stock.pdf");
		
    	
    }

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		try {
			loadData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
