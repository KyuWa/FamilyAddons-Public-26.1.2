package org.kyowa.familyaddons.features

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
                .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                .createRenderSetup()
        )
    }

    /** The same lines as [LINES]: this build has no see-through variant. */
    val LINES_NO_DEPTH: RenderType get() = LINES

    // 26.1 dropped the public textured beacon-beam RenderType factory, so the
    // beam is drawn as position+color quads (no texture) via DEBUG_QUADS.
    /**
     * Beacon columns, the way they have always been: a marker you can see from
     * anywhere, so this one draws over the world on purpose.
     */
    val BEAM: RenderType by lazy {
        RenderType.create(
            "familyaddons_beam",
            RenderSetup.builder(RenderPipelines.DEBUG_QUADS)
                .setOutputTarget(OutputTarget.MAIN_TARGET)
                .createRenderSetup()
        )
    }
}
