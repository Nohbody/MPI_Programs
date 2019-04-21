package Java;
import mpi.Struct;

public class IndexPairValue extends Struct
{
    // This section defines the offsets of the fields.
    public final int rank = addInt(),
    listIndex = addInt(), value = addDouble();

    // This method tells the super class how to create a data object.
    @Override protected Data newData() { return new Data(); }

    public class Data extends Struct.Data
    {
        // These methods read from the buffer:
        public int getRank() { return getInt(rank); }
        public int getListIndex() { return getInt(listIndex); }
        public double getValue() { return getDouble(value); }

       // These methods write to the buffer:
       public void putRank(int f) { putInt(rank, f); }
       public void putListIndex(int s) { putInt(listIndex, s); }
       public void putValue(double v) { putDouble(value, v); }
    }
}
