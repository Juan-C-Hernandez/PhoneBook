package phonebook;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PhoneBook {
    private static HashMap<String, String> contacts;

    public PhoneBook() {
        contacts = new HashMap<>();
    }

    public void addContact(String name, String phoneNumber) {
        contacts.put(name, phoneNumber);
    }

    public String search(String name) {
        return contacts.get(name);
    }

    public void importContacts(String fileName) {
        Path path = Paths.get(fileName);

        try (FileReader fileReader = new FileReader(fileName)) {
            Files.readAllLines(path)
                .stream().map(line -> Arrays.asList(line.split("\\s+", 2)))
                .forEach(list -> addContact(list.get(1), list.get(0)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportContacts(String exportFileName) {
        try (FileWriter fileWriter = new FileWriter(exportFileName)) {
            for (Map.Entry entry : contacts.entrySet()) {
                fileWriter.write(entry.getKey() + " ");
                fileWriter.write(entry.getValue() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total :" + contacts.size()).append(System.lineSeparator());
        String ln = System.lineSeparator();
        int i = 0;
        for (Map.Entry entry : contacts.entrySet()) {
            sb.append((i + 1) + ": ").append(ln);
            sb.append("Name: ").append(entry.getKey()).append(ln);
            sb.append("Phone Number: ").append(entry.getValue()).append(ln);
        }

        return sb.toString();
    }
}