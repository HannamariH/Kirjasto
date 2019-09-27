/**
 * 
 */
package fxKirjasto;

import java.io.PrintStream;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import kirjasto.Kirja;
import kirjasto.Kirjasto;

/**
 * Hakutulosikkunan kontrolleriluokka
 * @author Hannamari Heiniluoma
 * @version 8 Feb 2019
 *
 */
public class HakutulosGUIController implements ModalControllerInterface<Kirja> {

    @FXML private TextArea textareaKirjantiedot;
    
    @FXML private Button buttonPaluu;

    @FXML private Button buttonMuokkaa;

    @FXML private Button buttonPoista;

    /**
     * Avaa Muokkaa/Lisää-ikkunan
     */
    @FXML void handleMuokkaaTeoksenTietoja() {
        ModalController.showModal(MuokkaaLisaaGUIController.class.getResource("MuokkaaLisaa.fxml"), "Muokkaa/Lisää", null, "");
    }

    @FXML void handlePoistaTeos() {
        Dialogs.showMessageDialog("Ei osata vielä poistaa!");
    }

    @FXML void handleSuljeIkkuna() {
        Dialogs.showMessageDialog("Ei osata vielä palata takaisin!");
    }

//============================================================
    private Kirjasto kirjasto;
    private Kirja kirjaKohdalla;
    
    
    @Override
    public void handleShown() {        
        naytaKirja(kirjaKohdalla);        
    }


    @Override
    public Kirja getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    
    @Override
    public void setDefault(Kirja arg0) {
        this.kirjaKohdalla = arg0;      
    }

    
    /**
     * Näyttää kirjan tiedot textarea-kentässä
     * @param kirja kirja, jonka tiedot näytetään
     */
    public void naytaKirja(Kirja kirja) {
        if (kirja == null) return;
        //this.textareaKirjantiedot.setText(kirja.toString());       
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(textareaKirjantiedot)) {
            tulosta(os, kirja);
        }
    }
    
    
    /**
     * Tulostaa kirjan tiedot
     * @param os tietovirta johon tulostetaan
     * @param kirja tulostettava kirja
     */
    public void tulosta(PrintStream os, final Kirja kirja) {
        os.println("----------------------------------------------");
        kirja.tulosta(os);
        os.println("----------------------------------------------");
        /*List<Harrastus> harrastukset = kerho.annaHarrastukset(jasen);   
        for (Harrastus har:harrastukset)
            har.tulosta(os);  */
    }
    
    
    // Ei käytetä vielä
    /**
     * Tulostaa listassa olevat kirjat tekstialueeseen
     * @param text alue johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki jäsenet");
            for (int i = 0; i < kirjasto.getKirjoja(); i++) {
                Kirja kirja = kirjasto.annaKirja(i);
                tulosta(os, kirja);
                os.println("\n\n");
            }
        }
    }


}
