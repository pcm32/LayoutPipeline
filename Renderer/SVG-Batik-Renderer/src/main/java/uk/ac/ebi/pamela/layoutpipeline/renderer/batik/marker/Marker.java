package uk.ac.ebi.pamela.layoutpipeline.renderer.batik.marker;

/**
 * Main functionality description for the representation of SVG markers, that are normally added at the beginning
 * of the SVG file and are referenced from elements in the document. This can be used for any shape that is recurrently
 * invoked within the document, saving space.
 *
 */
public interface Marker {

    /**
     * Produces the array of svg attributes for the marker.
     *
     * @return attributes array
     */
    String[] getAttributes();

    /**
     * Produces the array of svg attributes for the path of the marker.
     *
     * @return attributes for the path of the marker.
     */
    String[] getMarkerPath();

    Markers getMarkerType();
}
