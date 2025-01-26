package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class PlayerHealth {
    final Galaga game;
    private Texture texture;
    private Array<Sprite> array;

    public PlayerHealth(Galaga game){
        this.game = game;
        texture = new Texture(Gdx.files.internal("pixelHeart.png"));
        array = new Array<>();
        Sprite heartOne = new Sprite(texture);
        Sprite heartTwo = new Sprite(texture);
        Sprite heartThree = new Sprite(texture);
        heartOne.setSize(1f,1f);
        heartTwo.setSize(1f,1f);
        heartThree.setSize(1f,1f);
        heartOne.setX(game.viewport.getWorldWidth()-heartOne.getWidth());
        heartTwo.setX(heartOne.getX()-heartOne.getWidth());
        heartThree.setX(heartTwo.getX()-heartTwo.getWidth());
        heartOne.setY(game.viewport.getWorldHeight()-2);
        heartTwo.setY(game.viewport.getWorldHeight()-2);
        heartThree.setY(game.viewport.getWorldHeight()-2);
        array.addAll(heartOne,heartTwo,heartThree);
    }

    public Array<Sprite> getArray(){
        return array;
    }

}
