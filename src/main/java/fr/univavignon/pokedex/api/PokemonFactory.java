package fr.univavignon.pokedex.api;

import java.util.Random;

/**
 * Implementation of the IPokemonFactory interface.
 *
 * This class represents a factory for creating Pokemon instances.
 * It provides a method to create a Pokemon with computed IVs.
 *
 * Author: noobmaster122
 */
public class PokemonFactory implements IPokemonFactory {

    /**
     * The metadata provider used for retrieving Pokemon metadata.
     */
    private final IPokemonMetadataProvider metadataProvider;

    /**
     * Constructs a PokemonFactory with the given metadata provider.
     *
     * @param provider The metadata provider to use.
     */
    public PokemonFactory(final IPokemonMetadataProvider provider) {
        this.metadataProvider = provider;
    }

    /**
     *
     * @param index The index of the Pokemon.
     * @param cp The combat power of the Pokemon.
     * @param hp The hit points of the Pokemon.
     * @param dust The required dust for upgrading the Pokemon.
     * @param candy The required candy for upgrading the Pokemon.
     * @return The created Pokemon instance.
     */
    @Override
    public Pokemon createPokemon(final int index, final int cp, final int hp,
                                 final int dust, final int candy) {
        try {
            PokemonMetadata pokemonMetadata = metadataProvider
                    .getPokemonMetadata(index);

            Random rand = new Random();

            final int maxiv = 16;
            int ivAttack = rand.nextInt(maxiv);
            int ivDefense = rand.nextInt(maxiv);
            int ivStamina = rand.nextInt(maxiv);

            double iv = calculateIV(ivAttack, ivDefense, ivStamina);

            return new Pokemon(index, pokemonMetadata.getName(),
                    pokemonMetadata.getAttack() + ivAttack,
                    pokemonMetadata.getDefense() + ivDefense,
                    pokemonMetadata.getStamina() + ivStamina,
                    cp, hp, dust, candy, iv);

        } catch (PokedexException e) {
            System.err.println("Error while retrieving Pokemon metadata: "
                    + e.getMessage());
            return null;
        }
    }

    /**
     * Calculates the IV of a Pokemon based on its attack
     * defense, and stamina IVs.
     *
     * @param ivAttack The attack IV.
     * @param ivDefense The defense IV.
     * @param ivStamina The stamina IV.
     * @return The calculated IV value.
     */
    private double calculateIV(final int ivAttack,
                               final int ivDefense, final int ivStamina) {
        final int maxivvalue = 45;
        final int maxiv = 100;
        return ((double) (ivAttack + ivDefense + ivStamina)
                / maxivvalue) * maxiv;
    }
}