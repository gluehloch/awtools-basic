/*
 * $Id: AWToolsIOUtils.java 3725 2013-05-24 18:34:57Z andrewinkler $
 * ============================================================================
 * Project awtools-basic
 * Copyright (c) 2000-2013 by Andre Winkler. All rights reserved.
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

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.UnhandledException;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;

/**
 * Utility Klasse für IO-Operationen. Siehe auch
 * <a href="http://commons.apache.org/io/api-release/index.html?org/apache/commons/io/IOUtils.html">IOUtils</a>.
 *
 * @version $LastChangedRevision: 3725 $ $LastChangedDate: 2013-05-24 20:34:57 +0200 (Fr, 24. Mai 2013) $
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 */
public class AWToolsIOUtils {

    /** Der private Logger der Klasse. */
    private static final Logger log = LoggerFactory.make();

    /**
     * Liefert aus einem String einen InputStream.
     *
     * @param s Der zu bearbeitende String.
     * @return Der InputStream.
     */
    public static InputStream stringToInputStream(final String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    /**
     * Konvertiert eine Klassenpfad-Resource in ein Image.
     *
     * @param resource Eine Resource die im Klassenpfad liegt.
     * @return Das geladene Image.
     */
    public static Image loadImage(final String resource) {
        return (AWToolsIOUtils.loadImage(resource, AWToolsIOUtils.class));
    }

    /**
     * Konvertiert eine Klassenpfad-Resource in ein <code>Image</code>.
     *
     * @param resource Eine Resource die im Klassenpfad liegt.
     * @param classLoader Der zu verwendende Klassenlader.
     * @return Das geladene Image.
     */
    public static Image loadImage(final String resource,
        final Class<?> classLoader) {

        try {
            URL imageURL = classLoader.getResource(resource);
            // An alternative loading function:
            // return Toolkit.getDefaultToolkit().createImage(imageURL);
            return ImageIO.read(imageURL);
        } catch (IOException ex) {
            log.error("Fehler: ", ex);
            throw new UnhandledException(ex);
        }
    }

    /**
     * Legt ein temporäres Verzeichnis an. Mit {@link #recursiveDelete(File)}
     * kann das Verzeichnis gelöscht werden. Die Idee habe ich von einem
     * Artikel aus
     * <a href="http://stackoverflow.com/questions/617414/create-a-temporary-directory-in-java">stackoverflow</a>
     * entnommen.
     *
     * @return Ein temporäres Verzeichnis.
     * @throws IOException Im Fehlerfall.
     */
    public static File createTempDir() throws IOException {
        final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
        File newTempDir;
        final int maxAttempts = 9;
        int attemptCount = 0;
        do {
            attemptCount++;
            if (attemptCount > maxAttempts) {
                throw new IOException("The highly improbable has occurred! Failed to "
                    + "create a unique temporary directory after "
                    + maxAttempts + " attempts.");
            }
            String dirName = UUID.randomUUID().toString();
            newTempDir = new File(sysTempDir, dirName);
        } while (newTempDir.exists());

        if (newTempDir.mkdirs()) {
            return newTempDir;
        } else {
            throw new IOException("Failed to create temp dir named "
                + newTempDir.getAbsolutePath());
        }
    }

    /**
     * Entfernt ein Verzeichnis inklusiver aller Unterverzeichnisse.
     *
     * @param fileOrDir Das zu löschende Verzeichnis oder Datei.
     * @return <code>true</code>, wenn der Löschvorgang erfolgreich war.
     */
    public static boolean recursiveDelete(File fileOrDir) {
        if (fileOrDir.isDirectory()) {
            // recursively delete contents
            for (File innerFile : fileOrDir.listFiles()) {
                if (!FileUtils.deleteQuietly(innerFile)) {
                    return false;
                }
            }
        }

        return FileUtils.deleteQuietly(fileOrDir);
    }

}
