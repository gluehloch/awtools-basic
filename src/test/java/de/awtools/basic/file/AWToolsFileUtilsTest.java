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

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testet die Klasse {@link AWToolsFileUtils}.
 *
 * @author by Andre Winkler
 */
public class AWToolsFileUtilsTest {

	private static final String BUILD_TMP_DIR = ".build-tmp/";

	private static final String TMP_DIR;

	private static final String TEST_DIR = "test/winkler/arbeit";

	private static final String TEST_FILE = TEST_DIR + "/test.txt";

	static {
		StringBuilder sb = new StringBuilder(System.getProperty("user.home"));
		sb.append(File.separatorChar).append(BUILD_TMP_DIR);
		TMP_DIR = sb.toString();
	}

	@Test
	public void testRegexMatching() {
		Pattern pattern = Pattern.compile("^[a-zA-Z]:");
		Matcher matcher = pattern.matcher("C:/andre");
		Assert.assertTrue(matcher.find());
	}

	@Test
	public void testNormalizePath() throws Exception {
		assertThat(AWToolsFileUtils.normalizePath("/test/ab/./ab/test.xml")).isEqualTo("/test/ab/ab/test.xml");
		assertThat(AWToolsFileUtils.normalizePath("/test/ab/./ab\\test.xml")).isEqualTo("/test/ab/ab/test.xml");
		assertThat(AWToolsFileUtils.normalizePath("test/ab/\\.\\ab/test.xml")).isEqualTo("/test/ab/ab/test.xml");
		assertThat(AWToolsFileUtils.normalizePath("test/ab//.//ab/test.xml")).isEqualTo("/test/ab/ab/test.xml");
		assertThat(AWToolsFileUtils.normalizePath("/test.xml")).isEqualTo("/test.xml");
		assertThat(AWToolsFileUtils.normalizePath("/test/andre/")).isEqualTo("/test/andre");
		assertThat(AWToolsFileUtils.normalizePath("/test/andre")).isEqualTo("/test/andre");
		assertThat(AWToolsFileUtils.normalizePath("test/andre/")).isEqualTo("/test/andre");
		assertThat(AWToolsFileUtils.normalizePath("C:/test/andre/")).isEqualTo("/test/andre");
		assertThat(AWToolsFileUtils.normalizePath("C:/test/andre")).isEqualTo("/test/andre");
		assertThat(AWToolsFileUtils.normalizePath("C:\\test/andre/")).isEqualTo("/test/andre");
		assertThat(AWToolsFileUtils.normalizePath("C:\\test\\andre\\")).isEqualTo("/test/andre");
		assertThat(AWToolsFileUtils.normalizePath("C:\\test\\andre\\")).isEqualTo("/test/andre");
		assertThat(AWToolsFileUtils.normalizePath("/home/awinkler2/test")).isEqualTo("/home/awinkler2/test");
	}

	@Test
	public void testPathToRoot() {
		assertThat(AWToolsFileUtils.pathToRoot(0)).isEqualTo("./");
		assertThat(AWToolsFileUtils.pathToRoot(1)).isEqualTo("./../");
		assertThat(AWToolsFileUtils.pathToRoot(2)).isEqualTo("./../../");
		assertThat(AWToolsFileUtils.pathToRoot(3)).isEqualTo("./../../../");
	}

	@Test
	public void testStringUtils() {
		assertThat(StringUtils.replace("abc/./abc/./abc", "/./", "/")).isEqualTo("abc/abc/abc");
		assertThat(StringUtils.replace("abc\\.\\abc\\.\\abc", "\\.\\", "/")).isEqualTo("abc/abc/abc");
	}

	@Test
	public void testCountDirLevel() {
		assertThat(AWToolsFileUtils.countDirLevel("winkler.txt")).isEqualTo(0);
		assertThat(AWToolsFileUtils.countDirLevel("test\\winkler.txt")).isEqualTo(1);
		assertThat(AWToolsFileUtils.countDirLevel("ts\\ts\\winkler.txt")).isEqualTo(2);
		assertThat(AWToolsFileUtils.countDirLevel("tw/winkler.txt")).isEqualTo(1);
		assertThat(AWToolsFileUtils.countDirLevel("tw/te/winkler.txt")).isEqualTo(2);
		assertThat(AWToolsFileUtils.countDirLevel("t/w/sdf/winkler.txt")).isEqualTo(3);
		assertThat(AWToolsFileUtils.countDirLevel("C:/test/test/winkler.txt")).isEqualTo(2);
		assertThat(AWToolsFileUtils.countDirLevel("/t/w/sdf/winkler.txt")).isEqualTo(3);
		assertThat(AWToolsFileUtils.countDirLevel("t/./w/sdf/winkler.txt")).isEqualTo(3);
	}

	@Test
	public void testCreateFilePath() {
		AWToolsFileUtils.createFilePath(TMP_DIR, TEST_FILE);
		File dir = new File(TMP_DIR + TEST_DIR);
		assertThat(dir).isDirectory();
		assertThat(dir.canRead()).isTrue();
		assertThat(dir.canWrite()).isTrue();
	}

	@Test
	public void testGetParent() {
		assertThat(AWToolsFileUtils.getParent("tmp/ab/ab/test.txt")).isEqualTo("/tmp/ab/ab");
		assertThat(AWToolsFileUtils.getParent("tmp/test.txt")).isEqualTo("/tmp");
		assertThat(AWToolsFileUtils.getParent("/test.txt")).isEqualTo("");
		assertThat(AWToolsFileUtils.getParent("test.txt")).isEqualTo("");
	}

	@Test
	public void testGetFileName() {
		assertThat(AWToolsFileUtils.getFileName("tmp/ab/ab.txt")).isEqualTo("ab.txt");
		assertThat(AWToolsFileUtils.getFileName("test.txt")).isEqualTo("test.txt");
		assertThat(AWToolsFileUtils.getFileName("/test.txt")).isEqualTo("test.txt");
	}

	@Before
	public void setUp() {
		try {
			FileUtils.forceMkdir(new File(TMP_DIR));
		} catch (IOException ex) {
			// ignore
		}
	}

	@After
	public void tearDown() throws Exception {
		FileUtils.deleteQuietly(new File(TMP_DIR));
	}

}
