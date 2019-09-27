package fxKirjasto;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import kirjasto.*;

import java.io.IOException;
import java.io.PrintStream;

import fi.jyu.mit.fxgui.*;

/**
 * Luokka kirjaston käyttöliittymän pääikkunan tapahtumien hoitamiseksi.
 * 
 * @author Hannamari Heiniluoma
 * @version 25.4.2019
 *
 */
public class PaaikkunaGUIController implements Initializable {
    
    @FXML private ComboBoxChooser<String> cbHakuehdot;
    @FXML private TextField textHakuruutu;
    @FXML private ListChooser<Kirja> listChooserHakutulos;
    @FXML private TextArea textAreakirjanTiedot;
    @FXML private Label labelHaku;
    
    
    /**
     * Alustaa ikkunan
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();        
    }

    
    /**
     * Hakee kirjat listaan halutuilla hakuehdoilla
     */
    @FXML private void handleHae() {
        haeKirja(0);
    }
    
    
    /**
     * Näyttää listasta klikatun kirjan tiedot viereisessä tekstiruudussa.
     */
    @FXML private void handleAvaaTeoksenTiedot() {
        naytaKirja();
    }

    
    /**
     * Avaa muokkausikkunan uuden kirjan lisäämistä varten.
     */
    @FXML private void handleLisaaUusiTeos() {
        uusiKirja();
    }
    
    
    /**
     * Avaa muokkausikkunan kirjan tietojen muokkausta varten.
     */
    @FXML private void handleMuokkaaTeosta() {
        muokkaa();
    }
    
    
    /**
     * Poistaa teoksen.
     */
    @FXML private void handlePoistaTeos() {
        poistaKirja();
    }
    
    
    /**
     * Sulkee ohjelman.
     */
    @FXML private void handleLopeta() {
        Platform.exit();        
    }
    
    
    /**
     * Avaa selaimeen harkkatyön suunnitelman.
     */
    @FXML private void handleApua() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2019k/ht/hahelle");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }

    
    /**
     * TODO: tässä on jotain häikkää, ei aukea.
     * TODO: vaihe 3 toimii! Sen ainoa ero tähän on se ettei tämä aliohjelma ole private
     * (on vain @FXML void HandleTietoja)
     * Avaa tietoja ohjelmasta -ikkunan.
     */
    @FXML private void handleTietoja() {
        ModalController.showModal(PaaikkunaGUIController.class.getResource("Tietoja.fxml"), "Kirjasto", null, "");
    }
    
    
    //========================================================
    // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia
    
    private Kirjasto kirjasto;
    private Kirja kirjaKohdalla;
    private static Kirja apukirja = new Kirja();
    
    
    /**
     * Tekee tarvittavat alustukset eli tyhjentää listchooserin ja
     * laittaa comboboxiin oikeat hakuehdot
     */
    private void alusta() {
        this.listChooserHakutulos.clear();
        this.listChooserHakutulos.addSelectionListener(e -> naytaKirja());
        this.cbHakuehdot.clear();
        this.cbHakuehdot.add("Tekijä", null);
        for (int k = apukirja.ekaKentta(); k < apukirja.getKenttia(); k++) {
            this.cbHakuehdot.add(apukirja.getKysymys(k), null);
        }        
        this.cbHakuehdot.add("Asiasana", null);
        this.cbHakuehdot.getSelectionModel().select(0);
    }
    
    
    /**
     * Alustaa kirjaston lukemalla kaikki tarvittavat tiedostot
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    public String lueTiedosto() {
        try {
            this.kirjasto.lueTiedosto();
            haeKirja(0);
            return null;
        } catch (SailoException e) {
            haeKirja(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
    }
    
    
    /**
     * Muokkaa valitun kirjan tietoja.
     */
    private void muokkaa() {
        if (this.kirjaKohdalla == null) return;
        try {
            Kirja kirja = MuokkaaLisaaGUIController.kysyKirja(null, this.kirjaKohdalla.clone(), "Muokkaa teosta", this.kirjasto);
            if (kirja == null) return;
            this.kirjasto.korvaaTaiLisaa(kirja);
            haeKirja(kirja.getId());
        } catch (CloneNotSupportedException e) {
            //
        }
    }

  
     /**
      * Poistaa kirjan ja siihen liittyvät liimat liimatiedostosta
      * Kirjaan liittyvä tekijä ja asiasanat saavat jäädä tiedostoihin.
      */
     private void poistaKirja() {
         Kirja kirja = this.kirjaKohdalla;
         if (kirja == null) return;
         if (!Dialogs.showQuestionDialog("Poisto", "Poistetaanko kirja: " + kirja.getNimi(), "Kyllä", "Ei") )
             return;
         this.kirjasto.poista(kirja);
         tallenna();
         haeKirja(0);
     }
     
     
     /**
      * Tietojen tallennus
      */
      private String tallenna() {
         try {
             this.kirjasto.tallenna();
             return null;
         } catch (SailoException se) {
             Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + se.getMessage());
             return se.getMessage();
         }

     }
    
      
    /**
     * Tulostaa listChooserissa valittuna olevan kirjan tiedot viereiseen textAreaan
     */
    private void naytaKirja() {
        this.kirjaKohdalla = this.listChooserHakutulos.getSelectedObject();
        if (this.kirjaKohdalla == null) {
            this.textAreakirjanTiedot.clear();
            return;     
        }
        this.textAreakirjanTiedot.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(this.textAreakirjanTiedot)) {
            tulosta(os, this.kirjaKohdalla);  
        }
    }
    
    
    /**
     * Tulostaa kirjan tiedot
     * @param os tietovirta johon tulostetaan
     * @param kirja tulostettava kirja
     */
    public void tulosta(PrintStream os, final Kirja kirja) {
        os.println("----------------------------------------------");
        Tekija tekija = this.kirjasto.annaTekija(kirja);
        if (tekija != null ) tekija.tulosta(os);
        kirja.tulosta(os);        
        os.println("----------------------------------------------");
        os.println("Asiasanat:");
        List<Asiasana> asiasanat = this.kirjasto.annaAsiasanat(kirja);
        if (asiasanat != null && !asiasanat.isEmpty()) {
            for (Asiasana as : asiasanat) 
                as.tulosta(os);           
        }
    }
    
    
    /**
     * Asetetaan viite käytettävään kirjastoon
     * @param kirjasto kirjasto, jota käytetään
     */
    public void setKirjasto(Kirjasto kirjasto) {
        this.kirjasto = kirjasto;
    }
    
    
    /**
     * Lisää kirjastoon uuden kirjan ja haeKirja-metodia kutsumalla
     * lisää sen listChooser-listaan.
     * Joudutaan rekisteröimään jo ennen muokkausikkunan avautumista, jotta
     * kirjalle voidaan liittää tekijä ja asiasanat.
     */
    private void uusiKirja() {
        Kirja kirja = new Kirja();
        kirja.rekisteroi();
        kirja = MuokkaaLisaaGUIController.kysyKirja(null, kirja, "Lisää uusi", this.kirjasto);
        if (kirja == null || kirja.getNimi().equals("")) return;
        haeKirja(kirja.getId());        
    }
    
    
    /**
     * Hakee hakuehdot täyttävien kirjojen tiedot listChooser-listalle
     * @param kirjaId sen kirjan id, joka asetetaan valituksi
     */
    private void haeKirja(int kirjaId) {
        int kirjanro = kirjaId;
        this.listChooserHakutulos.clear();    
        if (kirjanro <= 0) {
            if (this.kirjaKohdalla != null) kirjanro = this.kirjaKohdalla.getId();
        }
        String ehto = this.textHakuruutu.getText();
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*";
        int index = 0;
        
        if (this.cbHakuehdot.getSelectedIndex() == 0) { //haetaan tekijän nimellä           
            Collection<Tekija> tekijat; 
            try {
                tekijat = this.kirjasto.etsiTekijat(ehto);
                List<Kirja> masterlist = new ArrayList<>();
                for (Tekija tekija : tekijat) {
                    Collection<Kirja> tekijanKirjat = this.kirjasto.etsiTekijanKirjat(tekija);
                    masterlist.addAll(tekijanKirjat);
                }
                index = listaaKirjat(masterlist, kirjanro);          
            } catch (SailoException e) {
                Dialogs.showMessageDialog("Kirjan hakemisessa ongelmia! " + e.getMessage());
            }                            
        }        
                
        else if (this.cbHakuehdot.getSelectedIndex() == 7) {// haetaan asiasanalla
            Collection<Asiasana> asiasanat;
            try {
                asiasanat = this.kirjasto.etsiAsiasanat(ehto);
                List<Kirja> masterlist = new ArrayList<>();
                for (Asiasana asiasana : asiasanat) {
                    ArrayList<Kirja> asiasananKirjat = this.kirjasto.etsiAsiasananKirjat(asiasana);
                    for (int i = 0; i < asiasananKirjat.size(); i++) {
                        Kirja lisattava = asiasananKirjat.get(i);
                        if (lisattava != null && !masterlist.contains(lisattava)) masterlist.add(lisattava);
                    }
                }
                index = listaaKirjat(masterlist, kirjanro);
            } catch (SailoException e) {
                Dialogs.showMessageDialog("Kirjan hakemisessa ongelmia! " + e.getMessage());
            }                       
        }                      
        
        else { //haetaan kirjaan tallennetuilla tiedoilla
            //koska ekana on tekijä, on  laitettava tähän -1 jotta saadaan kentät kohdilleen
            int k = this.cbHakuehdot.getSelectedIndex() -1 + apukirja.ekaKentta();             
            List<Kirja> kirjat;
            try {
                kirjat = this.kirjasto.etsiKirjat(ehto, k);
                index = listaaKirjat(kirjat, kirjanro);
            } catch (SailoException e) {
                Dialogs.showMessageDialog("Kirjan hakemisessa ongelmia! " + e.getMessage());
            }                      
        }        
        this.listChooserHakutulos.setSelectedIndex(index);
        naytaKirja();
    }
    
    
    /**
     * Lajittelee kirjalistan ja listaa sen näkyville listChooseriin
     * @param lista näytettävä lista kirjoista
     * @param kirjaId valituksi haluttavan kirja id-nro 
     * @return indeksi, joka listChooserissa laitetaan valituksi
     */
    private int listaaKirjat(List<Kirja> lista, int kirjaId) {
        Collections.sort(lista);
        int i = 0;
        int index = 0;
        for (Kirja kirja : lista) {
            if (kirja.getId() == kirjaId) index = i;
            this.listChooserHakutulos.add(kirja.getNimi(), kirja);
            i++;
        }
        return index;
    }

}
