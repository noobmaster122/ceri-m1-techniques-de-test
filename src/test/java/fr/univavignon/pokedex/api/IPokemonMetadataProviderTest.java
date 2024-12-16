package fr.univavignon.pokedex.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IPokemonMetadataProviderTest {

    private PokemonMetadataProvider metadataProvider;

    @BeforeEach
    public void setUp() {
        metadataProvider = new PokemonMetadataProvider();
        metadataProvider.addOrUpdateMetadata(133, "Aquali", 186, 168, 260);
    }

    @Test
    public void getPokemonMetadata_validIndexAquali() throws PokedexException {
        PokemonMetadata metadata = metadataProvider.getPokemonMetadata(133);

        assertNotNull(metadata, "Les métadonnées pour Aquali ne devraient pas être nulles");
        assertEquals(133, metadata.getIndex(), "L'index devrait être 133");
        assertEquals("Aquali", metadata.getName(), "Le nom devrait être Aquali");
        assertEquals(186, metadata.getAttack(), "L'attaque devrait être 186");
        assertEquals(168, metadata.getDefense(), "La défense devrait être 168");
        assertEquals(260, metadata.getStamina(), "L'endurance devrait être 260");
    }


    @Test
    public void testAddMetadata_invalidIndex() {
        assertThrows(IllegalArgumentException.class, () -> metadataProvider.addOrUpdateMetadata(151, "Test", 10, 10, 10),
                "L'ajout de métadonnées avec un index invalide (151) devrait lancer une IllegalArgumentException");
    }

    @Test
    public void getPokemonMetadata_MetadataNotFound() {
        int nonExistentIndex = 2;
        assertThrows(PokedexException.class, () -> metadataProvider.getPokemonMetadata(nonExistentIndex),
                "Une PokedexException doit être lancée si aucune métadonnée n'est trouvée pour l'index donné.");
    }

    @Test
    public void getPokemonMetadata_IndexTooLow() {
        assertThrows(PokedexException.class, () -> metadataProvider.getPokemonMetadata(0),
                "Accès à un index 0 devrait lancer une PokedexException.");
    }

    @Test
    public void getPokemonMetadata_IndexTooHigh() {
        assertThrows(PokedexException.class, () -> metadataProvider.getPokemonMetadata(151),
                "Accès à un index 151 devrait lancer une PokedexException.");
    }

    @Test
    public void addOrUpdateMetadata_IndexTooLow() {
        assertThrows(IllegalArgumentException.class,
                () -> metadataProvider.addOrUpdateMetadata(0, "TestLow", 10, 10, 10),
                "L'ajout de métadonnées avec un index 0 devrait lancer une IllegalArgumentException.");
    }

    @Test
    public void addOrUpdateMetadata_IndexTooHigh() {
        assertThrows(IllegalArgumentException.class,
                () -> metadataProvider.addOrUpdateMetadata(151, "TestHigh", 10, 10, 10),
                "L'ajout de métadonnées avec un index 151 devrait lancer une IllegalArgumentException.");
    }

}