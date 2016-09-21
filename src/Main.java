import java.util.Scanner;

/**
 * Created by alex on 17/09/16.
 */
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        CuckooHashTable<String> H = new CuckooHashTable<>(100);

        while (true) {
            System.out.println("Enter something: ");
            H.insert(input.next());
        }
    }
}
