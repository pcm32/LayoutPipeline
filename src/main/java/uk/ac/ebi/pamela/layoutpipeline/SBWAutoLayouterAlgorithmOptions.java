package uk.ac.ebi.pamela.layoutpipeline;

public class SBWAutoLayouterAlgorithmOptions {
    /**
     * Option class for the command line options
     * <p/>
     * <p/>
     * -f <file to convert>
     * [-o <directory to save to>         ]
     * [-a <degree for auto aliasing>     ]
     * [-w <width     of the image to create>]
     * [-h <height of the image to create>]
     * [-g <double value of gravity factor>]
     * [-l <double value of edge length>]
     * [-nosbml]
     * [-grid]
     * [-magnetism]
     * [-boundary]
     * [-emptySet]
     * [-emptySets]
     * [-r || --removeExisting]
     * [-sourceSink]
     * [--bmp]
     * [--png]
     * [--fullJPG]
     * [--ps]
     * [--svg]
     */

    private String fileToConvert, directoryToSaveTo, degreeOfAutoAliasing;
    private int width, height;
    private double gravityFactor, edgeLenght;
    private boolean noSbml, grid, magnetism, boundary, emptySet, emptySets, removeExisting, sourceSink, bmp, png, fullJPG, ps, svg;

//    public SBWAutoLayouterAlgorithmOptions(String fileToConvert, String directoryToSaveTo, String degreeOfAutoAliasing, int width, int height, double gravityFactor, double edgeLenght, boolean noSbml, boolean grid, boolean magnetism, boolean boundary, boolean emptySet, boolean emptySets, boolean removeExisting, boolean sourceSink, boolean bmp, boolean png, boolean fullJPG, boolean ps, boolean svg) {
//
//        this.fileToConvert = fileToConvert;
//        this.directoryToSaveTo = directoryToSaveTo;
//        this.degreeOfAutoAliasing = degreeOfAutoAliasing;
//        this.width = width;
//        this.height = height;
//        this.gravityFactor = gravityFactor;
//        this.edgeLenght = edgeLenght;
//        this.noSbml = noSbml;
//        this.grid = grid;
//        this.magnetism = magnetism;
//        this.boundary = boundary;
//        this.emptySet = emptySet;
//        this.emptySets = emptySets;
//        this.removeExisting = removeExisting;
//        this.sourceSink = sourceSink;
//        this.bmp = bmp;
//        this.png = png;
//        this.fullJPG = fullJPG;
//        this.ps = ps;
//        this.svg = svg;
//    }
    
    public SBWAutoLayouterAlgorithmOptions() {
    }

    public SBWAutoLayouterAlgorithmOptions(String fileToConvert, String directoryToSaveTo, String degreeOfAutoAliasing, int width, int height, double gravityFactor, double edgeLenght, boolean noSbml, boolean grid, boolean magnetism, boolean boundary, boolean emptySet, boolean emptySets, boolean removeExisting, boolean sourceSink) {

        this.fileToConvert = fileToConvert;
        this.directoryToSaveTo = directoryToSaveTo;
        this.degreeOfAutoAliasing = degreeOfAutoAliasing;
        this.width = width;
        this.height = height;
        this.gravityFactor = gravityFactor;
        this.edgeLenght = edgeLenght;
        this.noSbml = noSbml;
        this.grid = grid;
        this.magnetism = magnetism;
        this.boundary = boundary;
        this.emptySet = emptySet;
        this.emptySets = emptySets;
        this.removeExisting = removeExisting;
        this.sourceSink = sourceSink;

    }
    public String getFileToConvert() {
        return fileToConvert;
    }

    public void setFileToConvert(String fileToConvert) {
        this.fileToConvert = fileToConvert;
    }

    public String getDirectoryToSaveTo() {
        return directoryToSaveTo;
    }

    public void setDirectoryToSaveTo(String directoryToSaveTo) {
        this.directoryToSaveTo = directoryToSaveTo;
    }

    public String getDegreeOfAutoAliasing() {
        return degreeOfAutoAliasing;
    }

    public void setDegreeOfAutoAliasing(String degreeOfAutoAliasing) {
        this.degreeOfAutoAliasing = degreeOfAutoAliasing;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getGravityFactor() {
        return gravityFactor;
    }

    public void setGravityFactor(double gravityFactor) {
        this.gravityFactor = gravityFactor;
    }

    public double getEdgeLenght() {
        return edgeLenght;
    }

    public void setEdgeLenght(double edgeLenght) {
        this.edgeLenght = edgeLenght;
    }

    public boolean isNoSbml() {
        return noSbml;
    }

    public void setNoSbml(boolean noSbml) {
        this.noSbml = noSbml;
    }

    public boolean isGrid() {
        return grid;
    }

    public void setGrid(boolean grid) {
        this.grid = grid;
    }

    public boolean isMagnetism() {
        return magnetism;
    }

    public void setMagnetism(boolean magnetism) {
        this.magnetism = magnetism;
    }

    public boolean isBoundary() {
        return boundary;
    }

    public void setBoundary(boolean boundary) {
        this.boundary = boundary;
    }

    public boolean isEmptySet() {
        return emptySet;
    }

    public void setEmptySet(boolean emptySet) {
        this.emptySet = emptySet;
    }

    public boolean isEmptySets() {
        return emptySets;
    }

    public void setEmptySets(boolean emptySets) {
        this.emptySets = emptySets;
    }

    public boolean isRemoveExisting() {
        return removeExisting;
    }

    public void setRemoveExisting(boolean removeExisting) {
        this.removeExisting = removeExisting;
    }

    public boolean isSourceSink() {
        return sourceSink;
    }

    public void setSourceSink(boolean sourceSink) {
        this.sourceSink = sourceSink;
    }

//    public boolean isBmp() {
//        return bmp;
//    }
//
//    public void setBmp(boolean bmp) {
//        this.bmp = bmp;
//    }
//
//    public boolean isPng() {
//        return png;
//    }
//
//    public void setPng(boolean png) {
//        this.png = png;
//    }
//
//    public boolean isFullJPG() {
//        return fullJPG;
//    }
//
//    public void setFullJPG(boolean fullJPG) {
//        this.fullJPG = fullJPG;
//    }
//
//    public boolean isPs() {
//        return ps;
//    }
//
//    public void setPs(boolean ps) {
//        this.ps = ps;
//    }
//
//    public boolean isSvg() {
//        return svg;
//    }
//
//    public void setSvg(boolean svg) {
//        this.svg = svg;
//    }

    public SBWAutoLayouterAlgorithmOptions(String fileToConvert, String directoryToSaveTo, int width, int height) {

        this.fileToConvert = fileToConvert;
        this.directoryToSaveTo = directoryToSaveTo;
        this.width = width;
        this.height = height;
    }

    public SBWAutoLayouterAlgorithmOptions(String fileToConvert) {

        this.fileToConvert = fileToConvert;
    }

    public String toString() {


        // Compose the command line, parameters section
        String result = "";

        result = result + option2String("-f", fileToConvert);
        result = result + option2String("-o", directoryToSaveTo);
        result = result + option2String("-a", degreeOfAutoAliasing);
        result = result + option2String("-w", width);
        result = result + option2String("-h", height);

        result = result + option2String("-g", gravityFactor);
        result = result + option2String("-l", edgeLenght);

        result = result + option2String("-nosbml", noSbml);
        result = result + option2String("-grid", grid);
        result = result + option2String("-magnetism", magnetism);
        result = result + option2String("-boundary", boundary);
        result = result + option2String("-emptySet", emptySet);
        result = result + option2String("-emptySets", emptySets);
        result = result + option2String("-r", removeExisting);
        result = result + option2String("-sourceSink", sourceSink);
// Cancel different output options
//        result = result + option2String("--bmp", bmp);
//        result = result + option2String("--png", png);
//        result = result + option2String("--fullJPG", fullJPG);
//        result = result + option2String("--ps", ps);
//        result = result + option2String("--svg", svg);
        return result;


    }

    private String option2String(String optionPrefix, String value) {

        if (!(value == null || value.equals("null") || value.equals(""))) {

            return " " + optionPrefix + " " + value;
        }

        return "";
    }

    private String option2String(String optionPrefix, int value) {

        if (value != 0) {

            return " " +  optionPrefix + " " + value;
        }

        return "";
    }

    private String option2String(String optionPrefix, double value) {

        if (value != 0) {

            return " " + optionPrefix + " " + value;
        }

        return "";
    }

    private String option2String(String optionPrefix, boolean value) {

        if (value) {

            return " " + optionPrefix ;
        }

        return "";
    }

}