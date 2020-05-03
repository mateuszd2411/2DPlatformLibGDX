package com.mat.mainGame.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BasicDialog extends Image {
    private Label label;

    private final static int WIDHT = 800;
    private final static int HEIGHT = 480;

    public BasicDialog(){
        super(new Texture("badlogic.jpg"));

        this.setOrigin(WIDHT/2,HEIGHT/2);
        this.setSize(WIDHT,HEIGHT);

        this.setPosition(0,0);

        label = new Label("CZAS", new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));
        label.setPosition(200,400);

        //add some function after click dialog
        this.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                fadeOutDialog();
                System.out.println("click");

                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public void initContent(String text){
        label.setText(text);
        this.getStage().addActor(label);
    }

    public boolean fadeOutDialog() {
        System.out.println("fade");
        //here put function like super speed, jump etc
        BasicDialog.this.remove();
        label.remove();
        return true;
    }
}
