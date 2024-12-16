package fr.univavignon.pokedex.api;

/**
 * Factory class for creating Pokemon trainers.
 * Offers functionality to create trainers with specific teams and Pokedexes.
 */
public class PokemonTrainerFactory implements IPokemonTrainerFactory {

    /**
     * Metadata provider used by the factory.
     */
    private final IPokemonMetadataProvider metadataProvider;

    /**
     * Pokemon factory used by the factory.
     */
    private final IPokemonFactory pokemonFactory;

    /**
     * Constructs factory with specified metadata provider,Pokemon factory.
     *
     * @param metadataProv Metadata provider to use.
     * @param pokemonFact  Pokemon factory to use.
     */
    public PokemonTrainerFactory(final IPokemonMetadataProvider metadataProv,
                                 final IPokemonFactory pokemonFact) {
        this.metadataProvider = metadataProv;
        this.pokemonFactory = pokemonFact;
    }

    /**
     * Creates Pokemon trainer with specified name, team,Pokedex factory.
     *
     * @param name            Trainer's name.
     * @param team            Trainer's team.
     * @param pokedexFactory  Factory for the trainer's Pokedex.
     * @return Newly created Pokemon trainer.
     */
    @Override
    public PokemonTrainer createTrainer(final String name, final Team team,
                                        final IPokedexFactory pokedexFactory) {
        IPokedex pokedex = pokedexFactory.createPokedex(
                metadataProvider,
                pokemonFactory
        );
        return new PokemonTrainer(name, team, pokedex);
    }
}