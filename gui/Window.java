package gui;

import performance.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Window extends JFrame implements ActionListener {

    private static PerformanceClass performanceClass;

    public static HashTable participantsMap;

    public static HashTableWithDoubleHashing participantsMapDH;

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("gui.messages");

    private static final int TF_WIDTH = 5;

    private static final int NUMBER_OF_BUTTONS = 6;

    private final JComboBox cmbCollisionTypes = new JComboBox();

    private final JComboBox cmbHashFunctions = new JComboBox();

    private final JTextArea taInput = new JTextArea(), idCode = new JTextArea();

    private Table table = new Table();

    private final JScrollPane scrollTable = new JScrollPane(table);

    private final JPanel panParam123 = new JPanel();

    private final JScrollPane scrollParam123 = new JScrollPane(panParam123);

    private final JPanel panParam123Events = new JPanel();

    private final JTextArea taEvents = new JTextArea();

    private final JScrollPane scrollEvents = new JScrollPane(taEvents);

    private final JPanel panEast = new JPanel();

    private final JScrollPane scrollEast = new JScrollPane(panEast);

    private Panels panParam1, panParam2, panButtons;

    private int sizeOfInitialSubSet, sizeOfGenSet, colWidth, initialCapacity;

    private float loadFactor;

    public Window() {
        initComponents();
    }

    private void initComponents() {
        Stream.of(MESSAGES.getString("cmbCollisionType1"),
                MESSAGES.getString("cmbCollisionType4"))
                .forEach(s -> cmbCollisionTypes.addItem(s));
        cmbCollisionTypes.addActionListener(this);

        // Formuojamas mygtukų tinklelis (mėlynas). Naudojama klasė Panels.
        panButtons = new Panels(new String[]{
                MESSAGES.getString("button1"),
                MESSAGES.getString("button2"),
                MESSAGES.getString("button3"),
                MESSAGES.getString("button4"),
                MESSAGES.getString("button5"),}, 1, NUMBER_OF_BUTTONS);
        panButtons.getButtons().forEach((btn) -> btn.addActionListener(this));
        IntStream.of(1, 3).forEach( p -> panButtons.getButtons().get(p).setEnabled(false));

        // Viskas sudedama į vieną (rožinės spalvos) panelį
        panEast.setLayout(new BoxLayout(panEast, BoxLayout.Y_AXIS));
        Stream.of(new JLabel(MESSAGES.getString("border1")),
                cmbCollisionTypes,
                panButtons,
                new JLabel(MESSAGES.getString("border7")),
                idCode)
                .forEach(comp -> {
                    comp.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                    panEast.add(Box.createRigidArea(new Dimension(0, 3)));
                    panEast.add(comp);
                    panEast.add(new JPanel(new GridLayout(2,2,10,10)));
                    panEast.add( idCode );
                });

        // Formuojama pirmoji parametrų lentelė (šviesiai žalia). Naudojama klasė Panels.
        panParam1 = new Panels(
                new String[]{
                        MESSAGES.getString("lblParam11"),
                        MESSAGES.getString("lblParam12"),
                        MESSAGES.getString("lblParam13"),
                        MESSAGES.getString("lblParam14"),
                        MESSAGES.getString("lblParam15"),
                        MESSAGES.getString("lblParam16")},
                new String[]{
                        MESSAGES.getString("tfParam11"),
                        MESSAGES.getString("tfParam12"),
                        MESSAGES.getString("tfParam13"),
                        MESSAGES.getString("tfParam14"),
                        MESSAGES.getString("tfParam15"),
                        MESSAGES.getString("tfParam16")}, TF_WIDTH);

        // .. tikrinami ivedami parametrai dėl teisingumo. Negali būti neigiami.
        IntStream.of(0, 1, 2, 4).forEach(v -> panParam1.getTfOfTable().get(v).setInputVerifier(new NotNegativeNumberVerifier()));

        // Tikrinamas įvedamas apkrovimo faktorius. Turi būti (0;1] ribose
        panParam1.getTfOfTable().get(3).setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText().trim();
                try {
                    Float loadFactor = Float.valueOf(text);
                    if (loadFactor <= 0.0 || loadFactor > 1.0) {
                        input.setBackground(Color.RED);
                        return false;
                    }
                    input.setBackground(Color.WHITE);
                    return true;
                } catch (NumberFormatException e) {
                    input.setBackground(Color.RED);
                    return false;
                }
            }
        });
        // Formuojama antroji parametrų lentelė (gelsva). Naudojama klasė Panels
        panParam2 = new Panels(
                new String[]{
                        MESSAGES.getString("lblParam21"),
                        MESSAGES.getString("lblParam22"),
                        MESSAGES.getString("lblParam23"),
                        MESSAGES.getString("lblParam24"),
                        MESSAGES.getString("lblParam25"),
                        MESSAGES.getString("lblParam26")},
                new String[]{
                        MESSAGES.getString("tfParam21"),
                        MESSAGES.getString("tfParam22"),
                        MESSAGES.getString("tfParam23"),
                        MESSAGES.getString("tfParam24"),
                        MESSAGES.getString("tfParam25"),
                        MESSAGES.getString("tfParam26")}, TF_WIDTH);

        // Visų trijų parametrų lentelių paneliai sudedami į šviesiai pilką panelį
        Stream.of(panParam1, panParam2).forEach(p -> panParam123.add(p));

        // Toliau suformuojamas panelis iš šviesiai pilko panelio ir programos
        // įvykių JTextArea
        GroupLayout gl = new GroupLayout(panParam123Events);
        panParam123Events.setLayout(gl);
        gl.setHorizontalGroup(
                gl.createSequentialGroup().
                        addComponent(scrollParam123,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                Short.MAX_VALUE).
                        addComponent(scrollEvents,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                Short.MAX_VALUE));
        gl.setVerticalGroup(
                gl.createSequentialGroup().
                        addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER).
                                addComponent(scrollParam123).
                                addComponent(scrollEvents)));

        // Kad prijungiant tekstą prie JTextArea vaizdas visada nušoktų į apačią
        scrollEvents.getVerticalScrollBar()
                .addAdjustmentListener((AdjustmentEvent e) -> {
                    taEvents.select(taEvents.getCaretPosition() * taEvents.getFont().getSize(), 0);
                });

        setLayout(new BorderLayout());
        add(scrollEast, BorderLayout.EAST);
        add(scrollTable, BorderLayout.CENTER);
        add(panParam123Events, BorderLayout.SOUTH);
        appearance();
    }

    private void appearance() {
        // Rėmeliai
        idCode.setSize( 20, 20 );
        panParam123.setBorder(new TitledBorder(MESSAGES.getString("border4")));
        scrollTable.setBorder(new TitledBorder(MESSAGES.getString("border5")));
        scrollEvents.setBorder(new TitledBorder(MESSAGES.getString("border6")));

        scrollTable.getViewport().setBackground(Color.white);
        panParam1.setBackground(new Color(204, 255, 204));// Šviesiai žalia
        panParam2.setBackground(new Color(255, 255, 153));// Gelsva
        panEast.setBackground(Color.pink);
        panButtons.setBackground(new Color(112, 162, 255));// Blyškiai mėlyna
        panParam123.setBackground(Color.lightGray);

        // Antra parametrų lentelė (gelsva) bus neredaguojama
        panParam2.getTfOfTable().forEach((comp) -> comp.setEditable(false));
        Stream.of(taInput, taEvents).forEach(comp -> {
            comp.setBackground(Color.white);
            comp.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        });
        taEvents.setEditable(false);
        scrollEvents.setPreferredSize(new Dimension(350, 0));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        KsSwing.setFormatStartOfLine(true);
        try {
            System.gc();
            System.gc();
            System.gc();
            taEvents.setBackground(Color.white);
            Object source = ae.getSource();
            if (source.equals(panButtons.getButtons().get(0))) {
                mapGeneration();
            } else if (source.equals(panButtons.getButtons().get(1))) {
                mapAdd();
            } else if (source.equals(panButtons.getButtons().get(2))) {
                switch (cmbCollisionTypes.getSelectedIndex()) {
                    case 1:
                        KsSwing.oun(taEvents, performanceClass.isMapContains( participantsMapDH, idCode.getText() ));
                       break;
                     default:
                        KsSwing.oun(taEvents, performanceClass.isMapContains( participantsMap, idCode.getText() ));
                        break;
                }
            } else if (source.equals(panButtons.getButtons().get(3))) {
                switch (cmbCollisionTypes.getSelectedIndex()) {
                    case 1:
                        KsSwing.oun(taEvents, performanceClass.removeParticipant( participantsMapDH, idCode.getText() ));
                        break;
                    default:
                        KsSwing.oun(taEvents, performanceClass.removeParticipant( participantsMap, idCode.getText() ));
                        break;
                }
            } else if (source.equals(panButtons.getButtons().get(4))) {
                switch (cmbCollisionTypes.getSelectedIndex()) {
                    case 1:
                        KsSwing.oun(taEvents, performanceClass.getParticipantData( participantsMapDH, idCode.getText() ));
                        break;
                    default:
                        KsSwing.oun(taEvents, performanceClass.getParticipantData( participantsMap, idCode.getText() ));
                        break;
                }
            } else if (source.equals(cmbCollisionTypes) || source.equals(cmbHashFunctions)) {
                IntStream.of(1, 3).forEach(p -> panButtons.getButtons().get(p).setEnabled(false));
            }
        } catch (MyException e) {
            KsSwing.ounerr(taEvents, MESSAGES.getString(e.getMessage()), e.getValue());
        } catch (UnsupportedOperationException e) {
            KsSwing.ounerr(taEvents, e.getMessage());
        } catch (Exception e) {
            KsSwing.ounerr(taEvents, MESSAGES.getString("systemError"));
            e.printStackTrace(System.out);
        }
    }

    public void mapGeneration() {
        readMapParameters();
        switch (cmbCollisionTypes.getSelectedIndex()) {
            case 1:
                participantsMapDH = performanceClass.mapGenerationDH( sizeOfGenSet, initialCapacity, loadFactor);
                table.setModel(new TableModel(participantsMapDH.getModelList(panParam1.getTfOfTable().get(5).getText()),
                        panParam1.getTfOfTable().get(5).getText(), 1), colWidth);
                // Atnaujinamai maišos lentelės parametrai (geltona lentelė)
                updateHashtableParameters(false);
                // Įjungiami 2 ir 4 mygtukai
                IntStream.of(1, 3).forEach(p -> panButtons.getButtons().get(p).setEnabled(true));
                KsSwing.oun(taEvents, MESSAGES.getString("msg5"));
                break;
            default:
                participantsMap = performanceClass.mapGeneration( sizeOfGenSet, initialCapacity, loadFactor );
                table.setModel(new TableModel(participantsMap.getModelList(panParam1.getTfOfTable().get(5).getText()),
                        panParam1.getTfOfTable().get(5).getText(), participantsMap.getMaxChainSize()), colWidth);
                // Atnaujinamai maišos lentelės parametrai (geltona lentelė)
                updateHashtableParameters(false);
                // Įjungiami 2 ir 4 mygtukai
                IntStream.of(1, 3).forEach(p -> panButtons.getButtons().get(p).setEnabled(true));
                KsSwing.oun(taEvents, MESSAGES.getString("msg5"));
                break;
        }
    }


    public void mapAdd(){
        Participant p =  new Participant.Builder().buildRandom();
        switch (cmbCollisionTypes.getSelectedIndex()) {
            case 1:
                performanceClass.mapAdd( participantsMapDH, p );
                table.setModel(new TableModel(participantsMapDH.getModelList(panParam1.getTfOfTable().get(5).getText()),
                        panParam1.getTfOfTable().get(5).getText(), 1), colWidth);
                updateHashtableParameters(true);
                KsSwing.oun(taEvents, p, MESSAGES.getString("msg2"));
                break;
            default:
                performanceClass.mapAdd( participantsMap, p );
                table.setModel(new TableModel(participantsMap.getModelList(panParam1.getTfOfTable().get(5).getText()),
                        panParam1.getTfOfTable().get(5).getText(), participantsMap.getMaxChainSize()), colWidth);
                updateHashtableParameters(true);
                KsSwing.oun(taEvents, p, MESSAGES.getString("msg2"));
            break;
        }
    }

    private void updateHashtableParameters(boolean colorize) {
        switch (cmbCollisionTypes.getSelectedIndex()) {
            case 1:
                String[] parameters = new String[]{
                        String.valueOf(participantsMapDH.size()),
                        String.valueOf(participantsMapDH.getTableCapacity()),
                        String.valueOf(participantsMapDH.getRehashesCounter()),

                };
                for (int i = 0; i < parameters.length; i++) {
                    String str = panParam2.getTfOfTable().get(i).getText();
                    if ((!str.equals(parameters[i]) && !str.equals("") && colorize)) {
                        panParam2.getTfOfTable().get(i).setForeground(Color.RED);
                    } else {
                        panParam2.getTfOfTable().get(i).setForeground(Color.BLACK);
                    }
                    panParam2.getTfOfTable().get(i).setText(parameters[i]);
                }break;
            default:
                parameters = new String[]{
                        String.valueOf(participantsMap.size()),
                        String.valueOf(participantsMap.getTableCapacity()),
                        String.valueOf(participantsMap.getMaxChainSize()),
                        String.valueOf(participantsMap.getRehashesCounter()),
                        String.valueOf(participantsMap.getLastUpdatedChain()),
                        // Užimtų maišos lentelės elementų skaičius %
                        String.format("%3.2f", (double) participantsMap.getChainsCounter() / participantsMap.getTableCapacity() * 100) + "%"
                        // .. naujus parametrus tęsiame čia ..

                };
                for (int i = 0; i < parameters.length; i++) {
                    String str = panParam2.getTfOfTable().get(i).getText();
                    if ((!str.equals(parameters[i]) && !str.equals("") && colorize)) {
                        panParam2.getTfOfTable().get(i).setForeground(Color.RED);
                    } else {
                        panParam2.getTfOfTable().get(i).setForeground(Color.BLACK);
                    }
                    panParam2.getTfOfTable().get(i).setText(parameters[i]);
                }                break;
        }
    }

    private void readMapParameters() {
        int i = 0;
        List<JTextField> tfs = panParam1.getTfOfTable();

        String errorMessages[] = {"badSizeOfInitialSubSet", "badSizeOfGenSet",
                "badInitialCapacity", "badLoadFactor", "badColumnWidth"};
        //Patikrinimas dėl parametrų teisingumo
        for (int j = 0; j < tfs.size(); j++) {
            JTextField tf = tfs.get( j );
            if (tf.getInputVerifier() != null && !tf.getInputVerifier().verify( tf )) {
                throw new MyException( errorMessages[ i ], tf.getText() );
            }
        }
        //Kai parametrai teisingi - juos nuskaitome
        sizeOfInitialSubSet = Integer.valueOf( tfs.get( i++ ).getText() );
        sizeOfGenSet = Integer.valueOf( tfs.get( i++ ).getText() );
        initialCapacity = Integer.valueOf( tfs.get( i++ ).getText() );
        loadFactor = Float.valueOf( tfs.get( i++ ).getText() );
        colWidth = Integer.valueOf( tfs.get( i++ ).getText() );
    }

    public JTextArea getTaEvents() {
        return taEvents;
    }

    public void createAndShowGUI() {
        performanceClass = new PerformanceClass();
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName()
                        // Arba sitaip, tada swing komponentu isvaizda priklausys
                        // nuo naudojamos OS:
                        // UIManager.getSystemLookAndFeelClassName()
                        // Arba taip:
                        //  "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
                        // Linux'e dar taip:
                        // "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
                );
                UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                KsSwing.ounerr(taEvents, ex.getMessage());
            }
            Window window = new Window();
            window.setIconImage(new ImageIcon(MESSAGES.getString("icon")).getImage());
            window.setTitle(MESSAGES.getString("title"));
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            //window.setPreferredSize(new Dimension(1000, 650));
            window.pack();
            window.setVisible(true);
        });
    }

    private class NotNegativeNumberVerifier extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextField) input).getText();
            try {
                int result = Integer.valueOf(text);
                input.setBackground(result >= 0 ? Color.WHITE : Color.RED);
                return result >= 0;
            } catch (NumberFormatException e) {
                input.setBackground(Color.RED);
                return false;
            }
        }
    }
}
