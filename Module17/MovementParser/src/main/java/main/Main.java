package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String filePath = "movementList.csv";
    private static final int incomePosition = 6;
    private static final int expensesPosition = 7;
    private static final int descriptionPosition = 5;

    public static void main(String[] args) {
        for (Map.Entry<String, Double> entry : parseBankStatements().entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

    private static Map<String, Double> parseBankStatements() {
        Map<String, Double> expensesStats = new HashMap<>();

        InputStream in = Main.class.getClassLoader().getResourceAsStream(Main.filePath);
        if (in == null) return expensesStats;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            if (reader.readLine() == null) {
                return expensesStats;
            }

            String currentStr;
            while ((currentStr = reader.readLine()) != null) {
                String[] tokens = currentStr.split(",");
                String expenseType = parseExpenseType(tokens[descriptionPosition]);
                boolean typeExists = expensesStats.containsKey(expenseType);
                Double net = Double.parseDouble(tokens[incomePosition])
                        - Double.parseDouble(tokens[expensesPosition]);
                expensesStats.put(expenseType, typeExists ? expensesStats.get(expenseType) + net : net);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return expensesStats;
    }

    private static String parseExpenseType(String token) {
        String[] endsWithType = token.split("\\s{10}")[0].split("");

        int i;
        for (i = endsWithType.length - 1; i > 0; i--) {
            if (endsWithType[i].matches("[\\p{L}\\d ]")) continue;
            else break;
        }
        return token.substring(i + 1, endsWithType.length);
    }
}
