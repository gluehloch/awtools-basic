/*
 * ============================================================================
 * Project awtools-basic Copyright (c) 2000-2016 by Andre Winkler. All rights
 * reserved.
 * ============================================================================
 * GNU LESSER GENERAL PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING,
 * DISTRIBUTION AND MODIFICATION
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package de.awtools.basic.groovy;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;

/**
 * Testet das Starten der Groovy Embedded Variante.
 * 
 * @author Andre Winkler
 */
public class GroovyEmbeddedTest {

    /**
     * Testet die native Einbindung von Groovy. Alternativ existiert auch eine
     * allgemeinere Variante mit dem Bean-Scripting-Framework.
     */
    @Test
    public void testStartGroovyEmbedded() {
        Binding binding = new Binding();
        binding.setVariable("foo", new Integer(2));
        GroovyShell shell = new GroovyShell(binding);

        Object value = shell
                .evaluate("println 'Hello World!'; x = 123; return foo * 10");

        assertThat(value).isEqualTo(new Integer(20));
        assertThat(binding.getVariable("x")).isEqualTo(new Integer(123));
    }

    @Test
    public void testStartGroovyEmbeddedCompileScript() throws Exception {
        GroovyScriptEngine engine = new GroovyScriptEngine("src/test/resources",
                this.getClass().getClassLoader());

        engine.run("de/awtools/basic/groovy/Operator.groovy", new Binding());

        Class<?> clazz = engine
                .loadScriptByName("de/awtools/basic/groovy/Operator.groovy");
        IOperator op = (IOperator) clazz.newInstance();
        assertThat(op.add(2, 3)).isEqualTo(5);
    }

    @Test
    public void testGroovyClassLoader_new() throws Exception {
        // Achtung: Wenn die Datei in *.groovy umbenannt wird, wird diese
        // Datei von Eclipse automatisch compiliert und in ein anderes
        // Verzeichnis verschoben.
        InputStream is = this.getClass()
                .getResourceAsStream("/Operator2.groov");
        assertThat(is).isNotNull();
        // Diese Variante funktioniert nicht aus Eclipse heraus. Die Groovy
        // Dateien
        // landen in ein anderes Verzeichnis, welches nicht im Klassenpfad
        // liegt.
        // InputStream is =
        // this.getClass().getResourceAsStream("/Operator2.groovy");
        // assertNotNull(is);

        GroovyClassLoader gcl = new GroovyClassLoader(
                this.getClass().getClassLoader());
        InputStreamReader isr = new InputStreamReader(is);
        GroovyCodeSource gcs = new GroovyCodeSource(isr, "i dont´t know",
                "unknown");
        Class<?> clazz = gcl.parseClass(gcs);
        assertThat(clazz.newInstance()).isInstanceOf(IOperator.class);

        IOperator op = (IOperator) clazz.newInstance();
        assertThat(op.add(2, 3)).isEqualTo(5);

        gcl.close();
    }

}
