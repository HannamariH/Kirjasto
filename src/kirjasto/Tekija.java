/**
 * 
 */
package kirjasto;

import static kanta.ISBNtarkistus.rand;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Tekijä, joka mm. huolehtii omasta id-numerostaan.
 * 
 * @author Hannamari Heiniluoma
 * @version 26.4.2019
 *
 */
public class Tekija implements Comparable<Tekija> {
    
    private int id;
    private String etunimi;
    private String sukunimi;
    
    private static int seuraavaId = 1;
     

    
    /**
     * Antaa tekijälle seuraavan id-numeron
     * @return lisätyn tekikjän id-numero
     * @example
     * <pre name="test">
     * Tekija mika = new Tekija();
     * mika.getId() === 0;
     * mika.rekisteroi();
     * Tekija mika2 = new Tekija();
     * mika2.rekisteroi();
     * int id1 = mika.getId();
     * int id2 = mika2.getId();
     * id1 === id2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.id = seuraavaId;
        seuraavaId++;
        return this.id;
    }
    
    
    /**
     * Palauttaa tekijän id-numeron
     * @return tekijän id-numero
     */
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Asettaa uuden id-numeron ja samalla varmistaa, että
     * seuraava id on aina suurempi kuin tähän mennessä suurin.
     * @param nro asetettava tekijän id-numero
     */
    private void setId(int nro) {
        this.id = nro;
        if (this.id >= seuraavaId ) seuraavaId = this.id + 1;
    }


    /**
     * @return etunimi
     */
    public String getEtunimi() {
        return this.etunimi;
    }


    /**
     * @param etunimi asetettava etunimi
     */
    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }


    /**
     * @return sukunimi
     */
    public String getSukunimi() {
        return this.sukunimi;
    }


    /**
     * @param sukunimi asetettava sukunimi
     */
    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }
    
    
    /**
     * Palauttaa tekijän nimen näyttöön tulostamista varten.
     * @return nimi muodossa sukunimi, etunimi
     */
    public String getSukunimiEtunimi() {
        return this.sukunimi + ", " + this.etunimi;
    }
    
    
    /**
     * Palauttaa tekijän tiedot merkkijonona, jonka voi tallentaa tiedostoon.
     * @return tekijä tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     * Tekija tekija = new Tekija();
     * tekija.parse("   4  |  Mika|  Waltari");
     * tekija.toString().startsWith("4|Mika|Waltari") === true;
     * </pre>
     */
    @Override
    public String toString() {
        return this.id + "|" + this.etunimi + "|" + this.sukunimi;
    }
    
    
     /**
     * Selvittää tekijän tiedot "|"-merkillä erotellusta merkkijonosta.
     * Huolehtii, että seuraavaId on suurempi kuin tuleva id.
     * @param rivi rivi, josta tekijän tiedot otetaan
     * @example
     * <pre name="test">
     * Tekija tekija = new Tekija();
     * tekija.parse("   4  |  Mika|  Waltari");
     * tekija.getId() === 4;
     * tekija.toString().startsWith("4|Mika|Waltari") === true;
     * </pre>
     */
     public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setId(Mjonot.erota(sb, '|', getId()));
        this.etunimi = Mjonot.erota(sb, '|', this.etunimi);
        this.sukunimi = Mjonot.erota(sb, '|', this.sukunimi);
     }
    
    
     /**
     * Parsii sukunimi, etunimi -stringistä tekijälle nimet
     * @param rivi tekijän tiedot 
     * @param merkki merkki jonka kohdalta erotellaan
     * @example
     * <pre name="test">
     * Tekija tekija = new Tekija();
     * tekija.parse("Pakkanen, Pekka", ',');
     * tekija.getSukunimi() === "Pakkanen";
     * tekija.getEtunimi() === "Pekka";
     * tekija.getSukunimiEtunimi() === "Pakkanen, Pekka";
     * </pre>
     */
     public void parse(String rivi, char merkki) {
         StringBuilder sb = new StringBuilder(rivi);
         this.sukunimi = Mjonot.erota(sb, merkki, this.sukunimi);
         this.etunimi = Mjonot.erota(sb, merkki, this.etunimi);
     }
    
       
    /**
     * Vertaa kahta tekijää (etu- ja sukunimiä, ei välitä kirjainkoosta)
     * @param verrattava verrattava Tekija
     * @return 0 jos tekijät ovat samoja, -1 jos erilaisia
     * @example
     * <pre name="test">
     * Tekija t1 = new Tekija();
     * t1.setEtunimi("Kalle");
     * t1.setSukunimi("Päätalo");
     * Tekija t2 = new Tekija();
     * t2.setEtunimi("Kille");
     * t2.setSukunimi("Puutalo");
     * Tekija t3 = new Tekija();
     * t3.setEtunimi("kalle");
     * t3.setSukunimi("päätalo");
     * t1.compareTo(t2) === -1;
     * t1.compareTo(t3) === 0;
     * </pre>
     */
    @Override
    public int compareTo(Tekija verrattava) {
        if (this.etunimi.equalsIgnoreCase(verrattava.getEtunimi()) && 
                this.sukunimi.equalsIgnoreCase(verrattava.getSukunimi())) return 0;
        return -1;
    }
    
    
    /**
     * Tulostetaan tekijän tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("Tekijä: " + this.etunimi + " " + this.sukunimi);
    }
    
    
    /**
     * Tulostetaan tekijän tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    
//--------------------------------------------------------------------------
//alla vanha rakennusteline, joka kuitenkin testejä varten tarpeen
       
    /**
     * Asettaa tekijän nimeksi Mika Waltari
     */
    public void taytaWaltari() {
        this.etunimi = "Mika";
        this.sukunimi= "Waltari " + rand(1000,9999);
    }
}
