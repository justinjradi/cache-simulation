package memory_structures;
import utils.Utils;

public abstract class MemoryLevel
{
    public String name;
    protected final int addressSize;  // In bits, maximum 31
    protected final int wordSize;   // In bits, maximum 31
    public MemoryLevel nextMemoryLevel;
    protected int[] data;
    public abstract int read(int address);
    public abstract void write(int address, int value);
    public abstract void printContents();
    public MemoryLevel(int addressSize, int wordSize, MemoryLevel nextMemoryLevel,
                       String name)
    {
        if (addressSize > 31)
        {
            throw new IllegalArgumentException("Address size may not exceed 31");
        }
        if (wordSize > 31)
        {
            throw new IllegalArgumentException("Word size may not exceed 31");
        }
        this.name = name;
        this.addressSize = addressSize;
        this.wordSize = wordSize;
        this.nextMemoryLevel = nextMemoryLevel;
    }
}