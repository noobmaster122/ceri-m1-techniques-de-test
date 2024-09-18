package fr.univavignon.pokedex.api;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class PokemonTest {

    private Pokemon pokemon;

    @Before
    public void setUp() {
        // Create a Pokemon instance with sample data
        pokemon = new Pokemon(1, "Pikachu", 55, 40, 35, 500, 35, 1000, 25, 0.9);
    }

    @Test
    public void testGetCp() {
        assertEquals(500, pokemon.getCp());
    }

    @Test
    public void testGetHp() {
        assertEquals(35, pokemon.getHp());
    }

    @Test
    public void testGetDust() {
        assertEquals(1000, pokemon.getDust());
    }

    @Test
    public void testGetCandy() {
        assertEquals(25, pokemon.getCandy());
    }

    @Test
    public void testGetIv() {
        assertEquals(0.9, pokemon.getIv(), 0.01); // Delta for double comparison
    }
}
