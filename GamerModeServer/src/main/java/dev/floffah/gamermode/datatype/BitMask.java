package dev.floffah.gamermode.datatype;

import lombok.Getter;

public class BitMask {

    /**
     * The bitmask int
     * -- GETTER --
     * Get the bitmask int
     *
     * @return the bitmask int
     */
    @Getter
    public int mask;

    public BitMask(int mask) {
        this.mask = mask;
    }

    public BitMask() {
        this(0);
    }

    /**
     * Get the boolean value of the bit at the index
     * @param index the index of the bit
     * @return the boolean value of the bit
     */
    public boolean booleanAt(byte index) {
        return (this.mask & index) == index;
    }

    /**
     * Set the bit at the index to the value
     * @param index the index of the bit
     * @param value the value to set
     */
    public void setBooleanAt(byte index, boolean value) {
        this.mask = value ? this.mask | index : this.mask & ~index;
    }
}
