package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final Galaga game;
    private Array<Sprite> starArray;
    private Texture star;
    private float starTimer;
    private float shipTimer;
    private float shipSpeedY = 0.1f;
    private float shipSpeedX = 0f;
    private Texture ship;
    Sprite shipSprite;

    public MainMenuScreen(final Galaga game) {
        this.game = game;
        starArray = new Array<>();
        star = new Texture(Gdx.files.internal("star.png"));
        ship = new Texture(Gdx.files.internal("spaceShips.png"));
        shipSprite = new Sprite(ship);
        shipSprite.setSize(1,1);
        shipSprite.setX(game.viewport.getWorldWidth()/2);
        shipSprite.setY(game.viewport.getWorldHeight()/2);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        logic();
        draw();
    }

    @Override
    public void resize(int i, int i1) {
        game.viewport.update(i, i1, true);
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

    public void logic() {
    float delta = Gdx.graphics.getDeltaTime();
    float worldWidth = game.viewport.getWorldWidth();
    float worldHeight = game.viewport.getWorldHeight();

    float shipWidth = shipSprite.getWidth();
    float shipHeight = shipSprite.getHeight();


    shipSprite.setX(MathUtils.clamp(shipSprite.getX(), 0, worldWidth-shipWidth));
    shipSprite.setY(MathUtils.clamp(shipSprite.getY(),0,worldHeight-shipHeight));


    for(int i = starArray.size-1; i>=0;i--){
        Sprite starSprite = starArray.get(i);
        float starWidth = starSprite.getWidth();
        float starHeight = starSprite.getHeight();

        shipSprite.translateY(shipSpeedY*delta);
        shipSprite.translateX(shipSpeedX*delta);
        starSprite.translateY(-2f*delta);

        if(starSprite.getY() < -starHeight){
            starArray.removeIndex(i);
        }
    }
        starTimer+=delta;
        shipTimer+=delta;
        if(shipTimer>1f){
            shipSpeedY = MathUtils.random(-0.1f,0.1f);
            shipSpeedX = MathUtils.random(-0.1f,0.1f);
            shipTimer = 0;
        }
        if(starTimer>0.5f){
            createStars();
        starTimer = 0;
        }
    }

    public void draw(){
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        game.batch.draw(game.background, 0, 0, worldWidth, worldHeight);
        for(Sprite stars:starArray){
            stars.draw(game.batch);
        }
        shipSprite.draw(game.batch);
        game.welcomeFont.draw(game.batch, "WELCOME TO GALAGA!", 0.3f, worldHeight / 2);
        game.pressFont.draw(game.batch, "Press anywhere to begin", 1.5f, worldHeight / 2 - 2);

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
        game.batch.end();
    }

    public void createStars() {
        float starWidth = MathUtils.random(0.3f,0.5f);
        float starHeight = MathUtils.random(0.3f,0.5f);
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        Sprite starSprite = new Sprite(star);
        starSprite.setSize(starWidth, starHeight);
        starSprite.setX(MathUtils.random(0f, worldWidth - starWidth));
        starSprite.setY(worldHeight);
        starArray.add(starSprite);
    }
}
