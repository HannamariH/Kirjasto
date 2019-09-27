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
import java.util.List;
import java.util.Scanner;

/**
 * Pitää yllä listaa kirjaston kaikista asiasana-kirja-pareista.
 * 
 * @author Hannamari Heiniluoma
 * @version 26.4.2019
 *
 */
public class AsiasanaKirjaParit {
    
    private boolean muutettu = false;
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "liima";
    private ArrayList<AsiasanaKirjaPari> lista = new ArrayList<AsiasanaKirjaPari>();
    
    
    
    /**
     * Lisää listalle AsiasanaKirjaParin
     * @param akp listalle lisättävä AsiasanaKirjaPari
     */
    public void lisaa(AsiasanaKirjaPari akp) {
        this.lista.add(akp);
        this.muutettu = true;
    }
    
    
    /**
     * @param akp poistettava AsiasanaKirjaPari
     */
    public void poista(AsiasanaKirjaPari akp) {
        this.lista.remove(akp);
        this.muutettu = true;
    }
    
    
    /**
     * Palauttaa kirjaston asiasana-kirja-parien määrän.
     * @return asiasana-kirja-parien lukumäärä listalla
     */
    public int getLkm() {
        return this.lista.size();
    }
    
    
    /**
     * Palauttaa pyydetyssä indeksissä olevan AsiasanaKirjaParin.
     * @param i listan indeksi, josta AsiasanaKirjaPari halutaan
     * @return paikassa i oleva AsiasanaKirjaPari
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public AsiasanaKirjaPari anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || this.lista.size() <= i) {
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        }
        return this.lista.get(i);
    }
    
    /**
     * Hakee listalle ne AsiasanaKirjaParit, jotka kuuluvat ko. kirjalle
     * @param kirja kirja, jonka asiasanat halutaan
     * @return lista AsiasanaKirjaPareista, joissa on halutun kirjan id (eli kuuluvat ko. kirjaan)
     */
    public List<AsiasanaKirjaPari> annaAsiasanat(Kirja kirja) {
        List<AsiasanaKirjaPari> asiasanaparit = new ArrayList<>();
        for (AsiasanaKirjaPari akp: this.lista) {
            if (akp.getKirjaId() == kirja.getId()) {
                asiasanaparit.add(akp);
            }
        }
        return asiasanaparit;
    }
    
    
    /**
     * Hakee listalle ne AsiasanaKirjaParit, jotka kuuluvat ko. asiasanalle
     * @param asiasana asiasana, jonka kirjat halutaan
     * @return lista AsiasanaKirjaPareista, joissa on halutun asiasanan id (eli koskevat ko. asiasanaa)
     */
    public ArrayList<AsiasanaKirjaPari> annaKirjat(Asiasana asiasana) {
        ArrayList<AsiasanaKirjaPari> asiasanaparit = new ArrayList<>();
        for (AsiasanaKirjaPari akp: this.lista) {
            if (akp.getAsiasanaId() == asiasana.getId()) {
                asiasanaparit.add(akp);       
            }
        }
        return asiasanaparit;
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
     * @return asiasanaKirjaParilistan nimi
     */
    public String getKokoNimi() {
        return this.kokoNimi;
    }
   
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedosto(getTiedostonPerusNimi());
    }
    
    
    /**
     * @param tiedosto tiedoston perusnimi
     * @throws SailoException jos tiedosto ei aukea
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     * AsiasanaKirjaParit akp = new AsiasanaKirjaParit();
     * AsiasanaKirjaPari akp1 = new AsiasanaKirjaPari(), akp2 = new AsiasanaKirjaPari();
     * String hakemisto = "testikirjasto";
     * String tiedNimi = hakemisto + "/tekijat";
     * File ftied = new File(tiedNimi + ".dat");
     * File dir = new File(hakemisto);
     * dir.mkdir();
     * ftied.delete();
     * akp.lueTiedosto(tiedNimi);  #THROWS SailoException
     * akp.lisaa(akp1);
     * akp.lisaa(akp2);
     * akp.tallenna();
     * akp = new AsiasanaKirjaParit();
     * akp.lueTiedosto(tiedNimi);
     * akp.anna(0).toString() === akp1.toString();
     * akp.anna(1).toString() === akp2.toString();
     * akp.lisaa(akp2);
     * akp.tallenna();
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
                AsiasanaKirjaPari akp = new AsiasanaKirjaPari();
                akp.parse(rivi);
                lisaa(akp);
            }
            this.muutettu = false;
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea.");
        }
    }
    
    
    /**
     * Tallentaa asiasanat tiedostoon
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied.getCanonicalPath(), true))) {
            for (AsiasanaKirjaPari akp : this.lista) {
                fo.println(akp.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }   
    
}
