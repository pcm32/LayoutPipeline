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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Created with IntelliJ IDEA. User: conesa Date: 20/03/2013 Time: 11:30 Singleton class for serving properties..
 */
public class PropertiesUtil {

    static Properties props = null;
    static Preferences prefs;
    private static final Logger LOGGER = Logger.getLogger(PropertiesUtil.class);

    public enum PrefNames {

        pathToRendererEXE("Please enter the path to SBW SBML LayoutReader.exe"),
        pathToSaveLayoutEXE("Please enter the path to SBW SaveLayout.exe");
        
        String msg;

        PrefNames(String msg) {
            this.msg = msg;
        }

        String getMessage() {
            return msg;
        }
    }

    private PropertiesUtil() {
    }

    static public String getProperty(String propertyName) {

        // If there aren't any properties...load them
        if (props == null) {
            readProperties();
        }
        return props.getProperty(propertyName);
    }
    
    static public String getPreference(String prefName, String defaultValue) {
        // If there isn't any preference...load them
        if (prefs == null) {
            readPreferences();
        }
        return prefs.get(prefName, defaultValue);
    }
    
    static public String getPreference(RheaDBConnectionSetter.RheaDBField field) {
        return getPreference(field.toString(), "");
    }

    static public String getPreference(PrefNames prefName, String defaultValue) {

        return getPreference(prefName.toString(), defaultValue);
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

    static private void readPreferences() {
        prefs = Preferences.userRoot().node(PropertiesUtil.class.getName());
    }

    static public void setPreference(String preferenceName, String value) throws IOException, BackingStoreException {
        prefs = Preferences.userRoot().node(PropertiesUtil.class.getName());
        prefs.put(preferenceName, value);
        prefs.flush();
    }

    public static void main(String[] args) throws IOException, BackingStoreException {

        for (PrefNames prefNames : PrefNames.values()) {
            System.out.println(prefNames.getMessage());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String saveLayoutPath = reader.readLine();
            setPreference(prefNames.toString(), saveLayoutPath);
        }
    }
}
