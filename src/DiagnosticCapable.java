/**
 * Capability interface for professionals who perform clinical diagnostic
 * procedures such as physical examinations, ECGs, scans or blood draws.
 */
public interface DiagnosticCapable {

    // Target turnaround times in minutes for urgent and routine orders
    int URGENT_TURNAROUND_MINUTES = 30;
    int ROUTINE_TURNAROUND_MINUTES = 120;

    /*
     * Performs this professional's primary diagnostic procedure for a patient
     * and returns a formatted summary of the action.
     */
    String performDiagnostic(String patientName);

    // Returns a summary of the primary diagnostic tool or scanner used
    String getDiagnosticEquipment();

    // Estimates report turnaround time in minutes for urgent or routine priority
    int estimateTurnaroundMinutes(boolean isUrgent);
}
