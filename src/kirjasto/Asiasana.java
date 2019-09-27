/**
 * 
 */
package kirjasto;

import static kanta.ISBNtarkistus.rand;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Kirjaan liitettävä asiasana. Tallettaa myös asiasanan id-numeron.
 * 
 * @author Hannamari Heiniluoma
 * @version 18.4.2019
 *
 */
public class Asiasana implements Comparable<Asiasana> {
    
    private int id;
    private String asiasana = "";
    
    private static int seuraavaId = 1;
    
    
    /**
     * getteri asiasanaoliolle
     * @return asiasana
     * @example
     * <pre name="test">
     * Asiasana egypti = new Asiasana();
     * egypti.lisaaEgypti();
     * egypti.rekisteroi();
     * egypti.getAsiasana() =R= "Egypti .*";
     * </pre>
     */
    public String getAsiasana() {
        return this.asiasana;
    }
    
    
    /**
     * @param as asetettava asiasana
     */
    public void setAsiasana(String as) {
        this.asiasana = as;
    }
    
    
    /**
     * Antaa asiasanalle seuraavan id-numeron
     * @return lisätyn asiasanan id-numero
     * @example
     * <pre name="test">
     * Asiasana egypti = new Asiasana();
     * egypti.getId() === 0;
     * egypti.rekisteroi();
     * Asiasana egypti2 = new Asiasana();
     * egypti2.rekisteroi();
     * int id1 = egypti.getId();
     * int id2 = egypti2.getId();
     * id1 === id2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.id = seuraavaId;
        seuraavaId++;
        return id;
    }
    
    
    /**
     * Palauttaa asiasanan id-numeron
     * @return asiasanan id-numero
     */
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Asettaa uuden id-numeron ja samalla varmistaa, että
     * seuraava id on aina suurempi kuin tähän mennessä suurin.
     * @param nro asetettava asiasanan id-numero
     */
    private void setId(int nro) {
        this.id = nro;
        if (this.id >= seuraavaId ) seuraavaId = this.id + 1;
    }
    
    
    /**
     * Selvittää asiasanan tiedot "|"-merkillä erotellusta merkkijonosta.
     * Huolehtii, että seuraavaId on suurempi kuin tuleva id.
     * @param rivi rivi, josta asiasanan tiedot otetaan
     * @example
     * <pre name="test">
     * Asiasana asiasana = new Asiasana();
     * asiasana.parse("   4  |  kissa");
     * asiasana.getId() === 4;
     * asiasana.toString() === "4|kissa";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setId(Mjonot.erota(sb, '|', getId()));
        this.asiasana = Mjonot.erota(sb, '|', this.asiasana);
    }
    
    
    /**
     * Palauttaa asiasanan tiedot merkkijonona, jonka voi tallentaa tiedostoon.
     * @return asiasana tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     * Asiasana asiasana = new Asiasana();
     * asiasana.parse("   4  |  kissa");
     * asiasana.toString() === "4|kissa";
     * </pre>
     */
    @Override
    public String toString() {
        return this.id + "|" + this.asiasana;
    }
    
    
    /**
     * Vertaa kahta asiasanaa (ei välitä kirjainkoosta)
     * @param verrattava verrattava asiasana
     * @return 0 jos asiasanat ovat samoja, -1 jos erilaisia
     * @example
     * <pre name="test">
     * Asiasana as1 = new Asiasana();
     * as1.setAsiasana("kissa");
     * Asiasana as2 = new Asiasana();
     * as2.setAsiasana("koira");
     * Asiasana as3 = new Asiasana();
     * as3.setAsiasana("Kissa");
     * as1.compareTo(as2) === -1;
     * as1.compareTo(as3) === 0;
     * </pre>
     */
    @Override
    public int compareTo(Asiasana verrattava) {
        if (this.asiasana.equalsIgnoreCase(verrattava.getAsiasana())) return 0;
        return -1;
    }
    
    
    /**
     * Tulostetaan asiasanan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(this.asiasana);
    }
    
    
    /**
     * Tulostetaan asiasanan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
//-------------------------------------------------------------------------
//tässä alla rakennustelineitä, joita kuitenkin testit edelleen käyttää
    
    /**
     * Esimerkkimetodi, joka lisää aina asiasanaksi Egypti + random-luku.
     */
    public void lisaaEgypti() {
        this.asiasana = "Egypti " + rand(1000,9999);
    }




}
