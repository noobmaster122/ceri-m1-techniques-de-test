package fr.univavignon.pokedex.api;

import java.util.Comparator;

/**
 * Enumeration of pokemon comparator.
 *
 * @author fv
 */
public enum PokemonComparators implements Comparator<Pokemon> {

	/** Comparator using Pokemon name. **/
	NAME(Comparator.comparing(Pokemon::getName)),

	/** Comparator using Pokemon index. **/
	INDEX(Comparator.comparing(Pokemon::getIndex)),

	/** Comparator using Pokemon combat point. **/
	CP(Comparator.comparing(Pokemon::getCp));

	/** The delegate comparator instance. **/
	private final Comparator<Pokemon> delegate;

	/**
	 * Default constructor.
	 *
	 * @param comparatorDelegate Delegate comparator instance.
	 */
	PokemonComparators(final Comparator<Pokemon> comparatorDelegate) {
		this.delegate = comparatorDelegate;
	}

	/**
	 * Get the delegate comparator instance.
	 *
	 * @return The delegate comparator.
	 */
	public Comparator<Pokemon> getDelegate() {
		return delegate;
	}

	/** {@inheritDoc} **/
	@Override
	public int compare(final Pokemon first, final Pokemon second) {
		return delegate.compare(first, second);
	}

}