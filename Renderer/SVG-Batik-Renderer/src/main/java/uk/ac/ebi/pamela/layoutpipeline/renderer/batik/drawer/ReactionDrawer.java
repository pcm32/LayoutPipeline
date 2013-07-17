package uk.ac.ebi.pamela.layoutpipeline.renderer.batik.drawer;

import org.sbml.jsbml.ext.layout.ReactionGlyph;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 17/7/13
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */
public interface ReactionDrawer {

    public void draw(ReactionGlyph glyph, Element svgElement);

    boolean isDrawBoundingBox();

    void setDrawBoundingBox(boolean drawBoundingBox);
}
