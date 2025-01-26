package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class Gameover implements Screen {
    final Galaga game;
    final GameScreen gameScreen;
    private Sound sound;
    int score;


    public Gameover(Galaga game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        sound = Gdx.audio.newSound(Gdx.files.internal("gameOver.mp3"));
        score = gameScreen.score;
    }

    @Override
    public void show() {
    sound.play();
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
    game.gameOverFont.draw(game.batch, "[RED]G[][GREEN]A[][YELLOW]M[][BLUE]E[]\n[GREEN]O[][YELLOW]V[][RED]E[][BLUE]R[][GREEN]![]",2f,worldHeight/2+1);
    game.tryAgainFont.draw(game.batch, "PRESS ANYWHERE  TO  TRY AGAIN",1.3f,worldHeight/2-2);
    game.endGameScore.draw(game.batch, "YOUR SCORE WAS [RED] " + score + "[]", 1.85f,worldHeight/2-1.5f);

    if(Gdx.input.isTouched()){
        game.setScreen(new GameScreen(this.game));
    }

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
