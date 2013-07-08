package uk.ac.ebi.pamela.layoutpipeline.detection.autorxn;

import org.junit.Test;
import static org.junit.Assert.*;
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

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 9/5/13
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
public class AutoRxnMotifDetectorTest {
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

        MetabolicReaction rxn1 = new MetabolicReactionImpl();
        rxn1.addReactant(mrA);
        MetabolicParticipant p_srB = new MetabolicParticipantImplementation(srB);
        rxn1.addReactant(p_srB);
        rxn1.addProduct(mpC);


        recons.reactome().add(rxn1);

        MetabolicReaction rxn2 = new MetabolicReactionImpl();
        rxn2.addReactant(mrA);
        rxn2.addProduct(mpC);

        recons.reactome().add(rxn2);

        MetabolicReaction rxn3 = new MetabolicReactionImpl();
        rxn3.addReactant(mrA);
        MetabolicParticipant p_spD = new MetabolicParticipantImplementation(spD);
        rxn3.addProduct(p_spD);
        rxn3.addProduct(mrA);

        recons.reactome().add(rxn3);

        AutoRxnMotifDetector detector = new AutoRxnMotifDetector(recons);

        Set<MetabolicReaction> detected = detector.nextCompliantGroup();
        assertNotNull(detected);
        assertTrue(detected.size()==1);
        assertTrue(detected.contains(rxn3));

        detected = detector.nextCompliantGroup();
        assertNull(detected);
    }
}
