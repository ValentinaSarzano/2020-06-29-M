/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.RegistaAttoriCondivisi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    
    	Integer year = boxAnno.getValue();
    	if(year == null) {
    		txtResult.setText("ERRORE: Selezionare un anno!\n");
    	}
    	
    	//TODO creare grafo
    	this.model.creaGrafo(year);
    	
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("#VERTICI: " + this.model.nVertici() + "\n");
    	txtResult.appendText("#ARCHI: " + this.model.nArchi());
    
    	
    	boxRegista.getItems().addAll(this.model.getVertici());
    	btnAdiacenti.setDisable(false);
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {

    	txtResult.clear();
    	Director regista = boxRegista.getValue();
    	if(regista == null) {
    		txtResult.setText("ERRORE: Selezionare un regista!\n");
    	}
    	
    	//ADIACENTI di un grafo non orientato --> neighborListOf
    	List<RegistaAttoriCondivisi> adiacenti = this.model.getAttoriCondivisi(regista);
    	txtResult.appendText("REGISTI ADIACENTI A "+ regista + ": \n");
    	for(RegistaAttoriCondivisi r: adiacenti) {
        	txtResult.appendText(r +"\n");
    	}
    	
    }

    @FXML
    void doRicorsione(ActionEvent event) {

    	txtResult.clear();
    	Director regista = boxRegista.getValue();
    	if(regista == null) {
    		txtResult.setText("ERRORE: Selezionare un regista!\n");
    	}
    	
    	int c = 0;
        try {
        	c = Integer.parseInt(txtAttoriCondivisi.getText());
        }catch(NumberFormatException e) {
        	txtResult.setText("ERRORE: Inserire un numero valido!\n");
        }
    	
        List<Director> percorsoMigliore = this.model.trovaPercorso(regista, c);
    	
        txtResult.appendText("PERCORSO MIGLIORE:\n");
        for(Director d: percorsoMigliore) {
        	txtResult.appendText(""+d.getFirstName()+" "+ d.getLastName()+ "\n");
        }

        txtResult.appendText("# Totale attori condivisi: " + this.model.getTotAttoriCondivisi(regista, c));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

        btnAdiacenti.setDisable(true);
       
    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	 
        for(int year=2004; year<=2006; year++) {
        	boxAnno.getItems().add(year);
        }
    }
    
}
