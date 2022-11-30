package net.deechael.fabric.brightmagic.item;

import net.deechael.fabric.brightmagic.registry.BrightMagicItems;
import net.deechael.fabric.brightmagic.registry.BrightMagicSkills;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.skill.SkillData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SkillScrollItem extends Item {

    public SkillScrollItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            return super.use(world, user, hand);
        if (SkillData.getUnlocked((IDataHolder) user) > 25) {
            user.sendMessage(Text.translatable("brightmagic.messages.skillmaxed").formatted(Formatting.RED));
            return super.use(world, user, hand);
        }
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound nbtCompound = itemStack.getNbt();
        if (nbtCompound == null)
            return super.use(world, user, hand);
        if (!nbtCompound.contains("brightmagic", NbtElement.COMPOUND_TYPE))
            return super.use(world, user, hand);
        NbtCompound brightMagic = nbtCompound.getCompound("brightmagic");
        if (!brightMagic.contains("item-type", NbtElement.STRING_TYPE))
            return super.use(world, user, hand);
        if (!Objects.equals(brightMagic.getString("item-type"), "skill_scroll"))
            return super.use(world, user, hand);
        if (!brightMagic.contains("skill", NbtElement.STRING_TYPE))
            return super.use(world, user, hand);
        String skillId = brightMagic.getString("skill");
        Skill skill = Skill.get(new Identifier(skillId));
        if (skill == null)
            return super.use(world, user, hand);
        if (SkillData.isUnlocked((IDataHolder) user, skill)) {
            user.sendMessage(Text.translatable("brightmagic.messages.skillunlocked").formatted(Formatting.RED));
            return super.use(world, user, hand);
        }
        SkillData.addUnlocked((IDataHolder) user, skill);

        Formatting[] color = skill.getElement() == null ? new Formatting[] {Formatting.UNDERLINE, Formatting.WHITE} : new Formatting[] {skill.getElement().getColor()};
        user.sendMessage(Text.translatable("brightmagic.messages.unlockedelement").formatted(Formatting.YELLOW)
                .append(Text.translatable(skill.getTranslateKey()).formatted(color)));
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(itemStack, world, tooltip, context);
        NbtCompound nbtCompound = itemStack.getNbt();
        if (nbtCompound == null)
            return;
        if (!nbtCompound.contains("brightmagic", NbtElement.COMPOUND_TYPE))
            return;
        NbtCompound brightMagic = nbtCompound.getCompound("brightmagic");
        if (!brightMagic.contains("item-type", NbtElement.STRING_TYPE))
            return;
        if (!Objects.equals(brightMagic.getString("item-type"), "skill_scroll"))
            return;
        if (!brightMagic.contains("skill", NbtElement.STRING_TYPE))
            return;
        String skillId = brightMagic.getString("skill");
        Skill skill = Skill.get(new Identifier(skillId));
        if (skill == null)
            return;

        Formatting[] color = skill.getElement() == null ? new Formatting[] {Formatting.UNDERLINE, Formatting.WHITE} : new Formatting[] {skill.getElement().getColor()};
        MutableText base = Text.translatable("brightmagic.lore.skillscroller").formatted(Formatting.YELLOW);
        base.append(Text.translatable(skill.getTranslateKey()).formatted(color));
        tooltip.add(base);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (group == ItemGroup.COMBAT) {
            for (Skill skill : BrightMagicSkills.getAll())
                stacks.add(createStackWithSkill(skill.getId().toString()));
        }
    }

    private static ItemStack createStackWithSkill(String skill) {
        ItemStack itemStack = new ItemStack(BrightMagicItems.SKILL_SCROLL);
        NbtCompound nbtCompound = new NbtCompound();
        NbtCompound brightMagic = new NbtCompound();
        brightMagic.putString("item-type", "skill_scroll");
        brightMagic.putString("skill", skill);
        nbtCompound.put("brightmagic", brightMagic);
        itemStack.setNbt(nbtCompound);
        return itemStack;
    }

}
