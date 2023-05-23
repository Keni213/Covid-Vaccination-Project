package edu.upenn.cit594.ui;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.AreaValueAverage;
import edu.upenn.cit594.processor.Average;
import edu.upenn.cit594.processor.MarketValueAverage;
import edu.upenn.cit594.processor.Processor;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineUserInterface {

    protected Processor processor;
    protected Logger logger;

    public CommandLineUserInterface(Processor processor, Logger logger) {
        this.processor = processor;
        this.logger = logger;
    }


    public void MainMenu() {
        String userOption = "";
        Scanner scanner = new Scanner(System.in);

        while (!userOption.equals("0")) {
            System.out.println();
            System.out.print("Please enter a number for your choice.\n"
                    + "0- Exit the program.\n"
                    + "1- Show the available data sets.\n"
                    + "2- Show the total population for all ZIP Codes.\n"
                    + "3- Show the total vaccinations per capita for each ZIP Code for the specified date.\n"
                    + "4- Show the average market value for properties in a specified ZIP Code.\n"
                    + "5- Show the average total livable area for properties in a specified ZIP Code.\n"
                    + "6- Show the total market value of properties, per capita, for a specified ZIP Code.\n"
                    + "7- Show the full vaccinations per capita for the area with highest market value per capita for specified date.\n"
            );

            userOption = getUserInput(scanner);
            switch (userOption) {
                //0.exit the program
                case "0":
                    System.out.println("Exited.");
                    break;
                case "1":
                    printAvailableData();
                    break;
                case "2":
                    printTotalPopulation();
                    break;
                case "3":
                    printTotalVaccinationsPerCapita(scanner);
                    break;
                case "4":
                    printDataAverage(scanner, new MarketValueAverage());
                    break;
                case "5":
                    printDataAverage(scanner, new AreaValueAverage());
                    break;
                case "6":
                    printMarketValuePerCapita(scanner);
                    break;
                case "7":
                    printFullVaccinationsPerCapitaOfAreaWithHighestPropertyValuePerCapita(scanner);
                    break;
                default:
                    System.out.println("ERROR: Please input correct number from 0-7.\n");
            }
        }

        scanner.close();
    }

    /**
     * 3.1 Show the available data sets
     */
    public void printAvailableData() {
        System.out.println("BEGIN OUTPUT");
        if (processor.covidList != null) {
            System.out.println("covid");
        }
        if (processor.populationMap != null) {
            System.out.println("population");
        }
        if (processor.propertyList != null) {
            System.out.println("properties");
        }
        System.out.println("END OUTPUT");
    }

    /**
     * 3.2 show the total population for all ZIP Codes
     */
    private void printTotalPopulation() {
        if (processor.getPopulationMap() == null) {
            System.out.println("Sorry, the population file is missing.\n");
            return;
        }

        System.out.println("BEGIN OUTPUT");
        System.out.println(processor.totalPopulation());
        System.out.println("END OUTPUT");
    }

    /**
     * 3.3 Total Partial or Full Vaccination
     * ask user for which type of vaccination data to inquire
     * ask user for the data of the data to inquire
     * print the partial/full vaccinations per capita
     *
     * @param scanner scanner to get user's input
     */
    private void printTotalVaccinationsPerCapita(Scanner scanner) {
        if (processor.getCovidList() == null) {
            System.out.println("Sorry, the covid file is missing.\n");
            return;
        }
        if (processor.getPopulationMap() == null) {
            System.out.println("Sorry, the population file is missing.\n");
            return;
        }

        String userInputForType = getInputType(scanner);
        String userInputForDate = getInputDate(scanner);

        System.out.println("BEGIN OUTPUT");
        if (processor.vaccineInfoMap(userInputForDate, userInputForType).isEmpty()) {
            // if there is no data for the input data, print 0
            System.out.println(0);
        } else {
            for (Map.Entry<String, Double> entry : processor.vaccineInfoMap(userInputForDate, userInputForType).entrySet()) {
                System.out.printf("%s %.4f\n", entry.getKey(), entry.getValue());
            }
        }
        System.out.println("END OUTPUT");
    }

    /**
     * 3.4 Average Market Value
     * 3.5 Average Total Livable Area
     * Ask user to input a ZIP Code and print market value/livable area average associated to the ZIP Code
     *
     * @param scanner scanner to get user's input
     * @param average average class that determines whether to print market value or livable area average
     */
    private void printDataAverage(Scanner scanner, Average average) {
        if (processor.getPropertyList() == null) {
            System.out.println("the properties file is missing.\n");
            return;
        }

        String inputZIPCode = getInputZIPCode(scanner);

        System.out.println("BEGIN OUTPUT");
        System.out.println(processor.getAverageValueByZIPCode(inputZIPCode, average));
        System.out.println("END OUTPUT");
    }

    /**
     * 3.6 Total Market Value Per Capita
     * Ask user to input a ZIP Code and then print market value per Capita for that ZIP Code
     *
     * @param scanner scanner to read user's input
     */
    private void printMarketValuePerCapita(Scanner scanner) {
        if (processor.getPropertyList() == null) {
            System.out.println("the properties file is missing.\n");
            return;
        }
        if (processor.getPopulationMap() == null) {
            System.out.println("Sorry, the population file is missing.\n");
            return;
        }

        String inputZIPCode = getInputZIPCode(scanner);

        System.out.println("BEGIN OUTPUT");
        System.out.println(processor.marketValuePerCapita(inputZIPCode));
        System.out.println("END OUTPUT");
    }

    /**
     * 3.7 ask user to input the date to inquire full vaccinations per capita for the area with the highest market
     * value per capita and print the inquired result
     *
     * @param scanner scanner to get user's input
     */
    private void printFullVaccinationsPerCapitaOfAreaWithHighestPropertyValuePerCapita(Scanner scanner) {
        if (processor.getCovidList() == null) {
            System.out.println("Sorry, the covid file is missing.\n");
            return;
        }
        if (processor.getPopulationMap() == null) {
            System.out.println("Sorry, the population file is missing.\n");
            return;
        }
        if (processor.getPropertyList() == null) {
            System.out.println("the properties file is missing.\n");
            return;
        }

        String date = getInputDate(scanner);

        System.out.println("BEGIN OUTPUT");
        System.out.printf(processor.fullVaccinationsPerCapitaOfAreaWithHighestPropertyValuePerCapita(date) + "\n");
        System.out.println("END OUTPUT");
    }

    /**
     * helper function to get a 5-digit ZIP Code from user's input
     *
     * @param scanner scanner to get user's input
     * @return a ZIP Code in string format
     */
    private String getInputZIPCode(Scanner scanner) {
        System.out.println("Please enter a 5-digit ZIP Code.");
        String inputZIPCode;
        String validZIPCode = "^\\d{5}$";
        Pattern p = Pattern.compile(validZIPCode);
        Matcher m;

        while (true) {
            inputZIPCode = getUserInput(scanner);
            m = p.matcher(inputZIPCode);
            if (m.find()) break;
            System.out.println("Invalid Input. Please enter a 5-digit ZIP Code.");
        }

        return inputZIPCode;
    }

    /**
     * helper function to get user's input for type(partial/full vaccinations)
     *
     * @param scanner scanner to get user's input
     * @return a type in string format
     */
    private String getInputType(Scanner scanner) {
        // prompt the user to type "partial" or "full"
        System.out.println("Please enter 'partial' or 'full' Vaccination. ");
        String userInputForType;
        while (true) {
            userInputForType = getUserInput(scanner);
            if (userInputForType.equals("partial") || userInputForType.equals("full")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'partial' or 'full' Vaccination. ");
            }
        }
        return userInputForType;
    }

    /**
     * helper function to get user's input for a date
     *
     * @param scanner scanner to get user's input
     * @return a date in string format
     */
    private String getInputDate(Scanner scanner) {
        // prompt the user to type a date in the format: YYYY-MM-DD
        System.out.println("Please enter a date in the format: YYYY-MM-DD. ");
        String userInputForDate;
        String validDate = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        Pattern p = Pattern.compile(validDate);
        Matcher m;

        while (true) {
            userInputForDate = getUserInput(scanner);
            m = p.matcher(userInputForDate);
            if (m.find()) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a date in the format: YYYY-MM-DD. ");
            }
        }

        return userInputForDate;
    }

    /**
     * helper function get user's input from the scanner
     *
     * @param scanner scanner to get user's input
     * @return user's input in string format
     */
    private String getUserInput(Scanner scanner) {
        System.out.print("> ");
        System.out.flush();
        String userInput = scanner.nextLine();
        // log user's input
        try {
            Logger.getInstance().log(userInput);
        } catch (IOException e) {
            System.out.println("failed to log user's input.");
            throw new RuntimeException(e);
        }
        System.out.println();
        return userInput;
    }

}
