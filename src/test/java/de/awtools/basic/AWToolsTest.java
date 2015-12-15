/*
 * $Id: AWToolsTest.java 3817 2013-09-29 14:54:25Z andrewinkler $
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

package de.awtools.basic;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;
import de.awtools.basic.AWTools;

/**
 * Testet die Klasse WinString.
 *
 * @version $LastChangedRevision: 3817 $ $LastChangedDate: 2013-09-29 16:54:25 +0200 (So, 29. Sep 2013) $
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 */
public class AWToolsTest {

    /** Der private Logger der Klasse. */
    private final Logger log = LoggerFactory.make();

    @Test
    public void testStringToDate() {
        // Wird eine NullPointerException geworfen?
        try {
            AWTools.stringToDate(null);
            fail("Eine Exception erwartet.");
        } catch (IllegalArgumentException ex) {
            // Ok
        } catch (ParseException ex) {
            fail("Eine Null-Pointer-Exception erwartet.");
        }

        // Ein 24.12.2002 in Date umwandeln.
        GregorianCalendar greg1 = new GregorianCalendar(1971, 2, 24, 0, 0, 0);
        String stringDate1 = "24.3.1971 00:00:00";
        GregorianCalendar greg2 = new GregorianCalendar(1971, 2, 24, 15, 30, 0);
        String stringDate2 = "24.3.1971 15:30:00";

        try {
            Date convert =
                    AWTools.stringToDate(stringDate1, "dd.MM.yyyy HH:mm:ss");

            log.debug("Input: " + stringDate1);
            log.debug("Equal: "
                + DateFormat.getDateInstance().format(greg1.getTime()));
            log.debug("Parse: " + convert);
            log.debug("Conv : " + DateFormat.getDateInstance().format(convert));

            assertThat(convert).isNotNull();
            assertThat(convert).isEqualTo(greg1.getTime());
            assertThat(greg1.getTime().compareTo(convert)).isEqualTo(0);
        } catch (ParseException  ex) {
            log.debug("ParseException", ex);
            fail("Eine ParseException erwartet.");
        }

        try {
            Date convert =
                    AWTools.stringToDate(stringDate2, "dd.MM.yyyy HH:mm:ss");

            log.debug("Input: " + stringDate2);
            log.debug("Equal: "
                + DateFormat.getDateInstance(DateFormat.LONG).format(
                    greg2.getTime()));
            log.debug("Parse: " + convert);
            log.debug("Conv : " + DateFormat.getDateInstance().format(convert));

            assertThat(convert).isNotNull();
            assertThat(convert).isEqualTo(greg2.getTime());
            assertThat(convert).isEqualTo(greg2.getTime());
        } catch (ParseException ex) {
            log.debug("ParseException", ex);
            fail("Eine ParseException erwartet.");
        }
    }

    @Test
    public void testFormatDouble() {
        double[] value =
                new double[] { 100.01, 100.001, 89.3, 5555.5555, 12.1, 13.12,
                        5.5 };

        String[] conv =
                new String[] { "100,01", "100,00", "89,30", "5.555,56",
                        "12,10", "13,12", "5,50" };

        for (int i = 0; i < value.length; i++) {
            String x = AWTools.toString(value[i], "###,##0.00", Locale.GERMAN);

            log.debug("Alt: " + new Double(value[i]) + " Neu: " + x);
            assertThat(x).isEqualTo(conv[i]);
        }
    }

    @Test
    public void testReplacePlaceholder() {
        String test = "Hallo ${name}! Wie war der ${heute}?";
        Map<String, String> placeholder = new HashMap<>();

        placeholder.put("name", "Andre");
        assertThat(AWTools.replacePlaceholder(test, placeholder)).isEqualTo(
            "Hallo Andre! Wie war der ${heute}?");

        placeholder.put("heute", "Sonntag");
        assertThat(AWTools.replacePlaceholder(test, placeholder)).isEqualTo(
            "Hallo Andre! Wie war der Sonntag?");

        test = "Ganz normaler String";
        assertThat(AWTools.replacePlaceholder(test, placeholder)).isEqualTo(
            test);
    }

    @Test
    public void testAWToolsToString() {
        int[] array = new int[] { 1, 2, 3, 4, 5, 6 };
        assertThat(AWTools.toString(array)).isEqualTo("1,2,3,4,5,6");
    }

    @Test
    public void testAWToolsToStringWithAnEmptyArray() {
        int[] array = new int[] {};
        assertThat(AWTools.toString(array)).isEqualTo("");
    }

    @Test
    public void testAWToolsToStringWithSingleElementArray() {
        int[] array = new int[] { 1 };
        assertThat(AWTools.toString(array)).isEqualTo("1");
    }

}
