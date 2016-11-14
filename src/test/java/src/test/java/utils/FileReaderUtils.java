package src.test.java.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * Utils class for file management.
 * @author RED
 */
public final class FileReaderUtils {

    /**
     * Reads file content.
     * @param path file name with absolute path included
     * @return file content as String
     */
    public static String readFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br;
        br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }
}
