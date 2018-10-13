
package ixs171130;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

//Driver program for skip list implementation.

public class SkipListDriver {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        Long returnValue = null;
        SkipList<Long> skipList = new SkipList<>();
        // Initialize the timer
        Timer timer = new Timer();
        int index = 0;
        while (!((operation = sc.next()).equals("End"))) {
            index++;
            switch (operation) {
                case "Add": {
                    operand = sc.nextLong();
                    if (skipList.add(operand)) {
                        result = (result + 1) % modValue;
                        System.out.println(index + " Add : " + 1);
                    }
                    else {
                        System.out.println(index + " Add : " + 0);
                    }
                    break;
                }
                case "Ceiling": {
                    operand = sc.nextLong();
                    returnValue = skipList.ceiling(operand);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                        System.out.println(index + " Ceiling : " + returnValue);
                    }
                    else {
                        System.out.println(index + " Ceiling : " + returnValue);
                    }
                    break;
                }
                case "First": {
                    returnValue = skipList.first();
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    System.out.println(index + " First : " + returnValue);
                    break;
                }
                case "Get": {
                    int intOperand = sc.nextInt();
                    returnValue = skipList.get(intOperand);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    System.out.println(index + " Get : " + returnValue);
                    break;
                }
                case "Last": {
                    returnValue = skipList.last();
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    System.out.println(index + " Last : " + returnValue);
                    break;
                }
                case "Floor": {
                    operand = sc.nextLong();
                    returnValue = skipList.floor(operand);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    System.out.println(index + " Floor : " + returnValue);
                    break;
                }
                case "Remove": {
                    operand = sc.nextLong();
                    if (skipList.remove(operand) != null) {
                        result = (result + 1) % modValue;
                        System.out.println(index + " Remove : " + 1);
                    }
                    else {
                        System.out.println(index + " Remove : " + 0);
                    }
                    break;
                }
                case "Contains": {
                    operand = sc.nextLong();
                    if (skipList.contains(operand)) {
                        result = (result + 1) % modValue;
                        System.out.println(index + " Contains : " + 1);
                    }
                    else {
                        System.out.println(index + " Contains : " + 0);
                    }

                    break;
                }

            }
        }

        // End Time
        timer.end();

        System.out.println(result);
        System.out.println(timer);
    }
}
