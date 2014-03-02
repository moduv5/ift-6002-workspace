package ca.ulaval.ift6002.m2.services;

import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.ift6002.m2.application.assemblers.InstrumentResponseAssembler;
import ca.ulaval.ift6002.m2.application.assemblers.OperationResponseAssembler;
import ca.ulaval.ift6002.m2.application.responses.InstrumentResponse;
import ca.ulaval.ift6002.m2.application.responses.OperationResponse;
import ca.ulaval.ift6002.m2.application.validator.response.InvalidResponseException;
import ca.ulaval.ift6002.m2.domain.instrument.Instrument;
import ca.ulaval.ift6002.m2.domain.operation.Operation;
import ca.ulaval.ift6002.m2.domain.operation.OperationRepository;

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceTest {

    private static final String INSTRUMENT_ID = "1313131";
    private static final String OPERATION_ID = "1232";
    private static final InstrumentResponse INSTRUMENT_RESPONSE = new InstrumentResponse("123", "UNUSED", "1312422");
    private static final OperationResponse OPERATION_RESPONSE = new OperationResponse("Cataracte à l'oeil gauche",
            101224, "0000-00-00T24:01:00", "blocB", "oeil", "en cours", 1);

    @InjectMocks
    private OperationService operationService;

    @Mock
    private OperationResponseAssembler operationAssembler;

    @Mock
    private InstrumentResponseAssembler instrumentAssembler;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private Instrument instrument;

    @Mock
    private Operation operation;

    @Test
    public void whenSavingOperationShouldStoreUsingRepository() throws InvalidResponseException {
        willReturn(operation).given(operationAssembler).fromResponse(OPERATION_RESPONSE);
        operationService.saveOperation(OPERATION_RESPONSE);

        verify(operationRepository).store(operation);
    }

    @Test
    public void whenSavingInstrumentOperationShouldAddInstrument() throws InvalidResponseException {
        willReturn(instrument).given(instrumentAssembler).fromResponse(INSTRUMENT_RESPONSE);
        willReturn(operation).given(operationRepository).getOperation(Integer.valueOf(OPERATION_ID));

        operationService.saveInstrument(OPERATION_ID, INSTRUMENT_RESPONSE);

        verify(operation).add(instrument);
    }

    @Test
    public void whenSavingInstrumentServiceShouldStoreOperationUsingRepository() throws InvalidResponseException {
        willReturn(operation).given(operationRepository).getOperation(Integer.valueOf(OPERATION_ID));

        operationService.saveInstrument(OPERATION_ID, INSTRUMENT_RESPONSE);

        verify(operationRepository).store(operation);
    }

    @Test
    public void whenModifyingInstrumentOperationShouldUpdateInstrumentStatus() throws InvalidResponseException {
        willReturn(instrument).given(operationRepository).getInstrument(Integer.valueOf(INSTRUMENT_RESPONSE.serial));
        willReturn(operation).given(operationRepository).getOperation(Integer.valueOf(OPERATION_ID));

        operationService.modifyInstrumentStatus(OPERATION_ID, INSTRUMENT_RESPONSE);

        verify(operation).updateInstrumentStatus(instrument, INSTRUMENT_RESPONSE.status);
    }
    /*
     * @Test public void whenModifyingInstrumentShouldStoreBackUsingRepository()
     * throws InvalidResponseException {
     * willReturn(instrument).given(operationRepository
     * ).getInstrument(Integer.valueOf(INSTRUMENT_RESPONSE.serial));
     * willReturn(operation
     * ).given(operationRepository).getOperation(Integer.valueOf(OPERATION_ID));
     * 
     * operationService.modifyInstrumentStatus(OPERATION_ID,
     * INSTRUMENT_RESPONSE);
     * 
     * verify(operationRepository).storeInstrument(operation, instrument); }
     */
}
