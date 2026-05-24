import java.util.*;

public class ControlFlowDispatcher {

    public static void handle(List<String> tokens) {

        String first = tokens.get(0);

        if (first.equals("if")) handleIf(tokens);
        else if (first.equals("while")) handleWhile(tokens);
    }

    private static void handleIf(List<String> tokens) {

        if (tokens.size() < 4 || !tokens.get(1).equals("(")) {
            System.out.println("Syntax Error!");
            return;
        }

        int end = findMatchingParenIndex(tokens, 1);
        if (end == -1) {
            System.out.println("Syntax Error!");
            return;
        }

        int elseIndex = tokens.indexOf("else");
        if (elseIndex != -1 && elseIndex < end)
            elseIndex = -1;

        List<String> condition = tokens.subList(2, end);
        List<String> truePart;
        List<String> falsePart = null;

        if (elseIndex == -1) {
            truePart = tokens.subList(end + 1, tokens.size());
        } else {
            truePart = tokens.subList(end + 1, elseIndex);
            falsePart = tokens.subList(elseIndex + 1, tokens.size());
        }

        try {
            double result = Parser.evaluateExpressionPublic(condition);

            if (result != 0) {
                if (!truePart.isEmpty())
                    Parser.parseAndEvaluate(truePart, false);
            } else if (falsePart != null && !falsePart.isEmpty()) {
                Parser.parseAndEvaluate(falsePart, false);
            }

            CodeGenerator.addIf(condition, truePart, falsePart);
            TACGenerator.addIf(condition, truePart, falsePart);
        } catch (RuntimeException e) {
            if (e.getMessage() != null &&
                    (e.getMessage().equals("Undefined Variable") ||
                            e.getMessage().equals("Division By Zero"))) {
                System.out.println("Semantic Error!");
            } else {
                System.out.println("Syntax Error!");
            }
        }
    }

    private static void handleWhile(List<String> tokens) {

        if (tokens.size() < 4 || !tokens.get(1).equals("(")) {
            System.out.println("Syntax Error!");
            return;
        }

        int end = findMatchingParenIndex(tokens, 1);
        if (end == -1) {
            System.out.println("Syntax Error!");
            return;
        }

        List<String> condition = tokens.subList(2, end);
        List<String> body = tokens.subList(end + 1, tokens.size());

        int safety = 0;

        try {
            while (Parser.evaluateExpressionPublic(condition) != 0) {
                if (!body.isEmpty())
                    Parser.parseAndEvaluate(body, false);

                safety++;
                if (safety > 100) break;
            }

            CodeGenerator.addWhile(condition, body);
            TACGenerator.addWhile(condition, body);
        } catch (RuntimeException e) {
            if (e.getMessage() != null &&
                    (e.getMessage().equals("Undefined Variable") ||
                            e.getMessage().equals("Division By Zero"))) {
                System.out.println("Semantic Error!");
            } else {
                System.out.println("Syntax Error!");
            }
        }
    }

    private static int findMatchingParenIndex(List<String> tokens,
                                             int openIndex) {

        int depth = 0;

        for (int i = openIndex; i < tokens.size(); i++) {
            if (tokens.get(i).equals("("))
                depth++;
            else if (tokens.get(i).equals(")")) {
                depth--;
                if (depth == 0)
                    return i;
            }
        }

        return -1;
    }
}