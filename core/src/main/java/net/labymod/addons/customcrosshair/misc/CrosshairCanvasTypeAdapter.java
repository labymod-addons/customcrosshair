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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.labymod.addons.customcrosshair.canvas.CrosshairCanvas;

public class CrosshairCanvasTypeAdapter implements JsonDeserializer<CrosshairCanvas>,
    JsonSerializer<CrosshairCanvas> {

  @Override
  public CrosshairCanvas deserialize(
      final JsonElement json,
      final Type typeOfT,
      final JsonDeserializationContext context
  ) throws JsonParseException {
    return CrosshairCanvas.decode(json.getAsString());
  }

  @Override
  public JsonElement serialize(
      final CrosshairCanvas canvas,
      final Type typeOfSrc,
      final JsonSerializationContext context
  ) {
    return new JsonPrimitive(CrosshairCanvas.encode(canvas));
  }
}
