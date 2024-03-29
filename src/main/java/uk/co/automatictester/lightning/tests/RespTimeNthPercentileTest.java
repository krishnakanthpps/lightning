package uk.co.automatictester.lightning.tests;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.JMeterTransactions;
import uk.co.automatictester.lightning.TestResult;
import uk.co.automatictester.lightning.utils.IntToOrdConverter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class RespTimeNthPercentileTest extends LightningTest {

    private static final String MESSAGE = "%s percentile of transactions have response time ";
    private static final String EXPECTED_RESULT_MESSAGE = MESSAGE + "<= %s";
    private static final String ACTUAL_RESULT_MESSAGE = MESSAGE + "= %s";

    private final double maxRespTime;
    private final int percentile;

    public RespTimeNthPercentileTest(String name, String type, String description, String transactionName, int percentile, double maxRespTime) {
        super(name, type, description, transactionName);
        this.maxRespTime = maxRespTime;
        this.percentile = percentile;
        expectedResult = String.format(EXPECTED_RESULT_MESSAGE, new IntToOrdConverter().convert(percentile), maxRespTime);
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

            DescriptiveStatistics ds = new DescriptiveStatistics();
            for (List<String> transaction : transactions) {
                String elapsed = transaction.get(1);
                ds.addValue(Double.parseDouble(elapsed));
            }
            double actualRespTimePercentile = ds.getPercentile((double) percentile);
            DecimalFormat df = new DecimalFormat("#.##");
            double roundedActualRespTimePercentile = Double.valueOf(df.format(actualRespTimePercentile));

            actualResult = String.format(ACTUAL_RESULT_MESSAGE, new IntToOrdConverter().convert(percentile), roundedActualRespTimePercentile);

            if (roundedActualRespTimePercentile > maxRespTime) {
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
        if (obj instanceof RespTimeNthPercentileTest) {
            RespTimeNthPercentileTest test = (RespTimeNthPercentileTest) obj;
            return name.equals(test.name) &&
                    description.equals(test.description) &&
                    transactionName.equals(test.transactionName) &&
                    expectedResult.equals(test.expectedResult) &&
                    actualResult.equals(test.actualResult) &&
                    result == test.result &&
                    maxRespTime == test.maxRespTime &&
                    percentile == test.percentile &&
                    type.equals(test.type);
        } else {
            return false;
        }
    }
}
