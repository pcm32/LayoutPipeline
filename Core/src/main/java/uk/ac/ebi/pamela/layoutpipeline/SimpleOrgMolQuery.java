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

/**
 * SimpleOrgMolQuery.java
 *
 * 2013.02.25
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.pamela.layoutpipeline;


import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.type.ChemicalIdentifier;

/**
 * @name    SimpleOrgMolQuery
 * @date    2013.02.25
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class SimpleOrgMolQuery implements Query {

    private static final Logger LOGGER = Logger.getLogger( SimpleOrgMolQuery.class );
    private Taxonomy organismIdentifier;
    private ChemicalIdentifier chemicalIdentifier;
    
    /**
     * 
     * @param chebiID should begin with ChEBI:
     * @param orgTaxID the taxonomy identifier for the species.
     */
    public SimpleOrgMolQuery(String chebiID, String orgTaxID) {
        this.organismIdentifier = new Taxonomy();
        if (!orgTaxID.equals(""))  this.organismIdentifier.setAccession(orgTaxID);
        this.chemicalIdentifier = new ChEBIIdentifier(chebiID);
    }

    public Taxonomy getOrganismIdentifier() {
        return this.organismIdentifier;
    }

    public ChemicalIdentifier getChemicalIdentifier() {
        return this.chemicalIdentifier;
    }


}
