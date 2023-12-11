package TetrisHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import TetrisRunners.TetrisFrame;
import javafx.util.Pair;

public class LeaderboardManager {

    // these array lists are used to represent a name and score column
    // to hold the name values
    ArrayList<String> names = new ArrayList<>();
    // to hold the scores associated to the name
    ArrayList<Integer> scores = new ArrayList<>();
    ArrayList<Pair<String,Integer>> pairList = new ArrayList<>();

    String curLine;
    String[] values;

    File text;
    
    public LeaderboardManager(TetrisFrame frame){        
        try{
            text = new File("C:\\Users\\boeaz\\Documents\\Tetris\\Tetris\\src\\TetrisHelper\\HS.csv");
            Scanner scnr = new Scanner(text);
            while(scnr.hasNextLine()){
                curLine = scnr.nextLine();
                values = curLine.split(",");
                pairList.add(new Pair(values[0], Integer.parseInt(values[1])));
            }
            scnr.close();
        } catch(IOException e){
            System.out.println("could not read HS.csv");
            e.printStackTrace();
        }
        
        frame.drawLeaderboard(pairList);
    }

    public void bubbleSort(ArrayList<Pair<String,Integer>> list){
        
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                // compare values from largest to smallest
                if (list.get(j).getValue() < list.get(j + 1).getValue()) {
                    // swap elements if in wrong order
                    Pair<String, Integer> temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    public ArrayList<Pair<String,Integer>> getLeaderboard(){
        return pairList;
    }

    
}
