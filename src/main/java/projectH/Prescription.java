package projectH;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Prescription {
	private int practitioner;
	private GregorianCalendar date;
	private int renewals;

	public Prescription(int practitioner, GregorianCalendar date, int renewals, String medecineName) throws InvalidPrescriptionException {
		if(renewals < 0)
			throw new InvalidPrescriptionException("The number of renewals must be greater or equals than zero");
		this.practitioner = practitioner;
		this.date = date;
		this.renewals = renewals;
	}

	public int getPractioner() {
		return practitioner;
	}

	public String getDate() {
		String formatedDate = String.format("%d-%02d-%02dT%02d:%02d:%02d",
				date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE),
				date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), date.get(Calendar.SECOND));
		return formatedDate;
	}

	public int getRenewals() {
		return renewals;
	}

}
