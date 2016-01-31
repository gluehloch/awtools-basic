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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.text.MaskFormatter;

/**
 * Eine Utility Klasse für das Formatieren von Zahlen sowie für das Parsen von
 * Zahlen aus Strings heraus.
 * 
 * @author by Andre Winkler
 * 
 * @todo Es wird das Default-Locale für alle Formatierungsaufgaben verwendet.
 *       Zusätzlich wird momentan der deutsche Dezimaltrenner ',' durch ein '.'
 *       ersetzt.
 *
 * @todo Ein Cache für die verschiedenen Formatierungsklassen?
 */
public class NumberUtils {

	/** Das default-mäßige Format. */
	public static final String DEFAULT_DECIMAL_FORMAT = "###,###.##";

	/** Das Format für Währungsanzeigen. */
	public static final String DEFAULT_ZERO_FORMAT = "##,##0.00";

	/** the empty string (placeholder) */
	private static final String EMPTY = "";

	/** Das Default-Locale. */
	private Locale locale = Locale.getDefault();

	/** Das Default-Pattern. */
	private String pattern = DEFAULT_DECIMAL_FORMAT;

	/**
	 * Setzt das Pattern neu.
	 *
	 * @param _pattern
	 *            Das zu verwendende Pattern.
	 */
	public void setPattern(final String _pattern) {
		setPatternAndLocale(locale, _pattern);
	}

	/**
	 * Setzt das Locale neu.
	 *
	 * @param _locale
	 *            Das zu verwendende Locale.
	 */
	public void setLocale(final Locale _locale) {
		setPatternAndLocale(_locale, pattern);
	}

	/**
	 * Setzt das Pattern und das Locale neu.
	 *
	 * @param _locale
	 *            Das zu verwendende Locale.
	 * @param _pattern
	 *            Das zu verwendende Pattern.
	 */
	public void setPatternAndLocale(final Locale _locale, final String _pattern) {
		if (_locale == null) {
			locale = Locale.getDefault();
		} else {
			locale = _locale;
		}

		if (_pattern == null) {
			pattern = DEFAULT_DECIMAL_FORMAT;
		} else {
			pattern = _pattern;
		}
	}

	private DecimalFormat createNumberFormatter() {
		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
		decimalFormat.applyPattern(pattern);
		return decimalFormat;
	}

	// -------------------------------------------------------------------------

	/**
	 * Erzeugt einen <code>MaskFormatter</code> für ein
	 * <code>JFormattedTextField</code>.
	 * 
	 * @param mask
	 *            Das Pattern für den <code>MaskFormatter</code>.
	 * @return Der erzeugte Formatter.
	 */
	public MaskFormatter createFormatter(final String mask) {
		MaskFormatter formatter;
		try {
			formatter = new MaskFormatter(mask);
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
		return formatter;
	}

	// ------------------------------------------------------------------------
	// Abschnitt mit den toInt, toDouble, toXxx Methoden. Alle Methoden
	// basieren auf der Methode #toNumber(String) und werfen eine
	// RuntimeException.
	//

	/**
	 * Parst einen String nach <code>int</code>. Falls eine Ausnahme auftritt,
	 * wird eine <code>RuntimeException</code> geworfen.
	 * 
	 * @param value
	 *            Der zu parsende String
	 * @return Der geparste Wert.
	 */
	public int toInt(final String value) {
		return (toNumber(value).intValue());
	}

	/**
	 * Parst einen String nach <code>short</code>. Falls eine Ausnahme auftritt,
	 * wird eine <code>RuntimeException</code> geworfen.
	 * 
	 * @param value
	 *            Der zu parsende String
	 * @return Der geparste Wert.
	 */
	public short toShort(final String value) {
		return (toNumber(value).shortValue());
	}

	/**
	 * Parst einen String nach <code>long</code>. Falls eine Ausnahme auftritt,
	 * wird eine <code>RuntimeException</code> geworfen.
	 * 
	 * @param value
	 *            Der zu parsende String
	 * @return Der geparste Wert.
	 */
	public long toLong(final String value) {
		return (toNumber(value).longValue());
	}

	/**
	 * Parst einen String nach <code>double</code>. Falls eine Ausnahme
	 * auftritt, wird eine <code>RuntimeException</code> geworfen.
	 * 
	 * @param value
	 *            Der zu parsende String
	 * @return Der geparste Wert.
	 */
	public double toDouble(final String value) {
		return (toNumber(value).doubleValue());
	}

	/**
	 * Parst einen String nach <code>float</code>. Falls eine Ausnahme auftritt,
	 * wird eine <code>RuntimeException</code> geworfen.
	 * 
	 * @param value
	 *            Der zu parsende String
	 * @return Der geparste Wert.
	 */
	public float toFloat(final String value) {
		return (toNumber(value).floatValue());
	}

	// -- formatter -----------------------------------------------------------

	/**
	 * Formatiert ein <code>long</code>.
	 * 
	 * @param value
	 *            <code>long</code>
	 * @return Der formatierte Wert.
	 */
	public String formatLong(final long value) {
		return (format(new Double(value)));
	}

	/**
	 * Formatiert ein <code>double</code>.
	 * 
	 * @param value
	 *            <code>double</code>
	 * @return Der formatierte Wert.
	 */
	public String formatDouble(final double value) {
		return (format(new Double(value)));
	}

	/**
	 * Formatiert ein <code>Number</code>.
	 * 
	 * @param value
	 *            <code>Number</code>
	 * @return Der formatierte Wert.
	 */
	public String format(final Number value) {
		DecimalFormat decimalFormat = createNumberFormatter();
		if (value == null) {
			return EMPTY;
		} else {
			return (decimalFormat.format(value));
		}
	}

	/**
	 * Parst einen <code>String</code> nach <code>Number</code>.
	 * 
	 * @param value
	 *            Ein String.
	 * @return Der übersetzte Wert als <code>Number</code>.
	 * 
	 * @see #parseNumber(String)
	 */
	public Number toNumber(final String value) {
		Number number;
		try {
			number = parseNumber(value);
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
		return number;
	}

	/**
	 * Parst einen <code>String</code> nach <code>Number</code>.
	 * 
	 * @param value
	 *            Ein String.
	 * @return Der übersetzte Wert als <code>Number</code>.
	 * @throws ParseException
	 *             Der String konnte nicht geparst werden.
	 */
	public Number parseNumber(final String value) throws ParseException {
		DecimalFormat decimalFormat = createNumberFormatter();
		return (decimalFormat.parse(value));
	}

}
