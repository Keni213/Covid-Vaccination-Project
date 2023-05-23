package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class JSONCovidDataReader implements CovidDataReader {
    String filename;
    List<CovidRecord> covidRecords;

    /**
     * constructor of JSONCovidDataReader
     *
     * @param filename name of the file to read data from
     * @throws IOException    if the constructor fails to read data from FileReader/BufferedReader
     * @throws ParseException if the construct fails to parse data from JSON file
     */
    public JSONCovidDataReader(String filename) throws IOException, ParseException {
        this.filename = filename;
        readCovidRecords();
    }

    /**
     * method to read valid covid record
     * @throws IOException    if the constructor fails to read data from FileReader/BufferedReader
     * @throws ParseException if the construct fails to parse data from JSON file
     */
    public void readCovidRecords() throws IOException, ParseException {
        this.covidRecords = new LinkedList<>();
        Object obj = new JSONParser().parse(new FileReader(this.filename));
        JSONArray ja = (JSONArray) obj;

        for (Object o : ja) {
            JSONObject jo = (JSONObject) o;
            String zipCode = jo.get("zip_code").toString();
            String timestamp = jo.get("etl_timestamp").toString();

            // if the record has an invalid zipcode format or timestamp, ignore this record
            if (!CovidRecord.isValidZIPCode(zipCode) || !CovidRecord.isValidTimestamp(timestamp)){
                continue;
            }

            // construct covid record
            CovidRecord record = new CovidRecord(zipCode, timestamp);

            if (jo.get("partially_vaccinated") != null) {
                String partiallyVaccinated = jo.get("partially_vaccinated").toString();
                try {
                    record.setPartiallyVaccinated(Integer.parseInt(partiallyVaccinated));
                } catch (NumberFormatException ignored) { }
            }
            if (jo.get("fully_vaccinated") != null) {
                String fullyVaccinated = jo.get("fully_vaccinated").toString();
                try {
                    record.setFullyVaccinated(Integer.parseInt(fullyVaccinated));
                } catch (NumberFormatException ignored) { }
            }
            this.covidRecords.add(record);
        }
    }

    @Override
    public List<CovidRecord> getCovidRecords() {
        return covidRecords;
    }
}
