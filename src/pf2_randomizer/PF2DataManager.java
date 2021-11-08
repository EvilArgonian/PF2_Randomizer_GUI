/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pf2_randomizer;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 *
 * @author pmele
 */
public class PF2DataManager {
    
    public static final String[] SUPERCATEGORIES = {"Ancestry", "Class", "Archetypes", "Other"};
    
    //If I feel ambitious enough to have this check AoN for updates, these will instead generate from functions.
    public static final String[] CLASSES = {"Alchemist", "Barbarian", "Bard", "Champion", "Cleric", 
        "Druid", "Fighter", "Gunslinger", "Inventor", "Investigator", "Magus", "Monk", "Oracle", 
        "Ranger", "Rogue", "Sorcerer", "Summoner", "Swashbuckler", "Witch", "Wizard"};
    public static final String[] ANCESTRIES = {"Dwarf", "Elf", "Gnome", "Goblin", "Halfling", 
        "Human", "Azarketi", "Catfolk", "Fetchling", "Gnoll", "Grippli", "Hobgoblin", "Kitsune", 
        "Kobold", "Leshy", "Lizardfolk", "Orc", "Ratfolk", "Tengu", "Anadi", "Android", 
        "Conrasu", "Fleshwarp", "Goloma", "Shisk", "Shoony", "Sprite", "Strix"};
    public static final String[] VERSATILE_HERITAGES = {"Aasimar", "Aphorite", "Changeling", 
        "Dhampir", "Duskwalker", "Ganzi", "Ifrit", "Oread", "Suli", "Sylph", "Tiefling", "Undine", "Beastkin"};
    
    //Merisyl Homebrew
    public static final String[] MERISYL_ANCESTRIES = {"Ixen", "Korthjach", "Minotaur", "Tamgol"};
    public static final String[] MERISYL_VERSATILE_HERITAGES = {"Morphic"};
    
    //The Elder Scrolls Homebrew
    public static final String[] TES_ANCESTRIES = {"Altmer", "Argonian", "Bosmer", "Breton", "Dunmer", "Imperial", "Khajiit", "Nord", "Orc", "Redguard"};
    
     public static HashMap<String, Group> readInGroupCSVs() {
        //Set up all the groups here
        //File dir = new File("C:\\Users\\pmele\\Documents\\NetBeansProjects\\PF2_Randomizer_GUI\\Feats");
        File dir = new File(System.getProperty("user.dir") + "\\Feats"); //Verify that this actually reaches the right folder!
        File[] directoryListing = dir.listFiles();
        HashMap<String, Group> genGroups = new HashMap<>();
        for (File child : directoryListing) {
            genGroups.put(child.getName().split(".csv")[0], readInOneCSV(child));
        }

        //genGroups.putAll(generateLevelMerges(genGroups)); 
        return genGroups;
    }
     
    public static Group readInOneCSV(File csvFile) {
        ArrayList<Feat> feats = new ArrayList<>();
        if (csvFile.length() == 0) {
            return new Group(feats);
        }
        try (Stream<String> lines = Files.lines(Paths.get(csvFile.getPath()), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> feats.add(readCsvLine(line)));
        } catch (Exception e){
            if (!(e instanceof ArrayIndexOutOfBoundsException)) {
                System.out.println("Error in File: " + csvFile.getName());
                e.printStackTrace(System.out);
            }
        }
        return new Group(feats);
    } 
     
    private static Feat readCsvLine(String line) {
        String[] parts = line.split(",");
        ArrayList<String> prereqs = new ArrayList<>();
        for (String prereq : parts[3].split(";")) {
            prereqs.add(prereq.trim());
        }
        return new Feat(parts[0], parts[1], parts[2], prereqs);
    }
     
     /**
      * Produces a map of feat groups containing feats from across all normally needed level ranges.
      * These level ranges are 1 to X, for each level X for which feats exist.
      * @param initMap The map of originally read feat groups
      * @return 
      */
    /*
     private static HashMap<String, Group> generateLevelMerges(HashMap<String, Group> initMap) {
        Group group;
        HashMap<String, Group> groupsUpdate = new HashMap<>();
        for (String key : initMap.keySet()) {
            group = initMap.get(key);
            if (key.endsWith("1")) {
                for (int level = 2; level <= 20; level++) {
                    for (String key2 : initMap.keySet()) {
                        try {
                            if (key2.split("_")[0].equals(key.split("_")[0]) && key2.split("_")[1].equals("Feats") && Integer.parseInt(key2.split("_")[2]) == level) {
                                group = group.merge(initMap.get(key2));
                                groupsUpdate.put(key + "-" + key2.split("_")[2], group);
                                //Testing line - Comment out if unused
                                //System.out.println(key + "-" + key2.split("_")[2]);
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
        return groupsUpdate;
     }
    */
    
    public static Group mergeAcrossLevels(HashMap<String, Group> initMap, String keyBase, int min, int max) {
        Group group = new Group(new ArrayList<>());
        for (int level = min; level <= max; level++) {
            if (initMap.containsKey(keyBase + level)) {
                group = group.merge(initMap.get(keyBase + level));
            }
        }
        return group;
     }
}
