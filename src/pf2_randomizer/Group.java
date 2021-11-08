/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pf2_randomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author pmele
 */
public class Group {
    private List<Feat> feats;
    private int[] weights;
    private int totalWeighting;

    public Group(List<Feat> feats) {
        this.feats = feats;
        this.weights = new int[] {3, 2, 1, 0};
        this.totalWeighting = getTotalWeighting();
    }

    public Group(List<Feat> feats, int[] weights) {
        this.feats = feats;
        if (weights.length != 4) {
            throw new IllegalArgumentException(weights.length + " weights provided, when 4 were expected.");
        }
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < 0) {
                weights[i] = 0;
            }
        }
        this.weights = weights;
        this.totalWeighting = getTotalWeighting();
    }
    
    public Feat random() {
        if (feats.isEmpty()) {
            return null;
        }
        Random rand = new Random();
        int weightIndex = rand.nextInt(totalWeighting);
        int realIndex = 0;
        while (weightIndex > -1) {
            String rarity = feats.get(realIndex).getRarity();
            switch(rarity){
                case("Common"):
                    weightIndex -= weights[0];
                    break;
                case("Uncommon"):
                    weightIndex -= weights[1];
                    break;
                case("Rare"):
                    weightIndex -= weights[2];
                    break;
                case("Unique"):
                    weightIndex -= weights[3];
                    break;
            }
            realIndex++;
        }
        return feats.get(realIndex - 1);
    }
    
    private int getTotalWeighting() {
        int sum = 0;
        for (Feat feat : feats) {
            String rarity = feat.getRarity();
            switch(rarity){
                case("Common"):
                    sum += weights[0];
                    break;
                case("Uncommon"):
                    sum += weights[1];
                    break;
                case("Rare"):
                    sum += weights[2];
                    break;
                case("Unique"):
                    sum += weights[3];
                    break;
            }
        }
        return sum;
    }

    public List<Feat> getFeats() {
        return feats;
    }
    
    public void setWeights(int[] weights) {
        if (weights.length != 4) {
            throw new IllegalArgumentException(weights.length + " weights provided, when 4 were expected.");
        }
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < 0) {
                weights[i] = 0;
            }
        }
        this.weights = weights;
        this.totalWeighting = getTotalWeighting();
    }
    
    public Group merge(Group otherGroup) {
        ArrayList<Feat> mergedFeats = new ArrayList<>();
        mergedFeats.addAll(feats);
        mergedFeats.addAll(otherGroup.getFeats());
        return new Group(mergedFeats);
    }
    
    
}
