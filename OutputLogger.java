import java.io.FileWriter;
import java.io.IOException;

public class OutputLogger {

    private static FileWriter writer;

    // initialize file
    public static void init() {
        try {
            writer = new FileWriter("output.txt", false);
        } catch (IOException e) {
            System.out.println("Cannot open output file!");
        }
    }

    // write line to file
    public static void log(String text) {
        try {
            writer.write(text + "\n");
        } catch (IOException e) {
            System.out.println("Write error!");
        }
    }

    // close file
    public static void close() {
        try {
            if (writer != null)
                writer.close();
        } catch (IOException e) {
            System.out.println("Close error!");
        }
    }
}