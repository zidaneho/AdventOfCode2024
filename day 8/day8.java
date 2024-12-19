import java.util.ArrayList;
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
                
                    sum += createAntiNode(1, 0, distI, distJ, grid, antenna, markedPoints);
                    sum += createAntiNode(1, 0, -distI, -distJ, grid, point, markedPoints);
                    
                    if (setAntiNode(antenna, grid, markedPoints)) sum++;
                    if (setAntiNode(point, grid, markedPoints)) sum++;
                    
                    
                //     if (isAntiNodeValid(grid, antiNode1I, antiNode1J,antenna,point)) {
                //         printInfo(cell, point,i,j, antiNode1I, antiNode1J);
                //         if (!isValidLetter(grid.get(antiNode1I)[antiNode1J].charAt(0))) {
                //             grid.get(antiNode1I)[antiNode1J] = "#";
                //         }
                //         else {
                //             System.out.println("Overlapped letter");
                //         }
                //         String markedPoint = antiNode1I + ","+antiNode1J;
                //         if (!markedPoints.contains(markedPoint)) {
                //             sum++;
                //             markedPoints.add(markedPoint);
                //         }
                        
                //         //printGrid(grid);
                        
                //     }
                //     if (isAntiNodeValid(grid, antiNode2I, antiNode2J,antenna,point)) {
                //         printInfo(cell, point,i,j, antiNode2I, antiNode2J);
                //         if (!isValidLetter(grid.get(antiNode2I)[antiNode2J].charAt(0))) {
                //             grid.get(antiNode2I)[antiNode2J] = "#";
                //         }
                //         else {
                //             System.out.println("Overlapped letter");
                //         }
                //         String markedPoint = antiNode2I + ","+antiNode2J;
                //         if (!markedPoints.contains(markedPoint)) {
                //             sum++;
                //             markedPoints.add(markedPoint);
                //         }
                //         //printGrid(grid);
                        
                //     }
                }
                
                letters.add(cell);


            }
        }
        
        System.out.println("SUM: " +sum);

        
    }
    public static void printInfo(ArrayList<String[]> grid, String cell, Point point, int i, int j,int antiNodeI, int antiNodeJ) {
        System.out.println(cell + " Pos: " + i + " " + j + " Antinode:" +(antiNodeI) + " " + (antiNodeJ));
        System.out.println(point);
        printGrid(grid);
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
    public static boolean isAntiNodeValid(ArrayList<String[]> grid, int i, int j) {
        
        String letter = grid.get(i)[j];
        
        return !letter.equals("#") && isWithinBounds(grid, i, j);
    }
    public static boolean isWithinBounds(ArrayList<String[]> grid, int i, int j) {
        return i >= 0 && i < grid.size() && j >= 0 && j < grid.get(i).length;
    }
    
    public static boolean setAntiNode(Point antenna, ArrayList<String[]> grid, HashSet<String> markedPoints) {
        if (isAntiNodeValid(grid, antenna.i, antenna.j)) {
            String markedPoint1 = antenna.i + "," + antenna.j;
            if (!isValidLetter(grid.get(antenna.i)[antenna.j].charAt(0))) {
                grid.get(antenna.i)[antenna.j] = "#";
            }
            if (!markedPoints.contains(markedPoint1)) {
                markedPoints.add(markedPoint1);
                return true;
            }
        }
        return false;
    }
    public static int createAntiNode(int i, int limit, int distI, int distJ, ArrayList<String[]> grid, Point antenna, HashSet<String> markedPoints) {
        if (limit > 0 && i > limit) return 0;

        int sum = 0;

        int antiNodeI1 = antenna.i + distI * i;
        int antiNodeJ1 = antenna.j + distJ * i;

        if (!isWithinBounds(grid, antiNodeI1, antiNodeJ1)) return 0;

        
        String markedPoint1 = antiNodeI1 + "," + antiNodeJ1;
            
           
        if (!markedPoints.contains(markedPoint1)) {
            markedPoints.add(markedPoint1);
            if (!isValidLetter(grid.get(antiNodeI1)[antiNodeJ1].charAt(0))) {
                grid.get(antiNodeI1)[antiNodeJ1] = "#";
            }
            sum++;
            //printInfo(grid,antenna.letter, other, antenna.i, antenna.j, antiNodeI1, antiNodeJ1);
        }
        
        
        
        return sum + createAntiNode(i+1, limit, distI, distJ, grid, antenna, markedPoints); //base case is when the anti node reaches out of the map
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