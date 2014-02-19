package ca.ulaval.ift6002.m2.infrastructure.persistence.inmemory.repository;

import ca.ulaval.ift6002.m2.domain.instrument.Instrument;
import ca.ulaval.ift6002.m2.domain.instrument.InstrumentRepository;
import ca.ulaval.ift6002.m2.domain.operation.Operation;

// TODO: To be implemented and tested
public class InstrumentInMemoryRepository implements InstrumentRepository {

    private boolean isEmpty = true;

    @Override
    public void store(Operation operation, Instrument instrument) {
        isEmpty = false;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public Instrument get(String serial) {
        return null;
    }

    @Override
    public boolean contains(String serial) {
        return false;
    }

}
