package uk.co.automatictester.lightning.tests;

import uk.co.automatictester.lightning.Test;

public class RespTimesStableTest extends Test {

    private String transactionName;

    public RespTimesStableTest(String name, String description, String transactionName) {
        super(name, description);
        this.transactionName = transactionName;
    }

    // TODO implement
    public void execute() {
        System.out.println("name : " + name);
        System.out.println("description : " + description);
        System.out.println("transactionName : " + transactionName);
    }
}
