import java.io.*;
import java.util.Collections;
import java.util.LinkedList;

public class WorkWithIO {

    private static String[] readFile() {
        try (BufferedReader in = new BufferedReader(
                new FileReader("Results.txt"))) {
            return in.readLine().split(" ");
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }

    public static LinkedList<Integer> read() {
        LinkedList<Integer> list = new LinkedList<>();
        String[] s = readFile();
        if (s != null) {
            for (String value : s) {
                try {
                    list.add(Integer.parseInt(value));
                } catch (NullPointerException e) {
                    list = new LinkedList<>(Collections.nCopies(10, 0));
                    break;
                } catch (NumberFormatException ignored) {
                }
            }
            return list;
        }
        return null;
    }

    private static void write(final String numbers) {
        try (BufferedWriter out = new BufferedWriter(
                new FileWriter("Results.txt"))) {
            out.write(numbers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(final LinkedList<Integer> list) {
        StringBuilder str = new StringBuilder();
        for (Integer integer : list) {
            str.append(integer).append(" ");
        }
        String res = str.toString();
        write(res);
    }
}
