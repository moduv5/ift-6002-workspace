package ca.ulaval.ift6002.m2.domain.instrument;

import java.util.Objects;

public class Instrument {

    private InstrumentStatus status;
    private final Serial serial;
    private final Typecode typecode;

    public Instrument(Typecode typecode, InstrumentStatus status, Serial serialNumber) {
        /*
         * if (typecode == null || typecode.isEmpty()) { throw new
         * IllegalArgumentException("Typecode cannot be empty"); } if
         * (serialNumber == null) { throw new
         * IllegalArgumentException("Serial number cannot be 'null'"); }
         */

        this.status = status;
        this.typecode = typecode;
        this.serial = serialNumber;
    }

    public Instrument(Typecode typecode, InstrumentStatus status) {
        this(typecode, status, new Serial(""));
    }

    public InstrumentStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        // TODO it might be moved to a "service"
        InstrumentStatus statusFound = InstrumentStatus.valueOf(status);

        setStatus(statusFound);
    }

    public void setStatus(InstrumentStatus status) {
        this.status = status;
    }

    public Serial getSerial() {
        return serial;
    }

    public boolean isAnonymous() {
        return serial.isEmpty();
    }

    public Typecode getTypecode() {
        return typecode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, serial, typecode);
    }

    @Override
    public boolean equals(Object obj) {
        Instrument instrument = (Instrument) obj;
        return !isAnonymous() && this.serial.equals(instrument.serial);
    }
}