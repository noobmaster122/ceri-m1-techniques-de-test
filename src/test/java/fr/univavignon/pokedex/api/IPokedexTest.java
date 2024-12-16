package fr.univavignon.pokedex.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IPokedexTest {

    private IPokedex pokedex;
    private Pokemon pikachu;
    private Pokemon bulbasaur;


    // Mocks initiaux
    @BeforeEach
    public void setup() {
        pokedex = mock(IPokedex.class);
        pikachu = new Pokemon(0, "Pikachu", 55, 40, 90, 260, 35, 500, 50, 0.6);
        bulbasaur = new Pokemon(1, "Bulbasaur", 45, 49, 45, 230, 30, 500, 50, 0.5);

        try {
            when(pokedex.size()).thenReturn(2);
            when(pokedex.addPokemon(any(Pokemon.class))).thenAnswer(i -> {
                Pokemon p = i.getArgument(0);
                return p.getIndex();
            });
            when(pokedex.getPokemon(0)).thenReturn(pikachu);
            when(pokedex.getPokemon(1)).thenReturn(bulbasaur);
            when(pokedex.getPokemons()).thenReturn(Arrays.asList(pikachu, bulbasaur));
            when(pokedex.getPokemons(any(Comparator.class))).thenReturn(Arrays.asList(bulbasaur, pikachu));

            doThrow(new PokedexException("Invalid index")).when(pokedex).getPokemon(-1);
        } catch (PokedexException e) {

            e.printStackTrace();
        }
    }

    // Mocks initiaux
    @Test
    public void testAddPokemon() {

        int indexPikachu = pokedex.addPokemon(pikachu);
        int indexBulbasaur = pokedex.addPokemon(bulbasaur);
        assertNotEquals(indexPikachu, indexBulbasaur, "chaque Pokemon doit avoir un unique index.");
    }

    // Mocks initiaux
    @Test
    public void testGetSize() {
        // On test la taille du Pokedex
        assertEquals(2, pokedex.size());
    }

    // Mocks initiaux
    @Test
    public void testGetPokemonValidIndex() {

        try {
            Pokemon resultPikachu = pokedex.getPokemon(0);
            Pokemon resultBulbasaur = pokedex.getPokemon(1);
            assertEquals(pikachu, resultPikachu, "Should return Pikachu.");
            assertEquals(bulbasaur, resultBulbasaur, "Should return Bulbasaur.");
        } catch (PokedexException e) {
            fail("PokedexException should not be thrown for valid indexes.");
        }
    }


    // Mocks initiaux
    @Test
    public void testGetPokemons() {
        List<Pokemon> pokemons = pokedex.getPokemons();
        assertNotNull(pokemons);
        assertEquals(2, pokemons.size());
    }

    // Mocks initiaux
    @Test
    public void testGetPokemonThrowsException() {
        // Test pour vérifier le comportement lorsqu'un index invalide est utilisé
        assertThrows(PokedexException.class, () -> pokedex.getPokemon(-1));
    }

// Mocks initiaux

    @Test
    public void testGetPokemonsSortedByName() {
        List<Pokemon> sortedPokemons = pokedex.getPokemons(PokemonComparators.NAME);
        assertEquals("Bulbasaur", sortedPokemons.get(0).getName(), "Le premier Pokémon devrait être Bulbasaur lors du tri par nom.");
        assertEquals("Pikachu", sortedPokemons.get(1).getName(), "Le deuxième Pokémon devrait être Pikachu lors du tri par nom.");

        int comparisonResultName = PokemonComparators.NAME.compare(sortedPokemons.get(0), sortedPokemons.get(1));
        assertTrue(comparisonResultName < 0, "Bulbasaur devrait venir avant Pikachu lors du tri par nom.");
    }

    // Mocks initiaux
    @Test
    public void testGetPokemonsSortedByIndex() {
        when(pokedex.getPokemons(PokemonComparators.INDEX)).thenReturn(Arrays.asList(pikachu, bulbasaur));

        List<Pokemon> sortedPokemons = pokedex.getPokemons(PokemonComparators.INDEX);
        assertEquals(pikachu.getIndex(), sortedPokemons.get(0).getIndex(), "Le premier Pokémon devrait être Pikachu lors du tri par index.");
        assertEquals(bulbasaur.getIndex(), sortedPokemons.get(1).getIndex(), "Le deuxième Pokémon devrait être Bulbasaur lors du tri par index.");
    }

    @Test
    public void testPokemonCreationThroughPokedex() throws PokedexException {
        IPokemonMetadataProvider metadataProvider = mock(IPokemonMetadataProvider.class);
        IPokemonFactory pokemonFactory = new PokemonFactory(metadataProvider);
        IPokedex pokedex = new Pokedex(metadataProvider, pokemonFactory);

        when(metadataProvider.getPokemonMetadata(1)).thenReturn(new PokemonMetadata(1, "Bulbasaur", 60, 62, 63));
        Pokemon bulbasaur = pokedex.createPokemon(1, 600, 100, 5000, 4);

        assertNotNull(bulbasaur, "La création de Pokémon à travers le Pokedex a échoué.");
        assertEquals("Bulbasaur", bulbasaur.getName(), "Le nom du Pokémon créé ne correspond pas.");
        assertTrue(bulbasaur.getAttack() >= 60 && bulbasaur.getAttack() <= 75, "L'attaque du Pokémon n'est pas dans la plage attendue.");

    }

    // Nouveau mock utilisé dans cette méthode
    @Test
    public void testCreatePokemonHandlesPokedexException() throws PokedexException {

        IPokemonMetadataProvider metadataProviderMock = mock(IPokemonMetadataProvider.class);
        when(metadataProviderMock.getPokemonMetadata(anyInt())).thenThrow(new PokedexException("Erreur de récupération des métadonnées"));
        IPokemonFactory pokemonFactory = new PokemonFactory(metadataProviderMock);

        Pokemon result = pokemonFactory.createPokemon(0, 100, 100, 1000, 10);
        assertNull(result, "La méthode createPokemon devrait retourner null en cas d'échec de récupération des métadonnées.");

        verify(metadataProviderMock).getPokemonMetadata(0);
    }


    // Nouveau mock utilisé dans cette méthode
    @Test
    public void testGetPokemonMetadataFailure() throws PokedexException {
        IPokemonMetadataProvider mockMetadataProvider = mock(IPokemonMetadataProvider.class);
        IPokemonFactory mockPokemonFactory = mock(IPokemonFactory.class);
        IPokedex pokedex = new Pokedex(mockMetadataProvider, mockPokemonFactory);

        int invalidIndex = 200;
        when(mockMetadataProvider.getPokemonMetadata(invalidIndex)).thenThrow(new PokedexException("Métadonnées non trouvées"));

        assertThrows(PokedexException.class, () -> pokedex.getPokemonMetadata(invalidIndex), "Devrait lancer une PokedexException en cas d'échec de récupération des métadonnées");
    }

    // Nouveau mock utilisé dans cette méthode
    @Test
    public void testGetPokemonByIdWithRealInstance() throws PokedexException {
        IPokemonMetadataProvider metadataProvider = mock(IPokemonMetadataProvider.class);
        IPokemonFactory pokemonFactory = mock(IPokemonFactory.class);

        Pokedex pokedex = new Pokedex(metadataProvider, pokemonFactory);
        when(pokemonFactory.createPokemon(eq(0), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(pikachu);
        when(pokemonFactory.createPokemon(eq(1), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(bulbasaur);

        int pikachuId = pokedex.addPokemon(pikachu);
        int bulbasaurId = pokedex.addPokemon(bulbasaur);

        assertEquals(pikachu, pokedex.getPokemon(pikachuId), "Le Pokémon récupéré avec l'ID devrait être Pikachu.");
        assertEquals(bulbasaur, pokedex.getPokemon(bulbasaurId), "Le Pokémon récupéré avec l'ID devrait être Bulbasaur.");
    }

    // Nouveau mock utilisé dans cette méthode
    @Test
    public void testGetPokemonsReturnsUnmodifiableList() throws PokedexException {

        IPokemonMetadataProvider metadataProvider = mock(IPokemonMetadataProvider.class);
        IPokemonFactory pokemonFactory = mock(IPokemonFactory.class);

        Pokedex realPokedex = new Pokedex(metadataProvider, pokemonFactory);

        Pokemon testPokemon = new Pokemon(0, "Test Pokemon", 100, 100, 100, 1000, 100, 10, 1, 0.5);
        when(pokemonFactory.createPokemon(anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(testPokemon);
        realPokedex.addPokemon(testPokemon);

        List<Pokemon> pokemons = realPokedex.getPokemons();
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> pokemons.add(testPokemon),
                "La modification de la liste des Pokémon devrait lancer une UnsupportedOperationException.");

        assertNotNull(exception, "Une exception devrait être levée lors de la tentative de modification de la liste.");
    }

    // Nouveau mock utilisé dans cette méthode
    @Test
    public void testGetPokemonsSortedAndUnmodifiable() throws PokedexException {

        Pokedex realPokedex = new Pokedex(mock(IPokemonMetadataProvider.class), mock(IPokemonFactory.class));

        realPokedex.addPokemon(new Pokemon(0, "Bulbasaur", 45, 49, 45, 230, 30, 500, 50, 0.5));
        realPokedex.addPokemon(new Pokemon(1, "Charmander", 39, 52, 43, 220, 25, 500, 50, 0.6));
        realPokedex.addPokemon(new Pokemon(2, "Squirtle", 44, 48, 65, 225, 28, 500, 50, 0.7));

        Comparator<Pokemon> byName = Comparator.comparing(Pokemon::getName);

        List<Pokemon> sortedPokemons = realPokedex.getPokemons(byName);

        assertEquals("Bulbasaur", sortedPokemons.get(0).getName());
        assertEquals("Charmander", sortedPokemons.get(1).getName());
        assertEquals("Squirtle", sortedPokemons.get(2).getName());
        assertThrows(UnsupportedOperationException.class, () -> sortedPokemons.add(new Pokemon(3, "Pikachu", 55, 40, 90, 260, 35, 500, 50, 0.8)));
    }


    // Nouveau mock utilisé dans cette méthode
    @Test
    public void testGetPokemonMetadata() throws PokedexException {
        IPokemonMetadataProvider mockMetadataProvider = mock(IPokemonMetadataProvider.class);
        IPokemonFactory mockPokemonFactory = mock(IPokemonFactory.class);

        Pokedex pokedex = new Pokedex(mockMetadataProvider, mockPokemonFactory);
        int testIndex = 25;
        PokemonMetadata expectedMetadata = new PokemonMetadata(testIndex, "Pikachu", 55, 40, 35);
        when(mockMetadataProvider.getPokemonMetadata(testIndex)).thenReturn(expectedMetadata);

        PokemonMetadata resultMetadata = pokedex.getPokemonMetadata(testIndex);
        assertEquals(expectedMetadata, resultMetadata, "Les métadonnées retournées devraient correspondre à celles attendues.");
        verify(mockMetadataProvider).getPokemonMetadata(testIndex);
    }


    // Nouveau mock utilisé dans cette méthode
    @Test
    public void testGetPokemonWithNegativeIdThrowsException() {
        IPokemonMetadataProvider mockMetadataProvider = mock(IPokemonMetadataProvider.class);
        IPokemonFactory mockPokemonFactory = mock(IPokemonFactory.class);
        Pokedex pokedex = new Pokedex(mockMetadataProvider, mockPokemonFactory);

        int negativeId = -1;

        assertThrows(PokedexException.class, () -> pokedex.getPokemon(negativeId),
                "Demander un Pokémon avec un ID négatif devrait lever une PokedexException.");
    }


    // Nouveau mock utilisé dans cette méthode
    @Test
    public void testGetPokemonWithIdBeyondSizeThrowsException() throws PokedexException {
        IPokemonMetadataProvider mockMetadataProvider = mock(IPokemonMetadataProvider.class);
        IPokemonFactory mockPokemonFactory = mock(IPokemonFactory.class);
        Pokedex pokedex = new Pokedex(mockMetadataProvider, mockPokemonFactory);

        Pokemon pikachu = new Pokemon(25, "Pikachu", 55, 40, 90, 260, 35, 500, 50, 0.6);
        pokedex.addPokemon(pikachu);

        int idBeyondSize = pokedex.size();
        assertThrows(PokedexException.class, () -> pokedex.getPokemon(idBeyondSize),
                "Demander un Pokémon avec un ID supérieur ou égal à la taille de la liste devrait lever une PokedexException.");
    }


    // Nouveau mock utilisé dans cette méthode
    @Test
    public void testSortPokemonUsingComparators() {
        List<Pokemon> pokemons = Arrays.asList(
                new Pokemon(0, "Pikachu", 55, 40, 90, 260, 35, 500, 50, 0.6),
                new Pokemon(1, "Bulbasaur", 45, 49, 45, 230, 30, 500, 50, 0.5)
        );

        List<Pokemon> sortedByName = new ArrayList<>(pokemons);
        sortedByName.sort(PokemonComparators.NAME.getDelegate());

        assertEquals("Bulbasaur", sortedByName.get(0).getName(), "Bulbasaur should be first when sorted by NAME.");
        assertEquals("Pikachu", sortedByName.get(1).getName(), "Pikachu should be second when sorted by NAME.");


    }


}