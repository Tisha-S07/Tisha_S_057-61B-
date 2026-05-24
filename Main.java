import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        try {
    System.setOut(
        new java.io.PrintStream(
            new java.io.FileOutputStream("output.txt", false)
        )
    );
} catch (java.io.FileNotFoundException e) {
    System.out.println("Output redirection failed");
}
OutputLogger.init();
        CodeGenerator.startProgram();

        String[] program = {

                "ক=৫+৩",
                "খ=১০-৪",
                "গ=৫*৬",
                "ঘ=২০/৫",

                "ঙ=৫+৬*৩",
                "চ=(৫+৬)*৩",

                "ছ=((৫+২)*(৩+৪))",
                "জ=((২+৩)*(৪+(৫*২)))",

                "ক=৫",
                "খ=১০",
                "গ=ক+খ",
                "ঘ=ক+খ*২",

                "ক=১৫",
                "খ=ক+৫",

                "চ=((ক+খ)*(৩+(২*৪)))",

                "if (ক<খ) ক=100 else ক=200",
                "while (ক<205) ক=ক+1",

                "ছ=৫+",
                "জ=৫++২",
                "ঝ=(৫+২",
                "ঞ=৫+২)",
                "ট=৫(২+৩)",
                "ঠ=অ+৫",
                "ড=১০/(৫-৫)",
                "ঢ=(())",
                "ণ=৫+@",
                "ত=(৫+*)২"
        };

        for (String line : program) {

            System.out.println("\n================================");
            System.out.println("Source Code: " + line);

            try {

                List<String> tokens = Lexer.tokenize(line);

                System.out.println("Tokens: " + tokens);

                if (tokens.get(0).equals("if") || tokens.get(0).equals("while")) {
                    ControlFlowDispatcher.handle(tokens);
                } else {
                    Parser.parseAndEvaluate(tokens);
                }

            } catch (Exception e) {
                //System.out.println("Recovered From Error...");
            }
        }

        TACGenerator.printTAC();
        TACGenerator.saveToFile();

        Parser.printSymbolTable();

        CodeGenerator.endProgram();
        CodeGenerator.saveToFile();

        System.out.println("\nCompiler Finished Successfully!");
    }
}