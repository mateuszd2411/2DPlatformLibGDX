package com.mat.mainGame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mat.mainGame.MainGame;
import com.mat.mainGame.Scenes.Hud;
import com.mat.mainGame.Sprites.Enemies.Enemy;
import com.mat.mainGame.Sprites.Items.Bonus;
import com.mat.mainGame.Sprites.Items.Item;
import com.mat.mainGame.Sprites.Items.ItemDef;
import com.mat.mainGame.Sprites.Player;
import com.mat.mainGame.Tools.B2WorldCreator;
import com.mat.mainGame.Tools.WorldContactListener;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen
    extends Stage
        implements Screen {
    public static boolean alreadyDestroyed = false;
    private TextureAtlas atlas = new TextureAtlas("Player_and_Enemies.pack");
//    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;
    private MainGame game;
    private Viewport gamePort;
    private OrthographicCamera gamecam;
    private Hud hud;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    private TiledMap map;
    private TmxMapLoader maploader;
    private Music music;
    private Music boosMusic;
    private Player player;
    private OrthogonalTiledMapRenderer renderer;
    private World world;

    public PlayScreen(MainGame mainGame) {
        this.game = mainGame;
        this.gamecam = new OrthographicCamera();
        this.gamePort = new FitViewport(4.0f, 2.08f, (Camera)this.gamecam);
        this.hud = new Hud(MainGame.batch);
        this.maploader = new TmxMapLoader();
        this.map = this.maploader.load("level1.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(this.map, 0.01f);
        this.gamecam.position.set(this.gamePort.getWorldWidth() / 2.0f, this.gamePort.getWorldHeight() / 2.0f, 0.0f);               /////////this.gamecam.position.set(this.gamePort.getWorldWidth() / 2.0f, this.gamePort.getWorldHeight() / 2.0f, 0.0f);
        this.world = new World(new Vector2(0.0f, -10.0f), true);        ///grawitacja
//        this.b2dr = new Box2DDebugRenderer();
        this.creator = new B2WorldCreator(this);
        this.player = new Player(this);
        this.world.setContactListener(new WorldContactListener());
//        play  music
        this.music = MainGame.manager.get("audio/music/music.mp3", Music.class);
        this.music.setLooping(true);
        this.music.setVolume(0.1f);
        this.music.play();
        this.boosMusic = MainGame.manager.get("audio/music/boosMusic.mp3", Music.class);
        this.boosMusic.stop();

        this.items = new Array();
        this.itemsToSpawn = new LinkedBlockingQueue();

        randomRotateAndZoomCamBooseMusic();
        cameraZoomOnPlayer();
    }

    //Shaking Camera and boos music
    private void randomRotateAndZoomCamBooseMusic() {
        if (Player.b2body.getPosition().x > 18 && Player.b2body.getPosition().x < 24  ||
                Player.b2body.getPosition().x > 62 && Player.b2body.getPosition().x < 67
        ){
            music.pause();
            boosMusic.setLooping(true);
            boosMusic.play();
            //show random after 20 s
            //earthquake after 20 s
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {

                    if (Player.b2body.getPosition().x > 18 && Player.b2body.getPosition().x < 24  ||
                            Player.b2body.getPosition().x > 62 && Player.b2body.getPosition().x < 67){
                        if (MathUtils.randomBoolean()){
                            gamecam.rotate(0.2f);
                            gamecam.rotate(-0.1f);
                            gamecam.zoom -=0.005f;
                            if (gamecam.zoom < 0.7f){
                                gamecam.zoom = 0.7f;
                            }
                        }else {
                            gamecam.rotate(-0.2f);
                            gamecam.rotate(0.1f);
                        }
                        gamecam.rotate(0);
                    }
                }
            },1.f,0.1f);
        }else {
            gamecam.rotate(((float) -Math.atan2(gamecam.up.x, gamecam.up.y) * MathUtils.radiansToDegrees)  );
            boosMusic.stop();
            music.play();
        }
    }

    private void cameraZoomOnPlayer() {
        if (Player.b2body.getPosition().x > 79 && Player.b2body.getPosition().x < 81 &&
                Player.b2body.getPosition().y > 1 && Player.b2body.getPosition().y < 2
        ){
            Player.b2body.setLinearVelocity(0.5f,0);
            Player.bdef.position.set(0.3f, 0);
            gamecam.zoom -=0.008f;
            if (gamecam.zoom < 0.25f){
                gamecam.zoom = 0.25f;
            }
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    System.out.println("timer run");
                    MainGame.manager.get("audio/sounds/stomp.wav", Sound.class).play();
                    Player.b2body.setTransform(87,0.35f,0);
                }
            },1.5f,0.01f,1);
        }else {
            gamecam.zoom = 1f;
        }
    }

    @Override
    public void dispose() {
        this.map.dispose();
        this.renderer.dispose();
        this.world.dispose();
//        this.b2dr.dispose();
        this.hud.dispose();
    }

    public boolean gameOver() {
        return this.player.currentState == Player.State.DEAD
                && this.player.getStateTimer() > 3.0f;
    }

    public boolean shake() {
        return  true && this.player.getStateTimer() > 1.0f;
    }

    public TextureAtlas getAtlas() {
        return this.atlas;
    }

    public Hud getHud() {
        return this.hud;
    }

    public TiledMap getMap() {
        return this.map;
    }

    public World getWorld() {
        return this.world;
    }

//    Enabled aggressive block sorting
    public void handleInput(float f) {
        if (this.player.currentState != Player.State.DEAD) {
            if (MainGame.controller.isUpPressed()) {
                this.player.jump();
            }
            if (MainGame.controller.isRightPressed() && this.player.b2body.getLinearVelocity().x <= 2.0f) {
                this.player.b2body.applyLinearImpulse(new Vector2(0.12f, 0.0f), this.player.b2body.getWorldCenter(), true);
            }
            else if (MainGame.controller.isLeftPressed() && this.player.b2body.getLinearVelocity().x >= -2.0f) {
                this.player.b2body.applyLinearImpulse(new Vector2(-0.12f, 0.0f), this.player.b2body.getWorldCenter(), true);
            }
            else if (MainGame.controller.isFirePress()) {
                this.player.fire();
            }
            if (Gdx.input.isKeyJustPressed(62)) {
                this.player.fire();
            }
            //bomb
            else if (MainGame.controller.isBombPress()) {
                this.player.putBomb();
            }
            if (Gdx.input.isKeyJustPressed(30)) {
                this.player.putBomb();
            }
            //bomb

            //cam zoom
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                gamecam.zoom += 0.02f;
            }

            //cam zoom
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
                gamecam.zoom -= 0.02f;
            }
            //cam zoom

            //delete score
            if (Gdx.input.isKeyJustPressed(Input.Keys.J)){
                Hud.scoreLabel.setText(String.format("%06d", 0));
                Hud.resetGameScore();
            }
            //delete score
        }
    }

    public void handleSpawningItems() {
        if (!this.itemsToSpawn.isEmpty()) {
            ItemDef itemDef = (ItemDef)this.itemsToSpawn.poll();
            if (itemDef.type == Bonus.class) {
                this.items.add((Item)new Bonus(this, itemDef.position.x, itemDef.position.y));
            }
        }
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void render(float f) {
        this.update(f);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.f, 1.0f);
        Gdx.gl.glClear(16384);
        this.renderer.render();
//        this.b2dr.render(this.world, this.gamecam.combined);
        MainGame.batch.setProjectionMatrix(this.gamecam.combined);
        MainGame.batch.begin();
        Player player = this.player;
        player.draw((Batch) MainGame.batch);
        for (Enemy enemy : this.creator.getEnemies()) {
            enemy.draw((Batch) MainGame.batch);
        }
        for (Item item : this.items) {
            item.draw((Batch) MainGame.batch);
        }
        MainGame.batch.end();
        MainGame.batch.setProjectionMatrix(this.hud.stage.getCamera().combined);
        this.hud.stage.draw();
        if (this.gameOver()) {
            this.game.setScreen((Screen)new GameOverScreen((Game)this.game));
            this.dispose();
        }
    }

    @Override
    public void resize(int n, int n2) {
        this.gamePort.update(n, n2);
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
    }

    public void spawnItem(ItemDef itemDef) {
        this.itemsToSpawn.add((ItemDef) itemDef);
    }

    public void update(float f) {
        this.handleInput(f);
        this.handleSpawningItems();
        this.world.step(0.016666668f, 6, 2);
        this.player.update(f);
        for (Enemy enemy : this.creator.getEnemies()) {
            enemy.update(f);
            if (!(enemy.getX() < 2.24f + this.player.getX())) continue;
            enemy.b2body.setActive(true);
        }
        Iterator<Item> iterator = this.items.iterator();
        while (iterator.hasNext()) {
            ((Item)iterator.next()).update(f);
        }
        this.hud.update(f);
        if (this.player.currentState != Player.State.DEAD) {
            this.gamecam.position.x = this.player.b2body.getPosition().x;
        }
        this.gamecam.update();
        this.renderer.setView(this.gamecam);

        randomRotateAndZoomCamBooseMusic();
        cameraZoomOnPlayer();
    }
}