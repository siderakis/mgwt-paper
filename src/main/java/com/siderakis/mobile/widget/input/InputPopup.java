package com.siderakis.mobile.widget.input;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.resources.client.GssResource;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.googlecode.mgwt.ui.client.widget.input.MTextArea;

/**
 * User: nick
 * Date: 8/30/14
 */
public class InputPopup extends MTextArea {

  private final SimplePanel popupPanel = new SimplePanel();
  private final TextArea popupTextArea = new TextArea();

  public InputPopup() {

    addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        show();
      }
    });

    popupTextArea.addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        hide();
      }
    });

    popupPanel.setStyleName(css().popup());
    popupTextArea.setStyleName(css().textArea());
    popupPanel.add(popupTextArea);

  }

  public void hide() {
    setText(popupTextArea.getText());
    RootPanel.get().remove(popupPanel);
  }

  public void show() {
    popupTextArea.setText(getText());
    RootPanel.get().add(popupPanel);
    Scheduler.get().scheduleDeferred(new Command() {
      @Override
      public void execute() {
        popupTextArea.setFocus(true);
      }
    });
  }

  static {
    Resources.INSTANCE.css().ensureInjected();
  }

  interface Resources extends ClientBundle {

    Resources INSTANCE = GWT.create(Resources.class);

    @Source({"textarea-popup.gss"})
    TextAreaPopupCss css();
  }

  public TextAreaPopupCss css() {
    return Resources.INSTANCE.css();
  }

  interface TextAreaPopupCss extends GssResource, CssResource {
    @CssResource.ClassName("popup")
    String popup();

    @CssResource.ClassName("text-area")
    String textArea();

  }
}
