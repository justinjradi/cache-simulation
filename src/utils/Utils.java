package utils;

public class Utils
{
    public enum LogLevel
    {
        ERROR,
        WARNING,
        MESSAGE,
        DEBUG,
    }
    private static LogLevel logLevelThreshold = LogLevel.DEBUG;

    public static void setLogLevelThreshold(LogLevel level)
    {
        logLevelThreshold = level;
    }

    public static void log(LogLevel level, String message)
    {
        if (level.ordinal() <= logLevelThreshold.ordinal())
        {
            switch (level)
            {
                case ERROR:
                    System.err.println("[ERROR] " + message);
                    break;
                case WARNING:
                    System.out.println("[WARNING] " + message);
                    break;
                case MESSAGE:
                    System.out.println("[MESSAGE] " + message);
                    break;
                case DEBUG:
                    System.out.println("[DEBUG] " + message);
                    break;
                default:
                    System.out.println("[UNKNOWN] " + message);
            }
        }
    }

    public static boolean validateSize(int number, int sizeInBits)
    {
        if ((sizeInBits > 31) || (sizeInBits < 0))
        {
            throw new IllegalArgumentException("Validate size failed because given size in bits is invalid");
        }
        if (number < 0)
        {
            return false;
        }
        int maxValue = 0x7FFFFFFF >> (31 - sizeInBits);
        if (number > maxValue)
        {
            return false;
        }
        return true;
    }

    public static boolean isPowerOfTwo(int x)
    {
        return (x != 0) && ((x & (x - 1)) == 0);
    }

    public static void printTable(String[][] table, int numRows, int numColumns)
    {
        int[] maxLengthByColumn = new int[numColumns];
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numColumns; j++)
            {
                if (table[i][j].length() > maxLengthByColumn[j])
                {
                    maxLengthByColumn[j] = table[i][j].length();
                }
            }
        }
        String[][] paddedTable = new String[numRows][numColumns];
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numColumns; j++)
            {
                paddedTable[i][j] = table[i][j];
                for (int k = 0; k < (maxLengthByColumn[j] - table[i][j].length()); k++)
                {
                    if (k % 2 == 0)
                    {
                        paddedTable[i][j] =  paddedTable[i][j] + " ";
                    }
                    else
                    {
                        paddedTable[i][j] = " " + paddedTable[i][j];
                    }
                }
                paddedTable[i][j] = " " + paddedTable[i][j] + " ";
            }
        }
        for (int i = 0; i < numRows; i++)
        {
            System.out.print("|");
            for (int j = 0; j < numColumns; j++)
            {
                System.out.print(paddedTable[i][j]);
                System.out.print("|");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

}