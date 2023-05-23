package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidRecord;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class CSVCovidDataReader extends CSVReader implements CovidDataReader {
    List<CovidRecord> covidRecords;

    /**
     * @param filename input filename
     * @throws IOException if input filename is not valid
     */
    public CSVCovidDataReader(String filename) throws IOException, ParseException {
        super(filename);
        covidRecords = new LinkedList<>();
        readCovidRecords();
    }

    /**
     * @throws IOException if method fails
     */
    public void readCovidRecords() throws IOException {
        parseHeader();
        parseLine();
    }

    /**
     * parse lines from the lexer
     */
    public void parseLine() throws IOException {
        List<String> line = readLine();

        while (line != null && line.size() != 0) {
            String zipCode = line.get(this.header.get("zip_code"));
            String timestamp = line.get(this.header.get("etl_timestamp"));

            // ignore the record if zip code or timestamp has invalid format
            if (!CovidRecord.isValidZIPCode(zipCode) || !CovidRecord.isValidTimestamp(timestamp)){
                line = readLine();
                continue;
            }

            CovidRecord record = new CovidRecord(zipCode, timestamp);

            if (this.header.get("partially_vaccinated") != null) {
                String partiallyVaccinated = line.get(this.header.get("partially_vaccinated"));
                if (partiallyVaccinated.length() == 0) {
                    record.setPartiallyVaccinated(0);
                } else {
                    try {
                        record.setPartiallyVaccinated(Integer.parseInt(partiallyVaccinated));
                    }catch (NumberFormatException ignored){}
                }
            }

            if (this.header.get("fully_vaccinated") != null) {
                String fullyVaccinated = line.get(this.header.get("fully_vaccinated"));
                if (fullyVaccinated.length() == 0) {
                    record.setFullyVaccinated(0);
                } else {
                    try{
                        record.setFullyVaccinated(Integer.parseInt(fullyVaccinated));
                    }catch (NumberFormatException ignored){}
                }
            }
            this.covidRecords.add(record);
            line = readLine();
        }
    }

    @Override
    public List<CovidRecord> getCovidRecords() {
        return this.covidRecords;
    }
}
