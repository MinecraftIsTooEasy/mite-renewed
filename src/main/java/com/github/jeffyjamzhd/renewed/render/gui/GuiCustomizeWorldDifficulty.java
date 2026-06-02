package com.github.jeffyjamzhd.renewed.render.gui;

import com.github.jeffyjamzhd.renewed.api.IGuiCreateWorld;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyProvider;
import com.github.jeffyjamzhd.renewed.api.difficulty.gui.IParameterField;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiCustomizeWorldDifficulty extends GuiScreen {
    private final GuiScreen parentScreen;
    private GuiDifficultyParameterList list;
    protected int difficultyIndice;
    protected Difficulty difficulty;
    private String screenTitle;

    private GuiButton btnDone;
    private GuiButton btnCancel;
    private GuiButton btnDifficulty;

    public GuiCustomizeWorldDifficulty(GuiScreen parent, int difficultyIndice) {
        this.parentScreen = parent;
        this.difficultyIndice = difficultyIndice;
    }

    public GuiCustomizeWorldDifficulty(GuiScreen parent, Difficulty difficulty) {
        this.parentScreen = parent;
        this.difficultyIndice = 0;
        this.difficulty = difficulty;
    }

    @Override
    public void initGui() {
        this.screenTitle = I18n.getString("difficulty.customize");

        this.buttonList.add(this.btnDone = new GuiButton(100, 16, this.height - 28, 100, 20, I18n.getString("gui.done")));
        this.buttonList.add(this.btnCancel = new GuiButton(101, 16, this.height - 52, 100, 20, I18n.getString("gui.cancel")));
        this.buttonList.add(this.btnDifficulty = new GuiButton(102, 16, 16, 100, 20, ""));

        Difficulty base = RenewedDifficulties.LIST.get(difficultyIndice);
        if (this.difficulty == null) {
            this.difficulty = base.cloneAsCustom();
        } else {
            this.difficulty = difficulty.cloneAsCustom();
        }
        this.btnDifficulty.displayString = base.getLocalizedName();

        this.list = new GuiDifficultyParameterList(this);
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
        super.actionPerformed(btn);

        switch(btn.id) {
            case 100: donePressed();        break;
            case 101: cancelPressed();      break;
            case 102: difficultyPressed();  break;
        }
    }

    private void donePressed() {
        if (this.parentScreen instanceof GuiCreateWorld) {
            ((IGuiCreateWorld)this.parentScreen).mr$assignCustomDifficulty(this.difficulty);
        }

        cancelPressed();
    }

    private void cancelPressed() {
        this.mc.displayGuiScreen(this.parentScreen);
    }

    private void difficultyPressed() {
        this.difficultyIndice = (this.difficultyIndice + 1) % RenewedDifficulties.LIST.size();
        Difficulty base = RenewedDifficulties.LIST.get(this.difficultyIndice);
        this.difficulty = base.cloneAsCustom();
        this.btnDifficulty.displayString = base.getLocalizedName();

        int scroll = this.list.getAmountScrolled();
        this.list = new GuiDifficultyParameterList(this);
        this.list.scrollBy(scroll);
    }

    @Override
    protected void mouseClicked(int mX, int mY, int mButton) {
        super.mouseClicked(mX, mY, mButton);
        this.list.mouseClicked(mX, mY, mButton);
    }

    @Override
    protected void mouseMovedOrUp(int mX, int mY, int mButton) {
        super.mouseMovedOrUp(mX, mY, mButton);
        this.list.mouseReleased(mX, mY, mButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        this.list.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 16777215);

        int targetWidth = this.width / 3 - 32;
        for (Object o : buttonList) {
            if (o instanceof GuiButton button) {
                button.width = Math.min(targetWidth, 300);
                button.xPosition = targetWidth <= 300 ? 16 : (this.width / 3) / 2 - 150;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        int slot = list.getSlotIndexFromScreenCoords(mouseX, mouseY);
        this.list.drawScreen(mouseX, mouseY, partialTicks);

        if (slot >= 0) {
            drawDescription(slot);
        }
    }

    private <T> void drawDescription(int slot) {
        int workableWidth = (int) (this.width * 0.66F);
        GuiListExtended.IGuiListEntry entry = this.list.getListEntry(slot);
        if (entry instanceof ParameterEntry<?> parameterEntry) {
            DifficultyParameter<T> parameter = (DifficultyParameter<T>) parameterEntry.parameter;
            T value = difficulty.getParamValue(parameter);
            String desc = parameter.getDescription(value);

            String wrapped = StringUtil.wrapLines(desc, workableWidth / 5 - 3);
            AtomicInteger yOffset = new AtomicInteger(0);

            wrapped.lines().forEach(s -> this.drawString(this.fontRenderer, s, (int) (this.width * 0.33F), this.height - 40 + yOffset.getAndAdd(12), -1));
        }
    }

    public void updateScreen() {
        super.updateScreen();
    }

    public class GuiDifficultyParameterList extends GuiListExtended {
        private final List<IGuiListEntry> entries = new ArrayList<>();
        private final GuiCustomizeWorldDifficulty owner;
        protected Difficulty difficulty;

        public GuiDifficultyParameterList(GuiCustomizeWorldDifficulty screen) {
            super(screen.mc, (int) (screen.width * 0.66F), screen.height, 24, screen.height - 48, 24);
            this.owner = screen;
            this.field_148163_i = false;

            this.left = (int) (screen.width * 0.33F);
            this.right = screen.width;
            this.difficulty = screen.difficulty;

            for (DifficultyParameter.Category category : DifficultyParameter.Category.values()) {
                this.entries.add(new CategoryEntry(category));
                List<DifficultyParameter<?>> parameters = DifficultyProvider.getParametersForCategory(category);
                for (DifficultyParameter<?> parameter : parameters) {
                    this.entries.add(new ParameterEntry<>(difficulty, parameter));
                }
            }
        }

        @Override
        public int getListWidth() {
            return (int) (this.width * 0.95F);
        }

        @Override
        protected int getScrollBarX() {
            return this.right - 5;
        }

        @Override
        protected void drawTooltip(int slotIdx, int x, int y, int listWidth, int height, int mX, int mY) {
        }

        @Override
        protected void drawSelectionBox(int x, int y, int mouseXIn, int mouseYIn) {
            int size = this.getSize();
            Tessellator tessellator = Tessellator.instance;
            int focused = getSlotIndexFromScreenCoords(mouseXIn, mouseYIn);

            for(int id = 0; id < size; ++id) {
                int top = y + id * this.slotHeight + this.headerPadding;
                int bottom = this.slotHeight - 4;
                if (top <= this.bottom && top + bottom >= this.top) {

                    if (focused == id) {
                        this.owner.drawGradientRect(x, top, x + getListWidth(), top + bottom, 0x20AAAAAA, 0x20AAAAAA);
                    }

                    this.drawSlot(id, x, top, bottom, tessellator, mouseXIn, mouseYIn);
                }
            }
        }

        @Override
        public int getSlotIndexFromScreenCoords(int mouseX, int mouseY) {
            int result = super.getSlotIndexFromScreenCoords(mouseX, mouseY);
            if (result != -1) {
                return mouseY < this.bottom && mouseY > this.top ? result : -1;
            }
            return -1;
        }

        @Override
        public IGuiListEntry getListEntry(int i) {
            return this.entries.get(i);
        }

        @Override
        protected int getSize() {
            return this.entries.size();
        }
    }

    public static class ParameterEntry<T> implements GuiListExtended.IGuiListEntry {
        private final Minecraft mc;
        private final DifficultyParameter<T> parameter;
        private final Difficulty difficulty;
        private final IParameterField<T> field;

        public ParameterEntry(Difficulty difficulty, DifficultyParameter<T> parameter) {
            this.mc = Minecraft.getMinecraft();
            this.difficulty = difficulty;
            this.parameter = parameter;

            T value = difficulty.getParamValue(parameter);
            this.field = this.parameter.getField(value, this.difficulty);
            this.field.setValue(value);
        }

        @Override
        public void setSelected(int slotIdx, int mX, int mY) {
        }

        @Override
        public void drawEntry(int slotIdx, int x, int y, int listWidth, int slotHeight, int mX, int mY, boolean selected) {
            // Draw name
            this.mc.fontRenderer.drawString(parameter.getName(), x + 4, y + (slotHeight - 8) / 2, -1);

            // Draw button
            field.setPosition(x + listWidth - field.getWidth(), y + (slotHeight - field.getHeight()) / 2);
            field.onDraw(this.mc, mX, mY);
        }

        @Override
        public boolean mousePressed(int slotIdx, int x, int y, int mouseEvent, int rX, int rY) {
            if (field.onMousePressed(this.mc, mouseEvent, x, y)) {
                difficulty.setParamValue(this.parameter, field.getValue());
                return true;
            }
            return false;
        }

        @Override
        public void mouseReleased(int slotIdx, int x, int y, int mouseEvent, int rX, int rY) {
            field.onMouseReleased(this.mc, mouseEvent, x, y);
            difficulty.setParamValue(this.parameter, field.getValue());
        }

        @Override
        public void keyTyped(int slotIdx, char typedChar, int keyCode) {

        }
    }

    public static class CategoryEntry implements GuiListExtended.IGuiListEntry {
        private final Minecraft mc;
        private final DifficultyParameter.Category category;

        public CategoryEntry(DifficultyParameter.Category category) {
            this.mc = Minecraft.getMinecraft();
            this.category = category;
        }

        @Override
        public void drawEntry(int slotIdx, int x, int y, int listWidth, int slotHeight, int mX, int mY, boolean selected) {
            String categoryStr = category.getName();
            int length = this.mc.fontRenderer.getStringWidth(categoryStr);

            this.mc.fontRenderer.drawString(categoryStr, x + (listWidth / 2) - (length / 2), y - 4 + slotHeight / 2, -1);
        }

        @Override
        public void setSelected(int i, int i1, int i2) {
        }

        @Override
        public boolean mousePressed(int i, int i1, int i2, int i3, int i4, int i5) {
            return false;
        }

        @Override
        public void mouseReleased(int i, int i1, int i2, int i3, int i4, int i5) {
        }

        @Override
        public void keyTyped(int i, char c, int i1) {
        }
    }
}
