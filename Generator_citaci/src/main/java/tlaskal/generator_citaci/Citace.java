package tlaskal.generator_citaci;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
//import Enum prkvů citace
import static tlaskal.generator_citaci.PrvkyEnum.*;

/**
 * Třída uchovávající vlastní citaci v podobě dat uložených v proměnné
 * {@code Map<PrvkyEnum, String> vstupPolozky}. Třída obsahuje
 * {@link #getCitace() metodu} pro generování citace, která text formátuje a
 * sestavuje pomocí {@code StringBuilder builder}. Tato metoda funguje zárověň
 * jako getter stringu citace.<br>
 * Dále třída obsahuje
 * {@link #setPolozka(tlaskal.generator_citaci.PrvkyEnum, java.lang.String metodu) metodu}
 * pro nastavování hodnot jednotlivých prvků citace. Funkci uchování dat pro
 * citaci plní proměnná {@code vstupPolozky} typu {@code Map<PrvkyEnum, String>}
 *
 * @author Tobiáš Tláskal
 * @see #getCitace()
 * @see #setPolozka(PrvkyEnum, String)
 */
public class Citace {

    //proměnná StringBuilderu
    private StringBuilder builder;
    //proměnná uchovávající data pro vytvoření citace
    private Map<PrvkyEnum, String> vstupPolozky;
    //pole povinných prvků
    private final PrvkyEnum povinnePrvky[] = {ISBN, NAZEV, PRIJMENI, JMENO, VYDANI, ROK_VYDANI};

    /**
     * Konstruktor třídy Citace. Inicializuje StringBuilder a pole vstupníh dat.
     */
    public Citace() {
        //inicializace pole vstupních dat
        vstupPolozky = new HashMap<>();
        for (PrvkyEnum p : PrvkyEnum.values()) {
            vstupPolozky.put(p, "");
        }
    }

    /**
     * Metoda vkládá hodnotu parametru polozka do objektu Map podle parametru
     * klic
     *
     * @param klic PrvkyEnum klic proměnné typu Map uchovávající data citace
     * @param polozka String hodnota prvku citace
     * @return <ul><li>0 pokud vklad hodnoty proběhl úspěšně</li><li>1 pokud
     * došlo k chybě</li></ul>
     */
    public int setPolozka(PrvkyEnum klic, String polozka) {
        if (jePovinne(klic) && !polozka.isBlank() || !jePovinne(klic)) {
            vstupPolozky.put(klic, polozka);
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Metoda ověřující, je-li prvek citace povinný
     *
     * @param prvek Enum název prvku
     * @return true - je povinný<br>false - není povinný
     */
    public boolean jePovinne(PrvkyEnum prvek) {
        return Arrays.toString(povinnePrvky).contains(prvek.toString());
    }

    /**
     * Metoda pro vygenerování formátované citace. Z dat v proměnné vstupPolozky
     * vytvoří pomocí StringBuilderu formátovanou citaci
     *
     * @return String citace
     */
    public String getCitace() {
        builder = new StringBuilder();
        String tempPrvek; // uchovává aktuálně zpracovávaný prvek citace
        //PRIJMENI - UPPERCASE
        tempPrvek = vstupPolozky.get(PRIJMENI).strip().toUpperCase();
        if (!tempPrvek.isBlank()) {
            appendBuilder(tempPrvek + ", ");
        }
        // JMENO - 1. pismeno UPPERCASE
        tempPrvek = vstupPolozky.get(JMENO).strip().toUpperCase().substring(0, 1);
        if (!tempPrvek.isBlank()) {
            appendBuilder(tempPrvek + ". ");
        }
        // NAZEV - pokud existuje podnazev, vytiskni za nazev ":", jinak "."
        tempPrvek = vstupPolozky.get(NAZEV);
        if (!tempPrvek.isBlank() && !vstupPolozky.get(PODNAZEV).isBlank()) {
            appendBuilder(tempPrvek + ": ");
        } else if (!tempPrvek.isBlank()) {
            appendBuilder(tempPrvek + ". ");
        }
        // PODNAZEV
        tempPrvek = vstupPolozky.get(PODNAZEV);
        if (!tempPrvek.isBlank()) {
            appendBuilder(tempPrvek + ". ");
        }
        // VYDANI - pokud obsahuje "." na posledním znaku, netiskni ji znovu
        tempPrvek = vstupPolozky.get(VYDANI);
        if (!tempPrvek.isBlank() && tempPrvek.charAt(tempPrvek.length() - 1) == '.') {
            appendBuilder(tempPrvek + " ");
        } else if (!tempPrvek.isBlank()) {
            appendBuilder(tempPrvek + ". ");
        }
        // MISTO_VYDANI
        tempPrvek = vstupPolozky.get(MISTO_VYDANI);
        if (!tempPrvek.isBlank()) {
            appendBuilder(tempPrvek + ": ");
        }
        // ROK_VYDANI
        tempPrvek = vstupPolozky.get(ROK_VYDANI);
        if (!tempPrvek.isBlank()) {
            appendBuilder(tempPrvek + ". ");
        }
        // POZNAMKA
        tempPrvek = vstupPolozky.get(POZNAMKA);
        if (!tempPrvek.isBlank()) {
            appendBuilder(tempPrvek + ". ");
        }
        // ISBN
        tempPrvek = vstupPolozky.get(ISBN);
        if (!tempPrvek.isBlank()) {
            appendBuilder("ISBN " + tempPrvek + ".");
        }
        return builder.toString();
    }

    /**
     * Pomocná metoda, která na konec řetězce {@code StringBuilderu} připojí
     * řetězec z parametru.
     *
     * @param polozka Řetězec textu k připojení
     */
    private void appendBuilder(String polozka) {
        builder.append(polozka);
    }
}
