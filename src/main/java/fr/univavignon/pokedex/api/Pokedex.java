package fr.univavignon.pokedex.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of the IPokedex interface.
 * Represents a Pokedex.
 * Provides methods to manage a collection of Pokemon.
 *
 * Author: noobmaster122
 */
public class Pokedex implements IPokedex {

    /**.
     * Provider for Pokemon Metadata
     */
    private final IPokemonMetadataProvider metadataProvider;

    /**
     * The IPokemonFactory instance used for creating Pokemon instances.
     */
    private final IPokemonFactory pokemonFactory;

    /**
     * The list of Pokemons in the Pokedex.
     */
    private final List<Pokemon> pokemons = new ArrayList<>();

    /**
     *Constructs a Pokedex with given providers.
     *
     * @param metadataProv The metadata provider to use.
     * @param pokemonFact The Pokemon factory to use.
     */
    public Pokedex(
            final IPokemonMetadataProvider metadataProv,
            final IPokemonFactory pokemonFact
    ) {
        this.metadataProvider = metadataProv;
        this.pokemonFactory = pokemonFact;
    }

    /**
     * Returns the number of Pokemon in the Pokedex.
     *
     * @return The number of Pokemon in the Pokedex.
     */
    @Override
    public int size() {
        return pokemons.size();
    }

    /**
     * Adds a Pokemon to the Pokedex.
     *
     * @param pokemon The Pokemon to add.
     * @return The index of the added Pokemon in the Pokedex.
     */
    @Override
    public int addPokemon(final Pokemon pokemon) {
        pokemons.add(pokemon);
        return pokemons.indexOf(pokemon);
    }

    /**
     * Retrieves a Pokemon from the Pokedex based on its ID.
     *
     * @param id The ID of the Pokemon to retrieve.
     * @return The Pokemon with the specified ID.
     * @throws PokedexException If the specified ID is invalid.
     */
    @Override
    public Pokemon getPokemon(final int id) throws PokedexException {
        if (id < 0 || id >= pokemons.size()) {
            throw new PokedexException("Invalid ID specified: " + id);
        }
        return pokemons.get(id);
    }


    /**
     * Returns an unmodifiable list of all Pokemon in the Pokedex.
     *
     * @return An unmodifiable list of all Pokemon.
     */
    @Override
    public List<Pokemon> getPokemons() {
        return Collections.unmodifiableList(pokemons);
    }

    /**
     * @param order The comparator used for sorting the Pokemon list.
     * @return all Pokemon sorted by the specified comparator.
     */
    @Override
    public List<Pokemon> getPokemons(final Comparator<Pokemon> order) {
        List<Pokemon> sortedPokemons = new ArrayList<>(pokemons);
        sortedPokemons.sort(order);
        return Collections.unmodifiableList(sortedPokemons);
    }

    /**
     * Creates a new Pokemon instance with the specified attributes.
     *
     * @param index The index of the Pokemon.
     * @param combatPower The combat power of the Pokemon.
     * @param hitPoints The hit points of the Pokemon.
     * @param upgradeDust The required dust for upgrading the Pokemon.
     * @param upgradeCandy The required candy for upgrading the Pokemon.
     * @return The newly created Pokemon instance.
     */
    @Override
    public Pokemon createPokemon(
            final int index,
            final int combatPower,
            final int hitPoints,
            final int upgradeDust,
            final int upgradeCandy
    ) {
        return pokemonFactory.createPokemon(
                index, combatPower, hitPoints, upgradeDust, upgradeCandy
        );
    }


    /**
     * Retrieves the metadata for a Pokemon with the specified index.
     *
     * @param index The index of the Pokemon.
     * @return The metadata for the Pokemon.
     * @throws PokedexException If metadata retrieval fails.
     */
    @Override
    public PokemonMetadata getPokemonMetadata(final int index) throws
            PokedexException {
        try {
            return metadataProvider.getPokemonMetadata(index);
        } catch (Exception e) {
            throw new PokedexException(
                    "Failed to retrieve metadata for Pokemon with index "
                            + index + "."
            );
        }
    }
}