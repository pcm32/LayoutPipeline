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

package uk.ac.ebi.pamela.layoutpipeline.reaction;

import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.object.Chemical;
import com.sri.biospice.warehouse.schema.object.Reaction;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.BiochemicalReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.BioWarehouseChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.metabolomes.biowh.BioChemicalReactionSetProviderFactory;
import uk.ac.ebi.metabolomes.biowh.BiochemicalReactionSetProvider;
import uk.ac.ebi.warehouse.util.ReactionUtil;

/**
 * @name    PAMELARecursiveReactionGetter
 * @date    2013.03.21
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class PAMELARecursiveReactionGetter extends AbstractRecursiveReactionGetter<Chemical, Reaction>{
    
    private BiochemicalReactionSetProvider provider;
    
    public PAMELARecursiveReactionGetter(DataSet ds, Taxonomy orgIdentifier,Integer depth,
                                         CurrencyCompoundDecider<Chemical,Reaction> currencyDecider,
                                         MainCompoundDecider<Chemical,Reaction> mainCompDecider) {
        super(depth,currencyDecider,mainCompDecider);
        this.provider = BioChemicalReactionSetProviderFactory.getBiochemicalReactionSetProvider(ds);
        this.provider.setSpecieForProvider(orgIdentifier.getTaxon());
    }

    @Override
    Collection<Reaction> getReactionsForChemical(Chemical chem) {
        return ReactionUtil.getBWHReactionsForChemical(chem);
    }

    @Override
    MetabolicReaction convertReaction(Reaction rxn) {
        try {
            BiochemicalReaction biochemrxn = provider.getBioChemicalRXNFromRxn(rxn);
            Collection<Chemical> currencies = getCurrencyDecider().getCurrencyMetabolites(rxn);
            Collection<BioWarehouseChemicalIdentifier> currenciesIdents = getCurrencyBWHIdents(currencies);
            for (MetabolicParticipant part : biochemrxn.getParticipants()) {
                Metabolite m = part.getMolecule();
                if(currenciesIdents.contains(m.getIdentifier())) {
                    part.setSideCompound(Boolean.TRUE);
                }
            }
            return biochemrxn;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        
    }

    private Collection<BioWarehouseChemicalIdentifier> getCurrencyBWHIdents(Collection<Chemical> currencies) {
        Collection<BioWarehouseChemicalIdentifier> idents = new LinkedList<BioWarehouseChemicalIdentifier>();
        Iterator<Chemical> it = currencies.iterator();
        while(it.hasNext()) {
            Chemical chem = it.next();
            BioWarehouseChemicalIdentifier ident = new BioWarehouseChemicalIdentifier(chem.getWID(), chem.getDataSetWID());
            idents.add(ident);
        }
        return idents;
    }


}
