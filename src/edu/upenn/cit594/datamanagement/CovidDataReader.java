package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidRecord;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface CovidDataReader {

    /**
     * reads covid data from the given file and put it into covidRecords. It will be called when an instance is
     * constructed. Need to be implemented for different input file types
     */
    void readCovidRecords() throws IOException, ParseException;

    /**
     * method used to get read covid records
     *
     * @return a list of CovidRecords
     */
    List<CovidRecord> getCovidRecords();
}
