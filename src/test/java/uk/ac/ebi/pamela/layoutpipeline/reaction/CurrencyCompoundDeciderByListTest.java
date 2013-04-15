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

package uk.ac.ebi.pamela.layoutpipeline.reaction;

import static org.junit.Assert.*;
import org.junit.Test;
import uk.ac.ebi.rhea.domain.Compound;
import java.util.Collection;

/**
 * User: conesa
 * Date: 25/03/2013
 * Time: 14:38
 */
public class CurrencyCompoundDeciderByListTest {

    @Test
    public void getCurrencyMetabolitesTestEmptyList(){

        CurrencyCompoundDeciderByList currDecider = new CurrencyCompoundDeciderByList(null);

        Collection<Compound> compounds = currDecider.getCurrencyMetabolites(null);


        // It has to be null
        assertNull("returned currency metabolites must be null", compounds);

    }

}
