package ca.ulaval.ift6002.m2.application.assemblers;

import java.util.Date;

import ca.ulaval.ift6002.m2.application.requests.ConsumptionRequest;
import ca.ulaval.ift6002.m2.application.responses.ConsumptionResponse;
import ca.ulaval.ift6002.m2.domain.date.DateFormatter;
import ca.ulaval.ift6002.m2.domain.prescription.Consumption;
import ca.ulaval.ift6002.m2.domain.prescription.Pharmacy;

public class ConsumptionAssembler {

    private final DateFormatter dateFormatter;

    protected ConsumptionAssembler(DateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public ConsumptionAssembler() {
        this.dateFormatter = new DateFormatter();
    }

    public Consumption fromRequest(ConsumptionRequest request) {
        Date formattedDate = dateFormatter.parse(request.date);
        Pharmacy pharmacy = new Pharmacy(request.pharmacy);
        Integer consumptionsCount = request.consumptions;

        return new Consumption(formattedDate, pharmacy, consumptionsCount);
    }

    public ConsumptionResponse toResponse(Consumption consumption) {
        String date = dateFormatter.dateToString(consumption.getDate());
        String pharmacy = consumption.getPharmacy().toString();
        Integer count = Integer.valueOf(consumption.getCount());

        return new ConsumptionResponse(date, pharmacy, count);
    }
}