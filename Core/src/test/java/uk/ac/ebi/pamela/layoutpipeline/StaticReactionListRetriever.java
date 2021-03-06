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

package uk.ac.ebi.pamela.layoutpipeline;

import java.util.*;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.ReconstructionImpl;

/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 27/02/2013
 * Time: 10:20
 * To change this template use File | Settings | File Templates.
 */
public class StaticReactionListRetriever implements ReactionListRetriever {

    List<Reconstruction> recs = new ArrayList<Reconstruction>();

    public List<Reconstruction> getReactionsAsReconstructions(Query query) {

        if (recs.size() ==0) {

            recs.add(getReconstruction("Rec1"));
            recs.add(getReconstruction("Rec2"));
            recs.add(getReconstruction("Rec4"));
        }

        return recs;

    }

    private Reconstruction getReconstruction(String param1){

        Reconstruction rec = new ReconstructionImpl();

        // Fill the reconstruction


        // Return the reconstruction
        return rec;

    }
}
