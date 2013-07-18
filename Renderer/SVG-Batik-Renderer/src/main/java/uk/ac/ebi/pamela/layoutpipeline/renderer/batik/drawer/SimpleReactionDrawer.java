package uk.ac.ebi.pamela.layoutpipeline.renderer.batik.drawer;

import org.apache.log4j.Logger;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.ext.layout.*;
import org.w3c.dom.Element;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.DOMWriter;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 17/7/13
 * Time: 11:32
 * To change this template use File | Settings | File Templates.
 */
public class SimpleReactionDrawer implements ReactionDrawer {

    private static final Logger LOGGER = Logger.getLogger(SimpleReactionDrawer.class);
    final DOMWriter writer;
    final SpeciesReferenceDrawer speciesReferenceDrawer;
    final CurveSegmentDrawer curveSegmentDrawer;
    boolean drawBoundingBox = false;

    @Override
    public boolean isDrawBoundingBox() {
        return drawBoundingBox;
    }

    @Override
    public void setDrawBoundingBox(boolean drawBoundingBox) {
        this.drawBoundingBox = drawBoundingBox;
    }


    /**
     * A reaction writer which draws on a SVG document
     *
     * @param writer the DOM writer that handles the writing to the SVG document
     */
    public SimpleReactionDrawer(DOMWriter writer) {
        this.writer = writer;
        this.curveSegmentDrawer = new CurveSegmentDrawer(this.writer);
        this.speciesReferenceDrawer = new SpeciesReferenceDrawer(this.writer);
    }

    @Override
    public void draw(ReactionGlyph reactionGlyph, Element svgRoot) {
        LOGGER.debug("Drawing reaction " + reactionGlyph.getReaction());

        // Draw the small square
        if (drawBoundingBox && reactionGlyph.getBoundingBox()!=null){

            // Set sqaure attributes
            String[] squareAttributes =new String[]{
                    "x", String.valueOf(reactionGlyph.getBoundingBox().getPosition().getX())
                    ,"y", String.valueOf(reactionGlyph.getBoundingBox().getPosition().getY())
                    ,"width", String.valueOf(reactionGlyph.getBoundingBox().getDimensions().getWidth())
                    ,"height", String.valueOf(reactionGlyph.getBoundingBox().getDimensions().getHeight())
                    ,"fill", "white"
            };

            // Add the ellipse
            this.writer.addDOMElement(svgRoot, "rect", squareAttributes, null);
        }

        drawCurve(svgRoot, reactionGlyph.getCurve());

        drawSpeciesReferences(svgRoot, reactionGlyph);
    }

    /**
     * This method only draws the curve that belong to the reaction itself, but not the curves that directly connect
     * it with the species references.
     *
     * @param svgRoot
     * @param curve
     */
    private void drawCurve(Element svgRoot, Curve curve) {
        for (ICurveSegment segment : curve.getListOfCurveSegments()) {
            curveSegmentDrawer.draw(segment,svgRoot,false,false);
        }
    }

    private void drawSpeciesReferences(Element svgRoot, ReactionGlyph reactionGlyph) {
        Reaction rxn = (Reaction)reactionGlyph.getReactionInstance();
        Boolean reversible = rxn.isSetReversible() && rxn.isReversible();

        for (SpeciesReferenceGlyph speciesReferenceGlyph : reactionGlyph.getListOfSpeciesReferenceGlyphs()) {
            speciesReferenceDrawer.draw(speciesReferenceGlyph,svgRoot,reversible);
        }
    }

}
