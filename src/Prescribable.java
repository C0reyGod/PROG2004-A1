/**
 * Capability for professionals who may prescribe medication. It sits outside
 * the class hierarchy because only some professionals (doctors) can prescribe.
 *
 * @author kwu
 * @version 3.0
 */
public interface Prescribable {

    /**
     * Writes a prescription.
     *
     * @param patientName the patient's name
     * @param medication  the medication and dosage
     * @return a description of the prescription
     */
    String prescribe(String patientName, String medication);
}
