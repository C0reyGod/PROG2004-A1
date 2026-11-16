/**
 * Radiologist class representing a medical imaging specialist who performs
 * diagnostic scans without prescribing privileges.
 */
public class Radiologist extends HealthProfessional implements DiagnosticCapable {

    public static final String DEFAULT_DEPARTMENT = "Radiology";
    public static final String DEFAULT_PROFILE = "Diagnostic imaging specialist.";
    public static final String DEFAULT_IMAGING_SUITE = "R1";

    private String imagingModality;
    private String imagingSuite;

    /*
     * Creates a Radiologist with full schedule, imaging modality and suite
     * identifier validated.
     */
    public Radiologist(int id, String name, String gender, String profile, String[] workingDays,
                       String shiftStart, String shiftEnd,
                       String imagingModality, String imagingSuite) {
        super(id, name, gender, DEFAULT_DEPARTMENT, profile, workingDays, shiftStart, shiftEnd);
        this.imagingModality = requireText(imagingModality, "Imaging modality");
        this.imagingSuite = requireText(imagingSuite, "Imaging suite");
    }

    // Creates a Radiologist on the default weekday shift in suite R1
    public Radiologist(int id, String name, String gender, String imagingModality) {
        this(id, name, gender, DEFAULT_PROFILE, DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, imagingModality, DEFAULT_IMAGING_SUITE);
    }

    // Returns the concrete specialty title
    @Override
    public String getProfessionalType() {
        return "Radiologist";
    }

    // Returns the radiology clinical duty summary
    @Override
    public String getClinicalRole() {
        return "Medical imaging and radiology reporting";
    }

    // Returns the main imaging modality
    public String getImagingModality() {
        return imagingModality;
    }

    // Updates the imaging modality after blank-text validation
    public void setImagingModality(String imagingModality) {
        this.imagingModality = requireText(imagingModality, "Imaging modality");
    }

    // Returns the assigned imaging suite
    public String getImagingSuite() {
        return imagingSuite;
    }

    // Updates the assigned imaging suite after blank-text validation
    public void setImagingSuite(String imagingSuite) {
        this.imagingSuite = requireText(imagingSuite, "Imaging suite");
    }

    /*
     * Radiologist-specific action: writes a scan report for a specific body
     * region and returns the summary note.
     */
    public String writeImagingReport(String patientName, String bodyRegion) {
        return getName() + " reported the " + imagingModality + " scan of "
                + patientName + "'s " + bodyRegion + ".";
    }

    // Runs an imaging scan in the assigned suite after validating patientName
    @Override
    public String performDiagnostic(String patientName) {
        return getName() + " (Radiologist) performed a " + imagingModality + " scan on "
                + requireText(patientName, "Patient name") + " in suite " + imagingSuite + ".";
    }

    // Returns the scanner description including modality and suite
    @Override
    public String getDiagnosticEquipment() {
        return imagingModality + " scanner (Suite " + imagingSuite + ")";
    }

    // Returns 45 minutes for urgent imaging reports or 120 minutes for routine scans
    @Override
    public int estimateTurnaroundMinutes(boolean isUrgent) {
        return isUrgent ? 45 : ROUTINE_TURNAROUND_MINUTES;
    }

    // Appends modality and suite fields to the base clinician summary
    @Override
    public String toString() {
        return super.toString()
                + Display.field("Imaging Modality", imagingModality)
                + Display.field("Imaging Suite", imagingSuite);
    }
}
