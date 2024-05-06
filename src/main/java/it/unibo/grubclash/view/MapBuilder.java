package it.unibo.grubclash.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.border.LineBorder;

import javax.imageio.ImageIO;
import javax.swing.*;

import it.unibo.grubclash.model.GrubPanel;

import java.io.File;
import java.io.IOException;

/*
    da gestire il ridimensionamento dello schermo che si è buggato --> una volta gestito il ridimensionamento basta creare una matrice ROWSxCOLS di bool per capire se è terreno o se non lo è.
*/

public class MapBuilder extends Canvas {

    private static JFrame mapContainer;
    public static JFrame getMapContainer() {
        return mapContainer;
    }
    public static void setMapContainer(JFrame mapContainer) {
        MapBuilder.mapContainer = mapContainer;
    }

    private static JPanel map;
    public static JPanel getMap() {
        return map;
    }
    public static void setMap(JPanel map) {
        MapBuilder.map = map;
    }

    private static JPanel mapBase[][];
    public static JPanel[][] getMapBase() {
        return mapBase;
    }
    public static JPanel getMapBase (int i, int j) {
        return mapBase[i][j]; // TODO non ho capito perché dentro p1map(), se metto getMapBase(i, j) = new JPanel; mi da errore
    }
    public static void setMapBase(JPanel[][] mapBase) {
        MapBuilder.mapBase = mapBase;   
    }

    private static JButton btnMatrix[][];
    public static JButton[][] getBtnMatrix() {
        return btnMatrix;
    }
    public static JButton getBtnMatrix(int i, int j) {
        return btnMatrix[i][j];
    }
    public static void setBtnMatrix(JButton[][] btnMatrix) {
        MapBuilder.btnMatrix = btnMatrix;
    }

    private static JLayeredPane layeredPaneGrid;
    public static JLayeredPane getLayeredPaneGrid() {
        return layeredPaneGrid;
    }
    public static void setLayeredPaneGrid(JLayeredPane layeredPaneGrid) {
        MapBuilder.layeredPaneGrid = layeredPaneGrid;
    }

    protected static int currentPlayer;
    protected static int numPlayers;
    
    protected static Color playerColor[];

    private static boolean characterPlacementPhase; //diventa true quando viene toccato il primo "finish"
    private static boolean colorSpawnpoint; //bool che serve per tenere traccia se lo spawnpoint è stato messo o no, si usa nel metodo switchBackground
    private static boolean mapDrawer;

    final static int ROWS = 20;
    final static int COLS = 20; 

    private static EnumEntity.entities[][] entityMatrix;

    public MapBuilder(final int playerCount) {
        setNumPlayers(playerCount);
        MapBuilder.entityMatrix = new EnumEntity.entities[ROWS][COLS];
        mapDrawer = false;
    }

    public static boolean getMapDrawer () {
        return mapDrawer;
    }

    public static void setMapDrawer (boolean bool) {
        mapDrawer = bool;
    }

    public static void updateMapDrawer () {
        if (mapDrawer == false) {
            mapDrawer = true;
        }else{
            mapDrawer = false;
        }
    }

    public static EnumEntity.entities[][] getEntityMatrix() { // metodo per le altre classi
        return entityMatrix;
    }

    public static void setEntityInMatrix (int i, int j, EnumEntity.entities entity) {
        MapBuilder.entityMatrix[i][j] = entity;
    }

    public static void initCharacterPlacementPhase () { // questi tre metodi servono al programma a capire se siamo nella fase del piazzamento dei personaggi, che si trova dopo la fase della creazione dei blocchi di terra della mappa
        characterPlacementPhase = false;
    }
    public static void updateCharacterPlacementPhase () {
        characterPlacementPhase = true;
    } 
    public static boolean getCharacterPlacementPhase () {
        return characterPlacementPhase;
    }

    public static void initColorSpawnpoint () {
        colorSpawnpoint = false;
    }

    public static boolean getColorSpawnpoint () {
        return colorSpawnpoint;
    }

    public static void switchColorSpawnpoint () {
        if (colorSpawnpoint == false) {
            colorSpawnpoint = true;
        }else{
            colorSpawnpoint = false;
        }
    }
    
    public static Color switchBackground(JPanel[][] mapBase, int i, int j, Color color, JButton[][] btnMatrix, JPanel map, JFrame mapContainer) {
        // dopo primo click rimane false
        if (getCharacterPlacementPhase() == false) {
            Color btnColor = (color == Color.WHITE ? Color.BLACK : Color.WHITE);
            mapBase[i][j].setBackground(btnColor);
            return btnColor;
        }else{ // cioè se siamo nella fase di placement dei personaggi
            Color btnColor;
            if (mapBase[i][j].getBackground() == Color.WHITE && getColorSpawnpoint() == false) { //permetto il cambio del colore solo se o lo sfondo è bianco, o è dello stesso colore del palyer che voglio cambiare
                btnColor = getPlayerColor(getCurrentPlayer());
                switchColorSpawnpoint();
                mapBase[i][j].setBackground(btnColor);
                return btnColor;
            }else{
                if (mapBase[i][j].getBackground() == getPlayerColor(getCurrentPlayer()) && getColorSpawnpoint() == true) {
                    btnColor = Color.WHITE;
                    switchColorSpawnpoint();
                    mapBase[i][j].setBackground(btnColor);
                    return btnColor;
                }
            }
            return mapBase[i][j].getBackground();
        }
    }

    public static void setNumPlayers ( int numPlayers ) {
        MapBuilder.numPlayers = numPlayers;
        initPlayerColor();
    }

    public static int getNumPlayers () {
        return numPlayers;
    }

    public static int getCurrentPlayer () {
        return MapBuilder.currentPlayer;
    }

    public static void updateCurrentPlayer () {
        if ( getCurrentPlayer() < getNumPlayers() ) {
            MapBuilder.currentPlayer++;
        }else{
            initializeCurrentPlayer();
        }
    }

    public static void initializeCurrentPlayer () {
        MapBuilder.currentPlayer = 0;
    }

    public static void initPlayerColor () {
        Color[] playerColor = new Color[getNumPlayers()]; //colori che identificano i giocatori
        for (int f = 0; f < getNumPlayers(); f++) {
            switch (f) {
                case 0:
                    playerColor[f] = Color.PINK;
                    break;
                case 1:
                    playerColor[f] = Color.YELLOW;
                    break;
                case 2:
                    playerColor[f] = Color.LIGHT_GRAY;
                    break;
                case 3:
                    playerColor[f] = Color.RED;
                    break;
                case 4:
                    playerColor[f] = Color.ORANGE;
                    break;
                case 5:
                    playerColor[f] = Color.BLUE;
                    break;
            }
        }
        MapBuilder.playerColor = playerColor;
    }

    public static Color getPlayerColor (int currentPlayer) { //verrà passato tramite getCurrentPlayer();
        return playerColor[currentPlayer];
    }

    public static JButton createButton(int i, int j) {
        final int BUTTON_WIDTH = 30;
        final int BUTTON_HEIGHT = 30;

        JButton invisibleBtn = new JButton();
        if (i == 0 && j == COLS - 1) {
            invisibleBtn.setText("Finish");
            invisibleBtn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        } else { // Gestisce tutti i bottoni che non sono quello di chiusura dell'editing della mappa
            //invisibleBtn.setText(String.valueOf(i) + "-" + String.valueOf(j));
            invisibleBtn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            invisibleBtn.setBorder(new LineBorder(Color.MAGENTA));
            invisibleBtn.setBorderPainted(true);
            invisibleBtn.setContentAreaFilled(false);
            invisibleBtn.setOpaque(true);
        }
        return invisibleBtn; // Fa strano chiamare "invisibleBtn" l'unico bottone che fa da finish, ma non ho idee migliori
    }

    public static void mapInitialization(JButton[][] btnMatrix, JPanel map, JPanel[][] mapBase, JFrame mapContainer, JLayeredPane layeredPaneGrid) throws IOException {
        mapContainer.setResizable(false); //blocco il resize, aiuta le hitbox
        for(int i = 0; i < ROWS; i++ ) {
            for(int j = 0; j < COLS; j++) {
                btnMatrix[i][j].setVisible(false);
                if(mapBase[i][j].getBackground() == Color.BLACK) {
                    panelBackground(mapBase, i, j);
                    setEntityInMatrix(i, j, EnumEntity.entities.WALL);
                    //TODO qui chiamo l'entity per mettere il wall tra le hitbox               
                } else {
                    mapBase[i][j].setBackground(Color.CYAN);
                    setEntityInMatrix(i, j, EnumEntity.entities.SKY);
                }        
                mapBase[i][j].repaint();
            }
        }
        createPlayableLayer(btnMatrix, map, mapBase, mapContainer, layeredPaneGrid);
        /* GrubPanel grubPanel = new GrubPanel(getNumPlayers());
        grubPanel.startGameThread(); */
    }

    private static void createPlayableLayer(JButton[][] btnMatrix, JPanel map, JPanel[][] mapBase, JFrame mapContainer, JLayeredPane layeredPaneGrid) { 
        
        GrubPanel playableLayer = new GrubPanel(getNumPlayers()); 
        playableLayer.startGameThread();
        playableLayer.setBounds(0, 0, (int)mapContainer.getSize().getWidth(), (int)mapContainer.getSize().getHeight());
        playableLayer.setOpaque(false); //come si rende trasparente?
        layeredPaneGrid.add(playableLayer, JLayeredPane.PALETTE_LAYER);
        layeredPaneGrid.setVisible(true);
        mapContainer.repaint();
        mapContainer.revalidate();
    }

    public static void panelBackground(JPanel[][] mapBase, int i, int j) throws IOException {
        if ( i==0 || mapBase[i-1][j].getBackground() == Color.WHITE || mapBase[i-1][j].getBackground() == Color.CYAN) {
            BufferedImage platformImg = ImageIO.read(new File("src\\main\\resources\\gameplay\\platform.png")); //TODO cambio img
            JLabel picLabel = new JLabel(new ImageIcon(platformImg));
            mapBase[i][j].add(picLabel);
        }else{
            BufferedImage platformImg = ImageIO.read(new File("src\\main\\resources\\gameplay\\platform_ground.png"));
            JLabel picLabel = new JLabel(new ImageIcon(platformImg));
            mapBase[i][j].add(picLabel);
        }
    }

    public static void p2Map(JPanel map, JPanel[][] mapBase, JButton[][] btnMatrix, int btnFinishI, int btnFinishJ, JFrame mapContainer, JLayeredPane layeredPaneGrid) {
        JButton btnFinish = btnMatrix[btnFinishI][btnFinishJ];
        if (getMapDrawer() == true) {
            updateMapDrawer();
        }
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                final int finalI = i;
                final int finalJ = j;
                if(btnMatrix[i][j] == btnFinish) { //if(è nel bottone finish, ovvero quello dello switch della fase)
                    btnFinish.addActionListener(o -> {
                        if (getCurrentPlayer() == getNumPlayers() - 1 && getColorSpawnpoint() == true) {
                            try {
                                mapInitialization(btnMatrix, map, mapBase, mapContainer, layeredPaneGrid);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            if (getColorSpawnpoint() == true) { //se il giocatore è stato piazzato, allora si può fare l'update dei player
                                switch (getCurrentPlayer()) {
                                    case 1:
                                        setEntityInMatrix(finalI, finalJ, EnumEntity.entities.PLAYER1);
                                        break;
                                    case 2:
                                        setEntityInMatrix(finalI, finalJ, EnumEntity.entities.PLAYER2);
                                        break;
                                    case 3:
                                        setEntityInMatrix(finalI, finalJ, EnumEntity.entities.PLAYER3);
                                        break;
                                    case 4:
                                        setEntityInMatrix(finalI, finalJ, EnumEntity.entities.PLAYER4);
                                        break;
                                    case 5:
                                        setEntityInMatrix(finalI, finalJ, EnumEntity.entities.PLAYER5);
                                        break;
                                    default:
                                        break;
                                }
                                updateCurrentPlayer();
                            }
                            initColorSpawnpoint();
                            System.out.println("siamo al giocatore numero" + getCurrentPlayer());
                            System.out.println(getNumPlayers());
                        }
                    });
                }
            }
        }
        SwingUtilities.updateComponentTreeUI(map);
    }

    /*
     * TODO da pulire il codice implementando dove necessario i getter e setter appena creati, rimuovere gli attributi addizionali dei metodi per tenere un'architettura più leggibile
     */
    public static void p1Map() {
        JFrame mapContainer = new JFrame();
        setMapContainer(mapContainer);
        FrameManager.setTitle(getMapContainer());
        FrameManager.setIcon(getMapContainer());
        initCharacterPlacementPhase();
        getMapContainer().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getMapContainer().setSize(1200, 900);

        setNumPlayers(4);//TODO debug sake
            
        getMapContainer().setResizable(false); //TODO prima o poi mettiamole true
        //mapContainer.setMinimumSize(new Dimension(900, 900));

        JLayeredPane layeredPaneGrid = new JLayeredPane();
        setLayeredPaneGrid(layeredPaneGrid);
        getLayeredPaneGrid().setPreferredSize(mapContainer.getSize());
        getLayeredPaneGrid().setLayout(null);

        // Creo una griglia di panels
        JPanel map = new JPanel();
        setMap(map);
        getMap().setBounds(0, 0, (int)getMapContainer().getSize().getWidth(), (int)getMapContainer().getSize().getHeight());
        getMap().setLayout(new GridLayout(ROWS, COLS));  //Imposto il layout del pane che contiene le matrix di bottoni e altri pane

        JPanel[][] mapBase = new JPanel[ROWS][COLS];
        setMapBase(mapBase);
        JButton[][] btnMatrix = new JButton[ROWS][COLS];
        setBtnMatrix(btnMatrix);

        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                final int finalI = i; // devono stare final altrimenti da errori
                final int finalJ = j;
                mapBase[i][j] = new JPanel();
                mapBase[i][j].setBackground(Color.WHITE);
                mapBase[i][j].setLayout(new FlowLayout());
                btnMatrix[i][j] = createButton(i, j);
                if(i == 0 && j == COLS - 1) { //if(è nel bottone finish, ovvero quello dello switch della fase)
                    JButton btnFinish = btnMatrix[i][j];
                    btnFinish.addActionListener(f -> {
                        initializeCurrentPlayer();
                        mapBase[finalI][finalJ].remove(btnFinish);
                        if (getCharacterPlacementPhase() == false) {
                            btnMatrix[finalI][finalJ] = createButton(finalI, finalJ);
                            mapBase[finalI][finalJ].add(btnMatrix[finalI][finalJ], BorderLayout.CENTER);
                        }
                        updateCharacterPlacementPhase();
                        p2Map(map, mapBase, btnMatrix, finalI, finalJ, mapContainer, layeredPaneGrid);
                        mapContainer.repaint();
                        mapContainer.validate();
                    });
                } else {

                    final Color[] previousColorState = { btnMatrix[finalI][finalJ].getBackground() };
                    //btnMatrix[i][j].addActionListener(e -> previousColorState[0] = switchBackground(mapBase, finalI, finalJ, mapBase[finalI][finalJ].getBackground(), btnMatrix, map, mapContainer))
                    btnMatrix[i][j].addMouseListener(new MouseAdapter() {
                    
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (getMapDrawer() == false) {
                                switchBackground(mapBase, finalI, finalJ, mapBase[finalI][finalJ].getBackground(), btnMatrix, map, mapContainer);
                            }
                            if (getCharacterPlacementPhase() == false) {
                                updateMapDrawer(); //se il bool è in true, allora si può disegnare la mappa con il mouse (ecco perché nell'altra fase non viene rispettato questo bool)
                            }
                        };

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            if (getMapDrawer() == true) {
                                switchBackground(mapBase, finalI, finalJ, mapBase[finalI][finalJ].getBackground(), btnMatrix, map, mapContainer);
                            }
                            previousColorState[0] = btnMatrix[finalI][finalJ].getBackground();
                            Color menuColor = Color.decode("#EF3B10"); //da mettere grigio, non arancione
                            btnMatrix[finalI][finalJ].setBackground(menuColor);
                            btnMatrix[finalI][finalJ].setContentAreaFilled(true);
                        }
                        // Reimposta il colore della cella
                        @Override
                        public void mouseExited(MouseEvent e) {
                            btnMatrix[finalI][finalJ].setBackground(previousColorState[0]);
                            btnMatrix[finalI][finalJ].setContentAreaFilled(false);
                        }
                    });
                }
                mapBase[i][j].add(btnMatrix[i][j], BorderLayout.CENTER); //la linea terra di default alla base della mappa
                if(i == 19) {
                    mapBase[i][j].setBackground(Color.BLACK);
                }
                btnMatrix[i][j].setBackground(mapBase[i][j].getBackground());
                mapBase[i][j].setVisible(true);
                
                map.add(mapBase[i][j]);
                //map.add(grubPanel);
            }
        }   
        layeredPaneGrid.add(map, JLayeredPane.DEFAULT_LAYER);
        mapContainer.add(layeredPaneGrid);
        mapContainer.pack();
        mapContainer.setLocationRelativeTo(null);
        mapContainer.setVisible(true);
        map.setVisible(true);
        FrameManager.showMessageBox("Messaggio", "Crea o rimuovi spazi con collisione interagendo con i blocchi per creare la tua mappa!", JOptionPane.INFORMATION_MESSAGE);
    }

}