package core.basesyntax.services.impl;

import core.basesyntax.services.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReaderImpl implements Reader {
    @Override
    public List<String> read(String filePath) {
        final String correctData = "type,fruit,quantity";
        List<String> list = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new FileReader(filePath))) {
            String header = bf.readLine();
            if (header == null) {
                throw new IOException("File is empty");
            }
            if (!header.equals(correctData)) {
                throw new IOException("Data is incorrect");
            }
            String line = bf.readLine();
            while (line != null) {
                list.add(line);
                line = bf.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file: " + filePath, e);
        }
        return list;
    }
}
