package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class WarlordHealth {
    final Galaga game;
    private Texture bossHealthOne;
    private Texture bossHealthTwo;
    private Texture bossHealthThree;
    private Texture bossHealthFour;
    private Texture bossHealthFive;
    private Array<Sprite> array;

    public WarlordHealth(Galaga game){
        this.game = game;
        bossHealthOne = new Texture(Gdx.files.internal("bossHealthOne.png"));
        bossHealthTwo = new Texture(Gdx.files.internal("bossHealthTwo.png"));
        bossHealthThree = new Texture(Gdx.files.internal("bossHealthThree.png"));
        bossHealthFour = new Texture(Gdx.files.internal("bossHealthFour.png"));
        bossHealthFive = new Texture(Gdx.files.internal("bossHealthFive.png"));
        array = new Array<>();
        array.add(new Sprite(bossHealthOne));
        array.add(new Sprite(bossHealthTwo));
        array.add(new Sprite(bossHealthThree));
        array.add(new Sprite(bossHealthFour));
        array.add(new Sprite(bossHealthFive));
        float xPos = 2.7f;
        float yPos = game.viewport.getWorldHeight()-1f;
        for(Sprite sprites:array){
            sprites.setSize(0.25f,0.25f);
            sprites.setX(xPos);
            sprites.setY(yPos);
            xPos+=0.25f;
        }
    }

    public Array<Sprite> getArray(){
        return array;
    }

    public void remove(){
        array.removeIndex(array.size-1);
    }

}
