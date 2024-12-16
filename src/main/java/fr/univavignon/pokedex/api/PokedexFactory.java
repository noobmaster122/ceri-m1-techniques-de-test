package fr.univavignon.pokedex.api;

/**
 * Factory for creating Pokedex instances.
 */
public class PokedexFactory implements IPokedexFactory {

    /**
     * Creates and returns a new instance of Pokedex.
     *
     * @param metadataProvider The metadata provider for Pokemons, necessary
     *                         for initializing the Pokedex.
     * @param pokemonFactory The factory for creating new Pokemon instances.
     * @return A new Pokedex initialized with the specified metadata and
     *         Pokemon factory providers.
     */
    @Override
    public IPokedex createPokedex(
            final IPokemonMetadataProvider metadataProvider,
            final IPokemonFactory pokemonFactory
    ) {
        return new Pokedex(
                metadataProvider,
                pokemonFactory
        );
    }
}