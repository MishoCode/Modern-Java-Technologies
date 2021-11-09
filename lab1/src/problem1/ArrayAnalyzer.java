package problem1;

public class ArrayAnalyzer {
    public static boolean isMountainArray(int[] array) {
        if (array.length <= 2) {
            return false;
        }

        boolean summitFound = false;
        int i = 0;

        while (i < array.length - 1) {
            if (summitFound) {
                if (array[i] <= array[i + 1]) {
                    return false;
                }
            } else {
                if (array[i] == array[i + 1]) {
                    return false;
                }

                if (array[i] > array[i + 1]) {
                    summitFound = true;
                }
            }
            i++;
        }

        return summitFound;
    }

    public static void main(String[] args) {
        int[] array1 = new int[] {2, 1};
        System.out.println(isMountainArray(array1));

        int[] array2 = new int[] {3, 5, 5};
        System.out.println(isMountainArray(array2));

        int[] array3 = new int[] {0, 3, 2, 1};
        System.out.println(isMountainArray(array3));

        int[] array4 = new int[] {0, 2, 3, 3, 5, 2, 1, 0};
        System.out.println(isMountainArray(array4));

        int[] array5 = new int[] {0, 2, 3, 5, 2, 1, 0};
        System.out.println(isMountainArray(array5));

        int[] array6 = new int[] {1, 2, 3, 4, 5};
        System.out.println(isMountainArray(array6));

        int[] array7 = new int[] {2, 1, 0};
        System.out.println(isMountainArray(array7));

        int[] array8 = new int[] {0, 1, 0};
        System.out.println(isMountainArray(array8));
    }
}
