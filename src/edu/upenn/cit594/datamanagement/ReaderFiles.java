package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidRecord;
import edu.upenn.cit594.util.Population;
import edu.upenn.cit594.util.Property;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReaderFiles {

    Map<String, String> inputFiles;

    /**
     * constructor of the data reader class
     *
     * @param inputs a string array that has all the command line arguments
     */
    public ReaderFiles(String[] inputs) {
        this.inputFiles = new TreeMap<>();

        // define valid name for the inputs
        ArrayList<String> validNames = new ArrayList<>();
        validNames.add("covid");
        validNames.add("population");
        validNames.add("properties");
        validNames.add("log");

        // define regex with named capturing groups
        String filenameRegex = "^--(?<name>.+?)=(?<value>.+)$";
        Pattern p = Pattern.compile(filenameRegex);

        // find all matching inputs and store them into the inputFiles
        for (String s : inputs) {
            Matcher m = p.matcher(s);
            if (m.find()) {

                String name = m.group("name");
                String value = m.group("value");

                // throw an error message and terminate the program if the input name is invalid
                if (!validNames.contains(name)) {
                    throw new RuntimeException("Error: name of an argument is not one of the valid names" +
                            "(covid/properties/population/log).");
                }

                // throw an error message and terminate the program if the name has been input more than once
                if (inputFiles.get(name) != null) {
                    throw new RuntimeException("Error: The name of an argument is used more than once.");
                }

                // throw an error message and terminate the program if the logger file cannot be initialized
                if (name.equals("log")) {
                    try {
                        Logger.getInstance().writeTo(name);

                        //The command lind arguments to the program
                        //single entry with a space between each argument
                        StringBuilder logIn = new StringBuilder();
                        for (String arg : inputs) {
                            logIn.append(arg).append(" ");
                        }
                        Logger.getInstance().log(logIn.toString().trim());
                    } catch (IOException e) {
                        System.out.println("Error: The logger cannot be correctly initialized.");
                        throw new RuntimeException(e);
                    }
                }

                // throw an error message and terminate the program if covid data file is neither .csv nor .json
                if (name.equals("covid")) {
                    if (!value.toLowerCase().endsWith(".csv") && !value.toLowerCase().endsWith(".json")) {
                        throw new RuntimeException("Error: The format of the COVID data file can not be determined from " +
                                "the filename extension (\"csv\" or \"json\")");
                    }
                }

                this.inputFiles.put(name, value);

            } else {
                // throw an error message and terminate the program if the input format is incorrect
                throw new RuntimeException("Error: the argument does not match the form \"--name=value\"");
            }
        }
    }

    /**
     * method to read covid data from the given file
     *
     * @return a list of covid record
     */
    public List<CovidRecord> getCovidRecordList() {
        String covidFile = this.inputFiles.get("covid");
        if (covidFile == null) return null;
        try {
            // log file name for property data
            Logger.getInstance().log(covidFile);
            // read covid data file ending with .csv
            if (covidFile.toLowerCase().endsWith(".csv")) {
                CSVCovidDataReader csvCovidDataReader = new CSVCovidDataReader(covidFile);
                return csvCovidDataReader.getCovidRecords();

                // read covid data file ending with .json
            } else {
                JSONCovidDataReader jsonCovidDataReader = new JSONCovidDataReader(covidFile);
                return jsonCovidDataReader.getCovidRecords();
            }
        } catch (IOException | ParseException e) {
            // throw an error message and terminate the program if the input file does not exist or cannot be read
            System.out.println("Error: " + covidFile + " does not exist or cannot be opened for reading.");
            throw new RuntimeException(e);
        }
    }

    /**
     * method to read property data from the given file
     *
     * @return a list of Property
     */
    public List<Property> getPropertyRecordList() {
        String propertyFile = inputFiles.get("properties");
        if (propertyFile == null) return null;
        try {
            // log file name for property data
            Logger.getInstance().log(propertyFile);
            // create property data reader
            CSVPropertyDataReader csvPropertyDataReader = new CSVPropertyDataReader(propertyFile);
            return csvPropertyDataReader.getPropertyRecords();
        } catch (IOException e) {
            // throw an error message and terminate the program if the input file does not exist or cannot be read
            System.out.println("Error: " + propertyFile + " does not exist or cannot be opened for reading.");
            throw new RuntimeException(e);
        }
    }


    /**
     * method to read population data from the given file
     *
     * @return a list of population objects
     */
    public List<Population> getPopulationRecordList() {
        String populationFile = inputFiles.get("population");
        if (populationFile == null) return null;
        try {
            // log file name for population data
            Logger.getInstance().log(populationFile);
            // create population data reader
            CSVPopulationDataReader csvPopulationDataReader = new CSVPopulationDataReader(populationFile);
            return csvPopulationDataReader.getPopulations();
        } catch (IOException e) {
            System.out.println("Error: " + populationFile + " does not exist or cannot be opened for reading.");
            throw new RuntimeException(e);
        }
    }

}
