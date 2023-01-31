/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.labymod.addons.customcrosshair.canvas;

public class CrosshairCanvas {

  public static final int SIZE = 15;
  public static final int TOTAL_SIZE = SIZE * SIZE;
  private static final String X_OUT_OF_BOUNDS = "x is %s but must be smaller than %s";
  private static final String Y_OUT_OF_BOUNDS = "y is %s but must be smaller than %s";
  private final boolean[] pixels;

  public CrosshairCanvas() {
    this.pixels = new boolean[TOTAL_SIZE];
  }

  public CrosshairCanvas enablePixel(int pixel) {
    this.checkTotalRange(pixel);
    this.pixels[pixel] = true;
    return this;
  }

  public CrosshairCanvas disablePixel(int pixel) {
    this.checkTotalRange(pixel);
    this.pixels[pixel] = false;
    return this;
  }

  public CrosshairCanvas togglePixel(int pixel) {
    if (this.isPixelActive(pixel)) {
      this.disablePixel(pixel);
    } else {
      this.enablePixel(pixel);
    }

    return this;
  }

  public CrosshairCanvas togglePixel(int x, int y) {
    this.checkRange(x, X_OUT_OF_BOUNDS, x, SIZE);
    this.checkRange(y, Y_OUT_OF_BOUNDS, y, SIZE);
    return this.togglePixel(x * y);
  }

  public CrosshairCanvas enablePixel(int x, int y) {
    this.checkRange(x, X_OUT_OF_BOUNDS, x, SIZE);
    this.checkRange(y, Y_OUT_OF_BOUNDS, y, SIZE);
    return this.enablePixel(x + SIZE * y);
  }

  public CrosshairCanvas enableFromCenter(int x, int y) {
    int yFromCenter = 7 + y;
    int xFromCenter = 7 + x;
    this.checkRange(xFromCenter, X_OUT_OF_BOUNDS, xFromCenter, SIZE);
    this.checkRange(yFromCenter, Y_OUT_OF_BOUNDS, yFromCenter, SIZE);
    return this.enablePixel(xFromCenter, yFromCenter);
  }

  public CrosshairCanvas disablePixel(int x, int y) {
    this.checkRange(x, X_OUT_OF_BOUNDS, x, SIZE);
    this.checkRange(y, Y_OUT_OF_BOUNDS, y, SIZE);
    return this.disablePixel(x + SIZE * y);
  }

  public CrosshairCanvas disableFromCenter(int x, int y) {
    int yFromCenter = 7 + y;
    int xFromCenter = 7 + x;
    this.checkRange(xFromCenter, X_OUT_OF_BOUNDS, xFromCenter, SIZE);
    this.checkRange(yFromCenter, Y_OUT_OF_BOUNDS, yFromCenter, SIZE);
    return this.disablePixel(xFromCenter, yFromCenter);
  }

  public boolean isPixelActive(int pixel) {
    this.checkTotalRange(pixel);
    return this.pixels[pixel];
  }

  public boolean isPixelActive(int x, int y) {
    this.checkRange(x, X_OUT_OF_BOUNDS, x, SIZE);
    this.checkRange(y, Y_OUT_OF_BOUNDS, y, SIZE);
    return this.isPixelActive(x * y);
  }

  public CrosshairCanvas copy() {
    CrosshairCanvas canvas = new CrosshairCanvas();
    System.arraycopy(this.pixels, 0, canvas.pixels, 0, this.pixels.length);
    return canvas;
  }

  public boolean[] getPixels() {
    return this.pixels;
  }

  private void checkRange(int value, String message, Object... arguments) {
    if (value < 0 || value >= SIZE) {
      throw new IllegalArgumentException(String.format(message, arguments));
    }
  }

  private void checkTotalRange(int value) {
    if (value < 0 || value >= TOTAL_SIZE) {
      throw new IllegalArgumentException(
          "Pixel " + value + " is out of bounds (min: 0, max: " + TOTAL_SIZE + ")");
    }
  }
}
