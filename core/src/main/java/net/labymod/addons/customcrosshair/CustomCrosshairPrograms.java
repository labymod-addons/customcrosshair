package net.labymod.addons.customcrosshair;

import net.labymod.api.laby3d.pipeline.RenderStates;
import net.labymod.laby3d.api.pipeline.RenderState;
import net.labymod.laby3d.api.pipeline.blend.DefaultBlendFunctions;
import net.labymod.laby3d.api.resource.AssetId;

public final class CustomCrosshairPrograms {

  public static final RenderState VANILLA_BLENDED = RenderState.builder(RenderStates.GUI_TEXTURED.toSnippet())
      .setId(AssetId.of(CustomCrosshair.NAMESPACE, "state/vanilla_blended"))
      .setBlendFunction(DefaultBlendFunctions.INVERT)
      .build();

}
