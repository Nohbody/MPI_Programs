package Java;
import mpi.Struct;

public class IndexPair extends Struct
{
    // This section defines the offsets of the fields.
    private final int first = addInt(),
    second = addInt();

    // This method tells the super class how to create a data object.
    @Override protected Data newData() { return new Data(); }

    public class Data extends Struct.Data
    {
        // These methods read from the buffer:
        public int getFirst() { return getInt(first); }
        public int getSecond() { return getInt(second); }

       // These methods write to the buffer:
       public void putFirst(int f) { putInt(first, f); }
       public void putSecond(int s) { putInt(second, s); }
    }
}
