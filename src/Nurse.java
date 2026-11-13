/**
 * A Nurse: provides triage and collects blood samples. Diagnostic work is
 * shared with doctors, but nurses cannot prescribe.
 *
 * @author kwu
 * @version 3.0
 */
public class Nurse extends HealthProfessional implements DiagnosticCapable {

    private String ward;
    private boolean phlebotomyCertified;

    /**
     * Creates a nurse with every detail specified.
     *
     * @param id                  positive staff ID
     * @param name                full name
     * @param profile             biography and expertise
     * @param workingDays         weekdays worked
     * @param shiftStart          first bookable slot
     * @param shiftEnd            last bookable slot
     * @param ward                assigned ward
     * @param phlebotomyCertified whether certified to take blood
     */
    public Nurse(int id, String name, String profile, String[] workingDays,
                 String shiftStart, String shiftEnd,
                 String ward, boolean phlebotomyCertified) {
        super(id, name, "Nursing", profile, workingDays, shiftStart, shiftEnd);
        this.ward = requireText(ward, "Ward");
        this.phlebotomyCertified = phlebotomyCertified;
    }

    /**
     * Creates a blood-certified nurse on the default weekday shift.
     *
     * @param id   positive staff ID
     * @param name full name
     * @param ward assigned ward
     */
    public Nurse(int id, String name, String ward) {
        this(id, name, "Registered nurse in " + ward + ".", DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, ward, true);
    }

    @Override
    public String getProfessionalType() {
        return "Nurse";
    }

    @Override
    public String getClinicalRole() {
        return "Triage, vital signs, wound care and blood collection";
    }

    /** @return the assigned ward */
    public String getWard() {
        return ward;
    }

    /** @param ward the new ward */
    public void setWard(String ward) {
        this.ward = requireText(ward, "Ward");
    }

    /** @return true if certified to take blood */
    public boolean isPhlebotomyCertified() {
        return phlebotomyCertified;
    }

    /** @param phlebotomyCertified whether certified to take blood */
    public void setPhlebotomyCertified(boolean phlebotomyCertified) {
        this.phlebotomyCertified = phlebotomyCertified;
    }

    /**
     * Takes blood samples, if certified.
     *
     * @param patientName the patient's name
     * @param vials       number of vials
     * @return a description of what happened
     */
    public String takeBloodSample(String patientName, int vials) {
        if (!phlebotomyCertified) {
            return getName() + " is not certified to take blood.";
        }
        return getName() + " took " + vials + " blood vial(s) from " + patientName + ".";
    }

    @Override
    public String performDiagnostic(String patientName) {
        return getName() + " (Nurse) triaged " + patientName + " and checked vital signs.";
    }

    @Override
    public String toString() {
        return super.toString()
                + Display.field("Ward", ward)
                + Display.field("Phlebotomy Certified", phlebotomyCertified ? "Yes" : "No");
    }
}
