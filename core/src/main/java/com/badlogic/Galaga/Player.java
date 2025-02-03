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
    private Texture healthTexture;
    private Array<Sprite> healthSpriteArray;
    private float shipSpeed = 4f;
    float coolDownTimerCoefficient = 1f;
    float powerUpTimer = 0f;
    public boolean powerUpStatus = false;

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
        healthTexture = new Texture(Gdx.files.internal("pixelHeart.png"));
        healthSpriteArray = new Array<>();
        Sprite heartOne = new Sprite(healthTexture);
        Sprite heartTwo = new Sprite(healthTexture);
        Sprite heartThree = new Sprite(healthTexture);
        heartOne.setSize(1f,1f);
        heartTwo.setSize(1f,1f);
        heartThree.setSize(1f,1f);
        heartOne.setX(game.viewport.getWorldWidth()-heartOne.getWidth());
        heartTwo.setX(heartOne.getX()-heartOne.getWidth());
        heartThree.setX(heartTwo.getX()-heartTwo.getWidth());
        heartOne.setY(game.viewport.getWorldHeight()-2);
        heartTwo.setY(game.viewport.getWorldHeight()-2);
        heartThree.setY(game.viewport.getWorldHeight()-2);
        healthSpriteArray.addAll(heartOne,heartTwo,heartThree);
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
    public Array<Sprite> getHealthArray(){
        return healthSpriteArray;
    }
    public void doubleShipSpeed(){
        this.shipSpeed = 8f;
    }
    public void normalizeShipSpeed(){
        this.shipSpeed = 4f;
    }
    public float getShipSpeed(){
        return this.shipSpeed;
    }

}
