package com.siderakis.mobile.widget.paper.button;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.uibinder.client.UiField;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapEvent;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapHandler;
import com.googlecode.mgwt.ui.client.widget.button.ButtonBase;
import com.siderakis.mobile.widget.paper.shadow.ShadowAppearance;
import com.siderakis.mobile.widget.paper.ripple.RippleHelper;

/**
 * User: nick
 * Date: 6/28/14
 */
public class PaperButton extends ButtonBase {

  private final ButtonPaperAppearance appearance;
  private final ShadowAppearance shadowAppearance;
  @UiField
  DivElement text, shadowTop, shadowBottom;

  @UiField
  Element canvas;

  final Element r;

  private static final ButtonPaperAppearance DEFAULT_BUTTON_APPEARANCE = GWT
          .create(ButtonPaperAppearance.class);

  private static final ShadowAppearance DEFAULT_SHADOW_APPEARANCE = GWT.create(ShadowAppearance.class);

  public PaperButton() {
    this(DEFAULT_BUTTON_APPEARANCE, DEFAULT_SHADOW_APPEARANCE);
  }

  public PaperButton(String text) {
    this(DEFAULT_BUTTON_APPEARANCE, DEFAULT_SHADOW_APPEARANCE);
    setText(text);
  }

  private void onUp() {

    RippleHelper.up(r);
    shadowTop.removeClassName(shadowAppearance.css().paperShadowTopZ2());
    shadowTop.addClassName(shadowAppearance.css().paperShadowTopZ1());
    shadowBottom.removeClassName(shadowAppearance.css().paperShadowBottomZ2());
    shadowBottom.addClassName(shadowAppearance.css().paperShadowBottomZ1());

  }

  public PaperButton(final ButtonPaperAppearance appearance, ShadowAppearance defaultShadowAppearance) {

    super(appearance);

    shadowAppearance = defaultShadowAppearance;
    this.appearance = appearance;
    setElement(appearance.uiBinder().createAndBindUi(this));
    r = RippleHelper.create(canvas);
    setRaisedButton(false);

    addTouchHandler(new TouchHandler() {


      @Override
      public void onTouchCancel(TouchCancelEvent event) {
        onUp();
      }

      @Override
      public void onTouchEnd(TouchEndEvent event) {

        onUp();
      }

      @Override
      public void onTouchMove(TouchMoveEvent event) {
      }

      @Override
      public void onTouchStart(TouchStartEvent event) {
        Touch touch = event.getChangedTouches().get(0);
        RippleHelper.down(r, touch.getRelativeX(canvas), touch.getRelativeY(canvas));
        shadowTop.addClassName(shadowAppearance.css().paperShadowTopZ2());
        shadowTop.removeClassName(shadowAppearance.css().paperShadowTopZ1());
        shadowBottom.addClassName(shadowAppearance.css().paperShadowBottomZ2());
        shadowBottom.removeClassName(shadowAppearance.css().paperShadowBottomZ1());
      }
    });


    addTapHandler(new TapHandler() {
      @Override
      public void onTap(TapEvent event) {
        onUp();
      }
    });

    addLongTapHandler(new LongTapHandler() {
      @Override
      public void onLongTap(LongTapEvent event) {
        onUp();
      }
    });

  }

  @Override
  public String getText() {
    return text.getInnerText();
  }

  @Override
  public void setText(String text) {
    this.text.setInnerText(text);
  }


  public PaperButton setRaisedButton(boolean raisedButton) {
    if (!raisedButton) {
      shadowTop.getStyle().setDisplay(Style.Display.NONE);
      shadowBottom.getStyle().setDisplay(Style.Display.NONE);
    } else {
      shadowTop.getStyle().clearDisplay();
      shadowBottom.getStyle().clearDisplay();
    }
    return this;
  }
}
