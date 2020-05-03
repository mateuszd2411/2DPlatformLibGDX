package com.mat.mainGame.Screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
//not currently used    not currently used  not currently used  not currently used  not currently used
public class GameLabel extends Label {
    public GameLabel() {
        super("",prepareLabelStyle());
        init();
    }

    private void init() {
        this.setX(70);
        this.setY(650);
    }

    private static LabelStyle prepareLabelStyle(){
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        return labelStyle;
    }

}
