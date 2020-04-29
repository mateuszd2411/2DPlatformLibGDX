package com.mat.mainGame.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mat.mainGame.MainGame;
import com.mat.mainGame.Sprites.Enemies.Enemy;
import com.mat.mainGame.Sprites.Items.Item;
import com.mat.mainGame.Sprites.Player;
import com.mat.mainGame.Sprites.Other.Bomb;
import com.mat.mainGame.Sprites.Other.FireBall;
import com.mat.mainGame.Sprites.TitleObjects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case  MainGame.PLAYER_HEAD_BIT | MainGame.BRICK_BIT:
            case  MainGame.PLAYER_HEAD_BIT | MainGame.COIN_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.PLAYER_HEAD_BIT)
                    ((InteractiveTileObject)fixB.getUserData()).onHeadHit((Player) fixA.getUserData());
                else
                    ((InteractiveTileObject)fixA.getUserData()).onHeadHit((Player) fixB.getUserData());
                break;

            case MainGame.ENEMY_HEAD_BIT | MainGame.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead((Player) fixB.getUserData());
                else
                    ((Enemy)fixB.getUserData()).hitOnHead((Player) fixA.getUserData());
                break;

            case MainGame.ENEMY_BIT | MainGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;

            case MainGame.PLAYER_BIT | MainGame.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.PLAYER_BIT)
                    ((Player) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((Player) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                break;

            case MainGame.ENEMY_BIT | MainGame.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
                break;

            case MainGame.ITEM_BIT | MainGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true,false);
                break;

            case MainGame.ITEM_BIT | MainGame.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Player) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Player) fixA.getUserData());
                break;
            ///fire
            case MainGame.FIREBALL_BIT | MainGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MainGame.FIREBALL_BIT)
                    ((FireBall)fixA.getUserData()).setToDestroy();
                else
                    ((FireBall)fixB.getUserData()).setToDestroy();
                break;

            case MainGame.FIREBALL_BIT | MainGame.ENEMY_BIT:
            case MainGame.FIREBALL_BIT | MainGame.ENEMY_HEAD_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.FIREBALL_BIT)
                    ((Enemy) fixB.getUserData()).flamed((FireBall) fixA.getUserData());
                else
                    ((Enemy) fixA.getUserData()).flamed((FireBall) fixB.getUserData());
                break;
            ///fire

            ///ball
            case MainGame.BOMB_BIT | MainGame.ENEMY_BIT:
            case MainGame.BOMB_BIT | MainGame.ENEMY_HEAD_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.BOMB_BIT)
                    ((Enemy) fixB.getUserData()).flamed((Bomb) fixA.getUserData());
                else
                    ((Enemy) fixA.getUserData()).flamed((Bomb) fixB.getUserData());
            ///ball
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
