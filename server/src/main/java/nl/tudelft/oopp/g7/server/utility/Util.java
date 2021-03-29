package nl.tudelft.oopp.g7.server.utility;

import nl.tudelft.oopp.g7.server.DemoApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Util {

    /*
   Method to export a file from the jar to the plugin folder.
    */
    public static void exportResource(String resourceName, File output) {
        try (InputStream stream = Util.class.getResourceAsStream(resourceName); OutputStream resStreamOut = new FileOutputStream(output)) {
            if (stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            //Fail silently
        }
    }

}
