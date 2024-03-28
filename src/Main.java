import java.io.*;
import java.util.Scanner;


//See bottom of the file for to do list

public class Main {
    private static final String fileName = "very important data.txt";

    /*
    Below isInteger method, as the name suggests, checks whether a String, that is passed to it, is
    an integer, and returns a boolean.
    */
    private static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
    Below saveToFile method is used to save variables from Main to a text file. As of now it needs the file
    to already contain that data in order to find the right line to overwrite with value that is passed onto it.
    It also only takes int type, so it definitely could use some work in the future.
    */
    private static void saveToFile(String s, int i) {

        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                if (line.endsWith(s)) {
                    try (FileWriter writer = new FileWriter(fileName)) {

                        writer.write(i + " " + s + "\n");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static int readFromFile(String s) {

        String value = "";
        int i = 1;
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                if (line.endsWith(s)) {

                    for (int j = 0; j <= line.length(); j++) {

                        if (line.charAt(j) == ' ') break;
                        value += line.charAt(j);
                    }
                    break;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        i = Integer.parseInt(value);

        return i;
    }

    public static void main(String[] args) {
        //Declarations of necessary variables
        Scanner scanner = new Scanner(System.in);
        int itemNr = 0;
        String userInput;
        int optionNr;
        int whileHolder = 1;

        System.out.println("Running \"toDoList\"...");
        itemNr = readFromFile("itemNr");
        System.out.println("Current size of the list: " + itemNr);

        while (whileHolder == 1) {
            System.out.println("\nOption selection.\nIn order to pick an option please input the corresponding number.");
            System.out.println("1. Create new list (overwrites any existing list)");
            System.out.println("2. Display list");
            System.out.println("3. Add to list");
            System.out.println("4. Remove from list");
            System.out.println("5. Exit");

            userInput = scanner.nextLine();

            if (isInteger(userInput)) {
                optionNr = Integer.parseInt(userInput);

                switch (optionNr) {
                    case 1:
                        itemNr = ToDoItem.createList(scanner);
                        saveToFile("itemNr", itemNr);
                        if (itemNr == 10) System.out.println("Your list is full!");
                        else System.out.println("List created successfully!");
                        break;

                    case 2:
                        ToDoItem.readList();
                        break;

                    case 3:
                        itemNr = ToDoItem.addItem(itemNr, scanner);
                        saveToFile("itemNr", itemNr);
                        break;

                    case 4:
                        itemNr = ToDoItem.removeItem(itemNr, scanner);
                        System.out.println(itemNr);
                        break;

                    case 5:
                        whileHolder++;
                        System.out.println("Exiting program...");
                        break;

                    default:
                        System.out.println("Incorrect input (default in switch statement)");
                        break;


                }
            } else System.out.println("Incorrect input (else statement)");
        }

        saveToFile("itemNr", itemNr);
        scanner.close();

    }


}

class ToDoItem {
    static String[] thingsToDo = new String[10]; //Declaration of an array of String type variables. I is only used in the createList method.
    static String fileName = "to do list.txt";
    boolean isComplete;     //this variable is obsolete as of now


    /*
    Below readList method simply reads the contents of the text file 'to do list.txt'
    */
    public static void readList() {

        System.out.println("To do:\n");
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    Below addItem method takes the itemNr int from the main program and uses it to
    find the place on the list where it is going to add another item. It also saves
    the added item to the file, where the list is stored. At the end it returns
    int itemNr (incremented by the amount equal to the number of added positions)
    to the main program.
    */
    public static int addItem(int itemNr, Scanner scanner) {

        //Scanner scanner = new Scanner(System.in);                   //Declaration of Scanner class object
        if (itemNr == 10) {
            System.out.println("Your list is already full!");
            return itemNr;
        }

        System.out.println("To stop the input type \"stop\"");
        System.out.println("Adding position " + (itemNr + 1) + ":");
        try (FileWriter writer = new FileWriter(fileName, true)) {
            for (; itemNr < 10; itemNr++) {
                ToDoItem.thingsToDo[itemNr] = scanner.nextLine();

                if (ToDoItem.thingsToDo[itemNr].equals("stop")) {
                    ToDoItem.thingsToDo[itemNr] = null;
                    break;
                }

                writer.write((itemNr + 1) + ". " + ToDoItem.thingsToDo[itemNr] + "\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //scanner.close();

        return itemNr;
    }


    /*
    Below removeItem method takes the itemNr int from the main program and uses it
    to locate the last position on the list which is then removed. This is done
    by creating a new text file and rewriting the original to do list.txt to it.
    New file is then renamed to to do list.txt, and the original file is deleted.
    This method returns int itemNr decremented by 1 to the main program, unless
    itemNr already equals one, in which case the method ends at it's very beginning
    and returns the same value that it originally received.
    */

    /*
    Idea for expanding this method - rewrite it so that it saves contents of the file to an array.
    Once in an array - fixes the numbers of positions to account for some of them being deleted from
    the middle of the list, and then saves fixed array back to the file.
    */
    public static int removeItem(int itemNr, Scanner scanner) {

        if (itemNr == 0) {
            System.out.println("Your list is already empty!");
            return itemNr;
        }

        //int [] arr;   this is gonna be useful to expand this method
        //Scanner scanner = new Scanner(System.in);
        String currentLine = null;
        int lineNr = 1;

        try {
            File inputFile = new File(fileName);
            File tempFile = new File("tempFile.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            while ((currentLine = reader.readLine()) != null) {

                if (lineNr != itemNr) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
                lineNr++;
            }

            writer.close();
            reader.close();

            if (inputFile.delete()) {
                if (!tempFile.renameTo(inputFile)) {
                    System.out.println("Could not rename the temporary file.");
                } else {
                    System.out.println("Could not delete the original file");
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (itemNr == 1) System.out.println("Your list has been emptied.");

        //scanner.close();

        return --itemNr;
    }


    /*
    Below createList method is used to set up a list. It takes user input,
    stores it in an array and later saves it to text file to do list.txt.
    It returns int itemNr which is equal to the number of the last position
    on the list to the main program.
    */
    public static int createList(Scanner scanner) {

        int itemNr = 0;
        //Scanner scanner = new Scanner(System.in);

        System.out.println("This todolist will take up to 10 positions. To stop the input type \"stop\"");
        while (itemNr <= 9) {
            ToDoItem.thingsToDo[itemNr] = scanner.nextLine();

            if (ToDoItem.thingsToDo[itemNr].equals("stop")) {

                ToDoItem.thingsToDo[itemNr] = null;
                break;
            }

            itemNr++;
        }

        //Saving to file happens below. File is located in this project's folder.
        try (FileWriter writer = new FileWriter("to do list.txt")) {
            for (int i = 0; i < itemNr; i++) {
                writer.write((i + 1) + ". " + ToDoItem.thingsToDo[i] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //scanner.close();

        return itemNr;
    }

}

    /*
    TO DO LIST:
    1.Store thingsToDo in external file DONE
    2.Change the code to make more sense - class ToDoItem is pretty much useless right now DONE
    3.Create a method that reads from file DONE
    4.Create a method that adds positions to the list DONE
    5.Create a method that changes positions in the list (maybe adds information that the position has already been completed)
    6.Add a menu DONE
    7.Method addItem() doesn't work correctly. If it is used before createList() it doesn't receive the right value through
    int itemNr (it is set to 0 at the start of the program). This needs fixing. DONE I THINK
    8. Add a method that removes item from the list DONE
    9. Make removeItem() capable of removing more than just the last position from the list. This could be done easily by
    passing an array of ints as an argument instead of a single int.
    9. Add a method that shuffles items on the list (can be random, can be ordered). This might be challenging.
    10. MAKE PROPER NOTES!!!!!!!!!!!!!!!!!!!!!!!!
     */