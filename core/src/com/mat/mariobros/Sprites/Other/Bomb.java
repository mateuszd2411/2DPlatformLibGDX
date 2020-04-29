package com.mat.mariobros.Sprites.Other;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mat.mariobros.MarioBros;
import com.mat.mariobros.Screens.PlayScreen;

public  class Bomb extends Sprite {

    PlayScreen screen;
    World world;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;

    Body b2body;
    public Bomb(PlayScreen screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();

        setRegion(screen.getAtlas().findRegion("fireball"),0,0,8,8);
        setBounds(x, y, 10 / MarioBros.PPM, 10 / MarioBros.PPM);
        defineBomb();
    }

    public void defineBomb(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() - 18 /MarioBros.PPM : getX() - 10 /MarioBros.PPM, getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / MarioBros.PPM);

        fdef.filter.categoryBits = MarioBros.BOMB_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT;

        fdef.shape = shape;
        fdef.restitution = 0 ;    ////belching
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt){  //dt
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 6 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2.15f);
        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        MarioBros.manager.get("audio/sounds/bomb.wav", Sound.class).play();
        return destroyed;
    }
}