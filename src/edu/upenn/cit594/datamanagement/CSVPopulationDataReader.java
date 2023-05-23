package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Population;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

public class CSVPopulationDataReader extends CSVReader{

    LinkedList<Population> populations;

    /**
     * constructor
     * @param filename name of the input file
     * @throws FileNotFoundException if the constructor fails
     */
    public CSVPopulationDataReader(String filename) throws IOException {
        super(filename);
        populations = new LinkedList<>();
        readPopulationRecords();
    }

    /**
     * read header and records from the population data
     * @throws IOException if the method fails
     */
    private void readPopulationRecords() throws IOException {
        parseHeader();
        parseLine();
    }

    public LinkedList<Population> getPopulations() {
        return populations;
    }

    @Override
    public void parseLine() throws IOException {
        LinkedList<String> line = readLine();

        while (line != null && line.size() != 0) {
            String zipCode = line.get(this.header.get("zip_code"));
            String population = line.get(this.header.get("population"));

            // ignore the record if zip code is not 5-digit number or population is not an integer
            if (Population.readZIPCode(zipCode) == null || !Population.isInteger(population)){
                line = readLine();
                continue;
            }

            Population record = new Population(zipCode, Integer.parseInt(population));

            this.populations.add(record);
            line = readLine();
        }
    }
}
