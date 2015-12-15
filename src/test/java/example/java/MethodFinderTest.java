/*
 * $Id: org.eclipse.jdt.ui.prefs 823 2007-07-05 10:24:57Z andrewinkler $
 * ============================================================================
 * Project gluehloch-util
 * Copyright (c) 2004-2014 by Andre Winkler. All rights reserved.
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

package example.java;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

/**
 * Which method will be called?
 * 
 * @version $LastChangedRevision: 1502 $ $LastChangedDate: 2009-02-20 22:02:51 +0100 (Fr, 20 Feb 2009) $
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 */
public class MethodFinderTest {

    @Test
    public void testMethodFinder() {
        Master object = new Aaa();
        String value = get(object);
        assertThat(value, CoreMatchers.equalTo("Master"));
        
        Aaa a = new Aaa();
        assertThat(get(a), CoreMatchers.equalTo("Aaa")) ;
    }

//    private <T> String get(T t) {
//        return t.toString();
//    }
//
    private String get(Master master) {
        return "Master";
    }

    private String get(Aaa aaa) {
        return aaa.toString();
    }

    private String get(Bbb bbb) {
        return bbb.toString();
    }

    private String get(Ccc ccc) {
        return ccc.toString();
    }

    private interface Master {

    }

    private class Aaa implements Master {
        @Override
        public String toString() {
            return "Aaa";
        }
    }

    private class Bbb implements Master {
        @Override
        public String toString() {
            return "Bbb";
        }
    }

    private class Ccc implements Master {
        @Override
        public String toString() {
            return "Ccc";
        }
    }

}
