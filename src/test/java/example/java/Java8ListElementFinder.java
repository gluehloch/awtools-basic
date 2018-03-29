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

package example.java;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Find an element with Java 8 streams.
 * 
 * @author Andre Winkler
 */
public class Java8ListElementFinder {

    @Test
    public void findListElement() {
        List<String> list = new ArrayList<>();
        list.add("Andre");
        list.add("Christine");
        list.add("Adam");
        list.add("Lars");
        list.add("Erwin");
        list.add("Spike");

        assertThat("Lars",
                is(list.stream()
                        .filter((String a) -> StringUtils.equals("Lars", a))
                        .findFirst().get()));
    }

    @Test
    public void findListElement2() {
        List<String> list = Arrays.asList("Andre", "Lars", "Adam", "Erwin",
                "Christine");
        assertThat("Lars",
                is(list.stream()
                        .filter((String a) -> StringUtils.equals("Lars", a))
                        .findFirst().get()));
        
        list.forEach(s -> System.out.println(s));
        list.forEach(System.out::println);
    }

}
