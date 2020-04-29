package com.mat.mainGame.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mat.mainGame.MainGame;
import com.mat.mainGame.Screens.PlayScreen;
import com.mat.mainGame.Sprites.Player;

public class Bonus extends Item {
    public Bonus(PlayScreen screen, float x, float y) {
        super(screen, x, y);
            setRegion(screen.getAtlas().findRegion("bonus"),0,0,16,16);
        velocity = new Vector2(0.7f,0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / MainGame.PPM);
        fdef.filter.categoryBits = MainGame.ITEM_BIT;
        fdef.filter.maskBits = MainGame.PLAYER_BIT |
                MainGame.OBJECT_BIT |
                MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Player player) {
        destroy();
        player.grow();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
