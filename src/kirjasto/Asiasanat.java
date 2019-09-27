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
import java.util.List;
import java.util.Scanner;

import fi.jyu.mit.ohj2.Mjonot;
import fi.jyu.mit.ohj2.WildChars;

/**
 * Kirjastossa käytössä olevat asiasanat. Ylläpitää listaa asiasanoista,
 * osaa hakea, lisätä ja poistaa niitä.
 * 
 * @author Hannamari Heiniluoma
 * @version 25.4.2019
 *
 */
public class Asiasanat {
    
    private boolean muutettu = false;
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "asiasanat";
    private ArrayList<Asiasana> lista = new ArrayList<Asiasana>();
    
    
    /**
     * Lisää listalle asiasanan
     * @param asiasana listalle lisättävä asiasana
     */
    public void lisaa(Asiasana asiasana) {
        this.lista.add(asiasana);
        this.muutettu = true;
    }
    
    
    /**
     * Palauttaa pyydetyssä indeksissä olevan asiasanan.
     * @param i listan indeksi, josta asiasana halutaan
     * @return paikassa i oleva asiasana
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     * @example
     * <pre name="test">
     * Asiasanat asiasanat = new Asiasanat();
     * Asiasana as1 = new Asiasana();
     * Asiasana as2 = new Asiasana();
     * Asiasana as3 = new Asiasana();
     * asiasanat.lisaa(as1);
     * asiasanat.lisaa(as2);
     * asiasanat.lisaa(as3);
     * Asiasana haettu = asiasanat.anna(2);
     * haettu.toString() === as2.toString();
     * </pre>
     */
    public Asiasana anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || this.lista.size() <= i) {
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        }
        return this.lista.get(i);
    }
    
    
    /**
     * Palauttaa asiasanan, jolla on haluttu id-numero
     * @param id asiasanan id-numero
     * @return asiasana, jolla on ko. numero. Jos numeroa ei löydy, palautetaan null.
     */
    public Asiasana annaIdlla(int id) {
        for (int i = 0; i < this.lista.size(); i++) {
            if (this.lista.get(i).getId() == id) return this.lista.get(i);
        }
        return null;
    }
    
    
    /**
     * Palauttaa kirjaston asiasanojen lukumäärän.
     * @return asiasanojen lukumäärä listalla
     */
    public int getLkm() {
        return this.lista.size();
    }
    
    
    /**
     * Selvittää, onko tutkittava asiasana jo tieorakenteessa
     * @param asiasana tutkittava asiasana
     * @return ko. Asiasana jos se löytyy, muuten null
     * @example
     * <pre name="test">
     * Asiasanat asiasanat = new Asiasanat();
     * Asiasana kissa = new Asiasana();
     * kissa.setAsiasana("kissa");
     * asiasanat.lisaa(kissa);
     * Asiasana koira = new Asiasana();
     * koira.setAsiasana("koira");
     * asiasanat.lisaa(koira);
     * Asiasana kana = new Asiasana();
     * kana.setAsiasana("kana");
     * asiasanat.lisaa(koira);
     * Asiasana kani = new Asiasana();
     * kani.setAsiasana("kani");
     * asiasanat.onkoJo(kani) === null;
     * asiasanat.onkoJo(koira) === koira;
     * </pre>
     */
    public Asiasana onkoJo(Asiasana asiasana) {
        Asiasana verrattava;
        for (int i = 0; i < this.lista.size(); i++) {
            verrattava = this.lista.get(i);
            if (verrattava.compareTo(asiasana) == 0) return verrattava;
        }
        return null;
    }
    
    
    /**
     * Parsii merkillä erotetusta stringistä asiasanat ja 
     * palauttaa ne listana
     * @param rivi asiasanat
     * @param merkki merkki jonka kohdalta erotellaan
     * @return lista parsimalla luoduista asiasanoista
     * @example
     * <pre name="test">
     * #import java.util.List;
     * #import java.util.ArrayList;
     * Asiasanat asiasanat = new Asiasanat();
     * List<Asiasana> lista = asiasanat.parse("kissa, koira, kana", ',');
     * Asiasana kissa = new Asiasana();
     * kissa.setAsiasana("kissa");
     * Asiasana koira = new Asiasana();
     * koira.setAsiasana("koira");
     * Asiasana kana = new Asiasana();
     * kana.setAsiasana("kana");
     * ArrayList<Asiasana> toinenLista = new ArrayList<Asiasana>();
     * toinenLista.add(kissa);
     * toinenLista.add(koira);
     * toinenLista.add(kana);
     * lista.toString() === toinenLista.toString();
     * </pre>
     */
     public List<Asiasana> parse(String rivi, char merkki) {
         StringBuilder sb = new StringBuilder(rivi);
         List<Asiasana> asLista = new ArrayList<>();
         for (int i = 0; i < sb.length(); i++) {
            Asiasana asiasana = new Asiasana();
            String as = Mjonot.erota(sb, merkki).trim();
            asiasana.setAsiasana(as);
            asLista.add(asiasana);
         }         
         return asLista;
     }     
     
     
     /**
      * Etsii asiasanoista ne, jotka täsmäävät annettuun hakusanaan
      * @param hakuehto käyttäjän syöttämä hakusana
      * @return lista, jolla on hakuun täsmäävät asiasanat
      */
     public Collection<Asiasana> etsi(String hakuehto) {
         String ehto = "*";
         if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto;
         Collection<Asiasana> loytyneet = new ArrayList<Asiasana>();
         for (Asiasana asiasana : this.lista) {
             if (WildChars.onkoSamat(asiasana.getAsiasana(), ehto)) loytyneet.add(asiasana);
         }
         return loytyneet;
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
     * @return asiasanalistan nimi
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
     * #import java.util.Iterator;
     * 
     * Asiasanat asiasanat = new Asiasanat();
     * Asiasana as1 = new Asiasana(), as2 = new Asiasana();
     * String hakemisto = "testikirjasto";
     * String tiedNimi = hakemisto + "/tekijat";
     * File ftied = new File(tiedNimi + ".dat");
     * File dir = new File(hakemisto);
     * dir.mkdir();
     * ftied.delete();
     * asiasanat.lueTiedosto(tiedNimi);  #THROWS SailoException
     * asiasanat.lisaa(as1);
     * asiasanat.lisaa(as2);
     * asiasanat.tallenna();
     * asiasanat = new Asiasanat();
     * asiasanat.lueTiedosto(tiedNimi);
     * asiasanat.anna(0).toString() === as1.toString();
     * asiasanat.anna(1).toString() === as2.toString();
     * asiasanat.lisaa(as2);
     * asiasanat.tallenna();
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
                Asiasana as = new Asiasana();
                as.parse(rivi);
                lisaa(as);
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
            for (Asiasana as : this.lista) {
                fo.println(as.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }

}
