package com.gmail.jannyboy11.customrecipes.util.inventory;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.InventorySubcontainer;
import net.minecraft.server.v1_12_R1.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

/**
 * View a net.minecraft.server.IInventory as a grid
 * <p>
 * e.g.
 *      <pre>
 *          y\x  0   1   2   3   4   5   6   7   8
 *          0    0   1   2   3   4   5   6   7   8
 *          1    9   10  11  12  13  14  15  16  17
 *          2    18  19  20  21  22  23  24  25  26
 *          3    27  28  29  30  31  32  33  34  35
 *          4    36  37  38  39  40  41  42  43  44
 *          5    45  46  47  48  49  50  51  52  53
 *      </pre>
 * </p>
 */
public class NMSGridView implements InventoryHolder {

    private final IInventory inventory;
    private final int width, height;
    
    private CraftInventory bukkitView;

    public NMSGridView(int height) {
        this.inventory = new InventorySubcontainer("", false, (this.width=9) * (this.height=height), this);
    }

    public NMSGridView(String title, int height) {
        this.inventory = new InventorySubcontainer(title, true, (this.width=9) * (this.height=height), this);
    }

    public NMSGridView(IInventory inventory, int width, int height) {
        this.inventory = Objects.requireNonNull(inventory);
        this.width = width;
        this.height = height;
    }
    
    public int getIndex(int x, int y) {
        return x + y * width;
    }

    public ItemStack getItem(int x, int y) {
        return inventory.getItem(getIndex(x, y));
    }

    public List<? extends ItemStack> getColumnItems(int x) {
        List<ItemStack> items = new ArrayList<>(height);
        for (int y = 0; y < height; y++) {
            items.add(getItem(x, y));
        }
        return items;
    }

    public List<? extends ItemStack> getRowItems(int y) {
        List<ItemStack> items = new ArrayList<>(width);
        for (int x = 0; x < width; x++) {
            items.add(getItem(x, y));
        }
        return items;
    }

    public List<? extends ItemStack> getVerticalLine(int x, int yOffset, int yLength) {
        return getItems(x, yOffset, 1, yLength);
    }

    public List<? extends ItemStack> getHorizontalLine(int y, int xOffset, int xLength) {
        return getItems(xOffset, y, xLength, 1);
    }


    public List<? extends ItemStack> getItems(int xOffset, int yOffset, int xLength, int yLength) {
        List<ItemStack> items = new ArrayList<>(xLength * yLength);
        for (int x = xOffset; x < xLength + xOffset; x++) {
            for (int y = xOffset; y < yLength + yOffset; y++) {
                items.add(getItem(x, y));
            }
        }
        return items;
    }


    public void setItem(int xRightWards, int yDownWards, ItemStack itemStack) {
        inventory.setItem(xRightWards + yDownWards * width, itemStack);
    }

    public void fill(ItemStack with) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, with);
        }
    }

    public void fillRow(int y, ItemStack with) {
        for (int x = 0; x < width; x++) {
            setItem(x, y, with);
        }
    }

    public void fillRow(int y, CoordinateFunction with) {
        for (int x = 0; x < width; x++) {
            setItem(x, y, with.computeItemStack(x, y));
        }
    }

    public void fillColumn(int x, ItemStack with) {
        for (int y = 0; y < height; y++) {
            setItem(x, y, with);
        }
    }

    public void fillColumn(int x, CoordinateFunction with) {
        for (int y = 0; y < height; y++) {
            setItem(x, y, with.computeItemStack(x, y));
        }
    }

    public void fill(int xOffset, int yOffset, int xLength, int yLength, ItemStack with) {
        for (int x = xOffset; x < xLength + xOffset; x++) {
            for (int y = yOffset; y < yLength + yOffset; y++) {
                setItem(x, y, with);
            }
        }
    }

    public void verticalLine(int x, int yOffset, int yLength, ItemStack with) {
        fill(x, yOffset, 1, yLength, with);
    }

    public void horizontalLine(int xOffset, int y, int xLength, ItemStack with) {
        fill(xOffset, y, xLength, 1, with);
    }

    public void verticalLine(int x, int yOffset, int yLength, CoordinateFunction with) {
        fill(x, yOffset, 1, yLength, with);
    }

    public void horizontalLine(int y, int xOffset, int xLength, CoordinateFunction with) {
        fill(xOffset, y, xLength, 1, with);
    }

    public void fill(int xOffset, int yOffset, int xLength, int yLength, CoordinateFunction with) {
        for (int x = xOffset; x < xLength + xOffset; x++) {
            for (int y = xOffset; y < yLength + yOffset; y++) {
                ItemStack item = with.computeItemStack(x, y);
                setItem(x, y, item);
            }
        }
    }

    public <T> void fill(ToIntFunction<T> fTtoX, ToIntFunction<T> fTtoY, CoordinateFunction with, T... ts) {
        for (T t : ts) {
            int x = fTtoX.applyAsInt(t);
            int y = fTtoY.applyAsInt(t);
            ItemStack item = with.computeItemStack(x, y);
            setItem(x, y, item);
        }
    }

    public <T> void fill(ToIntFunction<T> fTtoX, ToIntFunction<T> fTtoY, CoordinateFunction with, Iterable<? extends T> ts) {
        fill(fTtoX, fTtoY, with, ts.iterator());
    }

    public <T> void fill(ToIntFunction<T> fTtoX, ToIntFunction<T> fTtoY, CoordinateFunction with, Iterator<? extends T> ts) {
        while (ts.hasNext()) {
            T t = ts.next();
            int x = fTtoX.applyAsInt(t);
            int y = fTtoY.applyAsInt(t);
            ItemStack item = with.computeItemStack(x, y);
            setItem(x, y, item);
        }
    }

    public void plotXtoY(IntUnaryOperator xToYFunction, ItemStack with, int... xs) {
        for (int x : xs) {
            int y = xToYFunction.applyAsInt(x);
            setItem(x, y, with);
        }
    }

    public void plotXtoY(IntUnaryOperator xToYFunction, ItemStack with, Iterable<Integer> xs) {
        for (int x : xs) {
            int y = xToYFunction.applyAsInt(x);
            setItem(x, y, with);
        }
    }

    public void plotXtoY(IntUnaryOperator xToYFunction, ItemStack with, Iterator<Integer> xs) {
        xs.forEachRemaining(x -> {
            int y = xToYFunction.applyAsInt(x);
            setItem(x, y, with);
        });
    }

    public void plotXtoY(IntUnaryOperator xToYFunction, CoordinateFunction with, int... xs) {
        for (int x : xs) {
            int y = xToYFunction.applyAsInt(x);
            ItemStack item = with.computeItemStack(x, y);
            setItem(x, y, item);
        }
    }

    public void plotXtoY(IntUnaryOperator xToYFunction, CoordinateFunction with, Iterable<Integer> xs) {
        for (int x : xs) {
            int y = xToYFunction.applyAsInt(x);
            ItemStack item = with.computeItemStack(x, y);
            setItem(x, y, item);
        }
    }

    public void plotXtoY(IntUnaryOperator xToYFunction, CoordinateFunction with, Iterator<Integer> xs) {
        xs.forEachRemaining(x -> {
            int y = xToYFunction.applyAsInt(x);
            ItemStack item = with.computeItemStack(x, y);
            setItem(x, y, item);
        });
    }

    public void plotYtoX(IntUnaryOperator yToXFunction, ItemStack with, int... ys) {
        for (int y : ys) {
            int x = yToXFunction.applyAsInt(y);
            setItem(x, y, with);
        }
    }

    public void plotYtoX(IntUnaryOperator yToXFunction, ItemStack with, Iterable<Integer> ys) {
        for (int y : ys) {
            int x = yToXFunction.applyAsInt(y);
            setItem(x, y, with);
        }
    }

    public void plotYtoX(IntUnaryOperator yToXFunction, ItemStack with, Iterator<Integer> ys) {
        ys.forEachRemaining(y -> {
            int x = yToXFunction.applyAsInt(y);
            setItem(x, y, with);
        });
    }

    public void plotYToX(IntUnaryOperator yToXFunction, CoordinateFunction with, int... ys) {
        for (int y : ys) {
            int x = yToXFunction.applyAsInt(y);
            ItemStack item = with.computeItemStack(x, y);
            setItem(x, y, item);
        }
    }

    public void plotYToX(IntUnaryOperator yToXFunction, CoordinateFunction with, Iterable<Integer> ys) {
        for (int y : ys) {
            int x = yToXFunction.applyAsInt(y);
            ItemStack item = with.computeItemStack(x, y);
            setItem(x, y, item);
        }
    }

    public void plotYToX(IntUnaryOperator yToXFunction, CoordinateFunction with, Iterator<Integer> ys) {
        ys.forEachRemaining(y -> {
            int x = yToXFunction.applyAsInt(y);
            ItemStack item = with.computeItemStack(x, y);
            setItem(x, y, item);
        });
    }

    public void fillZip(Iterable<Integer> xs, Iterable<Integer> ys, ItemStack with) {
        fillZip(xs.iterator(), ys.iterator(), with);
    }

    public void fillZip(Iterable<Integer> xs, Iterable<Integer> ys, CoordinateFunction with) {
        fillZip(xs.iterator(), ys.iterator(), with);
    }

    public void fillZip(Iterator<Integer> xs, Iterator<Integer> ys, ItemStack with) {
        while (xs.hasNext() && ys.hasNext()) {
            int x = xs.next();
            int y = ys.next();
            setItem(x, y, with);
        }
    }

    public void fillZip(Iterator<Integer> xs, Iterator<Integer> ys, CoordinateFunction with) {
        while(xs.hasNext() && ys.hasNext()) {
            int x = xs.next();
            int y = ys.next();
            ItemStack item = with.computeItemStack(x, y);
            setItem(x, y, item);
        }
    }

    public void fillCarthesian(Iterable<Integer> xs, Iterable<Integer> ys, ItemStack with) {
        for (int x : xs) {
            for (int y : ys) {
                setItem(x, y, with);
            }
        }
    }

    public void fillCarthesian(Iterable<Integer> xs, Iterable<Integer> ys, CoordinateFunction itemStackFunction) {
        for (int x : xs) {
            for (int y : ys) {
                ItemStack with = itemStackFunction.computeItemStack(x, y);
                setItem(x, y, with);
            }
        }
    }

    @Override
    public CraftInventory getInventory() {
        return bukkitView == null ? bukkitView = new CraftInventory(inventory) : bukkitView;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NMSGridView (" + getWidth() + "x" + getHeight() + ") {");
        stringBuilder.append(System.lineSeparator());
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i % width == 0) {
                stringBuilder.append(';');
                stringBuilder.append(System.lineSeparator());
            }
            stringBuilder.append(inventory.getItem(i));
        }
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof NMSGridView)) return false;

        NMSGridView that = (NMSGridView) o;
        return Objects.equals(this.inventory, that.inventory);
    }

    @Override
    public int hashCode() {
        return inventory.hashCode();
    }


    @FunctionalInterface
    public static interface CoordinateFunction {

        public ItemStack computeItemStack(int xCoordinate, int yCoordinate);

        public static CoordinateFunction constant(ItemStack consant) {
            return (x, y) -> consant;
        }

        public static CoordinateFunction ignoreY(IntFunction<? extends ItemStack> xFunction) {
            return (x, y) -> xFunction.apply(x);
        }

        public static CoordinateFunction ignoreX(IntFunction<? extends ItemStack> yFunction) {
            return (x, y) -> yFunction.apply(y);
        }

        public default IntFunction<? extends ItemStack> applyX(int x) {
            return y -> computeItemStack(x, y);
        }

        public default IntFunction<? extends ItemStack> applyY(int y) {
            return x -> computeItemStack(x, y);
        }
    }
}
