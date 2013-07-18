package uk.ac.ebi.pamela.layoutpipeline.renderer.batik.drawer;

import org.sbml.jsbml.ext.layout.CubicBezier;
import org.sbml.jsbml.ext.layout.ICurveSegment;
import org.sbml.jsbml.ext.layout.LineSegment;
import org.w3c.dom.Element;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.DOMWriter;

public class CurveSegmentDrawer {

    final CubicBezierDrawer cubicBezierDrawer;
    final LineSegmentDrawer lineSegmentDrawer;

    public CurveSegmentDrawer(DOMWriter writer) {
        this.cubicBezierDrawer = new CubicBezierDrawer(writer);
        this.lineSegmentDrawer = new LineSegmentDrawer(writer);
    }


    public void draw(ICurveSegment segment, Element svgRoot, Boolean endArrow, Boolean startArrow) {
        if (segment instanceof CubicBezier) {
            cubicBezierDrawer.draw((CubicBezier) segment, svgRoot, endArrow,startArrow);
        } else if(segment instanceof LineSegment) {
            lineSegmentDrawer.draw((LineSegment) segment, svgRoot, endArrow,startArrow);
        }
    }
}