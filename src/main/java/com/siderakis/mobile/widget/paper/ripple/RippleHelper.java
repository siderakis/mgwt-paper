package com.siderakis.mobile.widget.paper.ripple;

import com.google.gwt.dom.client.Element;

/**
 * User: nick
 * Date: 6/28/14
 */
public class RippleHelper {

  public static native Element down(Element impl, int x, int y) /*-{
      impl.downAction(x, y);

  }-*/;


  public static native Element up(Element impl) /*-{
      impl.upAction();
  }-*/;


  public static native Element create(Element canvas) /*-{

      //
      // INK EQUATIONS
      //
      function waveRadiusFn(touchDownMs, touchUpMs, anim) {
          // Convert from ms to s.
          var touchDown = touchDownMs / 1000;
          var touchUp = touchUpMs / 1000;
          var totalElapsed = touchDown + touchUp;

          // Outdated equation: distance travelled = a * t^2 + v(initial)
          var distanceOnDown = anim.touchDownAcceleration * pow(totalElapsed, 2);

          // Accelerate faster on touch up
          var distanceOnUp = anim.touchUpAcceleration * (pow(touchUp, 2));
          return distanceOnDown + distanceOnUp;
      }

      function waveOpacityFn(td, tu, anim) {
          // Convert from ms to s.
          var touchDown = td / 1000;
          var touchUp = tu / 1000;
          var totalElapsed = touchDown + touchUp;

          if (tu <= 0) {  // before touch up
              return anim.initialOpacity;
          }
          return Math.max(0, anim.initialOpacity - touchUp * anim.opacityDecayVelocity);
      }

      function waveOuterOpacityFn(td, tu, anim) {
          // Convert from ms to s.
          var touchDown = td / 1000;
          var touchUp = tu / 1000;

          // Linear increase in background opacity, capped at the opacity
          // of the wavefront (waveOpacity).
          var outerOpacity = touchDown * 0.3;
          var waveOpacity = waveOpacityFn(td, tu, anim);
          return Math.max(0, Math.min(outerOpacity, waveOpacity));
      }

      function waveGravityToCenterPercentageFn(td, tu, r) {
          // Convert from ms to s.
          var touchDown = td / 1000;
          var touchUp = tu / 1000;
          var totalElapsed = touchDown + touchUp;

          return Math.min(1.0, touchUp * 6);
      }

      // Determines whether the wave should be completely removed.
      function waveDidFinish(wave, radius, anim) {
          var waveOpacity = waveOpacityFn(wave.tDown, wave.tUp, anim);
          // If the wave opacity is 0 and the radius exceeds the bounds
          // of the element, then this is finished.
          if (waveOpacity < 0.01 && radius >= wave.maxRadius) {
              return true;
          }
          return false;
      };

      //
      // DRAWING
      //
      function drawRipple(canvas, x, y, radius, innerColor, outerColor, innerColorAlpha, outerColorAlpha) {
          var ctx = canvas.getContext('2d');
          if (outerColor) {
              ctx.fillStyle = outerColor;
              ctx.fillRect(0, 0, canvas.width, canvas.height);
          }
          ctx.beginPath();
          ctx.arc(x, y, radius, 0, 2 * Math.PI, false);
          ctx.fillStyle = innerColor;
          ctx.fill();
      }

      //
      // SETUP
      //
      function createWave(elem) {
          var elementStyle = window.getComputedStyle(elem);
          var fgColor = elementStyle.color;

          var wave = {
              waveColor: fgColor,
              maxRadius: 0,
              isMouseDown: false,
              mouseDownStart: 0.0,
              mouseUpStart: 0.0,
              tDown: 0,
              tUp: 0
          };
          return wave;
      }

      function removeWaveFromScope(scope, wave) {
          if (scope.waves) {
              var pos = scope.waves.indexOf(wave);
              scope.waves.splice(pos, 1);
          }
      };

      // Shortcuts.
      var pow = Math.pow;
      var now = function () {
          return new Date().getTime();
      };

      // Quad beizer where t is between 0 and 1.
      function quadBezier(t, p0, p1, p2, p3) {
          return pow(1 - t, 3) * p0 +
              3 * pow(1 - t, 2) * t * p1 +
              (1 - t) * pow(t, 2) * p2 +
              pow(t, 3) * p3;
      }

      function easeIn(t) {
          return quadBezier(t, 0.4, 0.0, 1, 1);
      }

      function cssColorWithAlpha(cssColor, alpha) {
          var parts = cssColor.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
          if (typeof alpha == 'undefined') {
              alpha = 1;
          }
          if (!parts) {
              return 'rgba(255, 255, 255, ' + alpha + ')';
          }
          return 'rgba(' + parts[1] + ', ' + parts[2] + ', ' + parts[3] + ', ' + alpha + ')';
      }

      function dist(p1, p2) {
          return Math.sqrt(pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2));
      }

      function distanceFromPointToFurthestCorner(point, size) {
          var tl_d = dist(point, {x: 0, y: 0});
          var tr_d = dist(point, {x: size.w, y: 0});
          var bl_d = dist(point, {x: 0, y: size.h});
          var br_d = dist(point, {x: size.w, y: size.h});
          return Math.max(Math.max(tl_d, tr_d), Math.max(bl_d, br_d));
      }

      var impl = {

//       * The initial opacity set on the wave.
//       *
//       * @attribute initialOpacity
//       * @type number
//       * @default 0.25
//
          initialOpacity: 0.25,

//   * How fast (opacity per second) the wave fades out.
//   *
//   * @attribute opacityDecayVelocity
//   * @type number
//   * @default 0.5
//   *
          opacityDecayVelocity: 0.5,

//   * Touch down acceleration (px per second^2).
//   *
//   * @attribute touchDownAcceleration
//   * @type number
//   * @default 512.0
//
          touchDownAcceleration: 512.0,

//   * Touch up acceleration (px per second^2).
//   *
//   * @attribute touchUpAcceleration
//   * @type number
//   * @default 1024.0
//   *
          touchUpAcceleration: 1024.0,

          backgroundFill: true,
          pixelDensity: 2,

          eventDelegates: {
              down: 'downAction',
              up: 'upAction'
          },

          ready: function () {
              this.waves = [];
              this.canvas = canvas;
          },

          setupCanvas: function () {
              this.canvas.setAttribute('width', this.canvas.clientWidth * this.pixelDensity + "px");
              this.canvas.setAttribute('height', this.canvas.clientHeight * this.pixelDensity + "px");
              this.canvas.getContext('2d').scale(this.pixelDensity, this.pixelDensity);
          },

          downAction: function (touchX, touchY) {
              this.setupCanvas();
              var wave = createWave(this.canvas);

              this.cancelled = false;
              wave.isMouseDown = true;
              wave.tDown = 0.0;
              wave.tUp = 0.0;
              wave.mouseUpStart = 0.0;
              wave.mouseDownStart = now();

              var width = this.canvas.width / 2; // Retina canvas
              var height = this.canvas.height / 2;

//              var rect = this.canvas.getBoundingClientRect();
//              var touchX = (e.x || e.touches[0].pageX) - rect.left;
//              var touchY = (e.y || e.touches[0].pageY) - rect.top;

              wave.startPosition = {x: touchX, y: touchY};

//              if (this.canvas.classList.contains("recenteringTouch")) {
//                  wave.endPosition = {x: width / 2, y: height / 2};
//                  wave.slideDistance = dist(wave.startPosition, wave.endPosition);
//              }
              wave.containerSize = Math.max(width, height);
              wave.maxRadius = distanceFromPointToFurthestCorner(wave.startPosition, {w: width, h: height});
              this.waves.push(wave);
              requestAnimationFrame(this.animate.bind(this));
          },

//          getBoundingClientRect: function () {
//              return this.canvas.getBoundingClientRect();
//          },
          upAction: function () {
              for (var i = 0; i < this.waves.length; i++) {
                  // Declare the next wave that has mouse down to be mouse'ed up.
                  var wave = this.waves[i];
                  if (wave.isMouseDown) {
                      wave.isMouseDown = false
                      wave.mouseUpStart = now();
                      wave.mouseDownStart = 0;
                      wave.tUp = 0.0;
                      break;
                  }
              }
          },

          cancel: function () {
              this.cancelled = true;
          },

          animate: function () {
              var shouldRenderNextFrame = false;

              // Clear the canvas
              var ctx = this.canvas.getContext('2d');
              ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

              var deleteTheseWaves = [];
              // The oldest wave's touch down duration
              var longestTouchDownDuration = 0;
              var longestTouchUpDuration = 0;
              // Save the last known wave color
              var lastWaveColor = null;
              // wave animation values
              var anim = {
                  initialOpacity: this.initialOpacity,
                  opacityDecayVelocity: this.opacityDecayVelocity,
                  touchDownAcceleration: this.touchDownAcceleration,
                  touchUpAcceleration: this.touchUpAcceleration
              };

              for (var i = 0; i < this.waves.length; i++) {
                  var wave = this.waves[i];

                  if (wave.mouseDownStart > 0) {
                      wave.tDown = now() - wave.mouseDownStart;
                  }
                  if (wave.mouseUpStart > 0) {
                      wave.tUp = now() - wave.mouseUpStart;
                  }

                  // Determine how long the touch has been up or down.
                  var tUp = wave.tUp;
                  var tDown = wave.tDown;
                  longestTouchDownDuration = Math.max(longestTouchDownDuration, tDown);
                  longestTouchUpDuration = Math.max(longestTouchUpDuration, tUp);

                  // Obtain the instantenous size and alpha of the ripple.
                  var radius = waveRadiusFn(tDown, tUp, anim);
                  var waveAlpha = waveOpacityFn(tDown, tUp, anim);
                  var waveColor = cssColorWithAlpha(wave.waveColor, waveAlpha);
                  lastWaveColor = wave.waveColor;

                  // Position of the ripple.
                  var x = wave.startPosition.x;
                  var y = wave.startPosition.y;

                  // Ripple gravitational pull to the center of the canvas.
                  if (wave.endPosition) {

                      var translateFraction = waveGravityToCenterPercentageFn(tDown, tUp, wave.maxRadius);

                      // This translates from the origin to the center of the view  based on the max dimension of
                      var translateFraction = Math.min(1, radius / wave.containerSize * 2 / Math.sqrt(2));

                      x += translateFraction * (wave.endPosition.x - wave.startPosition.x);
                      y += translateFraction * (wave.endPosition.y - wave.startPosition.y);
                  }

                  // If we do a background fill fade too, work out the correct color.
                  var bgFillColor = null;
                  if (this.backgroundFill) {
                      var bgFillAlpha = waveOuterOpacityFn(tDown, tUp, anim);
                      bgFillColor = cssColorWithAlpha(wave.waveColor, bgFillAlpha);
                  }

                  // Draw the ripple.
                  drawRipple(this.canvas, x, y, radius, waveColor, bgFillColor);

                  // Determine whether there is any more rendering to be done.
                  var shouldRenderWaveAgain = !waveDidFinish(wave, radius, anim);
                  shouldRenderNextFrame = shouldRenderNextFrame || shouldRenderWaveAgain;
                  if (!shouldRenderWaveAgain || this.cancelled) {
                      deleteTheseWaves.push(wave);
                  }
              }

              if (shouldRenderNextFrame) {
                  requestAnimationFrame(this.animate.bind(this));
              } else {
                  // If there is nothing to draw, clear any drawn waves now because
                  // we're not going to get another requestAnimationFrame any more.
                  var ctx = this.canvas.getContext('2d');
                  ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
              }

              for (var i = 0; i < deleteTheseWaves.length; ++i) {
                  var wave = deleteTheseWaves[i];
                  removeWaveFromScope(this, wave);
              }
          }

      };

      impl.ready();

      return impl;

  }-*/;
}
