/**
 * A Surgeon: assesses patients for operations and prescribes post-operative
 * medication, but does not run diagnostic tests itself.
 *
 * @author kwu
 * @version 3.0
 */
public class Surgeon extends HealthProfessional implements Prescribable {

    private String surgicalSpecialty;
    private String operatingTheatre;

    /**
     * Creates a surgeon with every detail specified.
     *
     * @param id                positive staff ID
     * @param name              full name
     * @param profile           biography and expertise
     * @param workingDays       weekdays worked
     * @param shiftStart        first bookable slot
     * @param shiftEnd          last bookable slot
     * @param surgicalSpecialty area of surgery
     * @param operatingTheatre  assigned theatre
     */
    public Surgeon(int id, String name, String profile, String[] workingDays,
                   String shiftStart, String shiftEnd,
                   String surgicalSpecialty, String operatingTheatre) {
        super(id, name, "Surgery", profile, workingDays, shiftStart, shiftEnd);
        this.surgicalSpecialty = requireText(surgicalSpecialty, "Surgical specialty");
        this.operatingTheatre = requireText(operatingTheatre, "Operating theatre");
    }

    /**
     * Creates a surgeon on the default weekday shift in theatre OT-A.
     *
     * @param id                positive staff ID
     * @param name              full name
     * @param surgicalSpecialty area of surgery
     */
    public Surgeon(int id, String name, String surgicalSpecialty) {
        this(id, name, "Consultant surgeon in " + surgicalSpecialty + ".", DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, surgicalSpecialty, "OT-A");
    }

    @Override
    public String getProfessionalType() {
        return "Surgeon";
    }

    @Override
    public String getClinicalRole() {
        return "Surgical assessment, operations and post-operative care";
    }

    /** @return the surgical specialty */
    public String getSurgicalSpecialty() {
        return surgicalSpecialty;
    }

    /** @param surgicalSpecialty the new surgical specialty */
    public void setSurgicalSpecialty(String surgicalSpecialty) {
        this.surgicalSpecialty = requireText(surgicalSpecialty, "Surgical specialty");
    }

    /** @return the operating theatre */
    public String getOperatingTheatre() {
        return operatingTheatre;
    }

    /** @param operatingTheatre the new operating theatre */
    public void setOperatingTheatre(String operatingTheatre) {
        this.operatingTheatre = requireText(operatingTheatre, "Operating theatre");
    }

    /**
     * Assesses whether a patient is ready for an operation.
     *
     * @param patientName the patient's name
     * @param procedure   the planned operation
     * @return a description of the assessment
     */
    public String assessForSurgery(String patientName, String procedure) {
        return getName() + " cleared " + patientName + " for " + procedure
                + " in " + operatingTheatre + ".";
    }

    @Override
    public String prescribe(String patientName, String medication) {
        return getName() + " (Surgeon) prescribed post-operative " + medication
                + " to " + patientName + ".";
    }

    @Override
    public String toString() {
        return super.toString()
                + Display.field("Surgical Specialty", surgicalSpecialty)
                + Display.field("Operating Theatre", operatingTheatre);
    }
}
