import java.io.*;
import java.util.*;

public class StorageManager {

    public List<String[]> readCSV(String path) {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    rows.add(line.split(","));
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + path);
        }

        return rows;
    }
}
