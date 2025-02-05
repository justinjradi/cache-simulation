import utils.Utils;
import memory_structures.Memory;
import memory_structures.Cache;

public class Main
{
    public static void main(String[] args)
    {
        Memory mem1 = new Memory(8, 8, null, "Mem1", 16);
        mem1.write(0xf, 0xff);
        mem1.printContents();
        // numSets = 2, numBlocksPerSet = 2, numWordsPerBlock = 4
        Cache cache1 = new Cache(8, 8, mem1, "cache1", 2, 2, 4, Cache.WriteHitPolicy.WRITE_THROUGH,
                Cache.WriteMissPolicy.NO_WRITE_ALLOCATE, Cache.ReplacementAlgorithm.RANDOM,
                Cache.InclusionPolicy.EXCLUSIVE);
        cache1.printContents();
    }
}