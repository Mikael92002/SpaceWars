package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class Octopus {
    final Galaga game;
    private Texture octopus;
    private Sprite octopusSprite;
    private boolean visibility = true;
    float shotTimer = 0.5f;
    float teleportTimer = 0f;
    float teleportFlicker = 0f;
    float teleportFlickerInterval = 0.10f;
    Rectangle hitbox;
    float octopusXSpeed = MathUtils.random(-2f,2f);
    float octopusYSpeed = MathUtils.random(-2f,2f);
    float delta;



    public Octopus(Galaga game){
        this.game = game;
        octopus = new Texture(Gdx.files.internal("basicOctopus.png"));
        octopusSprite = new Sprite(octopus);
        octopusSprite.setSize(0.6f,0.6f);
        octopusSprite.setX(MathUtils.random(0, game.viewport.getWorldWidth()-octopusSprite.getWidth()));
        octopusSprite.setY(MathUtils.random(game.viewport.getWorldHeight()/2-octopusSprite.getHeight(), game.viewport.getWorldHeight()-octopusSprite.getHeight()));
        hitbox = new Rectangle();
        hitbox.set(octopusSprite.getX(),octopusSprite.getY(),octopusSprite.getWidth(),octopusSprite.getHeight());
        delta = Gdx.graphics.getDeltaTime();


    }

    public void addShotTime(){
        shotTimer+=delta;
    }

    public void addTeleportTime(){
        teleportTimer+=delta;
    }

    public void teleportLogic(){
        if(teleportTimer>=1.25f){
            teleportFlicker+=delta;

            if(teleportFlicker>=teleportFlickerInterval) {
                visibility = !visibility;
                teleportFlicker = 0;
            }
        }
        if(teleportTimer>=2f){
            octopusSprite.setX(MathUtils.random(0,game.viewport.getWorldWidth()-octopusSprite.getWidth()));
            octopusSprite.setY(MathUtils.random(game.viewport.getWorldHeight()/2 - octopusSprite.getHeight(), game.viewport.getWorldHeight()-octopusSprite.getHeight()));
            visibility = true;
            teleportTimer = 0;
        }
    }
    public Sprite getOctopusSprite(){
        return octopusSprite;
    }
    public Rectangle getOctopusHitBox(){
        return hitbox;
    }
    public boolean isVisibility(){
        return visibility;
    }
    public void randomizeSpeed(){
        octopusXSpeed = MathUtils.random(-2f,2f);
        octopusYSpeed = MathUtils.random(-2f,2f);
    }
}
