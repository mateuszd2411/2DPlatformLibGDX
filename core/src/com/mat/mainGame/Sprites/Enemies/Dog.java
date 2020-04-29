package com.mat.mainGame.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mat.mainGame.MainGame;
import com.mat.mainGame.Scenes.Hud;
import com.mat.mainGame.Screens.PlayScreen;
import com.mat.mainGame.Sprites.Player;
import com.mat.mainGame.Sprites.Other.Bomb;
import com.mat.mainGame.Sprites.Other.FireBall;

public class Dog extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private  boolean destroyedDog;

    public Dog(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("dog"), i * 22,0,22,18));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16/ MainGame.PPM,16 / MainGame.PPM);
        setToDestroy = false;
        destroyedDog = false;
    }

    public void update(float dt){
        stateTime += dt;
        if (setToDestroy && !destroyedDog){
            world.destroyBody(b2body);
            destroyedDog = true;
            MainGame.manager.get("audio/sounds/dog.wav", Sound.class).play();
            setRegion(new TextureRegion(screen.getAtlas().findRegion("dog"), 44,0,22,18));
            stateTime = 0;

            Hud.score = Hud.score -13;
            Hud.scoreLabel.setText(String.format("%06d",Hud.score  ));
        }
        else if (!destroyedDog) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3/ MainGame.PPM);
        fdef.filter.categoryBits = MainGame.ENEMY_BIT;
        fdef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT |
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT|
                MainGame.PLAYER_BIT |
                MainGame.FIREBALL_BIT|
                MainGame.BOMB_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];

        vertice[0] = new Vector2(-5,8).scl(1 / MainGame.PPM);
        vertice[1] = new Vector2(5,8).scl(1 / MainGame.PPM);
        vertice[2] = new Vector2(-3,3).scl(1 / MainGame.PPM);
        vertice[3] = new Vector2(3,3).scl(1 / MainGame.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = MainGame.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if (!destroyedDog || stateTime <1)
            super.draw(batch);
    }

    public void hitByEnemy(Enemy enemy){
        if (enemy instanceof YellowMonster && ((YellowMonster) enemy).currentState == YellowMonster.State.MOVING_SHELL)
            setToDestroy = true;
        else
            reverseVelocity(true, false);
    }

    @Override
    public void flamed(FireBall fireball) {
        setToDestroy = true;
        Hud.addScore(100);
        fireball.setToDestroy();
    }

    @Override
    public void flamed(Bomb bomb) {
        setToDestroy = true;
        Hud.addScore(100);
        bomb.setToDestroy();
    }

    @Override
    public void hitOnHead(Player mario) {
        setToDestroy = true;
        MainGame.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }
}
