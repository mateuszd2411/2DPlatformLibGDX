package com.mat.mainGame.Sprites.Other;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mat.mainGame.MainGame;
import com.mat.mainGame.Screens.PlayScreen;
import com.mat.mainGame.Sprites.Player;


public  class FireBall extends Sprite {
    PlayScreen screen;
    World world;
    Animation fireAnimation;
    float stateTime;
    public  boolean destroyed;
    public   boolean setToDestroy;
    boolean fireRight;

    Body b2body;
    public FireBall(PlayScreen screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();

        setRegion(screen.getAtlas().findRegion("fireball"),8,0,8,8);
        setBounds(x, y, 3 / MainGame.PPM, 3 / MainGame.PPM);      ////size
        defineFireBall();
    }

    public void defineFireBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12 / MainGame.PPM : getX() - 12 / MainGame.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / MainGame.PPM);

        fdef.filter.categoryBits = MainGame.FIREBALL_BIT;
        fdef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT |
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT;

        fdef.shape = shape;
        fdef.restitution = 0 ;    ////belching
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        if (Player.playerIsBig){
            b2body.setLinearVelocity(new Vector2(fireRight ? 15.5f : -15.5f, 0.5f));  /// 2  -2   .1f     3.5f : -3.5f, .1f));
        }else
        b2body.setLinearVelocity(new Vector2(fireRight ? 2.5f : -2.5f, 0.2f));  /// 2  -2   .1f     3.5f : -3.5f, .1f));
    }

    public void update(float dt){  //dt
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 3 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2.15f);
        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public  void setToDestroy(){
        setToDestroy = true;
    }

    public  boolean isDestroyed(){
        return destroyed;
    }
}