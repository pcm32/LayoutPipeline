package uk.ac.ebi.pamela.layoutpipeline.renderer.batik.drawer;

import org.sbml.jsbml.ext.layout.*;
import org.w3c.dom.Element;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.DOMWriter;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 17/7/13
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public class SpeciesReferenceDrawer extends AbstractDrawer{

    private List<SpeciesReferenceRole> substrateRoles;
    private List<SpeciesReferenceRole> productRoles;
    private CurveSegmentDrawer curveSegmentDrawer;

    public SpeciesReferenceDrawer(DOMWriter writer) {
        super(writer);
        substrateRoles = Arrays.asList(SpeciesReferenceRole.SIDESUBSTRATE, SpeciesReferenceRole.SUBSTRATE);
        productRoles = Arrays.asList(SpeciesReferenceRole.PRODUCT, SpeciesReferenceRole.SIDEPRODUCT);
        curveSegmentDrawer = new CurveSegmentDrawer(this.writer);
    }

    public void draw(SpeciesReferenceGlyph speciesReferenceGlyph, Element svgRoot, Boolean reversible) {
        Curve curve = speciesReferenceGlyph.getCurve();
        SpeciesReferenceRole role = speciesReferenceGlyph.getSpeciesReferenceRole();

        Boolean startArrow = false;
        Boolean endArrow = false;
        if(substrateRoles.contains(role)) {
            startArrow = reversible;
            endArrow = false;
        } else if(productRoles.contains(role)) {
            startArrow = false;
            endArrow = true;
        }

        for (ICurveSegment curveSegment : curve.getListOfCurveSegments()) {
            curveSegmentDrawer.draw(curveSegment,svgRoot,endArrow,startArrow);
        }
    }
}
