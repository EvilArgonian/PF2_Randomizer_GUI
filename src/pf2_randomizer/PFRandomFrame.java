/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pf2_randomizer;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author pmele
 */
public class PFRandomFrame extends javax.swing.JFrame {
    
    //EVERYTHING STILL NEEDS TO BE MADE THREAD SAFE
    private HashMap<String, Group> groups;
    private HashMap<String, List<String>> dropdowns;
    
    private List<String> prereqsMet;
    private List<String> prereqsNotMet;
    private String currentPrereqChecking = null;
    private final int CHECK_LIMIT = 200; //Checks allowed before giving up on generation

    /**
     * Creates new form PFRandomFrame
     */
    public PFRandomFrame(String str) {
        //super(str);
        //Load all data
        groups = PF2DataManager.readInGroupCSVs();
        dropdowns = new HashMap<>();
        //Establish Supercategories for first dropdown
        for (String supercategory : PF2DataManager.SUPERCATEGORIES) {
            dropdowns.put(supercategory, new ArrayList<>());
            System.out.println("Test Area 1: Supercategory " + supercategory);
        }
        List<String> ancCheck = Arrays.asList(PF2DataManager.ANCESTRIES);
        List<String> classCheck = Arrays.asList(PF2DataManager.CLASSES);
        List<String> versCheck = Arrays.asList(PF2DataManager.VERSATILE_HERITAGES);
        List<String> keysSeen = new ArrayList<>();
        for (String key : groups.keySet()) {
            if (key.split("_").length > 1) {
                key = key.split("_")[0];
            }
            if (keysSeen.contains(key)) {
                continue; //Necessary to prevent adding multiple additions of classes/ancestries with feats at multiple levels
            } else {
                keysSeen.add(key);
            }
            if (ancCheck.contains(key)) {
                dropdowns.get("Ancestry").add(key + " (Heritages)");
                dropdowns.get("Ancestry").add(key + " (Feats)");
            } else if (classCheck.contains(key)) {
                dropdowns.get("Class").add(key); 
            } else if (versCheck.contains(key)) {
                dropdowns.get("Ancestry").add(key); //There's an argument for these to get their own supercategory, but this will do.
            } else if (key.equals("Ancestries") || key.equals("Backgrounds") || key.equals("Classes") || key.equals("Dedications")) {
                dropdowns.get("Other").add(key); 
                /*Note that ancestries and classes are in this list to generate an 
                ancestry or class itself, as opposed to their use as categories 
                from which to generate an ancestry-specific or class-specific feat. */
            }
        }
        //End loading
        
        //Initialize Components
        initComponents();
        //Establish Menus
        //File Menu: Save and Open
        fileMenu.setMnemonic('F');
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        saveItem.setMnemonic('S');
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setMnemonic('O');
        fileMenu.add(openItem);
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile();
            }
        });
        //Edit Menu: Merge, Configure Weights, and Clear Prerequisites
        editMenu.setMnemonic('E');
        JMenuItem mergeItem = new JMenuItem("Merge");
        mergeItem.setMnemonic('M');
        editMenu.add(mergeItem);
        mergeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mergeGroups();
            }
        });
        JMenuItem configureItem = new JMenuItem("Configure Weights");
        mergeItem.setMnemonic('W');
        editMenu.add(configureItem);
        mergeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configureWeights();
            }
        });
        JMenuItem clearItem = new JMenuItem("Clear Prerequisites");
        mergeItem.setMnemonic('C');
        editMenu.add(clearItem);
        mergeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPrereqs();
            }
        });
        
        
        
        //Initial setup of dropdowns
        jComboBox2.removeAllItems();
        List<String> sortMe = new ArrayList<>();
        for (String item : dropdowns.get(String.valueOf(jComboBox1.getSelectedItem()))) {
            sortMe.add(item);
        }
        Collections.sort(sortMe);
        for (String item : sortMe) {
            jComboBox2.addItem(item);
        }
        //Initial setup of prereq checks
        prereqsMet = new ArrayList<>();
        prereqsNotMet = new ArrayList<>();
        prereqsMet.add("None"); //Refers to automatically meeting the prereqs of feats that have none
        
        //Fixes a display issue where dropdowns would be covered by label components
        jComboBox1.setLightWeightPopupEnabled(false);
        jComboBox2.setLightWeightPopupEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        minLevelField = new javax.swing.JFormattedTextField();
        maxLevelField = new javax.swing.JFormattedTextField();
        label1 = new java.awt.Label();
        label2 = new java.awt.Label();
        label3 = new java.awt.Label();
        generateButton = new javax.swing.JButton();
        label4 = new java.awt.Label();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultsDisplay = new javax.swing.JTextArea();
        noButton = new javax.swing.JButton();
        yesButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        prereqPrompt = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        numRandomField = new javax.swing.JFormattedTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        prereqsMetDisplay = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        prereqsNotMetDisplay = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        editMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ancestry", "Class", "Archetypes", "Other" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        minLevelField.setText("1");
        minLevelField.setToolTipText("Minimum Level");

        maxLevelField.setText("20");
        maxLevelField.setToolTipText("Maximum Level");

        label1.setAlignment(java.awt.Label.CENTER);
        label1.setText("-");

        label2.setText("Prerequisites Not Met");

        label3.setText("Prerequisites Met");

        generateButton.setText("Generate");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        label4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label4.setText("Level Range:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 255, Short.MAX_VALUE)
        );

        resultsDisplay.setEditable(false);
        resultsDisplay.setColumns(20);
        resultsDisplay.setRows(5);
        resultsDisplay.setText("Click generate to produce results!");
        jScrollPane1.setViewportView(resultsDisplay);

        noButton.setText("No");
        noButton.setEnabled(false);
        noButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noButtonActionPerformed(evt);
            }
        });

        yesButton.setText("Yes");
        yesButton.setEnabled(false);
        yesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesButtonActionPerformed(evt);
            }
        });

        prereqPrompt.setEditable(false);
        prereqPrompt.setEnabled(false);
        jScrollPane2.setViewportView(prereqPrompt);

        jLabel1.setText("Number:");

        numRandomField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        numRandomField.setText("3");

        prereqsMetDisplay.setEditable(false);
        prereqsMetDisplay.setColumns(20);
        prereqsMetDisplay.setRows(5);
        jScrollPane3.setViewportView(prereqsMetDisplay);

        prereqsNotMetDisplay.setEditable(false);
        prereqsNotMetDisplay.setColumns(20);
        prereqsNotMetDisplay.setRows(5);
        jScrollPane4.setViewportView(prereqsNotMetDisplay);

        fileMenu.setText("File");
        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");
        jMenuBar1.add(editMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(110, 110, 110)
                                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(78, 78, 78))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(minLevelField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(maxLevelField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(numRandomField)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(generateButton)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                        .addContainerGap(22, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(yesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                            .addComponent(noButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(22, 22, 22))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(minLevelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(generateButton, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(maxLevelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(numRandomField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(yesButton)
                                .addGap(18, 18, 18)
                                .addComponent(noButton))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        enableAppropriateFields();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        jComboBox2.removeAllItems();
        List<String> sortMe = new ArrayList<>();
        for (String item : dropdowns.get(String.valueOf(jComboBox1.getSelectedItem()))) {
            sortMe.add(item);
        }
        Collections.sort(sortMe);
        for (String item : sortMe) {
            jComboBox2.addItem(item);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        Runnable generationThread = new Runnable() {
            public void run() {
                //Turn off all buttons until generation is complete
                jComboBox1.setEnabled(false);
                jComboBox2.setEnabled(false);
                minLevelField.setEnabled(false);
                maxLevelField.setEnabled(false);
                numRandomField.setEnabled(false);
                generateButton.setEnabled(false);
                
                String groupKey;
                if (jComboBox1.getSelectedItem().toString().equals("Ancestry") ||
                    jComboBox1.getSelectedItem().toString().equals("Class") ||
                    jComboBox1.getSelectedItem().toString().equals("Archetypes")) {
                    if (jComboBox2.getSelectedItem().toString().contains("(Heritages)")) {
                        groupKey = jComboBox2.getSelectedItem().toString().replace(" (Heritages)", "_Heritages");
                    } else {
                        if (Integer.parseInt(minLevelField.getText()) == Integer.parseInt(maxLevelField.getText())) {
                            groupKey = jComboBox2.getSelectedItem() + "_Feats_" + minLevelField.getText();
                        } else {
                            groupKey = jComboBox2.getSelectedItem() + "_Feats_" + minLevelField.getText() + "-" + maxLevelField.getText();
                        }
                    }
                } else {
                    groupKey = jComboBox2.getSelectedItem().toString();
                }
                Group selectFromThisGroup = groups.get(groupKey);
                System.out.println("GROUP: " + groupKey);
                int numToGenerate;
                try {
                    numToGenerate = Integer.parseInt(numRandomField.getText());
                } catch (NumberFormatException e) {
                    //Default to 3
                    numToGenerate = 3;
                }
                ArrayList<Feat> selection = new ArrayList<>();
                boolean prereqsInvalid = false;
                String meetsReq = "";

                Feat selected;
                int trials = 0;
                try {
                    for (int i = 0; i < numToGenerate; i++) {
                        prereqsInvalid = true;
                        selected = null;
                        while (prereqsInvalid || selection.contains(selected)) {
                            selected = selectFromThisGroup.random();
                            if (selected == null) {
                                System.out.println("Group had no feats to select from!");
                                i = numToGenerate;
                                break;
                            }
                            for (String req : selected.getPrereqs()) {
                                if (prereqsNotMet.contains(req)) {
                                    prereqsInvalid = true;
                                    break;
                                } else if (prereqsMet.contains(req)) {
                                    prereqsInvalid = false;
                                } else {
                                    //Check prereqs here!
                                    promptForPrereq(req);

                                    if (prereqsInvalid) {
                                        break;
                                    }
                                }
                            }
                            if (trials >= CHECK_LIMIT) {
                                i = numToGenerate;
                                System.out.println("Check limit reached!");
                                break;
                            }
                            trials++;
                        }
                        if (i != numToGenerate) {
                            selection.add(selected);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Some error occurred... \n" + e);
                }
                //Display results
                StringBuilder displayMe = new StringBuilder("Your options are: \n");
                for (Feat result : selection) {
                    displayMe.append(result.toString() + "\n");
                }
                resultsDisplay.setText(displayMe.toString());
                
                //Turn on buttons again
                jComboBox1.setEnabled(true);
                jComboBox2.setEnabled(true);
                enableAppropriateFields();
                numRandomField.setEnabled(true);
                generateButton.setEnabled(true);
            }
        };
        new Thread(generationThread).start();
        
        
    }//GEN-LAST:event_generateButtonActionPerformed

    private void yesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesButtonActionPerformed
        synchronized(currentPrereqChecking) {
            if (currentPrereqChecking != null) {
                prereqsMet.add(currentPrereqChecking);
                currentPrereqChecking = null;
            }
        }
    }//GEN-LAST:event_yesButtonActionPerformed

    private void noButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonActionPerformed
        synchronized(currentPrereqChecking) {
            if (currentPrereqChecking != null) {
                prereqsNotMet.add(currentPrereqChecking);
                currentPrereqChecking = null;
            }
        }
    }//GEN-LAST:event_noButtonActionPerformed

    private void promptForPrereq(String req) {
        currentPrereqChecking = req;
        prereqPrompt.setText("Do you meet the prereq: \n" + req);
        yesButton.setEnabled(true);
        noButton.setEnabled(true);
        while(currentPrereqChecking != null) {
            try { //Waits for yes or no button to recognize the prereq
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
        prereqPrompt.setText("");
        updatePrereqDisplay();
        yesButton.setEnabled(false);
        noButton.setEnabled(false);
    }
    
    /**
     * Determines what fields should be active based on the contents of already selected fields.
     */
    private void enableAppropriateFields() {
        if (String.valueOf(jComboBox2.getSelectedItem()).contains("Heritages") ||
             (String.valueOf(jComboBox1.getSelectedItem()).equals("Other") &&
             !String.valueOf(jComboBox2.getSelectedItem()).equals("Dedications")
            )) { //Add other non-leveled items to this check!
            minLevelField.setEnabled(false);
            maxLevelField.setEnabled(false);
        } else {
            minLevelField.setEnabled(true);
            maxLevelField.setEnabled(true);
        }
    }
    
    private void updatePrereqDisplay() {
        //Update Prereqs Met Display
        StringBuilder displayUpdate = new StringBuilder();
        for (String prereq : prereqsMet) {
            if (prereq.equals("None")) {
                /*
                Skip 'None', as it represents that you automatically have all
                the prereqs for a feat that has none, and isn't useful for
                the user to see in display (and in fact may be confusing).
                */
                continue;
            }
            displayUpdate.append(prereq).append("\n");
        }
        if(displayUpdate.length()-1 > 0) {
            displayUpdate.replace(displayUpdate.length()-1,displayUpdate.length(),""); //Strip final new line
        }
        prereqsMetDisplay.setText(displayUpdate.toString());
        
        //Update Prereqs Not Met Display
        displayUpdate = new StringBuilder();
        for (String prereq : prereqsNotMet) {
            displayUpdate.append(prereq).append("\n");
        }
        if(displayUpdate.length()-1 > 0) {
            displayUpdate.replace(displayUpdate.length()-1,displayUpdate.length(),""); //Strip final new line
        }
        prereqsNotMetDisplay.setText(displayUpdate.toString());
    }
    
    /**
     * Saves the current prerequisite information to a file.
     */
    private void saveToFile() {
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir") + "\\Saves") ;
        if(jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        if(jfc.getSelectedFile() == null) {
            return;
        }
        File chosenFile = jfc.getSelectedFile();

        if(jfc.getSelectedFile().exists()) {
            String message = "File " + jfc.getSelectedFile().getName() + " exists.  Overwrite?";
            if( JOptionPane.showConfirmDialog(this, message) != JOptionPane.YES_OPTION) {
                return;			
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(chosenFile));
            for (String prereq : prereqsMet) {
                writer.append("MET: " + prereq + "\n");
            }
            for (String prereq : prereqsNotMet) {
                writer.append("NOT MET: " + prereq + "\n");
            }
            writer.flush();  writer.close();
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not write file", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Loads prerequisite information from a file. This overwrites any prerequisites already identified.
     */
    private void loadFromFile() {
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir") + "\\Saves");
        if( jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        if( jfc.getSelectedFile() == null) {
            return;
        }
        File chosenFile = jfc.getSelectedFile();

        try {
            //Clear existing prereqs met/unmet
            prereqsMet.clear();
            prereqsNotMet.clear();
            
            BufferedReader reader = new BufferedReader(new FileReader(chosenFile));
            String nextLine = reader.readLine();
            while (nextLine != null) {
                if (nextLine.startsWith("MET: ")) {
                    prereqsMet.add(nextLine.split("MET: ")[0]);
                } else if (nextLine.startsWith("NOT MET: ")) {
                    prereqsMet.add(nextLine.split("NOT MET: ")[0]);
                }
                nextLine = reader.readLine();
            }
            updatePrereqDisplay();
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not read file", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //INCOMPLETE
    private void mergeGroups() {
        //Actually implement group merging logic
    }
    
    /**
     * Reads in a CSV, formatted in the same manner as those in the 'Feats' 
     * folder, to form a custom group.
     */
    private void importGroup() {
        JFileChooser jfc = new JFileChooser();
        if( jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        if( jfc.getSelectedFile() == null) {
            return;
        }
        File chosenFile = jfc.getSelectedFile();

        try {
            Group importedGroup = PF2DataManager.readInOneCSV(chosenFile);
            String key = chosenFile.getName().split(".csv")[0];
            if (groups.containsKey(key) || dropdowns.containsKey(key)) {
                throw new Exception("Key appeared to already be in use!");
            }
            groups.put(key, importedGroup);
            if (key.split("_").length > 1) {
                key = key.split("_")[0];
            }
            //For now, simply added to the 'Other' supercategory.
            //In production-level code, this would likely use a dedicated
            //'Custom' supercategory, or have a mechanism for assigning
            //a more appropriate default category.
            dropdowns.get("Other").add(key);
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not read file", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //INCOMPLETE
    private void configureWeights() {
        //Actually implement weight configuring logic
    }
    
    /**
     * Clears the stored prerequisite memory.
     */
    private void clearPrereqs() {
        prereqsMet.clear();
        prereqsMet.add("None");
        prereqsNotMet.clear();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PFRandomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PFRandomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PFRandomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PFRandomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PFRandomFrame("Pathfinder 2E Roguelike Randomizer").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton generateButton;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private javax.swing.JFormattedTextField maxLevelField;
    private javax.swing.JFormattedTextField minLevelField;
    private javax.swing.JButton noButton;
    private javax.swing.JFormattedTextField numRandomField;
    private javax.swing.JTextPane prereqPrompt;
    private javax.swing.JTextArea prereqsMetDisplay;
    private javax.swing.JTextArea prereqsNotMetDisplay;
    private javax.swing.JTextArea resultsDisplay;
    private javax.swing.JButton yesButton;
    // End of variables declaration//GEN-END:variables
}
