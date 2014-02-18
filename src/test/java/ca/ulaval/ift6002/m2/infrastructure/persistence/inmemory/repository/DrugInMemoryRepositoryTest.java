package ca.ulaval.ift6002.m2.infrastructure.persistence.inmemory.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.ift6002.m2.domain.drug.CSVDrugDataAdapter;
import ca.ulaval.ift6002.m2.domain.drug.Din;
import ca.ulaval.ift6002.m2.domain.drug.Drug;
import ca.ulaval.ift6002.m2.domain.drug.DrugRepository;
import ca.ulaval.ift6002.m2.infrastructure.persistence.inmemory.DataAdapter;

@RunWith(MockitoJUnitRunner.class)
public class DrugInMemoryRepositoryTest {

    private static final Din TYLENOL_DIN = new Din("111111");
    private static final Din TYLANETOL_DIN = new Din("222222");
    private static final Din UNEXISTING_DIN = new Din("abcde");

    private static final String LESS_THAN_THREE_CHARACTERS_DRUG_NAME = "TY";

    private static final String UNEXISTING_BRAND_NAME = "UNEXISTING_BRAND_NAME";
    private static final String UNEXISTING_DESCRIPTOR = "UNEXISTING_DESCRIPTOR";

    private static final String TYLENOL_BRAND_NAME = "TYLENOL";
    private static final String TYLENOL_DESCRIPTOR_NAME = "ACETAMINOPHENE";

    private static final Drug TYLENOL = new Drug(TYLENOL_DIN, TYLENOL_BRAND_NAME, TYLENOL_DESCRIPTOR_NAME);
    private static final Drug TYLANETOL = new Drug(TYLANETOL_DIN, "TYLANETOL PRIME", "IBUPROPHENE");

    private static final List<Drug> ALL_DRUGS = Arrays.asList(TYLENOL, TYLANETOL);

    private static final String CAMEL_CASE_TYLENOL_BRAND_NAME = "TyLeNoL";

    private static final String SIMPLE_SEARCH_PATTERN = "TYL";
    private static final String SEARCH_PATTERN_WILDCARD = "TYL PRI";
    private static final String PATTERN_WITH_MULTIPLE_WILDCARDS = "TY NE OL PRI ME";
    private static final String INVALID_KEYWORD = "123" + SEARCH_PATTERN_WILDCARD + "123";

    private DrugRepository drugRepository;

    private final DataAdapter<Drug> drugDataAdapter = mock(CSVDrugDataAdapter.class);

    @Before
    public void setup() {
        setupDataAdapter();

        drugRepository = new DrugInMemoryRepository(drugDataAdapter);
    }

    private void setupDataAdapter() {
        willReturn(ALL_DRUGS).given(drugDataAdapter).retrieveData();
    }

    @Test
    public void givenRepositoryWhenGetByDinWithExistingDinShouldReturnCorrespondingDrug() {
        Drug drugFound = drugRepository.get(TYLENOL_DIN);

        assertEquals(TYLENOL, drugFound);
    }

    @Test(expected = NoSuchElementException.class)
    public void givenRepositoryWhenGetByDinWithUnexistingDinShouldThrowException() {
        drugRepository.get(UNEXISTING_DIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByBrandNameOrDescriptorWhenLessThanThreeCharactersShouldThrowException() {
        drugRepository.findByBrandNameOrDescriptor(LESS_THAN_THREE_CHARACTERS_DRUG_NAME);
    }

    @Test
    public void findByBrandNameOrDescriptorWithExistingBrandNameShouldNotBeEmpty() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(TYLENOL_BRAND_NAME);
        boolean result = drugsFound.isEmpty();
        assertFalse(result);
    }

    @Test
    public void findByBrandNameOrDescriptorWithTylenolBrandNameShouldReturnTylenolDrug() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(TYLENOL_BRAND_NAME);
        boolean result = drugsFound.contains(TYLENOL);
        assertTrue(result);
    }

    @Test
    public void findByBrandNameOrDescriptorWithExistingDescriptorShouldNotBeEmpty() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(TYLENOL_DESCRIPTOR_NAME);
        boolean result = drugsFound.isEmpty();
        assertFalse(result);
    }

    @Test
    public void findByBrandNameOrDescriptorWithTylenolDescriptorShouldReturnTylenolDrug() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(TYLENOL_DESCRIPTOR_NAME);
        boolean result = drugsFound.contains(TYLENOL);
        assertTrue(result);
    }

    @Test(expected = NoSuchElementException.class)
    public void findByBrandNameOrDescriptorWithUnexistingBrandNameShouldReturnException() {
        drugRepository.findByBrandNameOrDescriptor(UNEXISTING_BRAND_NAME);
    }

    @Test(expected = NoSuchElementException.class)
    public void findByBrandNameOrDescriptorWithUnexistingDescriptorShouldReturnException() {
        drugRepository.findByBrandNameOrDescriptor(UNEXISTING_DESCRIPTOR);
    }

    @Test
    public void findByBrandNameOrDescriptorWhenUsingValidPatternShouldReturnTwoDrugs() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(SIMPLE_SEARCH_PATTERN);
        int result = drugsFound.size();
        assertEquals(2, result);
    }

    @Test
    public void findByBrandNameOrDescriptorWhenUsingSimpleKeywordShouldContainsAllDrugsWithSameBeginning() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(SIMPLE_SEARCH_PATTERN);

        boolean resultContainsTylenol = drugsFound.contains(TYLENOL);
        boolean resultContainsTylanetol = drugsFound.contains(TYLANETOL);

        assertTrue(resultContainsTylenol);
        assertTrue(resultContainsTylanetol);
    }

    @Test
    public void findByBrandNameOrDescriptorWhenUsingPatternWithTwoWordsShouldReturnDrugs() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(SEARCH_PATTERN_WILDCARD);
        boolean result = drugsFound.isEmpty();
        assertFalse(result);
    }

    @Test
    public void findByBrandNameOrDescriptorWhenUsingPatternWithTwoWordShouldContainsSpecificDrug() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(SEARCH_PATTERN_WILDCARD);
        boolean result = drugsFound.contains(TYLANETOL);
        assertTrue(result);
    }

    @Test
    public void findByBrandNameOrDescriptorWhenUsingPatternWithMultipleWildcardsShouldContainsSpecificDrug() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(PATTERN_WITH_MULTIPLE_WILDCARDS);
        boolean result = drugsFound.contains(TYLANETOL);
        assertTrue(result);
    }

    @Test(expected = NoSuchElementException.class)
    public void findByBrandNameOrDescriptorWhenUsingInvalidPatternShouldThrowException() {
        drugRepository.findByBrandNameOrDescriptor(INVALID_KEYWORD);
    }

    @Test
    public void findByBrandNameOrDescriptorWithCamelCaseShouldReturnDrugs() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(CAMEL_CASE_TYLENOL_BRAND_NAME);
        boolean result = drugsFound.isEmpty();
        assertFalse(result);
    }

    @Test
    public void findByBrandNameOrDescriptorWithTylenolBrandNameCamelCaseShouldReturnTylenolDrug() {
        Collection<Drug> drugsFound = drugRepository.findByBrandNameOrDescriptor(CAMEL_CASE_TYLENOL_BRAND_NAME);
        boolean result = drugsFound.contains(TYLENOL);
        assertTrue(result);
    }

}