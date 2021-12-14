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
    
    public static final String[] SUPERCATEGORIES = {"Ancestry", "Class", "Subclass", "Archetype", "Other"};
    
    //If I feel ambitious enough to have this check AoN for updates, these will instead generate from functions.
    public static final String[] CLASSES = {"Alchemist", "Barbarian", "Bard", "Champion", "Cleric", 
        "Druid", "Fighter", "Gunslinger", "Inventor", "Investigator", "Magus", "Monk", "Oracle", 
        "Ranger", "Rogue", "Sorcerer", "Summoner", "Swashbuckler", "Witch", "Wizard"};
    public static final String[] SUBCLASSES = {"Alchemist Fields", "Barbarian Instincts", "Bard Muses", "Champion Causes", "Cleric Doctrines", 
        "Druid Orders", "Gunslinger Ways", "Inventor Innovations", "Investigator Methodologies", "Magus Hybrid Studies", "Oracle Mysteries", 
        "Ranger Hunter's Edges", "Rogue Rackets", "Sorcerer Bloodlines", "Summoner Eidolons", "Swashbuckler Styles", "Witch Patron Themes", 
        "Wizard Arcane Theses", "Wizard Arcane Schools"};
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
    
     public static HashMap<String, Group> readInGroupCSVs(int[] weights) {
        //Set up all the groups here
        //File dir = new File("C:\\Users\\pmele\\Documents\\NetBeansProjects\\PF2_Randomizer_GUI\\Feats");
        File dir = new File(System.getProperty("user.dir") + "\\Feats"); //Verify that this actually reaches the right folder!
        File[] directoryListing = dir.listFiles();
        HashMap<String, Group> genGroups = new HashMap<>();
        for (File child : directoryListing) {
            genGroups.put(child.getName().replace("#", " ").split(".csv")[0], readInOneCSV(child, weights));
        }

        return genGroups;
    }
     
    public static Group readInOneCSV(File csvFile, int[] weights) {
        ArrayList<Feat> feats = new ArrayList<>();
        if (csvFile.length() == 0) {
            return new Group(feats, weights);
        }
        try (Stream<String> lines = Files.lines(Paths.get(csvFile.getPath()), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> feats.add(readCsvLine(line)));
        } catch (Exception e){
            if (!(e instanceof ArrayIndexOutOfBoundsException)) {
                System.out.println("Error in File: " + csvFile.getName());
                e.printStackTrace(System.out);
            }
        }
        return new Group(feats, weights);
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
     * Given a particular category of Feats indicated by the front portion of its key in initMap and a minimum and maximum level, 
     * returns a group consisting of all Feats of that category between the given levels.
     * @param initMap
     * @param keyBase
     * @param weights
     * @param min
     * @param max
     * @return 
     */
    public static Group mergeAcrossLevels(HashMap<String, Group> initMap, String keyBase, int[] weights, int min, int max) {
        Group group = new Group(new ArrayList<>(), weights);
        for (int level = min; level <= max; level++) {
            if (initMap.containsKey(keyBase + level)) {
                group = group.merge(initMap.get(keyBase + level));
            }
        }
        return group;
     }
}
