package com.mat.mainGame.Sprites.TitleObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.mat.mainGame.MainGame;
import com.mat.mainGame.Scenes.Hud;
import com.mat.mainGame.Screens.PlayScreen;
import com.mat.mainGame.Sprites.Items.ItemDef;
import com.mat.mainGame.Sprites.Items.Bonus;
import com.mat.mainGame.Sprites.Player;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MainGame.COIN_BIT);
    }

    @Override
    public void onHeadHit(Player mario) {
        if (getCell().getTile().getId() == BLANK_COIN)
            MainGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else {
            if (object.getProperties().containsKey("bonus")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MainGame.PPM),
                        Bonus.class));
                MainGame.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            }
            else
                MainGame.manager.get("audio/sounds/coin.wav", Sound.class).play();
                getCell().setTile(tileSet.getTile(BLANK_COIN));
                Hud.addScore(100);
        }

    }
}
