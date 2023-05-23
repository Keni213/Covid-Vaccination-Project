package edu.upenn.cit594.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CovidRecord {
    String ZIPCode;
    String timestamp;
    int partiallyVaccinated;
    int fullyVaccinated;

    public void setPartiallyVaccinated(int partiallyVaccinated) {
        this.partiallyVaccinated = partiallyVaccinated;
    }

    public void setFullyVaccinated(int fullyVaccinated) {
        this.fullyVaccinated = fullyVaccinated;
    }

    public CovidRecord(String ZIPCode, String timestamp){
        this.ZIPCode = ZIPCode;
        this.timestamp = timestamp;
    }

    /**
     * check if the input string has valid ZIP Code format
     * @param ZIPCode input string
     * @return true if the input is valid
     */
    public static boolean isValidZIPCode(String ZIPCode){
        String zipCodeFormat = "^\\d{5}$";
        Pattern p = Pattern.compile(zipCodeFormat);
        Matcher m = p.matcher(ZIPCode);
        return m.find();
    }

    /**
     * check if the input string has valid ZIP Code format
     * @param timestamp input string
     * @return true if the input timestamp is valid
     */
    public static boolean isValidTimestamp(String timestamp){
        String zipCodeFormat = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])\\s" +
                "([0-1]\\d|2[0-3]):([0-4]\\d|5[1-9]):([0-4]\\d|5[1-9])$";
        Pattern p = Pattern.compile(zipCodeFormat);
        Matcher m = p.matcher(timestamp);
        return m.find();
    }

    public String getZIPCode() {
        return ZIPCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getPartiallyVaccinated() {
        return partiallyVaccinated;
    }

    public int getFullyVaccinated() {
        return fullyVaccinated;
    }

    public String toString(){
        return this.ZIPCode + "\t" + this.timestamp + "\t" + this.partiallyVaccinated + "\t" + this.fullyVaccinated;
    }
}
