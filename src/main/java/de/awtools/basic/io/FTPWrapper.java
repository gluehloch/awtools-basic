/*
 * $Id: FTPWrapper.java 2993 2011-11-24 19:51:48Z andrewinkler $
 * ============================================================================
 * Project awtools-basic
 * Copyright (c) 2004-2013 by Andre Winkler. All rights reserved.
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;

/**
 * Ein Wrapper für den Apache FTPClient.
 *
 * @version $LastChangedRevision: 2993 $ $LastChangedDate: 2011-11-24 20:51:48 +0100 (Thu, 24 Nov 2011) $
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 */
public class FTPWrapper {

    private static final Logger log = LoggerFactory.make();

    private final FTPClient ftpClient = new FTPClient();

    /**
     * Connect und Login Methode.
     *
     * @param host Der Host.
     * @param userName Ein User.
     * @param password Ein Password.
     * @return true, alles in Ordnung; false sonst.
     * @throws IOException Da ging was schief.
     * @throws UnknownHostException Da ging was schief.
     * @throws FTPConnectionClosedException Da ging was schief.
     */
    public boolean connectAndLogin(final String host, final String userName,
        final String password) throws IOException, UnknownHostException,
        FTPConnectionClosedException {

        boolean success = false;
        ftpClient.connect(host);
        int reply = ftpClient.getReplyCode();
        if (FTPReply.isPositiveCompletion(reply)) {
            success = ftpClient.login(userName, password);
        }
        if (!success) {
            ftpClient.disconnect();
        }
        return success;
    }

    /**
     * Setzt den Passiv-Modus.
     *
     * @param setPassive Passiv-Modus.
     */
    public void setPassiveMode(final boolean setPassive) {
        if (setPassive) {
            ftpClient.enterLocalPassiveMode();
        } else {
            ftpClient.enterLocalActiveMode();
        }
    }

    /**
     * ASCII Modus ein.
     *
     * @return true, alles in Ordnung; false, sonst.
     * @throws IOException Da ging was schief.
     */
    public boolean ascii() throws IOException {
        return ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
    }

    /**
     * Binär Modus ein.
     *
     * @return true, alles in Ordnung; false, sonst.
     * @throws IOException Da ging was schief.
     */
    public boolean binary() throws IOException {
        return ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    }

    /**
     * Startet einen File-Download.
     *
     * @param serverFile Das Server-File.
     * @param localFile Name der lokalen Datei.
     * @return true, alles in Ordnung; false, sonst.
     * @throws IOException Da ging was schief.
     * @throws FTPConnectionClosedException Da ging was schief.
     */
    public boolean downloadFile(final String serverFile, final String localFile)
        throws IOException, FTPConnectionClosedException {

        boolean result = false;

        try(FileOutputStream out = new FileOutputStream(localFile)) {

            log.info("Downloading file ->{}<- to local file ->{}<-.",
                serverFile, localFile);
            result = ftpClient.retrieveFile(serverFile, out);

        }
        return result;
    }

    /**
     * Startet einen File-Upload.
     *
     * @param localFile Name der lokalen Datei.
     * @param serverFile Name der Datei auf dem Server.
     * @return true, alles in Ordnung; false, sonst.
     * @throws IOException Da ging was schief.
     * @throws FTPConnectionClosedException Da ging was schief.
     */
    public boolean uploadFile(final String localFile, final String serverFile)
        throws IOException, FTPConnectionClosedException {

        FileInputStream in = null;
        boolean result = false;
        try {
            in = new FileInputStream(localFile);
            log.info("Downloading file ->{}<- to local file ->{}<-.",
                serverFile, localFile);
            result = ftpClient.storeFile(serverFile, in);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return result;
    }

    /**
     * Liest die Namen der Dateien im aktuellen Verzeichnis.
     *
     * @return Eine Liste mit Dateinamen.
     * @throws IOException Da ging was schief.
     * @throws FTPConnectionClosedException Da ging was schief.
     */
    public List<String> listFileNames() throws IOException,
        FTPConnectionClosedException {

        FTPFile[] files = ftpClient.listFiles();
        List<String> v = new ArrayList<String>();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory())
                v.add(files[i].getName());
        }
        return v;
    }

    /**
     * Ein String mit allen Dateinamen.
     *
     * @return Ein String mit Dateinamen.
     * @throws IOException Da ging was schief.
     * @throws FTPConnectionClosedException Da ging was schief.
     */
    public String listFileNamesString() throws IOException,
        FTPConnectionClosedException {

        return listToString(listFileNames(), "\n");
    }

    /** 
     * Eine Liste mit allen Sub-Directories.
     *
     * @return Eine Liste mit Sub-Directories.
     * @throws IOException Da ging was schief.
     * @throws FTPConnectionClosedException Da ging was schief.
     */
    public List<String> listSubdirNames() throws IOException,
        FTPConnectionClosedException {

        FTPFile[] files = ftpClient.listFiles();
        List<String> v = new ArrayList<String>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory())
                v.add(files[i].getName());
        }
        return v;
    }

    /**
     * Ein String mit allen Dateien aus dem aktuellen Verzeichnis.
     *
     * @return Ein String mit allen Dateien.
     * @throws IOException Da ging was schief.
     * @throws FTPConnectionClosedException Da ging was schief.
     */
    public String listSubdirNamesString() throws IOException,
        FTPConnectionClosedException {

        return listToString(listSubdirNames(), "\n");
    }

    /**
     * Konvertiert eine Liste in einen String.
     *
     * @param v Eine Liste.
     * @param delim Delimiter.
     * @return Der zusammengesetzte String.
     */
    private String listToString(final List<?> v, final String delim) {
        StringBuffer sb = new StringBuffer();
        String s = "";
        for (Iterator<?> i = v.iterator(); i.hasNext();) {
            sb.append(s).append(i.next().toString());
            s = delim;
        }
        return sb.toString();
    }

    /**
     * Wechselt das Arbeitsverzeichnis.
     *
     * @param directoryName Name des Verzeichnisses.
     * @return <code>true</code> success; <code>false</code> otherwise. 
     * @throws IOException Da ging was schief.
     *
     * @since 1.6
     */
    public boolean changeWorkingDirectory(final String directoryName)
        throws IOException {

        return ftpClient.changeWorkingDirectory(directoryName);
    }

    /**
     * Prüft den Reply-Code einer FTP Verbindung.
     * 
     * @throws RuntimeException Da ging was schief.
     *
     * @since 1.6
     */
    public void checkReplyCode() throws IOException {
        int reply = ftpClient.getReplyCode();

        if (!FTPReply.isPositiveCompletion(reply)) {
            log.info("Error: {}", ftpClient.getReplyString());
            throw new RuntimeException();
        }
    }

    /**
     * Legt ein Verzeichnis auf dem Server an.
     *
     * @param directoryName Der Name des anzulegenden Verzeichnisses.
     * @throws IOException Da ging was schief.
     *
     * @since 1.6
     */
    public void makeDirectory(final String directoryName) throws IOException {
        ftpClient.makeDirectory(directoryName);
    }

    /**
     * Wechselt in das angegebene Verzeichnis.
     *
     * @param rootDirectory Das Wurzelverzeichnis.
     * @param directory Das Verzeichnis.
     * @throws IOException Da ging was schief.
     *
     * @since 1.6
     */
    public void changeOrCreateDirectory(final String rootDirectory,
        final String directory) throws IOException {

        boolean ok = true;

        ok = ok && changeWorkingDirectory("/");
        ok = ok && changeWorkingDirectory(rootDirectory);

        if (!ok) {
            throw new IOException("Unable to change to the root directory.");
        }

        String[] dirs = StringUtils.split(directory, '/');
        for (int i = 0; i < dirs.length; i++) {
            if (changeWorkingDirectory(dirs[i])) {
                checkReplyCode();
            } else {
                makeDirectory(dirs[i]);
                checkReplyCode();
                changeWorkingDirectory(dirs[i]);
                checkReplyCode();
            }
        }
    }

    /**
     * Schliesst die FTP Verbindung.
     *
     * @since 1.6
     */
    public void close() {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException f) {
                // do nothing
            }
        }
    }

}
