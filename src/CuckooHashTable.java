import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 19/09/16.
 */

// ******************PUBLIC OPERATIONS*********************
// bool insert(x)        --> Insert x
// bool remove(x)        --> Remove x
// bool contains(x)      --> Return true if x is present
// void clearTable()     --> Remove all items
// int  size()           --> Return number of items

public class CuckooHashTable<Key> {

    private static final int numOfArrays = 2;

    private static final double maxLoadFactor = 0.50;

    private int rehashThrshld;
    private final int maxRehashesThrshld = 5;

    private int[] hashMultipliers = {1, 3};
    private int currentTableSize = 5;

    private Key[][] array; // The array of array of elements
    private int[] occupiedCellsCount; // The number of occupied cells

    public CuckooHashTable() {
        array = (Key[][]) new Object[numOfArrays][currentTableSize];
        occupiedCellsCount = new int[numOfArrays];
        rehashThrshld = log2(currentTableSize);
        clearTable();
    }

    public boolean insert(Key x) {
        if (contains(x)) return false;

        Key keyToInsert = x;
        int i = 0;
        int insertAttempts = 0;
        int rehashesAttempts = 0;

        while (true) {
            if (occupiedCellsCount[0] >= array[0].length * maxLoadFactor ||
                    occupiedCellsCount[1] >= array[1].length * maxLoadFactor || rehashesAttempts >= maxRehashesThrshld) {
                expand();
                insertAttempts = 0;
                rehashesAttempts = 0;
            }
            if (insertAttempts >= rehashThrshld) {
                changeHashFunctions();
                insertAttempts = 0;
                rehashesAttempts++;
            }
            if (array[i % numOfArrays][hash(keyToInsert, i % numOfArrays)] == null) {
                array[i % numOfArrays][hash(keyToInsert, i % numOfArrays)] = keyToInsert;
                occupiedCellsCount[i % numOfArrays]++;

                // debug
                System.out.println("Inserted " + keyToInsert + " in " + i % numOfArrays + "." + hash(keyToInsert, i % numOfArrays) + " array");

                return true;
            } else {
                Key oldKey = array[i % numOfArrays][hash(x, i % numOfArrays)];
                array[i % numOfArrays][hash(x, i % numOfArrays)] = keyToInsert;

                // debug
                System.out.println("Inserted " + keyToInsert + " in " + i % numOfArrays + "." + hash(keyToInsert, i % numOfArrays) + " array with kicking " + oldKey);

                keyToInsert = oldKey;
                insertAttempts++;
                i++;
            }
        }
    }

    public void printOutContent() {
        System.out.println("Current table size:   " + currentTableSize);
        System.out.println("Total cells occupied: " + totalKeysInTable());
        System.out.println("Hash multipliers:     " + hashMultipliers[0] + ", " + hashMultipliers[1]);
        System.out.println("Table's content:");
        System.out.println("  1st array |   2nd array");
        for (int i = 0; i < currentTableSize; i++) {
            System.out.format("%11s | %11s%n", array[0][i], array[1][i]);
        }
    }

    public boolean contains(Key x) {
        return findPos(x) != -1;
    }

    public void clearTable() {
        for (int j = 0; j < numOfArrays; j++) {
            occupiedCellsCount[j] = 0;
            for (int i = 0; i < array[j].length; i++) {
                array[j][i] = null;
                array[j][i] = null;
            }
        }
    }

    public int totalKeysInTable() {
        return occupiedCellsCount[0] + occupiedCellsCount[1];
    }

    public boolean remove(Key x) {
        int pos = findPos(x);

        if (pos != -1) {
            array[pos / array.length][pos % numOfArrays] = null;
            occupiedCellsCount[pos / array.length]--;
        }

        return pos != -1;
    }

    private void expand() {
        currentTableSize = nextPrime(currentTableSize * 2);

        // debug
        System.out.println("New table array size = " + currentTableSize);

        hashMultipliers[0] = 1;
        hashMultipliers[1] = 3;
        rehash();
    }

    private void changeHashFunctions() {
        hashMultipliers[0] = nextPrime(hashMultipliers[0]);
        hashMultipliers[1] = nextPrime(hashMultipliers[1]);

        // debug
        System.out.println("New hash Multipliers: " + hashMultipliers[0] + ", " + hashMultipliers[1]);

        rehash();
    }

    private int hash(Key x, int arrayNum) {
        return ((x.hashCode() & 0x7fffffff) * hashMultipliers[arrayNum]) % currentTableSize;
    }

    private void rehash() {
        Key[][] oldTable = array;
        array = (Key[][]) new Object[numOfArrays][currentTableSize];
        clearTable();
        rehashThrshld = log2(currentTableSize);
        for (int i = 0; i < numOfArrays; i++) {
            for (int j = 0; j < oldTable[i].length; j++) {
                if (oldTable[i][j] != null) insert(oldTable[i][j]);
            }
        }

        // debug
        System.out.println("Rehashed");
    }

    private int nextPrime(int x) {
        List<Integer> primes = new ArrayList<>();
        // start from 2
        OUTERLOOP:
        for (int i = 2; true; i++) {
            // try to divide it by all known primes
            for (Integer p : primes)
                if (i % p == 0)
                    continue OUTERLOOP; // i is not prime

            // i is prime
            primes.add(i);
            if (i > x) return i;
        }
    }

    private int findPos(Key x) {
        for (int i = 0; i < numOfArrays; i++) {
            int pos = hash(x, i);
            if (array[i][pos] != null && array[i][pos].equals(x)) {
                int tmp = i + 1;
                System.out.println("Position is: " + tmp + "." + pos);
                return (i + 1) * pos;
            }
        }

        return -1;
    }

    private int log2(int x) {
        return (int) (Math.log(x) / Math.log(2));
    }
}