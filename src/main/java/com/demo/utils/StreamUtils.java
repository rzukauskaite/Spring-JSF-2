package com.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
/**
 * Utils class for stream management. 
 * @author RED
 */
public final class StreamUtils {

    /**
     * Converts {@link InputStream} to {@link String}.
     * @param inputStream instance of {@link InputStream} containing xml content
     * @return xml content as String
     */
    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while (StringUtils.isNotEmpty((line = br.readLine()))) {
            sb.append(line);
        }
        return sb.toString();
    }
}
