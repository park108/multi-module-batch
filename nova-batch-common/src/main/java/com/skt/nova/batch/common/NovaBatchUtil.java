package com.skt.nova.batch.common;

public class NovaBatchUtil {

    private static final String divider = "============================================";

    public static void validateBatchStartup(String[] args) {

        boolean hasJob = false;

        // Log starting messages and arguments
        System.out.println(divider);
        System.out.println("\033[1;34m\uD83D\uDE80    Launching NOVA Batch Application    \uD83D\uDE80\033[0m");
        System.out.println(divider);
        System.out.println("\033[1;32m\uD83D\uDCDD  Parameters Passed to the Application  \uD83D\uDCDD\033[0m");

        for (String arg : args) {

            String[] keyValue = arg.split("=");
            System.out.printf("  \u001B[1;33m  - %s = %s\u001B[0m%n", keyValue[0], keyValue[1]);

            // Check required arguments
            if (keyValue[0].equals("job")) {
                hasJob = true;
            }
        }
        System.out.println(divider);

        // If it has no required arguments, invoke illegal argument exception
//        if (!hasJob) {
//            throw new IllegalArgumentException("Argument 'job' is required");
//        }
    }
}
