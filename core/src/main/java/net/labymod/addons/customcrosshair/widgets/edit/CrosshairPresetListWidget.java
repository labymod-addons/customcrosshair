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

package net.labymod.addons.customcrosshair.widgets.edit;

import net.labymod.addons.customcrosshair.widgets.CrosshairCanvasRendererWidget;
import net.labymod.api.client.gui.lss.property.LssProperty;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.AutoAlignType;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.BoundsType;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.ListWidget;
import net.labymod.api.util.bounds.ModifyReason;
import net.labymod.api.util.bounds.ReasonableMutableRectangle;
import net.labymod.api.util.bounds.Rectangle;

@AutoWidget
public class CrosshairPresetListWidget extends ListWidget<CrosshairCanvasRendererWidget> {

  private static final ModifyReason MODIFY_REASON = ModifyReason.of("CrosshairPresetListWidget");
  private final LssProperty<Float> spaceBetweenEntries = new LssProperty<>(0.0F);

  @Override
  public void onBoundsChanged(final Rectangle previousRect, final Rectangle newRect) {
    super.onBoundsChanged(previousRect, newRect);

    // Update the bounds of the entries
    this.updateChildren();
  }

  protected void updateChildren() {
    final Bounds bounds = this.bounds();
    final float maxX = bounds.getMaxX();

    final float startX = bounds.getX();
    final float startY = bounds.getY();
    float x = startX;
    float y = startY;

    float rowHeight = 0.0F;
    for (final CrosshairCanvasRendererWidget child : this.children) {
      final ReasonableMutableRectangle childBounds = child.bounds().rectangle(BoundsType.OUTER);
      final float childWidth = childBounds.getWidth();
      if (x + childWidth > maxX) {
        x = startX;
        y += rowHeight + this.spaceBetweenEntries.get();
      }

      childBounds.setPosition(x, y, MODIFY_REASON);
      rowHeight = Math.max(childBounds.getHeight(), rowHeight);
      x += childWidth + this.spaceBetweenEntries.get();
    }

    bounds.setHeight(y - startY + rowHeight, MODIFY_REASON);
  }

  public LssProperty<Float> spaceBetweenEntries() {
    return this.spaceBetweenEntries;
  }

  @Override
  public boolean hasAutoBounds(final Widget child, final AutoAlignType type) {
    return type == AutoAlignType.POSITION;
  }
}
