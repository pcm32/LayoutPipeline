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
