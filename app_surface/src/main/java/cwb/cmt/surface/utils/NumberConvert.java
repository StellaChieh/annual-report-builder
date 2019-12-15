package cwb.cmt.surface.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class NumberConvert {
    /**
     * Rounding mode to round towards positive infinity.  If the
     * {@code BigDecimal} is positive, behaves as for
     * {@code ROUND_UP}; if negative, behaves as for
     * {@code ROUND_DOWN}.  Note that this rounding mode never
     * decreases the calculated value.
     */
    public final static int ROUND_CEILING =      2;
    
    
	//Chinese characters of digits
    private static final String[] chineseNumericChars = new String[] {
            "〇", "一", "二", "三", "四",
            "五", "六", "七", "八", "九"
    };
    
    
    //Chinese characters of the unit of ten.
    private static final String[] chineseNumericChars2 = new String[] {
            "", "十", "百", "千", "萬"
    };
    
    
    //Converts number to Chinese representation
    public static final String digit2ChineseString(int n) {
        String result = "";
        for (int p=0, r=0, q=n; p<=5 && q>0; p++) {
            r = q % 10;
            q = q / 10;
            result = ((p==1 && q==0 && r<=1)
		    		  ? "" : chineseNumericChars[r])  +
            		 ((r == 0)
            		  ? "" : chineseNumericChars2[p]) + result;
            if (q == 0) {
                break;
            }
        }
        
        if (result.length() > 1 && result.endsWith("〇")) { 
            result = result.substring(0, result.length()-1);
        }
        return result;
    }
    
    /**
     * Examines if the string argument represents a number
     *
     * @param  s the string to be examined.
     * @return if the string argument represents a number
     * @throws NullPointerException if the string is null
     */
    public static boolean isNumeric(String s) {
        return isNumeric(s, false);
    }
    
    public static boolean isNumeric(String s, boolean throwsException) { //TODO: flag parameter
        if (s == null)
            throw new NullPointerException("input string is null.");
        
        try {
            Double.parseDouble(s);
        }
        catch (NumberFormatException e) {
            if (throwsException)
                throw e;
            return false;
        }
        return true;
    }
    
    /**
     * Check if a class is a wrapper class of any primitive type.
     *
     * @param  cls the class to check.
     * @return if cls is a wrapper class of any primitive type.
     * @throws IllegalArgumentException if the argument cls is null.
     */
    public static boolean isWrapperClass(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Invalid Class object. Please give one that is not null");
        }
        else {
            return Byte.class == cls    || Short.class == cls || Integer.class == cls || Long.class == cls ||
                   Float.class == cls   || Float.class == cls ||
                   Boolean.class == cls || Character.class == cls;
        }
    }
    
    public static String[] expand2Strings(Object...args) {
        return expand2Strings("", false, args);
    }

    public static String[] expand2StringsExcludingNull(Object...args) {
        return expand2Strings(null, true, args);
    }   
    
    private static String[] expand2Strings(String defValue, boolean excludesNull, Object...args) {
        if (args == null)
            return new String[] {};
        
        final ArrayList<String> list = new ArrayList<String>();
        for (Object obj : args) {
            if (obj == null) {
                if (!excludesNull)
                    list.add(defValue);
            }
            else if (!obj.getClass().isArray()) {
                if (obj.getClass().isPrimitive() || isWrapperClass(obj.getClass()))
                    list.add(String.valueOf(obj));
                else
                    list.add(obj.toString());
            }
            else {
                String[] a = expand2Strings(defValue, excludesNull, (Object[]) obj);
                for (String s : a) {
                    list.add(s);
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }
    
    public static String insertSpaceWithinCharacters(String str, String within) {
        final StringBuffer sb = new StringBuffer();
        final char[] chars = str.toCharArray();
        for (int i=0; i<chars.length; i++) {
            sb.append(chars[i]);
            if (i<chars.length-1)
                sb.append(within);
        }
        return sb.toString();
    }
    //
    // String and character generation
    //
    
    public static String repeatChar(char c, int n) {
        char[] a = new char[n];
        Arrays.fill(a, c);
        return new String(a);
    }
    
    public static String repeatString(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
    
    public static float round(float value, int scale, int mode) {
        return new BigDecimal(value).setScale(scale, mode).floatValue();
    }
	
}
