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
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Pitää yllä listaa tekijöistä, osaa hakea, lisätä ja poistaa niitä.
 * 
 * @author Hannamari Heiniluoma
 * @version 26.4.2019
 *
 */
public class Tekijat {
    
    private boolean muutettu = false;
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "tekijat";
    private final ArrayList<Tekija> lista = new ArrayList<Tekija>();
    
    
    
    /**
     * Lisää listalle tekijän
     * @param tekija listalle lisättävä tekijä
     */
    public void lisaa(Tekija tekija) {
        this.lista.add(tekija);
        this.muutettu = true;
    }
    
    
    /**
     * Selvittää, onko tutkittava tekijä jo olemassa tietorakenteessa.
     * @param tekija tutkittava tekijä
     * @return ko. Tekijan jos löytyy, muuten null
     * @example
     * <pre name="test">
     * Tekijat tekijat = new Tekijat();
     * Tekija mika = new Tekija();
     * mika.setEtunimi("Mika");
     * mika.setSukunimi("Waltari");
     * Tekija kalle = new Tekija();
     * kalle.setEtunimi("Kalle");
     * kalle.setSukunimi("Päätalo");
     * Tekija enni = new Tekija();
     * enni.setEtunimi("Enni");
     * enni.setSukunimi("Mustonen");
     * Tekija pirjo = new Tekija();
     * pirjo.setEtunimi("Pirjo");
     * pirjo.setSukunimi("Tuominen");
     * tekijat.lisaa(mika); tekijat.lisaa(kalle); tekijat.lisaa(enni);
     * tekijat.onkoJo(mika) === mika;
     * tekijat.onkoJo(pirjo) === null;
     * </pre>
     */
    public Tekija onkoJo(Tekija tekija) {
        Tekija verrattava;
        for (int i = 0; i < this.lista.size(); i++) {
            verrattava = this.lista.get(i);
            if (verrattava.compareTo(tekija) == 0) return verrattava;
        }
        return null;
    }
        
    
    /**
     * Palauttaa pyydetyssä indeksissä olevan tekijän.
     * @param i listan indeksi, josta tekijä halutaan
     * @return paikassa i oleva tekijä
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Tekija anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || this.lista.size() <= i) {
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        }
        return this.lista.get(i);
    }
    
    
    /**
     * Palauttaa tekijän, jolla on haluttu id-numero
     * @param id tekijän id-numero
     * @return tekijä, jolla on ko. numero. Jos numeroa ei löydy, palautetaan null.
     */
    public Tekija annaIdlla(int id) {
        for (int i = 0; i < this.lista.size(); i++) {
            if (this.lista.get(i).getId() == id) {
                return this.lista.get(i);
            }
        }
        return null;
    }
    
    
    /**
     * Palauttaa kirjaston tekijöiden lukumäärän.
     * @return tekijöiden lukumäärä listalla
     */
    public int getLkm() {
        return this.lista.size();
    }
    
    
    /**
     * Etsii tekijöistä ne, jotka täsmäävät annettuun hakusanaan
     * @param hakuehto käyttäjän syöttämä hakusana
     * @return lista, jolla on hakuun täsmäävät tekiät
     */
    public Collection<Tekija> etsi(String hakuehto) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto;
        Collection<Tekija> loytyneet = new ArrayList<Tekija>();
        for (Tekija tekija : this.lista) {
            if (WildChars.onkoSamat(tekija.getSukunimiEtunimi(), ehto)) loytyneet.add(tekija);
        }
        return loytyneet;
    }
    
    
    /**
     * Antaa halutun tekijän id-numeron
     * @param tekija tekijä, jonka numero halutaan
     * @return tekijän id-numero
     */
    public int annaTekijanId(Tekija tekija) {
        return tekija.getId();
    }
    
    
    /**
     * @return tekijälistan nimi
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
     * @param tiedosto tiedoston perusnimi
     * @throws SailoException jos tiedosto ei aukea
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     * #import java.util.Iterator;
     * 
     * Tekijat tekijat = new Tekijat();
     * Tekija tekija1 = new Tekija(), tekija2 = new Tekija();
     * String hakemisto = "testikirjasto";
     * String tiedNimi = hakemisto + "/tekijat";
     * File ftied = new File(tiedNimi + ".dat");
     * File dir = new File(hakemisto);
     * dir.mkdir();
     * ftied.delete();
     * tekijat.lueTiedosto(tiedNimi);  #THROWS SailoException
     * tekijat.lisaa(tekija1);
     * tekijat.lisaa(tekija2);
     * tekijat.tallenna();
     * tekijat = new Tekijat();
     * tekijat.lueTiedosto(tiedNimi);
     * tekijat.anna(0).toString() === tekija1.toString();
     * tekijat.anna(1).toString() === tekija2.toString();
     * tekijat.lisaa(tekija2);
     * tekijat.tallenna();
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
                Tekija tekija = new Tekija();
                tekija.parse(rivi);
                lisaa(tekija);
            }
            this.muutettu = false;
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea.");
        }
    }
    
    
    /**
     * Tallentaa tekijät tiedostoon
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied.getCanonicalPath(), true))) {
            for (Tekija tekija : this.lista) {
                fo.println(tekija.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }

}
