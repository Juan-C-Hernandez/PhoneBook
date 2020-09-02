package phonebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class Main {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("/home/asjkt/Descargas/directory.txt");
        List<String> namesToSearch = Files.readAllLines(Path.of("/home/asjkt/Descargas/find.txt"));

        Contact[] contacts = Files.readAllLines(path)
                .stream().map(line -> Arrays.asList(line.split("\\s+", 2)))
                .map(Contact::new).toArray(Contact[]::new);

        System.out.println("Start searching...");
        int found = 0;
        long startLinear = System.currentTimeMillis();
        for (String name : namesToSearch) {
            if (linearSearch(contacts, name) >= 0) {
                found++;
            }
        }
        long totalLinear = System.currentTimeMillis() - startLinear;
        System.out.print("Found " + found + " / 500 entries. ");
        System.out.println("Time taken: " +     millisToString(totalLinear));

        System.out.println();
        System.out.println("Start searching (bubble sort + jump search)...");
        found = 0;
        contacts = Files.readAllLines(path)
                .stream().map(line -> Arrays.asList(line.split("\\s+", 2)))
                .map(Contact::new).toArray(Contact[]::new);

        long sortingStart = System.currentTimeMillis();
        boolean finishedSorting = bubbleSort(contacts, totalLinear * 10);
        long sortingTotal = System.currentTimeMillis() - sortingStart;

        BiFunction<Contact[], String, Integer> searchFunction = finishedSorting ? Main::jumpSearch : Main::linearSearch;

        long searchingTime = System.currentTimeMillis();
        for (String name : namesToSearch) {
            if (searchFunction.apply(contacts, name) >= 0) {
                found++;
            }
        }
        long searchingTimeTotal = System.currentTimeMillis() - searchingTime;

        System.out.print("Found " + found + " / 500 entries. ");
        System.out.println("Time taken: " +     millisToString(sortingTotal + searchingTimeTotal));
        System.out.print("Sorting time: " + millisToString(sortingTotal));
        System.out.println(!finishedSorting ? " - STOPPED, moved to linear search" : "");
        System.out.println("Searching time: " + millisToString(searchingTimeTotal));

        System.out.println(System.lineSeparator() + "Start searching (quick sort + binary search)...");
        sortingStart = System.currentTimeMillis();
        quickSort(contacts, 0, contacts.length - 1);
        sortingTotal = System.currentTimeMillis() - sortingStart;

        found = 0;
        searchingTime = System.currentTimeMillis();
        for (String name : namesToSearch) {
            if (binarySearch(contacts, name) >= 0) {
                found++;
            }
        }
        searchingTimeTotal = System.currentTimeMillis() - searchingTime;

        System.out.print("Found " + found + " / 500 entries. ");
        System.out.println("Time taken: " +     millisToString(sortingTotal + searchingTimeTotal));
        System.out.println("Sorting time: " + millisToString(sortingTotal));
        System.out.println("Searching time: " + millisToString(searchingTimeTotal));

        System.out.println(System.lineSeparator() + "Start searching (hash table)...");
        PhoneBook phoneBook = new PhoneBook();
        sortingStart = System.currentTimeMillis();
        phoneBook.importContacts("/home/asjkt/Descargas/directory.txt");
        sortingTotal = System.currentTimeMillis() - sortingStart;
        found = 0;
        searchingTime = System.currentTimeMillis();
        for (String name : namesToSearch) {
            if (phoneBook.search(name) != null) {
                found++;
            }
        }
        searchingTimeTotal = System.currentTimeMillis() -searchingTime;
        System.out.print("Found " + found + " / 500 entries. ");
        System.out.println("Time taken: " +     millisToString(sortingTotal + searchingTimeTotal));
        System.out.println("Creating time: " + millisToString(sortingTotal));
        System.out.println("Searching time: " + millisToString(searchingTimeTotal));

    }

    public static String millisToString(long millis) {
        int sec = (int) (millis / 1000) % 60;
        int min = (int) (millis / 1000) / 60;
        int mil = (int) (millis % 1000);

        return min + " min. " + sec + " sec. " + mil + " ms.";
    }

    public static boolean bubbleSort(Contact[] contacts, long maxTime) {
        long start = System.currentTimeMillis();
        long total = 0;
        for (int i = 0; i < contacts.length - 1; i++) {
            for (int j = 0; j < contacts.length - 1 - i; j++) {
                if (contacts[j].compareTo(contacts[j + 1]) > 0) {
                    Contact tmp = contacts[j];
                    contacts[j] = contacts[j + 1];
                    contacts[j + 1] = tmp;
                }
            }
            total = System.currentTimeMillis() - start;
            if (total > maxTime) {
                return false;
            }
        }
        return true;
    }

    public static int jumpSearch(Contact[] contacts, String name) {
        if (contacts.length == 0) {
            return -1;
        }

        if (contacts[0].getName().compareToIgnoreCase(name) == 0) {
            return 0;
        }

        int right = 0;
        int left = 0;
        int blockSize = (int) Math.sqrt(contacts.length);

        while (right < contacts.length - 1) {
            right = Math.min(contacts.length - 1, right + blockSize);
            if (contacts[right].getName().compareToIgnoreCase(name) >= 0) {
                break;
            }

            left = right;
        }

        if (contacts[right].getName().compareToIgnoreCase(name) > 0) {
            return -1;
        }

        for (int i = right; i > left; i--) {
            if (contacts[right].getName().compareToIgnoreCase(name) == 0) {
                return i;
            }
        }

        return -1;
    }

    public static int linearSearch(Contact[] contacts, String name) {
        if (contacts.length == 0) {
            return -1;
        }
        for (int i = 0; i < contacts.length; i++) {
            if (contacts[i].getName().compareToIgnoreCase(name) == 0) {
                return i;
            }
        }
        return -1;
    }

    public static void quickSort(Contact[] contacts, int left, int right) {
        if (left < right) {
            int partitionIndex = partition(contacts, left, right);
            quickSort(contacts, left, partitionIndex - 1);
            quickSort(contacts, partitionIndex + 1, right);
        }
    }

    public static int partition(Contact[] contacts, int left, int right) {
        Contact pivot = contacts[right];
        int partitionIndex = left;
        for (int i = left; i < right; i++) {
            if (contacts[i].compareTo(pivot) < 0) {
                swap(contacts, i, partitionIndex);
                partitionIndex++;
            }
        }

        swap(contacts, partitionIndex, right);
        return partitionIndex;
    }

    public static void swap(Contact[] contacts, int i, int j) {
        Contact tmp = contacts[i];
        contacts[i] = contacts[j];
        contacts[j] = tmp;
    }

    public static int binarySearch(Contact[] contacts, String name) {
        if (contacts.length == 0) {
            return -1;
        }

        int left = 0;
        int right = contacts.length - 1;

        while (left <= right) {
            int mid = (left + right) >>> 1;
            int comp = name.compareToIgnoreCase(contacts[mid].getName());
            if (comp == 0) {
                return mid;
            } else if(comp < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return - 1;
    }

}