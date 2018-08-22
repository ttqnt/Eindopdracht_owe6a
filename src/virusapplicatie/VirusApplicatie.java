
package virusapplicatie;

/**
 * Tool for host based anlysis of the viral database found on ftp:genome.jp/pub/db/virushostdb/
 * 
 * @author Karin Geertse
 */
public class VirusApplicatie {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VirusGUI().setVisible(true);
            }
        });
        
    }
    
}
