import utils.Utils;
import memory_structures.Memory;

public class Main
{
    public static void main(String[] args)
    {
        Memory mem1 = new Memory(4, 8, null, "Mem1", 16);
        mem1.write(0xf, 0xff);
        mem1.printContents();
    }
}