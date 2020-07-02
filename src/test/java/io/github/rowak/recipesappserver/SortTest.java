package io.github.rowak.recipesappserver;

import org.junit.jupiter.api.Test;

public class SortTest {
	@Test
	public void ingredientSort() {
		String[] list = {"marinade", "marinade", null, "marinade", "chicken", "chicken"};
		sortByCategory(list);
		for (String str : list) {
			System.out.println(str);
		}
	}
	
	public static void sortByCategory(String[] ingredients) {
		quickSort(ingredients, 0, ingredients.length-1);
	}
	
	private static void quickSort(String[] list, int l, int r) {
		if (l < r) {
			int p = partition(list, l, r);
			quickSort(list, l, p);
			quickSort(list, p+1, r);
		}
	}
	
	private static int partition(String[] list, int l, int r) {
		String pivot = list[(l+r)/2];
		int i = l-1, j = r+1;
		while (true) {
			do {
				i++;
			} while (list[i] != null && pivot != null && list[i]
					.compareTo(pivot) < 0);
			do {
				j--;
			} while (list[j] != null && pivot != null && list[j]
					.compareTo(pivot) > 0);
			if (i >= j) {
				return j;
			}
			swap(list, i, j);
		}
	}
	
	private static void swap(Object[] list, int i, int j) {
		Object temp = list[i];
		list[i] = list[j];
		list[j] = temp;
	}
}
