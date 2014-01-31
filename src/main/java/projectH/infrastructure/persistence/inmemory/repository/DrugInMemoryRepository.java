package projectH.infrastructure.persistence.inmemory.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import projectH.domain.drug.Drug;
import projectH.domain.drug.DrugRepository;
import projectH.infrastructure.persistence.inmemory.ListingRepository;
import au.com.bytecode.opencsv.CSVReader;

public class DrugInMemoryRepository extends ListingRepository<Drug> implements DrugRepository {

	private static final String DRUG_FILE = "/drug.txt";
	private static final int MIN_LENGTH_OF_SEARCH_KEYWORD = 3;

	@Override
	public List<Drug> findByBrandNameOrDescriptor(String keyword) {
		if (keyword.length() < MIN_LENGTH_OF_SEARCH_KEYWORD) {
			throw new IllegalArgumentException("The minimum character's length is: " + MIN_LENGTH_OF_SEARCH_KEYWORD);
		}

		List<Drug> drugs = new ArrayList<Drug>();
		// Pattern style .*word.*otherword.* (exemple: len ace matches tylenol
		// acetaminophen
		Pattern pattern = Pattern.compile(".*" + keyword.replace(" ", ".*") + ".*", Pattern.CASE_INSENSITIVE);

		for (Drug drug : getCollection()) {
			Matcher matcherBrandName = pattern.matcher(drug.getBrandName());
			Matcher matcherDescriptor = pattern.matcher(drug.getDescriptor());

			if (matcherBrandName.find() || matcherDescriptor.find()) {
				drugs.add(drug);
			}
		}

		if (drugs.isEmpty()) {
			throw new NoSuchElementException("There is no drug found with keyword: " + keyword);
		}

		return drugs;
	}

	@Override
	protected int hashKeys(Drug element) {
		return Objects.hash(element.getDin());
	}

	@Override
	protected Collection<Drug> loadData() {
		Collection<Drug> drugs = new ArrayList<>();
		List<String[]> allLines = readAllLinesFromDataFile();

		for (String[] line : allLines) {
			Drug drugBuilt = hydrateDrugFromLine(line);

			drugs.add(drugBuilt);
		}

		return drugs;
	}

	private Drug hydrateDrugFromLine(String[] line) {
		String din = line[4];
		String brandName = line[5];
		String descriptor = line[6];

		return new Drug(din, brandName, descriptor);
	}

	private List<String[]> readAllLinesFromDataFile() {
		CSVReader reader = openDataFile();

		try {
			return reader.readAll();
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

	private CSVReader openDataFile() {
		InputStream drugResource = this.getClass().getResourceAsStream(DRUG_FILE);

		return new CSVReader(new InputStreamReader(drugResource));
	}

}
