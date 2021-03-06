/*
 * Copyright (C) 2013 EMBL-EBI
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.pamela.layoutpipeline.exec;

import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.pamela.layoutpipeline.SimpleOrgMolQuery;
import uk.ac.ebi.pamela.layoutpipeline.utils.ReactionRecursionDepthMonitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @name PAMELAPipelineExec
 * @date 2013.04.04
 * @version $Rev$ : Last Changed $Date$
 * @author Pablo Moreno <pablacious at users.sf.net>
 * @author $Author$ (this version)
 * @brief ...class description...
 *
 */
public class PAMELAMultiQueryPipelineExec {

    public static void main(String[] args) throws IOException, SQLException {
        String pathToOut = args[0];
        String pathToChemicalIdentifierList = args[1];

        PAMELAPipelineExec exec = new PAMELAPipelineExec(2, pathToOut);
        ReactionRecursionDepthMonitor.getMonitor().setOutputPath(pathToOut);
        BufferedReader reader = new BufferedReader(new FileReader(pathToChemicalIdentifierList));

        String line;
        int count = 0;
        Long startTime = System.currentTimeMillis();
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split("\t");
            ChEBIIdentifier identObj = new ChEBIIdentifier(tokens[0]);
            String organismTaxId = tokens[1];
            exec.setQuery(new SimpleOrgMolQuery(identObj.getAccession(), organismTaxId));
            exec.run();
            count++;
            Float speed = (count + 0f) / ((System.currentTimeMillis() - startTime) / 1000);
            if (count % 10 == 0) {
                System.out.println("Done " + count + " identifiers " + speed + " [idents/sec]");
            }
        }
        Float speed = (count + 0f) / ((System.currentTimeMillis() - startTime) / 1000);
        System.out.println("Done " + count + " identifiers " + speed + " [idents/sec]");
        exec.freeResources();
        ReactionRecursionDepthMonitor.getMonitor().close();
    }
}
