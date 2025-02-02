package memory_structures;
import utils.Utils;

public class Memory extends MemoryLevel
{
    private int[] memory;
    public Memory(int addressSize, int wordSize, MemoryLevel nextMemoryLevel,
                  String name, int numberOfWords)
    {
        super(addressSize, wordSize, nextMemoryLevel, name);
        this.memory = new int[numberOfWords];
    }

    @Override
    public int read(int address)
    {
        if (!Utils.validateSize(address, addressSize))
        {
            Utils.log(Utils.LogLevel.ERROR, "Address Size Invalid");
            return -1;
        }
        return this.memory[address];
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
        this.memory[address] = value;
    }
}