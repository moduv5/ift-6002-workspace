package ca.ulaval.ift6002.m2.contexts;

import java.util.Date;

import ca.ulaval.ift6002.m2.domain.date.DateFormatter;
import ca.ulaval.ift6002.m2.domain.drug.Din;
import ca.ulaval.ift6002.m2.domain.drug.Drug;
import ca.ulaval.ift6002.m2.domain.drug.DrugRepository;
import ca.ulaval.ift6002.m2.domain.patient.Patient;
import ca.ulaval.ift6002.m2.domain.patient.PatientFactory;
import ca.ulaval.ift6002.m2.domain.patient.PatientRepository;
import ca.ulaval.ift6002.m2.domain.prescription.Practitioner;
import ca.ulaval.ift6002.m2.domain.prescription.Prescription;
import ca.ulaval.ift6002.m2.domain.prescription.PrescriptionFactory;
import ca.ulaval.ift6002.m2.locator.FactoryLocator;
import ca.ulaval.ift6002.m2.locator.RepositoryLocator;

public class IntegrationPatientRepositoryFiller {

    private static final int PATIENT_NUMBER_WITH_RECENT_PRESCRIPTION = 3;
    private static final int PATIENT_NUMBER_WITH_OLD_PRESCRIPTION = 4;
    private static final int DEAD_PATIENT_NUMBER = 5;
    private static final int PATIENT_NUMBER_WITH_MULTIPLE_PRESCRIPTIONS = 6;

    private static final Din EXISTING_DIN = new Din("02229682");
    private static final Practitioner A_PRACTITIONER = new Practitioner("A random name");

    private static final Date RECENT_DATE = new Date();
    private static final Date OLD_DATE = new Date(0);
    private static final DateFormatter DATE_FORMATTER = new DateFormatter();
    private static final Date DATE1 = DATE_FORMATTER.parse("2009-01-04T12:08:56");
    private static final Date DATE2 = DATE_FORMATTER.parse("2010-02-04T12:08:56");
    private static final Date DATE3 = DATE_FORMATTER.parse("2007-03-04T12:08:56");
    private static final Date DATE4 = DATE_FORMATTER.parse("2012-04-04T12:08:56");

    private static final int RENEWALS = 2;

    private final PatientRepository patientRepository;
    private final DrugRepository drugRepository;

    private final PatientFactory patientFactory;
    private final PrescriptionFactory prescriptionFactory;

    public IntegrationPatientRepositoryFiller() {
        this.patientRepository = RepositoryLocator.getPatientRepository();
        this.drugRepository = RepositoryLocator.getDrugRepository();
        this.patientFactory = FactoryLocator.getPatientFactory();
        this.prescriptionFactory = FactoryLocator.getPrescriptionFactory();
    }

    public void fill() {
        patientRepository.store(patientFactory.create(1));
        patientRepository.store(patientFactory.create(2));

        Drug existingDrug = drugRepository.get(EXISTING_DIN);
        Prescription recentPrescription = prescriptionFactory.create(A_PRACTITIONER, RECENT_DATE, RENEWALS,
                existingDrug);
        Prescription oldPrescription = prescriptionFactory.create(A_PRACTITIONER, OLD_DATE, RENEWALS, existingDrug);

        Patient patientWithRecentPrescription = patientFactory.create(PATIENT_NUMBER_WITH_RECENT_PRESCRIPTION);
        patientWithRecentPrescription.receivesPrescription(recentPrescription);

        Patient patientWithOldPrescription = patientFactory.create(PATIENT_NUMBER_WITH_OLD_PRESCRIPTION);
        patientWithOldPrescription.receivesPrescription(oldPrescription);

        Patient deadPatient = patientFactory.create(DEAD_PATIENT_NUMBER);
        deadPatient.declareDead();

        Patient patientWithMultiplePrescriptions = patientFactory.create(PATIENT_NUMBER_WITH_MULTIPLE_PRESCRIPTIONS);
        Prescription prescription1 = prescriptionFactory.create(A_PRACTITIONER, DATE1, RENEWALS, existingDrug);
        Prescription prescription2 = prescriptionFactory.create(A_PRACTITIONER, DATE2, RENEWALS, existingDrug);
        Prescription prescription3 = prescriptionFactory.create(A_PRACTITIONER, DATE3, RENEWALS, existingDrug);
        Prescription prescription4 = prescriptionFactory.create(A_PRACTITIONER, DATE4, RENEWALS, existingDrug);
        patientWithMultiplePrescriptions.receivesPrescription(prescription1);
        patientWithMultiplePrescriptions.receivesPrescription(prescription2);
        patientWithMultiplePrescriptions.receivesPrescription(prescription3);
        patientWithMultiplePrescriptions.receivesPrescription(prescription4);

        patientRepository.store(patientWithRecentPrescription);
        patientRepository.store(patientWithOldPrescription);
        patientRepository.store(deadPatient);
        patientRepository.store(patientWithMultiplePrescriptions);
    }
}
