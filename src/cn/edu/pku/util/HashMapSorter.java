package cn.edu.pku.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class HashMapSorter {
	
	/**
	 * HashMap排序器
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map.Entry[] getSortedHashMapByValue(Map h, String string)
	{
		Set set = h.entrySet();
		Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		Arrays.sort(entries, new Comparator() 
		{
			public int compare(Object arg0, Object arg1) 
			{
		        Object value1 = ((Map.Entry) arg0).getValue();
		        Object value2 = ((Map.Entry) arg1).getValue();
		        return ((Comparable) value2).compareTo(value1);
		    }
		});
		return entries;
	}
	
	/**
	 * HashMap排序器
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map.Entry[] getSortedHashMapByValue(Map h, int num)
	{
		Set set = h.entrySet();
		Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		Arrays.sort(entries, new Comparator() 
		{
			public int compare(Object arg0, Object arg1) 
			{
				int value1 = (int) ((Map.Entry) arg0).getValue();
				int value2 = (int) ((Map.Entry) arg1).getValue();
				if (value2 < value1) {
					return -1;
				} else if (value2 == value1) {
					return 0;
				} else {
					return 1;
				}
		    }
		});
		return entries;
	}
}
