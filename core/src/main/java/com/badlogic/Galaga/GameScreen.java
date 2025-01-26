package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashMap;

public class GameScreen implements Screen {
    final Galaga game;
    //ship:
    private Texture ship;
    private Texture heroLaser;
    private Sprite shipSprite;
    private int health = 3;
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
    private Array<Alien> enemyArray;
    public Array<Sprite> enemyLaserArray;
    private Texture explosion;
    private Music music;
    //explodedArray Aliens:
    private Array<ExplosionTimer> explodedArray;
    private float explosionTimer = 0f;
    private boolean explosiveFlash = true;
    private float explosiveFlashTimer = 0f;
    private Sound destroySound;
    //Health:
    private PlayerHealth healthArray;
    //alien:
    private Sound alienLaserSound;
    //Score
    public int score = 0;
    //AlienWarlord:
    AlienWarlord alienWarlord = null;
    boolean bossAlive = false;
    private int warlordHealth = 5;
    boolean warlordHit = false;
    WarlordHealth warlordHealthGraphic;



    //collision:
    HashMap<Sprite, Rectangle> laserRec;
    HashMap<Alien, Rectangle> enemyRec;
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
        music.setVolume(0.5f);
        explodedArray = new Array<>();
        destroySound = Gdx.audio.newSound(Gdx.files.internal("destroySound.mp3"));
        healthArray = new PlayerHealth(this.game);
        alienLaserSound = Gdx.audio.newSound(Gdx.files.internal("shootSound.mp3"));
        warlordHealthGraphic = new WarlordHealth(this.game);

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
            if (Gdx.input.isKeyPressed(Input.Keys.UP) && coolDownTimer >= 0.75f) {
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
            if (alienCreationTimer > 3f && enemyArray.size < 5) {
                alienCreationTimer = 0;
                createAlien();
                float chance = MathUtils.random(0, 1f);
                if (chance < 0.25f && score<=1300 && enemyArray.size<=3) {
                    createAlien();
                    createAlien();
                }
            }

            shipSprite.setX(MathUtils.clamp(shipSprite.getX(), 0, worldWidth - shipSprite.getWidth()));
            shipRec.set(shipSprite.getX(), shipSprite.getY(), shipSprite.getWidth(), shipSprite.getHeight());


            for (int i = enemyArray.size - 1; i >= 0; i--) {
                Alien alien = enemyArray.get(i);
                float alienWidth = alien.getAlien().getWidth();
                float alienHeight = alien.getAlien().getWidth();

                alien.addMoveTimer();
                alien.addShotTime();

                if (alien.getMoveTimer() > 1f) {
                    alien.setMoveTimer(0);
                    alien.randomizeSpeed();
                    alien.randomizeYSpeed();
                }
                if (alien.getShotTimer() > 5f) {
                    alien.setShotTimer(0);
                    createAlienLaser(alien.getAlien());
                    alienLaserSound.play();
                }
                alien.getAlien().setX(MathUtils.clamp(alien.getAlien().getX(), 0, game.viewport.getWorldWidth() - alien.getAlien().getWidth()));
                alien.getAlien().setY(MathUtils.clamp(alien.getAlien().getY(), worldHeight / 2 - alienWidth, worldHeight - alienWidth));

                alien.getAlien().translateX(alien.getAlienXSpeed() * delta);
                alien.getAlien().translateY(alien.getAlienYSpeed() * delta);
                enemyRec.get(alien).set(alien.getAlien().getX(), alien.getAlien().getY(), alienWidth, alienHeight);


            }


            for (int i = heroLaserArr.size - 1; i >= 0; i--) {
                Sprite heroLaser = heroLaserArr.get(i);
                float heroHeight = heroLaser.getHeight();
                float heroWidth = heroLaser.getWidth();

                heroLaser.translateY(5f * delta);
                Rectangle laserRectangle = laserRec.get(heroLaser);
                if (laserRectangle != null) {
                    laserRectangle.set(heroLaser.getX(), heroLaser.getY(), heroLaser.getWidth(), heroLaser.getHeight());
                }

                for (int j = enemyArray.size - 1; j >= 0; j--) {
                    Alien alienSprite = enemyArray.get(j);
                    Rectangle rectangle = enemyRec.get(alienSprite);
                    if (laserRec.get(heroLaser) != null) {
                        if (laserRec.get(heroLaser).overlaps(rectangle)) {
                            destroySound.play();
                            heroLaserArr.removeIndex(i);
                            enemyArray.get(j).getAlien().setTexture(explosion);
                            explodedArray.add(new ExplosionTimer(enemyArray.get(j).getAlien()));
                            enemyArray.removeIndex(j);
                            laserRec.remove(heroLaser);
                            score += 100;
                        }


                    }



                }
                if (heroLaser.getY() > worldHeight) {
                    heroLaserArr.removeIndex(i);
                }

                if(alienWarlord != null){
                if(laserRec.get(heroLaser) != null){
                    if(laserRec.get(heroLaser).overlaps(alienWarlord.getRectangle())){
                        warlordHealth--;
                        warlordHealthGraphic.remove();
                        heroLaserArr.removeIndex(i);
                    }
                }
            }
            }

            for (int i = enemyLaserArray.size - 1; i >= 0; i--) {
                Sprite alienLaserSprite = enemyLaserArray.get(i);

                alienLaserSprite.translateY(-4f * delta);

                Rectangle laserRectangle = enemyLaserRec.get(alienLaserSprite);
                if (laserRectangle != null) {
                    laserRectangle.set(alienLaserSprite.getX(), alienLaserSprite.getY(), alienLaserSprite.getWidth(), alienLaserSprite.getHeight());
                }

                if (laserRectangle != null) {
                    if (laserRectangle.overlaps(shipRec)) {
                        enemyLaserArray.removeIndex(i);
                        health--;
                    }
                }
            }

            alienMovementTimer += delta;
            alienShotTimer += delta;
            if (alienMovementTimer > 2f) {
                alienMovementTimer = 0;
                alienSpeed = MathUtils.random(-4f, 4f);
            }


        }
        if(score % 2000 == 0 && score > 0 && !bossAlive){
            alienWarlord = new AlienWarlord(this.game);
            warlordHealth = 5;
            warlordHealthGraphic = new WarlordHealth(this.game);
        }
        if (alienWarlord != null) {
            if(warlordHealth<=0){
                    alienWarlord.getSprite().setTexture(explosion);
                    destroySound.play();
                    score+=300;
                    explodedArray.add(new ExplosionTimer(alienWarlord.getSprite()));
                    bossAlive = false;
                alienWarlord = null;
                return;
            }
            bossAlive = true;

            float delta = Gdx.graphics.getDeltaTime();
            alienWarlord.getSprite().setX(MathUtils.clamp(alienWarlord.getSprite().getX(),0,worldWidth-alienWarlord.getSprite().getWidth()));
            alienWarlord.getSprite().setY(MathUtils.clamp(alienWarlord.getSprite().getY(),worldHeight/2-alienWarlord.getSprite().getHeight(),worldHeight-alienWarlord.getSprite().getHeight()));

            alienWarlord.addMoveTime();
            alienWarlord.addShotTime();
            if (alienWarlord.getWarlordShotTimer() > 1f) {
                alienWarlord.setWarlordShotTimer(0);
                createAlienLaser(alienWarlord.getSprite());
                alienLaserSound.play();
            }
            if (alienWarlord.getWarlordMoveTimer() > 2f) {
                alienWarlord.setWarlordMoveTimer(0);
                alienWarlord.randomizeSpeed();
            }
            alienWarlord.getSprite().translateY(alienWarlord.getvSpeed() * delta);
            alienWarlord.getSprite().translateX(alienWarlord.getxSpeed() * delta);
            alienWarlord.getRectangle().set(alienWarlord.getSprite().getX(), alienWarlord.getSprite().getY(), alienWarlord.getSprite().getWidth(), alienWarlord.getSprite().getHeight());

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
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
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
        if (isVisible && gameTimer < 5f) {
            game.startFont.draw(game.batch, "[RED]S[][BLUE]T[][YELLOW]A[][RED]R[][BLUE]T[][YELLOW]![]", worldWidth / 2 - 1, worldHeight / 2);
        }
        if (gameTimer > 5f) {
            for (Sprite stars : starArray) {
                stars.draw(game.batch);
            }
            for (int i = health - 1; i >= 0; i--) {
                healthArray.getArray().get(i).draw(game.batch);
            }
            game.score.draw(game.batch, "[YELLOW]S[][RED]C[][BLUE]O[][GREEN]R[][YELLOW]E[] " + score, 0, worldHeight - 1.3f);
            for (Sprite heroLaser : heroLaserArr) {
                heroLaser.draw(game.batch);
            }
            for (Sprite aliensLaser : enemyLaserArray) {
                aliensLaser.draw(game.batch);
            }
            shipSprite.draw(game.batch);


            if (score >= 2000 && warlordHealth>0) {
                game.warlordHealthInfo.draw(game.batch,"ALIEN WARLORD", 2f, worldHeight-0.3f);
                for(Sprite health:warlordHealthGraphic.getArray()){
                    health.draw(game.batch);
                }
                alienWarlord.getSprite().draw(game.batch);
            }

            for (ExplosionTimer exploded : explodedArray) {
                if (explosiveFlash) {
                    exploded.returnSprite().draw(game.batch);
                }
                exploded.addTime();
                if (exploded.returnTime() > 3f) {
                    explodedArray.removeValue(exploded, true);
                }
            }
            for (Alien aliens : enemyArray) {
                aliens.getAlien().draw(game.batch);
            }
            if (health <= 0) {
                music.stop();
                game.setScreen(new Gameover(game, this));
                //SET DISPOSE METHODS::::
                dispose();
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
    //this.game.batch.dispose();
    this.alien.dispose();
    this.destroySound.dispose();
    this.alienLaserSound.dispose();
    this.heroLaser.dispose();
    this.explosion.dispose();
    this.music.dispose();
    this.ship.dispose();
    this.star.dispose();
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
        Alien alienSprite = new Alien(this.game);

        enemyRec.put(alienSprite, alienSprite.getRectangle());
        enemyArray.add(alienSprite);
    }

    public void createAlienLaser(Sprite alien) {
        Sprite alienLaserSprite = new Sprite(alienLaser);
        Rectangle laserRectangle = new Rectangle();

        alienLaserSprite.setSize(1f, 0.5f);
        alienLaserSprite.setX(alien.getX());
        alienLaserSprite.setY(alien.getY() - alien.getHeight());
        laserRectangle.set(alienLaserSprite.getX(), alienLaserSprite.getY(), alienLaserSprite.getWidth(), alienLaserSprite.getHeight());

        enemyLaserRec.put(alienLaserSprite, laserRectangle);
        enemyLaserArray.add(alienLaserSprite);
    }

}
