import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CodeGenerator {

        static List<String> generatedCode = new ArrayList<>();

        // ---------- ADDED FIX: TRACK VARIABLES ----------
        static Set<String> declaredVariables = new HashSet<>();

        // ---------- START PROGRAM ----------

        public static void startProgram() {

                generatedCode.add(
                                "public class GeneratedCode {");

                generatedCode.add(
                                "public static void main(String[] args) {");
        }

        // ---------- ADD GENERATED LINE ----------

        public static void addLine(
                        String line) {

                generatedCode.add(
                                "    " + line);
        }

        // ---------- SAFE VARIABLE DECLARATION (FIX) ----------

        public static void addVariable(
                        String variable,
                        String expression) {

                // first time -> declare
                if (!declaredVariables.contains(variable)) {

                        declaredVariables.add(variable);

                        generatedCode.add(
                                        "    double " +
                                                        variable +
                                                        " = " +
                                                        expression +
                                                        ";");
                }

                // later -> only assign (NO duplicate declaration)
                else {

                        generatedCode.add(
                                        "    " +
                                                        variable +
                                                        " = " +
                                                        expression +
                                                        ";");
                }
        }

        // ---------- ADD PRINT LINE ----------

        public static void addPrint(
                        String text) {

                generatedCode.add(
                                "System.out.println(\"" +
                                                text +
                                                "\");");
        }

        // ---------- IF GENERATION ----------

        public static void startIf(
                        String condition) {

                generatedCode.add(
                                "if(" +
                                                condition +
                                                ") {");
        }

        // ---------- ELSE GENERATION ----------

        public static void startElse() {

                generatedCode.add(
                                "else {");
        }

        // ---------- WHILE GENERATION ----------

        public static void startWhile(
                        String condition) {

                generatedCode.add(
                                "while(" +
                                                condition +
                                                ") {");
        }

        // ---------- BLOCK END ----------

        public static void endBlock() {

                generatedCode.add("}");
        }

        // ---------- END PROGRAM ----------

        public static void endProgram() {

                generatedCode.add("}");

                generatedCode.add("}");
        }

        // ---------- SAVE FILE ----------

        public static void saveToFile() {

                try {

                        FileWriter writer = new FileWriter(
                                        "GeneratedCode.java");

                        for (String line : generatedCode) {

                                writer.write(
                                                line + "\n");
                        }

                        writer.close();

                        System.out.println(
                                        "\nGeneratedCode.java created successfully!");
                }

                catch (IOException e) {

                        System.out.println(
                                        "File Writing Error!");
                }
        }
}