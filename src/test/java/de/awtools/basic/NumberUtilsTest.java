/*
 * ============================================================================
 * Project awtools-basic Copyright (c) 2000-2018 by Andre Winkler. All rights
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

package de.awtools.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Locale;

import org.assertj.core.data.Offset;
import org.junit.Test;

import de.awtools.basic.NumberUtils;

/**
 * Testet die Klasse {@link de.awtools.basic.NumberUtils}.
 * 
 * @author by Andre Winkler
 */
public class NumberUtilsTest {

    private static final Offset<Double> DELTA = Offset.offset(0.00001);

    @Test
    public void testNumberUtilsToInt() {
        NumberUtils numberUtils = NumberUtils.numbero(Locale.GERMANY,
                NumberUtils.DEFAULT_DECIMAL_FORMAT);

        assertThat(numberUtils.toInt("5")).isEqualTo(5);
        assertThat(numberUtils.toInt("5,0")).isEqualTo(5);
        assertThat(numberUtils.toInt("5,01")).isEqualTo(5);
        assertThat(numberUtils.toInt("5a")).isEqualTo(5);
        assertThat(numberUtils.toInt("5.0")).isEqualTo(50);
        assertThat(numberUtils.toInt("5.00")).isEqualTo(500);
    }

    @Test
    public void testNumberUtilsToIntFail() {
        NumberUtils numberUtils = NumberUtils.numbero(Locale.GERMANY,
                NumberUtils.DEFAULT_DECIMAL_FORMAT);

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
        NumberUtils numberUtils = NumberUtils.numbero(Locale.GERMANY,
                NumberUtils.DEFAULT_DECIMAL_FORMAT);

        assertThat(numberUtils.toDouble("5")).isEqualTo(5., DELTA);
        assertThat(numberUtils.toDouble("5,0")).isEqualTo(5., DELTA);
        assertThat(numberUtils.toDouble("5,01")).isEqualTo(5.01, DELTA);
        assertThat(numberUtils.toDouble("5a")).isEqualTo(5., DELTA);
        assertThat(numberUtils.toDouble("5.0")).isEqualTo(50., DELTA);
        assertThat(numberUtils.toDouble("5.00")).isEqualTo(500., DELTA);
    }

    @Test
    public void testNumberUtilsToDoubleFail() {
        NumberUtils numberUtils = NumberUtils.numbero(Locale.GERMANY,
                NumberUtils.DEFAULT_DECIMAL_FORMAT);

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
        NumberUtils numberUtils = NumberUtils.numbero(Locale.GERMANY,
                NumberUtils.DEFAULT_DECIMAL_FORMAT);
        assertThat(numberUtils.format(new Double(10.))).isEqualTo("10");
    }

}
