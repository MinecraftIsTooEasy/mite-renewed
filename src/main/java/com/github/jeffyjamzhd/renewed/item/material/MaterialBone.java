package com.github.jeffyjamzhd.renewed.item.material;

import huix.glacier.api.extension.material.GlacierMaterial;
import huix.glacier.api.extension.material.IArrowMaterial;
import huix.glacier.api.extension.material.IToolMaterial;

public class MaterialBone extends GlacierMaterial implements IToolMaterial, IArrowMaterial {
    public MaterialBone() {
        super(EnumMaterials.bone);
    }

    @Override
    public float getChanceOfRecovery() {
        return 0.6F;
    }

    @Override
    public float getHarvestEfficiency() {
        return 2.0F;
    }


}
