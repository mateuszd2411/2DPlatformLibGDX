package com.mat.mainGame.Sprites.TitleObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.mat.mainGame.MainGame;
import com.mat.mainGame.Scenes.Hud;
import com.mat.mainGame.Screens.PlayScreen;
import com.mat.mainGame.Sprites.Player;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MainGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Player mario) {
        if (!mario.isDead()){
            setCategoryFilter(MainGame.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            MainGame.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }
        MainGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }
}
