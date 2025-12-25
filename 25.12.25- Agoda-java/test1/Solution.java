import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();
        
        int[] executionTime = new int[n];
        for (int i = 0; i < n; i++) {
            executionTime[i] = scanner.nextInt();
        }
        
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        System.out.println(getMinOperations(executionTime, x, y));
    }

    public static long getMinOperations(int[] executionTime, int x, int y) {
        long low = 0;
        long maxVal = 0;
        for (int t : executionTime) maxVal = Math.max(maxVal, t);
        
        long high = (maxVal + y - 1) / y; 
        long result = high;
        long diff = (long) x - y;

        while (low <= high) {
            long mid = low + (high - low) / 2;
            if (mid == 0) {
                if (maxVal == 0) {
                    result = 0;
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
                continue;
            }
            if (check(mid, executionTime, diff, (long) y)) {
                result = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return result;
    }

    private static boolean check(long k, int[] executionTime, long diff, long y) {
        long totalExtraNeeded = 0;
        long autoReduce = k * y;

        for (int time : executionTime) {
            if (time > autoReduce) {
                long remaining = time - autoReduce;
                long needed = (remaining + diff - 1) / diff;
                totalExtraNeeded += needed;
            }
            if (totalExtraNeeded > k) return false;
        }
        return totalExtraNeeded <= k;
    }
}