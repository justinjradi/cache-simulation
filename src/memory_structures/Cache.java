package memory_structures;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class Cache extends MemoryLevel
{
    int numberOfSets;   // Must be a power of 2
    int numberOfBlocksPerSet;
    int numberOfWordsPerBlock;  // Must be a power of 2
    int tagSize;
    int indexSize;
    int offsetSize;
    int[][][] sets;
    public Cache(int addressSize, int wordSize, MemoryLevel nextMemoryLevel,
                  String name, int numberOfSets, int numberOfBlocksPerSet, int numberOfWordsPerBlock)
    {
        super(addressSize, wordSize, nextMemoryLevel, name);
        this.numberOfSets = numberOfSets;
        this.numberOfBlocksPerSet = numberOfBlocksPerSet;
        this.numberOfWordsPerBlock = numberOfWordsPerBlock;
        if (!Utils.isPowerOfTwo(numberOfSets))
        {
            throw new IllegalArgumentException("Number of sets must be a power of two");
        }
        if (!Utils.isPowerOfTwo(numberOfWordsPerBlock))
        {
            throw new IllegalArgumentException("Number of words per block must be a power of two");
        }
        this.indexSize = Integer.numberOfTrailingZeros(numberOfSets);
        this.offsetSize = Integer.numberOfTrailingZeros(numberOfWordsPerBlock);
        this.sets = new int[numberOfSets][numberOfBlocksPerSet][numberOfWordsPerBlock];
        for (int i = 0; i < numberOfSets; i++)
        {
            for (int j = 0; j < numberOfBlocksPerSet; j++)
            {
                for (int k = 0; k < numberOfWordsPerBlock; k++)
                {

                }
            }
        }

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

    }
}