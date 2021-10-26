/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pf2_randomizer;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 *
 * @author pmele
 */
public class PF2_Randomizer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        PathfinderRoguelikeFrame myFrame = new PathfinderRoguelikeFrame("Pathfinder 2E Roguelike Randomizer");
    }
    
    public static void execute() {
        //Set up all the groups here
        File dir = new File("C:\\Users\\pmele\\Documents\\NetBeansProjects\\PF2_Randomizer_GUI\\Feats");
        File[] directoryListing = dir.listFiles();
        HashMap<String, Group> groups = new HashMap<>();
        for (File child : directoryListing) {
            //Testing line - Comment out if unused
            //System.out.println(child.getName());
            ArrayList<Feat> feats = new ArrayList<>();
            if (child.length() == 0) {
                groups.put(child.getName().split(".csv")[0], new Group(feats));
                return;
            }
            try (Stream<String> lines = Files.lines(Paths.get(child.getPath()), Charset.defaultCharset())) {
                lines.forEachOrdered(line -> feats.add(readCsvLine(line)));
            } catch (Exception e){
                if (!(e instanceof ArrayIndexOutOfBoundsException)) {
                    System.out.println(e);
                    for (StackTraceElement trace : e.getStackTrace()) {
                        System.out.println(trace.toString());
                    }
                }
            }
            groups.put(child.getName().split(".csv")[0], new Group(feats));
        }
        
        Group group;
        Group group2;
        HashMap<String, Group> groupsUpdate = new HashMap<>();
        for (String key : groups.keySet()) {
            group = groups.get(key);
            if (key.endsWith("1")) {
                for (int level = 2; level <= 20; level++) {
                    for (String key2 : groups.keySet()) {
                        try {
                            if (key2.split("_")[0].equals(key.split("_")[0]) && key2.split("_")[1].equals("Feats") && Integer.parseInt(key2.split("_")[2]) == level) {
                                group = group.merge(groups.get(key2));
                                groupsUpdate.put(key + "-" + key2.split("_")[2], group);
                                //Testing line - Comment out if unused
                                System.out.println(key + "-" + key2.split("_")[2]);
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
        groups.putAll(groupsUpdate); 

        
        
        Scanner input = new Scanner(System.in);  // Create a Scanner object
        ArrayList<String> prereqsNotMet = new ArrayList<>();
        ArrayList<String> prereqsMet = new ArrayList<>();
        prereqsMet.add("None"); //Refers to automatically meeting the prereqs of feats that have none
        
        boolean go = true;
        String in;
        while(go) {
            System.out.println("Enter Group: ");
            String selectFrom = input.nextLine();
            int selectAmount = 0;
            boolean tryAgain = true;
            while (tryAgain) {
                try {
                    System.out.println("Enter Number: ");
                    selectAmount = Integer.parseInt(input.nextLine());  // Read user input
                    tryAgain = false;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number! Try again.");
                }
            }
            
            

            ArrayList<Feat> selection = new ArrayList<>();
            boolean prereqsInvalid = false;
            String meetsReq = "";
            
            Feat selected;
            int trials = 0;
            try {
                for (int i = 0; i < selectAmount; i++) {
                    prereqsInvalid = true;
                    selected = null;
                    while (prereqsInvalid || selection.contains(selected)) {
                        selected = groups.get(selectFrom).random();
                        if (selected == null) {
                            System.out.println("Group had no feats to select from!");
                            i = selectAmount;
                            break;
                        }
                        for (String req : selected.getPrereqs()) {
                            if (prereqsNotMet.contains(req)) {
                                prereqsInvalid = true;
                                break;
                            } else if (prereqsMet.contains(req)) {
                                prereqsInvalid = false;
                            } else {
                                System.out.println("Do you meet the prereq: " + req);
                                meetsReq = input.nextLine().toLowerCase();
                                switch(meetsReq) {
                                    case("yes"):
                                    case("ye"):
                                    case("y"):
                                        prereqsMet.add(req);
                                        prereqsInvalid = false;
                                        break;
                                    case("no"):
                                    case("n"):
                                    default:
                                        prereqsNotMet.add(req);
                                        prereqsInvalid = true;
                                        break;
                                }
                                if (prereqsInvalid) {
                                    break;
                                }
                            }
                        }
                        if (trials >= 50*selectAmount) {
                            i = selectAmount;
                            break;
                        }
                        trials++;
                    }

                    if (i != selectAmount) {
                        selection.add(selected);
                    }
                }
            } catch (Exception e) {
                System.out.println("Unrecognized group.");
            }
            String outStr = "Your options are: \n";
            for (int i = 0; i < selection.size(); i++) {
                outStr += selection.get(i).toString();
                if (i != selection.size() - 1) {
                    outStr += ", ";
                }
            }
            System.out.println(outStr);
            System.out.println("---");
            System.out.println("If you wish to quit, type 'Q'.\nIf you wish to merge groups, type 'M'.\nElse type nothing.");
            in = input.nextLine();
            if (in.equalsIgnoreCase("Q")) {
                go = false;
            } else if (in.equalsIgnoreCase("M")) {
                try {
                    System.out.println("Enter the first group to be merged.");
                    group = groups.get(input.nextLine());
                    System.out.println("Enter the second group to be merged.");
                    group2 = groups.get(input.nextLine());
                    System.out.println("Enter the name of the newly merged group.");
                    in = input.nextLine();
                    groups.put(in, group.merge(group2));
                } catch (Exception e) {
                    System.out.println("Merge failed; Input groups not recognized.");
                }
            }
        }
    }
    
    private static void test(HashMap<String, Group> groups) {
        for (String key : groups.keySet()) {
            System.out.println(key);
        }
        
        System.out.println("---------");
        
        HashMap<String, Integer> testFreq = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            String item = groups.get("Ancestries").random().toString();
            if (testFreq.containsKey(item)) {
                testFreq.put(item, testFreq.get(item) + 1);
            } else {
                testFreq.put(item, 1);
            }
        }
        
        for (String key : testFreq.keySet()) {
            System.out.println(key + ": " + testFreq.get(key));
        }
    }
    
    private static Feat readCsvLine(String line) {
        String[] parts = line.split(",");
        ArrayList<String> prereqs = new ArrayList<>();
        for (String prereq : parts[3].split(";")) {
            prereqs.add(prereq);
        }
        return new Feat(parts[0], parts[1], parts[2], prereqs);
    }
}
