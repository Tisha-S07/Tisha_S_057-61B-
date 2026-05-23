import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CodeGenerator {

    static List<String> generatedCode = new ArrayList<>();

    // ---------- TRACK VARIABLES ----------
    static Set<String> declaredVariables = new HashSet<>();

    // ---------- START PROGRAM ----------
    public static void startProgram() {

        generatedCode.add(
                "public class GeneratedCode {");

        generatedCode.add(
                "public static void main(String[] args) {");
    }

    // ---------- SAFE VARIABLE DECLARATION ----------
    public static void addVariable(
            String variable,
            String expression) {

        // first declaration
        if (!declaredVariables.contains(
                variable)) {

            declaredVariables.add(
                    variable);

            generatedCode.add(
                    " double " + variable + " = " + expression + ";");
        }

        // reassignment
        else {

            generatedCode.add(
                    " " + variable + " = " + expression + ";");
        }
    }

    // ---------- ADD PRINT ----------
    public static void addPrint(
            String text) {

        generatedCode.add(
                "System.out.println(\"" + text + "\");");
    }

    // ---------- END PROGRAM ----------
    public static void endProgram() {

        generatedCode.add("}");
        generatedCode.add("}");
    }

    // ---------- SAVE FILE ----------
    public static void saveToFile() {

        try {

            try (FileWriter writer = new FileWriter(
                    "GeneratedCode.java")) {
                for (String line :
                        generatedCode) {
                    
                    writer.write(
                            line + "\n");
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