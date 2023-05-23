package edu.upenn.cit594;
import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;
import edu.upenn.cit594.ui.CommandLineUserInterface;

public class Main {
    public static void main(String[] args) {

        // start a logger
        ReaderFiles reader = new ReaderFiles(args);
        //Call processor
        Processor processor = new Processor(reader);

        //get relevant list
        CommandLineUserInterface ui = new CommandLineUserInterface(processor,Logger.getInstance());

        //start the ui
        ui.MainMenu();

    }
}
