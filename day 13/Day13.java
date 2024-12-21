import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Jama.Matrix;



public class Day13 {
    public static void main(String[] args) {
        part2();
        
    }

    public static void part1() {
        File file = new File("day13_input.txt");
        Pattern pattern = Pattern.compile("X[+=]?(\\d+),\\s*Y[+=]?(\\d+)");
        double cheapestTokenPrice = 0;
    
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String lineA = scanner.nextLine();
                Matcher matcherA = pattern.matcher(lineA);
                matcherA.find();
                Button btnA = new Button(Double.parseDouble(matcherA.group(1)), Double.parseDouble(matcherA.group(2)));
    
                String lineB = scanner.nextLine();
                Matcher matcherB = pattern.matcher(lineB);
                matcherB.find();
                Button btnB = new Button(Double.parseDouble(matcherB.group(1)), Double.parseDouble(matcherB.group(2)));
    
                String linePrize = scanner.nextLine();
                Matcher matcherPrize = pattern.matcher(linePrize);
                matcherPrize.find();
                Button prize = new Button(Double.parseDouble(matcherPrize.group(1)), Double.parseDouble(matcherPrize.group(2)));

                double stratA = calculateStrategy(btnA, btnB, prize, 3, 1, 100);
                double stratB = calculateStrategy(btnB, btnA, prize, 1, 3, 100);

                cheapestTokenPrice += getTokenPrice(stratA, stratB);
                // Skip empty line
                if (scanner.hasNextLine()) scanner.nextLine();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    
        System.out.println("Cheapest Price: " + cheapestTokenPrice);
    }
    public static void part2() {
        File file = new File("day13_input.txt");
        Pattern pattern = Pattern.compile("X[+=]?(\\d+),\\s*Y[+=]?(\\d+)");
        double cheapestTokenPrice = 0;
    
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String lineA = scanner.nextLine();
                Matcher matcherA = pattern.matcher(lineA);
                matcherA.find();
                Button btnA = new Button(Double.parseDouble(matcherA.group(1)), Double.parseDouble(matcherA.group(2)));
    
                String lineB = scanner.nextLine();
                Matcher matcherB = pattern.matcher(lineB);
                matcherB.find();
                Button btnB = new Button(Double.parseDouble(matcherB.group(1)), Double.parseDouble(matcherB.group(2)));
    
                String linePrize = scanner.nextLine();
                Matcher matcherPrize = pattern.matcher(linePrize);
                matcherPrize.find();
                Button prize = new Button(Double.parseDouble(matcherPrize.group(1)), Double.parseDouble(matcherPrize.group(2)));

                prize.x += 10000000000000.0;
                prize.y += 10000000000000.0;

                //System.out.println("A : " + btnA + " B: " + btnB + " Prize: " + prize);

                
                cheapestTokenPrice += calculateStrategyUnlimited(btnA, btnB, prize);
    
                // Skip empty line
                if (scanner.hasNextLine()) scanner.nextLine();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    
        System.out.println("Cheapest Price: " + String.format("%f",cheapestTokenPrice));
    }
    public static double calculateStrategy(Button primary, Button secondary, Button prize, double primaryCost, double secondaryCost, double limit) {
        double minCost = Double.MAX_VALUE;

        // Exhaustively try all combinations of primary and secondary button presses
        for (double numPrimary = 0; numPrimary <= limit; numPrimary++) {
            for (double numSecondary = 0; numSecondary <= limit; numSecondary++) {
                double totalX = numPrimary * primary.x + numSecondary * secondary.x;
                double totalY = numPrimary * primary.y + numSecondary * secondary.y;

                // Check if the combination matches the prize coordinates
                if (totalX == prize.x && totalY == prize.y) {
                    double cost = numPrimary * primaryCost + numSecondary * secondaryCost;
                    minCost = Math.min(minCost, cost);
                }
            }
        }

        return minCost;
    }

    public static double calculateStrategyUnlimited(Button btnA, Button btnB, Button prize) {
        double[] presses = getConstants(btnA, btnB, prize);
        System.out.print("calc: ");
        if (presses == null) return 0;
        //System.out.println( " A Pressed : " + presses[0] + " B Presses: " +presses[1]);
        double num = Math.round(presses[0] * 3) + Math.round(presses[1]);
        System.out.println(num);
        return num;
    }

    public static double[] getConstants(Button btnA, Button btnB, Button prize) {
        
        double x1 = btnA.x;
        double x2 = btnB.x;
        double y1 = btnA.y;
        double y2 = btnB.y;
        double px = prize.x;
        double py = prize.y;
        double[][] data = {
            {x1,x2},
            {y1,y2}
        };
        double[][] prizeData = {
            {px},
            {py}
        };
        Matrix A = new Matrix(data);

        if (A.det() == 0) return null;
        Matrix B = new Matrix(prizeData);
        Matrix x = A.solve(B);

        double c1 = x.get(0,0);
        double c2 = x.get(1,0);

        c1 = Math.round(c1);
        c2 = Math.round(c2);
    
        if (c1 * x1 + c2 * x2 != px) return null;
        if (c1 * y1 + c2 * y2 != py ) return null;
       
            
        return new double[] {c1, c2};
        //return new double[] {c1, c2};
    }

    public static double getTokenPrice(double stratA, double stratB) {
        if (stratA == Double.MAX_VALUE && stratB == Double.MAX_VALUE) return 0;
        return Math.min(stratA, stratB);
    }
}

class Button {
    public double x;
    public double y;

    public Button(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "X:+" + x + " Y:+" + y;
    }
}