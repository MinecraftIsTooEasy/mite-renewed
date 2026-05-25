package com.github.jeffyjamzhd.renewed.render.gui;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyProvider;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;
import net.minecraft.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiCustomizeWorldDifficulty extends GuiScreen {
    private final GuiScreen parentScreen;
    private GuiDifficultyParameterList list;
    protected Difficulty difficulty;
    protected Difficulty customizedDifficulty;
    private String screenTitle;

    public GuiCustomizeWorldDifficulty(GuiScreen parent, Difficulty difficulty) {
        this.parentScreen = parent;
        this.difficulty = difficulty;
    }

    @Override
    public void initGui() {
        this.screenTitle = I18n.getString("difficulty.customize");
        this.list = new GuiDifficultyParameterList(this);
    }

    @Override
    protected void mouseClicked(int mX, int mY, int mButton) {
        super.mouseClicked(mX, mY, mButton);
        this.list.mouseClicked(mX, mY, mButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        this.list.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 16777215);

        int workableWidth = (int) (this.width * 0.66F);

        String wrapped = StringUtil.wrapLines(I18n.getString("difficulty.parameter.NonSolidLeaves.desc"), workableWidth / 5 - 3);
        AtomicInteger y = new AtomicInteger(0);
        wrapped.lines().forEach(s -> this.drawString(this.fontRenderer, s, (int) (this.width * 0.33F), this.height - 44 + y.getAndAdd(10), -1));


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateScreen() {
        super.updateScreen();
        // this.list.updateScreen();
    }

    public class GuiDifficultyParameterList extends GuiListExtended {
        private final List<ParameterEntry<?>> entries = new ArrayList<>();
        protected Difficulty difficulty;
        protected ParameterEntry<?> focused;

        public GuiDifficultyParameterList(GuiCustomizeWorldDifficulty screen) {
            super(screen.mc, (int) (screen.width * 0.66F), screen.height, 24, screen.height - 48, 24);
            this.field_148163_i = false;

            this.left = (int) (screen.width * 0.33F);
            this.right = screen.width;
            this.difficulty = screen.difficulty;

            for (DifficultyParameter<?> parameter : DifficultyProvider.identifierToParam.values()) {
                this.entries.add(new ParameterEntry<>(difficulty, parameter));
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
        public IGuiListEntry getListEntry(int i) {
            return this.entries.get(i);
        }

        @Override
        protected int getSize() {
            return this.entries.size();
        }
    }

    public class ParameterEntry<T> implements GuiListExtended.IGuiListEntry {
        private final Minecraft mc;
        private final DifficultyParameter<T> parameter;
        private final Difficulty difficulty;
        private final GuiButton field;
        private T value;

        public ParameterEntry(Difficulty difficulty, DifficultyParameter<T> parameter) {
            this.mc = Minecraft.getMinecraft();
            this.difficulty = difficulty;
            this.parameter = parameter;
            this.value = difficulty.getParamValue(parameter);

            this.field = this.parameter.getFieldButton(this.value, 0, 0, 0);
            this.field.displayString = this.parameter.getValueString(this.value);
        }

        @Override
        public void setSelected(int slotIdx, int mX, int mY) {

        }

        @Override
        public void drawEntry(int slotIdx, int x, int y, int listWidth, int slotHeight, int mX, int mY, boolean selected) {
            // Draw name
            this.mc.fontRenderer.drawString(parameter.getName(), x, y + (slotHeight - 8) / 2, -1);

            // Draw button
            field.xPosition = x + listWidth - field.width;
            field.yPosition = y + (slotHeight - 20) / 2;
            field.drawButton(this.mc, mX, mY);
        }

        @Override
        public boolean mousePressed(int slotIdx, int x, int y, int mouseEvent, int rX, int rY) {
            return false;
        }

        @Override
        public void mouseReleased(int slotIdx, int x, int y, int mouseEvent, int rX, int rY) {

        }

        @Override
        public void keyTyped(int slotIdx, char typedChar, int keyCode) {

        }
    }
}
