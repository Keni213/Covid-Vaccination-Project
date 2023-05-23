package edu.upenn.cit594.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Population {
    String zipCode;
    int population;

    /**
     * constructor of the class
     * @param zipCode ZIP Code
     * @param population population
     */
    public Population(String zipCode, int population){
        this.zipCode = zipCode;
        this.population = population;
    }

    /**
     * read first 5 digit from the given zip code
     * @param zipCode unchecked zip code
     * @return null if the zip code format is invalid
     */
    public static String readZIPCode(String zipCode) {
        String validZIPCode = "^(\\d{5})$";
        Pattern p = Pattern.compile(validZIPCode);
        Matcher m = p.matcher(zipCode);
        return m.find()? m.group(1): null;
    }

    /**
     * check if the population record can be converted into integer
     * @param population unchecked population record
     * @return true if population record is numeric
     */
    public static boolean isInteger(String population){
        String validPopulation = "^\\d+$";
        Pattern p = Pattern.compile(validPopulation);
        Matcher m = p.matcher(population);
        return m.find();
    }

    public String toString(){
        return zipCode + "/" + population;
    }

    public String getZipCode() {
        return zipCode;
    }

    public int getPopulation() {
        return population;
    }
}
