/**
 * A General Practitioner: the first point of care. A GP can both prescribe
 * and examine, so it implements two capability interfaces.
 *
 * @author kwu
 * @version 3.0
 */
public class GeneralPractitioner extends HealthProfessional implements Prescribable, DiagnosticCapable {

    private String consultationRoom;
    private boolean bulkBilling;

    /**
     * Creates a GP with every detail specified.
     *
     * @param id               positive staff ID
     * @param name             full name
     * @param profile          biography and expertise
     * @param workingDays      weekdays worked
     * @param shiftStart       first bookable slot
     * @param shiftEnd         last bookable slot
     * @param consultationRoom assigned room
     * @param bulkBilling      whether consultations are bulk billed
     */
    public GeneralPractitioner(int id, String name, String profile, String[] workingDays,
                               String shiftStart, String shiftEnd,
                               String consultationRoom, boolean bulkBilling) {
        super(id, name, "General Practice", profile, workingDays, shiftStart, shiftEnd);
        this.consultationRoom = requireText(consultationRoom, "Consultation room");
        this.bulkBilling = bulkBilling;
    }

    /**
     * Creates a bulk-billing GP on the default weekday shift.
     *
     * @param id               positive staff ID
     * @param name             full name
     * @param consultationRoom assigned room
     */
    public GeneralPractitioner(int id, String name, String consultationRoom) {
        this(id, name, "Family medicine and preventive care.", DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, consultationRoom, true);
    }

    @Override
    public String getProfessionalType() {
        return "General Practitioner";
    }

    @Override
    public String getClinicalRole() {
        return "Primary care, chronic disease management and referrals";
    }

    /** @return the consultation room */
    public String getConsultationRoom() {
        return consultationRoom;
    }

    /** @param consultationRoom the new consultation room */
    public void setConsultationRoom(String consultationRoom) {
        this.consultationRoom = requireText(consultationRoom, "Consultation room");
    }

    /** @return true if consultations are bulk billed */
    public boolean isBulkBilling() {
        return bulkBilling;
    }

    /** @param bulkBilling whether consultations are bulk billed */
    public void setBulkBilling(boolean bulkBilling) {
        this.bulkBilling = bulkBilling;
    }

    /**
     * Refers a patient to a specialist.
     *
     * @param patientName the patient's name
     * @param specialty   the specialty referred to
     * @return a description of the referral
     */
    public String issueReferral(String patientName, String specialty) {
        return getName() + " referred " + patientName + " to " + specialty + ".";
    }

    @Override
    public String prescribe(String patientName, String medication) {
        return getName() + " (GP) prescribed " + medication + " to " + patientName + ".";
    }

    @Override
    public String performDiagnostic(String patientName) {
        return getName() + " (GP) examined " + patientName + " in " + consultationRoom + ".";
    }

    @Override
    public String toString() {
        return super.toString()
                + Display.field("Consultation Room", consultationRoom)
                + Display.field("Bulk Billing", bulkBilling ? "Yes" : "No");
    }
}
