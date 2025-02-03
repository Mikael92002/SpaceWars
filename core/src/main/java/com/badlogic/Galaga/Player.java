package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class Player {
    final Galaga game;
    private Texture ship;
    private Texture heroLaser;
    private Sprite shipSprite;
    private int health = 3;
    private Array<Sprite> heroLaserArr;
    Sound laserSound;
    Rectangle shipRec;
    HashMap<Sprite, Rectangle> laserRec;

    public Player(Galaga game) {
        this.game = game;
        ship = new Texture(Gdx.files.internal("spaceShips.png"));
        shipSprite = new Sprite(ship);
        shipSprite.setSize(1, 1);
        shipSprite.setX(game.viewport.getWorldWidth() / 2 - shipSprite.getWidth());
        shipSprite.setY(0);
        heroLaser = new Texture(Gdx.files.internal("shipBeam.png"));
        heroLaserArr = new Array<>();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("laserSound.mp3"));
        shipRec = new Rectangle(shipSprite.getX(), shipSprite.getY(), shipSprite.getWidth(), shipSprite.getHeight());
        laserRec = new HashMap<>();
    }

    public void shoot() {
        Sprite heroLaserSprite = new Sprite(heroLaser);
        Rectangle heroRectangle = new Rectangle();

        heroLaserSprite.setSize(1, 0.5f);
        heroLaserSprite.setX(shipSprite.getX() + 0.05f);
        heroLaserSprite.setY(shipSprite.getHeight());

        heroRectangle.set(heroLaserSprite.getX(), heroLaserSprite.getY(), heroLaserSprite.getWidth(), heroLaserSprite.getY());
        laserSound.play();

        laserRec.put(heroLaserSprite, heroRectangle);
        heroLaserArr.add(heroLaserSprite);

    }

    public Sprite getShipSprite() {
        return shipSprite;
    }

    public void setHealth(int healthAmount) {
        this.health = healthAmount;
    }
    public int getHealth(){
        return this.health;
    }

    public Rectangle getShipRec() {
        return shipRec;
    }

    public Array<Sprite> getHeroLaserArr() {
        return heroLaserArr;
    }

    public HashMap<Sprite, Rectangle> getLaserRec() {
        return laserRec;
    }


}
