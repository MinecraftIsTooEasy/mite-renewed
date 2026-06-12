package com.github.jeffyjamzhd.renewed.mixins.backpack;

import com.github.jeffyjamzhd.renewed.api.inventory.BackpackInventory;
import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(GuiContainer.class)
public abstract class GuiContainerMixin extends GuiScreen {
    /**
     * EMI widget texture for backpack tooltip rendering
     */
    @Unique
    private static final ResourceLocation WIDGET_TEXURE =
            new ResourceLocation("emi:textures/gui/widgets.png");

    @Unique private static final int INTERACTION_INSERT = 0;
    @Unique private static final int INTERACTION_EXTRACT = 1;

    /**
     * Array of modified slots, to prevent unnecessary method calls
     * when dragging backpacks. Key is slot, value is if it should be rendered
     */
    @Unique private final HashMap<Slot, Boolean> modifiedSlots = new HashMap<>();

    @Unique private int interactionType = -1;

    @Shadow
    private long field_94070_G;
    @Shadow private Slot theSlot;

    @Shadow protected static RenderItem itemRenderer;
    @Shadow protected boolean field_94076_q;

    @Shadow public Container inventorySlots;

    @Shadow public abstract void handleMouseClick(Slot par1Slot, int par2, int par3, int par4);
    @Shadow protected abstract void drawSlotInventory(Slot par1Slot);
    @Shadow public abstract Slot getSlotAtPosition(int par1, int par2);

    @Inject(method = "mouseClicked", at = @At(
            value = "HEAD"))
    private void mr$addExtendedClickFunctionality(
            int x, int y,
            int clickType, CallbackInfo ci) {
        // Initialize drag data
        modifiedSlots.clear();
        interactionType = -1;
    }

    @Inject(method = "mouseMovedOrUp", at = @At(
            value = "HEAD"), cancellable = true)
    private void mr$addExtendedFunctionality(
            int x, int y, int clickType,
            CallbackInfo ci) {
        // Stop if this is the end of a drag
        if (!modifiedSlots.isEmpty()) {
            ci.cancel();
            mr$handleStack();
        }
    }

    @Inject(method = "mouseClickMove", at = @At(value = "HEAD"), cancellable = true)
    private void mr$addExtendedDragFunctionality(
            int x, int y, int clickType,
            long unused, CallbackInfo ci) {
        // Get slot and item
        Slot slot = this.getSlotAtPosition(x, y);
        ItemStack cursorStack = this.mc.thePlayer.inventory.getItemStack();

        if (slot != null && cursorStack != null && cursorStack.getItem() instanceof ItemWithInventory) {
            // Run click interaction for each new slot the drag reaches
            boolean mouseHeld = this.field_94076_q && clickType == 1;
            boolean modifiedSlot = modifiedSlots.containsKey(slot);
            boolean isValid = slot.isItemValid(cursorStack);
            boolean canDrag = this.inventorySlots.canDragIntoSlot(slot);

            if (canDrag && mouseHeld && isValid && !modifiedSlot) {
                // Determine interaction type for this drag
                if (interactionType == -1) {
                    interactionType = isShiftKeyDown() ? INTERACTION_INSERT : INTERACTION_EXTRACT;
                }

                // Do not interact with other backpack items
                if (slot.getHasStack() && slot.getStack().getItem() instanceof ItemWithInventory) {
                    modifiedSlots.put(slot, false);
                    ci.cancel();
                    return;
                }

                // Handle interaction
                if (interactionType == INTERACTION_INSERT) {
                    modifiedSlots.put(slot, slot.getHasStack());
                    if (slot.getHasStack()) {
                        this.handleMouseClick(slot, slot.slotIndex, 1, 0);
                    }
                } else if (interactionType == INTERACTION_EXTRACT) {
                    modifiedSlots.put(slot, !slot.getHasStack());
                    if (!slot.getHasStack()) {
                        this.handleMouseClick(slot, slot.slotIndex, 1, 1);
                    }
                }
            }

            ci.cancel();
        }
    }

    @Inject(method = "drawScreen", at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V",
            shift = At.Shift.AFTER,
            ordinal = 0
    ), cancellable = true)
    private void mr$drawTooltipWithItem(CallbackInfo ci, @Local(ordinal = 0, argsOnly = true) int x, @Local(ordinal = 1, argsOnly = true)  int y) {
        ItemStack cursorStack = mc.thePlayer.inventory.getItemStack();
        if (cursorStack != null && cursorStack.getItem() instanceof ItemWithInventory inv) {
            if (inv.hasProperCompoundTag(cursorStack)) {
                this.mr$renderTooltipWithStackContents(cursorStack, x, y);
                ci.cancel();
                return;
            }
        }

        if (this.theSlot != null && this.theSlot.getHasStack()) {
            ItemStack stackOver = this.theSlot.getStack();
            if (stackOver.getItem() instanceof ItemWithInventory inv && inv.hasProperCompoundTag(stackOver)) {
                this.mr$renderTooltipWithStackContents(stackOver, x, y);
                ci.cancel();
            }
        }
    }

    @Inject(method = "drawSlotInventory", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/InventoryPlayer;getItemStack()Lnet/minecraft/ItemStack;"))
    private void setHighlight(
            Slot slot, CallbackInfo ci,
            @Local(ordinal = 0) int x,
            @Local(ordinal = 1) int y) {
        if (modifiedSlots.containsKey(slot) && this.inventorySlots.canDragIntoSlot(slot)) {
            if (modifiedSlots.get(slot))
                drawRect(x, y, x + 16, y + 16, -2130706433);
        }
    }

    @Unique
    private void mr$handleStack() {
        // Clear modified slots
        modifiedSlots.clear();

        // Reset click state (click no longer held)
        if (Minecraft.getMinecraft()
                .thePlayer.inventory.getItemStack() == null) {
            this.field_94070_G = 0L;
        }
        this.field_94076_q = false;
    }

    @Unique
    private void mr$renderTooltipWithStackContents(ItemStack stack, int x, int y) {
        // Declare variables
        @SuppressWarnings(value = "unchecked")
        List<String> tooltipList = stack.getTooltip(mc.thePlayer, false, theSlot);

        ItemWithInventory invItem = (ItemWithInventory) stack.getItem();
        BackpackInventory inv = invItem.createInventory(stack);
        int gridX = invItem.getGridX(stack);
        int gridY = invItem.getGridY(stack);

        int invSize = inv.getSizeInventory();
        int invSelected = inv.currentSlotID;
        int posX = x + 12;
        int posY = y - 12;
        int width, height;

        // Prepare for render
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderHelper.disableStandardItemLighting();

        // Precalculate width
        width = (Math.min(gridX, invSize)) * 18 + 2;
        for (String string : tooltipList)
            width = Math.max(width, fontRenderer.getStringWidth(string));

        height = 5;
        int size = Math.max(invSize, 1);
        while (size > 0) {
            size -= gridX;
            height += 18;
        }
        height += tooltipList.size() * 9;

        // Handle screen margins
        if (posX + width > this.width) {
            posX -= 28 + width;
        }
        if (posY + height + 6 > this.height) {
            posY = this.height - height - 6;
        }
        this.zLevel = 300F;
        itemRenderer.zLevel = 300F;
        int color = -267386864;

        // Draw rect
        this.drawGradientRect(posX - 3, posY - 4, posX + width + 3, posY - 3, color, color);
        this.drawGradientRect(posX - 3, posY + height + 3, posX + width + 3, posY + height + 4, color, color);
        this.drawGradientRect(posX - 3, posY - 3, posX + width + 3, posY + height + 3, color, color);
        this.drawGradientRect(posX - 4, posY - 3, posX - 3, posY + height + 3, color, color);
        this.drawGradientRect(posX + width + 3, posY - 3, posX + width + 4, posY + height + 3, color, color);
        int var10 = 0x505000FF;
        int var11 = (var10 & 0xFEFEFE) >> 1 | var10 & 0xFF000000;
        this.drawGradientRect(posX - 3, posY - 3 + 1, posX - 3 + 1, posY + height + 3 - 1, var10, var11);
        this.drawGradientRect(posX + width + 2, posY - 3 + 1, posX + width + 3, posY + height + 3 - 1, var10, var11);
        this.drawGradientRect(posX - 3, posY - 3, posX + width + 3, posY - 3 + 1, var10, var10);
        this.drawGradientRect(posX - 3, posY + height + 2, posX + width + 3, posY + height + 3, var11, var11);

        // Draw tooltip strings
        for (int i = 0; i < tooltipList.size(); i++) {
            if (i == 0) {
                this.fontRenderer.drawStringWithShadow(tooltipList.get(i), posX, posY, -1);
                continue;
            }
            this.fontRenderer.drawStringWithShadow(
                    tooltipList.get(i), posX, posY + height - ((10 * tooltipList.size()) - 10 * i) + 2, -1);
        }


        // Prep render of backpack slot tex
        this.zLevel = 0.0f;
        itemRenderer.zLevel = 0.0f;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslatef(0F, 0F, 250F);
        this.mc.getTextureManager().bindTexture(WIDGET_TEXURE);
        // Render slots
        for (int i = 0; i < gridY; i++) {
            for (int j = 0; j < gridX; j++) {
                // Dont render if outside of inventory
                if ((i * gridX) + j >= Math.max(invSize, 1))
                    break;

                int stackX = posX + 1 + (j * 18);
                int stackY = posY + 11 + (i * 18);
                drawTexturedModalRect(stackX, stackY, 0, 0, 18, 18);
            }
        }

        // Prep render of backpack items
        RenderHelper.enableGUIStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef(0F, 0F, 250F);

        // Render items
        for (int i = 0; i < gridY; i++) {
            for (int j = 0; j < gridX; j++) {
                int invPos = ((i) * gridX) + j;
                // Dont render if outside of inventory
                if (invPos >= invSize)
                    break;

                int stackX = posX + 2 + (j * 18);
                int stackY = posY + 12 + (i * 18);

                drawSlotInventory(new Slot(inv, invPos, stackX, stackY));
                if (invPos == invSelected && !isShiftKeyDown()) {
                    // Draw slot selection overlay
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    this.drawGradientRect(stackX, stackY, stackX + 16, stackY + 16, -2130706433, -2130706433);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                }

            }
        }

        GL11.glTranslatef(0F, 0F, -500F);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }
}
