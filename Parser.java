import java.util.*;

public class Parser {

    public static Map<String, Double> symbolTable = new LinkedHashMap<>();

    public static void parseAndEvaluate(List<String> tokens) {

        if (tokens.size() < 3) return;

        String var = tokens.get(0);

        if (!tokens.get(1).equals("=")) {
            System.out.println("Syntax Error!");
            return;
        }

        try {

            List<String> expr = tokens.subList(2, tokens.size());

            double result = evaluateExpression(expr);

            symbolTable.put(var, result);

            if (result == (int) result)
                System.out.println(var + " = " + (int) result);
            else
                System.out.println(var + " = " + result);

            StringBuilder gen = new StringBuilder();
            for (String t : expr) gen.append(t);

            CodeGenerator.addVariable(var, gen.toString());

        } catch (Exception e) {
            System.out.println("Error handled in Parser...");
        }
    }

    // 🔥 PUBLIC WRAPPER FOR CONTROL FLOW
    public static double evaluateExpressionPublic(List<String> tokens) {
        return evaluateExpression(tokens);
    }

    // ---------- EXPRESSION EVALUATION ----------
    private static double evaluateExpression(List<String> tokens) {

        Stack<Double> values = new Stack<>();
        Stack<String> ops = new Stack<>();

        for (String token : tokens) {

            if (token.matches("\\d+")) {
                values.push(Double.valueOf(token));
            }

            else if (token.matches("[a-zA-Z][a-zA-Z0-9]*")) {

                if (!symbolTable.containsKey(token))
                    throw new RuntimeException("Undefined Variable");

                values.push(symbolTable.get(token));
            }

            else if (token.equals("(")) ops.push(token);

            else if (token.equals(")")) {

                while (!ops.peek().equals("(")) {
                    double b = values.pop();
                    double a = values.pop();
                    values.push(apply(a, b, ops.pop()));
                }

                ops.pop();
            }

            else {

                while (!ops.isEmpty() &&
                        precedence(ops.peek()) >= precedence(token)) {

                    double b = values.pop();
                    double a = values.pop();
                    values.push(apply(a, b, ops.pop()));
                }

                ops.push(token);
            }
        }

        while (!ops.isEmpty()) {

            double b = values.pop();
            double a = values.pop();
            values.push(apply(a, b, ops.pop()));
        }

        return values.pop();
    }

    private static int precedence(String op) {

        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/")) return 2;
        return 0;
    }

    private static double apply(double a, double b, String op) {

        if (op.equals("+")) return a + b;
        if (op.equals("-")) return a - b;
        if (op.equals("*")) return a * b;

        if (op.equals("/")) {
            if (b == 0) throw new RuntimeException("Division By Zero");
            return a / b;
        }

        throw new RuntimeException("Invalid Operator");
    }

    public static void printSymbolTable() {

        System.out.println("\nSymbol Table:");

        for (String k : symbolTable.keySet()) {

            double v = symbolTable.get(k);

            if (v == (int) v)
                System.out.println(k + " = " + (int) v);
            else
                System.out.println(k + " = " + v);
        }
    }
}