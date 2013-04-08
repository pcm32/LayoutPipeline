package uk.ac.ebi.pamela.layoutpipeline;

public class SBWRendererOptions {
    /**
     * Option class for the command line options
     * Executed example
     mono SBMLLayoutReader
     -f || --file <sbml file with layout>
     -o || --out <output filename>
     -s || --scale <scalefactor>
     -d || --dimensions <xdimensions> <ydimensions>

     If you don’t enter input and output file, the SBW module will be started.
     If the output filename ends in svg, an svg will be exported using the stylesheet.
     Otherwise an image will be saved (I recommend to use .png as extension for a lossless image)
     in the default size. That is equivalent to saving it with -s 1.
     Of course you are free to enter any float scaling factor.
     Instead of scale you can also enter the image dimensions that you would like to have.
     */


    private String fileToConvert, directoryToSaveTo;
    private double scaleFactor, xDimension, yDimension;
    private Format outputFormat = Format.png;

    public void setOutputFormat(Format format) {
        this.outputFormat = format;
    }

    public enum Format {
        svg,png;
    }

    public SBWRendererOptions() {
    }
    
    public SBWRendererOptions(String fileToConvert, String directoryToSaveTo, double scaleFactor) {

        this.fileToConvert = fileToConvert;
        this.directoryToSaveTo = directoryToSaveTo;
        this.scaleFactor = scaleFactor;

    }
    public String getFileToConvert() {
        return fileToConvert;
    }

    public void setFileToConvert(String fileToConvert) {
        this.fileToConvert = fileToConvert;
    }

    public String getDirectoryToSaveTo() {
        return directoryToSaveTo+"."+this.outputFormat.toString();
    }

    public void setDirectoryToSaveTo(String directoryToSaveTo) {
        this.directoryToSaveTo = directoryToSaveTo;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }


    public SBWRendererOptions(String fileToConvert, String directoryToSaveTo, double xDimension, double yDimension) {

        this.fileToConvert = fileToConvert;
        this.directoryToSaveTo = directoryToSaveTo;
        this.xDimension = xDimension;
        this.yDimension = yDimension;
    }

    public SBWRendererOptions(String fileToConvert) {

        this.fileToConvert = fileToConvert;
    }

    public String toString() {


        // Compose the command line, parameters section
        String result = "";

        result = result + option2String("--file", fileToConvert);
        result = result + option2String("--out", getDirectoryToSaveTo());
        if (scaleFactor!=0) result = result + option2String("--scale", scaleFactor);
        if (xDimension != 0) result = result + option2String("--dimensions", xDimension + " " + yDimension);
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