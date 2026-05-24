import java.util.*;

public class Parser {

    public static Map<String, Double> symbolTable = new LinkedHashMap<>();

    public static void parseAndEvaluate(List<String> tokens) {
        parseAndEvaluate(tokens, true);
    }

    public static void parseAndEvaluate(List<String> tokens,
                                       boolean generateCode) {

        if (tokens.size() < 3)
            return;

        String var = tokens.get(0);

        if (!tokens.get(1).equals("=")) {
            System.out.println("Syntax Error!");
            return;
        }

        try {

            List<String> expr = tokens.subList(2, tokens.size());

            validateExpression(expr);

            double result = evaluateExpression(expr);

            symbolTable.put(var, result);

            if (result == (int) result)
                System.out.println(var + " = " + (int) result);
            else
                System.out.println(var + " = " + result);

            if (generateCode) {
                CodeGenerator.addVariable(var,
                        expressionToString(expr));
                TACGenerator.addAssignment(var, expr);
            }

        } catch (RuntimeException e) {

            if (e.getMessage() != null &&
                    e.getMessage().equals("Undefined Variable")) {

                System.out.println("Semantic Error!");
            }

            else if (e.getMessage() != null &&
                    e.getMessage().equals("Division By Zero")) {

                System.out.println("Semantic Error!");
            }

            else {

                System.out.println("Syntax Error!");
            }
        }
    }

    // 🔥 PUBLIC WRAPPER FOR CONTROL FLOW
    public static double evaluateExpressionPublic(List<String> tokens) {
        validateExpression(tokens);
        return evaluateExpression(tokens);
    }

    private static String expressionToString(List<String> tokens) {
        return String.join(" ", tokens);
    }

    private static void validateExpression(List<String> tokens) {

        if (tokens.isEmpty())
            throw new RuntimeException("Syntax Error");

        int balance = 0;
        String prev = "";

        for (String token : tokens) {

            if (token.equals("(")) {

                if (isOperand(prev) || isCloseParen(prev))
                    throw new RuntimeException("Syntax Error");

                balance++;
            } else if (token.equals(")")) {

                if (balance == 0 ||
                        isOperator(prev) ||
                        prev.equals("(") ||
                        prev.isEmpty()) {

                    throw new RuntimeException("Syntax Error");
                }

                balance--;
            } else if (isOperand(token)) {

                if (isOperand(prev) || isCloseParen(prev))
                    throw new RuntimeException("Syntax Error");
            } else if (isOperator(token)) {

                if (prev.isEmpty() ||
                        isOperator(prev) ||
                        prev.equals("(")) {

                    throw new RuntimeException("Syntax Error");
                }
            } else {
                throw new RuntimeException("Syntax Error");
            }

            prev = token;
        }

        if (balance != 0 ||
                isOperator(prev) ||
                prev.equals("(")) {

            throw new RuntimeException("Syntax Error");
        }
    }

    private static boolean isOperand(String token) {
        return token.matches("\\d+") ||
                token.matches("[a-zA-Z][a-zA-Z0-9]*");
    }

    private static boolean isOperator(String token) {

        return token.equals("+") ||
                token.equals("-") ||
                token.equals("*") ||
                token.equals("/") ||
                token.equals("<") ||
                token.equals(">") ||
                token.equals("==");
    }

    private static boolean isCloseParen(String token) {
        return token.equals(")");
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

            else if (token.equals("("))
                ops.push(token);

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

        if (op.equals("*") || op.equals("/"))
            return 3;
        if (op.equals("+") || op.equals("-"))
            return 2;
        if (op.equals("<") || op.equals(">"))
            return 1;
        if (op.equals("=="))
            return 0;
        return 0;
    }

    private static double apply(double a, double b, String op) {

        if (op.equals("+"))
            return a + b;
        if (op.equals("-"))
            return a - b;
        if (op.equals("*"))
            return a * b;
        if (op.equals("/")) {
            if (b == 0)
                throw new RuntimeException("Division By Zero");
            return a / b;
        }
        if (op.equals("<"))
            return a < b ? 1.0 : 0.0;
        if (op.equals(">"))
            return a > b ? 1.0 : 0.0;
        if (op.equals("=="))
            return a == b ? 1.0 : 0.0;

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