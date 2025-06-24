package net.labymod.addons.customcrosshair;

import net.labymod.api.client.gfx.pipeline.program.BlendFunction;
import net.labymod.api.client.gfx.pipeline.program.DestFactor;
import net.labymod.api.client.gfx.pipeline.program.RenderProgram;
import net.labymod.api.client.gfx.pipeline.program.RenderPrograms;
import net.labymod.api.client.gfx.pipeline.program.ShaderConfig.UniformType;
import net.labymod.api.client.gfx.pipeline.program.SourceFactor;
import net.labymod.api.client.gfx.vertex.VertexFormats;

public final class CustomCrosshairPrograms {

  public static final RenderProgram VANILLA_BLENDED = RenderProgram.builder()
      .withId(CustomCrosshair.withDefaultNamespace("pipeline/vanilla_blended"))
      .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR)
      .withBlendFunction(new BlendFunction(SourceFactor.ONE_MINUS_DST_COLOR, DestFactor.ONE_MINUS_SRC_COLOR, SourceFactor.ONE, DestFactor.ZERO))
      .withShader(
          builder -> builder
              .withVertexShader(RenderPrograms.SHADER_RESOURCE_PATH.apply("vertex/position_texture_color.vsh"))
              .withFragmentShader(RenderPrograms.SHADER_RESOURCE_PATH.apply("vertex/position_texture_color.fsh"))
              .withSampler("DiffuseSampler", 0)
              .withUniform("ColorModulator", UniformType.VEC4),
          RenderPrograms.MATRICES_SNIPPET
      ).build();

}
