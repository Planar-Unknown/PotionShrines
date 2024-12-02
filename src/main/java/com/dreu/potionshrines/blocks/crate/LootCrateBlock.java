package com.dreu.potionshrines.blocks.crate;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

import static com.dreu.potionshrines.blocks.crate.Rarities.COMMON;

@SuppressWarnings("deprecation")
public class LootCrateBlock extends Block {
    public static final EnumProperty<Rarities> RARITY = EnumProperty.create("rarity", Rarities.class);
    public static final VoxelShape SHAPE =
                    Shapes.join(
                        Shapes.join(
                                Shapes.join(
                                        Shapes.join(
                                                Block.box(0.5, 0.5, 0.5, 15.5, 15.5, 15.5),
                                                Block.box(0, 0, 0, 3, 3, 3),
                                                BooleanOp.OR),
                                        Shapes.join(
                                                Block.box(13, 0, 0, 16, 3, 3),
                                                Block.box(0, 0, 13, 3, 3, 16),
                                                BooleanOp.OR
                                        ),
                                        BooleanOp.OR),
                                Shapes.join(
                                        Shapes.join(
                                                Block.box(13, 0, 13, 16, 3, 16),
                                                Block.box(0, 13, 13, 3, 16, 16),
                                                BooleanOp.OR),
                                        Shapes.join(
                                                Block.box(13, 13, 0, 16, 16, 3),
                                                Block.box(13, 13, 13, 16, 16, 16),
                                                BooleanOp.OR
                                        ),
                                        BooleanOp.OR),
                                BooleanOp.OR
                        ),
                        Block.box(0, 13, 0, 3, 16, 3),
                    BooleanOp.OR);


    public LootCrateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any().setValue(RARITY, COMMON));
    }

    @Override @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return SHAPE;
    }

    @Override @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        //Todo get this done
        return super.use(blockState, level, blockPos, player, interactionHand, hitResult);

    }

    @Override
    public ItemStack getCloneItemStack(BlockState blockState, HitResult hitResult, BlockGetter blockGetter, BlockPos blockPos, Player player) {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();
        tag.putString("Rarity", blockState.getValue(RARITY).toString());
        stack.setTag(tag);
        return stack;
    }

    @Override @SuppressWarnings("DataFlowIssue")
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        if (context.getItemInHand().hasTag() && Arrays.stream(Rarities.values()).map(Rarities::getSerializedName).toList().contains(context.getItemInHand().getTag().getString("Rarity"))){
            return this.defaultBlockState().setValue(RARITY, Rarities.fromString(context.getItemInHand().getTag().getString("Rarity")));
        }
        return super.getStateForPlacement(context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RARITY);
    }
}
