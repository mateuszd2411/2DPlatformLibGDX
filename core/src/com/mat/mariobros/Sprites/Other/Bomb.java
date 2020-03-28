package com.mat.mariobros.Sprites.Other;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
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
    //    Array<TextureRegion> frames;
//    Animation fireAnimation;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;

    Body b2body;
    public Bomb(PlayScreen screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();
//        frames = new Array<TextureRegion>();

        setRegion(screen.getAtlas().findRegion("fireball"),0,0,8,8);

//        for(int i = 0; i < 1; i++){      //////4
//            frames.add(new TextureRegion(screen.getAtlas().findRegion("fireball"), i * 8, 0, 8, 8));
//        }
//        fireAnimation = new Animation(0.1f, frames);    ////0.3
//        setRegion((TextureRegion) fireAnimation.getKeyFrame(0));
        setBounds(x, y, 10 / MarioBros.PPM, 10 / MarioBros.PPM);      ////size
        defineBomb();
    }

    public void defineBomb(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() - 18 /MarioBros.PPM : getX() - 10 /MarioBros.PPM, getY());
        bdef.type = BodyDef.BodyType.StaticBody;          /////static for bombs and change time destroy
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / MarioBros.PPM); //////////////////radi

        fdef.filter.categoryBits = MarioBros.BOMB_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT;

        fdef.shape = shape;
        fdef.restitution = 0 ;    ////belching
        fdef.friction = 0;      ////friction  (bomb  === high)
        b2body.createFixture(fdef).setUserData(this);
//        b2body.setLinearVelocity(new Vector2(fireRight ? 2.5f : -2.5f, .1f));  /// 2  -2   .1f



    }

    public void update(float dt){  //dt
        stateTime += dt;
//        setRegion((TextureRegion) fireAnimation.getKeyFrame(stateTime, true));
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