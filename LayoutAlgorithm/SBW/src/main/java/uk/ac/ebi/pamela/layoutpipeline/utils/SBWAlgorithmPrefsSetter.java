package uk.ac.ebi.pamela.layoutpipeline.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.prefs.BackingStoreException;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 25/6/13
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public class SBWAlgorithmPrefsSetter {

    public enum SBWAlgPrefsField implements MessageableField {

        pathToSaveLayoutEXE("Please enter the path to SBW SaveLayout.exe");

        String msg;

        SBWAlgPrefsField(String msg) {
            this.msg = msg;
        }

        public String getMessage() {
            return msg;
        }

        public String getField() {
            return this.toString();
        }
    }

    public static void main(String[] args) throws IOException, BackingStoreException {
        System.out.println("Reset values [Y/n]:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean reset=reader.readLine().equalsIgnoreCase("y");

        for (SBWAlgPrefsField rheaField : SBWAlgPrefsField.values()) {
            if(!reset && PropertiesUtil.getPreference(rheaField).length()>0) {
                continue;
            }
            System.out.println(rheaField.getMessage());
            String value = reader.readLine();
            PropertiesUtil.setPreference(rheaField, value);
        }

    }
}
