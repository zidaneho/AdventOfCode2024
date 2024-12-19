import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Day11 {
    public static void main(String[] args) {
        //part1();
        Long[] arrangement = getArrangement("day11_input.txt");
        long sum = blink2(arrangement, 75);
        System.out.println("STONES: " + sum);
    }
    public static void part1() {
        Long[] arrangement = getArrangement("day11_input.txt");
        arrangement = blink(arrangement, 25);
        System.out.println("STONES: " + arrangement.length);
    }

    public static Long[] getArrangement(String fileName) {
        List<Long> arrangement = new ArrayList<>();
        File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) { // Use try-with-resources for auto-closing
            while (scanner.hasNextLong()) {         // Use hasNextLong for better clarity
                arrangement.add(scanner.nextLong());
            }
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return arrangement.toArray(new Long[0]);
    }

    public static Long[] blink(Long[] arrangement, int n) {
        Long[] newArrangment = Arrays.copyOf(arrangement, arrangement.length);
        for (int i = 0; i < n; i++) {
            newArrangment = changeArrangement(newArrangment);
        }
        return newArrangment;
    }
    public static long blink2(Long[] arrangement, int n) {

        HashMap<Long, Long> map = new HashMap<>();
        for (Long l : arrangement) {
            map.put(l,1 + map.getOrDefault(l, 0L));
        }
        for (int i = 0; i < n; i++) {
            map = changeArrangement2(map);
        }
        // Calculate sum of all values in the new map
        long sum = 0;
        for (Long l : map.values()) {
            sum += l;
        }
        return sum;
    }

    public static Long[] changeArrangement(Long[] arrangement) {
        List<Long> newArrangement = new ArrayList<>();
        for (Long num : arrangement) {
            String str = num.toString();
            if (num == 0L) {
                newArrangement.add(1L); // Directly add 1 for zeros
            } else if (str.length() % 2 == 0) {
                if (str.length() % 2 != 0) {
                    str = "0" + str; // Add leading zero if odd-length
                }
                int mid = str.length() / 2;
                long leftNum = Long.parseLong(str.substring(0, mid));
                long rightNum = Long.parseLong(str.substring(mid));
                newArrangement.add(leftNum);
                newArrangement.add(rightNum);
            } else {
                newArrangement.add(num * 2024); // Directly add transformed number
            }
           
        }
        return newArrangement.toArray(new Long[0]);
    }
    public static HashMap<Long,Long> changeArrangement2(HashMap<Long, Long> newArrangement) {
        // Temporary map to store updates
        HashMap<Long, Long> tempMap = new HashMap<>();

        // Iterate over the original map
        for (Long num : newArrangement.keySet()) {
            String str = num.toString();
            
            if (num == 0L) {
                tempMap.put(1L, tempMap.getOrDefault(1L, 0L) + newArrangement.getOrDefault(num, 0L));
              
            } else if (str.length() % 2 == 0) {
                if (str.length() % 2 != 0) {
                    str = "0" + str; // Add leading zero if odd-length
                }
                int mid = str.length() / 2;
                long leftNum = Long.parseLong(str.substring(0, mid));
                long rightNum = Long.parseLong(str.substring(mid));

                // Add to temp map
                tempMap.put(leftNum, tempMap.getOrDefault(leftNum, 0L) + newArrangement.getOrDefault(num, 0L));
                tempMap.put(rightNum, tempMap.getOrDefault(rightNum, 0L) + newArrangement.getOrDefault(num, 0L));

            } else {
                long newValue = num * 2024;
                tempMap.put(newValue, tempMap.getOrDefault(newValue, 0L) + newArrangement.getOrDefault(num,0L));
                
            }
            //tempMap.put(num, 0L); // Set the original number count to 0
        }

       

        return tempMap;
    }
    

    public static void printArray(Long[] arrangement) {
        for (Long l : arrangement) {
            System.out.print(l + " ");
        }
        System.out.println();
    }
}