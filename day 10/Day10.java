import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Day10 {
    public static void main(String[] args) {
       day10_part2();
    }

    public static void day10_part1() {
        int[][] topographyMap = getTopographyMap();
       
        int sum = 0;
        for (int i = 0; i < topographyMap.length; i++) {
            for (int j = 0; j < topographyMap[i].length;j++) {
                if (topographyMap[i][j] == 0) {
                    HashMap<String,Integer> nineScores = new HashMap<>();
                    sum += checkTrailhead(0,i,j,topographyMap,nineScores);
                }
            }
        }
        System.out.println("SUM: " + sum);
    }
    public static void day10_part2() {
        int[][] topographyMap = getTopographyMap();
       
        int sum = 0;
        for (int i = 0; i < topographyMap.length; i++) {
            for (int j = 0; j < topographyMap[i].length;j++) {
                if (topographyMap[i][j] == 0) {
                    sum += checkTrailhead(0,i,j,topographyMap);
                }
            }
        }
        System.out.println("SUM: " + sum);
    }

    public static int checkTrailhead(int current, int i, int j, int[][] map, HashMap<String,Integer> hashMap) {
        if (map[i][j] == 9) {
            if (hashMap.containsKey(i + " " + j)) return 0;
            hashMap.put(i + " " + j, 1);
            return 1;
        } 
        int sum = 0;
        if (isIndexValid(map, i-1, j, current)) sum+= checkTrailhead(current + 1, i-1, j, map,hashMap);
        if (isIndexValid(map, i, j-1, current)) sum+= checkTrailhead(current + 1, i, j-1, map,hashMap);
        if (isIndexValid(map, i+1, j, current)) sum+=checkTrailhead(current + 1, i+1, j, map,hashMap);
        if (isIndexValid(map, i, j+1, current)) sum+=checkTrailhead(current + 1, i, j+1, map,hashMap);
        return sum;
    }
    public static int checkTrailhead(int current, int i, int j, int[][] map) {
        if (map[i][j] == 9) {
            return 1;
        } 
        int sum = 0;
        if (isIndexValid(map, i-1, j, current)) sum+= checkTrailhead(current + 1, i-1, j, map);
        if (isIndexValid(map, i, j-1, current)) sum+= checkTrailhead(current + 1, i, j-1, map);
        if (isIndexValid(map, i+1, j, current)) sum+=checkTrailhead(current + 1, i+1, j, map);
        if (isIndexValid(map, i, j+1, current)) sum+=checkTrailhead(current + 1, i, j+1, map);
        return sum;
    }
    public static boolean isIndexValid(int[][] map, int i, int j, int current) {
        if (i >= 0 && i < map.length && j>= 0 && j < map[i].length) {
            return map[i][j] - current == 1;
        }
        return false;
    }
    public static void printDoubleArray(int[][] arr) {
        for (int[] row : arr) {
            for (int cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    public static int[][] getTopographyMap() {
        ArrayList<int[]> map = new ArrayList<>();
        try {
            File file = new File("day10_input.txt");
            Scanner scanner = new Scanner(file);
            int i =0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                map.add(new int[line.length()]);
                for (int j = 0; j < line.length();j++) {
                    int num = Integer.parseInt(line.substring(j,j+1));
                    map.get(i)[j] = num;
                }
                i++;
            }
            scanner.close();
        }
        catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
        }
        return map.toArray(new int[0][0]);
    }
}