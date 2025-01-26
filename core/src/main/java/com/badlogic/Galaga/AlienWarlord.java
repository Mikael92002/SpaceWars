package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class AlienWarlord {
    final Galaga game;
    private Sprite sprite;
    private Texture texture;
    private float vSpeed = -2f;
    private float xSpeed = 0f;
    private float warlordMoveTimer = 0f;
    private float warlordShotTimer = 0f;
    private int warlordHealth = 5;
    float delta = Gdx.graphics.getDeltaTime();
    private Rectangle rectangle;

    public AlienWarlord(Galaga game){
        this.game = game;
        texture = new Texture(Gdx.files.internal("alienWarlord.png"));
        sprite = new Sprite(texture);
        sprite.setSize(1f,1f);
        sprite.setX(game.viewport.getWorldWidth()/2-sprite.getWidth());
        sprite.setY(game.viewport.getWorldHeight());
        rectangle = new Rectangle();
        rectangle.set(sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
    }

    public float getvSpeed(){
        return vSpeed;
    }

    public float getxSpeed() {
        return xSpeed;
    }
    public Sprite getSprite(){
        return sprite;
    }

    public float getWarlordMoveTimer() {
        return warlordMoveTimer;
    }

    public float getWarlordShotTimer() {
        return warlordShotTimer;
    }
    public void setWarlordMoveTimer(float time){
        this.warlordMoveTimer = time;
    }
    public void setWarlordShotTimer(float time){
        this.warlordShotTimer = time;
    }
    public void addShotTime(){
        this.warlordShotTimer+=delta;
    }
    public void addMoveTime(){
        this.warlordMoveTimer+=delta;
    }
    public void randomizeSpeed(){
        float xSpeedNegative = MathUtils.random(-2f,-1f);
        float ySpeedNegative = MathUtils.random(-2f,-1f);
        float xSpeedPositive = MathUtils.random(1f,2f);
        float ySpeedPositive = MathUtils.random(1f,2f);

        float yChance = MathUtils.random(-1f,1f);
        float xChance = MathUtils.random(-1f,1f);
        if(yChance>=0){
            vSpeed = ySpeedPositive;
        }
        else vSpeed = ySpeedNegative;
        if(xChance>=0){
            xSpeed = xSpeedPositive;
        }
        else xSpeed = xSpeedNegative;
    }
    public Rectangle getRectangle(){
        return rectangle;
    }
    public void reduceHealth(){
        warlordHealth--;
    }

    public int getWarlordHealth() {
        return warlordHealth;
    }
}
