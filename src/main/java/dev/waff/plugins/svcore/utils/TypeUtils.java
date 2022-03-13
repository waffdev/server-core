package dev.waff.plugins.svcore.utils;

/*
 purpose: hold utilities for manipulating data types
 */
public class TypeUtils {

    public static boolean isStringParsable(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException ex){
            return false;
        }
    }

}
