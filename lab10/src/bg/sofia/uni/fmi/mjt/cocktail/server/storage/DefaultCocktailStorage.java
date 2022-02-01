package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultCocktailStorage implements CocktailStorage {
    private final Map<String, Cocktail> cocktails;

    public DefaultCocktailStorage() {
        cocktails = new HashMap<>();
    }

    @Override
    public void createCocktail(Cocktail cocktail) throws CocktailAlreadyExistsException {
        if (cocktail == null) {
            throw new IllegalArgumentException("cocktail cannot be null");
        }

        if (cocktails.containsKey(cocktail.name())) {
            throw new CocktailAlreadyExistsException("The same cocktail already exists.");
        }

        cocktails.put(cocktail.name(), cocktail);
    }

    @Override
    public Collection<Cocktail> getCocktails() {
        return cocktails.values();
    }

    @Override
    public Collection<Cocktail> getCocktailsWithIngredient(String ingredientName) {
        if (ingredientName == null || ingredientName.isEmpty()) {
            throw new IllegalArgumentException("ingredientName cannot be null or an empty string.");
        }

        return cocktails.values().stream()
            .filter(c -> containsIngredient(c, ingredientName))
            .collect(Collectors.toSet());
    }

    @Override
    public Cocktail getCocktail(String name) throws CocktailNotFoundException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or an empty string.");
        }

        if (!cocktails.containsKey(name)) {
            throw new CocktailNotFoundException("Cocktail with this name is not found in the storage.");
        }

        return cocktails.get(name);
    }

    private boolean containsIngredient(Cocktail cocktail, String ingredientName) {
        Set<Ingredient> ingredients = cocktail.ingredients();
        for (Ingredient i : ingredients) {
            if (i.name().equals(ingredientName)) {
                return true;
            }
        }

        return false;
    }
}
