package fr.tse.fi2.hpp.labs.queries.impl.utils;

public class MergeSort {

	public MergeSort() {
	}

	public int[] sort(int[] tab) {
		int[] res = new int[tab.length];

		if (tab.length == 2) {
			if (tab[0] > tab[1]) {
				res[0] = tab[1];
				res[1] = tab[0];
			}
		} else if (tab.length > 2) {
			int[] leftArray = null;
			int[] rightArray = null;

			if (tab.length % 2 == 0) {
				rightArray = new int[tab.length / 2];
				leftArray = new int[tab.length / 2];

				for (int i = 0; i < (tab.length / 2); i++) {
					leftArray[i] = tab[i];
					rightArray[i] = tab[(tab.length / 2) + i];
				}
			}

			String s = null;
			for (int i = 0; i < leftArray.length; i++)
				s = s + leftArray[i] + " ";
			System.out.println("left " + s);

			String s2 = null;
			for (int i = 0; i < rightArray.length; i++)
				s2 = s2 + rightArray[i] + " ";
			System.out.println("right " + s2);

//			int[] resGauche = this.sort(leftArray);
//			int[] resDroit = this.sort(rightArray);

		}
		String s3 = "";
		for (int i = 0; i < res.length; i++)
			s3 = s3 + res[i] + " ";
		System.out.println("tableau " + s3);
		return res;
	}

	private int[] fusion(int[] left, int[] right) {
		boolean fini = false;
		int[] res = new int[left.length + right.length];
		while (!fini) {
			for (int i = 0; i < left.length; i++) {
				if (left[i] < right[i] && i < right.length) {
					res[i] = left[i];

				}
			}
		}

		return res;

	}

}