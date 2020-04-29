package com.mat.mariobros.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mat.mariobros.MarioBros;
import com.mat.mariobros.Screens.PlayScreen;
import com.mat.mariobros.Sprites.Mario;
import com.mat.mariobros.Sprites.Other.Bomb;
import com.mat.mariobros.Sprites.Other.FireBall;

public class MonsterMouths extends Enemy {
    private float stateTime;
    private Animation fireAnimation;
    private Array<TextureRegion> frames;

    public MonsterMouths(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("mouth"), i * 25,0,25,26));
        fireAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 80/ MarioBros.PPM,50 / MarioBros.PPM);
    }

    public void update(float dt){
        stateTime += dt;
        b2body.setLinearVelocity(velocity);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) fireAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(37/ MarioBros.PPM);

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

    }
}
