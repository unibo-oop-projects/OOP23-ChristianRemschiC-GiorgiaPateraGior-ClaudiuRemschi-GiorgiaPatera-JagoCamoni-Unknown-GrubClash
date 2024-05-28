package it.unibo.grubclash.controller.Implementation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import it.unibo.grubclash.model.Implementation.GrubPanel;
import it.unibo.grubclash.view.Implementation.EnumEntity;
import it.unibo.grubclash.view.Implementation.MapBuilder;
import it.unibo.grubclash.view.Implementation.EnumEntity.status;

public class Heal extends Entity{

    final char FS = File.separatorChar;

    public BufferedImage stand1, stand2;

    public Heal(GrubPanel grubPanel, int id) {

        super(grubPanel);

        this.x = EnumEntity.buttonToCoordsXConverter(MapBuilder.entityMatrix, EnumEntity.idToHealConverter(id).get());
        this.y = EnumEntity.buttonToCoordsYConverter(MapBuilder.entityMatrix, EnumEntity.idToHealConverter(id).get());

        this.width = 20;
        this.height = 20;
        setEntity(EnumEntity.idToHealConverter(id).get());
        Allowed.addDynamicEntity(this);
        getImage();
    }

    private void getImage() {

        stand1 = setup("src" + FS + "main" + FS + "resources" + FS + "items" + FS + "heal_1.png", this.width+23, this.height+23);
        stand2 = setup("src" + FS + "main" + FS + "resources" + FS + "items" + FS + "heal_2.png", this.width+23, this.height+23);    
    }

    public void draw(Graphics2D g2d){
        if(working == status.ALIVE){
            g2d.drawImage(stand1, x, y,null);
        }else{
            g2d.drawImage(stand2, x, y,null);
            Allowed.removeDynamicEntity(this);
        }
    }
    
}