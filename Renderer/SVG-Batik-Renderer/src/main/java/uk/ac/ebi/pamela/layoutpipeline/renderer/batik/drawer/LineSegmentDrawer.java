package uk.ac.ebi.pamela.layoutpipeline.renderer.batik.drawer;

import org.sbml.jsbml.ext.layout.LineSegment;
import org.w3c.dom.Element;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.DOMWriter;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 17/7/13
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */
public class LineSegmentDrawer extends AbstractDrawer {
    public LineSegmentDrawer(DOMWriter writer) {
        super(writer);
    }

    public void draw(LineSegment lineSegment, Element svgRoot, Boolean endArrow, Boolean startArrow) {

        String[] lineAttributes =new String[]{
                "x1", String.valueOf(lineSegment.getStart().getX())
                ,"y1", String.valueOf(lineSegment.getStart().getY())
                ,"x2", String.valueOf(lineSegment.getEnd().getX())
                ,"y2", String.valueOf(lineSegment.getEnd().getY())
                ,"style", "stroke:rgb(64,64,64);stroke-width:1;fill:none;"+getArrowsString(endArrow,startArrow)

        };

        writer.addDomElement(svgRoot,"line",lineAttributes);
    }
}
