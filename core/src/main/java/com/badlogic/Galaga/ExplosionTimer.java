package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;

public class ExplosionTimer {
    private float timer;
    Sprite sprite;

    public ExplosionTimer(Sprite sprite){
        this.sprite = sprite;
        timer = 0f;
    }

    public void addTime(){
        float delta = Gdx.graphics.getDeltaTime();
        timer+=delta;
    }

    public float returnTime(){
        return timer;
    }
    public Sprite returnSprite(){
        return sprite;
    }
}
