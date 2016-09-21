import java.util.Scanner;

/**
 * Created by alex on 17/09/16.
 */
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        CuckooHashTable<String> H = new CuckooHashTable<>();

        while (true) {
            System.out.print("Enter something: ");
            if (input.hasNext("print")) {
                input.next();
                H.printOutContent();
            } else if (input.hasNext("exit")) {
                break;
            } else {
                H.insert(input.next());
            }
        }
    }
}
