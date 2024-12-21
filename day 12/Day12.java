import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Day12 {
    public static void main(String[] args) {
        part2();
        
    
    }
    public static void part1() {
        Character[][] input = getInput();
        int regionID = 0;
        HashMap<String,Region> regions = new HashMap<>();
        boolean[][] visitedArea = new boolean[input.length][input[0].length];
        boolean[][] visitedPerimeter = new boolean[input.length][input[0].length];
        for (int r = 0; r < input.length; r++) {
            for (int c = 0; c < input[r].length; c ++) {
                String key = c + "-" + regionID;
                if (!visitedArea[r][c] && !visitedPerimeter[r][c]) {
                    int area = getArea(input, visitedArea, r, c);
                    int perimeter = getPerimeter(input, visitedPerimeter, r,c,input[r][c]);
                    Region reg = new Region(area, perimeter);
                    regions.put(key,reg);
                    regionID++;
                }
                
            }
        }
        long sum = 0;
        for (Region reg: regions.values()) {
            sum += reg.getArea() * reg.getPerimeter();
        }
        System.out.println("SUM : " + sum);
    }
    public static void part2() {
        Character[][] input = getInput();
        int regionID = 0;
        HashMap<String,Region> regions = new HashMap<>();
        boolean[][] visitedArea = new boolean[input.length][input[0].length];
        boolean[][] visitedSides = new boolean[input.length][input[0].length];
        for (int r = 0; r < input.length; r++) {
            for (int c = 0; c < input[r].length; c ++) {
                String key = c + "-" + regionID;
                if (!visitedArea[r][c] && !visitedSides[r][c]) {
                    int area = getArea(input, visitedArea, r, c);
                    int sides = getSides(input, visitedSides, r,c);
                    //System.out.println("area : " +area + " sides: " + sides);
                    Region reg = new Region(area, 0,sides);
                    regions.put(key,reg);
                    regionID++;
                }
                
            }
        }
        long sum = 0;
        for (Region reg: regions.values()) {
            sum += reg.getArea() * reg.getSides();
        }
        System.out.println("SUM : " + sum);
    }
    public static Character[][] getInput() {
        File file = new File("day12_input.txt");
        ArrayList<Character[]> arr = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                arr.add(new Character[line.length()]);
                for (int j = 0; j < line.length(); j++) {
                    arr.get(i)[j] = line.charAt(j);
                }
                i++;
            }
        }
        catch (Exception e) {
            System.out.println("ERROR:" + e.getMessage());
        }
        return arr.toArray(new Character[0][0]);
    }

    public static int getArea(Character[][] input, boolean[][] visited, int r, int c) {
        if (visited[r][c]) return 0;

        visited[r][c] = true;
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        char target = input[r][c];
        int sum = 1;
        for (int[] dir : directions) {
            if (isIndexValid(r + dir[0], c + dir[1], input) && input[r + dir[0]][c + dir[1]] == target) {
                sum += getArea(input, visited, r + dir[0], c + dir[1]);
            }
        }
        return sum;
    }
    public static int getPerimeter(Character[][] input, boolean[][] visited, int r, int c, char target) {
        

        if (!isIndexValid(r, c, input) || input[r][c] != target) {
            return 1;
        }
        if (visited[r][c]) return 0;

        visited[r][c] = true;
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        
        int sum = 0;
        for (int[] dir : directions) {
            sum += getPerimeter(input, visited, r + dir[0], c + dir[1],target);
            
        }
        return sum;
    }
    
    public static boolean isIndexValid(int r, int c, Character[][] input) {
        return (r>=0 && r < input.length && c >= 0 && c < input[r].length);
    }
    public static boolean isVertexValid(Character[][] input, int r, int c, char target) {
        return isIndexValid(r, c, input) && input[r][c] == target;
    }
    public static int getSides(Character[][] input, boolean[][] visited, int r, int c) {
        ArrayList<Vertex> northVertices = new ArrayList<>();
        ArrayList<Vertex> westVertices = new ArrayList<>();
        ArrayList<Vertex> eastVertices = new ArrayList<>();
        ArrayList<Vertex> southVertices = new ArrayList<>();

        ArrayList<Vertex> vertices = new ArrayList<>();
        getRegion(vertices, input, r, c, input[r][c],visited);

        for (Vertex v : vertices) {
            if (v.isNorthVertex(input)) {
                northVertices.add(v);
                
            }
            if (v.isEastVertex(input)) {
                eastVertices.add(v);
            }
            if (v.isWestVertex(input)) {
                westVertices.add(v);
                
            }
            if (v.isSouthVertex(input)) {
                southVertices.add(v);
              
            }
            //System.out.println(v.toString(input));
        }
        
        int nEdges = getEdges(northVertices, true);
        int wEdges = getEdges(westVertices, false);
        int sEdges = getEdges(southVertices, true);
        int eEdges = getEdges(eastVertices, false);
        //System.out.println("n : " + nEdges + " w: " + wEdges + " s: " + sEdges + " e: " + eEdges);
        return  nEdges + wEdges + sEdges + eEdges;
    }


    public static void getRegion(ArrayList<Vertex> vertices, Character[][] input, int r, int c, char target, boolean[][] visited) {
        if (!isIndexValid(r, c, input) || (isIndexValid(r, c, input) && input[r][c] != target)) return;
        if (visited[r][c]) return;
        visited[r][c] = true;
        if (input[r][c] == target) {
            vertices.add(new Vertex(r,c));
        }

        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] dir : directions) {
            getRegion(vertices, input, r + dir[0], c + dir[1], target, visited);
        }
    }

    public static int getEdges(ArrayList<Vertex> vertices, boolean shareRows) {
        // Create a map to store the row/column as the key and the list of indices (row/column) as the value
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        
        // Group vertices by row or column based on the shareRows flag
        for (Vertex v : vertices) {
            
            if (shareRows) {
                // Initialize list if it's not already present
                map.putIfAbsent(v.getRow(), new ArrayList<Integer>());
                map.get(v.getRow()).add(v.getCol());
            } else {
                // Initialize list if it's not already present
                map.putIfAbsent(v.getCol(), new ArrayList<Integer>());
                map.get(v.getCol()).add(v.getRow());
                //System.out.println(v.getRow() + "," + v.getCol());
            }
        }
    
        int edges = 0;
        //System.out.print("share rows: " + shareRows + " ");
        // Now for each group (rows or columns), count the edges
        for (ArrayList<Integer> list : map.values()) {
            // Sort the list to make it easier to check adjacency
            list.sort(Integer::compareTo);
            int localEdges = 0;
            // Compare consecutive vertices in the sorted list
            for (int i = 1; i < list.size(); i++) {
                //System.out.print(list.get(i) + " ");
                // If the difference between two adjacent vertices is greater than 1, they are not adjacent
                if (Math.abs(list.get(i) - list.get(i - 1)) > 1) {
                    localEdges++;
                }
                
            }
            localEdges++; //account for the last edge
            
            
            //System.out.println();
            
            edges += localEdges;
        }
        
        
        return edges;
    }
   
    
}

class Vertex  {
    private int r;
    private int c;

    public Vertex() {

    }
    public Vertex(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public int getRow() {
        return r;
    }
    public int getCol() {
        return c;
    }
    public boolean isNorthVertex(Character[][] input) {
        return !Day12.isVertexValid(input, r-1, c, input[r][c]);
    }
    public boolean isWestVertex(Character[][] input) {
        return !Day12.isVertexValid(input, r , c-1 , input[r][c]);
            
    }
    public boolean isEastVertex(Character[][] input) {
        return !Day12.isVertexValid(input, r, c +1, input[r][c]);
    }
    public boolean isSouthVertex(Character[][] input) {
        return !Day12.isVertexValid(input, r+1, c, input[r][c]);
    }
    public String toString(Character[][] input) {
        StringBuilder direction = new StringBuilder();
    
        if (isNorthVertex(input)) {
            direction.append("North ");
        }
        if (isSouthVertex(input)) {
            direction.append("South ");
        }
        if (isEastVertex(input)) {
            direction.append("East ");
        }
        if (isWestVertex(input)) {
            direction.append("West ");
        }
        
        // Trim the trailing space
        return "Vertex at " + r + "," + c + " (" + (direction.length() > 0 ? direction.toString().trim() : "None") + ") is "+input[r][c] ;
    }
    
}


class Region {
    private int area;
    private int perimeter;
    private int sides;
    public Region(int area, int perimeter) {
        this.area = area;
        this.perimeter = perimeter;
    }
    public Region(int area, int perimeter, int sides) {
        this.area = area;
        this.perimeter = perimeter;
        this.sides = sides;
    }
    public int getArea() {
        return area;
    }
    public int getPerimeter() {
        return perimeter;
    }
    public int getSides() {
        return sides;
    }
    
}