package tlaskal.generator_citaci;

import java.io.IOException;
import static java.lang.System.exit;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Třída obsluhující logiku tisku menu a loga. Obsahuje kód pro čištění textu
 * terminálu a určení přítomnosti systému Windows .
 *
 * @see #clearTerminal() Čištění terminálu
 * @see #error(java.lang.String, boolean) vypisování chyb
 */
public class Menu {

    private final Scanner scanner;
    private final boolean isWindows; // proměnná identifikující operační systém - inicializace v konstruktoru
    private final String logo = """
  _ _  _____ _ _                _ _
 ( | )/ ____(_) |              ( | )
  V V| |     _| |_ __ _  ___ ___V V
     | |    | | __/ _` |/ __/ _ \\
     | |____| | || (_| | (_|  __/
      \\_____|_|\\__\\__,_|\\___\\___|
~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

"""; // proměnná obsahující ASCII logo

    // sekce proměnných obsahující texty menu
    private final String menuUvod[] = {"""
                                     Vitejte v generatoru citaci. Vyberte pozadovanou funkci:
                                     1) Vytvo\u0159it novou citaci""", "\n2) Nastavit cestu ukladani citaci", "\n3) Zobrazit uložené citace"};

    /**
     * Konstruktor třídy Menu. Provádí kontrolu systému a zaznamenává stav bool
     * isWindows.
     */
    public Menu() {
        //inicializuj scanner
        scanner = new Scanner(System.in);
        //Zjisti, jestli je OS Windows
        boolean containsWindows = false;
        try {
            containsWindows = System.getProperty("os.name").contains("Windows");
        } catch (Exception e) {
            System.out.printf("Došlo k nečekané chybě: %s\nUkončuji program.", e.getMessage());
            exit(1);
        } finally {
            isWindows = containsWindows;
        }
    }

    public void printLogo() {
        clearTerminal();
        System.out.print(logo);
    }

    /**
     * Vytiskne úvodní menu aplikace do úrovně specifikované parametrem.
     *
     * @param maxUroven int specifikující maximální úroveň menu
     * @see #getMenuUvod(int) Úrovně menu
     */
    private void printMenuUvod(int maxUroven) {
        printLogo(); // vyčisti terminál a vytiskni logo
        try {         // podle parametru maxUroven vytiskni menu - pokud je mimo rozsah, ukonči aplikaci
            switch (maxUroven) {
                case 1 ->
                    System.out.print(menuUvod[0]);
                case 2 ->
                    System.out.print(menuUvod[0] + menuUvod[1]);
                case 3 ->
                    System.out.print(menuUvod[0] + menuUvod[1] + menuUvod[2]);
                default ->
                    throw new Exception("Neplatný index uvodniho menu! ");
            }
        } catch (Exception e) {
            error(e.getMessage(), true);
        }
        System.out.println();
    }

    /**
     * Spustí úvodní menu aplikace do úrovně specifikované parametrem.<br>
     * Úrovně:<br>
     * <ol><li>Vytvořit novou citaci</li><li>Nastavit
     * cestu pro export</li><li>Zobrazit citace</li></ol>
     *
     * @param maxUroven int specifikující maximální úroveň menu
     * @return int požadované funkce ke spuštění
     */
    public int getMenuUvod(int maxUroven) {
        int volba = 0; // promenna uchovaajici volbu uzivatele. 0 = chyba
        printMenuUvod(maxUroven); //vytiskni menu
        System.out.print("Zadejte číslo funkce: ");
        while (true) { // dokud není naskenované číslo, opakuj
            if (scanner.hasNextInt()) {
                volba = scanner.nextInt(); //pokud je číslo, načti
            } else {
                scanner.nextLine(); //pokud není číslo, smaž řádek a zadej znovu
                printMenuUvod(maxUroven);
                System.out.print("Nebylo zadáno číslo. Zadejte číslo funkce: ");
                continue;
            }

            if (volba > 0 && volba <= maxUroven) { // pokud je volba platná (je v menu), ukonči cyklus
                break;
            } else {
                printMenuUvod(maxUroven); //pokud volba není platná, zadej znovu
                System.out.printf("Funkce s číslem %s se nenachází v menu.\n\nZadejte číslo funkce znovu: ", volba);
                volba = 0;
            }
        }
        return volba;
    }

    /**
     * Metoda spouštějící příkaz pro vyčistění konzole. Na základě hodnoty bool
     * proměnné isWindows se volí příkaz (Unix/Windows).
     */
    public void clearTerminal() {
        try {
            if (!isWindows) {
                //parametry metody exec jsou v poli z toho důvodu, že metoda exec(String s) je zastaralá.
                Runtime.getRuntime().exec(new String[]{"clear"});
            } else {
                Runtime.getRuntime().exec(new String[]{"cls"});
            }
        } catch (IOException e) {
            error(e.getMessage(), false);
            System.out.println();
        }
    }

    /**
     * Metoda se používá pro vypsání základních chybových hlášek, které pouze
     * ohlašují chybu a dovolují přidat její popis. Pomocí parametrů je možné
     * určit, zda se běh programu ukončí.
     *
     * @param error řetězec znaků, který se má vypsat za chybovou hláškou
     * (většinou Exception.message)
     * @param exit boolean určující, jestli chyba ukončuje program (true -
     * ukončit, false - neukončit)
     */
    public void error(String error, boolean exit) {
        System.out.printf("Při běhu programu došlo k chybě: %s", error);
        if (exit) {
            System.out.println("Porgram se ukončil.");
            exit(1);
        } else {
            System.out.println("Program se neukoničl.");
        }
    }

    /**
     * Getter proměnné definující, běží-li aplikace v prostředí Windows.
     *
     * @return boolean isWindows
     */
    public boolean getIsWindows() {
        return isWindows;
    }
}
