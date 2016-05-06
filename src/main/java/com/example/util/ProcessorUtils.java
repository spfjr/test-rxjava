package com.example.util;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shaun Fleming
 */
@Slf4j
public class ProcessorUtils
{
    public static void randomSleep(final int maxSeconds)
    {
        final long millis = (long) (Math.random() * maxSeconds * 1000);
        try
        {
            log.info("Sleeping for {} seconds", String.valueOf(millis / 1000));
            Thread.sleep(millis);
        }
        catch (final InterruptedException e)
        {
            // swallow exception
        }
    }

    public static List<String> copyListAndAdd(final List<String> oldList, final String str)
    {
        final List<String> newList = new ArrayList<>(oldList);
        newList.add(str);
        return newList;
    }
}
