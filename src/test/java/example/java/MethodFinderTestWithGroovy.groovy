
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

/**
 * Which method will be called?
 * 
 * @author by Andre Winkler
 */
public class MethodFinderTest {

    @Test
    public void testMethodFinder() {
        Master object = new Aaa();
        String value = get(object);
        assertThat(value, CoreMatchers.equalTo("aaa_Aaa"));

        Aaa a = new Aaa();
        assertThat(get(a), CoreMatchers.equalTo("aaa_Aaa"));
        assertThat(to(a), CoreMatchers.equalTo("to_Aaa"));

        object = a; // And now the interesting stuff...
        
        // Master wird an die Methode #get(Master) übergeben. Java löst
        // den Typen nicht zur Laufzeit auf und bindet den Methodenaufruf
        // fest an #get(Master)
        assertThat(get(object), CoreMatchers.equalTo("aaa_Aaa"));
        
        // In der Template Variante findet die Auflösung ebenfalls nicht
        // zur Laufzeit statt. Über den Template Mechanismus weiss der
        // Compiler, dass es sich hier um die Klasse Aaa handelt.
        assertThat(to(object), CoreMatchers.equalTo("to_Aaa"));
    }

    public static <T> String to(T t) {
        return "to_" + t.toString();
    }

    private String get(Master master) {
        return "Master";
    }

    private String get(Aaa aaa) {
        return "aaa_" + aaa.toString();
    }

    @SuppressWarnings("unused")
    private String get(Bbb bbb) {
        return "bbb_" + bbb.toString();
    }

    @SuppressWarnings("unused")
    private String get(Ccc ccc) {
        return "ccc_" + ccc.toString();
    }

    // ------------------------------------------------------------------------

    private interface Master {
        String toString();
    }

    private class Aaa implements Master {
        @Override
        public String toString() {
            return "Aaa";
        }
    }

    private class Bbb implements Master {
        @Override
        public String toString() {
            return "Bbb";
        }
    }

    private class Ccc implements Master {
        @Override
        public String toString() {
            return "Ccc";
        }
    }

}
