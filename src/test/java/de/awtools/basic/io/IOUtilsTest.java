/*
 * ============================================================================
 * Project awtools-basic
 * Copyright (c) 2000-2016 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU LESSER GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.awtools.basic.io;

import static org.fest.assertions.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.UnhandledException;
import org.junit.Test;

import de.awtools.basic.file.AWToolsFileUtils;

/**
 * Testet die Funktionalität von WinIOUtils.
 *
 * @author by Andre Winkler
 */
public class IOUtilsTest {

    @Test
    public void testInputStreamToString() throws IOException {
        InputStream is = AWToolsFileUtils.classpathLoader("log4j.properties");

        assertThat(is).isNotNull();
        assertThat(IOUtilsTest.toString(is)).isNotNull();
    }

    @Test
    public void testWinIOUtilsCreateTempDir() throws Exception {
        File tempDir = AWToolsIOUtils.createTempDir();
        assertThat(tempDir).exists();
        AWToolsIOUtils.recursiveDelete(tempDir);
        assertThat(tempDir).doesNotExist();
    }

    /**
     * Zeilenumschalter.
     * 
     * @deprecated Use #IOUtils.LINE_SEPARATOR
     */
    @Deprecated
    public static final String LINESEPARATOR;

    // ------------------------------------------------------------------------

    static {
        LINESEPARATOR = System.getProperty("line.separator");
    }

    /**
     * Konvertiert einen InputStream in einen String.
     * <strong>Achtung:</strong> Diese Methode sollte nur in Verwendung mit
     * 'kleinen' <code>InputStream</code>s verwendet werden.<br>
     *
     * @param is Der zu konvertierende InputStream.
     * @return Das Endergebnis als String. Tritt während der Verarbeitung ein
     *  Fehler ein, wird eine 'null'-Referenz zurück geliefert.
     *  
     * @deprecated Siehe IOUtils#toString(InputStream) 
     */
    @Deprecated
    public static String toString(final InputStream is) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuilder buf = new StringBuilder();
            String string;

            while ((string = in.readLine()) != null) {
                buf.append(string);
                buf.append(LINESEPARATOR);
            }
            return buf.toString();
        } catch (IOException ex) {
            throw new UnhandledException(ex);
        }
    }

    /**
     * Wandelt den Inhalt einer Datei in einen String. Macht nur Sinn im
     * Zusammenhang mit Textdateien!
     *
     * @param file Die zu lesende Datei.
     * @return Der String.
     * @see #toString(InputStream)
     * 
     * @deprecated Siehe IOUtils#toString(InputStream) 
     */
    @Deprecated
    public static String toString(final File file) {
        FileInputStream in;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        return (toString(in));
    }

}
