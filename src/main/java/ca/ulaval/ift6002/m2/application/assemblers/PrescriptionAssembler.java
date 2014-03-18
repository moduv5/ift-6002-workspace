package ca.ulaval.ift6002.m2.application.assemblers;

import java.util.Collection;
import java.util.Date;

import ca.ulaval.ift6002.m2.application.requests.PrescriptionRequest;
import ca.ulaval.ift6002.m2.domain.date.DateFormatter;
import ca.ulaval.ift6002.m2.domain.drug.Din;
import ca.ulaval.ift6002.m2.domain.drug.Drug;
import ca.ulaval.ift6002.m2.domain.drug.DrugRepository;
import ca.ulaval.ift6002.m2.domain.prescription.Practitioner;
import ca.ulaval.ift6002.m2.domain.prescription.Prescription;
import ca.ulaval.ift6002.m2.infrastructure.persistence.locator.RepositoryLocator;

public class PrescriptionAssembler {

    private final DateFormatter dateFormatter;
    private final DrugRepository drugRepository;

    protected PrescriptionAssembler(DateFormatter dateFormatter, DrugRepository drugRepository) {
        this.dateFormatter = dateFormatter;
        this.drugRepository = drugRepository;
    }

    public PrescriptionAssembler() {
        this.dateFormatter = new DateFormatter();
        this.drugRepository = RepositoryLocator.getDrugRepository();
    }

    public PrescriptionRequest toResponse(Prescription prescription) {
        String formattedDate = dateFormatter.dateToString(prescription.getDate());
        String practitioner = prescription.getPractioner().toString();
        Integer renewals = prescription.getRenewals();
        String din = prescription.getDrug().getDin().toString();
        String brandName = prescription.getDrug().getBrandName();

        return new PrescriptionRequest(practitioner, formattedDate, renewals, din, brandName);
    }

    public PrescriptionRequest[] toResponses(Collection<Prescription> prescriptions) {
        PrescriptionRequest[] prescriptionResponses = new PrescriptionRequest[prescriptions.size()];
        int i = 0;

        for (Prescription prescription : prescriptions) {
            prescriptionResponses[i++] = toResponse(prescription);
        }

        return prescriptionResponses;
    }

    public Prescription fromResponse(PrescriptionRequest response) {
        Practitioner practitioner = new Practitioner(response.practitioner);
        Date parsedDate = dateFormatter.parse(response.date);
        Drug drug = getDrugFromRepository(response);

        return new Prescription(practitioner, parsedDate, response.renewals, drug);
    }

    private Drug getDrugFromRepository(PrescriptionRequest response) {
        if (isDinSpecified(response)) {
            Din din = new Din(response.din);

            return drugRepository.get(din);
        } else {
            return drugRepository.get(response.name);
        }
    }

    private boolean isDinSpecified(PrescriptionRequest response) {
        return !response.din.trim().isEmpty();
    }

}