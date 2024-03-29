package kirjasto.test;
// Generated by ComTest BEGIN
import java.io.File;
import static org.junit.Assert.*;
import org.junit.*;
import kirjasto.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2019.04.26 09:06:19 // Generated by ComTest
 *
 */
@SuppressWarnings("all")
public class AsiasanaKirjaParitTest {



  // Generated by ComTest BEGIN
  /** 
   * testLueTiedosto162 
   * @throws SailoException when error
   */
  @Test
  public void testLueTiedosto162() throws SailoException {    // AsiasanaKirjaParit: 162
    AsiasanaKirjaParit akp = new AsiasanaKirjaParit(); 
    AsiasanaKirjaPari akp1 = new AsiasanaKirjaPari(), akp2 = new AsiasanaKirjaPari(); 
    String hakemisto = "testikirjasto"; 
    String tiedNimi = hakemisto + "/tekijat"; 
    File ftied = new File(tiedNimi + ".dat"); 
    File dir = new File(hakemisto); 
    dir.mkdir(); 
    ftied.delete(); 
    try {
    akp.lueTiedosto(tiedNimi); 
    fail("AsiasanaKirjaParit: 173 Did not throw SailoException");
    } catch(SailoException _e_){ _e_.getMessage(); }
    akp.lisaa(akp1); 
    akp.lisaa(akp2); 
    akp.tallenna(); 
    akp = new AsiasanaKirjaParit(); 
    akp.lueTiedosto(tiedNimi); 
    assertEquals("From: AsiasanaKirjaParit line: 179", akp1.toString(), akp.anna(0).toString()); 
    assertEquals("From: AsiasanaKirjaParit line: 180", akp2.toString(), akp.anna(1).toString()); 
    akp.lisaa(akp2); 
    akp.tallenna(); 
    assertEquals("From: AsiasanaKirjaParit line: 183", true, ftied.delete()); 
    File fbak = new File(tiedNimi + ".bak"); 
    assertEquals("From: AsiasanaKirjaParit line: 185", true, fbak.delete()); 
    assertEquals("From: AsiasanaKirjaParit line: 186", true, dir.delete()); 
  } // Generated by ComTest END
}