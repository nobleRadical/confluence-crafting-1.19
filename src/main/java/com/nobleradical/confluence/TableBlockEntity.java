package com.nobleradical.confluence;

import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class TableBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(10, ItemStack.EMPTY);
    CraftingInventory craftInv = new CraftingInventory(new SmolScreenHandler(), 3, 3);

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
    }
    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        super.writeNbt(nbt);
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T be) {
        //use this to prevent items from consolidating
        if (world.isClient()) return;
        if (world.getTime() % 5 != 0) return;
        List<ItemEntity> entityItems = world.getEntitiesByClass(ItemEntity.class, new Box(pos.up(2).north().west(), pos.down().south().east()), EntityPredicates.VALID_ENTITY);
        for (ItemEntity item : entityItems) {
            // item. do not merge please. I not know how. And now I need mixin. rip me.
            item.addScoreboardTag("doNotClump");
            item.setPickupDelay(100);
        }
    }
    
    public void craft(World world, BlockPos pos) {
        Confluence.LOGGER.debug("Crafting has started...");
        // setting up my arrays
        BlockPos[] craftPos = new BlockPos[9];
        ItemStack[] craftItem = new ItemStack[9];
        // these are the positions of the cauldrons, above the table.
        craftPos[0] = pos.up().north().west();
        craftPos[1] = pos.up().north();
        craftPos[2] = pos.up().north().east();
        craftPos[3] = pos.up().west();
        craftPos[4] = pos.up();
        craftPos[5] = pos.up().east();
        craftPos[6] = pos.up().south().west();
        craftPos[7] = pos.up().south();
        craftPos[8] = pos.up().south().east();
        for (int i = 0; i < 9; i++) {
        if (world.getBlockState(craftPos[i]).isIn(BlockTags.CAULDRONS)) {
            Confluence.LOGGER.debug("Cauldrons found.");
            //grabbing the entities inside the cauldrons.
            List<ItemEntity> entityItems = world.getEntitiesByClass(ItemEntity.class, new Box(craftPos[i]), EntityPredicates.VALID_ENTITY);
            if (entityItems.size() == 0) {
                craftItem[i] = ItemStack.EMPTY;
                Confluence.LOGGER.debug("Emptied stack at "+Integer.toString(i));
                continue;
            }
            //add the item to the crafting stack.
            craftItem[i] = entityItems.get(0).getStack().split(1);
            Confluence.LOGGER.debug("Added "+craftItem[i].toString()+ "to stack at index "+Integer.toString(i));
            }
        }
        Confluence.LOGGER.debug("Matching recipes:");
        Optional<CraftingRecipe> recipeMatch = null;
        //making this rotatable.
        //rotation list
        int[][] rotations = new int[4][8];
        //normal rotation (north)
        rotations[0] = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        //east rotation
        rotations[1] = new int[]{2, 5, 8, 1, 4, 7, 0, 3, 6};
        //south rotation
        rotations[2] = new int[]{8, 7, 6, 5, 4, 3, 2, 1, 0};
        // west rotation
        rotations[3] = new int[]{6, 3, 0, 7, 4, 1, 8, 5, 2};
        for (int[] i : rotations){
            for (int c = 0; c < 9; c++) {
                int j = i[c];
                craftInv.setStack(j, craftItem[c]);
            }
            recipeMatch = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftInv, world);
            if (recipeMatch.isPresent()) break;
        }
        if (recipeMatch.isPresent()) {
            Confluence.LOGGER.debug("Match found!");
            items.set(0, recipeMatch.get().craft(craftInv)); // add the resulting item to the actual inventory of the block.
            // add the remaining stacks as well.
            int index = 1;
            for (ItemStack item : world.getRecipeManager().getRemainingStacks(RecipeType.CRAFTING, craftInv, world)) {
                items.set(index, item);
                index++;
            }
            craftInv.clear(); //housekeeping, I hope this doesn't mess anything up

            //success! let's celebrate!
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, 1, false);
            Confluence.LOGGER.debug("Crafting Complete.");
        } else {
            Confluence.LOGGER.debug("No Match.");
            for (int i = 0; i <= 8; i++) {
                Block.dropStack(world, craftPos[i].add(0, 0.5, 0), craftItem[i]);
            }
        }
    }

    public TableBlockEntity(BlockPos pos, BlockState state) {
        super(Confluence.TABLE_BLOCK_ENTITY, pos, state);
    }
}