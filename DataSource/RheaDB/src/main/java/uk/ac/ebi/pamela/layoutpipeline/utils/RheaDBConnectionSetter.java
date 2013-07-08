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

package uk.ac.ebi.pamela.layoutpipeline.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.prefs.BackingStoreException;

/**
 * @name    RheaDBConnectionSetter
 * @date    2013.04.16
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class RheaDBConnectionSetter {
    
    public enum RheaDBField implements MessageableField {
        rheaUsername("rhea.username","Enter the Rhea user name:"), 
        rheaPassword("rhea.password","Enter the Rhea password"), 
        rheUrl("rhea.url", "Enter the Rhea URL:"),
        rheaSchema("rhea.schema","Enter the Rhea Schema:");

        String field;
        String msg;
        
        private RheaDBField(String field, String msg) {
            this.field = field;
            this.msg = msg;
        }

        @Override
        public String toString() {
            return field;
        }
        
        public String getMessage() {
            return msg;
        }

        @Override
        public String getField() {
            return field;
        }

    }

    public static void main(String[] args) throws IOException, BackingStoreException {
        System.out.println("Reset values [Y/n]:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean reset=reader.readLine().equalsIgnoreCase("y");
        
        for (RheaDBField rheaField : RheaDBField.values()) {
            if(!reset && PropertiesUtil.getPreference(rheaField).length()>0) {
                continue;
            }
            System.out.println(rheaField.getMessage());
            String value = reader.readLine();
            PropertiesUtil.setPreference(rheaField, value);
        }
        
    }

}
