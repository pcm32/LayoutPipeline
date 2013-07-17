package uk.ac.ebi.pamela.layoutpipeline.renderer.batik.marker;

/**
 * The only difference with {@link ArrowEndMarker} is that the start arrow is not rotated 180º.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 17/7/13
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
public class ArrowStartMarker extends AbstractArrowMarker implements Marker {

    public ArrowStartMarker() {
        this.marker = Markers.ArrowStart;
    }

    @Override
    public String[] getMarkerPath() {
        return new String[]{
                "d", "M 0.0,0.0 L 5.0,-5.0 L -12.5,0.0 L 5.0,5.0 L 0.0,0.0 z"
                ,"style", "fill-rule:evenodd;stroke:#000000;stroke-width:1.0pt;marker-start:none;"
                ,"transform", "scale(0.4) translate(10,0)"
        };
    }
}
