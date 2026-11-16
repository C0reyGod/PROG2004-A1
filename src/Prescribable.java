/**
 * Capability interface for clinical professionals authorised to prescribe
 * medication. Separated from HealthProfessional so non-prescribing staff
 * (radiologists, nurses, dietitians) do not inherit script privileges.
 */
public interface Prescribable {

    // Default repeats and standard supply limit for a clinic prescription
    int DEFAULT_REPEAT_COUNT = 3;
    int MAX_STANDARD_SUPPLY_DAYS = 30;

    /*
     * Issues a medication prescription for a named patient and returns a
     * formatted summary string.
     */
    String prescribe(String patientName, String medication);

    // Returns true if this professional may prescribe restricted-schedule drugs
    boolean canPrescribeRestrictedMedication();

    // Returns the maximum supply duration in days permitted per prescription
    int getMaxSupplyDays();
}
