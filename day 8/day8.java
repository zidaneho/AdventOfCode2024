import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.io.File;

public class day8 {
    public static void main(String[] args) {
        File file = new File("day8_input.txt");
        ArrayList<String[]> grid = new ArrayList<>();
        int sum = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] letters = line.split("");
            
                grid.add(letters);
            }
            scanner.close();
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
        }
        HashSet<String> letters = new HashSet<>();
        HashSet<String> markedPoints = new HashSet<>();
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).length; j++) {
                String cell = grid.get(i)[j];
                if (!isValidLetter(cell.charAt(0))) continue;
               
                
                Point[] similarPoints = findSimilarLetters(grid, cell, i, j);
                Point antenna = new Point(cell,i,j);
                for (Point point : similarPoints) {
                    
                    int distI = i - point.i;
                    int distJ = j - point.j;

                    int antiNode1I = i + distI;
                    int antiNode1J = j + distJ;

                    int antiNode2I = point.i - distI;
                    int antiNode2J = point.j - distJ;

                    if (isAntiNodeValid(grid, antiNode1I, antiNode1J,antenna,point)) {
                        printInfo(cell, point,i,j, antiNode1I, antiNode1J);
                        if (!isValidLetter(grid.get(antiNode1I)[antiNode1J].charAt(0))) {
                            grid.get(antiNode1I)[antiNode1J] = "#";
                        }
                        else {
                            System.out.println("Overlapped letter");
                        }
                        String markedPoint = antiNode1I + ","+antiNode1J;
                        if (!markedPoints.contains(markedPoint)) {
                            sum++;
                            markedPoints.add(markedPoint);
                        }
                        
                        //printGrid(grid);
                        
                    }
                    if (isAntiNodeValid(grid, antiNode2I, antiNode2J,antenna,point)) {
                        printInfo(cell, point,i,j, antiNode2I, antiNode2J);
                        if (!isValidLetter(grid.get(antiNode2I)[antiNode2J].charAt(0))) {
                            grid.get(antiNode2I)[antiNode2J] = "#";
                        }
                        else {
                            System.out.println("Overlapped letter");
                        }
                        String markedPoint = antiNode2I + ","+antiNode2J;
                        if (!markedPoints.contains(markedPoint)) {
                            sum++;
                            markedPoints.add(markedPoint);
                        }
                        //printGrid(grid);
                        
                    }
                }
                letters.add(cell);


            }
        }
        
        System.out.println("SUM: " +sum);

        
    }
    public static void printInfo(String cell, Point point, int i, int j,int antiNodeI, int antiNodeJ) {
        System.out.println(cell + " Pos: " + i + " " + j + " Antinode:" +(antiNodeI) + " " + (antiNodeJ));
        System.out.println(point);
        
    }
    public static int getNMag(int n) {
        return n / Math.abs(n);
    }
    public static void printGrid(ArrayList<String[]> grid) {
        for (String[] row: grid) {
            for (String cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }

    public static boolean isValidLetter(char c) {
        return Character.isDigit(c) || Character.isLowerCase(c) || Character.isUpperCase(c);
    }
    public static Point[] findSimilarLetters(ArrayList<String[]> grid, String str, int i, int j) {
        ArrayList<Point> points = new ArrayList<>();
        for (int k = i; k < grid.size(); k++) {
            for (int h = 0; h < grid.get(k).length; h++) {
                if (k == i && h <= j) continue;
               
                String compareStr = grid.get(k)[h];
                if (str.equals(compareStr)) {
                    Point point = new Point(compareStr,k,h);
                    points.add(point);
                }
            }
        }
        return points.toArray(new Point[0]);
    }
    public static boolean isAntiNodeValid(ArrayList<String[]> grid, int i, int j, Point antenna, Point otherAntenna) {
        if (i < 0 || i >= grid.size() || j < 0 || j >= grid.get(i).length) return false;
        String letter = grid.get(i)[j];
        
        return !letter.equals("#") && isAntiNodeTwiceDistance(i, j, antenna, otherAntenna);
    }
    public static boolean isAntiNodeTwiceDistance(int aI, int aJ, Point antenna, Point other) {
        int dist1 = Math.abs(antenna.i - aI) + Math.abs(antenna.j - aJ);
        int dist2 = Math.abs(other.i - aI) + Math.abs(other.j - aJ);
        return dist1 * 2 == dist2 || dist2 * 2 == dist1;
    }
}

class Point {
    public String letter;
    public int i;
    public int j;

    public Point() {
        letter = "";
        i = 0;
        j = 0;
    }
    public Point(String letter, int i, int j ) {
        this.letter = letter;
        this.i = i;
        this.j = j;
    }
    @Override
    public String toString(){
        return "Letter: " + letter + " position: [" + i + "," + j + "]";
    }
}