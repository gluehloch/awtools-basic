/*
 * $Id: LoggerFactory.java 2984 2011-11-13 17:28:22Z andrewinkler $
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

package de.awtools.basic;

import org.slf4j.Logger;

/**
 * Eine Factory für das Generieren von klassenbezogenen commons-logging
 * Loggern.
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 2984 $ $Date: 2011-11-13 18:28:22 +0100 (So, 13. Nov 2011) $
 */
public class LoggerFactory {

    /**
     * Erstellt einen klassenbezogenen Logger.
     * <pre>
     * private static final Log logger = LoggerFactory.make();
     * </pre>
     * 
     * @return Liefert einen commons-logging <code>Log</code>.
     */
    public static Logger make() {
        Throwable t = new Throwable();
        StackTraceElement directCaller = t.getStackTrace()[1];
        return org.slf4j.LoggerFactory.getLogger(directCaller.getClassName());
    }

}
