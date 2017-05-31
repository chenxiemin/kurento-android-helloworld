package cxm.example.kurento.util;

/**
 * Created by cxm on 3/3/17.
 */
public class Strings {
    public static boolean isEmpty(String str) {
        return null == str || str.length() == 0;
    }

    public static boolean compare(String a, String b) {
        if (null == a && null == b) {
            return true;
        } else {
            if (null == a || null == b)
                return false;
            else
                return a.equals(b);
        }
    }

    /**
     * Normalize string array,
     * Appends zeros if string from the array
     * has length smaller than the maxLen.
     **/
    private static String normalize(String[] split, int maxLen){
        StringBuilder sb = new StringBuilder("");
        for(String s : split) {
            for(int i = 0; i<maxLen-s.length(); i++) sb.append('0');
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Removes trailing zeros of the form '.00.0...00'
     * (and does not remove zeros from, say, '4.1.100')
     **/
    public static String removeTrailingZeros(String s){
        int i = s.length()-1;
        int k = s.length()-1;
        while(i >= 0 && (s.charAt(i) == '.' || s.charAt(i) == '0')){
            if(s.charAt(i) == '.') k = i-1;
            i--;
        }
        return s.substring(0,k+1);
    }

    /**
     * Compares two versions(works for alphabets too),
     * Returns 1 if v1 > v2, returns 0 if v1 == v2,
     * and returns -1 if v1 < v2.
     **/
    public static int compareVersion(String v1, String v2) {

        // Uncomment below two lines if for you, say, 4.1.0 is equal to 4.1
        // v1 = removeTrailingZeros(v1);
        // v2 = removeTrailingZeros(v2);

        String[] splitv1 = v1.split("\\.");
        String[] splitv2 = v2.split("\\.");
        int maxLen = 0;
        for(String str : splitv1) maxLen = Math.max(maxLen, str.length());
        for(String str : splitv2) maxLen = Math.max(maxLen, str.length());
        int cmp = normalize(splitv1, maxLen).compareTo(normalize(splitv2, maxLen));
        return cmp > 0 ? 1 : (cmp < 0 ? -1 : 0);
    }
}
