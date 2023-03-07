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

import java.util.Base64;
import net.labymod.addons.customcrosshair.misc.Canvas;

public class CrosshairCanvas extends Canvas<CrosshairCanvas> {

  public static final String ENCODED_PREFIX = "LMCH-";
  public static final String ENCODED_LUNAR_PREFIX = "LCCH-";
  public static final int SIZE = 15;

  public CrosshairCanvas() {
    super(SIZE, SIZE);
  }

  public static String encode(final CrosshairCanvas canvas) {
    final int size = CrosshairCanvas.getSize(canvas);
    final byte[] bytes = new byte[(int) (Math.ceil(size * size / 8.0F) + 1)];
    bytes[0] = (byte) size;

    final int canvasOffset = (int) Math.floor((SIZE - size) / 2.0F);
    for (int canvasY = 0; canvasY < size; ++canvasY) {
      for (int canvasX = 0; canvasX < size; ++canvasX) {
        final int canvasIndex = canvasX + canvasOffset + (canvasY + canvasOffset) * SIZE;
        if (!canvas.isPixelActive(canvasIndex)) {
          continue;
        }

        final int index = canvasX + canvasY * size;
        bytes[index / 8 + 1] |= 1 << index % 8;
      }
    }

    return ENCODED_PREFIX + Base64.getEncoder().encodeToString(bytes);
  }

  public static CrosshairCanvas decode(final String encoded) {
    if (!encoded.startsWith(ENCODED_PREFIX)) {
      return CrosshairCanvas.decodeLunarCrosshair(encoded);
    }

    final byte[] bytes = Base64.getDecoder().decode(
        encoded.substring(ENCODED_PREFIX.length()).getBytes()
    );

    return CrosshairCanvas.parseEncoded(bytes[0], bytes, 1);
  }

  public static CrosshairCanvas decodeLunarCrosshair(final String encoded) {
    if (!encoded.startsWith(ENCODED_LUNAR_PREFIX)) {
      return null;
    }

    final String[] split = encoded.trim().split("-");
    if (split.length != 3) {
      return null;
    }

    final int size;
    try {
      size = Integer.parseInt(split[1]);
    } catch (final NumberFormatException e) {
      return null;
    }

    final byte[] bytes = Base64.getDecoder().decode(split[2]);
    return CrosshairCanvas.parseEncoded(size, bytes, 0);
  }

  private static CrosshairCanvas parseEncoded(
      final int size,
      final byte[] bytes,
      final int offset
  ) {
    final int canvasOffset = (int) Math.floor((SIZE - size) / 2.0F);
    final CrosshairCanvas canvas = new CrosshairCanvas();
    for (int canvasY = 0; canvasY < size; ++canvasY) {
      for (int canvasX = 0; canvasX < size; ++canvasX) {
        final int canvasIndex = canvasX + canvasOffset + (canvasY + canvasOffset) * SIZE;
        if (canvasIndex < 0 || canvasIndex >= SIZE * SIZE) {
          continue;
        }

        final int index = canvasX + canvasY * size;
        final int bit = 1 << index % 8;
        if ((bytes[index / 8 + offset] & bit) != 0) {
          canvas.enablePixel(canvasIndex);
        }
      }
    }

    return canvas;
  }

  private static int getSize(final CrosshairCanvas canvas) {
    final boolean[] pixels = canvas.getPixels();
    final int centerX = canvas.getCenterX();
    final int centerY = canvas.getCenterY();

    int minX = centerX;
    int maxX = centerX;
    int minY = centerY;
    int maxY = centerY;
    for (int canvasY = 0; canvasY < SIZE; canvasY++) {
      for (int canvasX = 0; canvasX < SIZE; canvasX++) {
        if (!pixels[canvasY * SIZE + canvasX]) {
          continue;
        }

        if (canvasX < minX) {
          minX = canvasX;
        }

        if (canvasX > maxX) {
          maxX = canvasX;
        }

        if (canvasY < minY) {
          minY = canvasY;
        }

        if (canvasY > maxY) {
          maxY = canvasY;
        }
      }
    }

    final int horizontalSize = Math.max(centerX - minX, maxX - centerX);
    final int verticalSize = Math.max(centerY - minY, maxY - centerY);
    return Math.max(horizontalSize, verticalSize) * 2 + 1;
  }

  @Override
  public CrosshairCanvas copy() {
    final CrosshairCanvas canvas = new CrosshairCanvas();
    this.copyPixelsTo(canvas);
    return canvas;
  }
}
