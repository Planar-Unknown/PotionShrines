package com.dreu.potionshrines.blocks.shrine;

import com.dreu.potionshrines.config.General;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.dreu.potionshrines.config.General.OBTAINABLE;

public class ShrineBaseBlock extends Block {
    public static final VoxelShape SIMPLE_BOTTOM_SHAPE =
            Shapes.join(
                Shapes.join(
                    Shapes.join(
                            Block.box(1, 0, 1, 15, 2, 15),
                            Block.box(2, 2, 2, 14, 4, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(3, 4, 3, 13, 11, 13),
                        Block.box(4, 11, 4, 12, 28,12), BooleanOp.OR),
                    BooleanOp.OR
                ),
                Shapes.join(
                    Shapes.join(
                        Block.box(3, 28, 3, 13, 29, 13),
                        Block.box(2, 29, 2, 14, 31, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(2, 31, 1, 14, 32, 15),
                        Block.box(1, 31, 2, 15, 32, 14), BooleanOp.OR),
                    BooleanOp.OR
                ),
                BooleanOp.OR
            );
    public static final VoxelShape SIMPLE_TOP_SHAPE =
            Shapes.join(
                Shapes.join(
                    Shapes.join(
                        Block.box(1, -16, 1, 15, -14, 15),
                        Block.box(2, -14, 2, 14, -12, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(3, -12, 3, 13, -5, 13),
                        Block.box(4, -5, 4, 12, 12,12), BooleanOp.OR),
                    BooleanOp.OR
                ),
                Shapes.join(
                    Shapes.join(
                        Block.box(3, 12, 3, 13, 13, 13),
                        Block.box(2, 13, 2, 14, 15, 14), BooleanOp.OR),
                    Shapes.join(
                        Block.box(2, 15, 1, 14, 16, 15),
                        Block.box(1, 15, 2, 15, 16, 14), BooleanOp.OR),
                    BooleanOp.OR
                ),
                BooleanOp.OR
            );

    public static final VoxelShape AURA_BOTTOM_SHAPE =
            Shapes.join(
                    Shapes.join(
                            Shapes.join(
                                    Block.box(1, 0, 1, 3, 9, 3),
                                    Block.box(2, 0.66, 2, 4, 30.66, 4), BooleanOp.OR),
                            Shapes.join(
                                    Block.box(3, 1.66, 3, 13, 6.66, 13),
                                    Block.box(4, 6.66, 4, 12, 7.33,12), BooleanOp.OR),
                            BooleanOp.OR
                    ),
                    Shapes.join(
                            Shapes.join(
                                    Block.box(5, 7.33, 5, 11, 7.66, 11),
                                    Block.box(6, 7.66, 6, 10, 28.499, 10), BooleanOp.OR),
                            Shapes.join(
                                    Block.box(5, 23.66, 5, 11, 24, 11),
                                    Block.box(4, 24, 4, 12, 24.66, 12), BooleanOp.OR),
                            BooleanOp.OR
                    ),
                    BooleanOp.OR
            );
    public static final VoxelShape AURA_TOP_SHAPE =
            Shapes.join(
                    Shapes.join(
                            Shapes.join(
                                    Shapes.join(
                                            Block.box(12, -15.34, 2, 14, 14.66, 4),
                                            Block.box(2, -15.34, 2, 4, 14.66, 4), BooleanOp.OR),
                                    Shapes.join(
                                            Block.box(3, -14.34, 3, 13, -9.34, 13),
                                            Block.box(4, -9.34, 4, 12, -8.67, 12), BooleanOp.OR),
                                    BooleanOp.OR
                            ),
                            Shapes.join(
                                    Shapes.join(
                                            Block.box(5, -8.67, 5, 11, -8.34, 11),
                                            Block.box(6, -8.34, 6, 10, 12.499, 10), BooleanOp.OR),
                                    Shapes.join(
                                            Block.box(5, 7.66, 5, 11, 8, 11),
                                            Block.box(4, 8, 4, 12, 8.66, 12), BooleanOp.OR),
                                    BooleanOp.OR
                            ),
                            BooleanOp.OR
                    ),
                    Shapes.join(
                            Shapes.join(
                                    Block.box(12, -15.34, 12, 14, 14.66, 14),
                                    Block.box(2, -15.34, 12, 4, 14.66, 14), BooleanOp.OR),
                            Block.box(3, 8.66, 3, 13, 13.66, 13), BooleanOp.OR
                    ), BooleanOp.OR
            );

    public static final VoxelShape AOE_BOTTOM_SHAPE =
            Shapes.join(
                    Shapes.join(
                            Block.box(1, 0, 1, 15, 4, 15),
                            Block.box(2, 4, 2, 14, 10, 14), BooleanOp.OR),
                    Shapes.join(
                            Block.box(3, 10, 3, 13, 20, 13),
                            Block.box(2, 20, 2, 14, 31,14), BooleanOp.OR),
                    BooleanOp.OR
            );
    public static final VoxelShape AOE_TOP_SHAPE =
            Shapes.join(
                    Shapes.join(
                            Block.box(1, -16, 1, 15, -12, 15),
                            Block.box(2, -12, 2, 14, -6, 14), BooleanOp.OR),
                    Shapes.join(
                            Block.box(3, -16, 3, 13, 4, 13),
                            Block.box(2, 4, 2, 14, 15,14), BooleanOp.OR),
                    BooleanOp.OR
            );

    public static final VoxelShape SIMPLE_COLLISION_SHAPE = Block.box(2, 0, 2, 14, 16, 14);
    public static final VoxelShape AURA_COLLISION_SHAPE = Block.box(3, 0, 3, 13, 16, 13);

    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    protected final Block shrineBlock;
    protected final VoxelShape topShape;
    protected final VoxelShape bottomShape;
    protected final VoxelShape collisionShape;

    public ShrineBaseBlock(Properties properties, Block shrineBlock, VoxelShape topShape, VoxelShape bottomShape, VoxelShape collisionShape) {
        super(properties);
        this.shrineBlock = shrineBlock;
        this.topShape = topShape;
        this.bottomShape = bottomShape;
        this.collisionShape = collisionShape;
        this.registerDefaultState(stateDefinition.any()
                .setValue(HALF, Half.BOTTOM));
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {return PushReaction.BLOCK;}
    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {return collisionShape;}

    @Override
    public ItemStack getCloneItemStack(BlockState blockState, HitResult target, BlockGetter level, BlockPos blockPos, Player player) {
        return level.getBlockState(blockPos.above()).getBlock().getCloneItemStack(level.getBlockState(blockPos.above()), target, level, blockPos.above(), player);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return blockState.getValue(HALF) == Half.TOP ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return blockState.getValue(HALF) == Half.TOP ? topShape : bottomShape;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        blockPos = blockPos.above(blockState.getValue(HALF) == Half.TOP ? 1 : 2);
        if (level.getBlockState(blockPos).is(shrineBlock)) {
            level.destroyBlock(blockPos.below(2), true);
            level.removeBlock(blockPos.below(1), true);
            level.removeBlock(blockPos, true);
        }
    }
    @SuppressWarnings("all")
    @Override
    public boolean onDestroyedByPlayer(BlockState blockState, Level level, BlockPos blockPos, Player player, boolean willHarvest, FluidState fluid) {
        BlockPos shrinePos = blockPos.above(blockState.getValue(HALF) == Half.BOTTOM ? 2 : 1);
        if (level.getBlockEntity(shrinePos) != null && !level.isClientSide) {
            if (OBTAINABLE && !player.isCreative()) {
                ItemStack drop = new ItemStack(this);
                level.getBlockEntity(shrinePos).saveToItem(drop);
                popResource(level, blockPos, drop);
            }
            level.removeBlock(shrinePos.below(2), true);
            level.removeBlock(shrinePos.below(1), true);
            level.removeBlock(shrinePos, true);
            return true;
        }
        return super.onDestroyedByPlayer(blockState, level, blockPos, player, !player.isCreative(), fluid);
    }

    @Override
    public boolean canEntityDestroy(BlockState blockState, BlockGetter level, BlockPos blockPos, Entity entity) {
        return !General.SHRINE_INDESTRUCTIBLE && super.canEntityDestroy(blockState, level, blockPos, entity);
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return General.SHRINE_INDESTRUCTIBLE ? 3600000 : 1200;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        blockPos = blockPos.above(blockState.getValue(HALF) == Half.TOP ? 1 : 2);
        return level.getBlockState(blockPos).getBlock().use(level.getBlockState(blockPos), level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        level.setBlock(blockPos.above(), blockState.getValue(HALF) == Half.BOTTOM
                ? this.defaultBlockState().setValue(HALF, Half.TOP)
                : shrineBlock.defaultBlockState(), 11);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }
}
