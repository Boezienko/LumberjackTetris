package TetrisHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javafx.util.Pair;

public class LeaderboardManager {

    // these array lists are used to represent a name and score column
    // to hold the name values
    ArrayList<String> names = new ArrayList<>();
    // to hold the scores associated to the name
    ArrayList<Integer> scores = new ArrayList<>();
    ArrayList<Pair<String,Integer>> plist = new ArrayList<>();

    String curLine;
    String[] values;

    File text;
    
    public LeaderboardManager(){        
        try{
            text = new File("C:\\Users\\boeaz\\Documents\\Tetris\\Tetris\\src\\TetrisHelper\\HS.csv");
            Scanner scnr = new Scanner(text);
            while(scnr.hasNextLine()){
                curLine = scnr.nextLine();
                values = curLine.split(",");
                plist.add(new Pair(values[0], Integer.parseInt(values[1])));
            }
            scnr.close();
        } catch(IOException e){
            System.out.println("could not read HS.csv");
            e.printStackTrace();
        }

        for(Pair<String, Integer> pair : plist){
            System.out.println(pair);
        }
        System.out.println("/////////////////////////////////////////////");
        sort(plist);
    }

    public void sort(ArrayList<Pair<String,Integer>> list){


        int n = list.size();
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compare the Integer values in reverse order (from largest to smallest)
                if (list.get(j).getValue() < list.get(j + 1).getValue()) {
                    // Swap elements if they are in the wrong order
                    Pair<String, Integer> temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }

        for(Pair<String, Integer> pair : plist){
            System.out.println(pair);
        }

        

    }
}
