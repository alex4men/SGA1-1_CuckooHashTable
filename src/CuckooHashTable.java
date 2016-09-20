import java.util.Random;

/**
 * Created by alex on 19/09/16.
 */

// Cuckoo Hash table class
//
// CONSTRUCTION: a hashing function family and
//               an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// bool insert( x )       --> Insert x
// bool remove( x )       --> Remove x
// bool contains( x )     --> Return true if x is present
// void clearTable( )      --> Remove all items
// int  size( )           --> Return number of items

public class CuckooHashTable<K>
{

    public CuckooHashTable(int tableSize)
    {
        array = (K[][]) new Object[NUM_OF_ARRAYS][tableSize];
        currentSize = new int[NUM_OF_ARRAYS];
        clearTable( );
    }

    private Random r = new Random( );

    private static final int NUM_OF_ARRAYS = 2;
    private static final double MAX_LOAD = 0.50;
    private static final int ALLOWED_REHASHES = 1;

    private int rehashes = 0;

//    private boolean insertHelper1(K x)
//    {
//        final int COUNT_LIMIT = 100;
//
//        while( true )
//        {
//            int lastPos = -1;
//            int pos;
//
//            for( int count = 0; count < COUNT_LIMIT; count++ )
//            {
//                for( int i = 0; i < numHashFunctions; i++ )
//                {
//                    pos = myhash( x, i );
//
//                    if( array[ pos ] == null )
//                    {
//                        array[ pos ] = x;
//                        currentSize++;
//                        return true;
//                    }
//                }
//
//                // none of the spots are available. Kick out a random one
//                int i = 0;
//                do
//                {
//                    pos = myhash( x, r.nextInt( numHashFunctions ) );
//                } while( pos == lastPos && i++ < 5 );
//
//                K tmp = array[ lastPos = pos ];
//                array[ pos ] = x;
//                x = tmp;
//            }
//
//            if( ++rehashes > ALLOWED_REHASHES )
//            {
//                expand( );      // Make the table bigger
//                rehashes = 0;
//            }
//            else
//                rehash( );
//        }
//    }
//
//    private boolean insertHelper2(K x)
//    {
//        final int COUNT_LIMIT = 100;
//
//        while( true )
//        {
//            for( int count = 0; count < COUNT_LIMIT; count++ )
//            {
//                int pos = myhash( x, count % numHashFunctions );
//
//                K tmp = array[ pos ];
//                array[ pos ] = x;
//
//                if( tmp == null )
//                    return true;
//                else
//                    x = tmp;
//            }
//
//            if( ++rehashes > ALLOWED_REHASHES )
//            {
//                expand( );      // Make the table bigger
//                rehashes = 0;
//            }
//            else
//                rehash( );
//        }
//    }

    /**
     * Insert into the hash table. If the item is
     * already present, return false.
     * @param x the item to insert.
     */
    public boolean insert(K x)
    {
        if(contains(x))
            return false;

        if(currentSize[0] >= array[0].length * MAX_LOAD || currentSize[1] >= array[1].length * MAX_LOAD)
            expand( );

        if (array[0][hash(x, 0)] == null) {
            array[0][hash(x, 0)] = x;
            currentSize[0]++;
            System.out.println("Inserted in 1 array");
        } else if (array[1][hash(x, 1)] == null) {
            array[1][hash(x, 1)] = x;
            currentSize[1]++;
            System.out.println("Inserted in 2 array");
        } else return false;

        return true;
    }

    private int hash(K x, int arrayNum)
    {
        if (arrayNum == 0) {
            int hashVal = x.hashCode();
            hashVal = (hashVal & 0x7fffffff) % array[arrayNum].length;

            return hashVal;
        } else {
            int hashVal = x.hashCode();
            hashVal = (hashVal & 0x7fffffff) % array[arrayNum].length;

            return hashVal;
        }
    }

    private void expand( )
    {
//        rehash( (int) ( array.length / MAX_LOAD ) );
    }
//
//    private void rehash( )
//    {
//        rehash( array.length );
//    }

//    private void rehash( int newLength )
//    {
//        //System.out.println( "REHASH: " + array.length + " " + newLength + " " + currentSize );
//        K[] oldArray = array;    // Create a new double-sized, empty table
//
//        currentSize = 0;
//
//        // Copy table over
//        for( K str : oldArray )
//            if( str != null )
//                insert( str );
//    }


    /**
     * Gets the size of the table.
     * @return number of items in the hash table.
     */
    public int size( )
    {
        return currentSize[0] + currentSize[1];
    }

    /**
     * Gets the length (potential capacity) of the table.
     * @return length of the internal array in the hash table.
     */
    public int capacity( )
    {
        return array[0].length + array[1].length;
    }

    /**
     * Method that searches all hash function places.
     * @param x the item to search for.
     * @return the position where the search terminates, or -1 if not found.
     */
    private int findPos( K x )
    {
        for (int i = 0; i < NUM_OF_ARRAYS; i++) {
            int pos = hash(x, i);
            if (array[i][pos] != null && array[i][pos].equals(x)) {
                System.out.println("Position is: " + i + "." + pos);
                return (i + 1) * pos;
            }
        }

        return -1;
    }

    /**
     * Remove from the hash table.
     * @param x the item to remove.
     * @return true if item was found and removed
     */
    public boolean remove( K x )
    {
        int pos = findPos( x );

        if( pos != -1 )
        {
            array[pos / array.length][pos % NUM_OF_ARRAYS] = null;
            currentSize[pos / array.length]--;
        }

        return pos != -1;
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return the matching item.
     */
    public boolean contains( K x )
    {
        return findPos( x ) != -1;
    }

    /**
     * Make the hash table logically empty.
     */
    public void clearTable( )
    {
        currentSize[0] = 0;
        currentSize[1] = 0;
        for( int i = 0; i < array.length; i++ ) {
            array[0][i] = null;
            array[1][i] = null;
        }
    }

    private K[][] array; // The array of array of elements
    private int[] currentSize; // The number of occupied cells

}