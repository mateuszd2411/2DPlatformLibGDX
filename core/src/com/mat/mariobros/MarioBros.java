package com.mat.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mat.mariobros.Scenes.Hud;
import com.mat.mariobros.Screens.PlayScreen;

public class MarioBros
		extends Game {
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short FIREBALL_BIT = 1024;
	public static final short BOMB_BIT = 2048;
	public static final short END_LEVEL = 8192;
	public static final short GROUND_BIT = 1;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_BIT = 2;
	public static final short MARIO_HEAD_BIT = 512;
	public static final short NOTHING_BIT = 0;
	public static final short OBJECT_BIT = 32;
	public static final float PPM = 100.0f;
	public static final int V_HEIGHT = 208;
	public static final int V_WIDTH = 400;
	public static SpriteBatch batch;
	public static Controller controller;
	public static AssetManager manager;


//	public static Preferences prefs;

	@Override
	public void create() {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/music.mp3", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
		manager.load("audio/sounds/powerup.wav", Sound.class);
		manager.load("audio/sounds/powerdown.wav", Sound.class);
		manager.load("audio/sounds/stomp.wav", Sound.class);
		manager.load("audio/sounds/mariodie.wav", Sound.class);
		manager.load("audio/sounds/bomb.wav", Sound.class);
		manager.load("audio/sounds/nope.wav", Sound.class);
		manager.load("audio/sounds/dog.wav", Sound.class);
		manager.load("audio/sounds/par.mp3", Sound.class);
		manager.load("audio/sounds/yeah.mp3", Sound.class);
		manager.load("audio/sounds/mee.mp3", Music.class);
		manager.finishLoading();
		this.setScreen(new PlayScreen(this));
		controller = new Controller();

//		prefs = Gdx.app.getPreferences(Hud.GAME_PREFS);
//		Hud.score = prefs.getInteger(Hud.GAME_SCORE);
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}

	@Override
	public void render() {
		super.render();
		controller.draw();
	}

	@Override
	public void resize(int n, int n2) {
		super.resize(n, n2);
		controller.resize(n, n2);
	}
}