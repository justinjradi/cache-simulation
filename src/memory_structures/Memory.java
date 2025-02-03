package memory_structures;
import utils.Utils;

import java.util.Arrays;

public class Memory extends MemoryLevel
{
    private int size;
    private int[] memory;
    public Memory(int addressSize, int wordSize, MemoryLevel nextMemoryLevel,
                  String name, int numberOfWords)
    {
        super(addressSize, wordSize, nextMemoryLevel, name);
        this.size = numberOfWords;
        this.memory = new int[size];
    }

    @Override
    public int read(int address)
    {
        if (!Utils.validateSize(address, addressSize))
        {
            throw new IllegalArgumentException("Failed read because address size invalid");
        }
        return memory[address];
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
        memory[address] = value;
    }
    @Override
    public void printContents()
    {
        System.out.println("Contents of memory " + name + ":");
        String[][] printTable = new String[2][size + 1];
        printTable[0][0] = "addy";
        printTable[1][0] = "val";
        for (int i = 0; i < size; i++)
        {
            printTable[0][i + 1] = "0x" + Integer.toHexString(i);
            printTable[1][i + 1] = "0x" + Integer.toHexString(memory[i]);
        }
        Utils.printTable(printTable, 2, size + 1);
    }
}