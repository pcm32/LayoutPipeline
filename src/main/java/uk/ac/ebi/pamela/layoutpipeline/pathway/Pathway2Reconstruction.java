package uk.ac.ebi.pamela.layoutpipeline.pathway;

import com.sri.biospice.warehouse.schema.object.Pathway;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 20/6/13
 * Time: 16:57
 * Describes the functionality of producing a {@link Reconstruction} from a Pathway type T.
 */
public interface Pathway2Reconstruction<T> {


    /**
     * Produces a {@link Reconstruction} that represents the given pathway.
     * @param pathway
     * @return
     */
    public Reconstruction getReconstruction(T pathway);
}
