/**
 * A Cardiologist: diagnoses and treats heart conditions, so it can both
 * prescribe and run diagnostic tests.
 *
 * @author kwu
 * @version 3.0
 */
public class Cardiologist extends HealthProfessional implements Prescribable, DiagnosticCapable {

    private String subSpecialty;
    private String diagnosticDevice;

    /**
     * Creates a cardiologist with every detail specified.
     *
     * @param id               positive staff ID
     * @param name             full name
     * @param profile          biography and expertise
     * @param workingDays      weekdays worked
     * @param shiftStart       first bookable slot
     * @param shiftEnd         last bookable slot
     * @param subSpecialty     cardiac sub-specialty
     * @param diagnosticDevice main diagnostic device
     */
    public Cardiologist(int id, String name, String profile, String[] workingDays,
                        String shiftStart, String shiftEnd,
                        String subSpecialty, String diagnosticDevice) {
        super(id, name, "Cardiology", profile, workingDays, shiftStart, shiftEnd);
        this.subSpecialty = requireText(subSpecialty, "Sub-specialty");
        this.diagnosticDevice = requireText(diagnosticDevice, "Diagnostic device");
    }

    /**
     * Creates a cardiologist on the default weekday shift using an ECG.
     *
     * @param id           positive staff ID
     * @param name         full name
     * @param subSpecialty cardiac sub-specialty
     */
    public Cardiologist(int id, String name, String subSpecialty) {
        this(id, name, "Heart specialist in " + subSpecialty + ".", DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, subSpecialty, "12-lead ECG");
    }

    @Override
    public String getProfessionalType() {
        return "Cardiologist";
    }

    @Override
    public String getClinicalRole() {
        return "Heart disease diagnosis, ECG interpretation and rhythm management";
    }

    /** @return the cardiac sub-specialty */
    public String getSubSpecialty() {
        return subSpecialty;
    }

    /** @param subSpecialty the new cardiac sub-specialty */
    public void setSubSpecialty(String subSpecialty) {
        this.subSpecialty = requireText(subSpecialty, "Sub-specialty");
    }

    /** @return the main diagnostic device */
    public String getDiagnosticDevice() {
        return diagnosticDevice;
    }

    /** @param diagnosticDevice the new diagnostic device */
    public void setDiagnosticDevice(String diagnosticDevice) {
        this.diagnosticDevice = requireText(diagnosticDevice, "Diagnostic device");
    }

    /**
     * Classifies a resting heart rate.
     *
     * @param patientName  the patient's name
     * @param beatsPerMinute the measured heart rate
     * @return the assessment
     */
    public String evaluateHeartRate(String patientName, int beatsPerMinute) {
        String result = "normal rhythm";
        if (beatsPerMinute < 60) {
            result = "bradycardia (slow)";
        } else if (beatsPerMinute > 100) {
            result = "tachycardia (fast)";
        }
        return getName() + ": " + patientName + " at " + beatsPerMinute + " bpm shows " + result + ".";
    }

    @Override
    public String prescribe(String patientName, String medication) {
        return getName() + " (Cardiologist) prescribed " + medication + " to " + patientName + ".";
    }

    @Override
    public String performDiagnostic(String patientName) {
        return getName() + " (Cardiologist) ran a " + diagnosticDevice + " test on " + patientName + ".";
    }

    @Override
    public String toString() {
        return super.toString()
                + Display.field("Sub-Specialty", subSpecialty)
                + Display.field("Diagnostic Device", diagnosticDevice);
    }
}
