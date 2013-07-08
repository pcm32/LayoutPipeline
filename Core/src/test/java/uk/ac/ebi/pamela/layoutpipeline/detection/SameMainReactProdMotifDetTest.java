package uk.ac.ebi.pamela.layoutpipeline.detection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.MetaboliteImpl;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.ReconstructionImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReactionImpl;
import uk.ac.ebi.mdk.domain.identifier.basic.ReconstructionIdentifier;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 9/5/13
 * Time: 13:57
 * To change this template use File | Settings | File Templates.
 */
public class SameMainReactProdMotifDetTest {

    @Test
    public void testNextCompliantGroup() throws Exception {
        Reconstruction recons = new ReconstructionImpl(new ReconstructionIdentifier("test"),"test","test");
        Metabolite mrA = new MetaboliteImpl();
        Metabolite srB = new MetaboliteImpl();
        Metabolite mpC = new MetaboliteImpl();
        Metabolite spD = new MetaboliteImpl();
        Metabolite mpF = new MetaboliteImpl();

        recons.metabolome().add(mrA);
        recons.metabolome().add(srB);
        recons.metabolome().add(mpC);
        recons.metabolome().add(spD);
        recons.metabolome().add(mpF);

        UUID r1uuid = UUID.randomUUID();
        MetabolicReaction rxn1 = new MetabolicReactionImpl(r1uuid);
        rxn1.addReactant(mrA);
        MetabolicParticipant p_srB = new MetabolicParticipantImplementation(srB);
        rxn1.addReactant(p_srB);
        rxn1.addProduct(mpC);
        MetabolicParticipant p_spD = new MetabolicParticipantImplementation(spD);
        rxn1.addProduct(p_spD);

        recons.reactome().add(rxn1);

        UUID r2uuid = UUID.randomUUID();
        MetabolicReaction rxn2 = new MetabolicReactionImpl(r2uuid);
        rxn2.addReactant(mrA);
        rxn2.addProduct(mpC);

        recons.addReaction(rxn2);

        UUID r3uuid = UUID.randomUUID();
        MetabolicReaction rxn3 = new MetabolicReactionImpl(r3uuid);
        rxn3.addReactant(mrA);
        rxn3.addProduct(mpF);

        recons.addReaction(rxn3);


        SameMainReactProdMotifDet detector = new SameMainReactProdMotifDet(recons);

        Set<MetabolicReaction> detected = detector.nextCompliantGroup();
        assertNotNull(detected);
        assertTrue(detected.size()==2);
        boolean foundRxn1 = false;
        boolean foundRxn2 = false;

        for (MetabolicReaction rxn : detected) {
            if(rxn.equals(rxn1))
                foundRxn1 = true;
            else if (rxn.equals(rxn2))
                foundRxn2 = true;
        }
        assertTrue(foundRxn1);
        assertTrue(foundRxn2);

        detected = detector.nextCompliantGroup();
        assertNull(detected);

    }
}
