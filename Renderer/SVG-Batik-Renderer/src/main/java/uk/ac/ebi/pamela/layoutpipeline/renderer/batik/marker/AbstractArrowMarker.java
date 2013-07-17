package uk.ac.ebi.pamela.layoutpipeline.renderer.batik.marker;

/**
 * Attributes and path for a regular Arrow.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 17/7/13
 * Time: 23:03
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractArrowMarker {
    Markers marker;

    /**
     * {@inheritDoc}
     */
    public String[] getAttributes() {
        return new String[]{
                "id", marker.toString()
                ,"orient","auto"
                ,"refX", "0.0"
                ,"refY", "0.0"
                ,"style", "overflow:visible"
        };
    }

    /**
     * {@inheritDoc}
     */
    public String[] getMarkerPath() {
        return new String[]{
                "d", "M 0.0,0.0 L 5.0,-5.0 L -12.5,0.0 L 5.0,5.0 L 0.0,0.0 z"
                ,"style", "fill-rule:evenodd;stroke:#000000;stroke-width:1.0pt;marker-start:none;"
                ,"transform", "scale(0.4) rotate(180) translate(10,0)"
        };
    }

    public Markers getMarkerType() {
        return marker;
    }

}
