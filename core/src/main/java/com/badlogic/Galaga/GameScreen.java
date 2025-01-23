package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import java.security.Key;

public class GameScreen implements Screen {
    final Galaga game;
    private Texture ship;
    private Texture heroLaser;
    private Sprite shipSprite;
    private Array<Sprite> starArray;
    private Array<Sprite> heroLaserArr;
    float gameTimer = 0f;
    boolean isVisible = true;
    float flickerTimer = 0f;
    float FLICKER_INTERVAL = 0.5f;
    float coolDownTimer = 0f;
    float coolDownInterval = 0.5f;
    boolean coolDownReset = true;
    float worldWidth;
    float worldHeight;
    Sound laserSound;
    private Texture star;
    private float starTimer = 0f;

    Rectangle laserRec;


    public GameScreen(Galaga game) {
        this.game = game;
        worldWidth = game.viewport.getWorldWidth();
        worldHeight = game.viewport.getWorldHeight();
        ship = new Texture(Gdx.files.internal("spaceShips.png"));
        star = new Texture(Gdx.files.internal("star.png"));
        shipSprite = new Sprite(ship);
        shipSprite.setSize(1, 1);
        shipSprite.setX(worldWidth / 2 - shipSprite.getWidth());
        shipSprite.setY(0);
        starArray = new Array<>();
        heroLaser = new Texture(Gdx.files.internal("shipBeam.png"));
        heroLaserArr = new Array<>();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("laserSound.mp3"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        float delta = Gdx.graphics.getDeltaTime();
        coolDownTimer += delta;
        input();
        logic();
        backgroundLogic();
        draw();
    }

    @Override
    public void resize(int i, int i1) {
        game.viewport.update(i, i1, true);
    }

    public void input() {
        if(gameTimer>5f){
        float delta = Gdx.graphics.getDeltaTime();
        float speed = 4f;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            shipSprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            shipSprite.translateX(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && coolDownTimer >= 0.25f) {
            laserSound.play();
            shoot();
            coolDownTimer = 0;
        }
        }
    }

    public void logic() {
        if (gameTimer > 5f) {
            float delta = Gdx.graphics.getDeltaTime();
            shipSprite.setX(MathUtils.clamp(shipSprite.getX(), 0, worldWidth - shipSprite.getWidth()));

            for (int i = heroLaserArr.size - 1; i >= 0; i--) {
                Sprite heroLaser = heroLaserArr.get(i);
                float heroHeight = heroLaser.getHeight();
                float heroWidth = heroLaser.getWidth();

                heroLaser.translateY(7f * delta);
                if (heroLaser.getY() > worldHeight) {
                    heroLaserArr.removeIndex(i);
                }
            }

        }
    }
    public void backgroundLogic(){
        if(gameTimer>5f){
        float delta = Gdx.graphics.getDeltaTime();
        for(int i = starArray.size-1; i>=0;i--){
            Sprite starSprite = starArray.get(i);
            float starWidth = starSprite.getWidth();
            float starHeight = starSprite.getHeight();
            starSprite.translateY(-2f*delta);

            if(starSprite.getY() < -starHeight){
                starArray.removeIndex(i);
            }
        }
        starTimer+=delta;
        if(starTimer>0.5f){
            createStars();
            starTimer = 0;
        }
        }
    }

    public void draw() {
        /*float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();*/

        float delta = Gdx.graphics.getDeltaTime();

        game.batch.begin();

        game.batch.draw(game.background, 0, 0, worldWidth, worldHeight);
        gameTimer += delta;
        flickerTimer += delta;
        if (flickerTimer >= FLICKER_INTERVAL && gameTimer < 5f) {
            isVisible = !isVisible;
            flickerTimer = 0;
        }
        if (isVisible) {
            game.startFont.draw(game.batch, "[RED]S[][BLUE]T[][YELLOW]A[][RED]R[][BLUE]T[][YELLOW]![]", worldWidth / 2 - 1, worldHeight / 2);
        }
        if (gameTimer > 5f) {
            for(Sprite stars:starArray){
                stars.draw(game.batch);
            }
            for (Sprite heroLaser : heroLaserArr) {
                heroLaser.draw(game.batch);
                System.out.println(heroLaserArr.size);
            }
            shipSprite.draw(game.batch);

        }

        game.batch.end();
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

    public void shoot() {
        Sprite heroLaserSprite = new Sprite(heroLaser);
        heroLaserSprite.setSize(1, 0.5f);

        heroLaserSprite.setX(shipSprite.getX() + 0.05f);
        heroLaserSprite.setY(shipSprite.getHeight());

        heroLaserArr.add(heroLaserSprite);
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
