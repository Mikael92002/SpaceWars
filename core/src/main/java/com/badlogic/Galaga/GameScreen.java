package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class GameScreen implements Screen {
    final Galaga game;
    //ship:
    private Texture ship;
    private Texture heroLaser;
    private Sprite shipSprite;
    private float health = 3f;
    //background:
    private Array<Sprite> starArray;
    private Array<Sprite> heroLaserArr;
    float gameTimer = 0f;
    boolean isVisible = true;
    float flickerTimer = 0f;
    float FLICKER_INTERVAL = 0.5f;
    float coolDownTimer = 0f;
    float worldWidth;
    float worldHeight;
    Sound laserSound;
    private Texture star;
    private float starTimer = 0f;
    //alien:
    private float alienMovementTimer = 0f;
    private float alienShotTimer = 0f;
    private float alienCreationTimer = 6f;
    private float alienSpeed;
    private Texture alien;
    private Texture alienLaser;
    private Array<Sprite> enemyArray;
    public Array<Sprite> enemyLaserArray;
    private Texture explosion;
    private Music music;
    //explodedArray Aliens:
    private Array<ExplosionTimer> explodedArray;
    private float explosionTimer = 0f;
    private boolean explosiveFlash = true;
    private float explosiveFlashTimer = 0f;


    //collision:
    HashMap<Sprite, Rectangle> laserRec;
    HashMap<Sprite, Rectangle> enemyRec;
    Rectangle shipRec;
    HashMap<Sprite, Rectangle> enemyLaserRec;


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
        alien = new Texture(Gdx.files.internal("alienBasic.png"));
        alienLaser = new Texture(Gdx.files.internal("alienBeam.png"));
        enemyArray = new Array<>();
        enemyLaserArray = new Array<>();
        laserRec = new HashMap<>();
        enemyLaserRec = new HashMap<>();
        shipRec = new Rectangle();
        enemyRec = new HashMap<>();
        explosion = new Texture(Gdx.files.internal("explosion.png"));
        music = Gdx.audio.newMusic(Gdx.files.internal("gameMusic.mp3"));
        music.setLooping(true);
        music.play();
        explodedArray = new Array<>();
    }

    @Override
    public void show() {
        music.play();
    }

    @Override
    public void render(float v) {
        float delta = Gdx.graphics.getDeltaTime();
        coolDownTimer += delta;
        alienShotTimer += delta;
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
        if (gameTimer > 5f) {
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
            alienCreationTimer += delta;
            if (alienCreationTimer > 5f && enemyArray.size < 5) {
                alienCreationTimer = 0;
                createAlien();
            }

            shipSprite.setX(MathUtils.clamp(shipSprite.getX(), 0, worldWidth - shipSprite.getWidth()));
            shipRec.set(shipSprite.getX(), shipSprite.getY(), shipSprite.getWidth(), shipSprite.getHeight());


            for (int i = enemyArray.size - 1; i >= 0; i--) {
                Sprite alienSprite = enemyArray.get(i);
                float alienWidth = alienSprite.getWidth();
                float alienHeight = alienSprite.getHeight();
                System.out.println(enemyArray.size);

                alienSprite.setX(MathUtils.clamp(alienSprite.getX(), 0, worldWidth - alienSprite.getWidth()));
                alienSprite.translateX(alienSpeed * delta);
                enemyRec.get(alienSprite).set(alienSprite.getX(), alienSprite.getY(), alienWidth, alienHeight);


            }
            for (int i = heroLaserArr.size - 1; i >= 0; i--) {
                Sprite heroLaser = heroLaserArr.get(i);
                float heroHeight = heroLaser.getHeight();
                float heroWidth = heroLaser.getWidth();

                heroLaser.translateY(7f * delta);
                Rectangle laserRectangle = laserRec.get(heroLaser);
                if (laserRectangle != null) {
                    laserRectangle.set(heroLaser.getX(), heroLaser.getY(), heroLaser.getWidth(), heroLaser.getHeight());
                }

                for (int j = enemyArray.size - 1; j >= 0; j--) {
                    Sprite alienSprite = enemyArray.get(j);
                    Rectangle rectangle = enemyRec.get(alienSprite);
                    if (laserRec.get(heroLaser) != null) {
                        if (laserRec.get(heroLaser).overlaps(rectangle)) {
                            heroLaserArr.removeIndex(i);
                            enemyArray.get(j).setTexture(explosion);
                            explodedArray.add(new ExplosionTimer(enemyArray.get(j)));
                            enemyArray.removeIndex(j);
                            laserRec.remove(heroLaser);

                        }
                    }

                }
                if (heroLaser.getY() > worldHeight) {
                    heroLaserArr.removeIndex(i);
                }

            }

            for(int i = enemyLaserArray.size;i>=0;i--){

            }

            alienMovementTimer += delta;
            alienShotTimer += delta;
            if (alienMovementTimer > 2f) {
                alienMovementTimer = 0;
                alienSpeed = MathUtils.random(-4f, 4f);
            }



        }
    }

    public void backgroundLogic() {
        if (gameTimer > 5f) {
            float delta = Gdx.graphics.getDeltaTime();
            for (int i = starArray.size - 1; i >= 0; i--) {
                Sprite starSprite = starArray.get(i);
                float starWidth = starSprite.getWidth();
                float starHeight = starSprite.getHeight();
                starSprite.translateY(-2f * delta);

                if (starSprite.getY() < -starHeight) {
                    starArray.removeIndex(i);
                }
            }
            starTimer += delta;
            if (starTimer > 0.5f) {
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
        explosionTimer += delta;
        if (flickerTimer >= FLICKER_INTERVAL && gameTimer < 5f) {
            isVisible = !isVisible;
            flickerTimer = 0;
        }
        if (explosionTimer >= 0.3f) {
            explosiveFlash = !explosiveFlash;
            explosionTimer = 0;
        }
        if (isVisible) {
            game.startFont.draw(game.batch, "[RED]S[][BLUE]T[][YELLOW]A[][RED]R[][BLUE]T[][YELLOW]![]", worldWidth / 2 - 1, worldHeight / 2);
        }
        if (gameTimer > 5f) {
            for (Sprite stars : starArray) {
                stars.draw(game.batch);
            }
            for (Sprite heroLaser : heroLaserArr) {
                heroLaser.draw(game.batch);
            }
            for (Sprite alienslaser : enemyLaserArray) {
                alienslaser.draw(game.batch);
            }
            shipSprite.draw(game.batch);

            for(ExplosionTimer exploded: explodedArray){
                if(explosiveFlash){
                    exploded.returnSprite().draw(game.batch);
                }
                exploded.addTime();
                if(exploded.returnTime()>3f){
                    explodedArray.removeValue(exploded,true);
                }
            }
            for (Sprite aliens : enemyArray) {
                aliens.draw(game.batch);
            }
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
        Rectangle heroRectangle = new Rectangle();

        heroLaserSprite.setSize(1, 0.5f);
        heroLaserSprite.setX(shipSprite.getX() + 0.05f);
        heroLaserSprite.setY(shipSprite.getHeight());

        heroRectangle.set(heroLaserSprite.getX(), heroLaserSprite.getY(), heroLaserSprite.getWidth(), heroLaserSprite.getHeight());

        laserRec.put(heroLaserSprite, heroRectangle);
        heroLaserArr.add(heroLaserSprite);

    }

    public void createStars() {
        float starWidth = MathUtils.random(0.3f, 0.5f);
        float starHeight = MathUtils.random(0.3f, 0.5f);
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        Sprite starSprite = new Sprite(star);
        starSprite.setSize(starWidth, starHeight);
        starSprite.setX(MathUtils.random(0f, worldWidth - starWidth));
        starSprite.setY(worldHeight);
        starArray.add(starSprite);
    }

    public void createAlien() {
        Sprite alienSprite = new Sprite(alien);
        Rectangle alienRectangle = new Rectangle();

        alienSprite.setSize(1f, 1f);
        float initPos = MathUtils.random(0, 3);
        if (initPos < 1) {
            alienSprite.setX(-1);
        } else if (initPos < 2) {
            alienSprite.setX(worldWidth / 2 - alienSprite.getWidth());
        } else alienSprite.setX(worldWidth);

        alienSprite.setY(MathUtils.clamp(worldHeight / 2, worldHeight / 2 - alienSprite.getHeight(), worldHeight - 1));

        alienRectangle.set(alienSprite.getX(), alienSprite.getY(), alienSprite.getWidth(), alienSprite.getHeight());

        enemyRec.put(alienSprite, alienRectangle);
        enemyArray.add(alienSprite);
    }



    /*public void alienMovement(float movementTimer, float shotTimer){
        float delta = Gdx.graphics.getDeltaTime();

        if(movementTimer>12f){
            alienSpeed = MathUtils.random(-4f, 4f);
            movementTimer = 0;
        }


        for(int i = enemyArray.size-1;i>=0;i--){
            Sprite alienSprite = enemyArray.get(i);
            enemyRec.set(alienSprite.getX(),alienSprite.getY(),alienSprite.getWidth(),alienSprite.getHeight());
            if(alienSprite.getX()>0 || alienSprite.getX()<worldWidth){
                alienSprite.setX(MathUtils.clamp(alienSprite.getX(),0f,worldWidth-alienSprite.getWidth()));
            }
            if(movementTimer>10f && alienSprite.getX()<shipSprite.getX()){
                alienSprite.translateX(4f*delta);
            }
            if(movementTimer>10f && alienSprite.getX()>shipSprite.getX()){
                alienSprite.translateX(-4f*delta);
            }
            alienSprite.translateX(alienSpeed*delta);

//            if(shotTimer>2f){
//                createAlienLaser(enemyArray.get(i));
//            }

        }
        for(int i = enemyLaserArray.size-1;i>=0;i--){

            Sprite alienLaserSprite = enemyLaserArray.get(i);
            alienLaserSprite.translateY(5f*delta);
            enemyLaserRec.setX(alienLaserSprite.getX());
            enemyLaserRec.setY(alienLaserSprite.getY());
            enemyLaserRec.setSize(alienLaserSprite.getWidth(),alienLaserSprite.getHeight());


            if(alienLaserSprite.getY()<0){
                enemyLaserArray.removeIndex(i);
            } else if (enemyLaserRec.overlaps(shipRec)) {
                //play sound, lose health
                health--;
                enemyLaserArray.removeIndex(i);
            }
        }

//        if(shotTimer>2f){
//            shotTimer = 0;
//            for(int i = enemyArray.size-1;i>=0;i--){
//                createAlienLaser(enemyArray.get(i));
//            }
//        }

    }*/

    public void createAlienLaser(Sprite alien) {
        Sprite alienLaserSprite = new Sprite(alienLaser);

        alienLaserSprite.setSize(1f, 0.5f);
        alienLaserSprite.setX(alien.getX());
        alienLaserSprite.setY(alien.getY() - alien.getHeight());

        enemyLaserArray.add(alienLaserSprite);
    }

}
