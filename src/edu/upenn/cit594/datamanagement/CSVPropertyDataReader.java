package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Property;

import java.io.IOException;
import java.util.LinkedList;

public class CSVPropertyDataReader extends CSVReader{
    LinkedList<Property> propertyRecords;

    /**
     * constructor
     * @param filename name of the input file
     * @throws IOException if the constructor fails
     */
    public CSVPropertyDataReader(String filename) throws IOException {
        super(filename);
        propertyRecords = new LinkedList<>();
        readPropertyRecords();
    }

    /**
     * parse and read header and records from the stream and store them into the global variables
     * @throws IOException if reading file fails
     */
    public void readPropertyRecords() throws IOException {
        parseHeader();
        parseLine();
    }

    public LinkedList<Property> getPropertyRecords() {
        return propertyRecords;
    }

    @Override

    public void parseLine() throws IOException {
        LinkedList<String> line = readLine();

        while (line != null && line.size() != 0) {
            String zipCode = line.get(this.header.get("zip_code"));
            String livableArea = line.get(this.header.get("total_livable_area"));
            String marketValue = line.get(this.header.get("market_value"));

            // ignore the record if zip code has invalid format
            if (Property.readZIPCode(zipCode) == null){
                line = readLine();
                continue;
            }

            if (zipCode.length() < 5 || Property.isValidZIPCode(zipCode)){
                Property record = new Property(zipCode.substring(0, 5));
                if (livableArea != null) record.setLiveableArea(livableArea);

                if (marketValue != null) record.setMarketValue(marketValue);
                this.propertyRecords.add(record);
            }

            line = readLine();
        }
    }
}
