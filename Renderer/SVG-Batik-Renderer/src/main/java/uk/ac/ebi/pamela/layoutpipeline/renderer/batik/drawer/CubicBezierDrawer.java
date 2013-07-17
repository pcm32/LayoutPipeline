package uk.ac.ebi.pamela.layoutpipeline.renderer.batik.drawer;

import org.sbml.jsbml.ext.layout.CubicBezier;
import org.w3c.dom.Element;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.DOMWriter;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 17/7/13
 * Time: 12:48
 * To change this template use File | Settings | File Templates.
 */
public class CubicBezierDrawer extends AbstractDrawer {

    protected CubicBezierDrawer(DOMWriter writer) {
        super(writer);
    }

    public void draw(CubicBezier segment, Element svgRoot, Boolean endArrow, Boolean startArrow) {
        CubicBezier cubicBezier = segment;

        // Trying to achieve this...
        // <path d="M100,250 C100,100 400,100 400,250" />
        String startPoint = "M " + cubicBezier.getStart().getX() + "," + cubicBezier.getStart().getY();
        String basePoint1 =  "C " + cubicBezier.getBasePoint1().getX() + "," + cubicBezier.getBasePoint1().getY();
        String basePoint2 =  cubicBezier.getBasePoint2().getX() + "," + cubicBezier.getBasePoint2().getY();
        String endPoint =  cubicBezier.getEnd().getX() + "," + cubicBezier.getEnd().getY();

        String arrowText = getArrowsString(endArrow, startArrow);

        String[] lineAttributes =new String[]{
                "d", startPoint + " " + basePoint1 + " " + basePoint2 + " " + endPoint
                ,"style", "stroke:rgb(64,64,64);stroke-width:1;fill:none;"+arrowText

        };

        // Add the ellipse
        writer.addDomElement(svgRoot, "path", lineAttributes);
    }

}
