package fr.univavignon.pokedex.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Mocks initiaux
@ExtendWith(MockitoExtension.class)
public class IPokedexFactoryTest {

    private static final Logger logger = LoggerFactory.getLogger(IPokedexFactoryTest.class);

    @Mock
    private IPokemonMetadataProvider metadataProvider;

    @Mock
    private IPokemonFactory pokemonFactory;

    @Mock
    private PokedexFactory pokedexFactory;

    private IPokedex pokedex;
    private Pokemon pikachu;
    private Pokemon bulbasaur;


    // Nouveau mock
    @BeforeEach
    void setUp() {
        pokedexFactory = new PokedexFactory();

        pokedex = mock(IPokedex.class, withSettings().lenient());
        pikachu = new Pokemon(0, "Pikachu", 55, 40, 90, 260, 35, 500, 50, 0.6);
        bulbasaur = new Pokemon(1, "Bulbasaur", 45, 49, 45, 230, 30, 500, 50, 0.5);

        try {
            when(pokedex.addPokemon(any(Pokemon.class))).thenAnswer(i -> {
                Pokemon p = i.getArgument(0);
                return p.getIndex();
            });
            when(pokedex.getPokemon(0)).thenReturn(pikachu);
            when(pokedex.getPokemons()).thenReturn(Arrays.asList(pikachu, bulbasaur));
            when(pokedex.getPokemons(any())).thenReturn(Arrays.asList(bulbasaur, pikachu));

            doThrow(new PokedexException("Invalid index")).when(pokedex).getPokemon(-1);
        } catch (PokedexException e) {
            logger.error("Une exception de Pokedex s'est produite", e);
        }

    }

    // Nouveau mock utilisé dans cette méthode
    @Test
    void testCreatePokedex() {
        IPokedex createdPokedex = pokedexFactory.createPokedex(metadataProvider, pokemonFactory);
        assertNotNull(createdPokedex, "Le pokedex créé ne doit pas être null");

    }

    // Nouveau mock utilisé dans cette méthode
    @Test
    void testGetPokemonInvalidIndexException() throws PokedexException {
        doThrow(new PokedexException("Invalid index")).when(pokedex).getPokemon(-1);
        IPokedex createdPokedex = pokedexFactory.createPokedex(metadataProvider, pokemonFactory);
        assertThrows(PokedexException.class, () -> createdPokedex.getPokemon(-1),
                "Accès à un index invalide doit lever une PokedexException.");
    }


    // Nouveau mock utilisé dans cette méthode
    @Test
    void testPokedexSizeAfterAddingPokemons() {
        when(pokedex.addPokemon(any(Pokemon.class))).thenReturn(0).thenReturn(1);
        when(pokedex.size()).thenReturn(2);
        IPokedex createdPokedex = pokedexFactory.createPokedex(metadataProvider, pokemonFactory);

        createdPokedex.addPokemon(pikachu);
        createdPokedex.addPokemon(bulbasaur);

        assertEquals(2, createdPokedex.size(), "Taille du Pokedex doit être 2 après ajout de deux Pokémons.");
    }



}
