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

package de.awtools.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;

/**
 * Lädt eine Property Datei aus dem Klassenpfad.
 *
 * @author by Andre Winkler
 */
public final class PropertyLoader {

    /** Der private Logger der Klasse. */
    private static final Logger log = LoggerFactory.make();

    /** Utility Klasse. */
    private PropertyLoader() {
    }

    /**
     * Lädt eine Property-Datei aus dem Klassenpfad. Kann die Property Datei
     * nicht gelesen werden, wird eine leere Map zurückgeliefert.
     *
     * @param fileName Der Filename der Property-Datei.
     * @return Die Eigenschaften dieser Property-Datei.
     */
    public static Map<Object, Object> load(final String fileName) {
        InputStream in =
                PropertyLoader.class.getClassLoader().getResourceAsStream(
                    fileName);

        if (in == null) {
            return throwsErrorMap(fileName);
        }

        Properties props = new Properties();
        try {
            props.load(in);
        } catch (IOException ex) {
            return throwsErrorMap(fileName);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                // Wird ignoriert.
            }
        }
        return props;
    }

    /**
     * Liefert eine leere Map zurück und schreibt eine Fehlermeldung ins Log.
     *
     * @param fileName Die Datei, welche nicht gelesen werden konnte.
     * @return Die leere Map.
     */
    private static Map<Object, Object> throwsErrorMap(final String fileName) {
        log.debug("Property Datei '" + fileName
            + "' konnte nicht gelesen werden!");
        return new HashMap<>();
    }

}
