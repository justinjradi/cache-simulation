package memory_structures;
import utils.Utils;

import java.util.Random;

/*
    Cache Construction:

    Fully Associative: numSets = 1, numBlocksPerSet = 1, other parameters free
    Direct Mapped: numBlocksPerSet = 1, other parameters free
    Set Associative (n-way associative): all parameters free

    Address Format:

    [        tag       | setIndex | wordOffset ]

    note: wordOffset is the same thing as the Block Offset or Word ID

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

    public enum InclusionPolicy
    {
        EXCLUSIVE
    }

    WriteHitPolicy writeHitPolicy;
    WriteMissPolicy writeMissPolicy;
    ReplacementAlgorithm replacementAlgorithm;
    InclusionPolicy inclusionPolicy;
    int numSets;   // Must be a power of 2
    int numBlocksPerSet;    // Must be a power of 2
    int numWordsPerBlock;  // Must be a power of 2
    int tagSize;
    int setIndexSize;
    int wordOffsetSize;
    int[][][] words;    // word = words[set][block][offset]
    int[][] tags;       // tag = tags[set][block]
    Random random;

    public Cache(int addressSize, int wordSize, MemoryLevel nextMemoryLevel,
                  String name, int numSets, int numBlocksPerSet, int numWordsPerBlock,
                 WriteHitPolicy writeHitPolicy, WriteMissPolicy writeMissPolicy,
                 ReplacementAlgorithm replacementAlgorithm, InclusionPolicy inclusionPolicy)
    {
        super(addressSize, wordSize, nextMemoryLevel, name);
        this.numSets = numSets;
        this.numBlocksPerSet = numBlocksPerSet;
        this.numWordsPerBlock = numWordsPerBlock;
        this.writeHitPolicy = writeHitPolicy;
        this.writeMissPolicy = writeMissPolicy;
        this.replacementAlgorithm = replacementAlgorithm;
        this.inclusionPolicy = inclusionPolicy;
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
        this.random = new Random();
    }
    // TODO: Implement
    @Override
    public int read(int address)
    {
        if (!Utils.validateSize(address, addressSize))
        {
            throw new IllegalArgumentException("Failed read because address size invalid");
        }
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
        // TODO: Account for inclusion policy and dirty/valid bits
        // TODO: Finish miss handling and test
        if (writeMissPolicy == WriteMissPolicy.WRITE_ALLOCATE)
        {
            // Not correct
            int blockNumToReplace = chooseBlockNumToReplace(setIndex);
            int[] newBlock = new int[numWordsPerBlock];
            words[setIndex][blockNumToReplace] = newBlock;
            tags[setIndex][blockNumToReplace] = tag;
        }
        else // writeMissPolicy == WriteMissPolicy.NO_WRITE_ALLOCATE
        {

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