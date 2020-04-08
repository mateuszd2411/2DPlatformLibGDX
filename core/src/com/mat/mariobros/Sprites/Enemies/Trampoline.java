package com.mat.mariobros.Sprites.Enemies;

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
import com.mat.mariobros.MarioBros;
import com.mat.mariobros.Scenes.Hud;
import com.mat.mariobros.Screens.PlayScreen;
import com.mat.mariobros.Sprites.Mario;
import com.mat.mariobros.Sprites.Other.Bomb;
import com.mat.mariobros.Sprites.Other.FireBall;

public class Trampoline extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;



    public Trampoline(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("trampoline"), i * 33,0,33,11));
        walkAnimation = new Animation(0.5f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 70/ MarioBros.PPM,16 / MarioBros.PPM);


    }

    public void update(float dt){
        stateTime += dt;
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));

    }

    @Override
    protected void defineEnemy() {

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(0.1f/ MarioBros.PPM);              //////////radio
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

        vertice[0] = new Vector2(-20,8).scl(1 / MarioBros.PPM);
        vertice[1] = new Vector2(20,8).scl(1 / MarioBros.PPM);
        vertice[2] = new Vector2(-20,3).scl(1 / MarioBros.PPM);
        vertice[3] = new Vector2(20,3).scl(1 / MarioBros.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 1.2f;
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);


    }

    public void draw(Batch batch){

            super.draw(batch);

    }

    public void hitByEnemy(Enemy enemy){

    }

    @Override
    public void flamed(FireBall fireball) {


    }

    @Override
    public void flamed(Bomb bomb) {

    }

    @Override
    public void hitOnHead(Mario mario) {


        MarioBros.manager.get("audio/sounds/stomp.wav", Sound.class).play();

    }
}
