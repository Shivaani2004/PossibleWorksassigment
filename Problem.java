import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Problem {

    // Function to convert a number from a given base to decimal (BigInteger for large values)
    private static BigInteger convertToDecimal(String value, int base) {
        return new BigInteger(value, base);
    }

    // Function to compute Lagrange Interpolation at x=0 to find constant term c
    private static BigInteger lagrangeInterpolation(List<int[]> points) {
        int k = points.size();
        BigInteger out = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            int xi = points.get(i)[0];
            BigInteger yi = new BigInteger(String.valueOf(points.get(i)[1]));
            BigInteger term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    int xj = points.get(j)[0];
                    term = term.multiply(BigInteger.valueOf(-xj));
                    term = term.divide(BigInteger.valueOf(xi - xj));
                }
            }
            out = out.add(term);
        }
        return out;
    }

    public static void main(String[] args) {
        try {
            // Read JSON from file
            String content = new String(Files.readAllBytes(Paths.get("testcases.json")));
            JSONObject jsonData = new JSONObject(content);
            JSONArray testcases = jsonData.getJSONArray("testcases");

            List<BigInteger> results = new ArrayList<>();

            // Loop through each test case
            for (int t = 0; t < testcases.length(); t++) {
                JSONObject testcase = testcases.getJSONObject(t);
                JSONObject keys = testcase.getJSONObject("keys");

                int n = keys.getInt("n");
                int k = keys.getInt("k");

                List<int[]> points = new ArrayList<>();

                // Parse (x, y) pairs
                for (String key : testcase.keySet()) {
                    if (key.equals("keys")) continue;
                    int x = Integer.parseInt(key);
                    JSONObject valueObj = testcase.getJSONObject(key);
                    int base = Integer.parseInt(valueObj.getString("base"));
                    String yValue = valueObj.getString("value");

                    // Convert y from given base to decimal
                    BigInteger y = convertToDecimal(yValue, base);

                    points.add(new int[]{x, y.intValue()});  // Store as integer pairs
                }

                // Sort points based on x values
                points.sort(Comparator.comparingInt(a -> a[0]));

                // Select the first k points
                List<int[]> selectedPoints = points.subList(0, k);

                // Compute constant term (c)
                BigInteger secretC = lagrangeInterpolation(selectedPoints);
                results.add(secretC);
            }

            // Print results for both test cases
            for (BigInteger res : results) {
                System.out.println("Secret C: " + res);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
