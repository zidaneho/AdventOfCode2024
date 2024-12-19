import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Scanner;

public class Day9 {
    public static void main(String[] args) {
        File file = new File("day9_input.txt");
        char[] disk = new char[0];

        try {
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            disk = line.toCharArray();
            scanner.close();
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
        }

        //String[] formattedDisk = convertToFormat(disk);
        //printList(formattedDisk);
        String[] formattedDisk2 = sortFormat2(disk);
        //sortFormat(formattedDisk);
        printList(formattedDisk2);
        System.out.println("SUM: "+getChecksum(formattedDisk2));
        
    }

    public static String[] convertToFormat(char[] disk) {
        ArrayList<String> list = new ArrayList<>();
        int fileID = 0;
        for (int i = 0; i < disk.length; i++) {
            int num = Character.digit(disk[i],10);
           
            if (isFile(i)) {
                for (int j = 0; j < num;j++) {
                    list.add(String.valueOf(fileID));
                }
                
                fileID++;
            }
            else {
                for (int j = 0; j < num; j++) {
                    list.add(".");
                }
            }
        }
        return list.toArray(new String[0]);
    }
    //we can assume the first digit is always a file
    public static void sortFormat(String[] list) {
        for (int i = list.length - 1; i >= 0; i--) {
            // Check if the current block is a file (not free space)
            if (Character.isDigit(list[i].charAt(0))) {
                // Find the first free space to the left
                for (int j = 0; j < i; j++) {
                    if (list[j].equals(".")) {
                        // Move file block to the free space
                        list[j] = list[i];
                        list[i] = "."; // Mark the current position as free space
                        break; // Only move the file once
                    }
                }
            }
        }
    }
    
    public static String[] sortFormat2( char[] unsortedList) {
        ArrayList<Block> blocks = new ArrayList<>();
        int fileID = 0;
        for (int i = 0; i < unsortedList.length; i++) {
            blocks.add(new Block(isFile(i),fileID,Character.digit(unsortedList[i],10)));
            if (isFile(i))fileID++;
        }
        //printBlocks(blocks);
        for (int i = blocks.size() - 1; i >= 0; i--) {
            Block block1 = blocks.get(i);
            if (block1.isFile()) {
                for (int j = 0; j < i; j++) {
                    Block block2 = blocks.get(j);
                    if (!block2.isFile() && block1.getSize() <= block2.getSize()) {
                        int difference = block2.getSize() - block1.getSize();
                        block2.setSize(block1.getSize());
                        block2.setId(block1.getId());
                        block2.setIsFile(true);
                        block1.setIsFile(false);
                        if (difference > 0) {
                            blocks.add(j + 1,new Block(false, block2.getId(),difference));
                        }
                        break;
                    }
                    
                    //printBlocks(blocks);
                }
            }
            
        }
        //printBlocks(blocks);
        ArrayList<String> newList = new ArrayList<>();
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            for (int j = 0; j < block.getSize(); j++) {
                if (block.isFile()) {
                    newList.add(String.valueOf(block.getId()));
                }
                else {
                    newList.add(".");
                }
            }
        }
        return newList.toArray(new String[0]);
         
        
    }
    public static void printBlocks(ArrayList<Block> blocks) {
        System.out.println("Block: ");
        for (Block block: blocks) {
            for (int i = 0; i < block.getSize(); i++) {
                if (block.isFile()) {
                    System.out.print(block.getId());
                }
                else {
                    System.out.print(".");
                }
            }
            System.out.print(" ");
        }
        System.out.println();
    }
    
    public static int indexOf(String[] list, String element) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }
    //if false, it is free space
    public static boolean isFile(int i) {
        return i % 2 == 0;
    }
    public static void printList(String[] list) {
        for (String c : list) {
            System.out.print(c + " ");
        }
        System.out.println();
    }
    public static long getChecksum(String[] list) {
        long sum = 0;    
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(".")) continue;
            long num = Integer.parseInt(list[i]);
            sum += i * num;
        }
        return sum;
    }
   
}

class Block {
    private boolean isFile; //if not it is a free space
    private int id;
    private int size;

    public Block() {

    }
    public Block(boolean isFile, int id, int size) {
        this.isFile = isFile;
        this.id = id;
        this.size = size;
    }
    public boolean isFile() {
        return isFile;
    }
    public int getId() {
        return id;
    }
    public int getSize() {
        return size;
    }
    public void setIsFile(boolean value) {
        isFile = value;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setSize(int size) {
        this.size = size;
    }
}


