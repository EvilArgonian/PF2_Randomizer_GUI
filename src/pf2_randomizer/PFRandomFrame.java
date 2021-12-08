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
    
    //EVERYTHING STILL NEEDS TO BE MADE THREAD SAFE (?)
    private HashMap<String, Group> groups;
    private HashMap<String, List<String>> dropdowns;
    
    private List<String> prereqsMet;
    private List<String> prereqsNotMet;
    private String currentPrereqChecking = null;
    private final int CHECK_LIMIT = 300; //Checks allowed before giving up on generation
    
    private int[] weights = new int[]{3, 2, 1, 0};

    /**
     * Creates new form PFRandomFrame
     */
    public PFRandomFrame(String str) {
        //super(str);
        //Load all data
        groups = PF2DataManager.readInGroupCSVs(weights);
        dropdowns = new HashMap<>();
        //Establish Supercategories for first dropdown
        for (String supercategory : PF2DataManager.SUPERCATEGORIES) {
            dropdowns.put(supercategory, new ArrayList<>());
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
                System.out.println("RECOGNIZED ANCESTRY DATA FOR: " + key);
            } else if (classCheck.contains(key)) {
                dropdowns.get("Class").add(key); 
                System.out.println("RECOGNIZED CLASS DATA FOR: " + key);
            } else if (versCheck.contains(key)) {
                dropdowns.get("Ancestry").add(key + " (Feats)"); //There's an argument for these to get their own supercategory, but this will do.
                System.out.println("RECOGNIZED VERSATILE HERITAGE DATA FOR: " + key);
            } else if (key.equals("Ancestries") || key.equals("Backgrounds") || key.equals("Classes") || key.equals("Dedications")) {
                dropdowns.get("Other").add(key); 
                System.out.println("RECOGNIZED MISC. DATA FOR: " + key);
                /*Note that ancestries and classes are in this list to generate an 
                ancestry or class itself, as opposed to their use as categories 
                from which to generate an ancestry-specific or class-specific feat. */
            }
        }
        //End loading
        
        //Initialize Components
        initComponents();
        //Fix where windows appear
        this.setLocationRelativeTo(null);
        configureWeightsDialog.setLocationRelativeTo(null);
        mergeGroupsDialog.setLocationRelativeTo(null);
        
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
        mergeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePairedDropdowns(mergeGroupCategory1, mergeGroupSelect1);
                updatePairedDropdowns(mergeGroupCategory2, mergeGroupSelect2);
                mergeGroupsDialog.setVisible(true);
            }
        });
        editMenu.add(mergeItem);
        JMenuItem configureItem = new JMenuItem("Configure Weights");
        configureItem.setMnemonic('W');
        editMenu.add(configureItem);
        configureItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configCommon.setText(Integer.toString(weights[0]));
                configUncommon.setText(Integer.toString(weights[1]));
                configRare.setText(Integer.toString(weights[2]));
                configUnique.setText(Integer.toString(weights[3]));
                configureWeightsDialog.setVisible(true);
            }
        });
        JMenuItem clearItem = new JMenuItem("Clear Prerequisites");
        clearItem.setMnemonic('C');
        editMenu.add(clearItem);
        clearItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPrereqs();
            }
        });
        
        //Initial setup of dropdowns
        refreshSelectionDropdown();
        
        //Initial setup of prereq checks
        prereqsMet = new ArrayList<>();
        prereqsNotMet = new ArrayList<>();
        prereqsMet.add("None"); //Refers to automatically meeting the prereqs of feats that have none
        
        //Fixes a display issue where dropdowns would be covered by label components
        jComboBox1.setLightWeightPopupEnabled(false);
        jComboBox2.setLightWeightPopupEnabled(false);
        mergeGroupCategory1.setLightWeightPopupEnabled(false);
        mergeGroupSelect1.setLightWeightPopupEnabled(false);
        mergeGroupCategory2.setLightWeightPopupEnabled(false);
        mergeGroupSelect2.setLightWeightPopupEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        configureWeightsDialog = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        configCommon = new javax.swing.JFormattedTextField();
        configUncommon = new javax.swing.JFormattedTextField();
        configRare = new javax.swing.JFormattedTextField();
        configUnique = new javax.swing.JFormattedTextField();
        configCancelButton = new javax.swing.JButton();
        configSetButton = new javax.swing.JButton();
        mergeGroupsDialog = new javax.swing.JDialog();
        mergeGroupCategory1 = new javax.swing.JComboBox();
        mergeGroupSelect1 = new javax.swing.JComboBox();
        mergeMinLevelField1 = new javax.swing.JFormattedTextField();
        mergeMaxLevelField1 = new javax.swing.JFormattedTextField();
        label5 = new java.awt.Label();
        label6 = new java.awt.Label();
        mergeGroupCategory2 = new javax.swing.JComboBox();
        mergeGroupSelect2 = new javax.swing.JComboBox();
        mergeMinLevelField2 = new javax.swing.JFormattedTextField();
        mergeMaxLevelField2 = new javax.swing.JFormattedTextField();
        label7 = new java.awt.Label();
        label8 = new java.awt.Label();
        mergeNameField = new javax.swing.JTextField();
        label9 = new java.awt.Label();
        label10 = new java.awt.Label();
        label11 = new java.awt.Label();
        label12 = new java.awt.Label();
        mergeButton = new javax.swing.JButton();
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

        configureWeightsDialog.setTitle("Configure Weights");
        configureWeightsDialog.setPreferredSize(new java.awt.Dimension(250, 210));
        configureWeightsDialog.setResizable(false);
        configureWeightsDialog.setSize(new java.awt.Dimension(250, 210));
        configureWeightsDialog.setType(java.awt.Window.Type.POPUP);

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getSize()+7f));
        jLabel2.setText("Common");

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getSize()+7f));
        jLabel3.setText("Uncommon");

        jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getSize()+7f));
        jLabel4.setText("Rare");

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getSize()+7f));
        jLabel5.setText("Unique");

        configCommon.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        configCommon.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        configCommon.setText("3");
        configCommon.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        configCommon.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);
        configCommon.setFont(configCommon.getFont().deriveFont(configCommon.getFont().getSize()+5f));

        configUncommon.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        configUncommon.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        configUncommon.setText("2");
        configUncommon.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);
        configUncommon.setFont(configUncommon.getFont().deriveFont(configUncommon.getFont().getSize()+5f));

        configRare.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        configRare.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        configRare.setText("1");
        configRare.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);
        configRare.setFont(configRare.getFont().deriveFont(configRare.getFont().getSize()+5f));

        configUnique.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        configUnique.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        configUnique.setText("0");
        configUnique.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);
        configUnique.setFont(configUnique.getFont().deriveFont(configUnique.getFont().getSize()+5f));

        configCancelButton.setFont(configCancelButton.getFont().deriveFont(configCancelButton.getFont().getSize()+7f));
        configCancelButton.setText("Cancel");
        configCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configCancelButtonActionPerformed(evt);
            }
        });

        configSetButton.setFont(configSetButton.getFont().deriveFont(configSetButton.getFont().getSize()+7f));
        configSetButton.setText("Set");
        configSetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configSetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout configureWeightsDialogLayout = new javax.swing.GroupLayout(configureWeightsDialog.getContentPane());
        configureWeightsDialog.getContentPane().setLayout(configureWeightsDialogLayout);
        configureWeightsDialogLayout.setHorizontalGroup(
            configureWeightsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configureWeightsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configureWeightsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configureWeightsDialogLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(configCommon, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(configureWeightsDialogLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(configUnique, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(configureWeightsDialogLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(configRare, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(configureWeightsDialogLayout.createSequentialGroup()
                        .addGroup(configureWeightsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(configCancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(configureWeightsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(configureWeightsDialogLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(configUncommon, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(configureWeightsDialogLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(configSetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        configureWeightsDialogLayout.setVerticalGroup(
            configureWeightsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configureWeightsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configureWeightsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(configCommon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configureWeightsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(configUncommon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configureWeightsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(configRare, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configureWeightsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(configUnique, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(configureWeightsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(configCancelButton)
                    .addComponent(configSetButton))
                .addContainerGap())
        );

        mergeGroupsDialog.setPreferredSize(new java.awt.Dimension(436, 335));
        mergeGroupsDialog.setResizable(false);
        mergeGroupsDialog.setSize(new java.awt.Dimension(436, 335));

        mergeGroupCategory1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ancestry", "Class", "Archetypes", "Other" }));
        mergeGroupCategory1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mergeGroupCategory1ActionPerformed(evt);
            }
        });

        mergeGroupSelect1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-" }));

        mergeMinLevelField1.setText("1");
        mergeMinLevelField1.setToolTipText("Minimum Level");

        mergeMaxLevelField1.setText("20");
        mergeMaxLevelField1.setToolTipText("Maximum Level");

        label5.setAlignment(java.awt.Label.CENTER);
        label5.setText("-");

        label6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label6.setText("Level Range:");

        mergeGroupCategory2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ancestry", "Class", "Archetypes", "Other" }));
        mergeGroupCategory2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mergeGroupCategory2ActionPerformed(evt);
            }
        });

        mergeGroupSelect2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-" }));

        mergeMinLevelField2.setText("1");
        mergeMinLevelField2.setToolTipText("Minimum Level");

        mergeMaxLevelField2.setText("20");
        mergeMaxLevelField2.setToolTipText("Maximum Level");

        label7.setAlignment(java.awt.Label.CENTER);
        label7.setText("-");

        label8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label8.setText("Level Range:");

        mergeNameField.setText("jTextField1");

        label9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label9.setText("Group 1:");

        label10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label10.setText("Name:");

        label11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label11.setText("Merged Group:");

        label12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label12.setText("Group 2:");

        mergeButton.setFont(mergeButton.getFont().deriveFont(mergeButton.getFont().getStyle() | java.awt.Font.BOLD));
        mergeButton.setText("Merge");
        mergeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mergeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mergeGroupsDialogLayout = new javax.swing.GroupLayout(mergeGroupsDialog.getContentPane());
        mergeGroupsDialog.getContentPane().setLayout(mergeGroupsDialogLayout);
        mergeGroupsDialogLayout.setHorizontalGroup(
            mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mergeGroupsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mergeGroupsDialogLayout.createSequentialGroup()
                        .addGroup(mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mergeGroupsDialogLayout.createSequentialGroup()
                                .addComponent(mergeGroupCategory1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mergeGroupSelect1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mergeGroupsDialogLayout.createSequentialGroup()
                                .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mergeMinLevelField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mergeMaxLevelField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(mergeGroupsDialogLayout.createSequentialGroup()
                                    .addComponent(mergeGroupCategory2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(mergeGroupSelect2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(mergeGroupsDialogLayout.createSequentialGroup()
                                    .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(mergeNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(mergeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mergeGroupsDialogLayout.createSequentialGroup()
                        .addGroup(mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mergeGroupsDialogLayout.createSequentialGroup()
                                .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mergeMinLevelField2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mergeMaxLevelField2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        mergeGroupsDialogLayout.setVerticalGroup(
            mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mergeGroupsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mergeGroupSelect1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mergeGroupCategory1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mergeMinLevelField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mergeMaxLevelField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mergeGroupSelect2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mergeGroupCategory2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mergeMinLevelField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mergeMaxLevelField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mergeGroupsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(mergeNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mergeButton))
                    .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(70, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(749, 512));

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
        editMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                editMenuMouseEntered(evt);
            }
        });
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        updatePairedDropdowns(jComboBox1, jComboBox2);
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        Runnable generationThread = new Runnable() {
            @Override
            public void run() {
                //Turn off all buttons until generation is complete
                jComboBox1.setEnabled(false);
                jComboBox2.setEnabled(false);
                minLevelField.setEnabled(false);
                maxLevelField.setEnabled(false);
                numRandomField.setEnabled(false);
                generateButton.setEnabled(false);
                
                String groupKey;
                ArrayList<Feat> selection = new ArrayList<>();
                boolean hasNone = false;
                StringBuilder displayMe = new StringBuilder();
                try {
                    groupKey = getGroupKey(jComboBox1, jComboBox2, minLevelField, maxLevelField);
                    Group selectFromThisGroup = groups.get(groupKey);
                    System.out.println("GROUP: " + groupKey);
                    
                    int numToGenerate;
                    try {
                        numToGenerate = Integer.parseInt(numRandomField.getText());
                    } catch (NumberFormatException e) {
                        //Default to 3
                        numToGenerate = 3;
                    }
                    boolean prereqsInvalid = false;
                    String meetsReq = "";

                    Feat selected;
                    int trials = 0;
                    for (int i = 0; i < numToGenerate; i++) {
                        prereqsInvalid = true;
                        selected = null;
                        while (prereqsInvalid || selection.contains(selected)) {
                            System.out.println("Weights used: " + Arrays.toString(selectFromThisGroup.getWeights()));
                            selected = selectFromThisGroup.random();
                            if (selected == null) {
                                System.out.println("Group had no feats to select from!");
                                hasNone = true;
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
                    hasNone = true;
                    displayMe.append(e.getMessage()).append("\n");
                    System.out.println("Some error occurred... \n");
                    e.printStackTrace(System.out);
                }
                //Display results
                if (hasNone) {
                    displayMe.append("No feats were found for this group/level range!\n");
                } else {
                    displayMe.append("Your options are: \n");
                }
                for (Feat result : selection) {
                    displayMe.append(result.toString()).append("\n");
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
        synchronized(this) {
            if (currentPrereqChecking != null) {
                prereqsMet.add(currentPrereqChecking);
                currentPrereqChecking = null;
            }
        }
    }//GEN-LAST:event_yesButtonActionPerformed

    private void noButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noButtonActionPerformed
        synchronized(this) {
            if (currentPrereqChecking != null) {
                prereqsNotMet.add(currentPrereqChecking);
                currentPrereqChecking = null;
            }
        }
    }//GEN-LAST:event_noButtonActionPerformed

    private void configCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configCancelButtonActionPerformed
        configCommon.setText(Integer.toString(weights[0]));
        configUncommon.setText(Integer.toString(weights[1]));
        configRare.setText(Integer.toString(weights[2]));
        configUnique.setText(Integer.toString(weights[3]));
        configureWeightsDialog.setVisible(false);
    }//GEN-LAST:event_configCancelButtonActionPerformed

    private void configSetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configSetButtonActionPerformed
        generateButton.setEnabled(false);
        synchronized (this) {
            try {
                weights[0] = Integer.parseInt(configCommon.getText());
                weights[1] = Integer.parseInt(configUncommon.getText());
                weights[2] = Integer.parseInt(configRare.getText());
                weights[3] = Integer.parseInt(configUnique.getText());
                System.out.println("Weights read: " + Arrays.toString(weights));
            } catch (Exception e) {
                System.out.println("Invalid weights entered; reverting to defaults.");
                weights[0] = 3;
                weights[1] = 2;
                weights[2] = 1;
                weights[3] = 0;
            }
        }
        for (Group group : groups.values()) {
            group.setWeights(weights);
        }
        generateButton.setEnabled(true);
        configureWeightsDialog.setVisible(false);
    }//GEN-LAST:event_configSetButtonActionPerformed

    private void editMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMenuMouseEntered
        this.validate();
    }//GEN-LAST:event_editMenuMouseEntered

    private void mergeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mergeButtonActionPerformed
        String nameNewGroup = mergeNameField.getText();
        if (groups.containsKey(nameNewGroup)) {
            System.out.println("This name is already in use! Try another name.");
            JOptionPane.showMessageDialog(this, "This name is already in use! Try another name.", "Name in use", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String groupKey1 = getGroupKey(mergeGroupCategory1, mergeGroupSelect1, mergeMinLevelField1, mergeMaxLevelField1);
        String groupKey2 = getGroupKey(mergeGroupCategory2, mergeGroupSelect2, mergeMinLevelField2, mergeMaxLevelField2);
        
        Group toMergeGroup1 = groups.get(groupKey1);
        Group toMergeGroup2 = groups.get(groupKey2);
        
        Group mergedGroup = toMergeGroup1.merge(toMergeGroup2);
        groups.put(nameNewGroup, mergedGroup);
        dropdowns.get("Other").add(nameNewGroup);
        refreshSelectionDropdown();
        
        mergeGroupsDialog.setVisible(false);
    }//GEN-LAST:event_mergeButtonActionPerformed

    private void mergeGroupCategory2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mergeGroupCategory2ActionPerformed
        updatePairedDropdowns(mergeGroupCategory2, mergeGroupSelect2);
    }//GEN-LAST:event_mergeGroupCategory2ActionPerformed

    private void mergeGroupCategory1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mergeGroupCategory1ActionPerformed
        updatePairedDropdowns(mergeGroupCategory1, mergeGroupSelect1);
    }//GEN-LAST:event_mergeGroupCategory1ActionPerformed

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
                System.out.println("Reading line: " + nextLine);
                if (nextLine.startsWith("MET: ")) {
                    prereqsMet.add(nextLine.replace("MET: ", ""));
                } else if (nextLine.startsWith("NOT MET: ")) {
                    prereqsNotMet.add(nextLine.replace("NOT MET: ", ""));
                }
                nextLine = reader.readLine();
            }
            updatePrereqDisplay();
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not read file", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Reads in a CSV, formatted in the same manner as those in the 'Feats' 
     * folder, to form a custom group.
     */
    private void importGroup() {
        //Possibly deserves its own popup window for supercategory selection?
        
        JFileChooser jfc = new JFileChooser();
        if( jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        if( jfc.getSelectedFile() == null) {
            return;
        }
        File chosenFile = jfc.getSelectedFile();

        try {
            Group importedGroup = PF2DataManager.readInOneCSV(chosenFile, weights);
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
    
    /**
     * Clears the stored prerequisite memory.
     */
    private void clearPrereqs() {
        prereqsMet.clear();
        prereqsMet.add("None");
        prereqsNotMet.clear();
        updatePrereqDisplay();
    }
    
    private String getGroupKey(javax.swing.JComboBox category, javax.swing.JComboBox select, javax.swing.JFormattedTextField minL, javax.swing.JFormattedTextField maxL) {
        String groupKey;
        if (category.getSelectedItem().toString().equals("Ancestry") ||
            category.getSelectedItem().toString().equals("Class") ||
            category.getSelectedItem().toString().equals("Archetypes")) {
            if (select.getSelectedItem().toString().contains("(Heritages)")) {
                groupKey = select.getSelectedItem().toString().replace(" (Heritages)", "_Heritages");
            } else {
                int min = Integer.parseInt(minL.getText());
                int max = Integer.parseInt(maxL.getText());
                if (min == max) {
                    groupKey = select.getSelectedItem().toString().replace(" (Feats)", "") + "_Feats_" + min;
                } else {
                    String keyBase = select.getSelectedItem().toString().replace(" (Feats)", "") + "_Feats_";

                    //Logic for merging groups here
                    //Get minimum actual existing level
                    if (min >= 21) {
                        //System.out.println("Minimum level must be no more than 20 (Given as " + min + ").");
                        JOptionPane.showMessageDialog(this, "Minimum level must be no more than 20 (Given as " + min + ").", "Invalid Entry", JOptionPane.ERROR_MESSAGE);
                        throw new IllegalArgumentException("Minimum level must be no more than 20 (Given as " + min + ").");
                    }
                    if (max <= 0) {
                        //System.out.println("Maximum level must be no less than 1 (Given as " + max + ").");
                        JOptionPane.showMessageDialog(this, "Maximum level must be no less than 1 (Given as " + max + ").", "Invalid Entry", JOptionPane.ERROR_MESSAGE);
                        throw new IllegalArgumentException("Maximum level must be no less than 1 (Given as " + max + ").");
                    }
                    if (min > max) {
                        //System.out.println("Minimum level must be no more than the maximum.");
                        JOptionPane.showMessageDialog(this, "Minimum level must be no more than the maximum.", "Invalid Entry", JOptionPane.ERROR_MESSAGE);
                        throw new IllegalArgumentException("Minimum level must be no less than the maximum.");
                    }
                    for (; min < 21 || min < max; min++) {
                        if (groups.containsKey(keyBase + min)) {
                            break;
                        }
                    }
                    //Get maximum actual existing level
                    for (; max > 0 || max > min; max--) {
                        if (groups.containsKey(keyBase + max)) {
                            break;
                        }
                    }
                    if (min == max) {
                        groupKey = keyBase + min;
                    } else {
                        groupKey = keyBase + min + "-" + max;
                        groups.putIfAbsent(groupKey, PF2DataManager.mergeAcrossLevels(groups, keyBase, weights, min, max));
                    }
                }
            }
        } else {
            groupKey = select.getSelectedItem().toString();
        }
        return groupKey;
    }
    
    private void updatePairedDropdowns(javax.swing.JComboBox category, javax.swing.JComboBox select) {
        select.removeAllItems();
        List<String> sortMe = new ArrayList<>();
        for (String item : dropdowns.get(String.valueOf(category.getSelectedItem()))) {
            sortMe.add(item);
        }
        Collections.sort(sortMe);
        for (String item : sortMe) {
            select.addItem(item);
        }
    }
    
    private void refreshSelectionDropdown() {
        jComboBox2.removeAllItems();
        List<String> sortMe = new ArrayList<>();
        for (String item : dropdowns.get(String.valueOf(jComboBox1.getSelectedItem()))) {
            sortMe.add(item);
        }
        Collections.sort(sortMe);
        for (String item : sortMe) {
            jComboBox2.addItem(item);
        }
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
    private javax.swing.JButton configCancelButton;
    private javax.swing.JFormattedTextField configCommon;
    private javax.swing.JFormattedTextField configRare;
    private javax.swing.JButton configSetButton;
    private javax.swing.JFormattedTextField configUncommon;
    private javax.swing.JFormattedTextField configUnique;
    private javax.swing.JDialog configureWeightsDialog;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton generateButton;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private java.awt.Label label1;
    private java.awt.Label label10;
    private java.awt.Label label11;
    private java.awt.Label label12;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
    private javax.swing.JFormattedTextField maxLevelField;
    private javax.swing.JButton mergeButton;
    private javax.swing.JComboBox mergeGroupCategory1;
    private javax.swing.JComboBox mergeGroupCategory2;
    private javax.swing.JComboBox mergeGroupSelect1;
    private javax.swing.JComboBox mergeGroupSelect2;
    private javax.swing.JDialog mergeGroupsDialog;
    private javax.swing.JFormattedTextField mergeMaxLevelField1;
    private javax.swing.JFormattedTextField mergeMaxLevelField2;
    private javax.swing.JFormattedTextField mergeMinLevelField1;
    private javax.swing.JFormattedTextField mergeMinLevelField2;
    private javax.swing.JTextField mergeNameField;
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
