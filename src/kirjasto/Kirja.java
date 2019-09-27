/**
 * 
 */
package kirjasto;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

import static kanta.ISBNtarkistus.*;

/**
 * Kirjaston kirja, joka huolehtii tiedoistaan ja id-numerostaan. Pitää myös
 * huolen, että ISBN-numero on oikean kaavan mukainen.
 * 
 * @author Hannamari Heiniluoma
 * @version 26.4.2019
 *
 */
public class Kirja implements Cloneable, Comparable<Kirja> {
    
    private int id = 0; 
    private int tekijanId = 0;
    private String nimi = "";    
    private int julkaisuvuosi = 0;
    private String kustantaja = "";
    private String isbn = "";
    private int sivumaara = 0;
    private double luokka = 0;
    private String tila = "hyllyssä";
    
    private static int seuraavaId = 1;
    
    
    /**
     * @return kirjan nimi
     * @example
     * <pre name="test">
     * Kirja sinuhe = new Kirja();
     * sinuhe.taytaSinuhe(1);
     * sinuhe.getNimi() =R= "Sinuhe Egyptiläinen .*";
     * </pre>
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
     * @return järkevien, textFieldeiksi tehtävien kenttien määrä
     * kun otetaan mukaan myös alun ei-näytettävät
     */
    public int getKenttia() {
        return 8;
    }
    
    
    /**
     * @return eka kenttä, josta tehdään textField
     */
    public int ekaKentta() {
        return 2;
    }
    
    
    /**
     * Palauttaa k:tta kirjan kenttää vastaavan kysymyksen (labelin tekstin)
     * @param k kuinka monennen kentän teksti halutaan
     * @return k:nnetta kenttää vastaava teksti
     */
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "Id";
        case 1: return "Tekijän id";
        case 2: return "Nimeke";
        case 3: return "Julkaisuvuosi";
        case 4: return "Kustantaja";
        case 5: return "ISBN";
        case 6: return "Sivumäärä";
        case 7: return "Luokka";
        default: return "Ei oo";
        }
    }
    
    
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + this.id;
        case 1: return "" + this.tekijanId;
        case 2: return "" + this.nimi;
        case 3: return "" + this.julkaisuvuosi;
        case 4: return "" + this.kustantaja;
        case 5: return "" + this.isbn;
        case 6: return "" + this.sivumaara;
        case 7: return "" + this.luokka;
        default: return "Ei oo";
        }
    }
    
    
    /**
     * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kentän arvo asetetaan
     * @param jono jono joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     * @example
     * <pre name="test">
     * Kirja kirja = new Kirja();
     * kirja.aseta(2, "Kissanhoito-opas") === null;
     * kirja.aseta(3, "Kissanhoito-opas") === "Vuoden on oltava numero";
     * kirja.aseta(3, "2019") === null;
     * kirja.aseta(3, "2019") === null;
     * kirja.aseta(5, "978-952-6-5124-13") ==="Virheellinen ISBN";
     * kirja.aseta(7, "98.3") === null;
     * kirja.aseta(7, "98,3") === "Luokan on oltava numero. Desimaalierottimena on piste.";
     * </pre>
     */
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        if (tjono.length() < 1) return null;
        StringBuilder sb = new StringBuilder(tjono);
        switch (k) {
        case 0:
            setId(Mjonot.erota(sb, '§', getId()));
            return null;
        case 1:
            this.tekijanId = Integer.parseInt(tjono);
            return null;
        case 2:
            this.nimi = tjono;
            return null;
        case 3:
            return setVuosi(tjono);
        case 4:
            this.kustantaja = tjono;
            return null;
        case 5:
            return setIsbn(tjono);
        case 6:
            return setSivumaara(tjono);
        case 7:
            return setLuokka(tjono);
        default:
            return "Ei oo";
        }
    }
        
    
    /**
     * Asettaa kirjalle tekijä-id:n
     * @param tid tekijän id-numero
     */
    public void setTekijanId(int tid) {
        this.tekijanId = tid;
    }
    
    
    /**
     * Palauttaa kirjan tekijän id-numeron.
     * @return kirjan tekijän id-numero
     */
    public int getTekijanId() {
        return this.tekijanId;
    }
    
    
    /**
     * Asettaa kirjalle tilan
     * @param tila asetettava tila
     */
    public void setTila(String tila) {
        this.tila = tila;
    }
    
    
    /**
     * @return kirjan tila
     */
    public String getTila() {
        return this.tila;
    }

    
    /**
     * Antaa kirjalle seuraavan id-numeron
     * @return lisätyn kirjan id-numero
     * @example
     * <pre name="test">
     * Kirja sinuhe = new Kirja();
     * sinuhe.getId() === 0;
     * sinuhe.rekisteroi();
     * Kirja sinuhe2 = new Kirja();
     * sinuhe2.rekisteroi();
     * int id1 = sinuhe.getId();
     * int id2 = sinuhe2.getId();
     * id1 === id2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.id = seuraavaId;
        seuraavaId++;
        return this.id;
    }
    
    
    /**
     * Palauttaa kirjan id-numeron
     * @return kirjan id-numero
     */
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Asettaa uuden id-numeron ja samalla varmistaa, että
     * seuraava id on aina suurempi kuin tähän mennessä suurin.
     * @param nro asetettava kirjan id-numero
     */
    public void setId(int nro) {
        this.id = nro;
        if (this.id >= seuraavaId ) seuraavaId = this.id + 1;
    }
    
    
    /**
     * Tulostetaan kirjan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("Teos: " + this.nimi);
        out.println("Julkaisuvuosi: " + this.julkaisuvuosi);
        out.println("Kustantaja: " + this.kustantaja);
        out.println("ISBN: " + this.isbn);
        out.println("" + this.sivumaara + " sivua");
        out.println("Luokka: " + this.luokka);
        out.println("Tila: " + this.tila+ "\n");
    }
    
    
    /**
     * Tulostetaan kirjan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    
    /**
     * Palauttaa kirjan tiedot merkkijonona, jonka voi tallentaa tiedostoon.
     * @return kirjan tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     * Kirja kirja = new Kirja();
     * kirja.parse("   5  | 4 | Sinuhe Egyptiläinen     |   2007");
     * kirja.toString().startsWith("5|4|Sinuhe Egyptiläinen|2007|") === true;
     * </pre>
     */
    @Override
    public String toString() {
        return this.id + "|" +
                this.tekijanId + "|" +
                this.nimi + "|" +
                this.julkaisuvuosi + "|" +
                this.kustantaja + "|" +
                this.isbn + "|" +
                this.sivumaara + "|" +                
                this.luokka + "|" +
                this.tila;
    }
    
    
    /**
     * Selvittää kirjan tiedot "|"-merkillä erotellusta merkkijonosta.
     * Huolehtii, että seuraavaId on suurempi kuin tuleva id.
     * @param rivi rivi, josta kirjan tiedot otetaan
     * @example
     * <pre name="test">
     * Kirja kirja = new Kirja();
     * kirja.parse("   5  | 3| Sinuhe Egyptiläinen     |   2007");
     * kirja.getId() === 5;
     * kirja.toString().startsWith("5|3|Sinuhe Egyptiläinen|2007|") === true;
     * kirja.rekisteroi();
     * int n = kirja.getId();
     * kirja.parse(""+(n+20));
     * kirja.rekisteroi();
     * kirja.getId() === n+20+1;
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setId(Mjonot.erota(sb, '|', getId()));
        this.tekijanId = Mjonot.erota(sb, '|', 0);
        this.nimi = Mjonot.erota(sb, '|', this.nimi);
        this.julkaisuvuosi = Mjonot.erota(sb, '|', this.julkaisuvuosi);
        this.kustantaja = Mjonot.erota(sb, '|', this.kustantaja);
        this.isbn = Mjonot.erota(sb, '|', this.isbn);
        this.sivumaara = Mjonot.erota(sb, '|', this.sivumaara);
        this.luokka = Mjonot.erota(sb, '|', this.luokka);
        this.tila = Mjonot.erota(sb, '|', this.tila);
    }
    
    /**
     * Kloonaa kirjan muokkaamista varten.
     */
    @Override
    public Kirja clone() throws CloneNotSupportedException {
        Kirja uusi = (Kirja)super.clone();
        return uusi;
    }  
        

    /**
     * @return julkaisuvuosi
     */
    public int getVuosi() {
        return this.julkaisuvuosi;
    }


    /**
     * @return kustantaja
     */
    public String getKustantaja() {
        return this.kustantaja;
    }


    /**
     * @return isbn
     */
    public String getISBN() {
        return this.isbn;
    }


    /**
     * @return sivumäärä
     */
    public int getSivumaara() {
        return this.sivumaara;
    }


    /**
     * @return luokka
     */
    public double getLuokka() {
        return this.luokka;
    }
    

    /**
     * @param s kirjan nimi
     * @return null jos ok, muuten virheilmoitus
     */
    public String setNimi(String s) {
        this.nimi = s;
        return null;
    }


    /**
     * @param s kustantajan nimi
     * @return null jos ok, muuten virheilmoitus
     */
    public String setKustantaja(String s) {
        this.kustantaja = s;
        return null;
    }


    /**
     * @param s kirjan isbn
     * @return null jos ok, muuten virheilmoitus
     * @example
     * <pre name="test">
     * Kirja kirja = new Kirja();
     * kirja.setIsbn("978-951-1-32256-6") === null;
     * kirja.setIsbn("978-951-1-32256-5") === "Virheellinen ISBN";
     * kirja.setIsbn("978-951-1-3225") === "Virheellinen ISBN";
     * </pre>
     */
    public String setIsbn(String s) {
        if (s.length() != 17) return "Virheellinen ISBN";
        int tarkistusnro = isbnTarkistusnumero(s);
        if (Character.getNumericValue(s.charAt(16)) == tarkistusnro) {
            this.isbn = s;
            return null;
        }
        return "Virheellinen ISBN";
    }


    /**
     * @param s kirjan julkaisuvuosi
     * @return null jos ok, muuten virheilmoitus
     */
    public String setVuosi(String s) {
        if (s.equals("") || !s.matches("[0-9]*")) {
            return "Vuoden on oltava numero";
        }
        this.julkaisuvuosi = Integer.parseInt(s);
        return null;
    }
    
    
    /**
     * @param s kirjan sivumaara
     * @return null jos ok, muuten virheilmoitus
     */
    public String setSivumaara(String s) {
        if (s.equals("") || !s.matches("[0-9]*")) {
            return "Sivumäärän on oltava numero";
        }
        this.sivumaara = Integer.parseInt(s);
        return null;
    }
    
    
    /**
     * @param s kirjan luokka
     * @return null jos ok, muuten virheilmoitus
     */
    public String setLuokka(String s) {
        try {
            double nro = Double.parseDouble(s);
            this.luokka = nro;
            return null;
        } catch (NumberFormatException e) {
            return "Luokan on oltava numero. Desimaalierottimena on piste.";
        }
    }
    
    
    /**
     * Vertailee kahta kirjaa keskenään nimen perusteella.
     */
    @Override
    public int compareTo(Kirja kirja) {
        return this.nimi.compareToIgnoreCase(kirja.getNimi());
    }
    
    
//------------------------------------------------------------------------------
//tässä alla turhia rakennustelineitä, joita testit kuitenkin käyttää
    
    /**
     * Apumetodi, joka syöttää testiarvoja kirjoille.
     * @param apuisbn satunnainen ISBN, joka annetaan kirjalle
     * @param tid kirjan tekijän id-numero
     */
    public void taytaSinuhe(String apuisbn, int tid) {
        this.tekijanId = tid;
        this.nimi = "Sinuhe Egyptiläinen " + rand(1000,9999);
        this.julkaisuvuosi = 2016;
        this.kustantaja = "WSOY";
        this.isbn = apuisbn;
        this.sivumaara = 500;
        this.luokka = 84.2;
        this.tila = "hyllyssä";        
    }
    
    
    /**
     * Apumetodi, joka arpoo satunnaisen ISBN-numeron.
     * @param tid kirjan tekijän id-numero
     */
    public void taytaSinuhe(int tid) {
        String apuisbn = arvoIsbn();
        taytaSinuhe(apuisbn, tid);
    }
    
}
