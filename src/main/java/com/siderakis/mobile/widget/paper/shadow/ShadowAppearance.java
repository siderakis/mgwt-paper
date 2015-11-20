package com.siderakis.mobile.widget.paper.shadow;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.GssResource;

/**
 * User: nick
 * Date: 6/28/14
 */
public class ShadowAppearance {

  public static final Resources INSTANCE = GWT.create(Resources.class);

  static {
    INSTANCE.css().ensureInjected();
  }


  interface Resources extends ClientBundle {
    @Source({"paper-shadow.gss"})
    public PaperShadowCss css();
  }

  public static PaperShadowCss css() {
    return INSTANCE.css();
  }


  public interface PaperShadowCss extends GssResource {
    @ClassName("paper-shadow-top-z-1")
    String paperShadowTopZ1();

    @ClassName("paper-shadow-bottom-z-1")
    String paperShadowBottomZ1();

    @ClassName("paper-shadow-top-z-2")
    String paperShadowTopZ2();

    @ClassName("paper-shadow-bottom-z-2")
    String paperShadowBottomZ2();

    @ClassName("paper-shadow-bottom-z-5")
    String paperShadowBottomZ5();

    @ClassName("paper-shadow-top-z-5")
    String paperShadowTopZ5();

    @ClassName("paper-shadow-top-z-4")
    String paperShadowTopZ4();

    @ClassName("paper-shadow-top-z-3")
    String paperShadowTopZ3();

    @ClassName("paper-shadow-bottom-z-3")
    String paperShadowBottomZ3();

    @ClassName("paper-shadow-animated")
    String paperShadowAnimated();

    @ClassName("paper-shadow-bottom-z-4")
    String paperShadowBottomZ();

    @ClassName("paper-shadow")
    String paperShadow();

    @ClassName("paper-shadow-animate-z-1-z-2")
    String paperShadowAnimateZZ();

    @ClassName("paper-shadow-bottom")
    String paperShadowBottom();

    @ClassName("paper-shadow-top")
    String paperShadowTop();
  }

}
