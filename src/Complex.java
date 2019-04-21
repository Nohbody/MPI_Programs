import mpi.Struct;

public class Complex extends Struct
{
    // This section defines the offsets of the fields.
    private final int real = addDouble(),
    imag = addDouble();

    // This method tells the super class how to create a data object.
    @Override protected Data newData() { return new Data(); }

    public class Data extends Struct.Data
    {
        // These methods read from the buffer:
        public double getReal() { return getDouble(real); }
        public double getImag() { return getDouble(imag); }

       // These methods write to the buffer:
       public void putReal(double r) { putDouble(real, r); }
       public void putImag(double i) { putDouble(imag, i); }
    } // Data
} // Complex
