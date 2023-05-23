package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private String filename;
    private static final Logger instance = new Logger(); // initialize instance of the singleton pattern

    /**
     * private constructor
     */
    private Logger(){}

    /**
     * @return instance of the Logger
     */
    public static Logger getInstance(){
        return instance;
    }

    /**
     * @param msg message to log
     * @throws IOException if filename is invalid
     */
    public void log(String msg) throws IOException {
        FileWriter fw = new FileWriter(this.filename, true);
        fw.write(System.currentTimeMillis() + " " + msg + "\n");
        fw.flush();
        fw.close();
    }

    /**
     * @param filename filename of the log file
     */
    public void writeTo(String filename){
        this.filename = filename;
    }
}
