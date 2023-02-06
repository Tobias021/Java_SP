package tlaskal.generator_citaci;

import javax.swing.filechooser.FileSystemView;

/**
 * Třída obsluhující export citace do textového souboru. Nabízí funkci pro kontrolu existence souboru exportů. 
 * @author Tobiáš Tláskal
 */
public class Export {
    private String citace = null; // proměnná uchovávající text citace
    private final String fileName = "citace.txt"; // název exportovaného souboru s citací
    private final String path;
    private final boolean isWindows; // ukládá informaci o OS
    
    /**
     * Konstruktor třídy Export
     * @param citace textový řetězec vlastní citace
     * @param isWindows boolean určující OS (true = windows, false = unix)
     */
    public Export(String citace, boolean isWindows){
        this.path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        this.citace = citace;
        this.isWindows = isWindows;
    }
    
    /**
     * Konstruktor třídy Export bez možnosti určení textu citace. Nabízí možnost kontroly existence exportovaných citací
     * @param isWindows boolean určující OS (true = windows, false = unix)
     */
    public Export(boolean isWindows){
        this.path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        this.isWindows = isWindows;
    }
    
    /**
     * Vykonává zápis citace do souboru.
     * @return 0 - úspěch, 1 - chyba
     */
    public int execute(){
        return 0;
    }
    
    /**
     * 
     * @return 
     */
    public boolean exists(){
        return false;
    }
}
