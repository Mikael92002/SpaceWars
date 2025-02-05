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
    Player player;
    //background:
    private Array<Sprite> starArray;
    float gameTimer = 0f;
    boolean isVisible = true;
    float flickerTimer = 0f;
    float FLICKER_INTERVAL = 0.5f;
    float coolDownTimer = 0f;
    float worldWidth;
    float worldHeight;
    private Texture star;
    private float starTimer = 0f;
    //alien:
    private float alienMovementTimer = 0f;
    private float alienShotTimer = 0f;
    private float alienCreationTimer = 6f;
    private float alienSpeed;
    private Texture alien;
    private Texture alienLaser;
    private Array<Alien> enemyAlienArray;
    public Array<Sprite> enemyLaserArray;
    private Texture explosion;
    private Music music;
    //explodedArray Aliens:
    private Array<ExplosionTimer> explodedArray;
    private float explosionTimer = 0f;
    private boolean explosiveFlash = true;
    private Sound destroySound;

    //Health:

    //alien:
    private Sound alienLaserSound;
    //Score
    public int score = 0;
    //AlienWarlord:
    AlienWarlord alienWarlord = null;
    boolean bossAlive = false;
    private int warlordHealth = 5;
    int warlordKills = 0;

    //collision:
    HashMap<Alien, Rectangle> enemyAlienRec;
    HashMap<Sprite, Rectangle> enemyLaserRec;
    HashMap<Octopus, Rectangle> enemyOctopusRec;

    //powerUps:
    PowerUps powerUps;
    boolean powerUpActive = false;
    float powerUpSpawnCooldown = 0f;

    //octopus:
    Array<Octopus> octopusArray;
    Array<Sprite> octopusLaserArray;
    int octopusLaserRotation = 1;
    int overlordLaserRotation = 1;
    HashMap<Octopus, Rectangle> octopusRectangleHashMap;
    HashMap<Sprite, Rectangle> octopusLaserRectangle;
    float octopusSpawnTimer = 0f;
    private Texture octopusLaserTexture;
    private Sound octopusShot;

    //octopusOverBoss:
    OctopusOverboss overBoss


    public GameScreen(Galaga game) {
        this.game = game;
        worldWidth = game.viewport.getWorldWidth();
        worldHeight = game.viewport.getWorldHeight();
        star = new Texture(Gdx.files.internal("star.png"));
        starArray = new Array<>();
        alien = new Texture(Gdx.files.internal("alienBasic.png"));
        alienLaser = new Texture(Gdx.files.internal("alienBeam.png"));
        enemyAlienArray = new Array<>();
        enemyLaserArray = new Array<>();
        enemyLaserRec = new HashMap<>();
        enemyAlienRec = new HashMap<>();
        explosion = new Texture(Gdx.files.internal("explosion.png"));
        music = Gdx.audio.newMusic(Gdx.files.internal("gameMusic.mp3"));
        music.setLooping(true);
        music.play();
        music.setVolume(0.5f);
        explodedArray = new Array<>();
        destroySound = Gdx.audio.newSound(Gdx.files.internal("destroySound.mp3"));
        alienLaserSound = Gdx.audio.newSound(Gdx.files.internal("shootSound.mp3"));
        player = new Player(game);
        octopusArray = new Array<>();
        octopusLaserTexture = new Texture(Gdx.files.internal("octopusBeam.png"));
        octopusRectangleHashMap = new HashMap<>();
        octopusLaserRectangle = new HashMap<>();
        octopusLaserArray = new Array<>();
        octopusShot = Gdx.audio.newSound(Gdx.files.internal("octopusShot.mp3"));
    }

    @Override
    public void show() {

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
            //float speed = 4f;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.getShipSprite().translateX(-player.getShipSpeed() * delta);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                player.getShipSprite().translateX(player.getShipSpeed() * delta);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP) && coolDownTimer >= 0.75f*player.coolDownTimerCoefficient) {
                player.shoot();
                coolDownTimer = 0;
            }
        }
    }

    public void logic() {
        if (gameTimer > 5f) {
            float delta = Gdx.graphics.getDeltaTime();
            //alien creating logic:
            alienCreationTimer += delta;
            octopusSpawnTimer += delta;
            if (alienCreationTimer > 3f && enemyAlienArray.size < 5 && warlordKills<2) {
                alienCreationTimer = 0;
                createAlien();
                float chance = MathUtils.random(0, 1f);
                if (chance < 0.25f && !bossAlive && enemyAlienArray.size<=3) {
                    createAlien();
                    createAlien();
                }
            }
            //octopus creating logic:
            if(octopusSpawnTimer>2.5f && octopusArray.size < 5 && warlordKills>=2 && enemyAlienArray.size == 0){
                createOctopus();
                float chance = MathUtils.random(0,1f);
                if(chance<=0.1f){
                    createOctopus();
                }
                octopusSpawnTimer = 0;

            }

            powerUpSpawnCooldown+=delta;
            if(powerUpSpawnCooldown>=20f){
                powerUps = new PowerUps(game);
                powerUpActive = true;
                powerUpSpawnCooldown = MathUtils.random(0,10f);
            }

            if(powerUpActive){
                powerUps.activeSprite.translateY(-2f*delta);
                powerUps.powerUpRectangle.setX(powerUps.activeSprite.getX());
                powerUps.powerUpRectangle.setY(powerUps.activeSprite.getY());
                if(powerUps.powerUpRectangle.overlaps(player.getShipRec())){
                    powerUps.activatePowerUp(player);
                    player.powerUpStatus = true;
                    powerUpActive = false;
                    powerUps = null;
                }
                else if(powerUps.activeSprite.getY()<-powerUps.activeSprite.getHeight()){
                    powerUps = null;
                    powerUpActive = false;
                }
            }

            if(player.powerUpStatus && player.powerUpTimer<=10f){
                player.powerUpTimer+=delta;
                if(player.powerUpTimer>10f){
                    player.powerUpStatus = false;
                    player.normalizeShipSpeed();
                    player.coolDownTimerCoefficient = 1f;
                player.powerUpTimer = 0;}
            }




            //player movement bounds and rectangle logic:
            player.getShipSprite().setX(MathUtils.clamp(player.getShipSprite().getX(), 0, worldWidth-player.getShipSprite().getWidth()));
            player.getShipRec().set(player.getShipSprite().getX(), player.getShipSprite().getY(),player.getShipSprite().getWidth(), player.getShipSprite().getHeight());


            for (int i = enemyAlienArray.size - 1; i >= 0; i--) {
                Alien alien = enemyAlienArray.get(i);
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
                enemyAlienRec.get(alien).set(alien.getAlien().getX(), alien.getAlien().getY(), alienWidth, alienHeight);


            }

            for(int i = octopusArray.size - 1; i>=0; i--){
                Octopus octopus = octopusArray.get(i);
                float octopusWidth = octopus.getOctopusSprite().getWidth();
                float octopusHeight = octopus.getOctopusSprite().getHeight();

                octopus.addTeleportTime();
                octopus.teleportLogic();
                octopus.addShotTime();

                if(octopus.shotTimer >= 2.5f){
                    createOctopusLaser(octopus.getOctopusSprite());
                    createOctopusLaser(octopus.getOctopusSprite());
                    octopusShot.play();
                    octopus.shotTimer = 0;
                    octopus.randomizeSpeed();
                }

                octopus.getOctopusSprite().setX(MathUtils.clamp(octopus.getOctopusSprite().getX(), 0, worldWidth - octopusWidth));
                octopus.getOctopusSprite().setY(MathUtils.clamp(octopus.getOctopusSprite().getY(), worldHeight/2 + octopusHeight, worldHeight - octopusHeight));

                octopus.getOctopusSprite().translateX(octopus.octopusXSpeed*delta);
                octopus.getOctopusSprite().translateY(octopus.octopusYSpeed*delta);

                octopusRectangleHashMap.get(octopus).set(octopus.getOctopusSprite().getX(), octopus.getOctopusSprite().getY(), octopusWidth, octopusHeight);
            }


            for (int i = player.getHeroLaserArr().size - 1; i >= 0; i--) {
                Sprite heroLaser = player.getHeroLaserArr().get(i);
                float heroHeight = heroLaser.getHeight();
                float heroWidth = heroLaser.getWidth();

                heroLaser.translateY(10f * delta);
                Rectangle laserRectangle = player.getLaserRec().get(heroLaser);

                if (laserRectangle != null) {
                    laserRectangle.set(heroLaser.getX(), heroLaser.getY(), heroLaser.getWidth(), heroLaser.getHeight());
                }

                for (int j = enemyAlienArray.size - 1; j >= 0; j--) {
                    Alien alienSprite = enemyAlienArray.get(j);
                    Rectangle rectangle = enemyAlienRec.get(alienSprite);
                    if (player.getLaserRec().get(heroLaser) != null) {
                        if (player.getLaserRec().get(heroLaser).overlaps(rectangle)) {
                            destroySound.play();
                            player.getHeroLaserArr().removeIndex(i);
                            enemyAlienArray.get(j).getAlien().setTexture(explosion);
                            explodedArray.add(new ExplosionTimer(enemyAlienArray.get(j).getAlien()));
                            enemyAlienArray.removeIndex(j);
                            player.getLaserRec().remove(heroLaser);
                            score += 100;
                        }



                    }
                }

                if(alienWarlord != null){
                if(player.getLaserRec().get(heroLaser) != null){
                    if(player.getLaserRec().get(heroLaser).overlaps(alienWarlord.getRectangle())){
                        warlordHealth--;
                        alienWarlord.removeHealth();
                        player.getHeroLaserArr().removeIndex(i);
                    }
                }
            }
                for(int k = octopusArray.size - 1;k>=0;k--){
                    Octopus octopusSprite = octopusArray.get(k);
                    Rectangle rectangle = octopusRectangleHashMap.get(octopusSprite);
                    if (player.getLaserRec().get(heroLaser) != null) {
                        if (player.getLaserRec().get(heroLaser).overlaps(rectangle)) {
                            destroySound.play();
                            player.getHeroLaserArr().removeIndex(i);
                            octopusArray.get(k).getOctopusSprite().setTexture(explosion);
                            octopusArray.get(k).getOctopusSprite().setColor(Color.PURPLE);
                            octopusArray.get(k).getOctopusSprite().setSize(1f,1f);
                            explodedArray.add(new ExplosionTimer(octopusArray.get(k).getOctopusSprite()));
                            octopusArray.removeIndex(k);
                            player.getLaserRec().remove(heroLaser);
                            score += 100;
                        }



                    }
                }
            }

            for (int i = enemyLaserArray.size - 1; i >= 0; i--) {
                Sprite alienLaserSprite = enemyLaserArray.get(i);

                alienLaserSprite.translateY(-6f * delta);

                Rectangle laserRectangle = enemyLaserRec.get(alienLaserSprite);
                if (laserRectangle != null) {
                    laserRectangle.set(alienLaserSprite.getX(), alienLaserSprite.getY(), alienLaserSprite.getWidth(), alienLaserSprite.getHeight());
                }

                if (laserRectangle != null) {
                    if (laserRectangle.overlaps(player.getShipRec())) {
                        enemyLaserArray.removeIndex(i);
                        player.setHealth(player.getHealth()-1);
                    }
                }
            }
            for(int i = octopusLaserArray.size - 1; i>=0; i--){
                Sprite octopusLaserSprite = octopusLaserArray.get(i);
                float speed = -6f;
                if(octopusLaserSprite.getRotation()>0){
                octopusLaserSprite.translateY(speed*delta);
                octopusLaserSprite.translateX(-(speed/4)*delta);
                }
                else if(octopusLaserSprite.getRotation()<0){
                    octopusLaserSprite.translateY(speed*delta);
                    octopusLaserSprite.translateX((speed/4)*delta);
                }
                System.out.println("position is: " + octopusLaserSprite.getX() + " Y: " + octopusLaserSprite.getY());

                Rectangle octopusLaserRectangle = this.octopusLaserRectangle.get(octopusLaserSprite);
                if(octopusLaserRectangle != null){
                    octopusLaserRectangle.set(octopusLaserSprite.getX(), octopusLaserSprite.getY(), octopusLaserSprite.getWidth(), octopusLaserSprite.getHeight());
                }
                if(octopusLaserRectangle != null){
                    if(octopusLaserRectangle.overlaps(player.getShipRec())){
                        octopusLaserArray.removeIndex(i);
                        player.setHealth(player.getHealth()-1);
                    }
                    else if(octopusLaserRectangle.getX()< 0 - octopusLaserRectangle.getWidth() || octopusLaserRectangle.getX()>worldWidth+octopusLaserRectangle.getWidth() || octopusLaserRectangle.getY() < -octopusLaserRectangle.getHeight()){
                        octopusLaserArray.removeIndex(i);
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
        if(score % 2000 == 0 && score > 0 && !bossAlive && warlordKills<2){
            alienWarlord = new AlienWarlord(this.game);
            warlordHealth = 5;
        }
        if (alienWarlord != null) {
            if(warlordHealth<=0){
                    alienWarlord.getSprite().setTexture(explosion);
                    destroySound.play();
                    score+=300;
                    explodedArray.add(new ExplosionTimer(alienWarlord.getSprite()));
                    bossAlive = false;
                alienWarlord = null;
                warlordKills++;
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
                createAlienWarlordLaser(alienWarlord.getSprite());
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
                starSprite.translateY(-5f * delta);

                if (starSprite.getY() < -starHeight) {
                    starArray.removeIndex(i);
                }
            }
            starTimer += delta;
            if (starTimer > 0.25f) {
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
            for (int i = player.getHealth() - 1; i >= 0; i--) {
                player.getHealthArray().get(i).draw(game.batch);
            }
            if(powerUps!=null){
                powerUps.activeSprite.draw(game.batch);
            }
            game.score.draw(game.batch, "[YELLOW]S[][RED]C[][BLUE]O[][GREEN]R[][YELLOW]E[] " + score, 0, worldHeight - 1.3f);
            for (Sprite heroLaser : player.getHeroLaserArr()) {
                heroLaser.draw(game.batch);
            }
            for (Sprite aliensLaser : enemyLaserArray) {
                aliensLaser.draw(game.batch);
            }
            for(Sprite octopusLasers: octopusLaserArray){
                octopusLasers.draw(game.batch);
            }


            player.getShipSprite().draw(game.batch);


            if (score >= 2000 && warlordHealth>0) {
                game.warlordHealthInfo.draw(game.batch,"ALIEN WARLORD", 2f, worldHeight-0.3f);
                for(Sprite health:alienWarlord.getHealthArray()){
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
            for (Alien aliens : enemyAlienArray) {
                aliens.getAlien().draw(game.batch);
            }
            for(Octopus octopus: octopusArray){
                if(octopus.isVisibility()){
                    octopus.getOctopusSprite().draw(game.batch);}

            }
            if (player.getHealth() <= 0) {
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
    //this.heroLaser.dispose();
    this.explosion.dispose();
    this.music.dispose();
    //this.ship.dispose();
    this.star.dispose();
    }

//    public void shoot() {
//        Sprite heroLaserSprite = new Sprite(heroLaser);
//        Rectangle heroRectangle = new Rectangle();
//
//        heroLaserSprite.setSize(1, 0.5f);
//        heroLaserSprite.setX(shipSprite.getX() + 0.05f);
//        heroLaserSprite.setY(shipSprite.getHeight());
//
//        heroRectangle.set(heroLaserSprite.getX(), heroLaserSprite.getY(), heroLaserSprite.getWidth(), heroLaserSprite.getHeight());
//
//        laserRec.put(heroLaserSprite, heroRectangle);
//        heroLaserArr.add(heroLaserSprite);
//    }

    public void createStars() {
        float starWidth = MathUtils.random(0.1f, 0.2f);
        float starHeight = MathUtils.random(0.1f, 0.2f);
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        Sprite starSprite = new Sprite(star);
        starSprite.setSize(starWidth, starHeight);
        starSprite.setX(MathUtils.random(0f, worldWidth - starWidth));
        starSprite.setY(worldHeight);
        starSprite.setAlpha(0.5f);
        starArray.add(starSprite);
    }

    public void createAlien() {
        Alien alienSprite = new Alien(this.game);

        enemyAlienRec.put(alienSprite, alienSprite.getRectangle());
        enemyAlienArray.add(alienSprite);
    }

    public void createAlienLaser(Sprite alien) {
        Sprite alienLaserSprite = new Sprite(alienLaser);
        Rectangle laserRectangle = new Rectangle();

        alienLaserSprite.setSize(0.5f, 0.25f);
        alienLaserSprite.setX(alien.getX());
        alienLaserSprite.setY(alien.getY() - alien.getHeight());
        laserRectangle.set(alienLaserSprite.getX(), alienLaserSprite.getY(), alienLaserSprite.getWidth(), alienLaserSprite.getHeight());

        enemyLaserRec.put(alienLaserSprite, laserRectangle);
        enemyLaserArray.add(alienLaserSprite);
    }
    public void createAlienWarlordLaser(Sprite alien){
        Sprite alienLaserSprite = new Sprite(alienLaser);
        Rectangle laserRectangle = new Rectangle();

        alienLaserSprite.setSize(1f, 0.5f);
        alienLaserSprite.setX(alien.getX());
        alienLaserSprite.setY(alien.getY() - alien.getHeight());
        laserRectangle.set(alienLaserSprite.getX(), alienLaserSprite.getY(), alienLaserSprite.getWidth(), alienLaserSprite.getHeight());

        enemyLaserRec.put(alienLaserSprite, laserRectangle);
        enemyLaserArray.add(alienLaserSprite);
    }

    public void createOctopus(){
        Octopus octopus = new Octopus(game);
        octopusRectangleHashMap.put(octopus, octopus.getOctopusHitBox());
        octopusArray.add(octopus);
    }
    public void createOctopusLaser(Sprite octopus){
        Sprite octopusLaser = new Sprite(octopusLaserTexture);
        octopusLaser.setSize(0.4f,0.4f);
        octopusLaser.setX(octopus.getX());
        octopusLaser.setY(octopus.getY()-octopus.getHeight());
        octopusLaser.setRotation(octopusLaserRotation*0.0001f);
        octopusLaserRotation*=-1;
        Rectangle rectangle = new Rectangle(octopusLaser.getX(), octopusLaser.getY(), octopusLaser.getWidth(), octopusLaser.getHeight());

        octopusLaserRectangle.put(octopusLaser, rectangle);
        octopusLaserArray.add(octopusLaser);
    }

    public void createOverBossLaser(Sprite sprite){
        Sprite overBossLaser = new Sprite(new Texture(Gdx.files.internal("octopusBeam.png")));

        overBossLaser.setSize(0.5f,0.5f);
        overBossLaser.setX(sprite.getX());
        overBossLaser.setY(sprite.getY()-sprite.getHeight());
        overBossLaser.setRotation(0.00001f*overlordLaserRotation);
        overlordLaserRotation*=-1;
        Rectangle rectangle = new Rectangle(overBossLaser.getX(), overBossLaser.getY(), overBossLaser.getWidth(), overBossLaser.getHeight());

        octopusLaserRectangle.put(overBossLaser, rectangle);
        octopusLaserArray.add(overBossLaser);
    }

}
