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

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import java.util.Locale;

import org.fest.assertions.Delta;
import org.junit.Test;

import de.awtools.basic.NumberUtils;

/**
 * Testet die Klasse {@link de.awtools.basic.NumberUtils}.
 * 
 * @version $LastChangedRevision: 3725 $ $LastChangedDate: 2013-05-24 20:34:57
 *          +0200 (Fr, 24. Mai 2013) $
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 */
public class NumberUtilsTest {

	private static final double DELTA = 0.00001;

	private static final Delta FEST_DELTA = Delta.delta(DELTA);

	@Test
	public void testNumberUtilsToInt() {
		NumberUtils numberUtils = new NumberUtils();
		numberUtils.setPatternAndLocale(Locale.GERMANY, NumberUtils.DEFAULT_DECIMAL_FORMAT);

		assertThat(numberUtils.toInt("5")).isEqualTo(5);
		assertThat(numberUtils.toInt("5,0")).isEqualTo(5);
		assertThat(numberUtils.toInt("5,01")).isEqualTo(5);
		assertThat(numberUtils.toInt("5a")).isEqualTo(5);
		assertThat(numberUtils.toInt("5.0")).isEqualTo(50);
		assertThat(numberUtils.toInt("5.00")).isEqualTo(500);
	}

	@Test
	public void testNumberUtilsToIntFail() {
		NumberUtils numberUtils = new NumberUtils();
		numberUtils.setLocale(Locale.GERMANY);
		try {
			numberUtils.toInt("a5");
			fail("Exception erwartet!");
		} catch (Exception ex) {
		}

		try {
			numberUtils.toInt("a5.00");
			fail("Exception erwartet!");
		} catch (Exception ex) {
		}
	}

	// ------------------------------------------------------------------------

	@Test
	public void testNumberUtilsToDouble() {
		NumberUtils numberUtils = new NumberUtils();
		numberUtils.setLocale(Locale.GERMANY);
		assertThat(numberUtils.toDouble("5")).isEqualTo(5., FEST_DELTA);
		assertThat(numberUtils.toDouble("5,0")).isEqualTo(5., FEST_DELTA);
		assertThat(numberUtils.toDouble("5,01")).isEqualTo(5.01, FEST_DELTA);
		assertThat(numberUtils.toDouble("5a")).isEqualTo(5., FEST_DELTA);
		assertThat(numberUtils.toDouble("5.0")).isEqualTo(50., FEST_DELTA);
		assertThat(numberUtils.toDouble("5.00")).isEqualTo(500., FEST_DELTA);
	}

	@Test
	public void testNumberUtilsToDoubleFail() {
		NumberUtils numberUtils = new NumberUtils();
		numberUtils.setLocale(Locale.GERMANY);
		try {
			numberUtils.toDouble("a5");
			fail("Exception erwartet!");
		} catch (Exception ex) {
		}

		try {
			numberUtils.toDouble("a5.00");
			fail("Exception erwartet!");
		} catch (Exception ex) {
		}
	}

	// ------------------------------------------------------------------------

	@Test
	public void testFormat() {
		NumberUtils numberUtils = new NumberUtils();
		numberUtils.setLocale(Locale.GERMANY);
		assertThat(numberUtils.format(new Double(10.))).isEqualTo("10");
	}

}
