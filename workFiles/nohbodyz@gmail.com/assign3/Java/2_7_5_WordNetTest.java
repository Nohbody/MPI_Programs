import org.junit.Test;

import static org.junit.Assert.*;

public class WordNetTest {

    @Test
    public void distance() throws Exception {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");

        assertTrue(wn.distance("Abydos", "Calamus") > 0);
        // Test two nouns that are in the same vertex
        assertTrue(wn.distance("Hess", "Victor_Hess") == 0);
    }

    @Test
    public void sca() throws Exception {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");

        // Test that the root is indeed the SCA of two vertices that connect to it
        assertTrue(wn.sca("abstraction", "physical_entity").equals("entity"));
        // Test two nouns in the same vertex
        assertTrue(wn.sca("Hess", "Victor_Hess").equals("Hess Victor_Hess Victor_Franz_Hess"));
    }
}