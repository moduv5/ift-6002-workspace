package ca.ulaval.ift6002.m2.domain.patient;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.ift6002.m2.domain.prescription.Prescription;

@RunWith(MockitoJUnitRunner.class)
public class PatientTest {

    private static final int PATIENT_NUMBER = 12345;

    @Mock
    private Prescription prescription;

    private Patient patient;

    @Before
    public void setUp() {
        patient = new Patient(PATIENT_NUMBER);
    }

    @Test
    public void givenNewPatientWhenCountPrescriptionsShouldBeEmpty() {
        int prescriptionsCount = patient.countPrescriptions();

        assertEquals(0, prescriptionsCount);
    }

    @Test
    public void givenPatientWhenAddPrescriptionShouldHaveCountOfOne() {
        patient.addPrescription(prescription);
        int prescriptionsCount = patient.countPrescriptions();
        assertEquals(1, prescriptionsCount);
    }

    @Test
    public void givenPatientWhenAddMultiplePrescriptionsShouldHaveCountOfTwo() {
        patient.addPrescription(prescription);
        patient.addPrescription(prescription);

        int prescriptionsCount = patient.countPrescriptions();

        assertEquals(2, prescriptionsCount);
    }
}
