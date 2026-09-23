package org.kyowa.familyaddons.features

import com.mojang.blaze3d.pipeline.DepthStencilState
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.CompareOp
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.rendertype.LayeringTransform
import net.minecraft.client.renderer.rendertype.OutputTarget
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.resources.Identifier
import org.kyowa.familyaddons.FamilyAddons

object FamilyRenderTypes {

    val LINES: RenderType by lazy {
        RenderType.create(
            "familyaddons_lines",
            RenderSetup.builder(RenderPipelines.LINES)
                .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                .setOutputTarget(OutputTarget.MAIN_TARGET)
                .createRenderSetup()
        )
    }

    /** The same lines as [LINES]: this build has no see-through variant. */
    val LINES_NO_DEPTH: RenderType get() = LINES

    // Solid-color translucent quad layer for beacon-beam columns. 26.1 dropped the
    // public textured beacon-beam RenderType factory, so the beam is drawn as
    // position+color quads (no texture) via the DEBUG_QUADS pipeline.
    /** The debug quad pipeline draws without a depth test, so this one adds it. */
    private val BEAM_PIPELINE: RenderPipeline by lazy {
        try {
            val base = RenderPipelines.DEBUG_QUADS
            val builder = RenderPipeline.builder()
                .withLocation(Identifier.fromNamespaceAndPath("familyaddons", "pipeline/beam_depth"))
                .withVertexShader(base.vertexShader)
                .withFragmentShader(base.fragmentShader)
                .withVertexFormat(base.vertexFormat, base.vertexFormatMode)
                .withColorTargetState(base.colorTargetState)
                .withCull(base.isCull)
                .withPolygonMode(base.polygonMode)
                .withDepthStencilState(DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
            base.samplers.forEach { builder.withSampler(it) }
            base.uniforms.forEach { u ->
                val tf = u.textureFormat()
                if (tf != null) builder.withUniform(u.name(), u.type(), tf)
                else builder.withUniform(u.name(), u.type())
            }
            base.shaderDefines.flags().forEach { builder.withShaderDefine(it) }
            base.shaderDefines.values().forEach { (k, v) ->
                v.toIntOrNull()?.let { builder.withShaderDefine(k, it) }
                    ?: v.toFloatOrNull()?.let { builder.withShaderDefine(k, it) }
            }
            builder.build()
        } catch (e: Exception) {
            FamilyAddons.LOGGER.warn("Couldn't build the depth-tested beam pipeline", e)
            RenderPipelines.DEBUG_QUADS
        }
    }

    val BEAM: RenderType by lazy {
        RenderType.create(
            "familyaddons_beam",
            RenderSetup.builder(BEAM_PIPELINE)
                .setOutputTarget(OutputTarget.MAIN_TARGET)
                .createRenderSetup()
        )
    }
}
