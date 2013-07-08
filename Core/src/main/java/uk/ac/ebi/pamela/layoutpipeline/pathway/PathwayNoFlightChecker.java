package uk.ac.ebi.pamela.layoutpipeline.pathway;

/**
 * Describes the functionality to veto pathways from being used for layout purposes (due to size or personal likes).
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 21/6/13
 * Time: 14:18
 */
public interface PathwayNoFlightChecker<T> {

    /**
     * Given a pathway, it returns true if the pathway should not be used for layout (given a criteria set in the
     * implementation).
     *
     * @param pathway
     * @return true if the pathway should not be used for layouting purposes.
     */
    public boolean checkNoFlight(T pathway);
}
