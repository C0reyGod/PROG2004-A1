/**
 * Capability for professionals who perform diagnostic procedures, such as an
 * examination, a scan or a blood test. Shared across unrelated specialties.
 *
 * @author kwu
 * @version 3.0
 */
public interface DiagnosticCapable {

    /**
     * Performs this professional's diagnostic procedure.
     *
     * @param patientName the patient's name
     * @return a description of the procedure
     */
    String performDiagnostic(String patientName);
}
