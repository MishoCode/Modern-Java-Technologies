package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CocktailStorageTest {

    private CocktailStorage storage;

    private final Set<Ingredient> ingredientSet1 = Set.of(
        new Ingredient("water", "500ml"),
        new Ingredient("beer", "200ml"),
        new Ingredient("vinegar", "150ml"));

    private final Set<Ingredient> ingredientSet2 = Set.of(
        new Ingredient("whiskey", "100ml"),
        new Ingredient("juice", "120ml")
    );

    private final Set<Ingredient> ingredientSet3 = Set.of(
        new Ingredient("bottle", "1"),
        new Ingredient("gasoline", "500ml"),
        new Ingredient("rag", "1")
    );

    @BeforeEach
    public void setUp() throws CocktailAlreadyExistsException {
        storage = new DefaultCocktailStorage();

        storage.createCocktail(new Cocktail("poison", ingredientSet1));
        storage.createCocktail(new Cocktail("manhattan", ingredientSet2));
    }

    @Test
    public void testCreateCocktailWhenCocktailIsNull() {
        String assertMessage =
            "IllegalArgumentException is expected to be thrown when cocktail is null";

        assertThrows(IllegalArgumentException.class,
            () -> storage.createCocktail(null), assertMessage);
    }

    @Test
    public void testCreateCocktailWhenTheCocktailAlreadyExists() {
        String assertMessage =
            "CocktailAlreadyExistsException is expected to be thrown when the cocktail recipe is already in the storage";

        assertThrows(CocktailAlreadyExistsException.class,
            () -> storage.createCocktail(new Cocktail("manhattan", ingredientSet2)), assertMessage);
    }

    @Test
    public void testCreateCocktail() throws CocktailAlreadyExistsException {
        String assertMessage = "createCocktail does not add new cocktail to the storage";

        storage.createCocktail(new Cocktail("Molotov", ingredientSet3));
        int expected = 3;
        int actual = storage.getCocktails().size();

        assertEquals(expected, actual, assertMessage);
    }

    @Test
    public void testGetCocktailsReturnsEmptyCollection() {
        String assertMessage =
            "getCocktails does not return empty collection when the storage is empty";

        CocktailStorage emptyStorage = new DefaultCocktailStorage();

        assertTrue(emptyStorage.getCocktails().isEmpty(), assertMessage);
    }

    @Test
    public void testGetCocktails() {
        String assertMessage =
            "getCocktails does not return the cocktails in the storage properly";

        List<Cocktail> expected = List.of(
            new Cocktail("poison", ingredientSet1),
            new Cocktail("manhattan", ingredientSet2)
        );
        Collection<Cocktail> actual = storage.getCocktails();

        assertTrue(assertCollectionsEqual(expected, actual), assertMessage);
    }

    @Test
    public void testGetCocktailsWithIngredientNameWithInvalidName() {
        String assertMessage =
            "IllegalArgumentException is expected to be thrown when ingredientName is null or an empty string";

        assertThrows(IllegalArgumentException.class,
            () -> storage.getCocktailsWithIngredient(null), assertMessage);

        assertThrows(IllegalArgumentException.class,
            () -> storage.getCocktailsWithIngredient(""), assertMessage);
    }

    @Test
    public void testGetCocktailsWithIngredientReturnEmptyCollection() {
        String assertMessage =
            "getCocktailWithIngredient is expected to return empty collection when no recipe contains this ingredient";

        assertTrue(storage.getCocktailsWithIngredient("test").isEmpty(), assertMessage);
    }

    @Test
    public void testGetCocktailsWithIngredient() {
        String assertMessage =
            "getCocktailsWithIngredient does not return the correct cocktails";

        List<Cocktail> expected = List.of(new Cocktail("manhattan", ingredientSet2));
        Collection<Cocktail> actual = storage.getCocktailsWithIngredient("whiskey");

        assertTrue(assertCollectionsEqual(expected, actual), assertMessage);
    }

    @Test
    public void testGetCocktailWhenNameIsNull() {
        String assertMessage =
            "IllegalArgumentException is expected to be thrown when name is null or an empty string";

        assertThrows(IllegalArgumentException.class,
            () -> storage.getCocktail(null), assertMessage);

        assertThrows(IllegalArgumentException.class,
            () -> storage.getCocktail(""), assertMessage);
    }

    @Test
    public void testGetCocktailWhenTheCocktailIsNotInTheStorage() {
        String assertMessage =
            "CocktailNotFoundException is expected to be thrown when the cocktail is not in the storage";

        assertThrows(CocktailNotFoundException.class,
            () -> storage.getCocktail("unknown"), assertMessage);
    }

    @Test
    public void testGetCocktail() throws CocktailNotFoundException {
        String assertMessage =
            "getCocktail does not return the correct cocktail";

        Cocktail expected = new Cocktail("manhattan", ingredientSet1);
        Cocktail actual = storage.getCocktail("manhattan");

        assertEquals(expected, actual, assertMessage);
    }

    private boolean assertCollectionsEqual(Collection<Cocktail> expected, Collection<Cocktail> actual) {
        return actual.containsAll(expected) && expected.containsAll(actual);
    }
}
