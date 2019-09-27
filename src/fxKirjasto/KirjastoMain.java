package fxKirjasto;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import kirjasto.*;


/**
 * Pääluokka, joka käynnistää kirjasto-ohjelman.
 * @author Hannamari Heiniluoma
 * @version 25.4.2019
 *
 */
public class KirjastoMain extends Application {
    
    /**
     * Asetetaan näytössä kaikki kohdalleen.
     */
	@Override
	public void start(Stage primaryStage) {
		try {		    
		    final FXMLLoader ldr = new FXMLLoader(getClass().getResource("Paaikkuna.fxml"));	
		    final Pane root = (Pane)ldr.load();
		    final PaaikkunaGUIController paaikkunaCtrl = (PaaikkunaGUIController)ldr.getController();
		    Kirjasto kirjasto = new Kirjasto();
		    paaikkunaCtrl.setKirjasto(kirjasto);		    
			
			final Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("kirjasto.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Kirjasto");
			primaryStage.show();
			
			paaikkunaCtrl.lueTiedosto();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Käynnistetään käyttöliittymä
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
