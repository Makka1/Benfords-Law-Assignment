/*
 * Date: May 6, 2021
 * Names: Johnny He and Tiffany Liang
 * Teacher: Mr. Ho
 * Description: A sales analysis system that allows the user to load the sales data, checks if fraud has likely occured or not, 
 *              and prints a graph + table of the first digit distribution results 
 * */

import java.io.File;
import java.io.FileNotFoundException; 
import java.util.Scanner;
import java.io.PrintWriter;
//Importing JavaFX applications 
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BenfordsLaw extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        String fraudCheck = "";
        double[] takeInput = new double[9];

        System.out.println("Sales Analysis System");
        System.out.println(); 

        // Open spreadsheet/file
        String fileName = "sales.csv";

        // Create file instance to reference the file
        File data = new File(fileName);

        // Read the file using scanner 
        Scanner scnr = new Scanner(data);

        // Array of first digit counts in the file
        int[] count = countLeadingDigits(scnr);

        // End message
        String endProgram = "End of program";
        
        // Prompt user to continue program/load file or quit program
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter y to read file or n to quit:"); 
        String start = reader.nextLine();
        if (start.equals("y")){
            // Prompt user to check for fraud or quit program
            System.out.println("Enter y to check for possible accounting fraud or n to quit:");
            fraudCheck = reader.nextLine(); 
            if (fraudCheck.equals("y")){
                takeInput = resultsOfFile(count);
                printTable(count); 
            }
            else if (fraudCheck.equals("n")){
                System.out.println(endProgram);
            }
            else if (start.equals("n")){
                System.out.println(endProgram);
            }
        }
        else if (start.equals("n")){
            System.out.println(endProgram);
        }

        // Invalid user response, prompt until valid
        else{
            while(!start.equals("y") && !start.equals("n")){
                System.out.println("Invalid input. Try again");
                System.out.println("Enter y to read file or n to quit:"); 
                start = reader.nextLine();
                if (start.equals("y")){
                    // Prompt user to check for fraud or quit program
                    System.out.println("Enter y to check for possible accounting fraud or n to quit:"); 
                    fraudCheck = reader.nextLine(); 
                    // Invalid user response, prompt until valid
                    while (!fraudCheck.equals("y") && !fraudCheck.equals("n")){
                        System.out.println("Invalid input. Try again");
                        System.out.println("Enter y to check for possible accounting fraud or n to quit:");
                        fraudCheck = reader.nextLine();
                        if (fraudCheck.equals("y")){
                            takeInput = resultsOfFile(count);
                            printTable(count); 
                        }
                        else if (fraudCheck.equals("n")){
                            System.out.println(endProgram);
                            break;
                        }
                    }
                }
                else if (start.equals("n")){
                    System.out.println(endProgram);
                    
                }
            }
        }

        if ((start.equals("y") && (fraudCheck.equals("y")))) {
            stage.setTitle("Graph Stage");
            //x axis
            CategoryAxis x = new CategoryAxis();
            x.setLabel("Digit");
            //y axis
            NumberAxis y = new NumberAxis();
            y.setLabel("Percentage");
            //bar chart creation
            BarChart analysisLawGraph = new BarChart(x, y);
            analysisLawGraph.setTitle("Benford's Distribution Leading Digit");
            //add values
            XYChart.Series benfordGraph = new XYChart.Series();
            benfordGraph.getData().add(new XYChart.Data("1", takeInput[0] ));
            benfordGraph.getData().add( new XYChart.Data("2", takeInput[1] ));
            benfordGraph.getData().add(new XYChart.Data("3", takeInput[2] ));
            benfordGraph.getData().add(new XYChart.Data("4", takeInput[3] ));
            benfordGraph.getData().add(new XYChart.Data("5", takeInput[4] ));
            benfordGraph.getData().add(new XYChart.Data("6", takeInput[5] ));
            benfordGraph.getData().add(new XYChart.Data("7", takeInput[6] ));
            benfordGraph.getData().add(new XYChart.Data("8", takeInput[7] ));
            benfordGraph.getData().add(new XYChart.Data("9", takeInput[8] ));
            analysisLawGraph.getData().add(benfordGraph);
            analysisLawGraph.setLegendVisible(false);
            //vertical box
            VBox vbox = new VBox(analysisLawGraph);
            Scene sc = new Scene(vbox, 800, 700);
            stage.setScene(sc);
            stage.setHeight(500);
            stage.setWidth(600);
            stage.show();   
        }   
    }
    
    public static void main(String[] args) throws FileNotFoundException{
        Application.launch(args);
    }

    /*
     * Author: Tiffany Liang
     * Reads the file and an array "receives" the counts/accumulations of each leading digit occurence
     * @param scnr - scanner that reads the sales file
     * @return count - integer array of each first digit's number of occurences
     * */
    public static int[] countLeadingDigits(Scanner scnr){
        // 'Empty' array of leading digits 
        int[] count = new int[10];

        while(scnr.hasNextLine()){
            // Get the first digit of the line (the element that is going to receive the "counts")
            int n = firstDigit(scnr.nextLine());

            // Accumulate (+1) for each time that digit occurs
            count[firstDigitOfRow(n)]++;
        }
        return count;
    }

    /*
     * Author: Tiffany Liang
     * Divides integer to find the first digit of sales #
     * @param n - the sales # of a row from the file
     * @return result - the first digit of the #
     * */
    public static int firstDigitOfRow(int n){
        int result = n;
        while(result >= 10){
            result /= 10;
        }
        return result;
    }
    
    /*
     * Author: Tiffany Liang
     * Returns the leading digit of each sale/line
     * @param fileName - the sales file string
     * @return - the first digit or 0
     * */
    public static int firstDigit(String fileName){
        // Loop that runs through each element/leading digit of the file (sales #'s start on 4th index/element)
        for (int i = 4; i < fileName.toCharArray().length; i++){
            char n = fileName.toCharArray()[i];
            if (n >= '1' && n <= '9'){
                // Returns n as the same character, in the form of a digit/integer
                return n - '0'; 
            }
        }
        // If leading digit is not 1-9
        return 0;
    }
    
    /*
     * Author: Tiffany Liang
     * Calculates each first digit's frequency percentage and number of occurences, then prints if fraud has likely occured or not
     * @param count - the array of counts for each first digit that occurs in the file
     * no return
     * */
    public static double[] resultsOfFile(int[] count){
        System.out.println(); // Spacing
        
        // Total rows in csv file
        int total = 1620;
        double[] percentage = new double[9];

        System.out.println("Result:");

        // Loops through array of first digits and calculates each percentage
        for (int i = 0; i < count.length-1; i++){
            double percent = (count[i+1] * 100.0) / total;
            percentage[i] = percent;

            // First digit frequency is between 29% and 32%
            if (percent > 29 && percent < 32){
                System.out.println("Fraud has likely NOT occured");
            }
        }
        return percentage;
    }

    /*
     * Author: Tiffany Liang
     * Iterates through array, takes in the percentage for each first digit, then prints it out as a table to results.csv file 
     * @param count - the array of counts for each first digit that occurs in the file
     * no return
     * */
    public static void printTable(int[] count) throws FileNotFoundException{
        // Create file instance to reference the file
        File data = new File("results.csv");

        // "Sends out" the data into the csv file
        PrintWriter out = new PrintWriter(data);

        // Loops through array of first digits and calculates each percentage
        for (int i = 1; i < count.length; i++){
            double percent = (count[i] * 100.0) / 1620; // 1620 is the total # of rows
            double percentRounded = Math.round(percent * 10) / 10.0; // Rounds % one decimal place
            out.println(i + " = " + percentRounded + "%");
        }   
        System.out.println("Table has been generated");
        out.close();
    }
}