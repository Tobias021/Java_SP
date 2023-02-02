package tlaskal.generator_citaci;

import java.io.IOException;
import static java.lang.System.exit;

/**
 * Třída obsluhující logiku tisku menu a loga. Obsahuje kód pro čištění textu terminálu.
 * @author Tobiáš Táskal
 */
public class Menu {
    private final boolean isWindows;
    private final String logo = "";
    private final String menuUvod[];

    public Menu() {
        this.menuUvod = new String[]{"""
                                     Vitejte v generatoru citaci. Vyberte pozadovanou funkci:
                                     1) Vytvo\u0159it novou citaci""", "\n2) Zobrazit citace", "\n3) Nastavit cestu ukladani citaci"};
        
        //Zjisti, jestli je OS Windows
        boolean containsWindows = false; 
        try{
            containsWindows = System.getProperty("os.name").contains("Windows");
        }catch(Exception e){
            System.out.printf("Došlo k nečekané chybě: %s\nUkončuji program.", e.getMessage());
            exit(1);
        }finally{
            isWindows = containsWindows;
        }
    }
    
    public void printLogo(){
        clearTerminal();
        System.out.print(logo);
    }
    
    public void printMenuUvod(int maxUroven){
        printLogo();
        System.out.flush();
        try{
            switch(maxUroven){
                case 1: System.out.print(menuUvod[0]);
                    break;
                case 2: System.out.print(menuUvod[0]+menuUvod[1]);
                    break;
                case 3: System.out.print(menuUvod[0]+menuUvod[1]+menuUvod[3]);
                default: throw new Exception("Neplatný index uvodniho menu!");
            }
        }catch(Exception e){
            error(e.getMessage(), true);
        }
        System.out.println();
    }
    
    /**
     * Metoda spouštějící příkaz pro vyčistění konzole. Na základě hodnoty bool proměnné isWindows se volí příkaz (Unix/Windows). 
     */
    public void clearTerminal(){
        try{
            if(!isWindows){
                //parametry metody exec jsou v poli z toho důvodu, že metoda exec(String s) je zastaralá.
                Runtime.getRuntime().exec(new String[] {"clear"});
            }else{
                Runtime.getRuntime().exec(new String[]{"cls"});
            }
        }catch(IOException e){
            error(e.getMessage(), false);
            System.out.println();
        }
    }
    
    /**
     * Metoda se používá pro vypsání základních chybových hlášek, které pouze ohlašují chybu a dovolují přidat její popis. Pomocí parametrů je možné určit, zda se běh programu ukončí. 
     * @param error řetězec znaků, který se má vypsat za chybovou hláškou (většinou Exception.message)
     * @param exit boolean určující, jestli chyba ukončuje program (true - ukončit, false - neukončit)
     */
    public void error(String error, boolean exit){
        System.out.printf("Při běhu programu došlo k chybě: %s", error);
        if(exit){
            System.out.println("Porgram se ukončil.");
            exit(1);
        }else{
            System.out.println("Program se neukoničl.");
        }
    }
}
