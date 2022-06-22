/**
 /**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
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
    	Integer anno = boxAnno.getValue();
    	if(anno == null) {
    		txtResult.appendText("ERRORE: Selezionare prima un anno dal menu a tendina!\n");
    		return;
    	}
    	this.model.creaGrafo(anno);
    	boxRegista.getItems().clear();
    	boxRegista.getItems().addAll(this.model.getVertici());
    	btnAdiacenti.setDisable(false);
    	
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("#VERTICI: "+ this.model.nVertici()+"\n");
    	txtResult.appendText("#ARCHI: "+ this.model.nArchi());
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {

    	txtResult.clear();
    	txtResult.clear();
    	Integer anno = boxAnno.getValue();
    	if(anno == null) {
    		txtResult.appendText("ERRORE: Selezionare prima un anno dal menu a tendina!\n");
    		return;
    	}
    	Director director = boxRegista.getValue();
    	if(director == null) {
    		txtResult.appendText("ERRORE: Selezionare prima un regista dal menu a tendina!\n");
    		return;
    	}
    	List<RegistaAttoriCondivisi> adiacenti = new ArrayList<>(this.model.getAdiacenti(director));
    	txtResult.appendText("REGISTI ADIACENTI A " + director + ":\n");
    	
    	for(RegistaAttoriCondivisi r: adiacenti) {
    		txtResult.appendText(r.getD() + " - " + "#attori condivisi: " + r.getAttoriCondivisi() + "\n");
    	}
    }

    @FXML
    void doRicorsione(ActionEvent event) {

    	txtResult.clear();
    	int n = 0;
    	try {
    		n = Integer.parseInt(txtAttoriCondivisi.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("ERRORE: Inserire un numero valido!\n");
    	}Director d = boxRegista.getValue();
    	if(d == null) {
    		txtResult.appendText("ERRORE: Selezionare prima un regista dal menu a tendina!\n");
    		return;
    	}
    	List<Director> affini = new ArrayList<>(this.model.trovaPercorso(n, d));
    	txtResult.appendText("TROVATI REGISTI AFFINI!\n");
    	for(Director di: affini) {
    		txtResult.appendText(di+"\n");
    	}
    	txtResult.appendText("#Tot attori condivisi: " + this.model.getTotAttoriCondivisi(d, n));
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

        for(int year=2004; year<=2007; year ++) {
        	boxAnno.getItems().add(year);
        }
    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	btnAdiacenti.setDisable(true);
    }
    
}