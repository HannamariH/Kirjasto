/**
 * 
 */
package kirjasto;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Tallettaa yhden kirjan ja yhden asiasanan yhteyden.
 * 
 * @author Hannamari Heiniluoma
 * @version 26.4.2019
 *
 */
public class AsiasanaKirjaPari {
    
    private int asiasanaId;
    private int kirjaId;


    /**
     * @return asiasanaId
     */
    public int getAsiasanaId() {
        return this.asiasanaId;
    }

    
    /**
     * @param asiasanaId asetettava asiasanaId
     */
    public void setAsiasanaId(int asiasanaId) {
        this.asiasanaId = asiasanaId;
    }

    
    /**
     * @return kirjaId
     */
    public int getKirjaId() {
        return this.kirjaId;
    }

    
    /**
     * @param kirjaId asetettava kirjaId
     */
    public void setKirjaId(int kirjaId) {
        this.kirjaId = kirjaId;
    }
    
    
    /**
     * Palauttaa asiasanaKirjaParin tiedot merkkijonona, jonka voi tallentaa tiedostoon.
     * Testattu parse-metodin kohdalla.
     * @return asiasanaKirjaPari tolppaeroteltuna merkkijonona
     */
    @Override
    public String toString() {
        return this.asiasanaId + "|" + this.kirjaId;
    }
    
    
    /**
     * Selvittää kirjan tiedot "|"-merkillä erotellusta merkkijonosta
     * ja asettaa attribuutit.
     * @param rivi rivi, josta kirjan tiedot otetaan
     * @example
     * <pre name="test">
     * AsiasanaKirjaPari akp = new AsiasanaKirjaPari();
     * akp.parse("   4  |  78 ");
     * akp.getKirjaId() === 78;
     * akp.getAsiasanaId() === 4;
     * akp.toString() === "4|78";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        this.asiasanaId = Mjonot.erota(sb, '|', asiasanaId);
        this.kirjaId = Mjonot.erota(sb, '|', this.kirjaId);
    }
    

}
