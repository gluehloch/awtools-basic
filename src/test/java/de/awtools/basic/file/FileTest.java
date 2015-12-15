/*
 * $Id: FileTest.java 3725 2013-05-24 18:34:57Z andrewinkler $
 * ============================================================================
 * Project awtools-basic
 * Copyright (c) 2000-2011 by Andre Winkler. All rights reserved.
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

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Test for the Java {@link File} class.
 * 
 * @version $LastChangedRevision: 3725 $ $LastChangedDate: 2013-05-24 20:34:57 +0200 (Fr, 24. Mai 2013) $
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 */
public class FileTest {

    @Test
    public void testFile() {
        File userHomeDir = new File(System.getProperty("user.home"));
        File userHomeDir_test = new File(userHomeDir, "test");
        File userHomeDir_test_awtools =
                new File(userHomeDir_test, "awtools.txt");

        assertThat(userHomeDir_test_awtools.toString()).endsWith("awtools.txt");
        System.out.println(userHomeDir_test_awtools);
    }

}
