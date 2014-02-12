package ca.ulaval.ift6002.m2.application.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import ca.ulaval.ift6002.m2.application.assemblers.PrescriptionDTOAssembler;
import ca.ulaval.ift6002.m2.application.responses.ExceptionDTO;
import ca.ulaval.ift6002.m2.application.responses.PrescriptionDTO;
import ca.ulaval.ift6002.m2.application.services.PrescriptionService;
import ca.ulaval.ift6002.m2.domain.date.DateFormatter;
import ca.ulaval.ift6002.m2.domain.drug.CSVDrugDataAdapter;
import ca.ulaval.ift6002.m2.domain.drug.DrugRepository;
import ca.ulaval.ift6002.m2.domain.prescription.InvalidPrescriptionException;
import ca.ulaval.ift6002.m2.domain.prescription.PrescriptionRepository;
import ca.ulaval.ift6002.m2.infrastructure.persistence.inmemory.repository.DrugInMemoryRepository;
import ca.ulaval.ift6002.m2.infrastructure.persistence.inmemory.repository.PrescriptionInMemoryRepository;

@Path("/patient/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PrescriptionResource {

    private static final String ERROR_CODE = "PRES001";
    
    //TODO will change when using the service locator
    private final DateFormatter dateFormatter = new DateFormatter();
    private final PrescriptionRepository prescriptionRepository = new PrescriptionInMemoryRepository();
    private final DrugRepository drugRepository = new DrugInMemoryRepository(new CSVDrugDataAdapter());
    private final PrescriptionDTOAssembler assembler = new PrescriptionDTOAssembler(dateFormatter, drugRepository);
    private final PrescriptionService prescriptionService = new PrescriptionService(prescriptionRepository, assembler);

    @POST
    @Path("{patientId}/prescriptions")
    public Response createPrescription(@PathParam("patientId") String patientId, @Context UriInfo uri,
            PrescriptionDTO dto) {
        try {
            prescriptionService.savePrescription(patientId, dto);

            return Response.created(uri.getRequestUri()).build();
        } catch (InvalidPrescriptionException e) {
            ExceptionDTO exception = new ExceptionDTO(ERROR_CODE, e.getMessage());

            return Response.status(Status.BAD_REQUEST).entity(exception).build();
        }
    }

}
