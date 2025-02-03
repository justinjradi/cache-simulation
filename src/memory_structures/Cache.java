package memory_structures;
import utils.Utils;

import java.util.Map;

/*

    // TODO This is wrong

    Sets map to blocks (1:many)
    Blocks map to sets (1:many)
    Tags map to blocks (1:1)
    Words map to tags (1:1)

    1 tag is associated with 1 set


    Fully Associative: numSets = 1,
 */

public class Cache extends MemoryLevel
{
    int numSets;   // Must be a power of 2
    int numBlocksPerSet;
    int numTagsPerBlock;  // Must be a power of 2
    int tagSize;
    int indexSize;
    int offsetSize;
    Map.Entry<Integer, Integer>[][][] cache; // [set][block][<tag, word>]

    public Cache(int addressSize, int wordSize, MemoryLevel nextMemoryLevel,
                  String name, int numSets, int numBlocksPerSet, int numTagsPerBlock)
    {
        super(addressSize, wordSize, nextMemoryLevel, name);
        this.numSets = numSets;
        this.numBlocksPerSet = numBlocksPerSet;
        this.numTagsPerBlock = numTagsPerBlock;
        if (numSets == 0)
        {
            throw new IllegalArgumentException("Number of sets must be 1 or greater.");
        }
        if (!Utils.isPowerOfTwo(numSets))
        {
            throw new IllegalArgumentException("Number of sets must be a power of two");
        }
        if (!Utils.isPowerOfTwo(numTagsPerBlock))
        {
            throw new IllegalArgumentException("Number of words per block must be a power of two");
        }
        this.indexSize = Integer.numberOfTrailingZeros(numSets);
        this.offsetSize = Integer.numberOfTrailingZeros(numTagsPerBlock);
        this.tagSize = this.addressSize - this.indexSize - this.offsetSize;
        if (this.tagSize < 0)
        {
            throw new IllegalArgumentException("Address size is too small for cache parameters.");
        }
        Map.Entry<Integer, Integer>[][][] cache =
                new Map.Entry[numSets][numBlocksPerSet][numTagsPerBlock];

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
        String[][] printTable = new String[][]
        for (int i = 0; i < this.numSets; i++)
        {
            for (int j = 0; j < this.numBlocksPerSet; j++)
            {
                for (int k = 0; k < this.numTagsPerBlock; k++)
                {

                }
            }
        }
    }
}