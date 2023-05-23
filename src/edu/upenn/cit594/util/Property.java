package edu.upenn.cit594.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Property {
    String marketValue;
    String liveableArea;
    String zipCode;

    /**
     * constructor
     * @param zipCode zip code
     */
    public Property(String zipCode){
        this.zipCode = zipCode;
    }


    /**
     * read first 5 digits as zipcode
     * @param zipCode unchecked zip code
     * @return null if the format is not valid
     */
    public static String readZIPCode(String zipCode){
        String validZIPCode = "^(\\d{5})";
        Pattern p = Pattern.compile(validZIPCode);
        Matcher m = p.matcher(zipCode);
        return m.find()? m.group(1): null;
    }

    /**
     * set market value
     * @param marketValue market value
     */
    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    /**
     * set livable area
     * @param liveableArea livable area
     */
    public void setLiveableArea(String liveableArea) {
        this.liveableArea = liveableArea;
    }

    public String toString(){
        return zipCode + "/ " + liveableArea + "/ " + marketValue;
    }

    /**
     * check if the input string has valid ZIP Code format
     * @param ZIPCode input string
     * @return true if the input is valid
     */
    public static boolean isValidZIPCode(String ZIPCode){
        // ignore the zip code that is less than 5 digits or the first 5 characters are not numbers
        String zipCodeFormat = "^\\d{5}";
        Pattern p = Pattern.compile(zipCodeFormat);
        Matcher m = p.matcher(ZIPCode);
        return m.find();
    }
    public String getMarketValue() {
        return marketValue;
    }

    public String getLiveableArea() {
        return liveableArea;
    }

    public String getZipCode() {
        return zipCode;
    }

}
