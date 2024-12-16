package fr.univavignon.pokedex.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RocketPokemonFactoryTest {

    private RocketPokemonFactory pokemonFactory;

    @BeforeEach
    public void setUp() {
        pokemonFactory = new RocketPokemonFactory();
    }

    @Test
    void testCreatePokemonWithValidIndex() {
        int index = 1; // Bulbasaur
        int cp = 500;
        int hp = 200;
        int dust = 1000;
        int candy = 25;

        Pokemon createdPokemon = pokemonFactory.createPokemon(index, cp, hp, dust, candy);

        assertNotNull(createdPokemon);
        assertEquals("Bulbasaur", createdPokemon.getName());
        assertEquals(cp, createdPokemon.getCp());
        assertEquals(hp, createdPokemon.getHp());
        assertEquals(dust, createdPokemon.getDust());
        assertEquals(candy, createdPokemon.getCandy());
    }

    @Test
    void testCreatePokemonWithInvalidIndex() {
        int index = 999; // Does not exist in index2name
        int cp = 300;
        int hp = 150;
        int dust = 800;
        int candy = 15;

        Pokemon createdPokemon = pokemonFactory.createPokemon(index, cp, hp, dust, candy);

        assertNotNull(createdPokemon);
        assertEquals("MISSINGNO", createdPokemon.getName()); // Default name for invalid index
        assertEquals(cp, createdPokemon.getCp());
        assertEquals(hp, createdPokemon.getHp());
        assertEquals(dust, createdPokemon.getDust());
        assertEquals(candy, createdPokemon.getCandy());
    }

    @Test
    void testCreatePokemonWithNegativeIndex() {
        int index = -1; // Special case for Ash's Pikachu
        int cp = 600;
        int hp = 300;
        int dust = 1200;
        int candy = 30;

        Pokemon createdPokemon = pokemonFactory.createPokemon(index, cp, hp, dust, candy);

        assertNotNull(createdPokemon);
        assertEquals("Ash's Pikachu", createdPokemon.getName());
        assertEquals(1000, createdPokemon.getAttack());
        assertEquals(1000, createdPokemon.getDefense());
        assertEquals(1000, createdPokemon.getStamina());
        assertEquals(0, createdPokemon.getIv());
    }

    @Test
    void testRandomStatGeneration() {
        int index = 1; // Valid Pokémon
        int cp = 400;
        int hp = 250;
        int dust = 900;
        int candy = 20;

        Pokemon createdPokemon = pokemonFactory.createPokemon(index, cp, hp, dust, candy);

        assertNotNull(createdPokemon);
        // Validate that attack, defense, and stamina are within the expected range
        int attack = createdPokemon.getAttack();
        int defense = createdPokemon.getDefense();
        int stamina = createdPokemon.getStamina();

        assertEquals(1, createdPokemon.getIv(), "IV should be set to 1 for valid Pokémon");
        assertNotNull((Integer)attack);
        assertNotNull((Integer)defense);
        assertNotNull((Integer)stamina);
    }
}
