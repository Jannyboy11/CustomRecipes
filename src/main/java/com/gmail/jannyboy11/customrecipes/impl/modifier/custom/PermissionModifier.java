package com.gmail.jannyboy11.customrecipes.impl.modifier.custom;

import java.util.Objects;

import org.bukkit.entity.Player;

import com.gmail.jannyboy11.customrecipes.api.modifier.CraftingModifier;
import net.minecraft.server.v1_12_R1.IRecipe;

public class PermissionModifier extends AbstractCraftingModifier<IRecipe, IRecipe> {
    
    private final String permission;
    
    public PermissionModifier(String permission) {
        this.permission = Objects.requireNonNull(permission);
    }

    @Override
    public IRecipe modify(IRecipe base) {
        return new ProxyRecipe((inventoryCrafting, world) -> {
            if (inventoryCrafting.getOwner() instanceof Player) {
                Player player = (Player) inventoryCrafting.getOwner();
                if (!player.hasPermission(permission)) return false;
            }
            
            return base.a(inventoryCrafting, world);
        },
                base::craftItem,
                base::b,
                base::b,
                base::d,
                base::c,
                base::toBukkitRecipe,
                base::setKey);
    }

    @Override
    protected CraftingModifier createBukkitModifier() {
        // TODO Auto-generated method stub
        return null;
    }

}
