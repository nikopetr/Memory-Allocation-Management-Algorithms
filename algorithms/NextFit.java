import java.util.ArrayList;

public class NextFit
{
    /*
     * Method to allocate memory to blocks according to the next fit
     * algorithm. It should return an ArrayList of Integers, where the
     * index is the process ID (zero-indexed) and the value is the block
     * number (also zero-indexed).
     */
    static ArrayList<Integer> nextFit(int sizeOfBlocks[], int sizeOfProcesses[])
    {
        int n = sizeOfProcesses.length; // Number of total processes
        int m = sizeOfBlocks.length; // Number of total blocks in memory

        // Holds the id of the block allocated to the processes
        ArrayList<Integer> memoryAllocation = new ArrayList<>();

        // Initializes the Array list with "-255" which means that the process is not allocated at the start
        for (int i = 0; i < n; i++)
            memoryAllocation.add(-255);

        // The id of the block that the algorithm starts searching from on each iteration
        // Starting from 0 since no process is allocated at the start
        int blockPointer = 0;

        // Running through every process and allocating a block for each on of them
        for (int i = 0; i < n; i++)
        {
            int blocksChecked = 0; // Total blocks checked for this process
            boolean blockFound = false;

            // Finds the first block that the current process fits starting
            // from the block that the last process was allocated to. If the process can't fit in any of the blocks then
            // no block id saved in the memoryAllocation arraylist and the iteration continues from where it left before
            while(!blockFound && blocksChecked < m)
            {
                if (sizeOfBlocks[blockPointer] >= sizeOfProcesses[i]) // Checks if the current process fits in this memory block
                {
                    // If a block that the current process fits was found,
                    // allocates the block found for the process with id i and reduces available memory in that block
                    memoryAllocation.set(i, blockPointer);
                    sizeOfBlocks[blockPointer] -= sizeOfProcesses[i];
                    blockFound = true;
                }

                else  // Else continue searching for a block
                {
                    // Increment block pointer, (mod with the total blocks,
                    // since we want to check the top blocks as well in case the pointer reaches the bottom block)
                    blockPointer = (blockPointer + 1) % m;

                    // Increment the blocks checked for the current process,
                    blocksChecked++;
                }
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

        ArrayList<Integer> memAlloc = nextFit(sizeOfBlocks, sizeOfProcesses);
        printMemoryAllocation(memAlloc);
    }
}