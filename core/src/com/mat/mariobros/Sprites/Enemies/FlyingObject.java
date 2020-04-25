package com.mat.mariobros.Sprites.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.mat.mariobros.MarioBros;

public class FlyingObject extends Image {

    public final static String MONEY = "badlogic.jpg";
    public static boolean touch = false;

    private  final  static int WIDHT = 150;
    private  final  static int HEIGHT = 100;

    private final static int STARTING_X = MarioBros.V_WIDTH/3;
    private final static int STARTING_Y = MarioBros.V_HEIGHT/2;

    public FlyingObject(){
        super(new Texture(MONEY));

        this.setOrigin(WIDHT/2, HEIGHT/2);
        this.setSize(WIDHT,HEIGHT);

        this.setPosition(STARTING_X,STARTING_Y);

        this.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                System.out.println("touched");
                touch = true;

//                Timer.schedule(new Timer.Task() {
//                    @Override
//                    public void run() {
//                        System.out.println("mor");
//
//
//                    }
//                },2,50);


                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        // Do something
                        System.out.println("sek");
                    }
                }, 1);

                FlyingObject.this.remove();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

    }

    public void flylikeHell(){

        System.out.println("hell");
        Action a = Actions.parallel(
                Actions.moveBy(300,200,5),
                Actions.rotateBy(360,5)
        );

        Action b = Actions.parallel(
                Actions.moveBy(-500,900,3),
                Actions.rotateBy(360,3)
        );

        Action c = Actions.run(new Runnable() {
            @Override
            public void run() {
                FlyingObject.this.remove();
            }
        });

        this.addAction(Actions.sequence(a,b,c));


    }

}
