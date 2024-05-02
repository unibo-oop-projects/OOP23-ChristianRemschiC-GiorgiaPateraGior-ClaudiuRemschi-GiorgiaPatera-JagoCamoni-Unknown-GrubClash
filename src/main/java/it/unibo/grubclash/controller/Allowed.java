package it.unibo.grubclash.controller;
import it.unibo.grubclash.view.EnumEntity.entities;

public class Allowed { 

    /*
    public static Insets giveInsetsBorder(JFrame map) { //da passare ad ALLOWED per quanto riguarda IsSolid --> ALLOWED viene chiamato qua dentro
        return map.getInsets();
    }
    da finire guida sulle collisioni, una volta risolto il problema dei player sul map builder
    */
    private static int borderX;
    private static int borderY;
    private static Entity[][] lvlData;
    private static int ROWS;
    private static int COLS;

    /*
    TODO mi devo ricordare di eliminare la posizione dei player una volta che sono spawnati i modelli
    addEntity mette nella matrice di entità una nuova entità, da fare controlli necessari affinché nuove entità non vadano in conflitto
    quando il costruttore di allowed viene chiamato, devo passargli anche le const relative all numero di celle, per rendere più flessibile il codice a future implementazioni
    */
    public Allowed(int borderX, int borderY, int ROWS, int COLS) { //quando creo Allowed DENTRO MAP BUILDER chiamo il costruttore e gli passo i bordi del JPanel che contiene il giochino e le righe e colonne totali
        Allowed.borderX = borderX;
        Allowed.borderY = borderY;
        Allowed.ROWS = ROWS;
        Allowed.COLS = COLS;
        Allowed.lvlData = new Entity[ROWS - 1][COLS - 1]; //TODO credo sia il numero giusto con un -1 a fianco, da testare
    }

    public static void addEntity (int x, int y, int height, int width, entities entity, int i, int j) {
        if (Allowed.lvlData[i][j].entity == entity.WALL || Allowed.lvlData[i][j].entity == entity.ITEM) {
            System.out.println("This box is already taken, overwriting floor or item on the map");
        }
        Allowed.lvlData[i][j] = new Entity((float) x, (float) y, width, height, entity); //TODO vorrei rendere entity una classe astratta, almeno per includerne una, ma non so come fare ancora
    }    

    public static boolean CanMoveThere(float x, float y, int width, int height, entities[][] entityMatrix) {
        //controlla ogni angolo del rettangolo, se gli angoli non sono contenuti nel WALL (cioè la piattaforma o i bordi), allora restituisce true
        if (whatIsFacing(x, y, lvlData) != entities.WALL) {
            if(whatIsFacing(x + width, y + width, lvlData) != entities.WALL) {
                if(whatIsFacing(x + width, y, lvlData) != entities.WALL) {
                    if(whatIsFacing(x, y + height, lvlData) != entities.WALL)
                        return true;
                }
            }
        }
        return false;
    }
    
    //controlleremo di volta in volta i quattro angoli di questo "rettangolo"
    //x & y sono la posizione dei giocatori
    private static entities whatIsFacing(float x, float y, Entity[][] lvlData) {
        //controlla, di quei punti dell'angolo che vengono passati, se è dentro un oggetto
        if (x < 0 || x >= borderX) {
            return entities.WALL;
        } 
        if (y < 0 || y >= borderY) {
            return entities.WALL;
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (x >= lvlData[i][j].x && x < (lvlData[i][j].width + lvlData[i][j].x) && y >= lvlData[i][j].x && y < (lvlData[i][j].y + lvlData[i][j].height) ) {
                    return lvlData[i][j].entity; //se le coordinate degli angoli si intersecano, ritorna il tipo di entità
                } 
            }

        }
        return entities.SKY;
    }
}
