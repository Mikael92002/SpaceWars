package com.badlogic.Galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class OctopusOverboss {
    final Galaga game;
    private Texture overBossTexture;
    Sprite overBossSprite;
    float shotTimer = 0;
    private float delta = Gdx.graphics.getDeltaTime();
    float moveTimer = 0;
    private float health;
    float teleportTimer = 0f;
    private float teleportFlickerInterval = 0.10f;
    private float teleportFlicker = 0f;
    private boolean visibility = true;
    Rectangle rectangle;
    private Texture healthOne;
    private Texture healthTwo;
    private Texture healthThree;
    private Texture healthFour;
    private Texture healthFive;
    private Texture healthSix;
    Array<Sprite> healthArray;
    float overBossXSpeed = 0;
    float overBossYSpeed = -4f;

    public OctopusOverboss(Galaga game) {
        this.game = game;
        overBossTexture = new Texture(Gdx.files.internal("octopusoverboss.png"));
        overBossSprite = new Sprite(overBossTexture);
        overBossSprite.setX(game.viewport.getWorldWidth() / 2 - overBossSprite.getWidth());
        overBossSprite.setY(game.viewport.getWorldHeight());
        overBossSprite.setSize(1f, 1f);
        overBossSprite.setScale(2f);
        rectangle = new Rectangle(overBossSprite.getX(), overBossSprite.getY(), overBossSprite.getWidth(), overBossSprite.getHeight());
        overBossSprite.setOriginCenter();
        healthOne = new Texture(Gdx.files.internal("octHealthOne.png"));
        healthTwo = new Texture(Gdx.files.internal("octHealthTwo.png"));
        healthThree = new Texture(Gdx.files.internal("octHealthThree.png"));
        healthFour = new Texture(Gdx.files.internal("octHealthFour.png"));
        healthFive = new Texture(Gdx.files.internal("octHealthFive.png"));
        healthSix = new Texture(Gdx.files.internal("octHealthSix.png"));
        healthArray = new Array<>();
        healthArray.add(new Sprite(healthOne));
        healthArray.add(new Sprite(healthTwo));
        healthArray.add(new Sprite(healthThree));
        healthArray.add(new Sprite(healthFour));
        healthArray.add(new Sprite(healthFive));
        healthArray.add(new Sprite(healthSix));
        float xPos = 2.25f;
        float yPos = game.viewport.getWorldHeight() - 1f;
        for (Sprite arraySprite : healthArray) {
            arraySprite.setSize(0.25f, 0.25f);
            arraySprite.setX(xPos);
            arraySprite.setY(yPos);
            xPos += 0.25f;
        }
    }

    public void addShotTime() {
        shotTimer += delta;
    }

    public void addTeleportTime() {
        teleportTimer += delta;
    }

    public void teleportLogic() {
        if (teleportTimer >= 2.25f) {
            teleportFlicker += delta;

            if (teleportFlicker >= teleportFlickerInterval) {
                teleportFlicker = 0;
                visibility = !visibility;
            }
        }
        if (teleportTimer >= 3f) {
            overBossSprite.setX(MathUtils.random(0, game.viewport.getWorldWidth() - overBossSprite.getWidth()));
            overBossSprite.setY(MathUtils.random(game.viewport.getWorldHeight() / 2 - overBossSprite.getHeight(), game.viewport.getWorldHeight() - overBossSprite.getHeight()));
            visibility = true;
            teleportTimer = 0;
        }
    }

    public Sprite getOverBossSprite() {
        return overBossSprite;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void randomizeSpeed() {
        overBossXSpeed = MathUtils.random(-2f, 2f);
        overBossYSpeed = MathUtils.random(-2f, 2f);
    }

    public void removeHealthFromArray() {
        if (healthArray.size > 0) {
            healthArray.removeIndex(healthArray.size - 1);
        }
        System.out.println("overboss Health: " + healthArray.size);
    }

}
