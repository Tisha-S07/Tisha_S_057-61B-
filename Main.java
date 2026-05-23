import java.util.*;

public class Main {

        public static void main(
                        String[] args) {

                // ---------- START CODE GENERATION ----------

                CodeGenerator.startProgram();

                String[] program = {

                                // ---------- BASIC OPERATIONS ----------

                                "ক=৫+৩",
                                "খ=১০-৪",
                                "গ=৫*৬",
                                "ঘ=২০/৫",

                                // ---------- OPERATOR PRECEDENCE ----------

                                "ঙ=৫+৬*৩",
                                "চ=(৫+৬)*৩",

                                // ---------- NESTED BRACKETS ----------

                                "ছ=((৫+২)*(৩+৪))",

                                "জ=((২+৩)*(৪+(৫*২)))",

                                // ---------- VARIABLES ----------

                                "ক=৫",

                                "খ=১০",

                                "গ=ক+খ",

                                "ঘ=ক+খ*২",

                                "ঙ=((ক+২)*(খ-৩))",

                                // ---------- REASSIGNMENT ----------

                                "ক=১৫",

                                "খ=ক+৫",

                                // ---------- COMPLEX MIX ----------

                                "চ=((ক+খ)*(৩+(২*৪)))",

                                // ---------- ERROR CASES ----------

                                "ছ=৫+", // ends with operator

                                "জ=৫++২", // double operator

                                "ঝ=(৫+২", // missing bracket

                                "ঞ=৫+২)", // extra bracket

                                "ট=৫(২+৩)", // missing operator

                                "ঠ=অ+৫", // undefined variable

                                "ড=১০/(৫-৫)", // division by zero

                                "ঢ=(())", // empty brackets

                                "ণ=৫+@", // invalid token

                                "ত=(৫+*)২" // invalid expression
                };

                // ---------- RUN PROGRAM ----------

                for (String line : program) {

                        System.out.println(

                                        "\n================================");

                        System.out.println(

                                        "Source Code: " + line);

                        try {

                                // tokenize

                                List<String> tokens = Lexer.tokenize(line);

                                // show tokens

                                System.out.println(

                                                "Tokens: " + tokens);

                                // parse + evaluate

                                Parser.parseAndEvaluate(
                                                tokens);
                        }

                        // ---------- ERROR RECOVERY ----------

                        catch (Exception e) {

                                System.out.println(

                                                "Recovered From Error...");
                        }
                }

                // ---------- PRINT SYMBOL TABLE ----------

                Parser.printSymbolTable();

                // ---------- FINISH CODE GENERATION ----------

                CodeGenerator.endProgram();

                // ---------- SAVE GENERATED FILE ----------

                CodeGenerator.saveToFile();

                System.out.println(

                                "\nCompiler Finished Successfully!");
        }
}