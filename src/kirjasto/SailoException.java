/**
 * 
 */
package kirjasto;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville virheille.
 * 
 * @author Hannamari Heiniluoma
 * @version 7 Mar 2019
 *
 */
public class SailoException extends Exception {
    
    private static final long serialVersionUID = 1L;

    /** Poikkeuksen konstruktori
     * @param viesti poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }



}
