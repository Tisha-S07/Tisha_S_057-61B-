import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TACGenerator {

    static List<String> instructions = new ArrayList<>();
    private static int tempCounter = 0;
    private static int labelCounter = 0;

    public static void addAssignment(String variable, List<String> expr) {
        String result = generateExpressionTAC(expr);
        instructions.add(variable + " = " + result);
    }

    public static void addIf(List<String> condition,
                              List<String> truePart,
                              List<String> falsePart) {

        String cond = generateExpressionTAC(condition);
        String trueLabel = newLabel();
        String falseLabel = falsePart != null ? newLabel() : null;
        String endLabel = newLabel();

        instructions.add("if " + cond + " != 0 goto " + trueLabel);
        instructions.add("goto " + (falsePart != null ? falseLabel : endLabel));

        instructions.add(trueLabel + ":");
        generateStatementTAC(truePart);

        if (falsePart != null) {
            instructions.add("goto " + endLabel);
            instructions.add(falseLabel + ":");
            generateStatementTAC(falsePart);
        }

        instructions.add(endLabel + ":");
    }

    public static void addWhile(List<String> condition,
                                List<String> body) {

        String startLabel = newLabel();
        String bodyLabel = newLabel();
        String endLabel = newLabel();

        instructions.add(startLabel + ":");
        String cond = generateExpressionTAC(condition);
        instructions.add("if " + cond + " != 0 goto " + bodyLabel);
        instructions.add("goto " + endLabel);
        instructions.add(bodyLabel + ":");
        generateStatementTAC(body);
        instructions.add("goto " + startLabel);
        instructions.add(endLabel + ":");
    }

    private static void generateStatementTAC(List<String> tokens) {
        if (tokens == null || tokens.isEmpty())
            return;

        if (tokens.size() >= 3 && tokens.get(1).equals("=")) {
            addAssignment(tokens.get(0), tokens.subList(2, tokens.size()));
        }
    }

    private static String generateExpressionTAC(List<String> expr) {
        if (expr == null || expr.isEmpty())
            return "";

        if (expr.size() == 1)
            return expr.get(0);

        List<String> postfix = infixToPostfix(expr);
        Stack<String> valueStack = new Stack<>();

        for (String token : postfix) {
            if (isOperand(token)) {
                valueStack.push(token);
            } else {
                String b = valueStack.pop();
                String a = valueStack.pop();
                String temp = newTemp();
                instructions.add(temp + " = " + a + " " + token + " " + b);
                valueStack.push(temp);
            }
        }

        return valueStack.pop();
    }

    private static List<String> infixToPostfix(List<String> expr) {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();

        for (String token : expr) {
            if (isOperand(token)) {
                output.add(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (!operators.isEmpty())
                    operators.pop();
            } else {
                while (!operators.isEmpty() &&
                        !operators.peek().equals("(") &&
                        precedence(operators.peek()) >= precedence(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        return output;
    }

    private static boolean isOperand(String token) {
        return token.matches("\\d+") || token.matches("[a-zA-Z][a-zA-Z0-9]*");
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

    private static String newTemp() {
        return "t" + tempCounter++;
    }

    private static String newLabel() {
        return "L" + labelCounter++;
    }

    public static void printTAC() {
        System.out.println("\n========== INTERMEDIATE TAC ==========");
        for (String line : instructions) {
            System.out.println(line);
        }
    }

    public static void saveToFile() {
        try (FileWriter writer = new FileWriter("GeneratedCode.tac")) {
            for (String line : instructions) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("TAC File Writing Error!");
        }
    }
}
