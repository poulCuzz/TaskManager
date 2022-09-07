package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {

    public static void main(String[] args) throws IOException {
        String[] options = {"add", "remove", "list", "exit"};
        String[][] tasks = tasks();
        System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.WHITE_BRIGHT);
        for (String option : options) {
            System.out.println(option);
        }
        Scanner scan = new Scanner(System.in);

        while (true) {

            String input = scan.next();

            switch (input) {
                case "add":
                    System.out.println("Please add task description:");
                    scan.nextLine();
                    String taskDescription = scan.nextLine();
                    System.out.println("Please add task due date:");
                    String taskDate = scan.next();
                    System.out.println("Is your task is important - true/false:");
                    String taskImportance = scan.next();
                    String[] task = {taskDescription, taskDate, taskImportance};
                    tasks = addTask(tasks, task);
                    break;
                case "remove":
                    System.out.println("Please select the number to remove:");
                    int indexInt = 0;
                    while (true) {
                        try {
                            scan.reset();
                            String index = scan.next();
                            indexInt = Integer.parseInt(index);
                            tasks = remove(tasks, indexInt);
                            break;
                        }catch (NumberFormatException e) {
                            System.out.println("It has to be a number, try again:");
                        }catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "list":
                    list(tasks);
                    break;
                case "exit":
                    exitAndSave(tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye");
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }
            if (input.equals("exit")) {
                break;
            }
            System.out.println(ConsoleColors.BLUE +  "select next option:" + ConsoleColors.WHITE_BRIGHT);
        }


    }
    static String[][] addTask (String[][] tasks, String[] task) {
        String[][] modifiesTasks = Arrays.copyOf(tasks, tasks.length + 1);
            modifiesTasks[modifiesTasks.length - 1] = task;
        return modifiesTasks;
    }
    static String[][] remove (String[][] tasks,int index) throws FileNotFoundException {
        try {
            return ArrayUtils.remove(tasks, index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("index out of bounds" + " you can't do this operation");
            return tasks;
        }
    }
    static void list (String[][] tasks) {
        for (int i = 0; i < tasks.length; i++) {
            System.out.print("(nr" + i + ")" + " :: ");
            for (int j = 0; j < tasks[i].length; j++) {
                if (j == (tasks[i].length - 1)) {
                    System.out.print(tasks[i][j]);
                    break;
                }
                System.out.print(tasks[i][j] + ", ");
            }
            System.out.println();
        }

    }
    static String[][] tasks () throws FileNotFoundException {

        File file = new File("tasks.csv");
        Scanner scan = null;
        Scanner scan1 = null;
        int lineCounter = 0;
        int columnCounter = 3;
        try {
            scan = new Scanner(file);
            while (scan.hasNextLine()) {
                scan.nextLine();
                lineCounter++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scan1 = new Scanner(file);
        String[] allLines = new String[lineCounter];
        for (int i = 0; i < lineCounter; i++) {
            allLines[i] = scan1.nextLine();
        }
        String[][] tasks = new String[lineCounter][columnCounter];

        for (int i = 0; i < lineCounter; i++) {
            for (int j = 0; j < columnCounter; j++) {
                tasks[i][j] = allLines[i].split(", ")[j];
            }
        }
        return tasks;
    }
    static void exitAndSave (String[][] tasks) {
        try (FileWriter fileWriter = new FileWriter("tasks.csv", false)) {
            for (int i = 0; i < tasks.length; i++) {
                for (int j = 0; j < 3; j++) {
                    if (j == 2) {
                        fileWriter.append(StringUtils.replace(tasks[i][j], ", ", ""));
                        break;
                    }
                    fileWriter.append(tasks[i][j]).append(", ");
                }
                fileWriter.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
