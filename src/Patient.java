import java.util.ArrayList;
import java.util.Objects;

/**
 * A patient of the clinic. Kept as its own class (rather than loose strings
 * inside Appointment) so patient details and their checks live in one place.
 *
 * @author kwu
 * @version 3.0
 */
public class Patient {

    public static final int MIN_AGE = 1;
    public static final int MAX_AGE = 120;

    private String patientId;
    private String name;
    private int age;
    private String mobilePhone;
    private String conditionDescription;
    private ArrayList<Appointment> appointments = new ArrayList<>();

    /**
     * Creates a placeholder patient with safe default values.
     */
    public Patient() {
        this("UNKNOWN", "Unknown Patient", MIN_AGE, "00000000", "Not specified");
    }

    /**
     * Creates a patient. Invalid values are rejected at the door.
     *
     * @param patientId            ID card or patient number
     * @param name                 full name
     * @param age                  age in years, 1 to 120
     * @param mobilePhone          mobile number, 8 to 15 digits
     * @param conditionDescription symptoms or reason for the visit
     * @throws IllegalArgumentException if any value is invalid
     */
    public Patient(String patientId, String name, int age,
                   String mobilePhone, String conditionDescription) {
        this.patientId = requireText(patientId, "Patient ID");
        this.name = requireText(name, "Patient name");
        this.age = requireAge(age);
        this.mobilePhone = requirePhone(mobilePhone);
        this.conditionDescription = requireText(conditionDescription, "Condition description");
    }

    /**
     * Checks an age.
     *
     * @param age the age to check
     * @return true if between 1 and 120
     */
    public static boolean isValidAge(int age) {
        return age >= MIN_AGE && age <= MAX_AGE;
    }

    /**
     * Checks a mobile number. Stored as a String, not an int, so leading
     * zeros such as "04..." are kept.
     *
     * @param phone the number to check
     * @return true if it has 8 to 15 digits and nothing else
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().length() < 8 || phone.trim().length() > 15) {
            return false;
        }
        String digits = phone.trim();
        for (int i = 0; i < digits.length(); i++) {
            if (digits.charAt(i) < '0' || digits.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    private static int requireAge(int age) {
        if (!isValidAge(age)) {
            throw new IllegalArgumentException("Age must be " + MIN_AGE + " to " + MAX_AGE + ": " + age);
        }
        return age;
    }

    private static String requirePhone(String phone) {
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("Mobile phone must be 8-15 digits: " + phone);
        }
        return phone.trim();
    }

    /** @return the ID card or patient number */
    public String getPatientId() {
        return patientId;
    }

    /** @param patientId the new ID card or patient number */
    public void setPatientId(String patientId) {
        this.patientId = requireText(patientId, "Patient ID");
    }

    /** @return the full name */
    public String getName() {
        return name;
    }

    /** @param name the new full name */
    public void setName(String name) {
        this.name = requireText(name, "Patient name");
    }

    /** @return the age in years */
    public int getAge() {
        return age;
    }

    /** @param age the new age, 1 to 120 */
    public void setAge(int age) {
        this.age = requireAge(age);
    }

    /** @return the mobile number */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /** @param mobilePhone the new mobile number, 8 to 15 digits */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = requirePhone(mobilePhone);
    }

    /** @return the symptoms or reason for the visit */
    public String getConditionDescription() {
        return conditionDescription;
    }

    /** @param conditionDescription the new symptoms or reason for the visit */
    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = requireText(conditionDescription, "Condition description");
    }

    /** @return a copy of this patient's booked appointments */
    public ArrayList<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    /** @param appointment a newly booked appointment to link to this patient */
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /** @param appointment a cancelled appointment to unlink from this patient */
    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    /** @return true if the patient has at least one booking */
    public boolean hasAppointments() {
        return !appointments.isEmpty();
    }

    /** Prints every field of this patient, one per line. */
    public void displayPatientInfo() {
        System.out.println(Display.LINE);
        System.out.print(this);
        System.out.println(Display.LINE);
    }

    @Override
    public String toString() {
        return Display.field("Patient ID / ID Card", patientId)
                + Display.field("Patient Name", name)
                + Display.field("Age", age)
                + Display.field("Mobile Phone", mobilePhone)
                + Display.field("Condition / Symptoms", conditionDescription)
                + Display.field("Booked Appointments", appointments.size());
    }

    /** Patients are identified by mobile number, which is how the clinic looks them up. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return mobilePhone.equals(((Patient) o).mobilePhone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobilePhone);
    }
}
