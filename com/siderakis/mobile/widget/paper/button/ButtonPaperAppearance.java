/*
 * Copyright 2013 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.siderakis.mobile.widget.paper.button;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.GssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.googlecode.mgwt.ui.client.widget.button.ButtonBaseAppearance;

public class ButtonPaperAppearance implements ButtonBaseAppearance {
  @UiTemplate("ButtonPaperAppearance.ui.xml")
  interface Binder extends UiBinder<Element, PaperButton> {
  }

  private static final Binder UI_BINDER = GWT.create(Binder.class);

  @Override
  public UiBinder<Element, PaperButton> uiBinder() {
    return UI_BINDER;
  }

  static {
    Resources.INSTANCE.css().ensureInjected();
  }

  interface Resources extends ClientBundle {

    Resources INSTANCE = GWT.create(Resources.class);

    @Source({"paper-button.gss"})
    PaperButtonCss css();
  }

  @Override
  public PaperButtonCss css() {
    return Resources.INSTANCE.css();
  }


  interface PaperButtonCss extends GssResource, ButtonBaseCss {
    @Override
    @ClassName("mgwt-Button")
    String button();

    @Override
    @ClassName("mgwt-Button-active")
    String active();

    @ClassName("mgwt-Button-round")
    String mgwtButtonRound();

    @ClassName("mgwt-Button-important")
    String mgwtButtonImportant();

    @ClassName("mgwt-Button-small")
    String mgwtButtonSmall();

    @ClassName("mgwt-Button-disabled")
    String mgwtButtonDisabled();

    @ClassName("mgwt-Button-confirm")
    String mgwtButtonConfirm();

  }
}
