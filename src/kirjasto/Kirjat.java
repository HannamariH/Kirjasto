/**
 * 
 */
package kirjasto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Kirjaston kirjat, osaa mm. lisätä uuden kirjan.
 * 
 * @author Hannamari Heiniluoma
 * @version 26.4.2019
 *
 */
public class Kirjat {
    
    private static final int MAX_KIRJOJA = 10;
    private boolean muutettu = false;
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "kirjat";
    private Kirja[] alkiot = new Kirja[MAX_KIRJOJA];
    private int lkm;
    private String[] tilat = {"hyllyssä", "lainassa", "kadonnut", "hankinnassa"};    
    
    
    /**
     * Lisää uuden kirjan tietorakenteeseen.
     * @param kirja lisättävän kirjan viite
     * @example
     * <pre name="test">
     * Kirjat kirjat = new Kirjat();
     * Kirja sinuhe1 = new Kirja(), sinuhe2 = new Kirja();
     * kirjat.getLkm() === 0;
     * kirjat.lisaa(sinuhe1); kirjat.getLkm() === 1;
     * kirjat.lisaa(sinuhe2); kirjat.getLkm() === 2;
     * kirjat.lisaa(sinuhe1); kirjat.getLkm() === 3;
     * kirjat.anna(0) === sinuhe1;
     * kirjat.anna(1) === sinuhe2;
     * kirjat.anna(2) === sinuhe1;
     * kirjat.anna(1) == sinuhe1 === false;
     * kirjat.anna(1) == sinuhe2 === true;
     * kirjat.anna(3) === sinuhe1; #THROWS IndexOutOfBoundsException
     * kirjat.lisaa(sinuhe1); kirjat.getLkm() === 4;
     * kirjat.lisaa(sinuhe1); kirjat.getLkm() === 5;
     * kirjat.lisaa(sinuhe1);
     * kirjat.lisaa(sinuhe1);
     * kirjat.lisaa(sinuhe1);
     * kirjat.lisaa(sinuhe1);
     * kirjat.lisaa(sinuhe1);
     * kirjat.lisaa(sinuhe1);  
     * </pre>
     */
    public void lisaa(Kirja kirja) {
        if (this.lkm >= this.alkiot.length) {
            Kirja[] uusi = new Kirja[this.alkiot.length * 2];
            for (int i = 0; i < this.alkiot.length; i++) {
                uusi[i] = this.alkiot[i];
            }
            this.alkiot = uusi;
        }
        this.alkiot[this.lkm] = kirja;
        this.lkm++;
        this.muutettu = true;
    }
    
    
    /**
     * Korvaa kirjan tietorakenteessa, jos samalla id:llä on jo kirja.
     * Muuten lisää kirjan uutena.
     * @param kirja lisättävä kirja
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     * Kirjat kirjat = new Kirjat();
     * Kirja kirja1 = new Kirja();
     * Kirja kirja2 = new Kirja();
     * kirja1.rekisteroi();
     * kirja2.rekisteroi();
     * kirjat.getLkm() === 0;
     * kirjat.korvaaTaiLisaa(kirja1); kirjat.getLkm() === 1;
     * kirjat.korvaaTaiLisaa(kirja2); kirjat.getLkm() === 2;
     * Kirja kirja3 = kirja1.clone();
     * kirja3.aseta(2, "Romaani");
     * kirjat.anna(0) == kirja1 === true;
     * kirjat.korvaaTaiLisaa(kirja3); kirjat.getLkm() === 2;
     * kirjat.anna(0) == kirja3 === true;
     * </pre>
     */
    public void korvaaTaiLisaa(Kirja kirja) {
        int id = kirja.getId();
        for (int i = 0; i < this.lkm; i++) {
            if (alkiot[i].getId() == id) {
                alkiot[i] = kirja;
                muutettu = true;
                return;
            }
        }
        lisaa(kirja);
    }
    
    
    /**
     * Palauttaa pyydetyssä indeksissä olevan kirjan.
     * @param i taulukon indeksi, josta kirja halutaan
     * @return paikassa i oleva kirja
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Kirja anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i) {
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        }
        return this.alkiot[i];
    }
    
    
    /**
     * Etsii ja antaa kirjan, jolla on haluttu id-numero
     * @param kid kirjan id-numero
     * @return kirja, jolla on haluttu id. Jos tällaista ei löydy, palautetaan null.
     * @example
     * <pre name="test">
     * Kirjat kirjat = new Kirjat();
     * Kirja kirja = new Kirja();
     * kirja.setId(8);
     * kirjat.lisaa(kirja);
     * kirjat.annaKirjaIdlla(8) === kirja;
     * </pre>
     */
    public Kirja annaKirjaIdlla(int kid) {
        for (int i = 0; i < this.alkiot.length; i++) {
            if (this.alkiot[i] != null && this.alkiot[i].getId() == kid) {
                return this.alkiot[i];
            }
        }
        return null;
    }    
    
    
    /**
     * Palauttaa kirjaston kirjojen lukumäärän.
     * @return kirjojen lukumäärä taulukossa
     */
    public int getLkm() {
        return this.lkm;
    }
    
    
    /**
     * Asettaa kirjalle tilan.
     * @param kirja kirja, jolle tila asetetaan
     * @param tila asetettava tila
     */
    public void asetaTila(Kirja kirja, String tila) {
        kirja.setTila(tila);
    }
    
    
    /**
     * @return tilat String-taulukkona
     */
    public String[] getTilat() {
        return this.tilat;
    }
    
    
    /**
     * Poistaa kirjan taulukosta. Vähentää taulukon
     * lkm-määrää yhdellä ja siirtää poistetun jäljessä
     * taulukossa olevia kirjoja askeleen eteenpäin. 
     * @param id poistettavan kirjan id
     * @return 1 jos poisto onnistui, 0 jos kirjaa ei löytynyt.
     * @example
     * <pre name="test">
     * Kirjat kirjat = new Kirjat();
     * Kirja k1 = new Kirja();
     * Kirja k2 = new Kirja();
     * Kirja k3 = new Kirja();
     * k1.rekisteroi();
     * k2.rekisteroi();
     * k3.rekisteroi();
     * int id1 = k1.getId();
     * kirjat.lisaa(k1); kirjat.lisaa(k2); kirjat.lisaa(k3);
     * kirjat.poista(id1 + 1) === 1;
     * kirjat.annaKirjaIdlla(id1 + 1) === null;
     * kirjat.getLkm() === 2; 
     * kirjat.poista(id1) === 1; kirjat.getLkm() === 1; 
     * kirjat.poista(id1+3) === 0; kirjat.getLkm() === 1;
     * </pre>
     */
    public int poista(int id) {
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1;
    }
    
    
    /**
     * Etsii id:n perusteella kirjan indeksin taulukossa
     * @param id kirjan id
     * @return kirjan indeksi
     */
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getId()) return i; 
        return -1; 
    }
    
    
    /**
     * Etsii kirjoista ne, jotka täsmäävät annettuun hakusanaan
     * @param hakuehto käyttäjän syöttämä hakusana
     * @param k kentän numero
     * @return lista, jolla on hakuun täsmäävät kirjat
     * @example
     * <pre name="test">
     * #import java.util.List;
     * Kirjat kirjat = new Kirjat();
     * Kirja k1 = new Kirja();
     * k1.parse("1|3|Sinuhe Egyptiläinen|2019");
     * Kirja k2 = new Kirja();
     * k2.parse("2|4|Rikosromaani|2008");
     * Kirja k3 = new Kirja();
     * k3.parse("3|7|Lastenkirja|2018");
     * Kirja k4 = new Kirja();
     * k4.parse("4|6|Keittokirja|1997");
     * Kirja k5 = new Kirja();
     * k5.parse("5|1|Raamattu|2000");
     * kirjat.lisaa(k1); kirjat.lisaa(k2); kirjat.lisaa(k3); kirjat.lisaa(k4); kirjat.lisaa(k5);
     * List<Kirja> loytyneet;
     * loytyneet = kirjat.etsi("*s*", 2);
     * loytyneet.size() === 3;
     * loytyneet.get(0) == k3 === true;  
     * loytyneet.get(1) == k2 === true;
     * loytyneet.get(2) == k1 === true;
     * loytyneet = kirjat.etsi("*19*", 3);
     * loytyneet.size() === 2;
     * loytyneet.get(0) == k4 === true;  
     * loytyneet.get(1) == k1 === true;
     * loytyneet = kirjat.etsi(null,-1);  
     * loytyneet.size() === 5; 
     * </pre>
     */
    public List<Kirja> etsi(String hakuehto, int k) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto;
        List<Kirja> loytyneet = new ArrayList<Kirja>();
        for (Kirja kirja : this.alkiot) {
            if (kirja != null && WildChars.onkoSamat(kirja.anna(k), ehto)) loytyneet.add(kirja);
        }
        Collections.sort(loytyneet);
        return loytyneet;
    }
    
    
    /**
     * Etsii ja palauttaa listalla kaikki tietyn tekijän kirjat
     * @param tid tekijän id-numero
     * @return lista tekijän kirjoista
     */
    public Collection<Kirja> etsiTekijanKirjat(int tid) {
        Collection<Kirja> kirjat = new ArrayList<>();
        for (int i = 0; i < alkiot.length; i++) {
            Kirja tutkittava = alkiot[i];
            if (tutkittava != null && tutkittava.getTekijanId() == tid) kirjat.add(tutkittava);
        }
        return kirjat;
    }
    
    
    /**
     * @return kirjakokoelman (kirjaston) nimi
     */
    public String getKokoNimi() {
        return this.kokoNimi;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return this.tiedostonPerusNimi;
    }


    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        this.tiedostonPerusNimi = nimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }
    
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return this.tiedostonPerusNimi + ".bak";
    }
    
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedosto(getTiedostonPerusNimi());
    }
    
    
    /**
     * Lukee kirjat tiedostosta.
     * @param tiedosto tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     * #import java.util.Iterator;
     * 
     * Kirjat kirjat = new Kirjat();
     * Kirja kirja1 = new Kirja(), kirja2 = new Kirja();
     * kirja1.taytaSinuhe(1);
     * kirja2.taytaSinuhe(1);
     * String hakemisto = "testikirjasto";
     * String tiedNimi = hakemisto + "/kirjat";
     * File ftied = new File(tiedNimi + ".dat");
     * File dir = new File(hakemisto);
     * dir.mkdir();
     * ftied.delete();
     * kirjat.lueTiedosto(tiedNimi);  #THROWS SailoException
     * kirjat.lisaa(kirja1);
     * kirjat.lisaa(kirja2);
     * kirjat.tallenna();
     * kirjat = new Kirjat();
     * kirjat.lueTiedosto(tiedNimi);
     * kirjat.anna(0).toString() === kirja1.toString();
     * kirjat.anna(1).toString() === kirja2.toString();
     * kirjat.lisaa(kirja2);
     * kirjat.tallenna();
     * ftied.delete() === true;
     * File fbak = new File(tiedNimi + ".bak");
     * fbak.delete() === true;
     * dir.delete() === true;
     * </pre>
     */
    public void lueTiedosto(String tiedosto) throws SailoException {
        setTiedostonPerusNimi(tiedosto);
        try (Scanner fi = new Scanner(new FileInputStream(new File(getTiedostonNimi())))) {
            String rivi;
            while (fi.hasNext()) {
                rivi = fi.nextLine();   
                rivi = rivi.trim();
                if (rivi.equals("")) continue;
                Kirja kirja = new Kirja();
                kirja.parse(rivi);
                lisaa(kirja);
            }
            this.muutettu = false;
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea.");
        }
    }
    
    
    /**
     * Tallentaa kirjat tiedostoon
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied.getCanonicalPath(), true))) {
            for (int i = 0; i < this.lkm; i++) {
                Kirja kirja = this.alkiot[i];
                fo.println(kirja.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }

}
