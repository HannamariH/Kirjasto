/**
 * 
 */
package kirjasto;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Lisää/poistaa/hakee kirjaston kirjoja, tekijöitä tai asiasanoja. Hallinnoi näitä ja välittää
 * käyttöliittymän komentoja eteenpäin.
 * @author Hannamari Heiniluoma
 * @version 26.4.2019
 */
public class Kirjasto {
    
    private Kirjat kirjat = new Kirjat();
    private Asiasanat asiasanat = new Asiasanat();
    private Tekijat tekijat = new Tekijat();
    private AsiasanaKirjaParit asiasanaKirjaParit = new AsiasanaKirjaParit();
    
    
    /**
     * Palautaa kirjaston kirjojen määrän
     * @return kirjojen määrä
     */
    public int getKirjoja() {
        return this.kirjat.getLkm();
    }
    
    
    /**
     * Palauttaa kirjastossa käytössä olevien asiasanojen määrän.
     * @return asiasanojen määrä
     */
    public int getAsiasanoja() {
        return this.asiasanat.getLkm();
    }
    
    
    /**
     * Palauttaa kirjaston erillisten tekijöiden määrän.
     * @return tekijöiden määrä
     */
    public int getTekijoita() {
        return this.tekijat.getLkm();
    }
    
    
    /**
     * Palauttaa kirjaston asiasana-kirja-parien määrän.
     * @return asiasana-kirja-parien määrä
     */
    public int getAsiasanaKirjaPareja() {
        return this.asiasanaKirjaParit.getLkm();
    }
    
    
    /**
     * Palauttaa i:n kirjan
     * @param i monesko kirja palautetaan
     * @return viite i:teen kirjaan
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Kirja annaKirja(int i) throws IndexOutOfBoundsException {
        return this.kirjat.anna(i);
    }
    
    
    /**
     * Palauttaa i:n tekijan
     * @param i monesko tekija palautetaan
     * @return viite i:teen tekijaan
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Tekija annaTekija(int i) throws IndexOutOfBoundsException {
        return this.tekijat.anna(i);
    }
    
    
    
    /**
     * Palauttaa kirjan tekijän
     * @param kirja kirja, jonka tekijä halutaan
     * @return kirjan tekijä (jos ei ole, antaa null)
     */
    public Tekija annaTekija(Kirja kirja) {
        int tid = kirja.getTekijanId();
        if (tid == 0) return null;
        return this.tekijat.annaIdlla(tid);
    }
    
    
    /**
     * Asettaa kirjalle tekijän. Jos tekijä on jo olemassa, lisää kirjalle
     * sen id:n. Muussa tapauksessa lisää tekijän ensin ja liittää
     * sitten kirjalle tekijän id:n.
     * @param tekija kirjalle lisättävä tekijä
     * @param kirja kirja, jolle tekijä lisätään
     */
    public void kirjalleTekija(Tekija tekija, Kirja kirja) {
        Tekija onko = onkoJo(tekija);
        if (onko != null) {
            kirja.setTekijanId(onko.getId()); 
        } else {
            tekija.rekisteroi();
            lisaa(tekija);
            kirja.setTekijanId(tekija.getId()); 
        }
    }
    
    
    /**
     * Palauttaa i:n asiasanan
     * @param i monesko asiasana palautetaan
     * @return viite i:teen asiasanaan
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Asiasana annaAsiasana(int i) throws IndexOutOfBoundsException {
        return this.asiasanat.anna(i);
    }
    
    
    /**
     * @return kirjojen tila-lista
     */
    public String[] annaTilat() {
        return this.kirjat.getTilat();
    }
    
    
    /**
     * @param tekija tutkittava tekijä
     * @return ko. Tekijan jos löytyy, muuten null
     */
    public Tekija onkoJo(Tekija tekija) {
        return this.tekijat.onkoJo(tekija);
    }
    
    
    /**
     * Selvittää, onko tutkittava asiasana jo tiedostossa
     * @param asiasana tutkittava asiasana
     * @return ko. Asiasana jos se löytyy, muuten null
     */
    public Asiasana onkoJo(Asiasana asiasana) {
        return this.asiasanat.onkoJo(asiasana);
    }
    
    
    /**
     * Lisää kirjastoon uuden kirjan.
     * @param kirja lisättävä kirja
     * @example
     * <pre name="test">
     * Kirjasto kirjasto = new Kirjasto();
     * Tekija tekija = new Tekija();
     * tekija.rekisteroi();
     * tekija.taytaWaltari();
     * kirjasto.lisaa(tekija);
     * Kirja sinuhe1 = new Kirja(), sinuhe2 = new Kirja();
     * sinuhe1.rekisteroi(); sinuhe2.rekisteroi();
     * kirjasto.getKirjoja() === 0;
     * kirjasto.lisaa(sinuhe1); kirjasto.getKirjoja() === 1;
     * kirjasto.lisaa(sinuhe2); kirjasto.getKirjoja() === 2;
     * kirjasto.lisaa(sinuhe1); kirjasto.getKirjoja() === 3;
     * kirjasto.getKirjoja() === 3;
     * kirjasto.annaKirja(0) === sinuhe1;
     * kirjasto.annaKirja(1) === sinuhe2;
     * kirjasto.annaKirja(2) === sinuhe1;
     * kirjasto.annaKirja(3) === sinuhe1; #THROWS IndexOutOfBoundsException 
     * kirjasto.lisaa(sinuhe1); kirjasto.getKirjoja() === 4;
     * kirjasto.lisaa(sinuhe1); kirjasto.getKirjoja() === 5;
     * </pre>
     */
    public void lisaa(Kirja kirja) {
        this.kirjat.lisaa(kirja);
    }
    
    /**
     * Lisää kirjastoon uuden asiasanan.
     * @param asiasana lisättävä asiasana
     * @example
     * <pre name="test">
     * Kirjasto kirjasto = new Kirjasto();
     * Asiasana eka = new Asiasana();
     * eka.lisaaEgypti();
     * eka.rekisteroi();
     * kirjasto.lisaa(eka);
     * Asiasana toka = new Asiasana();
     * toka.lisaaEgypti();
     * toka.rekisteroi();
     * kirjasto.lisaa(toka);
     * kirjasto.getAsiasanoja() === 2;
     * </pre>
     */
    public void lisaa(Asiasana asiasana) {
        this.asiasanat.lisaa(asiasana);
    }
    
    
    /**
     * Luo uuden AsiasanaKirjaParin ja lisää sen AsiasanaKirjaParit-olion listalle
     * @param kirja kirja, jolle asiasana laitetaan
     * @param asiasana asiasana, joka kirjalle laitetaan
     * @example
     * <pre name="test">
     * Kirjasto kirjasto = new Kirjasto();
     * Tekija tekija = new Tekija();
     * tekija.rekisteroi();
     * tekija.taytaWaltari();
     * kirjasto.lisaa(tekija);
     * Kirja sinuhe1 = new Kirja();
     * sinuhe1.rekisteroi();
     * kirjasto.lisaa(sinuhe1);
     * Asiasana eka = new Asiasana();
     * eka.lisaaEgypti();
     * eka.rekisteroi();
     * kirjasto.lisaa(eka);
     * Asiasana toka = new Asiasana();
     * toka.lisaaEgypti();
     * toka.rekisteroi();
     * kirjasto.lisaa(toka);
     * kirjasto.lisaaAsiasanaKirjalle(sinuhe1, eka);
     * kirjasto.lisaaAsiasanaKirjalle(sinuhe1, toka);
     * kirjasto.getAsiasanaKirjaPareja() === 2;
     * </pre>
     */
    public void lisaaAsiasanaKirjalle(Kirja kirja, Asiasana asiasana) {
        AsiasanaKirjaPari akp = new AsiasanaKirjaPari();
        akp.setKirjaId(kirja.getId());
        akp.setAsiasanaId(asiasana.getId());
        this.asiasanaKirjaParit.lisaa(akp);
    }
    
    
    /**
     * Lisää kirjalle asiasanat. Poistaa ensin kaikki vanhat liimat,
     * tarkistaa sitten, mitkä asiasanat jo löytyvät. Tekee niistä uudet
     * liimat, lisää ja rekisteröi uudet asiasanat ja liimaa nekin kirjoihin.
     * @param lista lista kirjalle lisättävistä asiasanoista
     * @param kirja kirja, jolle asiasanat lisätään
     */
    public void kirjalleAsiasanat(List<Asiasana> lista, Kirja kirja) {
        List<AsiasanaKirjaPari> kirjanAkp = this.asiasanaKirjaParit.annaAsiasanat(kirja);
        for (int i = 0; i < kirjanAkp.size(); i++) { //poistetaan kaikki ko. kirjan akp:t tiedostosta
            this.asiasanaKirjaParit.poista(kirjanAkp.get(i));
        }
        for (int i = 0; i < lista.size(); i++) {
            Asiasana uusi = lista.get(i);
            Asiasana onko = onkoJo(uusi);
            if (onko == null) { // asiasanaa ei vielä ole, se on siis lisättävä tiedostoon
                uusi.rekisteroi();
                lisaa(uusi);
                lisaaAsiasanaKirjalle(kirja, uusi);
            } else {
                lisaaAsiasanaKirjalle(kirja, onko); //lisätään as kirjalle, luo siis uuden akp:n
            }            
        }
    }
    
    
    /**
     * Poistaa tietorakenteesta (ja tiedostosta) akp:n
     * @param akp poistettava AsiasanaKirjaPari
     */
    public void poista(AsiasanaKirjaPari akp) {
        this.asiasanaKirjaParit.poista(akp);
    }
    
    
    /**
     * Poistaa kirjan ja siihen liittyvät AsiasanaKirjaParit.
     * @param kirja poistettava kirja
     * @return 1 jos poisto onnistui, 0 jos poistettavaa ei löytynyt
     */
    public int poista(Kirja kirja) {
        if (kirja == null) return 0;
        int ret = kirjat.poista(kirja.getId()); 
        poistaKirjanLiimat(kirja); 
        return ret; 
    }
    
    
    /**
     * Poistaa kirjaan liittyvät AsiasanaKirjaParit
     * @param kirja kirja, jonka liimat poistetaan
     */
    public void poistaKirjanLiimat(Kirja kirja) {
        List<AsiasanaKirjaPari> asiasanaparit = this.asiasanaKirjaParit.annaAsiasanat(kirja);
        for (int i = 0; i < asiasanaparit.size(); i++) {
            AsiasanaKirjaPari poistettava = asiasanaparit.get(i);
            this.asiasanaKirjaParit.poista(poistettava);
        }
    }
    
    
    /**
     * Lisää kirjastoon uuden tekijän.
     * @param tekija lisättävä tekijä
     * @example
     * <pre name="test">
     * Kirjasto kirjasto = new Kirjasto();
     * Tekija eka = new Tekija();
     * eka.taytaWaltari();
     * eka.rekisteroi();
     * kirjasto.lisaa(eka);
     * Tekija toka = new Tekija();
     * toka.taytaWaltari();
     * toka.rekisteroi();
     * kirjasto.lisaa(toka);
     * kirjasto.getTekijoita() === 2;
     * </pre>
     */
    public void lisaa(Tekija tekija) {
        this.tekijat.lisaa(tekija);
    }
    
    
    /**
     * Korvaa kirjan tietorakenteessa tai lisää sen uutena, jos
     * kyseisellä id:llä ei ole vielä olemassa kirjaa.
     * @param kirja lisättävä kirja
     */
    public void korvaaTaiLisaa(Kirja kirja) {
        kirjat.korvaaTaiLisaa(kirja);
    }
    
    
    /** 
     * Hakee halutun kirjan asiasanat
     * @param kirja kirja, jonka asiasanat halutaan
     * @return lista kirjan asiasanoista
     * @example
     * <pre name="test">
     * #import java.util.*;
     * Kirjasto kirjasto = new Kirjasto();
     * Tekija tekija = new Tekija();
     * tekija.rekisteroi();
     * tekija.taytaWaltari();
     * kirjasto.lisaa(tekija);
     * Kirja sinuhe1 = new Kirja();
     * sinuhe1.rekisteroi();
     * kirjasto.lisaa(sinuhe1);
     * Asiasana eka = new Asiasana();
     * eka.lisaaEgypti();
     * eka.rekisteroi();
     * kirjasto.lisaa(eka);
     * Asiasana toka = new Asiasana();
     * toka.lisaaEgypti();
     * toka.rekisteroi();
     * kirjasto.lisaa(toka);
     * kirjasto.lisaaAsiasanaKirjalle(sinuhe1, eka);
     * kirjasto.lisaaAsiasanaKirjalle(sinuhe1, toka);
     * kirjasto.getAsiasanaKirjaPareja() === 2;
     *
     * List<Asiasana> loytyneet;
     * loytyneet = kirjasto.annaAsiasanat(sinuhe1);
     * loytyneet.get(0) === eka;
     * loytyneet.size() === 2; 
     * </pre>
     */
    public List<Asiasana> annaAsiasanat(Kirja kirja) {
        List<AsiasanaKirjaPari> asiasanaparit = this.asiasanaKirjaParit.annaAsiasanat(kirja);
        List<Asiasana> kirjanAsiasanat = new ArrayList<Asiasana>();
        for (int i = 0; i < asiasanaparit.size(); i++) {
            int asId = asiasanaparit.get(i).getAsiasanaId();
            Asiasana asiasana = this.asiasanat.annaIdlla(asId);
            kirjanAsiasanat.add(asiasana);
        }
        return kirjanAsiasanat;
    }
    
    
    
    /**
     * Palauttaa kirjan asiasanat pilkulla eroteltuna listana 
     * näyttöön tulostamista varten
     * @param kirja kirja, jonka asiasanat halutaan
     * @return asiasaanat pilkkulistana
     * Kirjasto kirjasto = new Kirjasto();
     * Kirja k1 = new Kirja();
     * Asiasana a1 = new Asiasana();
     * a1.setAsiasana("kissa");
     * Asiasana a2 = new Asiasana();
     * a1.setAsiasana("koira");
     * Asiasana a3 = new Asiasana();
     * a1.setAsiasana("kana");
     * List<Asiasanat> aslista = new ArrayList<>();
     * aslista.add(a1); aslista.add(a2); aslista.add(a3);
     * kirjasto.kirjalleAsiasanat(aslista, k1);
     * kirjasto.annaAsiasanatJonona(k1) === "kissa, koira, kana";
     */
    public String annaAsiasanatJonona(Kirja kirja) {
        List<Asiasana> lista = annaAsiasanat(kirja);
        if (lista.size() <= 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(lista.get(0).getAsiasana());
        for (int i = 1; i < lista.size(); i++) {
            sb.append(", " + lista.get(i).getAsiasana());
        }
        return sb.toString();
    }
    
    
    /**
     * Palauttaa merkkojonona annetut asiasanat Asiasana-listana
     * @param rivi parsittava merkkijono
     * @param merkki merkki, jonka kohdalta erotellaan
     * @return lista erotelluista asiasanoista
     */
    public List<Asiasana> parse(String rivi, char merkki) {
        return this.asiasanat.parse(rivi, merkki);
    }
    
    
    /**
     * Palauttaa tietorakenteessa hakuehtoon vastaavat kirjat
     * @param hakuehto hakuehto
     * @param k haettavan kentän indeksi
     * @return "lista" löytyneistä kirjoista
     * @throws SailoException jos jokin menee pieleen
     */
    public List<Kirja> etsiKirjat(String hakuehto, int k) throws SailoException {
        return this.kirjat.etsi(hakuehto, k);
    }
    
    
    /**
     * Palauttaa tietorakenteessa hakuehtoon vastaavat tekijät
     * @param hakuehto hakuehto
     * @return "lista" löytyneistä tekijöistä
     * @throws SailoException jos jokin menee pieleen
     */
    public Collection<Tekija> etsiTekijat(String hakuehto) throws SailoException {
        return this.tekijat.etsi(hakuehto);
    }
    
    
    /**
     * Palauttaa tietorakenteessa halutun tekijän kaikki kirjat
     * @param tekija tekijä, jonka kirjat halutaan
     * @return "lista" löytyneistä kirjoista
     */
    public Collection<Kirja> etsiTekijanKirjat(Tekija tekija) {
        int tid = this.tekijat.annaTekijanId(tekija);
        return this.kirjat.etsiTekijanKirjat(tid);
    }
    
    
    /**
     * Palauttaa tietorakenteessa hakuehtoon vastaavat asiasanat
     * @param hakuehto hakuehto
     * @return "lista" löytyneistä asiasanoista
     * @throws SailoException jos jokin menee pieleen
     */
    public Collection<Asiasana> etsiAsiasanat(String hakuehto) throws SailoException {
        return this.asiasanat.etsi(hakuehto);
    }
    
    
    /**
     * Palauttaa tietorakenteen kirjoista, joilla on haettu asiasana
     * @param asiasana asiasana, jota kirjoista etsitään
     * @return "lista" kirjoista, joilla on ko. asiasana
     */
    public ArrayList<Kirja> etsiAsiasananKirjat(Asiasana asiasana) {
        ArrayList<Kirja> kirjalista = new ArrayList<>();
        ArrayList<AsiasanaKirjaPari> akpt = this.asiasanaKirjaParit.annaKirjat(asiasana);
        for (int i = 0; i < akpt.size(); i++) {
            AsiasanaKirjaPari tutkittava = akpt.get(i);
            int kid = tutkittava.getKirjaId();
            Kirja kirja = this.kirjat.annaKirjaIdlla(kid);
            if (kirja != null) kirjalista.add(kirja);
        }
        return kirjalista;
    }
    
    
    /**
     * Lukee kirjaston tiedot tiedostoista
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedosto() throws SailoException {
        this.kirjat = new Kirjat();
        this.tekijat = new Tekijat();
        this.asiasanat = new Asiasanat();
        this.asiasanaKirjaParit = new AsiasanaKirjaParit();
        
        setTiedosto();
        this.tekijat.lueTiedostosta();      
        this.asiasanat.lueTiedostosta();
        this.kirjat.lueTiedostosta();
        this.asiasanaKirjaParit.lueTiedostosta();
    }
    
    
    /**
     * Asettaa tiedostojen perusnimet
     */
    public void setTiedosto() {
        File dir = new File("kirjasto");
        dir.mkdirs();
        String hakemistonNimi = "kirjasto/";
        this.kirjat.setTiedostonPerusNimi(hakemistonNimi + "kirjat");
        this.tekijat.setTiedostonPerusNimi(hakemistonNimi + "tekijat");
        this.asiasanat.setTiedostonPerusNimi(hakemistonNimi + "asiasanat");
        this.asiasanaKirjaParit.setTiedostonPerusNimi(hakemistonNimi + "liima");
    }
    
    
    /**
     * Välittää tallennuskäskyn monikkoluokille, jotka
     * tallentavat tiedot omiin tiedostoihinsa. Vaikka jonkun
     * tiedoston tallennus epäonnistuisi, yritetään tallentaa
     * kaikki muut.
     * @throws SailoException jos tallennus ei onnistu
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            this.tekijat.tallenna();
        } catch (SailoException e) {
            virhe = e.getMessage();
        }
        try {
            this.asiasanat.tallenna();
        } catch (SailoException e) {
            virhe += e.getMessage();
        }
        try {
            this.kirjat.tallenna();
        } catch (SailoException e) {
            virhe += e.getMessage();
        }
        try {
            this.asiasanaKirjaParit.tallenna();  
        } catch (SailoException e) {
            virhe += e.getMessage();
        }
        if (!virhe.equals("")) throw new SailoException(virhe);
    }

}
    