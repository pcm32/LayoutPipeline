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

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 20/03/2013
 * Time: 11:30
 * Singleton class for serving properties..
 */
public class PropertiesUtil {

    static Properties props = null;
    private static final Logger LOGGER = Logger.getLogger( PropertiesUtil.class );

    private PropertiesUtil(){}

    static public String getProperty(String propertyName){

        // If there aren't any properties...load them
        if (props == null)  readProperties();
        return props.getProperty(propertyName);
    }

    static private void readProperties() {

        InputStream fileIn = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");

        props = new Properties();

        try {

            props.load(fileIn);

            LOGGER.info("application.properties file loaded.");

        } catch (IOException e) {
            LOGGER.error("Can't load application.properties file. There must be one in the resources folder.");
            props = null;
        }

    }

}
