package problem2;

public class WeatherForecaster {
    public static int[] getsWarmerIn(int[] temperatures) {
        int[] warmerDays = new int[temperatures.length];

        for (int i = 0; i < temperatures.length - 1; i++) {
            int days = 0;
            int j = i + 1;
            boolean found = false;
            while (!found && j < temperatures.length) {
                if (temperatures[j] > temperatures[i]) {
                    days = j - i;
                    found = true;
                }
                j++;
            }
            warmerDays[i] = days;
        }

        return warmerDays;
    }

    public static void printArray(int[] array) {
        for (int i : array) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] temperatures1 = new int[]{3, 4, 5, 1, -1, 2, 6, 3};
        int[] result1 = getsWarmerIn(temperatures1);
        printArray(result1);

        int[] temperatures2 = new int[]{3, 4, 5, 6};
        int[] result2 = getsWarmerIn(temperatures2);
        printArray(result2);

        int[] temperatures3 = new int[]{3, 6, 9};
        int[] result3 = getsWarmerIn(temperatures3);
        printArray(result3);
    }
}
