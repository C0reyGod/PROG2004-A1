/**
 * A Radiologist: performs and reports on medical imaging. Diagnoses through
 * scans but does not prescribe.
 *
 * @author kwu
 * @version 3.0
 */
public class Radiologist extends HealthProfessional implements DiagnosticCapable {

    private String imagingModality;
    private String imagingSuite;

    /**
     * Creates a radiologist with every detail specified.
     *
     * @param id              positive staff ID
     * @param name            full name
     * @param profile         biography and expertise
     * @param workingDays     weekdays worked
     * @param shiftStart      first bookable slot
     * @param shiftEnd        last bookable slot
     * @param imagingModality main imaging type, e.g. MRI
     * @param imagingSuite    assigned imaging suite
     */
    public Radiologist(int id, String name, String profile, String[] workingDays,
                       String shiftStart, String shiftEnd,
                       String imagingModality, String imagingSuite) {
        super(id, name, "Radiology", profile, workingDays, shiftStart, shiftEnd);
        this.imagingModality = requireText(imagingModality, "Imaging modality");
        this.imagingSuite = requireText(imagingSuite, "Imaging suite");
    }

    /**
     * Creates a radiologist on the default weekday shift in suite R1.
     *
     * @param id              positive staff ID
     * @param name            full name
     * @param imagingModality main imaging type
     */
    public Radiologist(int id, String name, String imagingModality) {
        this(id, name, "Diagnostic radiologist using " + imagingModality + ".", DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, imagingModality, "R1");
    }

    @Override
    public String getProfessionalType() {
        return "Radiologist";
    }

    @Override
    public String getClinicalRole() {
        return "Medical imaging and radiology reporting";
    }

    /** @return the imaging modality */
    public String getImagingModality() {
        return imagingModality;
    }

    /** @param imagingModality the new imaging modality */
    public void setImagingModality(String imagingModality) {
        this.imagingModality = requireText(imagingModality, "Imaging modality");
    }

    /** @return the imaging suite */
    public String getImagingSuite() {
        return imagingSuite;
    }

    /** @param imagingSuite the new imaging suite */
    public void setImagingSuite(String imagingSuite) {
        this.imagingSuite = requireText(imagingSuite, "Imaging suite");
    }

    /**
     * Writes an imaging report for one body region.
     *
     * @param patientName the patient's name
     * @param bodyRegion  the region scanned
     * @return a description of the report
     */
    public String writeImagingReport(String patientName, String bodyRegion) {
        return getName() + " reported the " + imagingModality + " scan of "
                + patientName + "'s " + bodyRegion + ".";
    }

    @Override
    public String performDiagnostic(String patientName) {
        return getName() + " (Radiologist) performed a " + imagingModality + " scan on "
                + patientName + " in suite " + imagingSuite + ".";
    }

    @Override
    public String toString() {
        return super.toString()
                + Display.field("Imaging Modality", imagingModality)
                + Display.field("Imaging Suite", imagingSuite);
    }
}
