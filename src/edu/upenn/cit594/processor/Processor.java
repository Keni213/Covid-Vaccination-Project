package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.ReaderFiles;
import edu.upenn.cit594.util.CovidRecord;
import edu.upenn.cit594.util.Population;
import edu.upenn.cit594.util.Property;

import java.util.*;

public class Processor {

    //public CSVReader;
    //public JSONCovidDataReader;
    public ReaderFiles read;

    public List<CovidRecord> covidList;
    public Map<String, Integer> populationMap;
    public List<Property> propertyList;

    // 3.2
    public int totalPopulation;

    //3.3
    public Map<String,Map<String,Double>> vaccineInfoMem;

    //3.4 & 3.5
    public Map<String,Integer> getAverageValueByZIPCodeMem;

    //3.6
    public Map<String,Integer> marketValuePerCapitaMem;

    //3.7
    public Map<String,String> valueMem;

    /**
     * constructor of the Processor class
     * @param read a complied file reader
     */
    public Processor(ReaderFiles read) {
        this.read = read;
        this.covidList = this.read.getCovidRecordList();
        this.propertyList = this.read.getPropertyRecordList();
        this.populationMap = new TreeMap<>();

        if (this.read.getPopulationRecordList() != null){
            for (Population population : this.read.getPopulationRecordList()) {
                populationMap.put(population.getZipCode(), population.getPopulation());
            }
        }

        this.totalPopulation = -1;
        this.vaccineInfoMem = new HashMap<>();
        this.getAverageValueByZIPCodeMem = new HashMap<>();
        this.marketValuePerCapitaMem = new HashMap<>();
        this.valueMem = new HashMap<>();
    }

    /**
     * 3.2 return the total population
     * @return total population
     */
    public int totalPopulation() {
        // Memorization
        if (totalPopulation != -1) return totalPopulation;

        int total = 0;
        for (Map.Entry<String, Integer> entry : populationMap.entrySet()) {
            total += entry.getValue();
        }
        this.totalPopulation = total;
        return total;
    }

    /**
     * 3.3 save as map <zip,rate> fully/partial vaccination
     * 1.ZIP Code must be written in ascending numerical order. Addressed by TreeMap
     * 2.total vaccinations per capita must be displayed rounded 4 digits after decimal point. Addressed in UI
     * 3.Ignore record whose ZIP Code is missing or has invalid format. Captured in dataReader.
     * 4.Do not display record where the vaccination number is 0. Addressed in the if statement below.
     * 5.Do not display record where the population record for the ZIP Code is not available. Addressed in the if statement below.
     * 6.Display 0 if the input date is out of range for underlying data. Addressed in UI.
     * @param date date from user input
     * @param type type from user input
     * @return map with ZIP Code as key and vaccination rate as value
     */
    public Map<String, Double> vaccineInfoMap(String date, String type) {
        // memorization
        String input = date + type;
        if(vaccineInfoMem.containsKey(input)){
            return vaccineInfoMem.get(input);
        }

        Map<String, Double> vaccineRateByZip = new TreeMap<>();
        for (CovidRecord c : this.covidList) {
            int population;

            //if time is null, or wrong format, continue//(???)
            // dataReader has checked the format of the timestamp when reading the data
            if (c.getTimestamp() == null || !c.getTimestamp().split(" ")[0].equals(date)) {
                continue;
            }

            // check if zipcode data is null or has invalid format
            // could be redundant because this is already checked when reading the data
            if (c.getZIPCode() == null || !CovidRecord.isValidZIPCode(c.getZIPCode())) {
                continue;
            }

            //Do not display record where the vaccination number is 0. Addressed in the if statement below
            if (type.equals("full") && c.getFullyVaccinated() == 0) {
                continue;
            }

            if (type.equals("partial") && c.getPartiallyVaccinated() == 0) {
                continue;
            }

            //if population datum is missing, ignore it
            if (this.populationMap.get(c.getZIPCode()) == null) {
                continue;
            } else {
                population = this.populationMap.get(c.getZIPCode());
            }

            // if population is 0, skip the calculation
            if (type.equals("full") && population != 0) {
                vaccineRateByZip.put(c.getZIPCode(), c.getFullyVaccinated() * 1.0 / population);
            }
            if (type.equals("partial") && population != 0) {
                vaccineRateByZip.put(c.getZIPCode(), c.getPartiallyVaccinated() * 1.0 / population);
            }
        }
        //add to memoization map
        vaccineInfoMem.put(input,vaccineRateByZip);

        return vaccineRateByZip;
    }


    /**
     * 3.4/3.5 get average market value/livable area for given ZIP Code
     * 1.check for valid ZIP Code in UI.
     * 2.If the value does not have a valid format, skip that record. Addressed in Average class.
     * 3.Returned result must be truncated integer instead of rounded. Addressed by using integer division.
     *
     * @param ZIPCode ZIP Code to filter property data
     * @param average an argument that determines the type of average
     * @return average of inquired data
     */
    public int getAverageValueByZIPCode(String ZIPCode, Average average) {
        //check Mem
        String input= ZIPCode + average.toString();
        if(getAverageValueByZIPCodeMem.containsKey(input)){
            return getAverageValueByZIPCodeMem.get(input);
        }

        ArrayList<Property> properties = new ArrayList<>();

        for (Property property : this.propertyList) {
            if (property.getZipCode().equals(ZIPCode)){
                properties.add(property);
            }
        }
        //add to Mem
        getAverageValueByZIPCodeMem.put(input, average.getAverage(properties));

        return average.getAverage(properties);
    }

    /**
     * 3.6 Total Market value Per Capita
     * 1.The market value of properties per capita must be displayed as a truncated integer. Addressed by integer division.
     * 2.display 0 if the total market value of properties in the ZIP Code is 0. Addressed in the if statement below.
     * 3.display 0 if the population of the ZIP Code is 0. Addressed in the if statement below.
     * 4.display 0 if the user enters a ZIP Code that is not listed in the population input file. Addressed in the if statement below.
     *
     * @param ZIPCode ZIP Code used for filter data
     * @return market value per capita
     */
    public int marketValuePerCapita(String ZIPCode) {
        if(marketValuePerCapitaMem.containsKey(ZIPCode)){
            return marketValuePerCapitaMem.get(ZIPCode);
        }

        // return 0 if the user enters a ZIP Code that is not listed in the population input file.
        if (this.populationMap.get(ZIPCode) == null) return 0;

        double totalValue = 0.0;
        int population = this.populationMap.get(ZIPCode);

        // return 0 if the population is 0
        if (population == 0) return 0;

        for (Property p : this.propertyList) {
            //ignore if zipcode is null or not such zipcode,continue
            if (p.getZipCode() != null && p.getZipCode().equals(ZIPCode)) {
                try {
                    double value = Double.parseDouble(p.getMarketValue());
                    totalValue += value;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        //if the total value of the market value is 0, return 0
        if (totalValue == 0.0) return 0;

        // save to memoization
        marketValuePerCapitaMem.put(ZIPCode,((int) totalValue / population));

        //if the zipcode is not list in the population, total=0,populaiton=0,return0
        return ((int) totalValue / population);
    }

    /**
     * 3.7 find out the full vaccinations per capita of the area that has the highest Property Value for the given date
     * @return full vaccinations per capita of the area that has the highest Property Value
     */
    public String fullVaccinationsPerCapitaOfAreaWithHighestPropertyValuePerCapita(String date){
        if(valueMem.containsKey(date)){
            return valueMem.get(date);
        }

        // collects the ZIP Codes for the areas that have full vaccination data
        Map<String, Double> vaccinationMap = vaccineInfoMap(date, "full");

        // return a message if there is no data for the given date
        if (vaccinationMap == null || vaccinationMap.isEmpty()) return "No vaccination data available on the given date.";

        // ZIP Code Of Area With Highest Market ValuePer Capita
        String ZIPCode = "";
        int highestMarketValuePerCapita = 0;

        // find the ZIP Code Of Area With Highest MarketValue Per Capita
        for (Map.Entry<String, Double> entry: vaccinationMap.entrySet()){
            String entryZIPCode = entry.getKey();
            if (marketValuePerCapita(entryZIPCode) > highestMarketValuePerCapita){
                highestMarketValuePerCapita = marketValuePerCapita(entryZIPCode);
                ZIPCode = entryZIPCode;
            }
        }
        if (ZIPCode.equals("")) return "No associated population data available.";

        valueMem.put(date,ZIPCode + " " + String.format("%.4f", vaccinationMap.get(ZIPCode)));

        // return message if no associated market value data is available.
        if (ZIPCode.length() == 0 || vaccinationMap.get(ZIPCode) == null){
            return "No associated market value data is available.";
        }

        return ZIPCode + " " + String.format("%.4f", vaccinationMap.get(ZIPCode));
    }

    /**
     * get CovidList
     * @return a list of CovidRecord
     */
    public List<CovidRecord> getCovidList() {
        return covidList;
    }

    /**
     * get PopulationMap
      * @return a TreeMap with ZIP Code as its key and population as its value
     */
    public Map<String, Integer> getPopulationMap() {
        return populationMap;
    }

    /**
     * get Property list
     * @return a list of Property objects
     */
    public List<Property> getPropertyList() {
        return propertyList;
    }
}