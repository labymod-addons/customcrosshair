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

package net.labymod.addons.customcrosshair.util;

public enum Rotation {
  NONE(0),
  FORTY_FIVE(45),
  NINETY(90),
  ONE_THIRTY_FIVE(135),
  ONE_EIGHTY(180),
  TWO_TWENTY_FIVE(225),
  TWO_SEVENTY(270),
  THREE_FIFTEEN(315),
  ;

  private final int degrees;

  Rotation(int degrees) {
    this.degrees = degrees;
  }

  public int getDegrees() {
    return this.degrees;
  }
}
