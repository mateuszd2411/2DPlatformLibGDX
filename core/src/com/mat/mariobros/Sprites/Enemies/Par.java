package com.mat.mariobros.Sprites.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mat.mariobros.MarioBros;
import com.mat.mariobros.Scenes.Hud;
import com.mat.mariobros.Screens.PlayScreen;
import com.mat.mariobros.Sprites.Mario;
import com.mat.mariobros.Sprites.Other.Bomb;
import com.mat.mariobros.Sprites.Other.FireBall;

public class Par extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;


    public Par(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 7; i++)
//            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16,0,16,16));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("par"), i * 60,0,60,60));
        walkAnimation = new Animation(0.2f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 120/ MarioBros.PPM,120 / MarioBros.PPM);
        setToDestroy = false;
        destroyed = false;
//        angle = 0;

    }

    public void update(float dt){
        stateTime += dt;
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("par"), 180,0,60,60));
            stateTime = 0;
            MarioBros.manager.get("audio/sounds/yeah.mp3", Sound.class).play();
        }
        else if (!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50/ MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT|
                MarioBros.MARIO_BIT|
                MarioBros.FIREBALL_BIT|
                MarioBros.BOMB_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5,8).scl(7 / MarioBros.PPM);
        vertice[1] = new Vector2(5,8).scl(7 / MarioBros.PPM);
        vertice[2] = new Vector2(-3,3).scl(7 / MarioBros.PPM);
        vertice[3] = new Vector2(3,3).scl(7 / MarioBros.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);


    }

    public void draw(Batch batch){
        if (!destroyed || stateTime <5)
            super.draw(batch);

    }

    public void hitByEnemy(Enemy enemy){
        System.out.println(MathUtils.random(100));
        if (MathUtils.random(100) > 5){

                setToDestroy = true;

        }
        else
            reverseVelocity(true, false);

    }

    @Override
    public void flamed(FireBall fireball) {
        System.out.println(MathUtils.random(100));
        if (MathUtils.random(100) < 10){
            setToDestroy = true;
            Hud.addScore(10);
        }

        fireball.setToDestroy();
    }

    @Override
    public void flamed(Bomb bomb) {
        setToDestroy = true;
        Hud.addScore(10);
        bomb.setToDestroy();
    }

    @Override
    public void hitOnHead(Mario mario) {
        System.out.println(MathUtils.random(100));
        if (MathUtils.random(100) < 5){
            setToDestroy = true;
            MarioBros.manager.get("audio/sounds/par.mp3", Sound.class).play();
        }


    }
}