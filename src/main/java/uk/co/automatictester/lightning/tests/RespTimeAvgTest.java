package uk.co.automatictester.lightning.tests;

import uk.co.automatictester.lightning.JMeterTransactions;
import uk.co.automatictester.lightning.TestResult;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class RespTimeAvgTest extends LightningTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Average response time <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Average response time = %s";

    private final long maxAvgRespTime;

    public RespTimeAvgTest(String name, String type, String description, String transactionName, long maxAvgRespTime) {
        super(name, type, description, transactionName);
        this.maxAvgRespTime = maxAvgRespTime;
        expectedResult = String.format(EXPECTED_RESULT_MESSAGE, maxAvgRespTime);
    }

    public void execute(JMeterTransactions originalJMeterTransactions) {
        Locale.setDefault(Locale.ENGLISH);

        try {
            JMeterTransactions transactions;
            if (transactionName != null) {
                transactions = originalJMeterTransactions.excludeLabelsOtherThan(transactionName);
            } else {
                transactions = originalJMeterTransactions;
            }

            double totalRespTime = 0;
            for (List<String> transaction : transactions) {
                String elapsed = transaction.get(1);
                totalRespTime += Long.parseLong(elapsed);
            }
            double avgRespTime = totalRespTime / transactions.size();
            DecimalFormat df = new DecimalFormat("#.##");
            double roundedAvgRespTime = Double.valueOf(df.format(avgRespTime));

            actualResult = String.format(ACTUAL_RESULT_MESSAGE, roundedAvgRespTime);

            if (roundedAvgRespTime > maxAvgRespTime) {
                result = TestResult.FAIL;
            } else {
                result = TestResult.PASS;
            }
        } catch (Exception e) {
            result = TestResult.IGNORED;
            actualResult = e.getMessage();
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof RespTimeAvgTest) {
            RespTimeAvgTest test = (RespTimeAvgTest) obj;
            return name.equals(test.name) &&
                    description.equals(test.description) &&
                    transactionName.equals(test.transactionName) &&
                    expectedResult.equals(test.expectedResult) &&
                    actualResult.equals(test.actualResult) &&
                    result == test.result &&
                    maxAvgRespTime == test.maxAvgRespTime &&
                    type.equals(test.type);
        } else {
            return false;
        }
    }
}
