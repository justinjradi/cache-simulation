package memory_structures;
import utils.Utils;

/*
    Cache Construction:

    Fully Associative: numSets = 1, numBlocksPerSet = 1, other parameters free
    Direct Mapped: numBlocksPerSet = 1, other parameters free
    Set Associative (n-way associative): all parameters free

    2-Way Associative Cache Example (with 2 sets):

    numSets = 2, numBlocksPerSet = 2, numWordsPerBlock = 4

    | Set  | Tag  | Block[00] | Block[01] | Block[10] | Block[11] |
    | 0    | tag  | word     | word     | word     | word     |
    | 0    | tag  | word     | word     | word     | word     |
    | 1    | tag  | word     | word     | word     | word     |
    | 1    | tag  | word     | word     | word     | word     |

 */

public class Cache extends MemoryLevel
{
    int numSets;   // Must be a power of 2
    int numBlocksPerSet;    // Must be a power of 2
    int numWordsPerBlock;  // Must be a power of 2
    int tagSize;
    int setIndexSize;
    int wordOffsetSize;
    int[][][] words;    // word = words[set][block][offset]
    int[][] tags;       // tag = tags[set][block]

    public Cache(int addressSize, int wordSize, MemoryLevel nextMemoryLevel,
                  String name, int numSets, int numBlocksPerSet, int numWordsPerBlock)
    {
        super(addressSize, wordSize, nextMemoryLevel, name);
        this.numSets = numSets;
        this.numBlocksPerSet = numBlocksPerSet;
        this.numWordsPerBlock = numWordsPerBlock;
        // Check 1 or greater
        if (numSets < 1)
        {
            throw new IllegalArgumentException("Number of sets must be 1 or greater.");
        }
        if (numBlocksPerSet < 1)
        {
            throw new IllegalArgumentException("Number of blocks per set must be 1 or greater");
        }
        if (numWordsPerBlock < 1)
        {
            throw new IllegalArgumentException("Number of words per block must be 1 or greater");
        }
        // Check powers of 2
        if (!Utils.isPowerOfTwo(numSets))
        {
            throw new IllegalArgumentException("Number of sets must be a power of two");
        }
        if (!Utils.isPowerOfTwo(numWordsPerBlock))
        {
            throw new IllegalArgumentException("Number of words per block must be a power of two");
        }
        /*
            TODO:
             - Allow for numSets and numWordsPerBlock that aren't powers of two
             - Compute minimum tag size log2(numBlocksPerSet) and add check
         */
        // Calculates log2
        this.setIndexSize = Integer.numberOfTrailingZeros(numSets);
        this.wordOffsetSize = Integer.numberOfTrailingZeros(numWordsPerBlock);
        this.tagSize = this.addressSize - this.setIndexSize - this.wordOffsetSize;
        // Check tag size is greater than zero, but doesn't compare to log2(numBlocksPerSet)
        if (this.tagSize < 0)
        {
            throw new IllegalArgumentException("Address size is too small for cache parameters.");
        }
        this.words = new int[numSets][numBlocksPerSet][numWordsPerBlock];
        this.tags = new int[numSets][numBlocksPerSet];
    }

    @Override
    public int read(int address)
    {
        if (!Utils.validateSize(address, addressSize))
        {
            throw new IllegalArgumentException("Failed read because address size invalid");
        }

        // TODO
        return -1;

    }
    @Override
    public void write(int address, int value)
    {
        if (!Utils.validateSize(address, addressSize))
        {
            throw new IllegalArgumentException("Failed write because address size invalid");
        }
        if (!Utils.validateSize(value, wordSize))
        {
            throw new IllegalArgumentException("Failed write because word size invalid");
        }

        // TODO

        return;
    }
    @Override
    public void printContents()
    {
        System.out.println("Contents of Cache " + name + ":");
        // Create header
        String[][] stringTable = new String[1 + numSets * numBlocksPerSet][2 + numWordsPerBlock];
        stringTable[0][0] = "Set";
        stringTable[0][1] = "Tag";
        // Add word offsets to header
        for (int i = 0; i < numWordsPerBlock; i++)
        {
            stringTable[0][2 + i] = "Block[0b" + Integer.toBinaryString(i) + "]";
        }
        // Go through each set
        for (int setIndex = 0; setIndex < numSets; setIndex++)
        {
            // Go through each line corresponding to a block
            for (int block = 0; block < numBlocksPerSet; block++)
            {
                // Add set index to each line
                String setIndexString = "0b" + Integer.toBinaryString(setIndex);
                stringTable[1 + (setIndex * numBlocksPerSet) + block][0] = setIndexString;
                // Add tag to each line
                String tagString = "0x" + Integer.toHexString(tags[block][setIndex]);
                stringTable[1 + (setIndex * numBlocksPerSet) + block][1] = tagString;
                // Add each word within a block (going horizontally across line)
                for (int wordOffset = 0; wordOffset < numWordsPerBlock; wordOffset++)
                {
                    String wordString = "0x" + Integer.toHexString(words[setIndex][block][wordOffset]);
                    stringTable[1 + (setIndex * numBlocksPerSet) + block][2 + wordOffset] = wordString;
                }
            }
        }
        Utils.printTable(stringTable, 1 + numSets * numBlocksPerSet, 2 + numWordsPerBlock);
    }
}