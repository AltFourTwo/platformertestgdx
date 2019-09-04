package com.platformertestgdx.game.guihud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class PointBar extends Widget implements Disableable {

    private PointBarStyle style;

    protected float alpha;

    protected float min, max, stepSize;
    protected float value1, animateFromValue;
    protected float position1;
    protected float animateDuration, animateTime;
    protected Interpolation animateInterpolation = Interpolation.linear;
    protected Interpolation visualInterpolation = Interpolation.linear;
    protected boolean disabled;
    protected boolean round;

    public PointBar(int min, int max, int current, float stepSize, Skin skin, float alpha) {
        this(min, max, current, stepSize, skin.get("default", PointBarStyle.class), alpha);
    }

    public PointBar(int min, int max, int current, float stepSize, Skin skin, String styleName, float alpha) {
        this(min, max, current, stepSize, skin.get(styleName, PointBarStyle.class), alpha);
    }

    protected PointBar(int min, int max, int current, float stepSize, float alpha) {
        this.min = min;
        this.max = max;
        this.stepSize = stepSize;
        this.value1 = current;
        this.alpha = alpha;
    }

    /** Creates a points bar.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @param current the initial value when the bar is created
     * @param stepSize the step size between values
     * @param style the {@Link PointsBarStyle}
     */
    public PointBar(int min, int max, int current, float stepSize, PointBarStyle style, float alpha) {
        if (stepSize <= 0) throw new IllegalArgumentException("stepSize must be > 0: " + stepSize);
        setStyle(style);
        this.alpha = alpha;
        this.min = min;
        this.max = max;
        this.stepSize = stepSize;
        this.value1 = current;
    }

    public void setStyle (PointBarStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    /** Returns the point bar style. Modifying the returned style may not have an effect until
     * {@link #setStyle(PointBarStyle)} is called. */
    public PointBarStyle getStyle () {
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
        PointBarStyle style = this.style;
        boolean disabled = this.disabled;
        final Drawable bg = (disabled && style.disabledbackground != null) ? style.disabledbackground : style.background;
        final Drawable ol = (disabled && style.disabledoverlay != null) ? style.disabledoverlay: style.overlay;
        final Drawable knob1 = getKnobDrawable();
        final Drawable knob1Before = (disabled && style.disabledknob1before != null) ? style.disabledknob1before : style.knob1before;

        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        float knob1Height = knob1 == null ? 0 : knob1.getMinHeight();
        float knob1Width = knob1 == null ? 0 : knob1.getMinWidth();
        float percent1 = getVisualPercent1();

        batch.setColor(color.r, color.g, color.b, color.a * alpha);

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

        float knob1WidthHalf = 0;
        if (min != max) {
            if (knob1 == null) {
                knob1WidthHalf = knob1Before == null ? 0 : knob1Before.getMinWidth() * 0.5f;
                position1 = (positionWidth - knob1WidthHalf) * percent1;
                position1 = Math.min(positionWidth - knob1WidthHalf, position1);
            } else {
                knob1WidthHalf = knob1Width * 0.5f;
                position1 = (positionWidth - knob1Width) * percent1;
                position1 = Math.min(positionWidth - knob1Width, position1) + bgLeftWidth;
            }
            position1 = Math.max(0, position1);
        }

        if (knob1Before != null) {
            float offset = 0;
            if (bg != null) offset = bgLeftWidth;
            if (round)
                knob1Before.draw(batch, Math.round(x + offset), Math.round(y + (height - knob1Before.getMinHeight()) * 0.5f),
                        Math.round(position1 + knob1WidthHalf), Math.round(knob1Before.getMinHeight()));
            else
                knob1Before.draw(batch, x + offset, y + (height - knob1Before.getMinHeight()) * 0.5f,
                        position1 + knob1WidthHalf, knob1Before.getMinHeight());
        }
        if (knob1 != null) {
            if (round)
                knob1.draw(batch, Math.round(x + position1), Math.round(y + (height - knob1Height) * 0.5f), Math.round(knob1Width), Math.round(knob1Height));
            else
                knob1.draw(batch, x + position1, y + (height - knob1Height) * 0.5f, knob1Width, knob1Height);
        }

        if (ol != null) {
            if (round)
                ol.draw(batch, x, Math.round(y + (height - bg.getMinHeight()) * 0.5f), width, Math.round(bg.getMinHeight()));
            else
                ol.draw(batch, x, y + (height - bg.getMinHeight()) * 0.5f, ol.getMinWidth(), ol.getMinHeight());
        }

    }

    public float getValue1() {
        return value1;
    }

    /** If {@link #setAnimateDuration(float) animating} the progress bar value1, this returns the value1 current displayed. */
    public float getVisualValue1 () {
        if (animateTime > 0) return animateInterpolation.apply(animateFromValue, value1, 1 - animateTime / animateDuration);
        return value1;
    }

    public float getPercent () {
        return (value1 - min) / (max - min);
    }

    public float getVisualPercent1 () {
        return visualInterpolation.apply((getVisualValue1() - min) / (max - min));
    }

    public Drawable getKnobDrawable () {
        return (disabled && style.disabledknob1 != null) ? style.disabledknob1 : style.knob1;
    }

    /** Returns progress bar visual position within the range. */
    public float getKnobPosition () {
        return this.position1;
    }

    /** Sets the progress bar position1, rounded to the nearest step size and clamped to the minimum and maximum values.
     * {@link #clamp(float)} can be overridden to allow values outside of the progress bar's min/max range.
     * @return false if the value1 was not changed because the progress bar already had the value1 or it was canceled by a
     *         listener. */
    public boolean setValue(float value) {
        value = clamp(Math.round(value / stepSize) * stepSize);
        float oldValue = this.value1;
        if (value == oldValue) return false;
        float oldVisualValue = getVisualValue1();
        this.value1 = value;
        if (animateDuration > 0) {
            animateFromValue = oldVisualValue;
            animateTime = animateDuration;
        }
        return true;
    }

    /** Clamps the value1 to the progress bar's min/max range. This can be overridden to allow a range different from the progress
     * bar knob's range. */
    protected float clamp (float value) {
        return MathUtils.clamp(value, min, max);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    /** Sets the range of this progress bar. The progress bar's current value1 is clamped to the range. */
    public void setRange (float min, float max) {
        if (min > max) throw new IllegalArgumentException("min must be <= max");
        this.min = min;
        this.max = max;
        if (value1 < min)
            setValue(min);
        else if (value1 > max) setValue(max);
    }

    public void setStepSize (float stepSize) {
        if (stepSize <= 0) throw new IllegalArgumentException("steps must be > 0: " + stepSize);
        this.stepSize = stepSize;
    }

    public float getPrefWidth () {
        final Drawable knob = getKnobDrawable();
        final Drawable bg = (disabled && style.disabledbackground != null) ? style.disabledbackground : style.background;
        return Math.max(knob == null ? 0 : knob.getMinWidth(), bg.getMinWidth());
    }

    public float getPrefHeight () {
        final Drawable knob = getKnobDrawable();
        final Drawable bg = (disabled && style.disabledbackground != null) ? style.disabledbackground : style.background;
        return Math.max(knob == null ? 0 : knob.getMinHeight(), bg.getMinHeight());
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

    /** If > 0, changes to the progress bar value1 via {@link #setValue(float)} will happen over this duration in seconds. */
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

    // TODO author name
    /** The style for a points bar (eg : hp bar), see {@link PointBar}.
     * @author me
     */

    static public class PointBarStyle {
        /** The progress bar background, stretched only in one direction. Optional. */
        public Drawable background, overlay;
        /** Optional. **/
        public Drawable disabledbackground, disabledoverlay;
        /** Optional, centered on the background. */
        public Drawable knob1, disabledknob1;
        /** Optional. */
        public Drawable knob1before, disabledknob1before;

        public PointBarStyle () {
        }

        public PointBarStyle (PointBarStyle style) {
            this.background = style.background;
            this.overlay = style.overlay;
            this.disabledbackground = style.disabledbackground;
            this.disabledoverlay = style.disabledoverlay;
            this.knob1 = style.knob1;
            this.disabledknob1 = style.disabledknob1;
            this.knob1before = style.knob1before;
            this.disabledknob1before = style.disabledknob1before;
        }
    }

}
