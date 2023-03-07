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

package net.labymod.addons.customcrosshair.misc;

import java.util.Arrays;
import java.util.Objects;

public abstract class Canvas<T extends Canvas<T>> {

  private static final String X_OUT_OF_BOUNDS = "x is %s but must be smaller than %s";
  private static final String Y_OUT_OF_BOUNDS = "y is %s but must be smaller than %s";

  private final boolean[] pixels;
  private final int width;
  private final int height;
  private final int centerX;
  private final int centerY;
  private final int totalSize;

  protected Canvas(final int width, final int height) {
    this.width = width;
    this.height = height;
    this.totalSize = width * height;
    this.pixels = new boolean[this.totalSize];

    this.centerX = (int) Math.floor(width / 2D);
    this.centerY = (int) Math.floor(height / 2D);
  }

  public T enablePixel(final int pixel) {
    this.checkTotalRange(pixel);
    this.pixels[pixel] = true;
    return (T) this;
  }

  public T disablePixel(final int pixel) {
    this.checkTotalRange(pixel);
    this.pixels[pixel] = false;
    return (T) this;
  }

  public T togglePixel(final int pixel) {
    if (this.isPixelActive(pixel)) {
      this.disablePixel(pixel);
    } else {
      this.enablePixel(pixel);
    }

    return (T) this;
  }

  public T togglePixel(final int x, final int y) {
    this.checkRange(x, X_OUT_OF_BOUNDS, this.width);
    this.checkRange(y, Y_OUT_OF_BOUNDS, this.height);
    return this.togglePixel(x + this.height * y);
  }

  public T enablePixel(final int x, final int y) {
    this.checkRange(x, X_OUT_OF_BOUNDS, this.width);
    this.checkRange(y, Y_OUT_OF_BOUNDS, this.height);
    return this.enablePixel(x + this.height * y);
  }

  public T enableFromCenter(final int x, final int y) {
    final int xFromCenter = this.centerX + x;
    final int yFromCenter = this.centerY + y;
    this.checkRange(xFromCenter, X_OUT_OF_BOUNDS, this.width);
    this.checkRange(yFromCenter, Y_OUT_OF_BOUNDS, this.height);
    return this.enablePixel(xFromCenter, yFromCenter);
  }

  public T disablePixel(final int x, final int y) {
    this.checkRange(x, X_OUT_OF_BOUNDS, this.width);
    this.checkRange(y, Y_OUT_OF_BOUNDS, this.height);
    return this.disablePixel(x + this.height * y);
  }

  public T disableFromCenter(final int x, final int y) {
    final int xFromCenter = this.centerX + x;
    final int yFromCenter = this.centerY + y;
    this.checkRange(xFromCenter, X_OUT_OF_BOUNDS, this.width);
    this.checkRange(yFromCenter, Y_OUT_OF_BOUNDS, this.height);
    return this.disablePixel(xFromCenter, yFromCenter);
  }

  public boolean isPixelActive(final int pixel) {
    this.checkTotalRange(pixel);
    return this.pixels[pixel];
  }

  public boolean isPixelActive(final int x, final int y) {
    this.checkRange(x, X_OUT_OF_BOUNDS, this.width);
    this.checkRange(y, Y_OUT_OF_BOUNDS, this.height);
    return this.isPixelActive(x + this.height * y);
  }

  public boolean[] getPixels() {
    return this.pixels;
  }

  public abstract T copy();

  public int getCenterX() {
    return this.centerX;
  }

  public int getCenterY() {
    return this.centerY;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  protected void checkRange(final int value, final String message, final int maxValue) {
    if (value < 0 || value >= maxValue) {
      throw new IllegalArgumentException(String.format(message, value, maxValue));
    }
  }

  protected void checkTotalRange(final int value) {
    if (value < 0 || value >= this.totalSize) {
      throw new IllegalArgumentException(
          "Pixel " + value + " is out of bounds (min: 0, max: " + this.totalSize + ")");
    }
  }

  public void copyPixelsTo(final Canvas<?> canvas) {
    System.arraycopy(this.pixels, 0, canvas.pixels, 0, this.pixels.length);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Canvas)) {
      return false;
    }

    final Canvas<?> canvas = (Canvas<?>) o;
    return this.width == canvas.width && this.height == canvas.height && Arrays.equals(this.pixels,
        canvas.pixels);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(this.width, this.height);
    result = 31 * result + Arrays.hashCode(this.pixels);
    return result;
  }
}
