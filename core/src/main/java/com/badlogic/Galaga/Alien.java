package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Alien {
    final Galaga game;
    Sprite alien;
    Texture alienTexture;
    float shotTimer = MathUtils.random(4f,4.5f);
    float delta;
    Rectangle hitbox;
    float moveTimer = 1f;
    float alienXSpeed = 0f;
    float alienYSpeed = 0f;

    public Alien(Galaga game){
        this.game = game;
        alienTexture = new Texture(Gdx.files.internal("alienBasic.png"));
        this.alien = new Sprite(alienTexture);
        this.alien.setSize(1f,1f);
        this.alien.setX(MathUtils.clamp(MathUtils.random(0,game.viewport.getWorldWidth()-alien.getWidth()),0,game.viewport.getWorldWidth()-alien.getWidth()));
        this.alien.setY(MathUtils.clamp(MathUtils.random(game.viewport.getWorldHeight()/2-alien.getHeight(),game.viewport.getWorldHeight()-alien.getHeight()),game.viewport.getWorldHeight()/2-alien.getHeight(),game.viewport.getWorldHeight()-alien.getHeight()));
        hitbox = new Rectangle();
        hitbox.set(alien.getX(), alien.getY(), alien.getWidth(),alien.getHeight());
        delta = Gdx.graphics.getDeltaTime();
    }

    public void addShotTime(){
        shotTimer+=delta;
    }
    public void addMoveTimer(){
        moveTimer+=delta;
    }
    public void randomizeSpeed(){
        float lowestNegative = MathUtils.random(-4f,-3f);
        float highestPositive = MathUtils.random(3f,4f);
        float chance = MathUtils.random(-1f,1f);
        if(chance>=0){
        alienXSpeed = highestPositive;
        }
        else alienXSpeed = lowestNegative;
    }

    public void randomizeYSpeed(){
        float lowestNegative = MathUtils.random(-4f,-3f);
        float highestPositive = MathUtils.random(3f,4f);
        float chance = MathUtils.random(-1f,1f);
        if(chance>=0){
            alienYSpeed = highestPositive;
        }
        else alienYSpeed = lowestNegative;
    }
    public void setMoveTimer(float timer){
        this.moveTimer = timer;
    }
    public void setShotTimer(float timer){
        this.shotTimer = timer;
    }
    public float getAlienXSpeed(){
        return alienXSpeed;
    }
    public void setAlienXSpeed(float speed){
        this.alienXSpeed = speed;
    }
    public float getAlienYSpeed(){
        return alienYSpeed;
    }
    public void setAlienYSpeed(float speed){
        this.alienXSpeed = speed;
    }

    public float getShotTimer(){
        return shotTimer;
    }
    public float getMoveTimer(){
        return moveTimer;
    }
    public Sprite getAlien(){
        return alien;
    }
    public Rectangle getRectangle(){
        return hitbox;
    }

}
