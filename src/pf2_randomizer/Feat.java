/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pf2_randomizer;

import java.util.ArrayList;

/**
 *
 * @author pmele
 */
public class Feat {
    private String name;
    private String link;
    private String rarity;
    private ArrayList<String> prereqs;

    public Feat(String name, String link) {
        this.name = name;
        this.link = link;
        this.rarity = "Common";
        this.prereqs = new ArrayList<>();
    }

    public Feat(String name, String link, ArrayList<String> prereqs) {
        this.name = name;
        this.link = link;
        this.rarity = "Common";
        this.prereqs = prereqs;
    }
    
    public Feat(String name, String link, String rarity) {
        this.name = name;
        this.link = link;
        this.rarity = rarity;
        this.prereqs = new ArrayList<>();
    }
    
    public Feat(String name, String link, String rarity, ArrayList<String> prereqs) {
        this.name = name;
        this.link = link;
        this.rarity = rarity;
        this.prereqs = prereqs;
    }
    
    public String getLink() {
        return link;
    }

    public ArrayList<String> getPrereqs() {
        return prereqs;
    }

    public String getRarity() {
        return rarity;
    }
    
    public String toString() {
        return name;
    }
}
