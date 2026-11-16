/**
 * GeneralPractitioner class representing a primary-care family doctor who
 * implements both Prescribable and DiagnosticCapable contracts.
 */
public class GeneralPractitioner extends HealthProfessional implements Prescribable, DiagnosticCapable {

    public static final String DEFAULT_DEPARTMENT = "General Practice";
    public static final String DEFAULT_PROFILE = "Family medicine and preventive care.";
    public static final boolean DEFAULT_BULK_BILLING = true;
    public static final int DEFAULT_MAX_SUPPLY_DAYS = 30;

    private String consultationRoom;
    private boolean bulkBilling;

    /*
     * Creates a GeneralPractitioner with all shared staff fields plus
     * consultation room and bulk-billing status validated.
     */
    public GeneralPractitioner(int id, String name, String gender, String profile, String[] workingDays,
                               String shiftStart, String shiftEnd,
                               String consultationRoom, boolean bulkBilling) {
        super(id, name, gender, DEFAULT_DEPARTMENT, profile, workingDays, shiftStart, shiftEnd);
        this.consultationRoom = requireText(consultationRoom, "Consultation room");
        this.bulkBilling = bulkBilling;
    }

    // Creates a bulk-billing GP working the default Monday-Friday shift
    public GeneralPractitioner(int id, String name, String gender, String consultationRoom) {
        this(id, name, gender, DEFAULT_PROFILE, DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, consultationRoom, DEFAULT_BULK_BILLING);
    }

    // Returns the concrete specialty title
    @Override
    public String getProfessionalType() {
        return "General Practitioner";
    }

    // Returns the GP clinical responsibility summary
    @Override
    public String getClinicalRole() {
        return "Primary care, chronic disease management and referrals";
    }

    // Returns the consultation room
    public String getConsultationRoom() {
        return consultationRoom;
    }

    // Updates the consultation room after blank-text validation
    public void setConsultationRoom(String consultationRoom) {
        this.consultationRoom = requireText(consultationRoom, "Consultation room");
    }

    // Returns true if consultations are bulk billed
    public boolean isBulkBilling() {
        return bulkBilling;
    }

    // Updates whether consultations are bulk billed
    public void setBulkBilling(boolean bulkBilling) {
        this.bulkBilling = bulkBilling;
    }

    /*
     * GP-specific action: refers a patient to a specialist department and
     * returns a confirmation note.
     */
    public String issueReferral(String patientName, String specialty) {
        return getName() + " referred " + patientName + " to " + specialty + ".";
    }

    // Issues a standard GP prescription after validating text inputs
    @Override
    public String prescribe(String patientName, String medication) {
        return getName() + " (GP) prescribed " + requireText(medication, "Medication")
                + " to " + requireText(patientName, "Patient name") + ".";
    }

    // GPs hold standard prescribing authority only (not specialist-restricted drugs)
    @Override
    public boolean canPrescribeRestrictedMedication() {
        return false;
    }

    // Returns the 30-day standard GP prescription supply limit
    @Override
    public int getMaxSupplyDays() {
        return DEFAULT_MAX_SUPPLY_DAYS;
    }

    // Performs a primary-care physical examination in the consultation room
    @Override
    public String performDiagnostic(String patientName) {
        return getName() + " (GP) examined " + requireText(patientName, "Patient name")
                + " in " + consultationRoom + ".";
    }

    // Returns the examination tools used in the consultation room
    @Override
    public String getDiagnosticEquipment() {
        return "Stethoscope, otoscope and blood pressure monitor";
    }

    // Returns 15 minutes for urgent GP checks or 30 minutes for routine visits
    @Override
    public int estimateTurnaroundMinutes(boolean isUrgent) {
        return isUrgent ? 15 : URGENT_TURNAROUND_MINUTES;
    }

    // Appends GP room and billing fields to the base clinician summary
    @Override
    public String toString() {
        return super.toString()
                + Display.field("Consultation Room", consultationRoom)
                + Display.field("Bulk Billing", bulkBilling ? "Yes" : "No");
    }
}
