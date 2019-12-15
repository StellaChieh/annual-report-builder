package cwb.cmt.surface.utils;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;



public class ConvertFileName {
	
	public static String encodeFilename(String s) {
        if (s == null) {
            //throw new NullPointerException("The input string is null. "
            //        + "Please specify an acceptable one.");
            return null;
        }
        return encodeFilename(s, new char[] {'<', '>', ':', '\"', '\\', '/', '|', '?', '*'});
    }
    
    public static  String encodeFilename(String s, char[] forbidden) {
        if (s == null) {
            //throw new NullPointerException("The input string is null. "
            //        + "Please specify an acceptable one.");
            return null;
        }
        if (forbidden == null) {
            throw new NullPointerException("The input string forbidden is null. "
                    + "Please specify an acceptable one.");
        }
        int[] intForbiddens = new int[forbidden.length];
        IntStream.rangeClosed(1, forbidden.length).forEach(i -> intForbiddens[i-1] = (int) forbidden[i-1]);
        StringBuilder output = new StringBuilder();        
        for (int i=0; i<s.length(); i++) {
            final char ch = s.charAt(i);
            if (ch < 32 || Arrays.stream(intForbiddens).anyMatch(c -> c == ch)) {
                output.append("A+" + String.format("%02X", (int) ch));
            }
            else {
                output.append(ch);
            }
        }        
        return output.toString();
    }
    
    public static String decodeFilename(String s) {
        if (s == null) {
            //throw new NullPointerException("The input string is null. "
            //        + "Please specify an acceptable one.");
            return null;
        }
        final StringBuilder output = new StringBuilder();        
        final int length = s.length();
        for (int i=0, dist=s.indexOf("A+"); i<length; i+=dist, dist=((dist=s.indexOf("A+", i)) != -1)? dist-i : length-i) {
            if (dist == -1 ||
                i+dist+2+2 > length ||
                s.substring(i+dist+2, i+dist+2+2).chars().anyMatch(c -> (c < '0' || c > '9') && (c < 'a' || c > 'f') && (c < 'A' || c > 'F'))) {
                dist = length-i;
                output.append(s.substring(i, i+dist));
            }
            else {
                // i ~ i+dist
                output.append(s.substring(i, i+dist));
                
                // i+dist+2 ("+A") ~ i+dist+6
                int ascii = Integer.parseInt(s.substring(i+dist+2, i+dist+2+2), 16);
                output.append((char) ascii);
                dist += 2+2;
            }
        }
        return output.toString();
    }
}
