package tlaskal.generator_citaci;

import java.io.IOException;
import static java.lang.System.exit;
import java.util.Scanner;

/**
 * Třída obsluhující logiku tisku menu a loga. Obsahuje kód pro čištění textu
 * terminálu a určení přítomnosti systému Windows.
 *
 * @see #clearTerminal() Čištění terminálu
 * @see #error(java.lang.String, boolean) vypisování chyb
 */
public class Menu {

    private Citace citace; // uchovává objekt citace
    private Scanner scanner; //
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
                                     1) Vytvořit novou citaci""", "\n2) Zobrazit citaci", "\n0) Ukončit"};

    /**
     * Konstruktor třídy Menu. Provádí kontrolu systému a zaznamenává stav bool
     * isWindows.
     */
    public Menu() {
        //Zjisti, jestli je OS Windows
        boolean containsWindows = false;
        try {
            containsWindows = System.getProperty("os.name").contains("Windows");
        } catch (Exception e) {
            System.out.printf("Došlo k nečekané chybě: %s\nUkončuji program.", e.getMessage());
            exit(1);
        } finally {
            isWindows = containsWindows; //zapiš výsledek do proměnné isWindows
        }
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
                    System.out.print(menuUvod[0] + menuUvod[2]);
                case 2 ->
                    System.out.print(menuUvod[0] + menuUvod[1] + menuUvod[2]);
                default ->
                    error("Index menu překročil hranice", true);
            }
        } catch (Exception e) {
            error(e.getMessage(), true);
        }
        System.out.println();
    }

    /**
     * Spustí úvodní menu aplikace do úrovně specifikované parametrem, úrovně se
     * kumulují.<br>
     * Úrovně:<br>
     * <ol><li>Vytvořit novou citaci</li><li>Zobrazit citaci</li></ol>
     *
     * @param maxUroven int specifikující maximální úroveň menu
     * @return int požadované funkce ke spuštění
     */
    public int getMenuUvod(int maxUroven) {
        scanner = new Scanner(System.in); //dump scanner
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
            if (volba >= 0 && volba <= maxUroven) { // pokud je volba platná (je v menu), ukonči cyklus. volba=0 kvůli možnosti ukončit
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
     * Metoda započne proces vytváření citace, pomocí Scanneru sbírá data od
     * učivatele a nakonec vrací objekt citace.
     *
     * @return Citace - hotová citace
     */
    public Citace getNovaCitace() {
        citace = new Citace();
        scanner = new Scanner(System.in); // pro zajištění čistého bufferu vytvoř nový scanner
        // ISBN
        setPolozkaCitace(PrvkyEnum.ISBN, "Zadejte ISBN");
        // NAZEV
        setPolozkaCitace(PrvkyEnum.NAZEV, "Zadejte název knihy");
        // PODNAZEV
        setPolozkaCitace(PrvkyEnum.PODNAZEV, "Zadejte podnázev");
        // JMENO
        setPolozkaCitace(PrvkyEnum.JMENO, "Zadejte jméno autora");
        // PRIJMENI
        setPolozkaCitace(PrvkyEnum.PRIJMENI, "Zadejte příjmení autora");
        // VYDANI
        setPolozkaCitace(PrvkyEnum.VYDANI, "Zadejte označení vydání");
        // MISTO_VYDANI
        setPolozkaCitace(PrvkyEnum.MISTO_VYDANI, "Zadejte místo vydání");
        // NAKLADATELSTVI
        setPolozkaCitace(PrvkyEnum.NAKLADATELSTVI, "Zadejte název nakladatelství");
        // ROK_VYDANI
        setPolozkaCitace(PrvkyEnum.ROK_VYDANI, "Zadejte rok vydání");
        // POZNAMKA
        setPolozkaCitace(PrvkyEnum.POZNAMKA, "Zadejte poznámku");
        return citace;
    }

    /**
     * Sprostředkovává načítání dat pro třídu Citace.
     *
     * @param polozka prvkyEnum hodnota klíče, do kterého má být načtená hodnota
     * uložena
     * @param text ukládaná hodnota načtená od uživatele
     * @throws NullPointerException Vyhazuje v případech, kdy nebyla
     * inicializována hodnota proměnné citace, na kterou se metoda odkazuje.
     */
    private void setPolozkaCitace(PrvkyEnum polozka, String text) throws NullPointerException {
        String textPovinne = text + "[*]: ";
        String textNepovinne = text + ": ";
        while (true) {
            printLogo();
            System.out.print(citace.jePovinne(polozka) ? textPovinne : textNepovinne);
            if (citace.setPolozka(polozka, scanner.nextLine()) == 0) {
                break;
            }
        }
    }

    //Utility ~~~~~
    /**
     * Vyčistí terminál, vytiskne logo a text
     *
     * @param text Řetězec k vytisknutí
     */
    public void print(String text) {
        scanner = new Scanner(System.in);
        clearTerminal();
        System.out.print(text + "\nPro pokračování zmáčkněte enter.");
        scanner.nextLine();
    }

    /**
     * Vytiskne logo
     */
    private void printLogo() {
        clearTerminal();
        System.out.print(logo);
    }

    /**
     * Metoda spouštějící příkaz pro vyčistění konzole. Na základě hodnoty bool
     * proměnné isWindows se volí příkaz (Unix/Windows).
     */
    private void clearTerminal() {
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
        System.out.printf("Při běhu programu došlo k chybě: %s\n", error);
        if (exit) {
            System.out.println("Porgram se ukončil.");
            exit(1);
        } else {
            System.out.println("Program se neukoničl.");
        }
    }
}
