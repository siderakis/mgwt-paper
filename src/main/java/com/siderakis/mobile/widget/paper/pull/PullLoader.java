package com.siderakis.mobile.widget.paper.pull;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.GssResource;

/**
 * User: nick
 */
public class PullLoader {

  public static final Resources INSTANCE = GWT.create(Resources.class);

  static {
    INSTANCE.css().ensureInjected();
  }


  interface Resources extends ClientBundle {
    @Source({"pull-loader.gss"})
    public PullLoaderCss css();
  }

  public static PullLoaderCss css() {
    return INSTANCE.css();
  }


  public interface PullLoaderCss extends GssResource {
    String loader();

    String outer();

  }

}
