import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CodeGenerator {

    static List<String> generatedCode = new ArrayList<>();

    // ---------- TRACK VARIABLES ----------
    static Set<String> declaredVariables = new HashSet<>();

    // ---------- START PROGRAM ----------
    public static void startProgram() {

        generatedCode.add("public class GeneratedCode {");
        generatedCode.add("    public static void main(String[] args) {");
    }

    // ---------- SAFE VARIABLE DECLARATION ----------
    public static void addVariable(
            String variable,
            String expression) {

        if (!declaredVariables.contains(variable)) {
            declaredVariables.add(variable);
            generatedCode.add("        double " + variable + " = " + expression + ";");
        } else {
            generatedCode.add("        " + variable + " = " + expression + ";");
        }
    }

    public static void addIf(
            List<String> condition,
            List<String> truePart,
            List<String> falsePart) {

        generatedCode.add("        if (" + expressionToString(condition) + ") {");
        appendStatement(truePart, "            ");
        generatedCode.add("        }");

        if (falsePart != null) {
            generatedCode.add("        else {");
            appendStatement(falsePart, "            ");
            generatedCode.add("        }");
        }
    }

    public static void addWhile(
            List<String> condition,
            List<String> body) {

        generatedCode.add("        while (" + expressionToString(condition) + ") {");
        appendStatement(body, "            ");
        generatedCode.add("        }");
    }

    private static void appendStatement(
            List<String> tokens,
            String indent) {

        if (tokens == null || tokens.isEmpty())
            return;

        if (tokens.size() >= 3 && tokens.get(1).equals("=")) {
            String variable = tokens.get(0);
            String expression = expressionToString(tokens.subList(2, tokens.size()));

            if (!declaredVariables.contains(variable)) {
                declaredVariables.add(variable);
                generatedCode.add(indent + "double " + variable + " = " + expression + ";");
            } else {
                generatedCode.add(indent + variable + " = " + expression + ";");
            }
        } else {
            generatedCode.add(indent + expressionToString(tokens) + ";");
        }
    }

    private static String expressionToString(List<String> tokens) {
        return String.join(" ", tokens);
    }

    // ---------- ADD PRINT ----------
    public static void addPrint(
            String text) {

        generatedCode.add(
                "System.out.println(\"" + text + "\");");
    }

    // ---------- END PROGRAM ----------
    public static void endProgram() {

        generatedCode.add("    }");
        generatedCode.add("}");
    }

    // ---------- SAVE FILE + PRINT TARGET CODE ----------
    public static void saveToFile() {

        try {

            try (FileWriter writer = new FileWriter(
                    "GeneratedCode.java")) {

                System.out.println(
                        "\n========== TARGET CODE ==========\n");

                for (String line : generatedCode) {

                    // write into java file
                    writer.write(
                            line + "\n");

                    // print target code
                    System.out.println(
                            line);
                }
            }

            System.out.println(
                    "\nGeneratedCode.java created successfully!");
        }

        catch (IOException e) {

            System.out.println(
                    "File Writing Error!");
        }
    }
}