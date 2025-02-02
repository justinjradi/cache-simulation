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
        return this.memory[address];
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
        this.memory[address] = value;
    }
    @Override
    public void printContents()
    {
        System.out.println("Contents of memory " + name + ":");
        String[][] table = new String[2][this.size + 1];
        table[0][0] = "addy";
        table[1][0] = "val";
        for (int i = 0; i < this.size; i++)
        {
            table[0][i + 1] = Integer.toHexString(i);
            table[1][i + 1] = Integer.toHexString(memory[i]);
        }
        Utils.printTable(table, 2, this.size + 1);
    }
}