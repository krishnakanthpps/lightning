package uk.co.automatictester.lightning.readers;

import com.opencsv.CSVReader;
import uk.co.automatictester.lightning.JMeterTransactions;
import uk.co.automatictester.lightning.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.exceptions.CSVFileMissingColumnNameException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JMeterCSVFileReader {

    private int labelIndex;
    private int elapsedIndex;
    private int successIndex;
    private int timeStampIndex;

    public JMeterTransactions getTransactions(String csvFile) {
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));

            String[] columnNames = reader.readNext();
            getColumnIndexes(columnNames);

            String[] jmeterTransaction;
            while ((jmeterTransaction = reader.readNext()) != null) {
                ArrayList<String> currentTransaction = new ArrayList<>();
                String labelValue = jmeterTransaction[labelIndex];
                String elapsedValue = jmeterTransaction[elapsedIndex];
                String successValue = jmeterTransaction[successIndex];
                String timeStampValue = jmeterTransaction[timeStampIndex];
                currentTransaction.add(labelValue);     // 0
                currentTransaction.add(elapsedValue);   // 1
                currentTransaction.add(successValue);   // 2
                currentTransaction.add(timeStampValue); // 3
                jmeterTransactions.add(currentTransaction);
            }
        } catch (IOException e) {
            throw new CSVFileIOException(e.getMessage());
        }
        return jmeterTransactions;
    }

    private void getColumnIndexes(String[] columnNames) {
        labelIndex = getColumnIndexFor(columnNames, "label");
        elapsedIndex = getColumnIndexFor(columnNames, "elapsed");
        successIndex = getColumnIndexFor(columnNames, "success");
        timeStampIndex = getColumnIndexFor(columnNames, "timeStamp");
    }

    private int getColumnIndexFor(String[] columnNames, String searchedColumnName) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(searchedColumnName)) {
                return i;
            }
        }
        throw new CSVFileMissingColumnNameException(searchedColumnName);
    }
}
