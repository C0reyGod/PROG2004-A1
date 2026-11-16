/**
 * Surgeon class representing a surgical specialist who assesses patients for
 * operations and prescribes post-operative medication without running diagnostics.
 */
public class Surgeon extends HealthProfessional implements Prescribable {

    public static final String DEFAULT_DEPARTMENT = "Surgery";
    public static final String DEFAULT_PROFILE = "Board-certified surgical specialist.";
    public static final String DEFAULT_OPERATING_THEATRE = "OT-A";
    public static final int DEFAULT_POST_OP_SUPPLY_DAYS = 14;

    private String surgicalSpecialty;
    private String operatingTheatre;

    /*
     * Creates a Surgeon with full profile, schedule, surgical specialty and
     * operating theatre details validated.
     */
    public Surgeon(int id, String name, String gender, String profile, String[] workingDays,
                   String shiftStart, String shiftEnd,
                   String surgicalSpecialty, String operatingTheatre) {
        super(id, name, gender, DEFAULT_DEPARTMENT, profile, workingDays, shiftStart, shiftEnd);
        this.surgicalSpecialty = requireText(surgicalSpecialty, "Surgical specialty");
        this.operatingTheatre = requireText(operatingTheatre, "Operating theatre");
    }

    // Creates a Surgeon on the default weekday shift assigned to theatre OT-A
    public Surgeon(int id, String name, String gender, String surgicalSpecialty) {
        this(id, name, gender, DEFAULT_PROFILE, DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, surgicalSpecialty, DEFAULT_OPERATING_THEATRE);
    }

    // Returns the concrete specialty title
    @Override
    public String getProfessionalType() {
        return "Surgeon";
    }

    // Returns the surgical clinical duty summary
    @Override
    public String getClinicalRole() {
        return "Surgical assessment, operations and post-operative care";
    }

    // Returns the area of surgical specialisation
    public String getSurgicalSpecialty() {
        return surgicalSpecialty;
    }

    // Updates the surgical specialty after blank-text validation
    public void setSurgicalSpecialty(String surgicalSpecialty) {
        this.surgicalSpecialty = requireText(surgicalSpecialty, "Surgical specialty");
    }

    // Returns the assigned operating theatre code
    public String getOperatingTheatre() {
        return operatingTheatre;
    }

    // Updates the operating theatre after blank-text validation
    public void setOperatingTheatre(String operatingTheatre) {
        this.operatingTheatre = requireText(operatingTheatre, "Operating theatre");
    }

    /*
     * Surgeon-specific action: assesses readiness and clears a patient for an
     * operation in this surgeon's theatre.
     */
    public String assessForSurgery(String patientName, String procedure) {
        return getName() + " cleared " + patientName + " for " + procedure
                + " in " + operatingTheatre + ".";
    }

    // Prescribes post-operative medication after validating text inputs
    @Override
    public String prescribe(String patientName, String medication) {
        return getName() + " (Surgeon) prescribed post-operative "
                + requireText(medication, "Medication")
                + " to " + requireText(patientName, "Patient name") + ".";
    }

    // Surgeons are authorised to prescribe restricted post-operative analgesics
    @Override
    public boolean canPrescribeRestrictedMedication() {
        return true;
    }

    // Returns the 14-day post-operative supply limit
    @Override
    public int getMaxSupplyDays() {
        return DEFAULT_POST_OP_SUPPLY_DAYS;
    }

    // Appends surgical specialty and theatre fields to the base clinician summary
    @Override
    public String toString() {
        return super.toString()
                + Display.field("Surgical Specialty", surgicalSpecialty)
                + Display.field("Operating Theatre", operatingTheatre);
    }
}
