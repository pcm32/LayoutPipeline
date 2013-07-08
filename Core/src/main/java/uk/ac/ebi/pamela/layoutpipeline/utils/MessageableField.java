package uk.ac.ebi.pamela.layoutpipeline.utils;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 25/6/13
 * Time: 21:45
 *
 * Describes the functionality of a field that can be accompanied by a message, for setting purposes.
 */
public interface MessageableField {

    public String getMessage();
    public String getField();
}
