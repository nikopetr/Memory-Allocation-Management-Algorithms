import java.util.ArrayList;
import java.util.TreeSet;

// Class used in order to put processes in the Tree set(red black tree)
// stored in ascending order compared to their available memory space
class BlockInWorstFit implements Comparable<BlockInWorstFit>{
    private int id; // The id of the block
    private int availableSpace; // The available memory space of the block

    // Constructor of the Block class
    // Takes two integers as parameters, one for the id and one for the available space of the block
    BlockInWorstFit(int id, int availableSpace){
        this.id = id;
        this.availableSpace = availableSpace;
    }

    // Returns the id of the block
    Integer getId() {
        return id;
    }

    // Returns the available space of the block
    Integer getAvailableSpace() {
        return availableSpace;
    }

    // Overriding the compareTo method in order to have the blocks of the tree set being compared by their available space
    // In case their available space is equal, the process with the bigger id will go first in the tree set,
    // since we want to get the lower ids first and we will be using the last() method.
    @Override
    public int compareTo(BlockInWorstFit block) {
        if (this.getAvailableSpace().compareTo(block.getAvailableSpace()) == 0)
            return block.getId().compareTo(this.getId());
        else
            return this.getAvailableSpace().compareTo(block.getAvailableSpace());
    }
}

public class WorstFit
{
    /*
     * Method to allocate memory to blocks according to the worst fit
     * algorithm. It should return an ArrayList of Integers, where the
     * index is the process ID (zero-indexed) and the value is the block
     * number (also zero-indexed).
     */
    static ArrayList<Integer> worstFit(int sizeOfBlocks[], int sizeOfProcesses[])
    {
        // In this implementation of the worst-fit algorithm,
        // the BlockInWorstFit objects in the TreeSet are stored in ascending order according to their
        // natural order (which in our case is the size of the block).
        // The TreeSet uses a self-balancing binary search tree, more specifically a Red-Black tree.
        // In other words, in a self-balancing binary search tree each node of the tree comprises of an
        // extra bit, which is used to identify the color of the node which is either red or black.
        // During insertions and deletions, these “color” bits are used in order to ensure
        // that the tree remains more or less balanced (similar to an avl tree with its rotations).
        // That way, this implementation provides guaranteed O(log(n)) time cost for the basic
        // operations (add, remove and search), according to the javadoc of TreeSet,
        // https://docs.oracle.com/javase/8/docs/api/java/util/TreeSet.html

        // Finally, with this implementation the overall time complexity will be O(nlogn).

        int n = sizeOfProcesses.length; // Number of total processes
        int m = sizeOfBlocks.length; // Number of total blocks in memory

        // Holds the id of the block allocated to the processes
        ArrayList<Integer> memoryAllocation = new ArrayList<>();

        // Initializes the Array list with "-255" which means that the process is not allocated at the start
        for (int i = 0; i < n; i++)
            memoryAllocation.add(-255);

        // The tree set (which is a red black tree) that contains the
        // blocks of the memory stored according to their available memory space,
        TreeSet<BlockInWorstFit> blocks = new TreeSet<>();

        // Initializes the set with the blocks (id and total size in KBs)
        for (int i = 0; i < m; i++)
            blocks.add(new BlockInWorstFit(i, sizeOfBlocks[i]));

        // Finds the biggest block for each process
        for (int i = 0; i < n; i++)
        {
            // last() method will return the "biggest" element in this set,
            // (In case two blocks have the same size the one with the smaller id will be returned since
            // we implemented the BlockInWorstFit' Class compareTo that way)
            BlockInWorstFit biggestBlock = blocks.last();

            // If a block that the current process fits was found
            if (biggestBlock.getAvailableSpace() >= sizeOfProcesses[i])
            {
                int biggestBlockId = biggestBlock.getId(); // The id of the block with the biggest size
                int biggestBlockAvailableSpace = biggestBlock.getAvailableSpace(); // The available space of the block with the biggest size

                memoryAllocation.set(i, biggestBlockId); // Allocates the block found for the process

                // Reduces available memory in that block by removing the previous Block object from the tree set and
                // adding a new Block with the same id and with the reduced available memory.
                BlockInWorstFit blockAfterAllocation = new BlockInWorstFit(biggestBlockId, biggestBlockAvailableSpace - sizeOfProcesses[i]);
                blocks.remove(biggestBlock);
                blocks.add(blockAfterAllocation);
            }
        }

        return memoryAllocation;
    }

    // Method to print the memory allocation
    public static void printMemoryAllocation(ArrayList<Integer> memAllocation) {
        System.out.println("Process No.\tBlock No.");
        System.out.println("===========\t=========");
        for (int i = 0; i < memAllocation.size(); i++)
        {
            System.out.print(" " + i + "\t\t");
            // if a process has been allocated position -255, it means that it
            // has not been actually allocated
            if (memAllocation.get(i) != -255)
                System.out.print(memAllocation.get(i));
            else
                System.out.print("Not Allocated");
            System.out.println();
        }
    }

    // Driver Method to test your algorithm with a simple example
    public static void main(String[] args)
    {
        /* There are 5 available blocks in this example. The block ID
         * is the array index and the available block size is the value.
         * So we have the following blocks and sizes:
         *
         *   BlockID    Size
         *   =======    ====
         *      0        200
         *      1        500
         *      2        100
         *      3        300
         *      4        600
         *
         */
        int sizeOfBlocks[] = {200, 500, 100, 300, 600};
        /* And there are 4 processes. The process ID is the array
         * index. So we have these processes and sizes:
         *
         *   ProcessID     Size
         *   =========     ====
         *       0          214
         *       1          415
         *       2          112
         *       3          425
         */
        int sizeOfProcesses[] = {214, 415, 112, 425};

        ArrayList<Integer> memAlloc = worstFit(sizeOfBlocks, sizeOfProcesses);
        printMemoryAllocation(memAlloc);
    }
} 