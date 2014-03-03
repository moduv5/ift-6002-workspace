package ca.ulaval.ift6002.m2.application.validator.response;

import org.junit.Before;
import org.junit.Test;

import ca.ulaval.ift6002.m2.application.responses.OperationResponse;

public class OperationResponseValidatorTest {

    private static final String DESCRIPTION = "description";
    private static final int SURGEON_NUMBER = 101224;
    private static final String DATE = "0000-00-00T24:01:00";
    private static final String ROOM = "blocB";
    private static final Integer PATIENT_NUMBER = 1234;
    private static final String TYPE = "eye";
    private static final String STATUS = "planned";

    private OperationResponseValidator operationResponseValidator;
    private OperationResponse operationResponse;

    @Before
    public void setUp() {
        operationResponseValidator = new OperationResponseValidator();
    }

    @Test(expected = InvalidResponseException.class)
    public void whenValidatingWithEmptyDescriptionShouldThrowException() throws InvalidResponseException {
        operationResponse = new OperationResponse("", SURGEON_NUMBER, DATE, ROOM, TYPE, STATUS, PATIENT_NUMBER);

        operationResponseValidator.validate(operationResponse);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenValidatingWithNullSurgeonNumberShouldThrowException() throws InvalidResponseException {
        operationResponse = new OperationResponse(DESCRIPTION, null, DATE, ROOM, TYPE, STATUS, PATIENT_NUMBER);

        operationResponseValidator.validate(operationResponse);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenValidatingWithEmptyDateShouldThrowException() throws InvalidResponseException {
        operationResponse = new OperationResponse(DESCRIPTION, SURGEON_NUMBER, "", ROOM, TYPE, STATUS, PATIENT_NUMBER);

        operationResponseValidator.validate(operationResponse);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenValidatingWithEmptyRoomShouldThrowException() throws InvalidResponseException {
        operationResponse = new OperationResponse(DESCRIPTION, SURGEON_NUMBER, DATE, "", TYPE, STATUS, PATIENT_NUMBER);

        operationResponseValidator.validate(operationResponse);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenValidatingWithEmptyTypeShouldThrowException() throws InvalidResponseException {
        operationResponse = new OperationResponse(DESCRIPTION, SURGEON_NUMBER, DATE, ROOM, "", STATUS, PATIENT_NUMBER);

        operationResponseValidator.validate(operationResponse);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenValidatingWithNullPatientNumberShouldThrowException() throws InvalidResponseException {
        operationResponse = new OperationResponse(DESCRIPTION, SURGEON_NUMBER, DATE, ROOM, TYPE, STATUS, null);

        operationResponseValidator.validate(operationResponse);
    }

    @Test
    public void whenValidatingWithValidResponseShouldNotThrowException() throws InvalidResponseException {
        operationResponse = new OperationResponse(DESCRIPTION, SURGEON_NUMBER, DATE, ROOM, TYPE, STATUS, PATIENT_NUMBER);

        operationResponseValidator.validate(operationResponse);
    }
}
