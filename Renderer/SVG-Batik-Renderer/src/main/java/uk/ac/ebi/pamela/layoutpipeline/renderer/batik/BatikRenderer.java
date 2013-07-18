package uk.ac.ebi.pamela.layoutpipeline.renderer.batik;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.log4j.Logger;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.layout.*;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import uk.ac.ebi.pamela.layoutpipeline.LayoutRenderer;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.drawer.ReactionDrawer;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.drawer.SimpleReactionDrawer;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.marker.ArrowEndMarker;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.marker.ArrowStartMarker;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.marker.Marker;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Will output an svg file based on an input SBML Document.
 * User: conesa
 * Date: 08/07/2013
 * Time: 16:30
 */
public class BatikRenderer  implements LayoutRenderer {

    private static final Logger LOGGER = Logger.getLogger(BatikRenderer.class);
    private final String ARROW_ID = "arrow";
    private final String SHADOW_ID = "shadow" ;

    private String outputFolder;
    private final String OUTPUT_FILE_NAME = "batikOutput.svg";
    private final String SVG_NS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    private final List<Marker> markers;

    private ReactionDrawer reactionDrawer;
    private DOMWriter writer;

    public BatikRenderer() {
        markers = new ArrayList<Marker>(Arrays.asList(new ArrowEndMarker(), new ArrowStartMarker()));
    }


    public void produceRender(SBMLDocument sbmlWithLayout, String outputFilePrefix) {

        outputFolder = outputFilePrefix;

        // Get the layout information
        Layout layout = getLayout(sbmlWithLayout);

        // Draw the layout
        Document svgDocument = Layout2SVG(layout);

        // Stream SVG (Save it to the output)
        saveSVG(svgDocument);

    }

    private File getOutputFile(){

        return new File (outputFolder + "/" + OUTPUT_FILE_NAME);

    }

    private Layout getLayout(SBMLDocument sbmlWithLayout){


        Model model = sbmlWithLayout.getModel();

        Layout sbmlLayout = new Layout();

        LayoutModelPlugin extendedModel = (LayoutModelPlugin) model.getExtension(LayoutConstants.namespaceURI);
//        ExtendedLayoutModel extendedModel =  (ExtendedLayoutModel) model.getExtension(LayoutConstants.namespaceURI);
        if (extendedModel != null) {
            for (Layout layoutInModel : extendedModel.getListOfLayouts()) {
                // Return the first layout...(There should be only one).
                return layoutInModel;
            }
        }

        return null;
    }

    private void saveSVG(Document svgGenerator) {

        // Finally, stream out SVG to output using
        // UTF-8 encoding.
        boolean useCSS = true; // we want to use CSS style attributes

        // Writer
        Writer out = null;

        try {

            File output = getOutputFile();

            out = new OutputStreamWriter(new FileOutputStream(output), "UTF-8");

            DOMUtilities.writeDocument(svgGenerator,out);

            out.flush();
            out.close();

            LOGGER.info("Svg generated at " + output.getAbsolutePath());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private Document Layout2SVG (Layout layout){

        // Get a DOMImplementation.
        DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();

        Document document = domImpl.createDocument(SVG_NS, "svg", null);

        Dimensions dimensions = layout.getDimensions();

        // Get the root element (the 'svg' element).
        Element svgRoot = document.getDocumentElement();

//        svgRoot.setAttributeNS(null, "width", String.valueOf(dimensions.getWidth()));
//        svgRoot.setAttributeNS(null, "height", String.valueOf(dimensions.getHeight()));

        // Allow it to be resizable in the browser
        // Based on this: https://blueprints.launchpad.net/inkscape/+spec/allow-browser-resizing
        svgRoot.setAttributeNS(null, "viewBox", "0 0 " + String.valueOf(dimensions.getWidth()) + " " +  String.valueOf(dimensions.getHeight()));


        this.writer = new DOMWriter(document);
        this.reactionDrawer = new SimpleReactionDrawer(writer);

        AddDefinitions(document,svgRoot);

        DrawCompartiments(layout, document, svgRoot);

        DrawSpecies(layout, document, svgRoot);

        DrawReactions(layout,document,svgRoot);

        DrawTexts(layout,document,svgRoot);

        return document;

    }

    private void DrawSpecies(Layout layout, Document document, Element svgRoot){

        for (SpeciesGlyph species: layout.getListOfSpeciesGlyphs()){
            DrawSpecie(document, svgRoot, species);
        }


    }
    private void DrawSpecie(Document document, Element svgRoot, SpeciesGlyph speciesGlyph){

        LOGGER.debug("Drawing species " + speciesGlyph.getSpecies());

        // Calculate the center of the ellipse
        String x = String.valueOf(speciesGlyph.getBoundingBox().getPosition().getX() + (speciesGlyph.getBoundingBox().getDimensions().getWidth()/2));
        String y = String.valueOf(speciesGlyph.getBoundingBox().getPosition().getY() + (speciesGlyph.getBoundingBox().getDimensions().getHeight()/2));

        // Set ellipse attributes
        String[] ellipseAttributes =new String[]{
                "cx", x
                ,"cy", y
                ,"rx", String.valueOf(speciesGlyph.getBoundingBox().getDimensions().getWidth()/2)
                ,"ry", String.valueOf(speciesGlyph.getBoundingBox().getDimensions().getHeight()/2)
                ,"fill", "white"
                ,"stroke", "black"
                ,"stroke-width", "1"
                //,"filter", "url(#" + SHADOW_ID + ")"
        };

        // Add the ellipse
        AddDomElement(document,svgRoot,"ellipse", ellipseAttributes, null);

    }

    private void DrawTexts(Layout layout, Document document, Element svgRoot){

        for (TextGlyph textGlyph: layout.getListOfTextGlyphs()){
            DrawText(document, svgRoot, textGlyph);
        }


    }
    private void DrawText(Document document, Element svgRoot, TextGlyph textGlyph){

        LOGGER.debug("Drawing text " + textGlyph.getText());

        // Calculate the center of the ellipse
        String x = String.valueOf(textGlyph.getBoundingBox().getPosition().getX() + (textGlyph.getBoundingBox().getDimensions().getWidth()/2));
        String y = String.valueOf(textGlyph.getBoundingBox().getPosition().getY() + (textGlyph.getBoundingBox().getDimensions().getHeight()/2));

        // Set text attributes
        //<text x="0" y="15" fill="red">I love SVG</text>

        String[] textAttributes =new String[]{
                "x", x
                ,"y", y
                ,"text-anchor", "middle"
                ,"dominant-baseline", "central"
                ,"fill", "black"
                ,"id", textGlyph.getText()

        };

        // Add the text
        AddDomElement(document,svgRoot,"text", textAttributes, textGlyph.getText());


    }


    private void DrawReactions(Layout layout, Document document, Element svgRoot){

        for (ReactionGlyph reactionGlyph: layout.getListOfReactionGlyphs()){
            //DrawReaction(document, svgRoot, reactionGlyph);
            this.reactionDrawer.draw(reactionGlyph,svgRoot);
        }


    }

    private void DrawReaction(Document document, Element svgRoot, ReactionGlyph reactionGlyph){

        LOGGER.debug("Drawing reaction " + reactionGlyph.getReaction());

        // Draw the small square
        if (reactionGlyph.getBoundingBox()!=null){

            // Set sqaure attributes
            String[] squareAttributes =new String[]{
                    "x", String.valueOf(reactionGlyph.getBoundingBox().getPosition().getX())
                    ,"y", String.valueOf(reactionGlyph.getBoundingBox().getPosition().getY())
                    ,"width", String.valueOf(reactionGlyph.getBoundingBox().getDimensions().getWidth())
                    ,"height", String.valueOf(reactionGlyph.getBoundingBox().getDimensions().getHeight())
                    ,"fill", "white"
            };

            // Add the ellipse
            AddDomElement(document,svgRoot,"rect", squareAttributes, null);

        }

        DrawCurve(document, svgRoot, reactionGlyph.getCurve());

        DrawSpeciesReferences(reactionGlyph,document,svgRoot);

    }

    private void DrawSpeciesReferences(ReactionGlyph reactionGlyph, Document document, Element svgRoot){

        for (SpeciesReferenceGlyph speciesReferenceGlyph: reactionGlyph.getListOfSpeciesReferenceGlyphs()){
            DrawSpeciesReference(document, svgRoot, speciesReferenceGlyph);
        }
    }

    private void DrawSpeciesReference(Document document, Element svgRoot, SpeciesReferenceGlyph speciesReferenceGlyph){

        DrawCurve(document, svgRoot, speciesReferenceGlyph.getCurve());

    }

    private void DrawCurve(Document document, Element svgRoot, Curve curve){

        for (ICurveSegment segment : curve.getListOfCurveSegments()){

            DrawSegment(document, svgRoot, segment);

        }


    }

    private void DrawSegment(Document document, Element svgRoot, ICurveSegment segment){


        if (segment instanceof CubicBezier) {
//        } else {

            CubicBezier cubicBezier = (CubicBezier)segment;

            // Trying to achieve this...
            // <path d="M100,250 C100,100 400,100 400,250" />
            String startPoint = "M " + cubicBezier.getStart().getX() + "," + cubicBezier.getStart().getY();
            String basePoint1 =  "C " + cubicBezier.getBasePoint1().getX() + "," + cubicBezier.getBasePoint1().getY();
            String basePoint2 =  cubicBezier.getBasePoint2().getX() + "," + cubicBezier.getBasePoint2().getY();
            String endPoint =  cubicBezier.getEnd().getX() + "," + cubicBezier.getEnd().getY();

            String[] lineAttributes =new String[]{
                    "d", startPoint + " " + basePoint1 + " " + basePoint2 + " " + endPoint
                    ,"style", "stroke:rgb(64,64,64);stroke-width:1;fill:none;marker-end:url(#" + ARROW_ID + ")"

            };

            // Add the ellipse
            AddDomElement(document,svgRoot,"path", lineAttributes, null);

        } else if (segment instanceof LineSegment){
//        if (segment.getBasePoint1() == null){

            LineSegment lineSegment = (LineSegment)segment;

            String[] lineAttributes =new String[]{
                    "x1", String.valueOf(lineSegment.getStart().getX())
                    ,"y1", String.valueOf(lineSegment.getStart().getY())
                    ,"x2", String.valueOf(lineSegment.getEnd().getX())
                    ,"y2", String.valueOf(lineSegment.getEnd().getY())
                    ,"style", "stroke:rgb(64,64,64);stroke-width:1;fill:none"

            };

            // Add the ellipse
            AddDomElement(document,svgRoot,"line", lineAttributes, null);

        // Its a CubicBezier segment
        }
    }


    private void DrawCompartiments(Layout layout, Document document, Element svgRoot){

        for (CompartmentGlyph compartmentGlyph: layout.getListOfCompartmentGlyphs()){
            DrawCompartiment(document, svgRoot, compartmentGlyph);
        }


    }

    private void DrawCompartiment( Document document, Element svgRoot, CompartmentGlyph compartmentGlyph){

        String[] compartimentAttributes = new String[]{
                "x",String.valueOf(compartmentGlyph.getBoundingBox().getPosition().getX())
                ,"y",String.valueOf(compartmentGlyph.getBoundingBox().getPosition().getY())
                ,"width", String.valueOf(compartmentGlyph.getBoundingBox().getDimensions().getWidth())
                ,"height", String.valueOf(compartmentGlyph.getBoundingBox().getDimensions().getHeight())
                ,"rx", String.valueOf(compartmentGlyph.getBoundingBox().getDimensions().getWidth()*0.05)
                ,"ry",String.valueOf(compartmentGlyph.getBoundingBox().getDimensions().getHeight()*0.05)
                ,"style","fill:rgb(200,200,200);stroke:black;stroke-width:2;opacity:0.5"

        };

        AddDomElement(document,svgRoot,"rect",compartimentAttributes);

    }

    private void AddDefinitions(Document document, Element svgRoot){


        // Add a definition section
        Element definition = AddDomElement(document,svgRoot,"defs");

        for (Marker marker : markers) {
            Element arrowMarker = writer.addDomElement(definition,"marker",marker.getAttributes());
            writer.addDomElement(arrowMarker,"path", marker.getMarkerPath());
        }

        //AddArrowDefinition(document, definition);


        AddShadowDefinition(document,definition);



    }

    private void AddShadowDefinition(Document document, Element definition) {
        /*

        <filter id="f1" x="0" y="0" width="200%" height="200%">
            <feOffset result="offOut" in="SourceAlpha" dx="20" dy="20" />
            <feGaussianBlur result="blurOut" in="offOut" stdDeviation="10" />
            <feBlend in="SourceGraphic" in2="blurOut" mode="normal" />
        </filter>

         */


        // Add a filter for the shadow --> <filter id="f1" x="0" y="0" width="200%" height="200%">
        String[] filterAttributes = new String[]{
                "id", SHADOW_ID
                ,"x","0"
                ,"y", "0"
                ,"width", "200%"
                ,"height", "200%"
        };

        // Create the filter element for the shadow
        Element shadowFilter = AddDomElement(document,definition,"filter", filterAttributes);



        // Add a feOffset --> <feOffset result="offOut" in="SourceAlpha" dx="20" dy="20" />
        String[] feOffsetAttributes = new String[]{
                "result", "offOut"
                ,"in", "SourceAlpha"
                ,"dx", "20"
                ,"dy", "20"
        };

        AddDomElement(document,shadowFilter,"feOffset", feOffsetAttributes);

        // Add a feGaussianBlur --> <feGaussianBlur result="blurOut" in="offOut" stdDeviation="10" />
        String[] feGaussianBlurAttributes = new String[]{
                "result", "blurOut"
                ,"in", "offOut"
                ,"stdDeviation", "10"
        };

        AddDomElement(document,shadowFilter,"feGaussianBlur", feGaussianBlurAttributes);


        // Add a feBlend --> <feBlend in="SourceGraphic" in2="blurOut" mode="normal" />
        String[] feBlendAttributes = new String[]{
                "in", "SourceGraphic"
                ,"in2", "blurOut"
                ,"mode", "normal"
        };

        AddDomElement(document,shadowFilter,"feBlend", feGaussianBlurAttributes);

    }

    private void AddArrowDefinition(Document document, Element definition) {
        // Add a marker
        String[] markerAttributes = new String[]{
                "id", ARROW_ID
               ,"orient","auto"
               ,"refX", "0.0"
               ,"refY", "0.0"
               ,"style", "overflow:visible"
        };

        // Create the marker for the arrow
        Element arrowMarker = AddDomElement(document,definition,"marker", markerAttributes);

        // Add a market for the arrows
        String[] arrowPathAttributes = new String[]{
            "d", "M 0.0,0.0 L 5.0,-5.0 L -12.5,0.0 L 5.0,5.0 L 0.0,0.0 z"
            ,"style", "fill-rule:evenodd;stroke:#000000;stroke-width:1.0pt;marker-start:none;"
            ,"transform", "scale(0.4) rotate(180) translate(10,0)"
        };

        AddDomElement(document,arrowMarker,"path", arrowPathAttributes);
    }

    private Element AddDomElement(Document document, Element svgParent, String svgElementName, String[] attributes) {
        return AddDomElement(document, svgParent, svgElementName, attributes, null);
    }

    private Element AddDomElement(Document document, Element svgRoot, String svgElementName) {
        return AddDomElement(document, svgRoot, svgElementName, null,null);
    }

    private Element AddDomElement(Document document, Element svgParent, String svgElementName, String[] attributes, String nodeValue){

        Element element = document.createElementNS(SVG_NS, svgElementName);


        // If there are attributes
        if (attributes != null){

            String attName= null;

            // For each element in the array
            for (String value:attributes){

                // Fill the name if empty and continue
                if (attName== null){
                    attName= value;
                    continue;
                }

                // If code reaches this point we are with the values
                element.setAttributeNS(null, attName, value);

                // Reset attribute name, for the next loop
                attName = null;

            }
        }

        // There is a node value
        if (nodeValue!=null) {

            Text text = document.createTextNode(nodeValue);
            element.appendChild(text);

        }

        svgParent.appendChild(element);

        return element;

    }

}
