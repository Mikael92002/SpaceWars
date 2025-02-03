package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Octopus {
    final Galaga game;
    private Texture octopus;
    private Sprite octopusSprite;
    float shotTimer = 0f;
    float teleportTimer = 0f;
    Rectangle hitbox;
    float octopusXSpeed;
    float octopusYSpeed;

    public Octopus(Galaga game){
        this.game = game;
        octopus = new Texture(Gdx.files.internal("basicOctopus.png"));
    }

}
