package edu.upenn.cit594.datamanagement;


import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class CSVReader {
    String filename;
    File file;
    FileReader fr;
    BufferedReader br;
    HashMap<String, Integer> header;

    public CSVReader(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.file = new File(filename);
        this.fr = new FileReader(file);
        this.br = new BufferedReader(fr);
    }

    // declaration of states
    enum STATES {START, QUOTED, UNQUOTED}

    /**
     * @return read tokens/strings in arrayList<String>
     * @throws IOException if the methods fail to read stream from the br
     */
    public LinkedList<String> readLine() throws IOException {
        LinkedList<String> record = new LinkedList<>();
        StringBuilder field = new StringBuilder();
        STATES state = STATES.START;

        // state machine to parse token from the steam into header ArrayList
        boolean escaped = false;
        boolean endOfLine = false;
        char c;

        // read token from the stream using state machine
        while (this.br.ready()) {
            c = (char) this.br.read();
            switch (state) {
                case START:
                    field.setLength(0); // clear the stringBuilder for new name
                    if (c == '\"') {
                        state = STATES.QUOTED;
                    } else if (c == '\n') {
                        record.add(field.toString());
                        endOfLine = true;
                    } else {
                        state = STATES.UNQUOTED;
                        if (c != ',' && c != '\r') {
                            field.append(c);
                        } else {
                            record.add("");
                            state = STATES.START;
                        }
                    }
                    break;
                case QUOTED:
                    switch (c) {
                        case '\r':
                            if (escaped) {
                                break;
                            } else {
                                field.append('\r');
                            }
                            break;
                        case '\n':
                            if (escaped) {
                                record.add(field.toString());
                                endOfLine = true;
                            } else {
                                field.append('\n');
                            }
                            break;
                        case '\"':
                            // if the escaped is true, append the '\"'
                            if (escaped) {
                                field.append('\"');
                                escaped = false;
                                // set the escape to true
                            } else {
                                escaped = true;
                            }
                            break;
                        case ',':
                            // if the escaped is true, the enclosure is completed. Therefore, the appending ends.
                            if (escaped) {
                                record.add(field.toString());
                                escaped = false;
                                state = STATES.START;
                                // if the escaped is false, the enclosure is not completed. append the ','.
                            } else {
                                field.append(',');
                            }
                            break;
                        default:
                            field.append(c);
                    }
                    break;
                case UNQUOTED:
                    switch (c) {
                        case '\n':
                            record.add(field.toString());
                            endOfLine = true;
                            break;
                        case ',':
                            record.add(field.toString());
                            state = STATES.START;
                            break;
                        case '\r':
                            break;
                        default:
                            field.append(c);
                    }
                    break;
            }
            if (endOfLine) break;
        }
        return record;
    }

    /**
     * parse header
     * @throws IOException if fail to read stream from br
     */
    public void parseHeader() throws IOException {
        this.header = new HashMap<>();
        LinkedList<String> headerFields = readLine();
        for (int i = 0; i < headerFields.size(); i++) {
            this.header.put(headerFields.get(i), i);
        }
    }

    public abstract void parseLine() throws IOException;

}
