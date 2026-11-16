/**
 * Nurse class representing a registered clinical nurse who performs triage and
 * diagnostic checks while excluding prescribing authority.
 */
public class Nurse extends HealthProfessional implements DiagnosticCapable {

    public static final String DEFAULT_DEPARTMENT = "Nursing";
    public static final String DEFAULT_PROFILE = "Registered clinical nurse.";
    public static final boolean DEFAULT_PHLEBOTOMY_CERTIFIED = true;

    private String ward;
    private boolean phlebotomyCertified;

    /*
     * Creates a Nurse with full schedule, assigned ward and phlebotomy
     * certification flag validated.
     */
    public Nurse(int id, String name, String gender, String profile, String[] workingDays,
                 String shiftStart, String shiftEnd,
                 String ward, boolean phlebotomyCertified) {
        super(id, name, gender, DEFAULT_DEPARTMENT, profile, workingDays, shiftStart, shiftEnd);
        this.ward = requireText(ward, "Ward");
        this.phlebotomyCertified = phlebotomyCertified;
    }

    // Creates a phlebotomy-certified nurse on the default weekday shift
    public Nurse(int id, String name, String gender, String ward) {
        this(id, name, gender, DEFAULT_PROFILE, DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, ward, DEFAULT_PHLEBOTOMY_CERTIFIED);
    }

    // Returns the concrete specialty title
    @Override
    public String getProfessionalType() {
        return "Nurse";
    }

    // Returns the nursing clinical duty summary
    @Override
    public String getClinicalRole() {
        return "Triage, vital signs, wound care and blood collection";
    }

    // Returns the assigned hospital or clinic ward
    public String getWard() {
        return ward;
    }

    // Updates the assigned ward after blank-text validation
    public void setWard(String ward) {
        this.ward = requireText(ward, "Ward");
    }

    // Returns true if the nurse is certified to draw blood
    public boolean isPhlebotomyCertified() {
        return phlebotomyCertified;
    }

    // Updates phlebotomy certification status
    public void setPhlebotomyCertified(boolean phlebotomyCertified) {
        this.phlebotomyCertified = phlebotomyCertified;
    }

    /*
     * Nurse-specific action: collects blood vials only when the nurse holds
     * active phlebotomy certification.
     */
    public String takeBloodSample(String patientName, int vials) {
        if (!phlebotomyCertified) {
            return getName() + " is not certified to take blood.";
        }
        return getName() + " took " + vials + " blood vial(s) from " + patientName + ".";
    }

    // Triages the patient and records vital signs
    @Override
    public String performDiagnostic(String patientName) {
        return getName() + " (Nurse) triaged " + requireText(patientName, "Patient name")
                + " and checked vital signs.";
    }

    // Returns nursing diagnostic equipment based on phlebotomy certification
    @Override
    public String getDiagnosticEquipment() {
        return phlebotomyCertified ? "Vital signs monitor and venepuncture kit" : "Vital signs monitor";
    }

    // Returns 20 minutes for urgent nursing triage or 60 minutes for routine checks
    @Override
    public int estimateTurnaroundMinutes(boolean isUrgent) {
        return isUrgent ? 20 : 60;
    }

    // Appends ward and certification fields to the base clinician summary
    @Override
    public String toString() {
        return super.toString()
                + Display.field("Ward", ward)
                + Display.field("Phlebotomy Certified", phlebotomyCertified ? "Yes" : "No");
    }
}
