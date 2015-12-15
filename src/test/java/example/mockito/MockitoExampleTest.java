/*
 * $Id: MockitoExampleTest.java 3725 2013-05-24 18:34:57Z andrewinkler $
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

package example.mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

/**
 * Beispiel für ein Mock mit Mockito.
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 3725 $ $Date: 2013-05-24 20:34:57 +0200 (Fr, 24. Mai 2013) $
 */
public class MockitoExampleTest {

    @Test
    public void testExampleMockito() {
        InterfaceForMock ifm = mock(InterfaceForMock.class);
        Worker worker = new Worker(ifm);
        worker.doIt();
        verify(ifm).doSomething();
    }

    private static class Worker {
        private final InterfaceForMock ifm;

        Worker(final InterfaceForMock _ifm) {
            ifm = _ifm;
        }

        public void doIt() {
            ifm.doSomething();
        }
    }

}
