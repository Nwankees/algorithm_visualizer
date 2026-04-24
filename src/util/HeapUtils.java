package util;

public class HeapUtils {
    public static boolean isValidMaxHeap(int[] data, int size) {
        for (int i = 0; i < size; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < size && data[i] < data[left]) {
                return false;
            }
            if (right < size && data[i] < data[right]) {
                return false;
            }
        }
        return true;
    }
}
