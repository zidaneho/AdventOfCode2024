import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
    public static void main(String[] args) {
        Robot[] robots = getRobotArray();
        int[][] map = mapRobots(robots);
        printMap(map);
        System.out.println();
        map = moveRobots(100, map, robots);
        int quad1 = 0;
        int quad2 = 0;
        int quad3 = 0;
        int quad4 = 0;
        printMap(map);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                
                if (y == map.length / 2 || x == map[y].length / 2) continue;
                int num = map[y][x];
                if (num < 1) continue;

                if (y > map.length / 2  && x > map[y].length / 2) {
                    quad1 += num;
                }
                else if (y > map.length / 2 && x < map[y].length / 2) {
                    quad2 += num;
                }
                else if (y < map.length / 2 && x < map[y].length / 2) {
                    quad3 += num;
                }
                else if (y < map.length / 2 && x > map[y].length / 2) {
                    quad4 += num;
                }
            }
        }
        System.out.println("Safety Factor: " + (quad1 * quad2 * quad3 * quad4));
    }
    public static Robot[] getRobotArray() {
        File file = new File("day14_input.txt");
        String pattern = "(-?\\b\\d+\\b)";
        ArrayList<Robot> robots = new ArrayList<>();

        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Pattern regex = Pattern.compile(pattern);
                Matcher matcher = regex.matcher(line);

                matcher.find();
                int x = Integer.parseInt(matcher.group());
                matcher.find();
                int y = Integer.parseInt(matcher.group());
                matcher.find();
                int velX = Integer.parseInt(matcher.group());
                matcher.find();
                int velY = Integer.parseInt(matcher.group());
                
               

                Robot robot = new Robot(x,y,velX,velY);
                
                robots.add(robot);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return robots.toArray(new Robot[0]);
    }
    public static int[][] mapRobots(Robot[] robots) {
        int[][] map = new int[7][11];
        for (Robot r : robots) {
            map[r.y][r.x]++;
        }
        return map;
    }
    public static void printMap(int[][] map) {
        for (int[] r : map) {
            for (int c : r) {
                System.out.print (c);
            }
            System.out.println();
        }
    }

    public static int[][] moveRobots(int seconds, int[][] map, Robot[] robots) {
        for (int i = 0; i <seconds;i++) {
            for (Robot r: robots) {
                r.move();
            }
            map = mapRobots(robots);
            //printMap(map);
            //System.out.println();
        }
        return map;
    }
    
}

class Robot {
    private static int width = 11;
    private static int height = 7;

    public int x;
    public int y;

    public int velX;
    public int velY;

    public Robot(int x, int y, int velX, int velY) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
    }
    public void move() {
        if (x + velX < 0) { //x = 3, velX = -5, width = 11, newspot = 9
            int remaining = Math.abs(velX + x);
            x = width -  remaining;
        }
        else if (x + velX >= width) { //x = 9, velX = 4, width = 11, newspot = 1
            int remaining = Math.abs(width - (x + velX));
            x = remaining;
        }
        else {
            x += velX;
        }
        
        if (y + velY < 0) { //x = 3, velX = -5, width = 11, newspot = 9
            int remaining = Math.abs(velY + y);
            y = height -  remaining;
        }
        else if (y + velY >= height) { //x = 9, velX = 4, width = 11, newspot = 1
            int remaining = Math.abs(width - (y + velY));
            y = remaining;
        }
        else {
            y += velY;
        }
    }
   
    public String toString() {
        return "POS : " + x + "," + y + " VEL : " + velX + "," + velY;
    }
}