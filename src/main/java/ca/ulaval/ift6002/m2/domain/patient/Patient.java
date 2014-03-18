package ca.ulaval.ift6002.m2.domain.patient;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.ulaval.ift6002.m2.domain.prescription.Consumption;
import ca.ulaval.ift6002.m2.domain.prescription.Prescription;
import ca.ulaval.ift6002.m2.domain.prescription.PrescriptionNotFoundException;

public class Patient {

    private final int number;
    private final Collection<Prescription> prescriptions;
    private boolean isDead;

    public Patient(int number) {
        this(number, new ArrayList<Prescription>());
    }

    public Patient(int number, Collection<Prescription> prescriptions) {
        this.number = number;
        this.prescriptions = prescriptions;
    }

    public int getNumber() {
        return number;
    }

    public Collection<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public boolean isDead() {
        return isDead;
    }

    public void receivesPrescription(Prescription prescription) {
        if (isDead()) {
            throw new DeadPatientException("A dead patient cannot receives a prescription.");
        }

        prescriptions.add(prescription);
    }

    public void consumesPrescription(int prescriptionNumber, Consumption consumption) {
        Prescription prescription = findPrescription(prescriptionNumber);
        prescription.addConsumption(consumption);
    }

    private Prescription findPrescription(int prescriptionNumber) {
        for (Prescription prescription : prescriptions) {
            if (prescription.hasNumber(prescriptionNumber)) {
                return prescription;
            }
        }

        throw new PrescriptionNotFoundException("No prescription found for number: " + prescriptionNumber);
    }

    public int countPrescriptions() {
        return prescriptions.size();
    }

    public void declareDead() {
        isDead = true;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);

    }

}
