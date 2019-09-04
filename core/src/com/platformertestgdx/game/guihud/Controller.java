package com.platformertestgdx.game.guihud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class Controller {

    private ControlPad joystick;
    private Slider slider;
    private AlphaButton btn1, btn2, btn3;

    private float padAlpha, sliderAlpha, btnAlpha;

    // TODO controller might be weird on other resolutions

    public Controller(Stage stage, Skin skin, float padDeadZone, float padAlpha, float sliderAlpha, float btnAlpha) {
        this.padAlpha = padAlpha;
        this.sliderAlpha = sliderAlpha;
        this.btnAlpha = btnAlpha;

        joystick = new ControlPad(padDeadZone, skin, padAlpha);
        //slider = new CurveSlider(0,1, 0.5f, true, skin);

        // TODO Stylename should be changed for something more representative of the button graphics
//        btn1 = new AlphaButton(skin , "btn1");
//        btn2 = new AlphaButton(skin , "btn2");
//        btn3 = new AlphaButton(skin , "btn3");

        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());
        table.align(Align.bottomLeft);

        table.add(joystick);
        stage.addActor(table);
    }


    public void setPadAlpha(float padAlpha) {
        this.padAlpha = padAlpha;
    }

    public void setSliderAlpha(float sliderAlpha) {
        this.sliderAlpha = sliderAlpha;
    }

    public void setBtnAlpha(float btnAlpha) {
        this.btnAlpha = btnAlpha;
    }

    public ControlPad getJoystick() {
        return joystick;
    }

    public void draw(Batch batch) {
        joystick.draw(batch, padAlpha);
        slider.draw(batch, sliderAlpha);
        btn1.draw(batch, btnAlpha);
        btn2.draw(batch, btnAlpha);
        btn3.draw(batch, btnAlpha);
    }
}
