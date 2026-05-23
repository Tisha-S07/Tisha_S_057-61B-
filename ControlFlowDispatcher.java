import java.util.*;

public class ControlFlowDispatcher {

    public static void handle(List<String> tokens) {

        String first = tokens.get(0);

        if (first.equals("if")) handleIf(tokens);
        else if (first.equals("while")) handleWhile(tokens);
    }

    private static void handleIf(List<String> tokens) {

        int end = tokens.indexOf(")");
        int elseIndex = tokens.indexOf("else");

        List<String> condition = tokens.subList(2, end);

        double result = Parser.evaluateExpressionPublic(condition);

        if (elseIndex == -1) {

            if (result != 0)
                Parser.parseAndEvaluate(tokens.subList(end + 1, tokens.size()));

            return;
        }

        List<String> truePart = tokens.subList(end + 1, elseIndex);
        List<String> falsePart = tokens.subList(elseIndex + 1, tokens.size());

        if (result != 0)
            Parser.parseAndEvaluate(truePart);
        else
            Parser.parseAndEvaluate(falsePart);
    }

    private static void handleWhile(List<String> tokens) {

        int end = tokens.indexOf(")");

        List<String> condition = tokens.subList(2, end);
        List<String> body = tokens.subList(end + 1, tokens.size());

        int safety = 0;

        while (Parser.evaluateExpressionPublic(condition) != 0) {

            Parser.parseAndEvaluate(body);

            safety++;
            if (safety > 100) break;
        }
    }
}