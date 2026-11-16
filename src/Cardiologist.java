/**
 * Cardiologist class representing a heart specialist who implements both
 * Prescribable and DiagnosticCapable contracts.
 */
public class Cardiologist extends HealthProfessional implements Prescribable, DiagnosticCapable {

    public static final String DEFAULT_DEPARTMENT = "Cardiology";
    public static final String DEFAULT_PROFILE = "Cardiovascular specialist.";
    public static final String DEFAULT_DIAGNOSTIC_DEVICE = "12-lead ECG";
    public static final int DEFAULT_CARDIAC_SUPPLY_DAYS = 90;

    private String subSpecialty;
    private String diagnosticDevice;

    /*
     * Creates a Cardiologist with full schedule, cardiac sub-specialty and
     * primary diagnostic device details validated.
     */
    public Cardiologist(int id, String name, String gender, String profile, String[] workingDays,
                        String shiftStart, String shiftEnd,
                        String subSpecialty, String diagnosticDevice) {
        super(id, name, gender, DEFAULT_DEPARTMENT, profile, workingDays, shiftStart, shiftEnd);
        this.subSpecialty = requireText(subSpecialty, "Sub-specialty");
        this.diagnosticDevice = requireText(diagnosticDevice, "Diagnostic device");
    }

    // Creates a Cardiologist on the default weekday shift equipped with a 12-lead ECG
    public Cardiologist(int id, String name, String gender, String subSpecialty) {
        this(id, name, gender, DEFAULT_PROFILE, DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, subSpecialty, DEFAULT_DIAGNOSTIC_DEVICE);
    }

    // Returns the concrete specialty title
    @Override
    public String getProfessionalType() {
        return "Cardiologist";
    }

    // Returns the cardiology clinical duty summary
    @Override
    public String getClinicalRole() {
        return "Heart disease diagnosis, ECG interpretation and rhythm management";
    }

    // Returns the cardiac sub-specialty
    public String getSubSpecialty() {
        return subSpecialty;
    }

    // Updates the cardiac sub-specialty after blank-text validation
    public void setSubSpecialty(String subSpecialty) {
        this.subSpecialty = requireText(subSpecialty, "Sub-specialty");
    }

    // Returns the main cardiac diagnostic device
    public String getDiagnosticDevice() {
        return diagnosticDevice;
    }

    // Updates the main cardiac diagnostic device after blank-text validation
    public void setDiagnosticDevice(String diagnosticDevice) {
        this.diagnosticDevice = requireText(diagnosticDevice, "Diagnostic device");
    }

    /*
     * Cardiologist-specific action: classifies a patient's resting heart rate
     * as bradycardia, normal rhythm or tachycardia.
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

    // Prescribes cardiac medication after validating text inputs
    @Override
    public String prescribe(String patientName, String medication) {
        return getName() + " (Cardiologist) prescribed " + requireText(medication, "Medication")
                + " to " + requireText(patientName, "Patient name") + ".";
    }

    // Cardiologists may prescribe specialist-restricted cardiac medications
    @Override
    public boolean canPrescribeRestrictedMedication() {
        return true;
    }

    // Returns the 90-day long-term cardiac therapy supply limit
    @Override
    public int getMaxSupplyDays() {
        return DEFAULT_CARDIAC_SUPPLY_DAYS;
    }

    // Performs a cardiac diagnostic test using diagnosticDevice
    @Override
    public String performDiagnostic(String patientName) {
        return getName() + " (Cardiologist) ran a " + diagnosticDevice + " test on "
                + requireText(patientName, "Patient name") + ".";
    }

    // Returns the diagnostic device name
    @Override
    public String getDiagnosticEquipment() {
        return diagnosticDevice;
    }

    // Returns 30 minutes for urgent cardiac tests or 90 minutes for routine tests
    @Override
    public int estimateTurnaroundMinutes(boolean isUrgent) {
        return isUrgent ? URGENT_TURNAROUND_MINUTES : 90;
    }

    // Appends sub-specialty and device fields to the base clinician summary
    @Override
    public String toString() {
        return super.toString()
                + Display.field("Sub-Specialty", subSpecialty)
                + Display.field("Diagnostic Device", diagnosticDevice);
    }
}
