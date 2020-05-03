package com.mat.mainGame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mat.mainGame.MainGame;
import com.mat.mainGame.Screens.PlayScreen;
import com.mat.mainGame.Sprites.Enemies.Enemy;
import com.mat.mainGame.Sprites.Enemies.FlyingObject;
import com.mat.mainGame.Sprites.Enemies.YellowMonster;
import com.mat.mainGame.Sprites.Other.Bomb;
import com.mat.mainGame.Sprites.Other.FireBall;

public class Player extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD }
    public State currentState;
    public State previousState;

    public static World world;
    public static Body b2body;

    private TextureRegion playerStand;
    private Animation playerRun;
    private TextureRegion playerJump;
    private TextureRegion playerDead;
    private TextureRegion bigPlayerStand;
    private TextureRegion bigPlayerJump;
    private Animation bigPlayerRun;
    private Animation growPlayer;

    private float stateTimer;
    private boolean runningRight;
    public static boolean playerIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigPlayer;
    private boolean timeToRedefinePlayer;
    private   boolean playerIsDead;
    private PlayScreen screen;

    private Array<FireBall> fireballs;
    private Array<Bomb> bombs;

    public static BodyDef bdef;

    public Player(PlayScreen screen){
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //get run animation frames and add them to playerRun Animation
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_player"), i * 16, 0, 16, 16));
        playerRun = new Animation(0.1f, frames);

        frames.clear();

        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_player"), i * 16, 0, 16, 16));
        bigPlayerRun = new Animation(0.1f, frames);
        frames.clear();

        //get set animation frames from growing mario
        frames.add(new TextureRegion(screen.getAtlas().findRegion("little_player"), 80, 0, 16, 16));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("little_player"), 80, 0, 16, 16));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("little_player"), 80, 0, 16, 16));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("little_player"), 80, 0, 16, 16));
        growPlayer = new Animation(0.2f, frames);

        //get jump animation frames and add them to playerJump Animation
        playerJump = new TextureRegion(screen.getAtlas().findRegion("little_player"), 80, 0, 16, 16);
        bigPlayerJump = new TextureRegion(screen.getAtlas().findRegion("little_player"), 80, 0, 16, 16);

        //create texture region for mario standing
        playerStand = new TextureRegion(screen.getAtlas().findRegion("little_player"), 0, 0, 16, 16);
        bigPlayerStand = new TextureRegion(screen.getAtlas().findRegion("little_player"), 0, 0, 16, 16);

        //create dead mario texture region
        playerDead = new TextureRegion(screen.getAtlas().findRegion("little_player"), 96, 0, 16, 16);

        //define player in Box2d
        definePlayer();

        //set initial values for player location, width and height. And initial frame as playerStand.
        setBounds(0, 0, 16 / MainGame.PPM, 16 / MainGame.PPM);
        setRegion(playerStand);

        fireballs = new Array<FireBall>();
        //bomb
        bombs = new Array<Bomb>();
        //bomb
    }

    public void update(float dt){

        // time is up : too late player dies T_T
        // the !isDead() method is used to prevent multiple invocation
        // of "die music" and jumping
        if (screen.getHud().isTimeUp() && !isDead()) {
            die();
        }

        //update our sprite to correspond with the position of our Box2D body
        if(playerIsBig)
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        else
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //update sprite with the correct frame depending on player current action
        setRegion(getFrame(dt));
        if(timeToDefineBigPlayer)
            defineBigPlayer();
        if(timeToRedefinePlayer)
            redefinePlayer();

        for(FireBall  ball : fireballs) {
            ball.update(dt);
            if(ball.isDestroyed())
                fireballs.removeValue(ball, true);
        }

        ///bomb
        for(Bomb bomb : bombs) {
            bomb.update(dt);
            if(bomb.isDestroyed())
                bombs.removeValue(bomb, true);
        }
        ///bomb

    }

    public TextureRegion getFrame(float dt){
        //get player current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch(currentState){
            case DEAD:
                region = playerDead;
                break;
            case GROWING:
                region = (TextureRegion) growPlayer.getKeyFrame(stateTimer);
                if(growPlayer.isAnimationFinished(stateTimer)) {
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = playerIsBig ? bigPlayerJump : playerJump;
                break;
            case RUNNING:
                region = (TextureRegion) (playerIsBig ? bigPlayerRun.getKeyFrame(stateTimer, true) : playerRun.getKeyFrame(stateTimer, true));
                break;
            case FALLING:
            case STANDING:
            default:
                region = playerIsBig ? bigPlayerStand : playerStand;
                break;
        }

        //if player is running left and the texture isnt facing left... flip it.
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        //if player is running right and the texture isnt facing right... flip it.
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;
    }

    public State getState(){
        //Test to Box2D for velocity on the X and Y-Axis
        //if player is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if(playerIsDead)
            return State.DEAD;
        else if(runGrowAnimation)
            return State.GROWING;
        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            int randomNumber = MathUtils.random(1,50);
            System.out.println(randomNumber);
            switch (randomNumber){
                case 1:
                    MainGame.manager.get("audio/sounds/mee.mp3", Music.class).play();
                    System.out.println("1");
                    break;

                    default:
                        break;

            }
            return State.JUMPING;
        }
            //if negative in Y-Axis mario is falling
        else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        //if player is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0){

            return State.RUNNING;
        }
            //if none of these return then he must be standing
        else
            return State.STANDING;
    }

    public void grow(){
        if( !isBig() ) {
            runGrowAnimation = true;
            playerIsBig = true;
            timeToDefineBigPlayer = true;
            setBounds(getX(), getY(), getWidth()*1.3f, getHeight()*1.5f);  /////////getHeight() * 2);
            MainGame.manager.get("audio/sounds/powerup.wav", Sound.class).play();
        }
    }

    public  void die() {
        if (!isDead()) {

            MainGame.manager.get("audio/music/music.mp3", Music.class).stop();
            MainGame.manager.get("audio/sounds/playerdie.wav", Sound.class).play();
            playerIsDead = true;
            Filter filter = new Filter();

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            b2body.applyLinearImpulse(new Vector2(3, 3f), b2body.getWorldCenter(), true);          ///die jump
        }
    }

    public  boolean isDead(){
        return playerIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public boolean isBig(){
        return playerIsBig;
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            if (Player.playerIsBig){
                b2body.applyLinearImpulse(new Vector2(0.2f, 4.5f), b2body.getWorldCenter(), true);
                currentState = State.JUMPING;
            }else
            b2body.applyLinearImpulse(new Vector2(0.2f, 3.5f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void hit(Enemy enemy){
        if(enemy instanceof YellowMonster && ((YellowMonster) enemy).currentState == YellowMonster.State.STANDING_SHELL)
            ((YellowMonster) enemy).kick(enemy.b2body.getPosition().x > b2body.getPosition().x ? YellowMonster.KICK_RIGHT : YellowMonster.KICK_LEFT);
        else {
            if (playerIsBig) {
                playerIsBig = false;
                timeToRedefinePlayer = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                MainGame.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                if (FlyingObject.touch = true){
                    die();
                }
            }
        }
    }

    public void redefinePlayer(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MainGame.PPM);
        fdef.filter.categoryBits = MainGame.PLAYER_BIT;
        fdef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT |
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT |
                MainGame.ENEMY_HEAD_BIT |
                MainGame.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MainGame.PPM, 6 / MainGame.PPM), new Vector2(2 / MainGame.PPM, 6 / MainGame.PPM));
        fdef.filter.categoryBits = MainGame.PLAYER_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToRedefinePlayer = false;
    }

    public void defineBigPlayer(){
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(40, 32 / MainGame.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MainGame.PPM);
        fdef.filter.categoryBits = MainGame.PLAYER_BIT;
        fdef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT |
                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT |
                MainGame.ENEMY_HEAD_BIT |
                MainGame.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, 0 / MainGame.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MainGame.PPM, 6 / MainGame.PPM), new Vector2(2 / MainGame.PPM, 6 / MainGame.PPM));
        fdef.filter.categoryBits = MainGame.PLAYER_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigPlayer = false;
    }

    public void definePlayer(){
        bdef = new BodyDef();

        Gdx.gl.glClearColor(.0f, 0.0f, 0.f, 1.0f);
        bdef.position.set(40 / MainGame.PPM, 40 / MainGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MainGame.PPM);
        fdef.filter.categoryBits = MainGame.PLAYER_BIT;
        fdef.filter.maskBits = MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT |
//                MainGame.ENEMY_BIT |
                MainGame.OBJECT_BIT |
                MainGame.ENEMY_HEAD_BIT |
                MainGame.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MainGame.PPM, 6 / MainGame.PPM), new Vector2(2 / MainGame.PPM, 6 / MainGame.PPM));
        fdef.filter.categoryBits = MainGame.PLAYER_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void fire(){
        fireballs.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
    }

    //add in the future
    public void fireG(){
        fireballs.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
    }

    //bomb
    public void putBomb(){
        bombs.add(new Bomb(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
    }
    //bomb

    public void draw(Batch batch){
        super.draw(batch);
        for(FireBall ball : fireballs)
            ball.draw(batch);
        ///bomb
        for(Bomb bomb : bombs)
            bomb.draw(batch);
        ///bomb
    }
}