/**
 * 
 */
package fxKirjasto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kirjasto.*;

/**
 * Muokkaa/Lisää-ikkunoiden kontrolleriluokka
 * Luo uuden dialogin, jossa kysytään kirjan tiedot.
 * 
 * @author Hannamari Heiniluoma
 * @version 25.4.2019
 *
 */
public class MuokkaaLisaaGUIController implements ModalControllerInterface<Kirja>, Initializable {

    @FXML private Label labelVirhe;
    @FXML private GridPane gridKirja;
    @FXML private TextField editTekija;
    @FXML private TextField editAsiasanat;
    @FXML private ComboBoxChooser<String> cbTeoksenTila;
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //tässä ei tehdä vielä mitään, koska ensin pitää käydä
        //setKirjasto-metodissa asettamassa kirjasto
    }
    

    /**
     * Asettaa viitteen uuteen/muokattuun kirjaan nulliksi ja sulkee ikkunan.
     */
    @FXML private void handlePeruuta() {
        this.kirjaKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }
    
    
    /**
     * Tallentaa muutokset ja sulkee ikkunan.
     */
    @FXML private void handleTallenna() {
        if (this.kirjaKohdalla != null && this.kirjaKohdalla.getNimi().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
            }
        kasitteleMuutosTekijaan(this.editTekija);
        kasitteleMuutosAsiasanoihin(this.editAsiasanat);
        this.kirjasto.korvaaTaiLisaa(this.kirjaKohdalla);
        tallenna();
        ModalController.closeStage(labelVirhe);
    }
    
    //=========================================================
    // Tästä eteenpäin koodi ei suoraan liity käyttöliittymään.

    private Kirjasto kirjasto;
    private static Kirja apuKirja = new Kirja();
    private Kirja kirjaKohdalla;
    private TextField[] edits;
    private String[] tilat;
    
    
    /**
     * Alustaa muokkaus-/lisäysikkunan luomalla tekstikentät ja ComboBoxChooserin
     */
    private void alusta() {
        this.edits = luoKentat(this.gridKirja);
        this.tilat = this.kirjasto.annaTilat();
        this.cbTeoksenTila.setRivit(this.tilat);
        this.cbTeoksenTila.addSelectionListener(e -> asetaTila());
        for (TextField edit : edits) {
            if (edit != null)
                edit.setOnKeyReleased(e -> kasitteleMuutosKirjaan((TextField)(e.getSource())));
        }
    }
    
    
    /**
     * Luodaan GridPaneen kirjan tiedot, sekä labelin että textfieldin
     * @param gridKirja mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridKirja) {
        gridKirja.getChildren().clear();
        TextField[] edits = new TextField[apuKirja.getKenttia()];
        
        for (int i=0, k = apuKirja.ekaKentta(); k < apuKirja.getKenttia(); k++, i++) {
            Label label = new Label(apuKirja.getKysymys(k));
            gridKirja.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridKirja.add(edit, 1, i);
        }
        return edits;
    }
    
    
    /**
     * Luodaan kirjan kysymisdialogi ja palautetaan joko
     * sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus minkä kirjan tiedot näytetään oletuksena
     * @param otsikko ikkunan otsikko (erilainen muokattaessa/lisättäessä)
     * @param kirjasto kirjasto, joka asetetaan kontrollerille atrribuutiksi
     * @return null jos painetaan Peruuta, muuten täytetty tietue
     */
    public static Kirja kysyKirja(Stage modalityStage, Kirja oletus, String otsikko, Kirjasto kirjasto) {
        return ModalController.<Kirja, MuokkaaLisaaGUIController>showModal(
                MuokkaaLisaaGUIController.class.getResource("MuokkaaLisaa.fxml"), 
                otsikko, modalityStage, oletus, ctrl -> ctrl.setKirjasto(kirjasto)
                );
    }
    
    
    /**
     * Näytetään kirjan tiedot TextField-komponentteihin
     * @param ed taulukko TextFieldeistä johon näytetään
     * @param kirja näytettävä kirja
     */
    public void naytaKirja(TextField[] ed, Kirja kirja) {
        if (kirja == null) return;
        if (this.kirjasto.annaTekija(kirja) == null) this.editTekija.setText("");
            else this.editTekija.setText(this.kirjasto.annaTekija(kirja).getSukunimiEtunimi());
        for (int k = kirja.ekaKentta(); k < kirja.getKenttia(); k++) {
            String annettava = kirja.anna(k);
            if (annettava.equals("0") || annettava.equals("0.0")) ed[k].setText("");
                else ed[k].setText(annettava);
        if (this.kirjasto.annaAsiasanatJonona(kirja) == null) this.editAsiasanat.setText("");
            else this.editAsiasanat.setText(this.kirjasto.annaAsiasanatJonona(kirja));
        
        String tila = kirja.getTila();
        int paikka = 0;
        for (int i = 0; i < this.tilat.length; i++) {
            if (this.tilat[i].equals(tila)) {
                paikka = i;
                break;
            }
        }
        this.cbTeoksenTila.getSelectionModel().select(paikka);
        }
    }
    
    
    /**
     * Käsitellään kirjaan (tekstikenttiin) tullut muutos
     * asettamalla käyttäjän antamat tiedot kirjan attribuutteihin.
     * @param edit muuttunut kenttä
     */
    private void kasitteleMuutosKirjaan(TextField edit) {
        if (this.kirjaKohdalla == null) return;
        int k = getFieldId(edit,apuKirja.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = this.kirjaKohdalla.aseta(k,s); 
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }        
    }
    
    
    /**
     * Asettaa kirjan tilan
     */
    private void asetaTila() {
        int tilaI = cbTeoksenTila.getSelectionModel().getSelectedIndex();
        String tila = this.kirjasto.annaTilat()[tilaI];
        this.kirjaKohdalla.setTila(tila);
    }
    
    
    /**
     * Liittää kirjalle tekijän id:n. Kirjasto hoitaa varsinaisen työn.
     * @param edit kenttä, johon tekijätieto on syötetty
     */
    private void kasitteleMuutosTekijaan(TextField edit) {
        if (this.kirjaKohdalla == null) return;
        String s = edit.getText();
        Tekija tekija = new Tekija();
        tekija.parse(s, ',');
        this.kirjasto.kirjalleTekija(tekija, this.kirjaKohdalla);        
    }
    
    
    /**
     * Tallentaa asiasanojen muutokset. Kirjasto hoitaa varsinaisen työn.
     * @param edit kenttä, johon asiasanat on syötetty
     */
    private void kasitteleMuutosAsiasanoihin(TextField edit) {
        if (this.kirjaKohdalla == null) return;
        String s = edit.getText();
        List<Asiasana> asLista = this.kirjasto.parse(s, ',');
        this.kirjasto.kirjalleAsiasanat(asLista, this.kirjaKohdalla);
    }

    
    /**
     * Antaa kentän numeron
     * @param obj kenttä
     * @param oletus oletuskentän numero, jota käytetään jollei saada muuta numeroa irti
     * @return kentän numero
     */
    private static int getFieldId(Object obj, int oletus) {
        if (!(obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        String s = node.getId();
        if (s.length() < 1) return oletus;
        return Mjonot.erotaInt(s.substring(1),oletus);
    }


    /**
     * Näyttää virheen virheLabelissa, virhetyylillä
     * @param virheLabeliin laitettava teksti
     */
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe"); 
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
    * Laittaa ikkunan avautuessa tekijäkentän (eli ylimmän) valituksi
    */
    @Override
    public void handleShown() {
        this.editTekija.requestFocus();  
    }

    
    /**
     * Palauttaa käsitellyn kirjan.
     */
    @Override
    public Kirja getResult() {
        return this.kirjaKohdalla;
    }

    
    @Override
    public void setDefault(Kirja oletus) {
        this.kirjaKohdalla = oletus;      
    }
    

    /**
     * Asettaa kirjaston, lisäksi alustaa ikkunan ja näyttää kirjan siinä
     * (ModalControllerin metodien käsittelyjärjestyksen vuoksi hoidettava
     * myös alustus ja näyttäminen tästä.)
     * @param kirjasto käyttöön asetettava kirjasto
     */
    public void setKirjasto(Kirjasto kirjasto) {
        this.kirjasto = kirjasto;     
        alusta();
        naytaKirja(edits, kirjaKohdalla);
    }

}
