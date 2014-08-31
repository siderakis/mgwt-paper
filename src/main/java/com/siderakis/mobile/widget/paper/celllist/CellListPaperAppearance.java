package com.siderakis.mobile.widget.paper.celllist;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

import com.googlecode.mgwt.ui.client.widget.list.celllist.CellListAbstractAppearance;

public class CellListPaperAppearance extends CellListAbstractAppearance {

  static {
    Resources.INSTANCE.css().ensureInjected();
  }

  interface Css extends CellListCss {}

  interface Resources extends ClientBundle {

    Resources INSTANCE = GWT.create(Resources.class);

    @Source({
        "celllist-paper.css"})
    Css css();

  }

  @Override
  public CellListCss css() {
    return Resources.INSTANCE.css();
  }
}
