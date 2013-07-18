package uk.ac.ebi.pamela.layoutpipeline.renderer.batik;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 17/7/13
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */
public class DOMWriter {

    private static final String SVG_NS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    private Document document;

    public DOMWriter(Document document) {
        this.document = document;
    }

    public Element addDomElement(Element svgParent, String svgElementName, String[] attributes) {
        return addDOMElement(svgParent, svgElementName, attributes, null);
    }

    public Element addDomElement(Element svgRoot, String svgElementName) {
        return addDOMElement(svgRoot, svgElementName, null, null);
    }

    public Element addDOMElement(Element svgParent, String svgElementName, String[] attributes, String nodeValue){

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
