package com.platformertestgdx.game.guihud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;

public class CurveSlider extends Widget implements Disableable {


    public static final int ORIENTATION_TOP_RIGHT = 0;
    public static final int ORIENTATION_TOP_LEFT  = 1;
    public static final int ORIENTATION_BOT_LEFT  = 2;
    public static final int ORIENTATION_BOT_RIGHT = 3;


    private CurveSliderStyle style;


    private float min, max, stepSize;
    private float value, animateFromValue;
    float position;
    private int sliderOrientation;
    private boolean mirrorX, mirrorY;
    private boolean leftToRight;
    private float animateDuration, animateTime;
    private Interpolation animateInterpolation;
    boolean disabled;
    private Interpolation visualInterpolation;
    private boolean round;



    public CurveSlider (float min, float max, float stepSize, int sliderOrientation, boolean leftToRight, Skin skin) {
        this(min, max, stepSize, sliderOrientation, leftToRight, skin.get("default", CurveSliderStyle.class));
    }

    public CurveSlider (float min, float max, float stepSize, int sliderOrientation, boolean leftToRight, Skin skin, String styleName) {
        this(min, max, stepSize, sliderOrientation, leftToRight, skin.get(styleName, CurveSliderStyle.class));
    }

    /** Creates a Curved Slider.
     *
     * @param min the minimum value
     * @param max tue maximum value
     * @param stepSize the step size between values
     * @param sliderOrientation the slider orientation. The slider is an arc a quarter of a circle.
     * @param leftToRight the initial position1 of the knob regardless of height
     * @param style the {@Link CurveSliderStyle}
     */
    public CurveSlider (float min, float max, float stepSize, int sliderOrientation, boolean leftToRight, CurveSliderStyle style) {
        if (min > max ) throw  new IllegalArgumentException("max must be > min. min,max: " + min + ", " + max);
        if (stepSize <= 0) throw new IllegalArgumentException("stepSize musc be > 0: " + stepSize);
        setStyle(style);
        this.min = min;
        this.max = max;
        this.stepSize = stepSize;
        this.sliderOrientation = sliderOrientation;
        this.leftToRight = leftToRight;
        this.value = min;
        setSize(getPrefWidth(),getPrefHeight());
    }

    public void setStyle (CurveSliderStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    /** Returns the curved slider's style. Modifying the returned style may not have an effect until
     * {@link #setStyle(CurveSliderStyle)} is called. */
    public CurveSliderStyle getStyle () {
        return style;
    }

    @Override
    public void act (float delta) {
        super.act(delta);
        Stage stage = getStage();
        if (stage != null && stage.getActionsRequestRendering()) Gdx.graphics.requestRendering();
    }


    @Override
    public void draw (Batch batch, float parentAlpha) {
        CurveSliderStyle style = this.style;
        boolean disabled = this.disabled;
        final Drawable knob = getKnobDrawable();
        final Drawable bg = (disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;
        final Drawable knobBefore = (disabled && style.disabledKnobBefore != null) ? style.disabledKnobBefore : style.knobBefore;
        final Drawable knobAfter = (disabled && style.disabledKnobAfter != null) ? style.disabledKnobAfter : style.knobAfter;

        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        float knobHeight = knob == null ? 0 : knob.getMinHeight();
        float knobWidth = knob == null ? 0 : knob.getMinWidth();
        float percent = getVisualPercent();

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        if (leftToRight) {
            float positionHeight = height;

            float bgTopHeight = 0;
            if (bg != null) {
                if (round)
                    bg.draw(batch, Math.round(x + (width - bg.getMinWidth()) * 0.5f), y, Math.round(bg.getMinWidth()), height);
                else
                    bg.draw(batch, x + width - bg.getMinWidth() * 0.5f, y, bg.getMinWidth(), height);
                bgTopHeight = bg.getTopHeight();
                positionHeight -= bgTopHeight + bg.getBottomHeight();
            }



            float knobHeightHalf = 0;
            if (min != max) {
                if (knob == null) {
                    knobHeightHalf = knobBefore == null ? 0 : knobBefore.getMinHeight() * 0.5f;
                    position = (positionHeight - knobHeightHalf) * percent;
                    position = Math.min(positionHeight - knobHeightHalf, position);
                } else {
                    knobHeightHalf = knobHeight * 0.5f;
                    position = (positionHeight - knobHeight) * percent;
                    position = Math.min(positionHeight - knobHeight, position) + bg.getBottomHeight();
                }
                position = Math.max(0, position);
            }

            if (knobBefore != null) {
                float offset = 0;
                if (bg != null) offset = bgTopHeight;
                if (round)
                    knobBefore.draw(batch, Math.round(x + (width - knobBefore.getMinWidth()) * 0.5f), Math.round(y + offset), Math.round(knobBefore.getMinWidth()),
                            Math.round(position + knobHeightHalf));
                else
                    knobBefore.draw(batch, x + (width - knobBefore.getMinWidth()) * 0.5f, y + offset, knobBefore.getMinWidth(),
                            position + knobHeightHalf);
            }
            if (knobAfter != null) {
                if (round)
                    knobAfter.draw(batch, Math.round(x + (width - knobAfter.getMinWidth()) * 0.5f), Math.round(y + position + knobHeightHalf),
                            Math.round(knobAfter.getMinWidth()), Math.round(height - position - knobHeightHalf));
                else
                    knobAfter.draw(batch, x + (width - knobAfter.getMinWidth()) * 0.5f, y + position + knobHeightHalf,
                            knobAfter.getMinWidth(), height - position - knobHeightHalf);
            }
            if (knob != null) {
                if (round)
                    knob.draw(batch, Math.round(x + (width - knobWidth) * 0.5f), Math.round(y + position), Math.round(knobWidth), Math.round(knobHeight));
                else
                    knob.draw(batch, x + (width - knobWidth) * 0.5f, y + position, knobWidth, knobHeight);
            }
        } else {
            float positionWidth = width;

            float bgLeftWidth = 0;
            if (bg != null) {
                if (round)
                    bg.draw(batch, x, Math.round(y + (height - bg.getMinHeight()) * 0.5f), width, Math.round(bg.getMinHeight()));
                else
                    bg.draw(batch, x, y + (height - bg.getMinHeight()) * 0.5f, width, bg.getMinHeight());
                bgLeftWidth = bg.getLeftWidth();
                positionWidth -= bgLeftWidth + bg.getRightWidth();
            }

            float knobWidthHalf = 0;
            if (min != max) {
                if (knob == null) {
                    knobWidthHalf = knobBefore == null ? 0 : knobBefore.getMinWidth() * 0.5f;
                    position = (positionWidth - knobWidthHalf) * percent;
                    position = Math.min(positionWidth - knobWidthHalf, position);
                } else {
                    knobWidthHalf = knobWidth * 0.5f;
                    position = (positionWidth - knobWidth) * percent;
                    position = Math.min(positionWidth - knobWidth, position) + bgLeftWidth;
                }
                position = Math.max(0, position);
            }

            if (knobBefore != null) {
                float offset = 0;
                if (bg != null) offset = bgLeftWidth;
                if (round)
                    knobBefore.draw(batch, Math.round(x + offset), Math.round(y + (height - knobBefore.getMinHeight()) * 0.5f),
                            Math.round(position + knobWidthHalf), Math.round(knobBefore.getMinHeight()));
                else
                    knobBefore.draw(batch, x + offset, y + (height - knobBefore.getMinHeight()) * 0.5f,
                            position + knobWidthHalf, knobBefore.getMinHeight());
            }
            if (knobAfter != null) {
                if (round)
                    knobAfter.draw(batch, Math.round(x + position + knobWidthHalf), Math.round(y + (height - knobAfter.getMinHeight()) * 0.5f),
                            Math.round(width - position - knobWidthHalf), Math.round(knobAfter.getMinHeight()));
                else
                    knobAfter.draw(batch, x + position + knobWidthHalf, y + (height - knobAfter.getMinHeight()) * 0.5f,
                            width - position - knobWidthHalf, knobAfter.getMinHeight());
            }
            if (knob != null) {
                if (round)
                    knob.draw(batch, Math.round(x + position), Math.round(y + (height - knobHeight) * 0.5f), Math.round(knobWidth), Math.round(knobHeight));
                else
                    knob.draw(batch, x + position, y + (height - knobHeight) * 0.5f, knobWidth, knobHeight);
            }
        }
    }

    public float getValue () {
        return value;
    }

    /** If {@link #setAnimateDuration(float) animating} the progress bar value, this returns the value current displayed. */
    public float getVisualValue () {
        if (animateTime > 0) return animateInterpolation.apply(animateFromValue, value, 1 - animateTime / animateDuration);
        return value;
    }

    public float getPercent () {
        return (value - min) / (max - min);
    }

    public float getVisualPercent () {
        return visualInterpolation.apply((getVisualValue() - min) / (max - min));
    }

    protected Drawable getKnobDrawable () {
        return (disabled && style.disabledKnob != null) ? style.disabledKnob : style.knob;
    }

    /** Returns progress bar visual position1 within the range. */
    protected float getKnobPosition () {
        return this.position;
    }

    /** Sets the progress bar position1, rounded to the nearest step size and clamped to the minimum and maximum values.
     * {@link #clamp(float)} can be overridden to allow values outside of the progress bar's min/max range.
     * @return false if the value was not changed because the progress bar already had the value or it was canceled by a
     *         listener. */
    public boolean setValue (float value) {
        value = clamp(Math.round(value / stepSize) * stepSize);
        float oldValue = this.value;
        if (value == oldValue) return false;
        float oldVisualValue = getVisualValue();
        this.value = value;
        ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
        boolean cancelled = fire(changeEvent);
        if (cancelled)
            this.value = oldValue;
        else if (animateDuration > 0) {
            animateFromValue = oldVisualValue;
            animateTime = animateDuration;
        }
        Pools.free(changeEvent);
        return !cancelled;
    }

    /** Clamps the value to the progress bar's min/max range. This can be overridden to allow a range different from the progress
     * bar knob's range. */
    protected float clamp (float value) {
        return MathUtils.clamp(value, min, max);
    }

    /** Sets the range of this progress bar. The progress bar's current value is clamped to the range. */
    public void setRange (float min, float max) {
        if (min > max) throw new IllegalArgumentException("min must be <= max");
        this.min = min;
        this.max = max;
        if (value < min)
            setValue(min);
        else if (value > max) setValue(max);
    }

    public void setStepSize (float stepSize) {
        if (stepSize <= 0) throw new IllegalArgumentException("steps must be > 0: " + stepSize);
        this.stepSize = stepSize;
    }

    public float getPrefWidth () {
        final Drawable knob = getKnobDrawable();
        final Drawable bg = (disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;
        return Math.max(knob == null ? 0 : knob.getMinWidth(), bg.getMinWidth());
    }

    public float getPrefHeight () {
        final Drawable knob = getKnobDrawable();
        final Drawable bg = (disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;
        return Math.max(knob == null ? 0 : knob.getMinHeight(), bg == null ? 0 : bg.getMinHeight());
    }

    public float getMinValue () {
        return this.min;
    }

    public float getMaxValue () {
        return this.max;
    }

    public float getStepSize () {
        return this.stepSize;
    }

    /** If > 0, changes to the progress bar value via {@link #setValue(float)} will happen over this duration in seconds. */
    public void setAnimateDuration (float duration) {
        this.animateDuration = duration;
    }

    /** Sets the interpolation to use for {@link #setAnimateDuration(float)}. */
    public void setAnimateInterpolation (Interpolation animateInterpolation) {
        if (animateInterpolation == null) throw new IllegalArgumentException("animateInterpolation cannot be null.");
        this.animateInterpolation = animateInterpolation;
    }

    /** Sets the interpolation to use for display. */
    public void setVisualInterpolation (Interpolation interpolation) {
        this.visualInterpolation = interpolation;
    }

    /** If true (the default), inner Drawable positions and sizes are rounded to integers.*/
    public void setRound(boolean round) {
        this.round = round;
    }

    public void setDisabled (boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled () {
        return disabled;
    }

    /** True if the progress bar is vertical, false if it is horizontal. **/
    public boolean isLeftToRight() {
        return leftToRight;
    }

    // TODO author name
    /** The style for a curved slider, see {@link CurveSlider}.
     * @author me
     */
    static public class CurveSliderStyle {
        /** The progress bar background, stretched only in one direction. Optional. */
        public Drawable background;
        /** Optional. **/
        public Drawable disabledBackground;
        /** Optional, centered on the background. */
        public Drawable knob, disabledKnob;
        /** Optional. */
        public Drawable knobBefore, knobAfter, disabledKnobBefore, disabledKnobAfter;

        public CurveSliderStyle () {
        }

        public CurveSliderStyle (Drawable background, Drawable knob) {
            this.background = background;
            this.knob = knob;
        }

        public CurveSliderStyle (CurveSliderStyle style) {
            this.background = style.background;
            this.disabledBackground = style.disabledBackground;
            this.knob = style.knob;
            this.disabledKnob = style.disabledKnob;
            this.knobBefore = style.knobBefore;
            this.knobAfter = style.knobAfter;
            this.disabledKnobBefore = style.disabledKnobBefore;
            this.disabledKnobAfter = style.disabledKnobAfter;
        }
    }
}
