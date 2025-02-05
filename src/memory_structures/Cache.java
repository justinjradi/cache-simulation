package memory_structures;
import utils.Utils;

import java.util.Random;

/*
    Cache Construction:

    - Fully Associative: numSets = 1, numBlocksPerSet = 1, other parameters free
    - Direct Mapped: numBlocksPerSet = 1, other parameters free
    - Set Associative (n-way associative): all parameters free

    Other Cache Attributes:

    - Cache is using a NINE inclusion policy
    - Exclusive inclusion policy could be implemented by adding a public evict() method to MemoryLevel

    Address Format:

    [        tag       | setIndex | wordOffset ]

    - wordOffset is the same thing as the Block Offset or Word ID

    printContents() Table Format:

    | Set         | Tag               | Block[wordOffset] | Block[wordOffset] | ...
    | setIndex    | tag               | word              | word              | ...
    | setIndex    | tag               | word              | word              | ...
    | setIndex    | tag               | word              | word              | ...
    | setIndex    | tag               | word              | word              | ...
    | ...         | ...               | ...               | ...               | ...

 */

public class Cache extends MemoryLevel
{
    public enum WriteHitPolicy
    {
        WRITE_THROUGH,  // Only write to current level
        WRITE_BACK      // Update cache and next level
    }
    public enum WriteMissPolicy
    {
        WRITE_ALLOCATE,     // Insert block in cache
        NO_WRITE_ALLOCATE   //
    }
    public enum ReplacementAlgorithm
    {
        RANDOM
    }

    final public WriteHitPolicy writeHitPolicy;
    final public WriteMissPolicy writeMissPolicy;
    final public ReplacementAlgorithm replacementAlgorithm;
    final public int numSets;   // Must be a power of 2
    final public int numBlocksPerSet;    // Must be a power of 2
    final public int numWordsPerBlock;  // Must be a power of 2
    private int tagSize;
    private int setIndexSize;
    private int wordOffsetSize;
    private int[][][] words;    // words[setIndex][blockNum][wordOffset]
    private int[][] tags;       // tags[setIndex][blockNum]
    private int[][] valid;      // valid[setIndex][blockNum]
    private int[][] dirty;      // dirty[setIndex][blockNum]
    Random random;

    public Cache(int addressSize, int wordSize, MemoryLevel nextMemoryLevel,
                  String name, int numSets, int numBlocksPerSet, int numWordsPerBlock,
                 WriteHitPolicy writeHitPolicy, WriteMissPolicy writeMissPolicy,
                 ReplacementAlgorithm replacementAlgorithm)
    {
        super(addressSize, wordSize, nextMemoryLevel, name);
        this.numSets = numSets;
        this.numBlocksPerSet = numBlocksPerSet;
        this.numWordsPerBlock = numWordsPerBlock;
        this.writeHitPolicy = writeHitPolicy;
        this.writeMissPolicy = writeMissPolicy;
        this.replacementAlgorithm = replacementAlgorithm;
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
        // TODO: Allow for numSets and numWordsPerBlock that aren't powers of two
        // TODO: Compute minimum tag size log2(numBlocksPerSet) and add check
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
        this.valid = new int[numSets][numBlocksPerSet];
        this.dirty = new int[numSets][numBlocksPerSet];
        this.random = new Random();
    }
    // TODO: Correct read/write algorithms and use valid and dirty bits
    @Override
    public int read(int address)
    {
        if (!Utils.validateSize(address, addressSize))
        {
            throw new IllegalArgumentException("Failed read because address size invalid");
        }
        // Calculate tag, setIndex, and wordOffset
        int setIndexMask = ((1 << setIndexSize) - 1) << wordOffsetSize;
        int wordOffsetMask = (1 << wordOffsetSize) - 1;
        int tag = address >> (setIndexSize + wordOffsetSize);
        int setIndex = (address & setIndexMask) >> (wordOffsetSize);
        int wordOffset = address & wordOffsetMask;
        // Go to set
        for (int blockNum = 0; blockNum < numBlocksPerSet; blockNum++)
        {
            if (tags[setIndex][blockNum] != tag)
            {
                continue;
            }
            // HIT!
            return words[setIndex][blockNum][wordOffset];
        }
        // MISS!
        int[] newBlock = new int[numWordsPerBlock];
        for (int i = 0; i < numWordsPerBlock; i++)
        {
            int addressMask = ~wordOffsetMask;
            newBlock[i] = nextMemoryLevel.read((address & addressMask) | i);
        }
        int blockNumToReplace = chooseBlockNumToReplace(setIndex);
        words[setIndex][blockNumToReplace] = newBlock;
        tags[setIndex][blockNumToReplace] = tag;
        return nextMemoryLevel.read(address);
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
        // Calculate tag, setIndex, and wordOffset
        int setIndexMask = ((1 << setIndexSize) - 1) << wordOffsetSize;
        int wordOffsetMask = (1 << wordOffsetSize) - 1;
        int tag = address >> (setIndexSize + wordOffsetSize);
        int setIndex = (address & setIndexMask) >> (wordOffsetSize);
        int wordOffset = address & wordOffsetMask;
        // Go to set
        for (int blockNum = 0; blockNum < numBlocksPerSet; blockNum++)
        {
            if (tags[setIndex][blockNum] != tag)
            {
                continue;
            }
            // HIT!
            if (writeHitPolicy == WriteHitPolicy.WRITE_BACK)
            {
                nextMemoryLevel.write(address, value);
            }
            // Else, writeHitPolicy == WriteHitPolicy.WRITE_THROUGH, so we continue as normal
            words[setIndex][blockNum][wordOffset] = value;
            return;
        }
        // MISS!
        if (writeMissPolicy == WriteMissPolicy.WRITE_ALLOCATE)
        {
            int[] newBlock = new int[numWordsPerBlock];
            for (int i = 0; i < numWordsPerBlock; i++)
            {
                int addressMask = ~wordOffsetMask;
                newBlock[i] = nextMemoryLevel.read((address & addressMask) | i);
            }
            int blockNumToReplace = chooseBlockNumToReplace(setIndex);
            words[setIndex][blockNumToReplace] = newBlock;
            tags[setIndex][blockNumToReplace] = tag;
            // Not implemented:
//            if (inclusionPolicy == InclusionPolicy.EXCLUSIVE)
//            {
//                nextMemoryLevel.evict(address);
//            }
        }
        else // writeMissPolicy == WriteMissPolicy.NO_WRITE_ALLOCATE
        {
            nextMemoryLevel.write(address, value);
        }
        return;
    }
    private int chooseBlockNumToReplace(int setIndex)
    {
        switch (replacementAlgorithm)
        {
            default:
                // Default to ReplacementAlgorithm.RANDOM
                return random.nextInt(numBlocksPerSet);
        }
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
            stringTable[0][2 + i] = "Blk[0b" + Integer.toBinaryString(i) + "]";
        }
        // Go through each set
        for (int setIndex = 0; setIndex < numSets; setIndex++)
        {
            // Go through each line corresponding to a block
            for (int blockNum = 0; blockNum < numBlocksPerSet; blockNum++)
            {
                // Add set index to each line
                String setIndexString = "0b" + Integer.toBinaryString(setIndex);
                stringTable[1 + (setIndex * numBlocksPerSet) + blockNum][0] = setIndexString;
                // Add tag to each line
                String tagString = "0x" + Integer.toHexString(tags[blockNum][setIndex]);
                stringTable[1 + (setIndex * numBlocksPerSet) + blockNum][1] = tagString;
                // Add each word within a block (going horizontally across line)
                for (int wordOffset = 0; wordOffset < numWordsPerBlock; wordOffset++)
                {
                    String wordString = "0x" + Integer.toHexString(words[setIndex][blockNum][wordOffset]);
                    stringTable[1 + (setIndex * numBlocksPerSet) + blockNum][2 + wordOffset] = wordString;
                }
            }
        }
        Utils.printTable(stringTable, 1 + numSets * numBlocksPerSet, 2 + numWordsPerBlock);
    }
}