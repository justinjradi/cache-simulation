package memory_structures;
import utils.Utils;

public class Cache extends MemoryLevel
{
    int numberOfSets;
    int numberOfBlocksPerSet;
    int numberOfWordsPerBlock;
    int tagSize;
    int indexSize;
    int offsetSize;
    public Cache(int addressSize, int wordSize, MemoryLevel nextMemoryLevel,
                  String name, int numberOfSets, int numberOfBlocksPerSet, int numberOfWordsPerBlock)
    {
        super(addressSize, wordSize, nextMemoryLevel, name);
        
    }
    @Override
    public int read(int address)
    {
        if (!Utils.validateSize(address, addressSize))
        {
            Utils.log(Utils.LogLevel.ERROR, "Address Size Invalid");
            return -1;
        }

        // TODO
        return -1;


    }
    @Override
    public void write(int address, int value)
    {
        if (!Utils.validateSize(address, addressSize))
        {
            Utils.log(Utils.LogLevel.ERROR, "Failed write because address size invalid");
            return;
        }
        if (!Utils.validateSize(value, wordSize))
        {
            Utils.log(Utils.LogLevel.ERROR, "Failed read because value size invalid");
            return;
        }


        // TODO
        return;
    }
}