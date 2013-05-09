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

package uk.ac.ebi.pamela.layoutpipeline.utils;

import static org.junit.Assert.*;
import org.junit.Test;
/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 20/03/2013
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesUtilsTest {

    @Test
    public void testProperties(){

        String user = PropertiesUtil.getProperty("rhea.username");
        assertTrue(PropertiesUtil.getProperty("rhea.username") != null);
        assertTrue(PropertiesUtil.getProperty("rhea.password") != null);
        assertTrue(PropertiesUtil.getProperty("rhea.url") != null);

    }

}
