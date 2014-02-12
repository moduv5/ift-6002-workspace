package ca.ulaval.ift6002.m2.domain.drug;

import java.util.Collection;

public interface DrugRepository {

    Drug get(Din din);

    Collection<Drug> findByBrandNameOrDescriptor(String keyword);

}
