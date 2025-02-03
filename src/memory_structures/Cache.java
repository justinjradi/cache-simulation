package memory_structures;
import utils.Utils;

import java.util.HashMap;

/*
    Example with numSets = 2, numBlocksPerSet = 2, numWordsPerBlock = 4

    | Set  | Tag  | Block[00] | Block[01] | Block[10] | Block[11] |
    | 0    | tag  | word     | word     | word     | word     |
    | 0    | tag  | word     | word     | word     | word     |
    | 1    | tag  | word     | word     | word     | word     |
    | 1    | tag  | word     | word     | word     | word     |

    Each cache maps to many sets
    Each set maps to block, each block maps to one tag
    Each block maps to many data words

    Fully Associative: numSets = 1, numBlocksPerSet = 1, other parameters free
    Direct Mapped: numBlocksPerSet = 1, other parameters free
    Set Associative (n-way associative): all parameters free
 */

public class Cache extends MemoryLevel
{
    int numSets;   // Must be a power of 2
    int numBlocksPerSet;    // Must be a power of 2
    int numWordsPerBlock;  // Must be a power of 2
    int tagSize;
    int indexSize;
    int offsetSize;
    int[][][] cache;
    HashMap<Integer, Integer> tagByBlock;

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
        this.indexSize = Integer.numberOfTrailingZeros(numSets);
        this.offsetSize = Integer.numberOfTrailingZeros(numWordsPerBlock);
        this.tagSize = this.addressSize - this.indexSize - this.offsetSize;
        // Check tag size is greater than zero, but doesn't compare to log2(numBlocksPerSet)
        if (this.tagSize < 0)
        {
            throw new IllegalArgumentException("Address size is too small for cache parameters.");
        }
        this.cache = new int[numSets][numBlocksPerSet][numWordsPerBlock];
        this.tagByBlock = new HashMap<Integer, Integer>(numBlocksPerSet);
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
        String[][] stringTable = new String[1 + numSets * numBlocksPerSet][2 + numWordsPerBlock];
        stringTable[0][0] = "Set";
        stringTable[0][1] = "Tag";
        for (int i = 0; i < numWordsPerBlock; i++)
        {
            stringTable[0][2 + i] = "Block[0b" + Integer.toBinaryString(i) + "]";
        }
        for (int set = 0; set < numSets; set++)
        {
            // Go through each block (corresponding to a line)
            for (int block = 0; block < numBlocksPerSet; block++)
            {
                // Add set
                stringTable[1 + (set * numBlocksPerSet)][0] = "0b" + Integer.toBinaryString(set);
                // Add tag
                Integer tag = tagByBlock.get(block);
                if (tag == null)
                {
                    stringTable[1 + (set * numBlocksPerSet)][1] = "";
                }
                else
                {
                    stringTable[1 + (set * numBlocksPerSet)][1] = "0x" + Integer.toHexString(tag);
                }
                // Go through each word within that block (within that line)
                for (int word = 0; word < numWordsPerBlock; word++)
                {
                    stringTable[1 + (set * numBlocksPerSet)][2 + word] =
                            Integer.toHexString(cache[set][block][word]);
                }
            }
        }
        Utils.printTable(stringTable, 1 + numSets * numBlocksPerSet, 2 + numWordsPerBlock);
    }
}