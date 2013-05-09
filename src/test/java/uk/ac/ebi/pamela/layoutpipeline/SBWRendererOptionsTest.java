/*
 * Copyright (C) 2013 EMBL-EBI
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.pamela.layoutpipeline;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 27/02/2013
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class SBWRendererOptionsTest {
    @Test
    public void testOptionFileConstructor() throws Exception {

        SBWRendererOptions options = new SBWRendererOptions("file");

        assertEquals("Test options for only file constructor", " --f file", options.toString());

    }

    @Test
    public void testOptionFileOutputAndDimensionsDirectoryConstructor() throws Exception {

        SBWRendererOptions options = new SBWRendererOptions("file", "output",10,20);

        assertEquals("Test options for constructor with dimensions", " --f file --out output --dimensions 10.0 20.0", options.toString());

    }


}
