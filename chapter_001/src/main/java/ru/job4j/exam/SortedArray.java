package ru.job4j.exam;

/**
 * SortedArray
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SortedArray {

    /**
     * Method mergeTwoSortedArrays
     * @param firstArray first int[] sorted in ascending order
     * @param secondArray second int[] sorted in ascending order
     * @return int[] array obtained by merging two input arrays
     */
    public int[] mergeTwoSortedArrays(int[] firstArray, int[] secondArray) {

        int[] mergedArray = new int[firstArray.length + secondArray.length];
        int firstArrIndex = 0;
        int secondArrIndex = 0;
        int mergedArrIndex = 0;

        // comparing two input arrays element by element
        // adding elements to merged array in ascending order
        // until all the elements of one input array is added to merged array
        while ((firstArrIndex < firstArray.length) && (secondArrIndex < secondArray.length)) {
            mergedArray[mergedArrIndex++] =
                    firstArray[firstArrIndex] <= secondArray[secondArrIndex]
                            ? firstArray[firstArrIndex++] : secondArray[secondArrIndex++];
        }

        // adding the remaining elements of the second array to merged array
        while (secondArrIndex < secondArray.length) {
            mergedArray[mergedArrIndex++] = secondArray[secondArrIndex++];
        }

        // or adding the rest elements of the first array to merged array
        while (firstArrIndex < firstArray.length) {
            mergedArray[mergedArrIndex++] = firstArray[firstArrIndex++];
        }

        return mergedArray;
    }
}
