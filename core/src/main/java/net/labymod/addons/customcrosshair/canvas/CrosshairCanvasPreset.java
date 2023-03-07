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

public enum CrosshairCanvasPreset {
  DEFAULT(new CrosshairCanvas()
      // vertical line
      .enableFromCenter(0, -4)
      .enableFromCenter(0, -3)
      .enableFromCenter(0, -2)
      .enableFromCenter(0, -1)
      .enableFromCenter(0, 0)
      .enableFromCenter(0, 1)
      .enableFromCenter(0, 2)
      .enableFromCenter(0, 3)
      .enableFromCenter(0, 4)
      // horizontal line
      .enableFromCenter(-4, 0)
      .enableFromCenter(-3, 0)
      .enableFromCenter(-2, 0)
      .enableFromCenter(-1, 0)
      .enableFromCenter(1, 0)
      .enableFromCenter(2, 0)
      .enableFromCenter(3, 0)
      .enableFromCenter(4, 0)
  ),
  DEFAULT_WITH_DOT(DEFAULT.getCanvas().copy()
      .disableFromCenter(-1, 0)
      .disableFromCenter(0, 1)
      .disableFromCenter(1, 0)
      .disableFromCenter(0, -1)
  ),
  DEFAULT_WITH_GAP(DEFAULT_WITH_DOT.getCanvas().copy()
      .disableFromCenter(0, 0)

      // make default lines longer
      .enableFromCenter(-5, 0)
      .enableFromCenter(0, -5)
      .enableFromCenter(5, 0)
      .enableFromCenter(0, 5)
  ),
  CROSS(DEFAULT.getCanvas().copy()
      // top left
      .enableFromCenter(-3, -2)
      .enableFromCenter(-3, -3)
      .enableFromCenter(-2, -3)
      // top right
      .enableFromCenter(2, -3)
      .enableFromCenter(3, -3)
      .enableFromCenter(3, -2)
      // bottom right
      .enableFromCenter(3, 2)
      .enableFromCenter(3, 3)
      .enableFromCenter(2, 3)
      // bottom left
      .enableFromCenter(-2, 3)
      .enableFromCenter(-3, 3)
      .enableFromCenter(-3, 2)
  ),
  CROSS_WITH_DOT(CROSS.getCanvas().copy()
      .disableFromCenter(-1, 0)
      .disableFromCenter(0, 1)
      .disableFromCenter(1, 0)
      .disableFromCenter(0, -1)
  ),
  CROSS_WITH_GAP(CROSS_WITH_DOT.getCanvas().copy()
      .disableFromCenter(0, 0)
  ),
  CIRCLE(new CrosshairCanvas()
      .enableFromCenter(-3, 0)
      .enableFromCenter(-3, -1)
      .enableFromCenter(-2, -2)
      .enableFromCenter(-1, -3)

      .enableFromCenter(0, -3)
      .enableFromCenter(1, -3)
      .enableFromCenter(2, -2)
      .enableFromCenter(3, -1)

      .enableFromCenter(3, 0)
      .enableFromCenter(3, 1)
      .enableFromCenter(2, 2)
      .enableFromCenter(1, 3)

      .enableFromCenter(0, 3)
      .enableFromCenter(-1, 3)
      .enableFromCenter(-2, 2)
      .enableFromCenter(-3, 1)
  ),
  CIRCLE_WITH_DOT(CIRCLE.getCanvas().copy()
      .enableFromCenter(0, 0)
  ),
  CROSSHAIR(DEFAULT_WITH_GAP.getCanvas().copy()
      // left vertical line
      .enableFromCenter(-4, 2)
      .enableFromCenter(-4, 1)
      .enableFromCenter(-4, -1)
      .enableFromCenter(-4, -2)

      // top horizontal line
      .enableFromCenter(-2, -4)
      .enableFromCenter(-1, -4)
      .enableFromCenter(1, -4)
      .enableFromCenter(2, -4)

      // right vertical line
      .enableFromCenter(4, -2)
      .enableFromCenter(4, -1)
      .enableFromCenter(4, 1)
      .enableFromCenter(4, 2)

      // bottom horizontal line
      .enableFromCenter(-2, 4)
      .enableFromCenter(-1, 4)
      .enableFromCenter(1, 4)
      .enableFromCenter(2, 4)

      // outer line connectors
      .enableFromCenter(-3, -3)
      .enableFromCenter(3, -3)
      .enableFromCenter(3, 3)
      .enableFromCenter(-3, 3)
  ),
  CROSSHAIR_WITH_DOT(CROSSHAIR.getCanvas().copy()
      .enableFromCenter(0, 0)
  ),
  ;

  private static final CrosshairCanvasPreset[] VALUES = values();

  private final CrosshairCanvas canvas;

  CrosshairCanvasPreset(final CrosshairCanvas canvas) {
    this.canvas = canvas;
  }

  public CrosshairCanvas getCanvas() {
    return this.canvas;
  }

  public static CrosshairCanvasPreset of(final CrosshairCanvas canvas) {
    for (final CrosshairCanvasPreset value : VALUES) {
      if (value.getCanvas().equals(canvas)) {
        return value;
      }
    }

    return null;
  }

  public static CrosshairCanvasPreset[] getValues() {
    return VALUES;
  }
}
