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
            Utils.log(Utils.LogLevel.ERROR, "Validate size failed because given size in bits is invalid");
            return false;
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

    public static void printTable(int[][] values, String[] headers, int rows, int columns)
    {
        int maxColumnSize = 0;
        for (int i = 0; i < columns; i++)
        {
            // TODO
        }
    }

}