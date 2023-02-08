package tlaskal.generator_citaci;

import static java.lang.System.exit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Semestrální projekt předmětu Objektové programování - CLI generátor citací
 *
 * @author Tobiáš Tláskal
 */
public class Generator_citaci {

    static Citace citace = null;
    static Menu menu = new Menu();

    /**
     * Metoda main. Spouští se při zapnutí programu. Spouští úvodní menu, která
     * vrací volbu uživatele.
     *
     * @param args parametry z příkazového řádku ve formě pole
     */
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("  Přerušeno, ukončuji...");
            }
        }));
        while (true) { // nekonenčý loop hlavní metody
            int volba = -1; // proměnná uchovávající volbu uživatele v menu; -1 = chyba
            if (citace == null) { //pokud je dostupná citace, zobraz možnost ji vypsat
                volba = menu.getMenuUvod(1);
            } else {
                volba = menu.getMenuUvod(2);
            }
            // proveď volbu
            switch (volba) {
                case 1 -> { // vygeneruj novou citaci
                    citace = menu.getNovaCitace();
                    tiskCitace();
                    break;
                }
                case 2 -> { // vytiskni citaci
                    tiskCitace();
                    break;
                }
                case 0 -> {
                    System.out.println("Ukončuji aplikaci...");
                    exit(0);
                }
                default -> {
                    menu.error("Došlo k neošetřené chybě, ukončuji program...", true);
                }
            }
        }
    }

    /**
     * Pomocná metoda, která vytiskne citaci, pokud existuje.
     */
    private static void tiskCitace() {
        if (citace != null) {
            menu.print("Vygenerovaná citace:\n\n" + citace.getCitace() + "\n\n");
        }
    }
}
