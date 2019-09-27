/**
 * 
 */
package kanta;

/**
 * Luokka ISBN-numeron oikeellisuuden tarkistamiseksi.
 * @author Hannamari Heiniluoma
 * @version 25.4.2019
 *
 */
public class ISBNtarkistus {

    /**
     * Palauttaa isbn:n tarkistusnumeron. Parametrinä viedään laillista
     * muotoa oleva isbn, josta tarkistusmerkki mahdollisesti puuttuu.
     * @param isbn tutkittava isbn
     * @return tarkistusnumero
     * @example
     * <pre name="test">
     * isbnTarkistusnumero("978-951-0-43361-4") === 4;
     * isbnTarkistusnumero("978-951-0-43361-") === 4;
     * isbnTarkistusnumero("978-951-0-43361") === 4;         
     * isbnTarkistusnumero("9789510403686") === 6;
     * isbnTarkistusnumero("978-1-4732-1189") === 6;
     * </pre>
     */
    public static int isbnTarkistusnumero(String isbn) {
        
        StringBuilder isbnSB = new StringBuilder(isbn);
        for (int i = 0; i < isbnSB.length(); i++) {
            if (isbnSB.charAt(i) == '-') {
                isbnSB.deleteCharAt(i);        
                i--;                                                                                                
            }
        }
        
        String isbnIlmanTarkistusmerkkia = isbnSB.toString();
        if (isbnIlmanTarkistusmerkkia.length() > 12) isbnIlmanTarkistusmerkkia = isbnIlmanTarkistusmerkkia.substring(0, 12); 

        int i = 0;
        int summa = 0;
        while (i < isbnIlmanTarkistusmerkkia.length()) {
            summa += Character.getNumericValue(isbnIlmanTarkistusmerkkia.charAt(i));
            i++;
            summa += 3 * Character.getNumericValue(isbnIlmanTarkistusmerkkia.charAt(i));
            i++;
        }
 
        int tarkistusnumero = 0;
        int jakojaannos = summa % 10;
        if (jakojaannos == 0) return tarkistusnumero;           
        return 10-jakojaannos;
        
    }
    

//----------------------------------------------------------------
// alla olevia ei tarvita lopullisessa ohjelmassa,
// mutta testien takia ovat edelleen tarpeen.    
    
    /**
     * Arvotaan satunnainen kokonaisluku välille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan yläraja
     * @return satunnainen luku väliltä [ala,yla]
     */
    public static int rand(int ala, int yla) {
      double n = (yla-ala)*Math.random() + ala;
      return (int)Math.round(n);
    }
    
    
    /**
     * Arvotaan satunnainen ISBN, joka täyttää ISBN:n ehdot
     * @return satunnainen laillinen ISBN
     */
    public static String arvoIsbn() {
        String apuIsbn = "978-" + rand(100, 999) + "-" + rand(0, 9) + "-" +
                         rand(10000, 99999) + "-";
        return apuIsbn + isbnTarkistusnumero(apuIsbn);
    }
    
    
    /**
     * Pääohjelma testausta varten.
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        String isbn = "978-951-0-43361-4";
        //String isbn = "978-186-19-7271-2";
        //String isbn = "978-641-2-51412-8";
        System.out.println(isbnTarkistusnumero(isbn));
        System.out.println(arvoIsbn());
        System.out.println(arvoIsbn());
        System.out.println(arvoIsbn());
    }

}
