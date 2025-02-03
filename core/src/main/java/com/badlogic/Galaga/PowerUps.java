package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class PowerUps {
    final Galaga game;
    private Texture shipSpeed;
    private Texture health;
    private Texture multipleLaser;
    private Sprite healthSprite;
    private Sprite shipSpeedSprite;
    private Sprite multipleLaserSprite;
    boolean powerUpActive = false;
    float powerUpTimer = 0f;
    Sprite activeSprite;
    Rectangle powerUpRectangle;
    Sound lightningPickUp;
    Sound laserPickUp;
    Sound healthPickUp;

    public PowerUps(Galaga game){
        this.game = game;
        shipSpeed = new Texture(Gdx.files.internal("pixelatedLightning.png"));
        health = new Texture(Gdx.files.internal("pixelHeart.png"));
        multipleLaser = new Texture(Gdx.files.internal("multipleShipBeam.png"));
        createPowerUp();
        laserPickUp = Gdx.audio.newSound(Gdx.files.internal("laserPickUp.mp3"));
        healthPickUp = Gdx.audio.newSound(Gdx.files.internal("healthPickUp.mp3"));
        lightningPickUp = Gdx.audio.newSound(Gdx.files.internal("lightningPickUp.mp3"));
    }

    public void createHealth(){
        healthSprite = new Sprite(this.health);
        healthSprite.setSize(0.5f,0.5f);
        healthSprite.setX(MathUtils.random(0,game.viewport.getWorldWidth()-healthSprite.getWidth()));
        healthSprite.setY(game.viewport.getWorldHeight());
    }
    public void createShipSpeed(){
        shipSpeedSprite = new Sprite(this.shipSpeed);
        shipSpeedSprite.setSize(0.25f, 0.25f);
        shipSpeedSprite.setX(MathUtils.random(0,game.viewport.getWorldWidth()-shipSpeedSprite.getWidth()));
        shipSpeedSprite.setY(game.viewport.getWorldHeight());
    }
    public void createMultipleLasers(){
        multipleLaserSprite = new Sprite(this.multipleLaser);
        multipleLaserSprite.setSize(0.5f, 0.5f);
        multipleLaserSprite.setX(MathUtils.random(0,game.viewport.getWorldWidth()-multipleLaserSprite.getWidth()));
        multipleLaserSprite.setY(game.viewport.getWorldHeight());
    }

    public void createPowerUp(){
        float chance = MathUtils.random(1,3);

        if(chance <= 1){
            createShipSpeed();
            powerUpActive = true;
            activeSprite = shipSpeedSprite;
            powerUpRectangle = new Rectangle();
            powerUpRectangle.set(activeSprite.getX(), activeSprite.getY(), activeSprite.getWidth(), activeSprite.getHeight());

        }
        else if(chance <=2){
            createHealth();
            powerUpActive = true;
            activeSprite = healthSprite;
            powerUpRectangle = new Rectangle();
            powerUpRectangle.set(activeSprite.getX(),activeSprite.getY(), activeSprite.getWidth(), activeSprite.getHeight());
        }
        else {
            createMultipleLasers();
            powerUpActive = true;
            activeSprite = multipleLaserSprite;
            powerUpRectangle = new Rectangle();
            powerUpRectangle.set(activeSprite.getX(),activeSprite.getY(),activeSprite.getWidth(),activeSprite.getHeight());
        }
    }

    public void activatePowerUp(Player sprite){
        if(activeSprite == multipleLaserSprite){
        sprite.coolDownTimerCoefficient = 0.75f;
        laserPickUp.play();
        }
        else if (activeSprite == healthSprite){
            if(sprite.getHealth()<=2){
        sprite.setHealth(sprite.getHealth()+1);}
            healthPickUp.play();
        }
        else if(activeSprite == shipSpeedSprite){
            sprite.doubleShipSpeed();
            lightningPickUp.play();
        }
    }
}
