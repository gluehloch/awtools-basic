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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;

/**
 * Testet <code>WinFileUtils.findFiles()</code>. Für die Tests wird im
 * USER_HOME Verzeichnis ein <code>.build-tmp/</code> Verzeichnis angelegt.
 * 
 * @author by Andre Winkler
 */
public class FindFilesTest {

    /** Der private Logger der Klasse. */
    private static Logger log = LoggerFactory.make();

    private static final String BUILD_TMP_DIR = ".gluehloch-util-build/";

    private static File TMP_DIR;

    private static final String TEST_DIR = "test/winkler/arbeit";

    private static final String TEST_FILE = TEST_DIR + "/test.txt";

    @Test
    public void testStringUtilsSubstring() {
        String fileName = "findmy.txt";
        String relativePath = StringUtils.substringBeforeLast(fileName, "/");
        String realFileName = StringUtils.substringAfterLast(fileName, "/");

        Assert.assertTrue(!(StringUtils.isBlank(relativePath)));
        Assert.assertEquals(fileName, relativePath);
        Assert.assertTrue(StringUtils.isBlank(realFileName));
    }

    @Test
    public void testFindFilesTwoParams() throws Exception {
        StringBuilder tempDir = new StringBuilder(TMP_DIR.getPath());
        if (!(TMP_DIR.getPath().endsWith(AWToolsFileUtils.FILESEPARATOR))) {
            tempDir.append(AWToolsFileUtils.FILESEPARATOR);
        }

        if (log.isDebugEnabled()) {
            log.debug("Searching in '" + tempDir + "'.");
        }

        List<File> files =
                AWToolsFileUtils.findFiles(new File(tempDir.toString()),
                    "test/winkler/arbeit/findmy.txt");

        if (log.isDebugEnabled()) {
            for (File file : files) {
                log.debug("(1) Find file: " + file);
            }
        }

        assertEquals(3, files.size());

        files =
                AWToolsFileUtils.findFiles(new File(tempDir.toString()),
                    "findmy.txt");

        for (File file : files) {
            log.debug("(2) Find file: " + file);
        }

        assertEquals(1, files.size());
    }

    @Test
    public void testFindFiles() throws Exception {
        StringBuilder tempDir = new StringBuilder(TMP_DIR.getPath());
        if (!(TMP_DIR.getPath().endsWith(AWToolsFileUtils.FILESEPARATOR))) {
            tempDir.append(AWToolsFileUtils.FILESEPARATOR);
        }

        if (log.isDebugEnabled()) {
            log.debug("WinFileUtils.findFiles('" + tempDir
                + "', 'test/winkler/arbeit', 'findmy.txt'");
        }

        List<File> files =
                AWToolsFileUtils.findFiles(new File(tempDir.toString()),
                    "test/winkler/arbeit", "findmy.txt");

        assertEquals(3, files.size());
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        TMP_DIR = new File(SystemUtils.getUserHome(), BUILD_TMP_DIR);
        try {
            FileUtils.forceMkdir(TMP_DIR);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        AWToolsFileUtils.createFilePath(TMP_DIR.getPath(), TEST_FILE);

        File file1 = new File(TMP_DIR, "findmy.txt");
        File file2 = new File(TMP_DIR, "test/winkler/findmy.txt");
        File file3 = new File(TMP_DIR, "test/winkler/arbeit/findmy.txt");

        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();
    }

    @AfterClass
    public static void tearDown() {
        FileUtils.deleteQuietly(TMP_DIR);
    }

}
