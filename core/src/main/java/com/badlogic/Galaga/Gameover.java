package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

public class Gameover implements Screen {
    final Galaga game;


    public Gameover(Galaga game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        game.batch.begin();

        game.batch.draw(game.background,0,0,worldWidth,worldHeight);
    game.gameOverFont.draw(game.batch, "GAME\nOVER!",2f,worldHeight/2+1);
    game.tryAgainFont.draw(game.batch, "TRY AGAIN",2.3f,worldHeight/2-2);

    game.batch.end();
    }

    @Override
    public void resize(int i, int i1) {
    game.viewport.update(i,i1,true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
