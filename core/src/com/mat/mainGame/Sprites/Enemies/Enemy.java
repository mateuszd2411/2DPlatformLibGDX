package com.mat.mainGame.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mat.mainGame.Screens.PlayScreen;
import com.mat.mainGame.Sprites.Player;
import com.mat.mainGame.Sprites.Other.Bomb;
import com.mat.mainGame.Sprites.Other.FireBall;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(-1.0f,-2);
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnHead(Player mario);
    public abstract void hitByEnemy(Enemy enemy);
    public abstract void flamed(FireBall fireball);
    public abstract void flamed(Bomb bomb);

    public void reverseVelocity(boolean x, boolean y){
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }
}
