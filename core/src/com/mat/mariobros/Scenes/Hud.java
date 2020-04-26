package com.mat.mariobros.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mat.mariobros.MarioBros;
import com.mat.mariobros.Sprites.Enemies.FlyingObject;


public class Hud implements Disposable{

    //Scene2D.ui Stage and its own Viewport for HUD
    public static Stage stage;
    private Viewport viewport;

    //Mario score/time Tracking Variables
    public static Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private float timeCount;
    public static Integer score;

    //Scene2D widgets
    public static Label countdownLabel;
    public static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;


    public static String GAME_PREFS = "com.mygdx.mariobros.prefs";
    public final  static  String GAME_SCORE = "com.mygdx.mariobros.prefs.score";

    public static Preferences prefs;

    public static Button resetScoreButton;

    public Hud(SpriteBatch sb){
        //define our tracking variables
        worldTimer = 900;
        timeCount = 0;
//        score = 0;
        prefs = Gdx.app.getPreferences(GAME_PREFS);
        score = prefs.getInteger(GAME_SCORE);
//        score = MarioBros.prefs.getInteger(GAME_SCORE);


        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

//        initResetScoreButton();

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));
        scoreLabel =new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));
        timeLabel = new Label("CZAS", new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));
        levelLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));
        worldLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));
        marioLabel = new Label("KOZA", new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        //add our table to the stage
        stage.addActor(table);




    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
//            System.out.println(countdownLabel);
            timeCount = 0;
        }
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
        prefs.putInteger(GAME_SCORE,score);
        prefs.flush();
    }



    public boolean isTimeUp() { return timeUp; }

    public static void resetGameScore() {
        System.out.println("reset score");
        score = 0;
        prefs.putInteger(GAME_SCORE,score);
        prefs.flush();
    }

//    public static void initResetScoreButton() {
//        System.out.println("button");
//
//
//        resetScoreButton = new Button(new Button.ButtonStyle());
//        resetScoreButton.setWidth(30);
//        resetScoreButton.setHeight(30);
//        resetScoreButton.setX(150);
//        resetScoreButton.setY(170);
//        resetScoreButton.setDebug(true);
//
//        stage.addActor(resetScoreButton);
//
//    }


    @Override
    public void dispose() { stage.dispose(); }
}