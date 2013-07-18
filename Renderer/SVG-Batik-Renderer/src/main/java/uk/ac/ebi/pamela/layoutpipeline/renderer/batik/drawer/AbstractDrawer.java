package uk.ac.ebi.pamela.layoutpipeline.renderer.batik.drawer;

import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.DOMWriter;import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.marker.Markers; /**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 17/7/13
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public class AbstractDrawer {

    DOMWDOMWriter er;

    public AbstractDrawer(DOMWriter writer) {
        this.writer = writer;
    }

    String getArrowsString(Boolean endArrow, Boolean startArrow) {
        String startArrowTxt = startArrow ? "marker-start:url(#"+ MarkMarkersowStart.toString() + ");" : "";
        String endArrowTxt = endArrow ? "marker-end:url(#" + Markers.ArrowEnd.toString() +");" : "";
        return startArrowTxt + endArrowTxt;
    }
}
