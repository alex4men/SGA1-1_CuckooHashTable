import java.util.Scanner;

/**
 * Created by alex on 17/09/16.
 */
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        CuckooHashTable<String> H = new CuckooHashTable<>();
        System.out.println("Enter keyword \"print\" to print current state of program. Enter \"exit\" for closing  the program. ");
        while (true) {
            System.out.print("Enter new key: ");
            if (input.hasNext("print")) {
                input.next();
                H.printOutContent();
            } else if (input.hasNext("exit")) {
                break;
            } else {
                try {
                    H.insert(input.next());
                } catch (Exception e) {
                    System.out.println("Fatal error: " + e);
                }
            }
        }
    }
}
