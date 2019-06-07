/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.model.Model;
import it.polito.tdp.model.Simulatore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CrimesController {

	private Model model = new Model();
	
	int anno ;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Month> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	String s = "";
    	 anno = boxAnno.getValue();
    	if(anno!=0) {
    	model.createGraph(anno);
    	}
    	else {
    		txtResult.appendText("Inserisci un anno, capra!");
    		return;
    		}
    	
    	
    	for(int i = 1 ;i <=7; i++) {
    	s +="L'elenco dei distretti ordinati per il distretto "+i+" è: "+ model.getDistrettiOrdinati(i)+"\n";	
    	}
    	txtResult.appendText(s);
    	List<Integer> l = new ArrayList<Integer>();
    	for(int i = 1 ; i<=31; i++) {
    		l.add(i);
    	}
    	boxMese.getItems().addAll(Month.values());
    	boxGiorno.getItems().addAll(l);
    	}
    
    
    
  
    

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    int h = Integer.parseInt(	txtN.getText());
    if((h<1)||(h>10)) {
    	txtResult.appendText("Devi inserire una N da 1 a 10");
    	return;
    }
    int j = model.getDistrettoCrimineMinore(anno);
    int i = boxMese.getValue().getValue();
    int f = boxGiorno.getValue().intValue();
    
    Simulatore s = new Simulatore(h,anno,i,f);	
    s.simulate();
    txtResult.appendText("Gli eventi mal gestiti nella simulazione sono stati : "+s.getEventiMalGestiti());
    		
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";
        boxAnno.getItems().addAll(this.model.getListaAnni());
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
