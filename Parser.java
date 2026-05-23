import java.util.*;

public class Parser {

    // ---------- SYMBOL TABLE ----------

    public static Map<String, Double> symbolTable = new LinkedHashMap<>();

    // ---------- PARSER ----------

    public static void parseAndEvaluate(
            List<String> tokens) {

        // minimum check

        if (tokens.size() < 3) {

            System.out.println(
                    "Syntax Error!");

            return;
        }

        String variable = tokens.get(0);

        String equalSign = tokens.get(1);

        // must contain =

        if (!equalSign.equals("=")) {

            System.out.println(
                    "Syntax Error!");

            return;
        }

        try {

            // expression part

            List<String> expr = tokens.subList(
                    2,
                    tokens.size());

            // empty expression

            if (expr.size() == 0) {

                System.out.println(
                        "Syntax Error!");

                return;
            }

            // syntax + semantic checks

            checkExpression(expr);

            // evaluate

            double result = evaluateExpression(expr);

            // save variable

            symbolTable.put(
                    variable,
                    result);

            // print result

            if (result == (int) result) {

                System.out.println(

                        variable + " = " +
                                (int) result);

            }

            else {

                System.out.println(

                        variable + " = " +
                                result);
            }

            // ---------- CODE GENERATION ----------

            StringBuilder generatedExpr = new StringBuilder();

            for (String t : expr) {

                generatedExpr.append(t);
            }

            CodeGenerator.addLine(

                    "double " +
                            variable +
                            " = " +
                            generatedExpr +
                            ";");
        }

        catch (Exception e) {

            // semantic error

            if (e.getMessage() != null &&
                    e.getMessage().equals(
                            "Undefined Variable")) {

                System.out.println(
                        "Semantic Error!");
            }

            // division by zero

            else if (e.getMessage() != null &&
                    e.getMessage().equals(
                            "Division By Zero")) {

                System.out.println(
                        "Semantic Error!");
            }

            // everything else

            else {

                System.out.println(
                        "Syntax Error!");
            }

            // ---------- ERROR RECOVERY ----------

            System.out.println(
                    "Recovered From Error...");
        }
    }

    // ---------- SYNTAX + SEMANTIC CHECK ----------

    private static void checkExpression(
            List<String> tokens) {

        int bracketCount = 0;

        for (int i = 0; i < tokens.size(); i++) {

            String token = tokens.get(i);

            // invalid token

            if (!token.matches("\\d+") &&
                    !token.matches(
                            "[a-zA-Z][a-zA-Z0-9]*")
                    &&
                    !token.equals("+") &&
                    !token.equals("-") &&
                    !token.equals("*") &&
                    !token.equals("/") &&
                    !token.equals("(") &&
                    !token.equals(")")) {

                throw new RuntimeException(
                        "Invalid Token");
            }

            // ---------- OPEN BRACKET ----------

            if (token.equals("(")) {

                bracketCount++;

                if (i > 0) {

                    String prev = tokens.get(i - 1);

                    if (prev.matches("\\d+") ||
                            prev.equals(")") ||
                            prev.matches(
                                    "[a-zA-Z][a-zA-Z0-9]*")) {

                        throw new RuntimeException(
                                "Missing Operator");
                    }
                }
            }

            // ---------- CLOSE BRACKET ----------

            if (token.equals(")")) {

                bracketCount--;

                if (bracketCount < 0) {

                    throw new RuntimeException(
                            "Extra Bracket");
                }

                // empty ()

                if (i > 0 &&
                        tokens.get(i - 1)
                                .equals("(")) {

                    throw new RuntimeException(
                            "Empty Bracket");
                }
            }

            // ---------- OPERATORS ----------

            if (token.equals("+") ||
                    token.equals("-") ||
                    token.equals("*") ||
                    token.equals("/")) {

                // operator at start/end

                if (i == 0 ||
                        i == tokens.size() - 1) {

                    throw new RuntimeException(
                            "Operator Error");
                }

                String prev = tokens.get(i - 1);

                String next = tokens.get(i + 1);

                // double operator

                if (prev.equals("+") ||
                        prev.equals("-") ||
                        prev.equals("*") ||
                        prev.equals("/") ||
                        prev.equals("(")) {

                    throw new RuntimeException(
                            "Double Operator");
                }

                if (next.equals("+") ||
                        next.equals("-") ||
                        next.equals("*") ||
                        next.equals("/") ||
                        next.equals(")")) {

                    throw new RuntimeException(
                            "Double Operator");
                }
            }

            // ---------- MISSING OPERATOR ----------

            if (token.matches("\\d+") ||
                    token.matches(
                            "[a-zA-Z][a-zA-Z0-9]*")) {

                if (i > 0) {

                    String prev = tokens.get(i - 1);

                    if (prev.matches("\\d+") ||
                            prev.equals(")") ||
                            prev.matches(
                                    "[a-zA-Z][a-zA-Z0-9]*")) {

                        throw new RuntimeException(
                                "Missing Operator");
                    }
                }
            }
        }

        // missing bracket

        if (bracketCount != 0) {

            throw new RuntimeException(
                    "Bracket Missing");
        }
    }

    // ---------- EVALUATION ----------

    private static double evaluateExpression(
            List<String> tokens) {

        Stack<Double> values = new Stack<>();

        Stack<String> ops = new Stack<>();

        for (int i = 0; i < tokens.size(); i++) {

            String token = tokens.get(i);

            // ---------- NUMBER ----------

            if (token.matches("\\d+")) {

                values.push(
                        Double.valueOf(token));
            }

            // ---------- VARIABLE ----------

            else if (token.matches(
                    "[a-zA-Z][a-zA-Z0-9]*")) {

                if (!symbolTable.containsKey(
                        token)) {

                    throw new RuntimeException(
                            "Undefined Variable");
                }

                values.push(
                        symbolTable.get(token));
            }

            // ---------- OPEN BRACKET ----------

            else if (token.equals("(")) {

                ops.push(token);
            }

            // ---------- CLOSE BRACKET ----------

            else if (token.equals(")")) {

                while (!ops.peek()
                        .equals("(")) {

                    double b = values.pop();

                    double a = values.pop();

                    String op = ops.pop();

                    values.push(
                            applyOp(a, b, op));
                }

                ops.pop();
            }

            // ---------- OPERATOR ----------

            else if (token.equals("+") ||
                    token.equals("-") ||
                    token.equals("*") ||
                    token.equals("/")) {

                while (!ops.isEmpty() &&
                        precedence(ops.peek()) >= precedence(token)) {

                    double b = values.pop();

                    double a = values.pop();

                    String op = ops.pop();

                    values.push(
                            applyOp(a, b, op));
                }

                ops.push(token);
            }
        }

        // remaining operations

        while (!ops.isEmpty()) {

            double b = values.pop();

            double a = values.pop();

            String op = ops.pop();

            values.push(
                    applyOp(a, b, op));
        }

        return values.pop();
    }

    // ---------- PRECEDENCE ----------

    private static int precedence(
            String op) {

        if (op.equals("+") ||
                op.equals("-")) {

            return 1;
        }

        if (op.equals("*") ||
                op.equals("/")) {

            return 2;
        }

        return 0;
    }

    // ---------- APPLY OP ----------

    private static double applyOp(
            double a,
            double b,
            String op) {

        if (op.equals("+")) {

            return a + b;
        }

        if (op.equals("-")) {

            return a - b;
        }

        if (op.equals("*")) {

            return a * b;
        }

        if (op.equals("/")) {

            // division by zero

            if (b == 0) {

                throw new RuntimeException(
                        "Division By Zero");
            }

            return a / b;
        }

        throw new RuntimeException(
                "Invalid Operator");
    }

    // ---------- SYMBOL TABLE ----------

    public static void printSymbolTable() {

        System.out.println(
                "\nSymbol Table:");

        for (String key : symbolTable.keySet()) {

            double value = symbolTable.get(key);

            if (value == (int) value) {

                System.out.println(

                        key + " = " +
                                (int) value);
            }

            else {

                System.out.println(

                        key + " = " +
                                value);
            }
        }
    }
}