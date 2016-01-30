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

package de.awtools.basic.file;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;

/**
 * Ein paar Utility Methoden rund um Verzeichnisse und Dateien.
 * 
 * @author by Andre Winkler
 */
public class AWToolsFileUtils {

    /** Der private Logger der Klasse. */
    private static final Logger log = LoggerFactory.make();

    /** Ein Verzeichnis rauf. */
    private static final String UP = "../";

    /** Das Trennerzeichen für Verzeichnisse. */
    public static final String FILESEPARATOR = File.separator;

    /**
     * Mein eigener File-Seperator. Wird für das normalisieren verwendet!
     */
    public static final char WINFILE_SEPEATOR_CHAR = '/';

    /**
     * Mein eigener File-Seperator. Wird für das normalisieren verwendet!
     */
    public static final String WINFILE_SEPERATOR = "" + WINFILE_SEPEATOR_CHAR;

    /** Pattern zur Erkennung von absoluten Windows Root Pfaden. */
    public static final Pattern WINDOWS_ROOT_PATTERN =
            Pattern.compile("^[a-zA-Z]:");

    /** Utility Klassen werden nicht instanziert. */
    private AWToolsFileUtils() {
    }

    /**
     * Lädt einen InputStream aus dem Klassenpfad.
     *
     * @param resource Die zu ladende Resource.
     * @return Der InputStream.
     */
    public static InputStream classpathLoader(final String resource) {
        return AWToolsFileUtils.class.getClassLoader().getResourceAsStream(
            resource);
    }

    /**
     * Prüft ob übergebener Dateibezeichner auf ein Verzeichnis verweist.
     *
     * @param dir Die zu prüfende File-Id.
     * @return true, ist ein Verzeichnis; false, es ist kein Verzeichnis.
     */
    public static boolean isDirectory(final String dir) {
        if (!StringUtils.isBlank(dir)) {
            File outDir = new File(dir);
            return outDir.isDirectory();
        } else {
            return false;
        }
    }

    /**
     * Erzeugt eine URL auf eine Datei.
     *
     * @param dirName Das Verzeichnis.
     * @param fileName Die Datei relativ zu dem Verzeichnis.
     * @return Die generierte URL oder 'null' falls keine URL generiert werden
     *  konnte.
     */
    public static URL createURL(final String dirName, final String fileName) {
        StringBuilder file = new StringBuilder();
        file.append("file:");
        file.append(dirName);
        file.append(System.getProperty("file.separator"));
        file.append(fileName);

        return createURL(file.toString());
    }

    /**
     * Erzeugt eine URL auf eine Datei.
     *
     * @param fileName Ein Dateiname.
     * @return Die generierte URL oder 'null' falls keine URL generiert werden
     *  konnte.
     */
    public static URL createURL(final String fileName) {
        URL url;
        try {
            url = new URL(fileName);
        } catch (MalformedURLException ex) {
            log.debug("Catched an MalformedURLException", ex);
            url = null;
        }
        return url;
    }

    /**
     * Erzeugt die nötigen Verzeichnisse unterhalb von <code>basePath</code>
     * um eine Datei <code>relativeFileName</code> anlegen zu können. Die Datei
     * selbst wird nicht angelegt, es wird aber dafür gesorgt, dass alle
     * notwendigen Verzeichnisse für diese Datei angelegt werden.<br>
     * Im Gegensatz zu der Methode <code>FileUtils#forceMkdir(File)</code> in
     * <code>IOUtils</code> aus jakarta-commons wird der letzte Pfadeintrag,
     * z.B. C:/tmp/www/web.xml nicht als Verzeichnis angelegt. Im Beispiel
     * wäre <code>web.xml</code> tatsächlich als Verzeichnis angelegt worden.
     *
     * @param basePath Ein Basisverzeichnis.
     * @param relativeFileName Ein relativer Filename (ab Basis).
     */
    public static void createFilePath(final String basePath,
        final String relativeFileName) {

        if (StringUtils.contains(relativeFileName, '\\')) {
            createFilePath(basePath, relativeFileName, '\\');
        } else if (StringUtils.contains(relativeFileName, '/')) {
            createFilePath(basePath, relativeFileName, '/');
        } else {
            log.debug("Directory creation not necessary!");
        }
    }

    /**
     * Unterstützt die {@link #createFilePath(String, String)} bei ihrer
     * Arbeit.
     *
     * @param basePath Ein Basisverzeichnis.
     * @param relativeFileName Ein relativer Filename (ab Basis).
     * @param separator Der Separator für Verzeichnisse.
     */
    private static void createFilePath(final String basePath,
        final String relativeFileName, final char separator) {

        StringBuilder path = new StringBuilder(basePath);

        // Alle möglichen Verzeichnisse/Unterverzeichnisse ermitteln ...
        String[] split = StringUtils.split(relativeFileName, separator);

        // ... und anlegen. Der letzte Eintrag bezeichnet die eigentliche Datei.
        for (int i = 0, max = split.length - 1; i < max; i++) {
            if ((!path.toString().endsWith("/"))
                && (!path.toString().endsWith("\\"))) {
                path.append(AWToolsFileUtils.FILESEPARATOR);
            }
            path.append(split[i]);

            File newDirFile = new File(path.toString());
            if (!newDirFile.exists()) {
                if (log.isDebugEnabled()) {
                    log.info("mkdir " + path);
                }
                newDirFile.mkdirs();
            }
        }
    }

    /**
     * Berechnet die Anzahl der Verzeichnisstufen bis
     * zum gesuchten Dateinamen. Die Angabe kann fiktiv sein, d.h.
     * mussen nicht notwendiger Weise im Dateisystem vorliegen.
     * Z.B. Der Parameter "./www/dir/dir1/index.html" liefert 3 zurück.
     *
     * @param fileName Die gesuchte Datei.
     * @return Anzahl der Verzeichnistiefen.
     */
    public static int countDirLevel(final String fileName) {
        // Normalisieren und ...
        String newFileName = AWToolsFileUtils.normalizePath(fileName);

        /*
        // ... nur noch die '/' zählen und fertig (ohne Root!)
        int correction = 0;

        if (fileName.startsWith(WINFILE_SEPERATOR)) {
            correction = 1;
        } else if (WINDOWS_ROOT_PATTERN.matcher(newFileName).find()) {
            correction = 1;
        }
        */
        //return (StringUtils.countMatches(newFileName, "/") - correction);
        return (StringUtils.countMatches(newFileName, "/") - 1);
    }

    /**
     * Erstellt einen relativen Pfad anhand der übergebenen Level-Nummer.
     *
     * @param level Die Level-Nummer.
     * @return Der generierte relative Pfad.
     */
    public static String pathToRoot(final int level) {
        StringBuilder buf = new StringBuilder("./");
        for (int i = 0; i < level; i++) {
            buf.append(UP);
        }

        return buf.toString();
    }

    /**
     * Erstellt einen relativen Pfad, der zur Wurzel führt. Z.B.
     * <code>test/winkler/andre.txt</code> generiert den Pfad
     * <code>./../../</code>.
     *
     * @param fileName Die gesuchte Datei.
     * @return Ein relativer Pfad zur Wurzel.
     */
    public static String pathToRoot(final String fileName) {
        return AWToolsFileUtils.pathToRoot(AWToolsFileUtils.countDirLevel(fileName));
    }

    /**
     * Siehe die Beschreibung in Methode
     * {@link #findFiles(java.io.File, java.lang.String, java.lang.String)}.
     *
     * @param basePath Basisverzeichnis.
     * @param fileName Die gesuchte Datei.
     * @return Eine Liste der gefundenen Dateien.
     *
     * @see #findFiles(java.io.File, java.lang.String, java.lang.String)
     */
    public static List<File> findFiles(final File basePath,
        final String fileName) {

        String relativePath = "";
        String realFileName = fileName;

        if ((StringUtils.contains(fileName, "/"))) {
            relativePath = StringUtils.substringBeforeLast(fileName, "/");
            realFileName = StringUtils.substringAfterLast(fileName, "/");
        }

        if (log.isDebugEnabled()) {
            log.debug("basePath ......: " + basePath);
            log.debug("relativePath ..: " + relativePath);
            log.debug("realFileName ..: " + realFileName);
        }

        return AWToolsFileUtils.findFiles(basePath, relativePath, realFileName);
    }

    /**
     * Sucht nach allen Dateien, die auf einem Verzeichnispfad liegen
     * und die die Bezeichnung 'fileName' besitzen. Der vorgegebene
     * Verzeichnistrenner ist der '/'. Beispiel:<br/>
     * <pre>
     *  BASE = /temp/readme.txt
     *         /temp/dir1/readme.txt
     *         /temp/dir11/
     *         /temp/dir11/dir121/readme.txt.
     * </pre>
     * Gesucht wird der <code>fileName = readme.txt</code>, der zu
     * untersuchende Pfadabschnitt lautet <code>dir11/dir121/</code> und
     * <code>basePath = /temp</code>, dann wird folgende Liste zurück
     * geliefert: <code>/temp/readme.txt</code>,
     * <code>/temp/dir1/readme.txt</code>,
     * <code>/temp/dir1/dir121/readme.txt</code>.  
     *
     * @param basePath Basisverzeichnis.
     * @param relativePath Der relative Pfad - zum Basisverzeichnis - zu der
     *  gesuchten Datei.
     * @param fileName Die gesuchte Datei.
     * @return Eine Liste der Dateien, die den selben Dateibezeichner haben
     *  wie im <code>fileName</code> und auf dem Pfad zwischen Basisverzeichnis
     *  und der gesuchten Datei liegen.
     */
    public static List<File> findFiles(final File basePath,
        final String relativePath, final String fileName) {

        List<File> filesWithSameName = new ArrayList<>();

        File fileInBase = new File(basePath, fileName);
        if (fileInBase.exists()) {
            filesWithSameName.add(fileInBase);
        }

        String newRelativePath = AWToolsFileUtils.normalizePath(relativePath);
        String[] split = StringUtils.split(newRelativePath, "/");

        StringBuilder intermediateDir = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            intermediateDir.append(split[i]).append(File.separatorChar);

            File currentDir = new File(basePath, intermediateDir.toString());
            File checkMyExistence = new File(currentDir, fileName);

            if (checkMyExistence.exists()) {
                filesWithSameName.add(checkMyExistence);
            }
        }

        return filesWithSameName;
    }

    /**
     * Normalisiert einen Pfadausdruck. D.h. aus den Windows-Trenner werden
     * Unix-Trenner, aus /./ wird /. Falls / vorne fehlt, wird dieser
     * vorangestellt. Doppelte // werden durch / ersetzt. Windows
     * Laufwerksbezeichner werden eliminiert.<br/>
     *
     * <b>ACHTUNG:</b> URL wie file://c:/temp werden nicht korrekt verarbeitet,
     * da das // ebenfalls durch / ersetzt wird. 
     *
     * @param fileName Der zu normalisierende Dateiname.
     * @return Der normalisierte Dateiname.
     */
    public static String normalizePath(final String fileName) {
        // Alle Windows Trenner durch Unix Trenner ersetzen.
        String newFileName =
                StringUtils.replace(fileName, "\\", WINFILE_SEPERATOR);

        // Alle // durch / ersetzen.
        newFileName = StringUtils.replace(newFileName, "//", WINFILE_SEPERATOR);
        newFileName =
                StringUtils.replace(newFileName, "///", WINFILE_SEPERATOR);

        // Alle /./ durch / ersetzen. 
        newFileName =
                StringUtils.replace(newFileName, "/./", WINFILE_SEPERATOR);

        // Startsymbole normalisieren.
        if (newFileName.startsWith("./")) {
            newFileName = StringUtils.replace(newFileName, ".", "", 1);
        }

        if (WINDOWS_ROOT_PATTERN.matcher(newFileName).find()) {
            newFileName = newFileName.substring(2);
        }

        if (!newFileName.startsWith(WINFILE_SEPERATOR)) {
            newFileName = WINFILE_SEPERATOR + newFileName;
        }

        // / Endsymbol bei Verzeichnissen entfernen
        if (newFileName.endsWith(WINFILE_SEPERATOR)) {
            newFileName = StringUtils.removeEnd(newFileName, WINFILE_SEPERATOR);
        }

        return newFileName;
    }

    /**
     * Extrahiert aus einem Dateibezeichner den Verzeichnispfad. Beispiel:<br/>
     * Der Parameter <code>temp/ab/db/text.txt</code> führt zu der Rückgabe
     * <code>/temp/ab/db</code>. Ähnliches versucht die Methode
     * <code>File.getParent()</code>, kann aber z.B. die zwei Pfade
     * 'temp/test.txt' und '/temp/test.txt' nicht auf Gleichheit prüfen.
     *
     * @param fileName Der Dateibezeichner dessen Verzeichnispfad ermittelt
     *  werden soll.
     * @return Der Verzeichnispfad.
     */
    public static String getParent(final String fileName) {
        String tmp = AWToolsFileUtils.normalizePath(fileName);
        if (StringUtils.contains(tmp, "/")) {
            return StringUtils.substringBeforeLast(tmp, "/");
        } else {
            return "";
        }
    }

    /**
     * Entfernt aus einem Dateibezeichner alle Pfadangaben.
     * 
     * @param fileName Der zu untersuchende Dateibezeichner. 
     * @return Liefert den Dateibezeichner ohne Pfadangaben.
     */
    public static String getFileName(final String fileName) {
        String tmp = AWToolsFileUtils.normalizePath(fileName);
        if (StringUtils.contains(tmp, "/")) {
            return StringUtils.substringAfterLast(tmp, "/");
        } else {
            return tmp;
        }
    }

}
