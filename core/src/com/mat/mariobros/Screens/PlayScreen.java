package com.mat.mariobros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mat.mariobros.MarioBros;
import com.mat.mariobros.Scenes.Hud;
import com.mat.mariobros.Sprites.Enemies.Enemy;
import com.mat.mariobros.Sprites.Items.Item;
import com.mat.mariobros.Sprites.Items.ItemDef;
import com.mat.mariobros.Sprites.Items.Mushroom;
import com.mat.mariobros.Sprites.Mario;
import com.mat.mariobros.Tools.B2WorldCreator;
import com.mat.mariobros.Tools.WorldContactListener;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;


public class PlayScreen
    extends Stage
        implements Screen {
    public static boolean alreadyDestroyed = false;
    private TextureAtlas atlas = new TextureAtlas("Mario_and_Enemies.pack");
//    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;
    private MarioBros game;
    private Viewport gamePort;
    private OrthographicCamera gamecam;
    private Hud hud;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    private TiledMap map;
    private TmxMapLoader maploader;
    private Music music;
    private Mario player;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private float timeHelper;
    private Button playerButton;
    protected Stage stage;

    public PlayScreen(MarioBros marioBros) {
        this.game = marioBros;
        this.gamecam = new OrthographicCamera();
        this.gamePort = new FitViewport(4.0f, 2.08f, (Camera)this.gamecam);
        this.hud = new Hud(MarioBros.batch);
        this.maploader = new TmxMapLoader();
        this.map = this.maploader.load("level1.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(this.map, 0.01f);
        this.gamecam.position.set(this.gamePort.getWorldWidth() / 2.0f, this.gamePort.getWorldHeight() / 2.0f, 0.0f);               /////////this.gamecam.position.set(this.gamePort.getWorldWidth() / 2.0f, this.gamePort.getWorldHeight() / 2.0f, 0.0f);
        this.world = new World(new Vector2(0.0f, -10.0f), true);        ///grawitacja
//        this.b2dr = new Box2DDebugRenderer();
        this.creator = new B2WorldCreator(this);
        this.player = new Mario(this);
        this.world.setContactListener(new WorldContactListener());
        this.music = MarioBros.manager.get("audio/music/music.mp3", Music.class);
        this.music.setLooping(true);
        this.music.setVolume(0.1f);
        this.music.play();
        this.items = new Array();
        this.itemsToSpawn = new LinkedBlockingQueue();
//        this.stage = new Stage(new StretchViewport(MarioBros.V_HEIGHT,MarioBros.V_HEIGHT,gamecam));
//        initPlayerButton();





//        after 1 s
//        Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                System.out.println("11");
//                gamecam.rotate(10);
//            }
//        },1);

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
        return this.player.currentState == Mario.State.DEAD && this.player.getStateTimer() > 3.0f;
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

    /*
     * Enabled aggressive block sorting
     */
    public void handleInput(float f) {
        if (this.player.currentState != Mario.State.DEAD) {
            if (MarioBros.controller.isUpPressed()) {
                this.player.jump();
            }
            if (MarioBros.controller.isRightPressed() && this.player.b2body.getLinearVelocity().x <= 2.0f) {
                this.player.b2body.applyLinearImpulse(new Vector2(0.12f, 0.0f), this.player.b2body.getWorldCenter(), true);
            }
            else if (MarioBros.controller.isLeftPressed() && this.player.b2body.getLinearVelocity().x >= -2.0f) {
                this.player.b2body.applyLinearImpulse(new Vector2(-0.12f, 0.0f), this.player.b2body.getWorldCenter(), true);
            }
            else if (MarioBros.controller.isFirePress()) {
                this.player.fire();
            }
            if (Gdx.input.isKeyJustPressed(62)) {
                this.player.fire();
            }
            //bomb
            else if (MarioBros.controller.isBombPress()) {
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

            if (Gdx.input.isKeyJustPressed(Input.Keys.J)){
//                Hud.score = 0;
                Hud.scoreLabel.setText(String.format("%06d", 0));
                Hud.resetGameScore();

            }

        }
    }

    public void handleSpawningItems() {
        if (!this.itemsToSpawn.isEmpty()) {
            ItemDef itemDef = (ItemDef)this.itemsToSpawn.poll();
            if (itemDef.type == Mushroom.class) {
                this.items.add((Item)new Mushroom(this, itemDef.position.x, itemDef.position.y));
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
        MarioBros.batch.setProjectionMatrix(this.gamecam.combined);
        MarioBros.batch.begin();
        Mario mario = this.player;
        mario.draw((Batch)MarioBros.batch);
        for (Enemy enemy : this.creator.getEnemies()) {
            enemy.draw((Batch)MarioBros.batch);
        }
        for (Item item : this.items) {
            item.draw((Batch)MarioBros.batch);
        }
        MarioBros.batch.end();
        MarioBros.batch.setProjectionMatrix(this.hud.stage.getCamera().combined);
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
        if (this.player.currentState != Mario.State.DEAD) {
            this.gamecam.position.x = this.player.b2body.getPosition().x;
        }
        this.gamecam.update();
        this.renderer.setView(this.gamecam);



        //rotate camera
//        timeHelper += Gdx.graphics.getDeltaTime();
//        if (timeHelper > 8.1f){
//            gamecam.rotate(0.20f);
//            timeHelper = 0;
//            System.out.println(timeHelper);}
    }


    private void initPlayerButton() {
        playerButton = new Button(new Button.ButtonStyle());
        playerButton.setWidth(460);
        playerButton.setHeight(360);
        playerButton.setX(10);
        playerButton.setY(170);
        playerButton.setDebug(true);

        stage.addActor(playerButton);

        playerButton.addListener(new ClickListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                System.out.println("click");
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
}