# This script will generate all the files needed to run the later process (those that genetrate the svg, either based on pathways or in PAMELA depth approach)

OUTPUT=$2
FILE_WITH_IDS=$1
echo "Using list of chebiId in $FILE_WITH_IDS"
echo "Ouputing to $OUTPUT"

echo "Generating files with list of pathways and chebiIds to use in later process.\n"


mvn -e -X exec:java -Dexec.mainClass="uk.ac.ebi.pamela.layoutpipeline.exec.CombinedPathwayDepthPreRunner" -Dexec.args="$FILE_WITH_IDS $OUTPUT"
