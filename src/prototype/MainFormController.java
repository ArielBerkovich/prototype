package prototype;

import java.io.IOException;
import java.util.ArrayList;

import client.Client;
import client.ClientInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class MainFormController implements ClientInterface {

	private Client client;
	private ArrayList<Product> products = new ArrayList<Product>();
	
    @FXML
    private ComboBox<String> productCbx;

    @FXML
    private Button showProductBtn;

    @FXML
    private Button exitBtn;

    @FXML
    void onExit(ActionEvent event) {
    	System.out.println("onExit was perssed");
    	client.quit();
    	System.exit(0);
    }

    @FXML
    void onShowProductInfo(ActionEvent event) {
    	
    	// get the index of the product selected
    	int productIndex = productCbx.getSelectionModel().getSelectedIndex();
    	// if no product was selected than abort
		if (productIndex == -1)
			return;
		
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		try
		{
			Pane root = loader.load(getClass().getResource("ProductInfoForm.fxml").openStream());
					
			ProductInfoFormController productFromController = loader.getController();

			productFromController.loadProduct(products.get(productCbx.getSelectionModel().getSelectedIndex()));
			productFromController.setClinet(client);
			
			Scene scene = new Scene(root);			
			//scene.getStylesheets().add(getClass().getResource("/gui/StudentForm.css").toExternalForm());
			
			primaryStage.setScene(scene);	
			primaryStage.setTitle("Prototype");
			primaryStage.show();
		}
		catch(IOException e)
		{
			System.out.println("Failed to open ProductInfoForm");
		}
    }

    public void display(Object message)
    {
    	System.out.println(message.toString());
    	System.out.println(message.getClass().toString());
    	String data = (String)message;
    	ArrayList<String> comboboxProductStrings = new ArrayList<String>();
    	    	
//    	// get the rows data from the data string by splitting around '['
    	String[] rows = data.split("\\[");
//    	
    	// read fields data from each row
    	// starting from 2 since 0,1 are empty
    	for (int i = 2; i < rows.length; i++)
    	{
    		String[] field = rows[i].split("\\,");	
    		
    		// remove the ending ] from the third field string
    		field[0] = field[0].trim();
    		field[1] = field[1].trim();
    		field[2] = field[2].replaceAll("\\]", "");
    		field[2] = field[2].trim();
    		Product p = new Product(Integer.parseInt(field[0]), field[1], field[2]);
    		products.add(p);
    		comboboxProductStrings.add(p.getName());
    	}
    	
    	System.out.println(comboboxProductStrings);
    	ObservableList<String> comboBoxList = FXCollections.observableArrayList(comboboxProductStrings);
    	productCbx.setItems(comboBoxList);
//    	   	
//    	productView.setItems(itemData);	
    }
    
    public void requestProductInfo()
    {
    	ArrayList<String> message = new ArrayList<String>();
    	
    	message.add("GET");
    	message.add("Product");
    	client.handleMessageFromClientUI(message);
    }
    
    public void initData(Client newClient)
    {
    	client = newClient;
    	requestProductInfo();
    }
    
}
