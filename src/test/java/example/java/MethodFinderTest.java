/*
 * ============================================================================
 * Project gluehloch-util
 * Copyright (c) 2004-2016 by Andre Winkler. All rights reserved.
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
 * @author by Andre Winkler
 */
public class MethodFinderTest {

	@Test
	public void testMethodFinder() {
		Master object = new Aaa();
		String value = get(object);
		assertThat(value, CoreMatchers.equalTo("Master"));

		Aaa a = new Aaa();
		assertThat(get(a), CoreMatchers.equalTo("aaa_Aaa"));
		assertThat(to(a), CoreMatchers.equalTo("to_Aaa"));

		object = a; // And now the interesting stuff...
		assertThat(get(object), CoreMatchers.equalTo("Master"));
		assertThat(to(object), CoreMatchers.equalTo("to_Aaa"));
	}

	public static <T> String to(T t) {
		return "to_" + t.toString();
	}

	private String get(Master master) {
		return "Master";
	}

	private String get(Aaa aaa) {
		return "aaa_" + aaa.toString();
	}

	@SuppressWarnings("unused")
	private String get(Bbb bbb) {
		return "bbb_" + bbb.toString();
	}

	@SuppressWarnings("unused")
	private String get(Ccc ccc) {
		return "ccc_" + ccc.toString();
	}

	// ------------------------------------------------------------------------

	private interface Master {
		String toString();
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
