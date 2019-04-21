import mpi.Comm;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

public class Main {
	public static void main(String[] args) throws MPIException {
		MPI.Init(args);

	    int rank = MPI.COMM_WORLD.getRank(),
	        size = MPI.COMM_WORLD.getSize();

	    // Get the name of the processor
	    String processor_name = MPI.getProcessorName();

	    // Print off a hello world message
	    System.out.println("Hello world from processor " + processor_name + 
	    		" rank " + rank + " out of " + size + " processors");

	    MPI.Finalize();
	    
	    
	}
}
