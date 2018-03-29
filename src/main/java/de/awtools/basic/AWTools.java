/*
 * ============================================================================
 * Project awtools-basic Copyright (c) 2000-2016 by Andre Winkler. All rights
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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * Some helper methods.
 * 
 * @author by Andre Winkler
 */
public final class AWTools {

    /**
     * Erstellt ein ArrayList.
     *
     * @param <T>
     *            Typ der Liste.
     * @param values
     *            Werte der Liste.
     * @return Eine Liste.
     */
    @SafeVarargs
    public static <T> List<T> arrayList(final T... values) {
        ArrayList<T> list = new ArrayList<>();
        boolean addAll = list.addAll(Arrays.asList(values));
        if (!addAll) {
            throw new IllegalStateException("There is a failure here.");
        }
        return list;
    }

    /**
     * Erstellt ein HashSet.
     *
     * @param <T>
     *            Typ des Sets.
     * @param values
     *            Die Werte.
     * @return Ein Set.
     */
    @SafeVarargs
    public static <T> Set<T> hashSet(final T... values) {
        HashSet<T> set = new HashSet<>();
        boolean addAll = set.addAll(Arrays.asList(values));
        if (!addAll) {
            throw new IllegalStateException("There is a failure here.");
        }
        return set;
    }

    /**
     * Transforms an int array to a string.
     * 
     * @param arrayOfInts
     *            an array with integer values
     * @return a string of kind '1,2,3,4,5,6'
     */
    public static String toString(int[] arrayOfInts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayOfInts.length; i++) {
            sb.append(arrayOfInts[i]);
            if (i < arrayOfInts.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Wandelt einen double Wert in einen String anhand des angegebenen
     * Patterns. Siehe zu diesem Thema auch im Java-Tutorial bzw. API.
     * 
     * @param value
     *            Der zu konvertierende Wert.
     * @param pattern
     *            Das zu verwendende Pattern.
     * @param locale
     *            Das zu verwendende Locale.
     * @return Formatiert einen <code>double</code>.
     */
    public static String toString(final double value, String pattern,
            final Locale locale) {

        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern(pattern);
        return df.format(value);
    }

    /**
     * Formatiert eine Datumsangabe mit dem Format DD.MM.YYYY in ein Date mit
     * Zeitangabe.
     * 
     * @param string
     *            Die zu transformierende Datumsangabe.
     * @return Der transformierte Wert.
     * @throws NullPointerException
     *             Falls string gleich null.
     * @throws ParseException
     *             Der Parsevorgang schlug fehl.
     * @deprecated Use {@link #toLocalDate_DD_MM_YYYY(String)}
     */
    public static Date stringToDate(final String string) throws ParseException {
        return stringToDate(string, "dd.MM.yyyy");
    }

    // DateTimeFormatter ist nicht veraenderlich und thread-safe!
    private static final DateTimeFormatter DATE_TIME_FORMAT_DD_MM_YYYY = DateTimeFormatter
            .ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT_YYYY_MM_DD = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");

    /**
     * Formatiert einen Datums-String mit dem Format {@code dd.MM.yyyy} nach
     * {@link LocalDate}.
     * 
     * @param date
     *            Ein String mit dem Format {@code dd.MM.yyyy}
     * @return Ein Datum.
     */
    public static LocalDate toLocalDate_DD_MM_YYYY(String date) {
        return LocalDate.parse(date, DATE_TIME_FORMAT_DD_MM_YYYY);
    }

    /**
     * Formatiert einen Datums-String mit dem Format {@code yyyy-MM-dd} nach
     * {@link LocalDate}.
     * 
     * @param date
     *            Ein String mit dem Format {@code yyyy-MM-dd}
     * @return Ein Datum.
     */
    public static LocalDate toLocalDate_YYYY_MM_DD(String date) {
        return LocalDate.parse(date, DATE_TIME_FORMAT_YYYY_MM_DD);
    }

    /**
     * Formatiert eine Datumsangabe mit dem angegebenen Format in ein Date mit
     * Zeitangabe.
     * 
     * @param string
     *            Die zu transformierende Datumsangabe.
     * @param format
     *            Das zu verwendende Pattern.
     * @return Der transformierte Wert.
     * @throws ParseException
     *             Der Parsevorgang schlug fehl.
     * @deprecated Use {@link #toLocalDate_DD_MM_YYYY(String)} or another
     *             feature of the Java 8 API.
     */
    public static Date stringToDate(final String string, final String format)
            throws ParseException {

        Validate.notNull(string, "string ist eine 'null' Referenz.");
        Validate.notNull(format, "format ist eine 'null' Referenz.");

        return new SimpleDateFormat(format).parse(string);
    }

    /**
     * Konvertiert die Stacktrace Meldungen einer Exception in einen String.
     * 
     * @param ex
     *            Die zu konvertierende Exception.
     * @return Der Stacktrace der Exception als String.
     */
    public static String stacktraceToString(final Exception ex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        ex.printStackTrace(ps);
        return baos.toString();
    }

    /**
     * Ersetzt die ${...} Platzhalter in einem String. Die Ersetzung werden in
     * einer Map gelagert. Die Schluessel repraesentieren die Platzhalter im
     * String. Die Ersetzungen sind die Werte der Schluessel in der
     * <code>placeholders</code> Map.
     *
     * @param string
     *            Der zu pruefende String.
     * @param placeholders
     *            Die Ersetzungen.
     * @return Der ueberarbeitete String.
     */
    public static String replacePlaceholder(final String string,
            final Map<String, String> placeholders) {

        String result = string;
        String[] keys = StringUtils.substringsBetween(string, "${", "}");

        if (keys != null) {
            for (String key : keys) {
                String value = placeholders.get(key);
                if (value != null) {
                    String sb = "${" + key + "}";
                    result = StringUtils.replace(result, sb.toString(), value);
                }
            }
        }

        return result;
    }

}
